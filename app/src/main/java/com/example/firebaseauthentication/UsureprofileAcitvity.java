package com.example.firebaseauthentication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsureprofileAcitvity extends AppCompatActivity {


    FirebaseAuth firebaseAuth ;
    LottieAnimationView animationView,veryfi;

    TextView tv_name,tv_nikname,tv_gender,tv_age,tv_dateofbirth,tv_mobile,tv_email,tv_address,tv_about;

    ImageView menu;


    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usureprofile_acitvity);

        animationView = findViewById(R.id.animationView);
        tv_name = findViewById(R.id.tv_name);
        tv_nikname = findViewById(R.id.tv_nikname);
        tv_gender = findViewById(R.id.tv_gender);
        tv_age = findViewById(R.id.tv_age);
        tv_dateofbirth = findViewById(R.id.tv_dateofbirth);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_email = findViewById(R.id.tv_email);
        tv_address = findViewById(R.id.tv_address);
        veryfi = findViewById(R.id.veryfi);
        tv_about = findViewById(R.id.tv_about);
        menu = findViewById(R.id.menu);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.refresh_profile){

                    startActivity(getIntent());
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);

                }else if (menuItem.getItemId()==R.id.updatepro){
                    startActivity(getIntent());
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (menuItem.getItemId()==R.id.change_email){
                    drawerLayout.closeDrawer(GravityCompat.START);

                }else if (menuItem.getItemId()==R.id.changepass){
                    drawerLayout.closeDrawer(GravityCompat.START);

                }else if (menuItem.getItemId()==R.id.deleteProfile){
                    drawerLayout.closeDrawer(GravityCompat.START);

                }else if (menuItem.getItemId()==R.id.logout){

                    LogIn.editor.putString("checkverify","");
                    LogIn.editor.apply();

                    firebaseAuth.signOut();
                    Toast.makeText(UsureprofileAcitvity.this, "Log Out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UsureprofileAcitvity.this,LogIn.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //to closs Main Activity
                    startActivity(intent);
                    finish();

                    drawerLayout.closeDrawer(GravityCompat.START);

                }
                return true;
            }
        });
        if (firebaseUser == null){

            animationView.setVisibility(View.GONE);
            Toast.makeText(this, "Sumthing working is error. Usure ditels detels are not avaialable at the mument", Toast.LENGTH_SHORT).show();
        }else {

            String loncherdata = LogIn.sharedPreferences.getString("checkverify","DefultValue");
            if (loncherdata.contains("verified")){
                animationView.setVisibility(View.VISIBLE);
                GetDataInUsureprofile(firebaseUser);
            }else {
                checkVerification(firebaseUser);
            }

        }
    }



    private void checkVerification(FirebaseUser firebaseUser) {
        ShowAlertDialog();
    }
    private void ShowAlertDialog() {

        new AlertDialog.Builder(UsureprofileAcitvity.this)
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
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        firebaseUser.sendEmailVerification();
                        firebaseAuth.signOut();

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void GetDataInUsureprofile(FirebaseUser firebaseUser) {

        String usureID = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(usureID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadRightUsureDelels readUsureDelels = snapshot.getValue(ReadRightUsureDelels.class);
                String nameif = firebaseUser.getDisplayName();
                String email = firebaseUser.getEmail();
                String nik_name = readUsureDelels.getS_name();
                String gender = readUsureDelels.getS_gender();
                String age = readUsureDelels.getS_age();
                String birthday = readUsureDelels.getS_birth();
                String about = readUsureDelels.getS_about();
                String phone = readUsureDelels.getS_phone();
                String address = readUsureDelels.getS_address();


                tv_name.setText(address);
                tv_nikname.setText(address);
                tv_gender.setText(gender);
                tv_age.setText(age);
                tv_dateofbirth.setText(birthday);
                tv_address.setText(nik_name);
                tv_mobile.setText(phone);
                tv_email.setText(email);
                tv_about.setText(about);






                veryfi.playAnimation();
                veryfi.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.GONE);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UsureprofileAcitvity.this, "Error", Toast.LENGTH_SHORT).show();
                animationView.setVisibility(View.GONE);
            }
        });


    }
}