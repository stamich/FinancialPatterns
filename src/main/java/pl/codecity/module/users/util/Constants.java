package pl.codecity.module.users.util;

public class Constants {

    public static final String HEADER_OAUTH_TOKEN_NAME = "X-Auth-Token";
    public static final String ADMIN_EMAIL = "rodo.admin@infomex.pl";

    /**
     * JSON RESPONSE MESSAGE
     */
    /**
     * **************** ERROR MESSAGES
     */
    public static final String ERROR_EMAIL_REQURIED = "email is required";
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String ERROR_PASSWORD_REQURIED = "Password is required";
    public static final String ERROR_PASSWORD_POLICY = "The password you entered doesn't meet the minimum security requirements";
    public static final String ERROR_FIRST_NAME_REQURIED = "first name is required";
    public static final String ERROR_LAST_NAME_REQURIED = "last name is required";
    public static final String ERROR_PASSWORD_IS_NOT_VALID = "password is not valid";
    public static final String ERROR_INCORRECT_EMAIL_OR_PASSWORD = "incorrect email or password";
    public static final String ERROR_THERE_IS_NO_USER_YET = "there is no user yet";

    /**
     * JSON STATUS
     */
    public static final String STATUS_FAILED = "Failed";
}
