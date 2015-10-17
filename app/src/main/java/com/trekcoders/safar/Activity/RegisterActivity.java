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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.trekcoders.safar.R;

public class RegisterActivity extends AppCompatActivity {

    Button submit;
    EditText email, pass, mobile;
    ParseUser user;

    //git commit change

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        submit = (Button)findViewById(R.id.btn_submit);
        email = (EditText)findViewById(R.id.register_usermail);
        pass = (EditText)findViewById(R.id.register_pass);
        mobile = (EditText)findViewById(R.id.register_user_contact);

        user = new ParseUser();
        progressDialog = new ProgressDialog(RegisterActivity.this);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Signing up..");
                progressDialog.show();
                String eMailUser = email.getText().toString();
                String passUser = pass.getText().toString();
                String mobileUser = mobile.getText().toString();
                user.setUsername(eMailUser);
                user.setPassword(passUser);
                user.setEmail(eMailUser);
                user.put("mobilenumber", Integer.parseInt(mobileUser));

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            progressDialog.dismiss(); //dismiss
                            System.out.println("Sign up successful:");
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            progressDialog.dismiss();
                            System.out.println("Sign up error:"+e);
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                        }
                    }
                });


                Log.d("Register_values", ": " + eMailUser + ", " + passUser + ", " + mobileUser);

                //user.saveInBackground();

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
