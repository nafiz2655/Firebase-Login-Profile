package com.example.firebaseauthentication;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
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

    TextView tv_name,tv_nikname,tv_gender,tv_age,tv_dateofbirth,tv_mobile,tv_email,tv_address;

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


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null){

            animationView.setVisibility(View.GONE);
            Toast.makeText(this, "Sumthing working is error. Usure ditels detels are not avaialable at the mument", Toast.LENGTH_SHORT).show();
        }else {
            animationView.setVisibility(View.VISIBLE);
            GetDataInUsureprofile(firebaseUser);
        }
    }

    private void GetDataInUsureprofile(FirebaseUser firebaseUser) {

        String usureID = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(usureID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadRightUsureDelels readUsureDelels = snapshot.getValue(ReadRightUsureDelels.class);
                String name = firebaseUser.getDisplayName();
                String email = firebaseUser.getEmail();
                String phone = readUsureDelels.getS_phone();
                String address = readUsureDelels.getS_address();

                tv_email.setText(email);
                tv_name.setText(name);
                tv_mobile.setText(phone);
                tv_address.setText(address);
                veryfi.setVisibility(View.VISIBLE);
                veryfi.playAnimation();

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