package com.example.firebaseauthentication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LogIn extends AppCompatActivity {

    EditText email,password;
    LottieAnimationView lottieAnimationView;

    TextView log_in,register;
    FirebaseAuth firebaseAuth ;

    public static String TAG = "RequesError";
    ImageView hight_pass;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    ScrollView lagin_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        lottieAnimationView = findViewById(R.id.animationView);
        log_in = findViewById(R.id.log_in);
        register = findViewById(R.id.register);
        hight_pass = findViewById( R.id.hight_pass);
        lagin_layout = findViewById(R.id.lagin_layout);

        firebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);
        editor = sharedPreferences.edit();



        String loncherdata = sharedPreferences.getString("checkverify","DefultValue");


        if (loncherdata.contains("verified")){
            lagin_layout.setVisibility(View.GONE);
            Toast.makeText(this, "all rady log in", Toast.LENGTH_SHORT).show();
            startActivity( new Intent(LogIn.this,UsureprofileAcitvity.class));

        }else {
            Toast.makeText(this, "Log In now", Toast.LENGTH_SHORT).show();
            lagin_layout.setVisibility(View.VISIBLE);

        }


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this,MainActivity.class));
            }
        });

        //hight password
        hight_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hight_pass.setImageResource(R.drawable.hide);
                }else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hight_pass.setImageResource(R.drawable.view);
                }
            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_email = email.getText().toString();
                String s_pass = password.getText().toString();

                if (TextUtils.isEmpty(s_email)){
                    Toast.makeText(LogIn.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    email.setError("enter your email");
                    email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(s_email).matches()){
                    Toast.makeText(LogIn.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    email.setError("Valied email is Required");
                    email.requestFocus();
                }else if (TextUtils.isEmpty(s_pass)){
                    Toast.makeText(LogIn.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    password.setError("password your Required");
                    password.requestFocus();
                }else {
                    lottieAnimationView.setVisibility(View.VISIBLE);

                    LogINMethod(s_email,s_pass);
                }
            }
        });
    }

    private void LogINMethod(String sEmail, String sPass) {

        firebaseAuth.signInWithEmailAndPassword(sEmail,sPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Get instance of the create user
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    //check is email is vallied then access the profile

                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(LogIn.this, "Log In success Full", Toast.LENGTH_SHORT).show();
                        editor.putString("checkverify","verified");
                        editor.apply();
                        startActivity( new Intent(LogIn.this,UsureprofileAcitvity.class));

                    }else {
                        firebaseUser.sendEmailVerification();
                        firebaseAuth.signOut();
                        ShowAlertDialog();

                    }


                    lottieAnimationView.setVisibility(View.GONE);
                }else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        String errorCode = ((FirebaseAuthInvalidCredentialsException) e).getErrorCode();
                        if (errorCode.equals("ERROR_WRONG_PASSWORD")) {
                            Toast.makeText(LogIn.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                            password.setError("Invalid Password");
                            password.requestFocus();
                        } else {
                            Toast.makeText(LogIn.this, "Invalid credentials, please check and re-enter.", Toast.LENGTH_SHORT).show();
                            email.setError("Invalid credentials, please check and re-enter.");
                            email.requestFocus();
                        }
                    }catch (FirebaseAuthInvalidUserException e) {
                        Toast.makeText(LogIn.this, "User does not exist. Register again.", Toast.LENGTH_SHORT).show();
                        email.setError("User does not exist. Register again.");
                        email.requestFocus();
                    }
                    catch (Exception e) {
                        Log.e(TAG, e.getMessage()); // Use Log.e for errors
                        Toast.makeText(LogIn.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                    lottieAnimationView.setVisibility(View.GONE);

                }
            }
        });
    }

    private void ShowAlertDialog() {

        new AlertDialog.Builder(LogIn.this)
                .setTitle("Email Not Verified")
                .setMessage("please verify your Email address. You can not login without verify")
                .setIcon(R.drawable.verify)
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .show();
    }


}