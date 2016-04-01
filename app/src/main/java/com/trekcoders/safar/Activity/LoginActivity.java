package com.trekcoders.safar.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.fitness.data.Session;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.trekcoders.safar.R;
import com.trekcoders.safar.SafarApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

public class LoginActivity extends AppCompatActivity {

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
    EditText edUser;

    //To check if set password is empty or not
    @NotEmpty(messageId = R.string.validation_pass, order = 2)
    EditText edPassword;

    Button login, register, btnFbLogin;

    TextView passForget;

    Boolean isValid;

    ParseQuery<ParseUser> loginQuery;
    ProgressDialog progressDialog;
    ParseUser updateUser = new ParseUser();

    List<String> permissions = Arrays.asList("public_profile", "email", "user_birthday");

    ParseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUser = (EditText) findViewById(R.id.username_edittext);
        edPassword = (EditText) findViewById(R.id.password_edittext);
        login = (Button) findViewById(R.id.btn_login);
        register = (Button) findViewById(R.id.btn_register);
        btnFbLogin = (Button) findViewById(R.id.btn_fb_login);
        passForget = (TextView) findViewById(R.id.tvPassForget);

        loginQuery = ParseUser.getQuery();

        progressDialog = new ProgressDialog(LoginActivity.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //sets a boolean value true to isValid if all validation conditions are satisfied
                isValid = FormValidator.validate(LoginActivity.this, new SimpleErrorPopupCallback(getApplicationContext(), true));

                //sets a boolean value true to isValid if all validation conditions are satisfied
                if (isValid) {

                    if (SafarApplication.app.checkNetwork.isNetworkAvailable(LoginActivity.this)) {
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Signing up..");
                        progressDialog.show();

                        if (edUser.getText().toString().isEmpty() || edPassword.getText().toString().isEmpty()) {

                            Toast.makeText(LoginActivity.this, "You must fill your login credentials", Toast.LENGTH_SHORT).show();

                        } else {

                            final String uName = edUser.getText().toString();
                            final String uPass = edPassword.getText().toString();

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
                                        Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    } else
                        Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
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

                LayoutInflater inflater = getLayoutInflater();
                final View dialogLayout = inflater.inflate(R.layout.layout_dialogue_forgetpass, null);
                alertDialog.setView(dialogLayout);

                final EditText email = (EditText) dialogLayout.findViewById(R.id.edEmailForgetPass);
                //successText.setText(finalResult);
                final EditText pass = (EditText) dialogLayout.findViewById(R.id.edNewPass);
                //alertDialog.setMessage("Registration information sent for approval");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Set New Password",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String userMail = email.getText().toString();
                                final String userPass = pass.getText().toString();
                                loginQuery.whereEqualTo("email", userMail);
                                loginQuery.findInBackground(new FindCallback<ParseUser>() {
                                    @Override
                                    public void done(List<ParseUser> list, ParseException e) {

                                        if (e == null) {
                                            updateUser.setPassword(userPass);
                                        } else {

                                        }
                                    }
                                });
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                alertDialog.show();
            }
        });

        btnFbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {

                        if (parseUser == null)
                            Log.d("FbLogin: ", "User cancelled FB Login " + parseUser.getUsername() + "--" + parseUser.getEmail());

                        else if (parseUser.isNew()) {
                            Log.d("FbLogin: ", "User signed up and logged in thru FB " + parseUser.getString("email") + "--" + parseUser.getEmail() + "--" + parseUser);
                            getUserDetailsFromFb();
                        } else {
                            Log.d("FbLogin: ", "User logged in thru FB ");
                            getUserDetailsFromFb();
                        }

                    }
                });
            }
        });
    }


    public void getUserDetailsFromFb() {

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name, birth_day");

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", parameters, HttpMethod.GET, new GraphRequest.Callback() {

            @Override
            public void onCompleted(GraphResponse response) {


                try {

                    user = new ParseUser();
                    String email = response.getJSONObject().getString("email");
                    Log.d("FbUserMail",": "+email);

                    //mEmailID.setText(email);
                    String name = response.getJSONObject().getString("name");
                    Log.d("FbUserName",": "+name);
                    //mUsername.setText(name);

                    user.setUsername(name);
                    user.setEmail(email);
                    user.put("mobilenumber","");

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                //progressDialog.dismiss(); //dismiss
                                System.out.println("Sign up successful:");
                                //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            } else {
                                //progressDialog.dismiss();
                                System.out.println("Sign up error:" + e);
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).executeAsync();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
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
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
