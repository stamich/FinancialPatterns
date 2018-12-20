package pl.codecity.module.users.controller;

import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.codecity.module.users.dto.PasswordDTO;
import pl.codecity.module.users.dto.UserDTO;
import pl.codecity.module.users.enumerate.eRole;
import pl.codecity.module.users.model.User;
import pl.codecity.module.users.service.UserService;;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping(value = "/user")
@RolesAllowed({eRole.ADMIN})
public class UserController extends CommonController{

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Registering users")
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        return userService.registerNewUserAccount(userDTO);
    }

    @ApiOperation(value = "Change user password")
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> changePassword(@RequestBody PasswordDTO passwordDTO) {
        return userService.changePassword(passwordDTO);
    }

    @ApiOperation(value = "Logging on and off")
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        return userService.login(userDTO);
    }

    @ApiOperation(value = "Listing of all GDPR users")
    @RequestMapping(value = "/list", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<?> list(){
        //  User user = modelMapper.map(userDTO, User.class);
        return userService.list();
    }

    @ApiOperation(value = "Sending forgotten password")
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> forgotPassword(@RequestBody UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);
        return userService.forgotPassword(userDTO);
    }

    @ApiOperation(value = "Resetting password")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);
        return userService.resetPassword(userDTO);
    }
}
