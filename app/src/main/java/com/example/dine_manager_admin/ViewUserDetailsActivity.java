package com.example.dine_manager_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ViewUserDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ViewUserDetailsActivity";

    private ImageView       backButton;
    private RecyclerView    allCustomersDetailsListView;
    private TextView        nothingToDisplayText;
    private ConstraintLayout viewUserDetailsLayout;
    private CustomDialog    customDialog;
    private LinearLayout    progressLayout;
    private ProgressBar     progressBar;

    private ArrayList<RestaurantDetails> allRestaurants = new ArrayList<>();

    public FirebaseDatabase     firebaseDatabase;
    public DatabaseReference    allRestaurantReference, restaurantDetailsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_details);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#2C0003"));

        setupUI();

        firebaseDatabase = FirebaseDatabase.getInstance();
        allRestaurantReference = firebaseDatabase.getReference().child("dine_manager_admin").child("registered_restaurants");

    }

    @Override
    protected void onStart() {
        super.onStart();

        progressLayout.setVisibility(View.VISIBLE);
        nothingToDisplayText.setVisibility(View.GONE);
        viewUserDetailsLayout.setBackgroundColor(getResources().getColor(R.color.White));
        allCustomersDetailsListView.setVisibility(View.GONE);

        allRestaurantReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allRestaurants.clear();
                for(DataSnapshot restaurantSnapshot : dataSnapshot.getChildren()) {
                    RestaurantDetails restaurantDetail = restaurantSnapshot.getValue(RestaurantDetails.class);
                    allRestaurants.add(restaurantDetail);
                }
                if(allRestaurants.isEmpty()) {
                    nothingToDisplayText.setVisibility(View.VISIBLE);
                    viewUserDetailsLayout.setBackgroundColor(getResources().getColor(R.color.White));
                    allCustomersDetailsListView.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.GONE);
                    customDialog.hideProgressDialog();
                } else {
                    nothingToDisplayText.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.GONE);
                    viewUserDetailsLayout.setBackgroundColor(getResources().getColor(R.color.listViewBackground));
                    allCustomersDetailsListView.setVisibility(View.VISIBLE);
                    RestaurantDetailsAdapter adapter = new RestaurantDetailsAdapter(ViewUserDetailsActivity.this, allRestaurants);
                    allCustomersDetailsListView.setAdapter(adapter);
                    customDialog.hideProgressDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupUI() {
        allCustomersDetailsListView = findViewById(R.id.layout3_customersListView);
        nothingToDisplayText = findViewById(R.id.tv3_nothingToDisplayText);
        progressLayout = findViewById(R.id.layout3_progressLayout);
        progressBar  = findViewById(R.id.pb3_progressBar);
        backButton = findViewById(R.id.imv3_backButton);
        allCustomersDetailsListView.setLayoutManager(new LinearLayoutManager(ViewUserDetailsActivity.this));
        itemOffsetDecoration itemDecoration = new itemOffsetDecoration(Objects.requireNonNull(getApplicationContext()), R.dimen.item_offset);
        allCustomersDetailsListView.addItemDecoration(itemDecoration);
        viewUserDetailsLayout = findViewById(R.id.layout3_viewUserDetailsLayout);
        customDialog = new CustomDialog(ViewUserDetailsActivity.this);
    }
}
