package pl.codecity.module.users.component;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.swagger.web.ApiResourceController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class RolesAllowedAspect {

    @Autowired
    private PermissionHelper permissionHelper;

    @Around(value = "@within(org.springframework.stereotype.Controller)")
    public Object securityMethodCheck(ProceedingJoinPoint pjp) {
        try {
            Object bean = pjp.getTarget();
            Signature signature = pjp.getSignature();
            Long noOfMethods = Arrays.stream(bean.getClass().getMethods()).filter(m -> m.getName().equals(signature.getName())).count();
            Method method = null;
            if (noOfMethods == 1) {
                method = Arrays.stream(bean.getClass().getMethods()).filter(m -> m.getName().equals(signature.getName())).findFirst().get();
            }
            if (method == null) {
                List<Method> methods = Arrays.stream(bean.getClass().getMethods()).filter(p -> {
                    return p.getName().equals(signature.getName()) && p.getParameterTypes().length == pjp.getArgs().length;
                }).collect(Collectors.toList());
                if (methods.isEmpty() || methods.size() > 1) {
                    throw new Exception(
                            "System error (SYS-CON-004). Wrong method signature, please contact with System Administrator. ("
                                    + signature.getDeclaringTypeName() + " " + bean.getClass() + "#"
                                    + signature.getName() + ")");
                }
                method = methods.get(0);
            }

            if (bean instanceof ApiResourceController) {
                return pjp.proceed();
            }

            if ("error".equals(method.getName())) {
                return pjp.proceed();
            }
            if ("register".equals(method.getName()) || "login".equals(method.getName())) {
                return pjp.proceed();
            }
            RolesAllowed securityRolesAllowed = method.getAnnotation(RolesAllowed.class);
            if (securityRolesAllowed == null) {
                securityRolesAllowed = bean.getClass().getAnnotation(RolesAllowed.class);
            }
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("X-Auth-Token");
            if (StringUtils.isEmpty(token)) {
                throw new Exception("unauthorized user");
            }
            if (permissionHelper.checkIfUserExistsAndHasRightsToPerformAction(token, securityRolesAllowed)) {
                return pjp.proceed();
            } else {
                throw new Exception("you are not allowed to perform this action");
            }

        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
