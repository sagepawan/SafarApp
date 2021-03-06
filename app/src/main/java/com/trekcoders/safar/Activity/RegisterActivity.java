package com.trekcoders.safar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.trekcoders.safar.R;
import com.trekcoders.safar.SafarApplication;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

public class RegisterActivity extends AppCompatActivity {

    //CODE PART FOR VALIDATION HANDLED BY UDAY (USE OF KOMENSKY VALIDATION LIBRARY)

    //Defining Regular expression for email validation
    final String EMAIL = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+";

    //Calling Regular expression validation, order 1 means this will be checked first
    @RegExp(value = EMAIL, messageId = R.string.validation_email, order = 1)
    EditText email;

    //To check if set password is empty or not
    @NotEmpty(messageId = R.string.validation_pass, order = 2)
    EditText pass;

    //To check if confirm password if empty or not
    @NotEmpty(messageId = R.string.validation_confirm_pass, order = 3)
    EditText confirmPass;

    //to check if mobile number is empty or not
    @NotEmpty(messageId = R.string.validation_mobile, order = 4)
    EditText mobile;


    Button submit, exit;

    ParseUser user;

    Boolean isValid;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        submit = (Button)findViewById(R.id.btn_submit);
        exit = (Button)findViewById(R.id.btn_exit);
        email = (EditText)findViewById(R.id.register_usermail);
        pass = (EditText)findViewById(R.id.register_pass);
        confirmPass = (EditText)findViewById(R.id.register_confirm_pass);
        mobile = (EditText)findViewById(R.id.register_user_contact);

        user = new ParseUser();
        progressDialog = new ProgressDialog(RegisterActivity.this);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //sets a boolean value true to isValid if all validation conditions are satisfied
                isValid = FormValidator.validate(RegisterActivity.this, new SimpleErrorPopupCallback(getApplicationContext(),true));

                //if validation true, registration works, else it shows error msg in each textbox
                if (isValid) {

                    if(SafarApplication.app.checkNetwork.isNetworkAvailable(RegisterActivity.this)) {
                        if (pass.getText().toString().equals(confirmPass.getText().toString())) {
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Signing up..");
                            progressDialog.show();
                            String eMailUser = email.getText().toString();
                            String passUser = pass.getText().toString();
                            String mobileUser = mobile.getText().toString();
                            user.setUsername(eMailUser);
                            user.setPassword(passUser);
                            user.setEmail(eMailUser);
                            user.put("mobilenumber", mobileUser);

                            user.signUpInBackground(new SignUpCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        progressDialog.dismiss(); //dismiss
                                        System.out.println("Sign up successful:");
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    } else {
                                        progressDialog.dismiss();
                                        System.out.println("Sign up error:" + e);
                                        // Sign up didn't succeed. Look at the ParseException
                                        // to figure out what went wrong
                                    }
                                }
                            });


                            Log.d("Register_values", ": " + eMailUser + ", " + passUser + ", " + mobileUser);

                            //user.saveInBackground();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Password and confirm password do not match", Toast.LENGTH_SHORT).show();
                        }

                    }else
                        Toast.makeText(RegisterActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
