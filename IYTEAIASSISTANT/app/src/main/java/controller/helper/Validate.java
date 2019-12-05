package controller.helper;

import android.util.Patterns;
import android.widget.EditText;

import controller.helper.InputValidation;

/*
    validate userName input
     */
public class Validate implements InputValidation {

    @Override
    public boolean validateUserName(EditText userName) {
        String usernameInput = userName.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            userName.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            userName.setError("Username too long");
            return false;
        } else {
            userName.setError(null);
            return true;
        }
    }
    /*
    validate password input
     */
    @Override
    public boolean validatePassword(EditText password) {
        String passwordInput = password.getText().toString().trim(); // convert txt to string

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (passwordInput.length() < 5) {
            password.setError("Password too short");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }
   /*
   validate email input
    */
    @Override
    public boolean validateEmail(EditText email) {
        String emailInput = email.getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        }
        else {
            email.setError(null);
            return true;
        }
    }
}
