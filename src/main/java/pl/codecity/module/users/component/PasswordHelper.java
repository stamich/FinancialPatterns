package pl.codecity.module.users.component;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordHelper {

    public String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public boolean isPasswordValid(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword))
            return true;
        else
            return false;
    }

    /**
     * password policy:
     - At least 8 chars
     - Contains at least one digit
     - Contains at least one lower alpha char and one upper alpha char
     - Contains at least one char within a set of special chars (@#%$^ etc.)
     - Does not contain space, tab, etc.
     ^                 # start-of-string
     (?=.*[0-9])       # a digit must occur at least once
     (?=.*[a-z])       # a lower case letter must occur at least once
     (?=.*[A-Z])       # an upper case letter must occur at least once
     (?=.*[@#$%^&+=])  # a special character must occur at least once
     (?=\S+$)          # no whitespace allowed in the entire string
     .{8,}             # anything, at least eight places though
     $                 # end-of-string
     * @param pass
     * @return
     */
    public static boolean isPasswordStrong(final String pass) {
        String passwd = "aaZZa44@";
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(pass);
        return m.matches();
    }
}
