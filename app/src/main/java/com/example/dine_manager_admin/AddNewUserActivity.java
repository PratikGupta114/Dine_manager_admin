package com.example.dine_manager_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddNewUserActivity extends AppCompatActivity {

    private static final String TAG = "AddNewUserActivity";

    private Button          scanQRCodeButton, createAccountButton;
    private ImageView       backButton;
    private RelativeLayout  firstView, secondView;
    private TextView        phoneNumberText, emailText, passwordText, restaurantNameText, restaurantOwnerNameText;
    private CustomDialog    customDialog;

    private FirebaseAuth        firebaseAuth;
    private FirebaseDatabase    firebaseDatabase;
    private DatabaseReference   dineManagerAdminReference, dineManagerReference;

    private String  phoneNumber, email, password, restaurantName, restaurantOwnerName;
    private boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#2C0003"));

        setupUI();
        isFirst = true;

        Intent intent = getIntent();
        if(intent.hasExtra("IS_COMING_FROM_SCANNER")) {
            if(intent.getBooleanExtra("IS_COMING_FROM_SCANNER", false)) {
                firstView.setVisibility(View.GONE);
                secondView.setVisibility(View.VISIBLE);
                isFirst = false;

                phoneNumber = intent.getStringExtra("PHONE_NUMBER");
                email = intent.getStringExtra("EMAIL_ADDRESS");
                password = intent.getStringExtra("PASSWORD");
                restaurantName = intent.getStringExtra("RESTAURANT_NAME");
                restaurantOwnerName = intent.getStringExtra("RESTAURANT_OWNER_NAME");

                phoneNumberText.setText(phoneNumber);
                emailText.setText(email);
                passwordText.setText(password);
                restaurantNameText.setText(restaurantName);
                restaurantOwnerNameText.setText(restaurantOwnerName);

            }
            else {
                isFirst = true;
                firstView.setVisibility(View.VISIBLE);
                secondView.setVisibility(View.GONE);
            }

        } else {
            isFirst = true;
            firstView.setVisibility(View.VISIBLE);
            secondView.setVisibility(View.GONE);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dineManagerAdminReference = firebaseDatabase.getReference().child("dine_manager_admin");
        dineManagerReference = firebaseDatabase.getReference().child("dine_manager_user");
    }
    @Override
    protected void onStart() {
        super.onStart();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        scanQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewUserActivity.this , ScannerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog.showProgressBarWithMessage("Signing Up User");

                final ArrayList<RestaurantDetails> restaurantDetailsArrayList = new ArrayList<>();

                Log.e(TAG, "Query started");
                Query numberQuery = dineManagerAdminReference.child("registered_restaurants").orderByChild("phoneNumber").equalTo(phoneNumber);
                numberQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot detailSnapshot : dataSnapshot.getChildren()) {
                            if(detailSnapshot != null)
                                restaurantDetailsArrayList.add(detailSnapshot.getValue(RestaurantDetails.class));
                        }
                        Log.e(TAG, "Query received array list is empty "+restaurantDetailsArrayList.isEmpty());

                        if(!restaurantDetailsArrayList.isEmpty()) {
                            Toast.makeText(AddNewUserActivity.this, "Phone number already registered", Toast.LENGTH_SHORT).show();
                            customDialog.hideProgressDialog();
                        } else
                            createUser();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void setupUI() {
        firstView = findViewById(R.id.layout2_firstView);
        secondView = findViewById(R.id.layout2_secondView);
        backButton = findViewById(R.id.imv2_backButton);
        scanQRCodeButton = findViewById(R.id.bt2_scanQButton);
        createAccountButton = findViewById(R.id.bt2_createAccount);
        phoneNumberText = findViewById(R.id.tv2_phoneNumber);
        emailText = findViewById(R.id.tv2_emailAddress);
        passwordText = findViewById(R.id.tv2_password);
        restaurantNameText = findViewById(R.id.tv2_restaurantName);
        restaurantOwnerNameText = findViewById(R.id.tv2_restaurantOwnerName);

        firstView.setVisibility(View.VISIBLE);
        secondView.setVisibility(View.INVISIBLE);

        customDialog = new CustomDialog(AddNewUserActivity.this);
    }
    private void createUser() {
        // Creating a new Restaurant Details Object
        final RestaurantDetails newRestaurantDetails = new RestaurantDetails();

        // Filling up the data received
        newRestaurantDetails.setEmailAddress(email);
        newRestaurantDetails.setPhoneNumber(phoneNumber);
        newRestaurantDetails.setPassword(password);
        newRestaurantDetails.setRestaurantName(restaurantName);
        newRestaurantDetails.setRestaurantOwnerName(restaurantOwnerName);
        newRestaurantDetails.setSmsCreditsLeft(10L);
        newRestaurantDetails.setDateCreatedOn(Tools.getCurrentDate());
        newRestaurantDetails.setTimeCreatedOn(Tools.getCurrentTime());
        newRestaurantDetails.setStatus(RestaurantDetails.STATUS_ALLOWED);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        String UID = firebaseAuth.getCurrentUser().getUid();
                        newRestaurantDetails.setUID(UID);
                        Log.e(TAG, "Current User's UID : "+UID);

                        customDialog.showProgressBarWithMessage("Updating User Data");
                        dineManagerAdminReference.child("registered_restaurants").child(UID).setValue(newRestaurantDetails)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {

                                            firebaseAuth.signOut();

                                            // When the task is successfully completed inform the user on his end
                                            customDialog.showProgressBarWithMessage("Informing User");
                                            dineManagerAdminReference.child("sign_up_status").setValue(true)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()) {

                                                                // When the User is informed
                                                                customDialog.hideProgressDialog();
                                                                Toast.makeText(AddNewUserActivity.this, "User registered Successfully", Toast.LENGTH_SHORT).show();

                                                            } else {

                                                                // When the User isn't informed
                                                                customDialog.hideProgressDialog();
                                                                Toast.makeText(AddNewUserActivity.this, "User registered successfully but not informed", Toast.LENGTH_SHORT).show();
                                                            }
                                                            Intent intent = new Intent(AddNewUserActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                        } else {

                                            // When user is registered but data not uploaded.
                                            customDialog.hideProgressDialog();
                                            Toast.makeText(AddNewUserActivity.this, "User Registered but data not uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customDialog.hideProgressDialog();
                        if(e instanceof FirebaseNetworkException)
                            Toast.makeText(AddNewUserActivity.this, "You are Offline", Toast.LENGTH_SHORT).show();
                        else if(e instanceof FirebaseAuthUserCollisionException)
                            Toast.makeText(AddNewUserActivity.this, "User with email address Already exists", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AddNewUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
