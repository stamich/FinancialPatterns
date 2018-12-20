package pl.codecity.module.users.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.codecity.module.users.repository.UserRepository;
import pl.codecity.module.users.util.Constants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    /**
     * Filtering function
     * @param servletRequest servlet request
     * @param servletResponse servlet response
     * @param chain filter chain
     * @throws IOException when I/O error occured
     * @throws ServletException when servlet error occured
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String authToken = httpServletRequest.getHeader(Constants.HEADER_OAUTH_TOKEN_NAME);
/*
            if (authToken != null) {
                User user = userRepository.findByToken(authToken);
                if (user != null) {
                    user.setTokenUpdateTime(LocalDateTime.now());
                    userRepository.save(user);
                }
            }
*/
        }
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
