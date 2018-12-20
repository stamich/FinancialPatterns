package pl.codecity.module.users.service;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.codecity.module.users.component.PasswordHelper;
import pl.codecity.module.users.component.TokenHelper;
import pl.codecity.module.users.dto.ErrorDTO;
import pl.codecity.module.users.dto.PasswordDTO;
import pl.codecity.module.users.dto.UserDTO;
import pl.codecity.module.users.dto.UserListDTO;
import pl.codecity.module.users.model.User;
import pl.codecity.module.users.repository.PrivilegeRepository;
import pl.codecity.module.users.repository.RoleRepository;
import pl.codecity.module.users.repository.UserRepository;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static pl.codecity.module.users.util.Constants.*;

@Service
public class UserService {

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHelper passwordHelper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = false)
    public ResponseEntity registerNewUserAccount(UserDTO userDTO) {

        if (StringUtils.isEmpty(userDTO.getEmail())) {
            return new ResponseEntity(new ErrorDTO(ERROR_EMAIL_REQURIED), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return new ResponseEntity(new ErrorDTO(ERROR_EMAIL_ALREADY_EXISTS), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(userDTO.getPassword())) {
            return new ResponseEntity(new ErrorDTO(ERROR_PASSWORD_REQURIED), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(userDTO.getFirstName())) {
            return new ResponseEntity(new ErrorDTO(ERROR_FIRST_NAME_REQURIED), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(userDTO.getLastName())) {
            return new ResponseEntity(new ErrorDTO(ERROR_LAST_NAME_REQURIED), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(passwordHelper.hashPassword(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        userRepository.save(user);
        userDTO.setPassword(null);
        return new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @Transactional(readOnly = false)
    public ResponseEntity login(UserDTO userDTO) {
        if (StringUtils.isEmpty(userDTO.getEmail())) {
            return new ResponseEntity(new ErrorDTO(ERROR_EMAIL_REQURIED), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(userDTO.getPassword())) {
            return new ResponseEntity(new ErrorDTO(ERROR_PASSWORD_REQURIED), HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user == null) {
            return new ResponseEntity(new ErrorDTO(ERROR_INCORRECT_EMAIL_OR_PASSWORD), HttpStatus.UNAUTHORIZED);
        }
        if (!passwordHelper.isPasswordValid(userDTO.getPassword(), user.getPassword())) {
            return new ResponseEntity(new ErrorDTO(ERROR_INCORRECT_EMAIL_OR_PASSWORD), HttpStatus.UNAUTHORIZED);
        }
        String generatedToken = tokenHelper.generateTokenBasedOnUserID(user.getId());
        user.setToken(generatedToken);
        userRepository.updateToken(generatedToken, user.getId());
        userDTO.setToken(generatedToken);

        userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setPassword(null);
        return new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @Transactional(readOnly = false)
    public ResponseEntity changePassword(PasswordDTO passwordDTO) {
        if (StringUtils.isEmpty(passwordDTO.getCurrentPassword())) {
            return new ResponseEntity(new ErrorDTO(ERROR_EMAIL_REQURIED), HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(passwordDTO.getEmail());
        if (user == null) {
            return new ResponseEntity(new ErrorDTO(ERROR_INCORRECT_EMAIL_OR_PASSWORD), HttpStatus.UNAUTHORIZED);
        }
        if (!passwordHelper.isPasswordValid(passwordDTO.getCurrentPassword(), user.getPassword())) {
            return new ResponseEntity(new ErrorDTO(ERROR_INCORRECT_EMAIL_OR_PASSWORD), HttpStatus.UNAUTHORIZED);
        }

        if (StringUtils.isEmpty(passwordDTO.getCurrentPassword())) {
            return new ResponseEntity(new ErrorDTO(ERROR_PASSWORD_REQURIED), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(passwordDTO.getNewPassword())) {
            return new ResponseEntity(new ErrorDTO(ERROR_PASSWORD_REQURIED), HttpStatus.BAD_REQUEST);
        }
        if (!passwordHelper.isPasswordStrong(passwordDTO.getNewPassword())) {
            return new ResponseEntity(new ErrorDTO(ERROR_PASSWORD_POLICY), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordHelper.hashPassword(passwordDTO.getNewPassword()));
        userRepository.save(user);
        passwordDTO.setCurrentPassword(null);
        passwordDTO.setNewPassword(null);
        passwordDTO.setEmail(null);
        return new ResponseEntity(passwordDTO, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity list() {
//        if (!StringUtils.isEmpty(userDTO.getEmail())){
//            List<User> users = userRepository.findAll(sortByEmail());
//            return new ResponseEntity<>(users, HttpStatus.OK);
//        }

        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOCollection = new LinkedList<>();
        UserListDTO userListDTO = new UserListDTO();

        //List<UserDTO> userDTOCollection = users.stream().map(user -> modelMapper.map(user., UserDTO.class)).collect(Collectors.<UserDTO> toList());

        for(User user : users) {
            //user.setRoles(null);
            UserDTO userDTOItem = modelMapper.map(user, UserDTO.class);
            userDTOCollection.add(userDTOItem);
        }
        userListDTO.setUsers(userDTOCollection);
        ResponseEntity<?> response = new ResponseEntity<>(userListDTO, HttpStatus.OK);
        return response;
    }

    @Transactional(readOnly = true)
    public ResponseEntity forgotPassword(UserDTO userDTO){
        if(StringUtils.isEmpty(userDTO.getEmail())){
            return new ResponseEntity(new ErrorDTO(ERROR_EMAIL_REQURIED), HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        userRepository.save(user);
        return new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @Transactional(readOnly = false)
    public ResponseEntity resetPassword(UserDTO userDTO){
        if (StringUtils.isEmpty(userDTO.getEmail())) {
            return new ResponseEntity(new ErrorDTO(ERROR_EMAIL_REQURIED), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(userDTO.getPassword())) {
            return new ResponseEntity(new ErrorDTO(ERROR_PASSWORD_REQURIED), HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        userRepository.save(user);
        userDTO = modelMapper.map(user, UserDTO.class);
        return new ResponseEntity(userDTO, HttpStatus.OK);
    }
}
