package pl.codecity.module.users.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.codecity.module.users.enumerate.eRole;
import pl.codecity.module.users.service.SystemService;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping(value = "/system")
@RolesAllowed({eRole.ADMIN})
public class SystemController extends CommonController{

    @Autowired
    private SystemService configurationService;

    @ApiOperation(value = "get system configuration")
    @RequestMapping(value = "/configuration", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> getConfiguration(){
        return new ResponseEntity(configurationService.getConfiguration(), HttpStatus.OK);
    }
}
