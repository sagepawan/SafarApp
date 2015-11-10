package com.trekcoders.safar.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.trekcoders.safar.R;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText user, password;
    Button login, register;

    TextView passForget;

    ParseQuery<ParseUser> loginQuery;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText)findViewById(R.id.username_edittext);
        password = (EditText)findViewById(R.id.password_edittext);
        login = (Button)findViewById(R.id.btn_login);
        register = (Button)findViewById(R.id.btn_register);
        passForget = (TextView)findViewById(R.id.tvPassForget);

        loginQuery = ParseUser.getQuery();

        progressDialog = new ProgressDialog(LoginActivity.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Signing up..");
                progressDialog.show();

                if(user.getText().toString().isEmpty() || password.getText().toString().isEmpty()){

                    Toast.makeText(LoginActivity.this,"You must fill your login credentials",Toast.LENGTH_SHORT).show();

                }
                else{

                    final String uName = user.getText().toString();
                    final String uPass = password.getText().toString();

                    ParseUser.logInInBackground(uName, uPass, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                progressDialog.dismiss();
                                System.out.println("login");
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                progressDialog.dismiss();
                                System.out.println("login eee:" + e);
                                Toast.makeText(LoginActivity.this,"Incorrect Username or Password",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




                }



            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        passForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("");
                //alertDialog.setMessage("Registration information sent for approval");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ask for new password",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                LayoutInflater inflater = getLayoutInflater();
                final View dialogLayout = inflater.inflate(R.layout.layout_dialogue_forgetpass, null);
                alertDialog.setView(dialogLayout);

                EditText email = (EditText)dialogLayout.findViewById(R.id.edEmailForgetPass);
                //successText.setText(finalResult);


                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
