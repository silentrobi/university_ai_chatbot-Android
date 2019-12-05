package controller.helper;

import android.widget.EditText;

public interface InputValidation {

    public boolean validateUserName(EditText userName);
    public boolean validatePassword(EditText password);
    public boolean validateEmail (EditText email);
}
