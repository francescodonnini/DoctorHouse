package ispw.uniroma2.doctorhouse.auth.beans;

import ispw.uniroma2.doctorhouse.auth.exceptions.EmailNotValid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Email class is used to check if an email is correct - it does not check whether the email exists.
 */
public class EmailBean {
    // The Email class uses a regular expression taken from https://learn.microsoft.com/en-us/dotnet/standard/base-types/how-to-verify-that-strings-are-in-valid-email-format?redirectedfrom=MSDN to check if a string passed by Email#setEmail is a valid email
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws EmailNotValid {
        email = email.strip();
        Matcher m = EMAIL_PATTERN.matcher(email);
        if (m.find(0) && m.hitEnd()) {
            this.email = email;
        } else {
            throw new EmailNotValid();
        }
    }
}
