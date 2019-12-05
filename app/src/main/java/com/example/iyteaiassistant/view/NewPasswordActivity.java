package com.example.iyteaiassistant.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.iyteaiassistant.R;
import controller.database.DatabaseHelper;
import controller.helper.Session;
import controller.helper.Validate;

public class NewPasswordActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    Session session;
    private Validate validate;
    private EditText password, confirm_password;
    private Button create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        session = new Session("USER", this);
        validate = new Validate();
        databaseHelper = new DatabaseHelper(this);

        create =(Button) findViewById(R.id.v_create);
        password= (EditText) findViewById(R.id.v_password);
        confirm_password = (EditText) findViewById(R.id.v_confirm_password);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!session.getUserEmail().equals("") // if user navigate from chat menu option
                            && validate.validatePassword(password)
                            && validate.validatePassword(confirm_password)
                            && isEqual(password,confirm_password)){

                    databaseHelper.updatePassword(session.getUserEmail().trim(), password.getText().toString().trim());
                    Intent i = new Intent(NewPasswordActivity.this, ChatActivity.class); // goes to ChatActivity page
                    startActivity(i);
                    finish(); //end of the activity
                }
                else if (!session.getPasswordVerifyEmail().equals("") // when no user logged in to the system yet.
                        && validate.validatePassword(password)
                        && validate.validatePassword(confirm_password)
                        && isEqual(password,confirm_password)){

                    databaseHelper.updatePassword(session.getPasswordVerifyEmail().trim(), password.getText().toString().trim());
                    session.setPasswordVerifyEmail("");
                    Intent i = new Intent(NewPasswordActivity.this, LoginActivity.class); // goes to LoginActivity page
                    startActivity(i);
                    finish();  //end of the activity

                }
            }

        });

    }


    private boolean isEqual(EditText et1, EditText et2){

        String firstET = et1.getText().toString().trim();
        String secondET= et2.getText().toString().trim();
        if (!firstET.equals(secondET)){
            et2.setError("Password mismatch");
            return false;
        }
        return true;
    }


}
