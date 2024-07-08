package com.example.firebaseauthentication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Month;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    EditText email,password,address,name,phone,con_password,age,birthday,yoursalf;
    RadioGroup rediogroopRegisterGendr;
    RadioButton redioButtonRegisterGenderSelected,radio_male,radio_female;

    TextView log_in;

    FirebaseAuth firebaseAuth;

    LottieAnimationView animationView;
    LinearLayout checkbox_lay;

    TextView one_number,lowercase,count_number,one_simble,uppercase,latter;

    int num,lowe,cou,sim,upe,con_pass= 0;

    public static final String TAG = "RegisterActivity";

    ImageView hight_con_pass,hight_pass,calender;

    DatePickerDialog picker;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        age = findViewById(R.id.age);
        birthday = findViewById(R.id.birthday);
        yoursalf = findViewById(R.id.yoursalf);
        rediogroopRegisterGendr = findViewById(R.id.gender_groop);
        rediogroopRegisterGendr.clearCheck();
        radio_male = findViewById(R.id.radio_male);
        radio_female = findViewById(R.id.radio_female);
        hight_con_pass = findViewById(R.id.hight_con_pass);
        hight_pass = findViewById(R.id.hight_pass);
        calender = findViewById(R.id.calender);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        log_in = findViewById(R.id.log_in);
        animationView = findViewById(R.id.animationView);
        checkbox_lay = findViewById(R.id.checkbox_lay);
        one_number = findViewById(R.id.one_number);
        lowercase = findViewById(R.id.lowercase);
        count_number = findViewById(R.id.count_number);
        one_simble = findViewById(R.id.one_simble);
        uppercase = findViewById(R.id.uppercase);
        latter = findViewById(R.id.latter);
        address = findViewById(R.id.address);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        con_password = findViewById(R.id.con_password);




        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()==0){
                    email.setBackgroundResource(R.drawable.error);

                }else {
                    email.setBackgroundResource(R.drawable.bottom_shade);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //check strong password
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // This method is called to notify you that somewhere within
                // s, the text has been changed.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // This method is called to notify you that somewhere within
                // s, the text has been changed.

                checkbox_lay.setVisibility(View.VISIBLE);
                password.setBackgroundResource(R.drawable.bottom_shade);

                if (charSequence.length()==0){
                    password.setBackgroundResource(R.drawable.error);

                }else {
                    password.setBackgroundResource(R.drawable.bottom_shade);
                }

                String PASS = charSequence.toString();
                if (containsAny(PASS, "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")) {
                    upe=1;
                    uppercase.setTextColor(Color.parseColor("#1dd1a1"));
                }else {
                    upe=0;
                    uppercase.setTextColor(Color.BLACK);
                }

                if (containsAny(PASS, "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z")) {
                    lowe=1;
                    lowercase.setTextColor(Color.parseColor("#1dd1a1"));
                    latter.setTextColor(Color.parseColor("#1dd1a1"));

                }else {
                    lowe=0;
                    lowercase.setTextColor(Color.BLACK);
                    latter.setTextColor(Color.parseColor("#000000"));

                }

                if (containsAny(PASS, "1","2","3","4","5","6","7","8","9","0")) {
                    num=1;
                    one_number.setTextColor(Color.parseColor("#1dd1a1"));
                }else {
                    num=0;
                    one_number.setTextColor(Color.BLACK);
                }

                if (containsAny(PASS, "!","#","$","%","&","'","(",")","*","+",",","-",".","/",":",";","<","=",">","?","@","[","]","^","_","`","{","|","}","~")) {
                    sim=1;
                    one_simble.setTextColor(Color.parseColor("#1dd1a1"));
                }else {
                    sim=0;
                    one_simble.setTextColor(Color.BLACK);
                }

                if (PASS.length()>=8&&PASS.length()<=50){
                    cou=1;
                    count_number.setTextColor(Color.parseColor("#1dd1a1"));
                }else {
                    cou=0;
                    count_number.setTextColor(Color.BLACK);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This method is called to notify you that somewhere within
                // s, the text has been changed.
            }
        });


        con_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass = password.getText().toString();
                String con_p = password.getText().toString();
                if (con_p.contains(pass)){
                    con_password.setBackgroundResource(R.drawable.bottom_shade);
                    con_pass=1;

                }else {
                    con_password.setBackgroundResource(R.drawable.error);
                    con_pass=0;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

        hight_con_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (con_password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    con_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hight_con_pass.setImageResource(R.drawable.hide);
                }else {
                    con_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hight_con_pass.setImageResource(R.drawable.view);
                }
            }
        });




        radio_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rediogroopRegisterGendr.setBackgroundResource(R.drawable.bottom_shade);
            }
        });

        radio_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rediogroopRegisterGendr.setBackgroundResource(R.drawable.bottom_shade);
            }
        });

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int date = calendar.get(calendar.DAY_OF_MONTH);
                int manth = calendar.get(calendar.MONTH);
                int year = calendar.get(calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String s_day = String.valueOf(dayOfMonth);
                                int i = month+1;
                                String s_manth = String.valueOf(i);
                                String s_year = String.valueOf(year);

                                if (s_day.length()==1){
                                    s_day="0"+s_day;
                                }
                                if (s_manth.length()==1){
                                    s_manth = "0"+s_manth;
                                }

                                birthday.setText(s_day+"/"+s_manth+"/"+year);



                            }
                        },

                        year,
                        manth,
                        date
                );
                datePickerDialog.show();
            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int date = calendar.get(calendar.DAY_OF_MONTH);
                int manth = calendar.get(calendar.MONTH);
                int year = calendar.get(calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String s_day = String.valueOf(dayOfMonth);
                                int i = month+1;
                                String s_manth = String.valueOf(i);
                                String s_year = String.valueOf(year);

                                if (s_day.length()==1){
                                    s_day="0"+s_day;
                                }
                                if (s_manth.length()==1){
                                    s_manth = "0"+s_manth;
                                }

                                birthday.setText(s_day+"/"+s_manth+"/"+year);



                            }
                        },

                        year,
                        manth,
                        date
                );
                datePickerDialog.show();

            }
        });


        //Register your account
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    int num,lowe,cou,sim,upe= 0;

                int selectGenderId = rediogroopRegisterGendr.getCheckedRadioButtonId();
                redioButtonRegisterGenderSelected = findViewById(selectGenderId);

                String s_email = email.getText().toString();
                String s_password = password.getText().toString();
                String conpasswoed = con_password.getText().toString();
                String s_name = name.getText().toString();
                String s_address  = address.getText().toString();
                String s_phone = phone.getText().toString();
                String s_age = age.getText().toString();
                int gander_id = rediogroopRegisterGendr.getCheckedRadioButtonId();
                String s_gender ;
                String s_birth = birthday.getText().toString();
                String s_about = yoursalf.getText().toString();

                if (TextUtils.isEmpty(s_email)){
                    Toast.makeText(MainActivity.this, "Enter Your Email Address", Toast.LENGTH_SHORT).show();
                    email.setError("Enter Email");
                    email.requestFocus();
                    //num,lowe,cou,sim,upe
                }else if (!Patterns.EMAIL_ADDRESS.matcher(s_email).matches()){
                    Toast.makeText(MainActivity.this, "Enter Your  Valied Email Address", Toast.LENGTH_SHORT).show();
                    email.setError("Email");
                    email.requestFocus();
                }else if (TextUtils.isEmpty(s_password)||num==0||lowe==0||cou==0||sim==0||upe==0){
                    Toast.makeText(MainActivity.this, "Enter Strong Passwoed", Toast.LENGTH_SHORT).show();
                    password.setError("Password");
                    password.requestFocus();
                    password.setBackgroundResource(R.drawable.error);
                }else if(con_pass==0||!conpasswoed.contains(s_password)){
                    Toast.makeText(MainActivity.this, "Passwoed Error", Toast.LENGTH_SHORT).show();
                    con_password.setError("Continue Password");
                    con_password.requestFocus();
                    con_password.setBackgroundResource(R.drawable.error);
                }else if (TextUtils.isEmpty(s_name)){
                    Toast.makeText(MainActivity.this, "Enter Your Nick Name", Toast.LENGTH_SHORT).show();
                    name.setError("Enter Nick Name");
                    name.requestFocus();
                }else if (TextUtils.isEmpty(s_address)){
                    Toast.makeText(MainActivity.this, "Enter Your Address", Toast.LENGTH_SHORT).show();
                    address.setError("Address");
                    address.requestFocus();
                }else if (TextUtils.isEmpty(s_phone)){
                    Toast.makeText(MainActivity.this, "Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                    phone.setError("Phone");
                    phone.requestFocus();
                }else if (TextUtils.isEmpty(s_age)){
                    Toast.makeText(MainActivity.this, "Enter Your Age", Toast.LENGTH_SHORT).show();
                    age.setError("Age");
                    age.requestFocus();
                }else if (gander_id==-1){
                    Toast.makeText(MainActivity.this, "Enter select your Gender", Toast.LENGTH_SHORT).show();
                    rediogroopRegisterGendr.setBackgroundResource(R.drawable.error);

                }else if (TextUtils.isEmpty(s_birth)){
                    Toast.makeText(MainActivity.this, "Enter Date Off Birth", Toast.LENGTH_SHORT).show();
                    birthday.setError("Date off birth");
                    birthday.requestFocus();
                }else if (TextUtils.isEmpty(s_about)) {
                    Toast.makeText(MainActivity.this, "About Yourself", Toast.LENGTH_SHORT).show();
                    yoursalf.setError("About Yourself");
                }else {
                    s_gender = redioButtonRegisterGenderSelected.getText().toString();

                    FirebaseRegister(s_email,s_password,s_name,s_address,s_phone,s_age,s_gender,s_birth,s_about);

                }

            }


        });



    }

    // Validate password
    private boolean containsAny(String text, String... characters) {
        for (String character : characters) {
            if (text.contains(character)) {
                return true;
            }
        }
        return false;
    }

    private void FirebaseRegister(String emailId, String passwordId, String s_address, String s_name, String s_phone, String s_age, String s_gender, String s_birth, String s_about) {

        animationView.setVisibility(View.VISIBLE);
        firebaseAuth =FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(emailId,passwordId)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            //usure profile change Request
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(s_name)
                                    .build();
                            firebaseUser.updateProfile(profileUpdates);




                            ReadRightUsureDelels readRightUsureDelels = new ReadRightUsureDelels(s_name,s_address,s_phone,s_age,s_gender,s_birth,s_about);
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                            try {
                                referenceProfile.child(firebaseUser.getUid()).setValue(readRightUsureDelels).addOnCompleteListener(
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    firebaseUser.sendEmailVerification();
                                                    Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                    animationView.setVisibility(View.GONE);

                                                    Intent intent = new Intent(MainActivity.this,LogIn.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                                    //to closs Main Activity
                                                    startActivity(intent);
                                                    finish();

                                                } else {
                                                    Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                    animationView.setVisibility(View.GONE);

                                                }
                                            }
                                        }
                                );
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                                name.setText("" + e);
                            }


                            //com.google.firebase.database.DatabaseException:No properrties to serialize found on class com.example.firebaseauthentitation.Readrightusuredatabase


                        }else {
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                password.setError("Your Password too week");
                                password.requestFocus();
                                animationView.setVisibility(View.GONE);


                            }catch (FirebaseAuthInvalidCredentialsException e){
                                email.setError("Your Email Address invalide");
                                email.requestFocus();
                                animationView.setVisibility(View.GONE);

                            }catch (FirebaseAuthUserCollisionException e){
                                email.setError("User is alrady registerd. use anather Email");
                                email.requestFocus();
                                animationView.setVisibility(View.GONE);

                            }catch (Exception e){
                                Log.d(TAG,e.getMessage());
                                animationView.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}