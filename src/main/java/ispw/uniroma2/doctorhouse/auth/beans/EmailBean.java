package ispw.uniroma2.doctorhouse.auth.beans;

import ispw.uniroma2.doctorhouse.auth.exceptions.EmailNotValid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Email class is used to check if an email is correct - it does not check whether the email exists.
 */
public class EmailBean {
    // The Email class uses a regular expression taken from https://emailregex.com/ to check if a string passed by Email#setEmail is a valid email
    private final Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");

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
