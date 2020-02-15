package com.example.dine_manager_admin;

import android.app.Activity;
import android.os.Build;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RestaurantDetailsAdapter extends RecyclerView.Adapter<RestaurantDetailsAdapter.RestaurantDetailsViewHolder>{

    private ArrayList<RestaurantDetails> allRestaurants;
    private Activity activity;

    private DatabaseReference userBlockingReference;
    private CustomDialog      customDialog;

    public RestaurantDetailsAdapter(Activity activity, ArrayList<RestaurantDetails> allRestaurants) {
        this.activity = activity;
        this.allRestaurants = allRestaurants;
        customDialog = new CustomDialog(activity);
    }

    @NonNull
    @Override
    public RestaurantDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_restaurant_details, parent, false);
        return new RestaurantDetailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RestaurantDetailsViewHolder holder, final int position) {
        final RestaurantDetails restaurantDetails = allRestaurants.get(position);

        holder.restaurantName.setText(restaurantDetails.getRestaurantName());
        holder.ownerName.setText(restaurantDetails.getRestaurantOwnerName());
        holder.email.setText(restaurantDetails.getEmailAddress());
        holder.smsCreditsLeft.setText(Long.toString(restaurantDetails.getSmsCreditsLeft()));
        holder.contact.setText(restaurantDetails.getPhoneNumber());
        holder.password.setText(restaurantDetails.getPassword());
        holder.createdOn.setText(Tools.formatDatetoDDMMYYYY(restaurantDetails.getDateCreatedOn())+" "+restaurantDetails.getTimeCreatedOn());
        setStatusStringForInt(restaurantDetails.getStatus(), holder.status);

        holder.menuOverflowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(activity.getApplicationContext(), holder.menuOverflowIcon);
                menu.inflate(R.menu.list_item_restaurants_menu);

                Menu listItemMenu = menu.getMenu();
                MenuItem itemBlockUser = listItemMenu.findItem(R.id.menu3_blockUser);

                if(restaurantDetails.getStatus() == RestaurantDetails.STATUS_ALLOWED)
                    itemBlockUser.setTitle("Block This User");
                else if(restaurantDetails.getStatus() == RestaurantDetails.STATUS_BLOCKED)
                    itemBlockUser.setTitle("Unblock This User");

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu3_addCredits : {

                            }
                            break;
                            case R.id.menu3_blockUser : {

                                userBlockingReference = FirebaseDatabase.getInstance().getReference().child("dine_manager_admin")
                                        .child("registered_restaurants").child(restaurantDetails.getUID()).child("status");

                                int status = restaurantDetails.getStatus();
                                if(status == RestaurantDetails.STATUS_ALLOWED) {
                                     final int newStatus = RestaurantDetails.STATUS_BLOCKED;
                                     customDialog.showProgressBar();
                                    userBlockingReference.setValue(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            customDialog.hideProgressDialog();
                                            if(task.isSuccessful()) {
                                                restaurantDetails.setStatus(newStatus);
                                                allRestaurants.set(position, restaurantDetails);
                                                notifyItemChanged(position);
                                                Toast.makeText(activity, "Blocked user successfully", Toast.LENGTH_SHORT).show();
                                                item.setTitle("Unblock This User");
                                            } else {
                                                Toast.makeText(activity, "Could not Block user.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else if(status == RestaurantDetails.STATUS_BLOCKED){
                                    final int newStatus = RestaurantDetails.STATUS_ALLOWED;
                                    customDialog.showProgressBar();
                                    userBlockingReference.setValue(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            customDialog.hideProgressDialog();
                                            if(task.isSuccessful()) {
                                                restaurantDetails.setStatus(newStatus);
                                                allRestaurants.set(position, restaurantDetails);
                                                notifyItemChanged(position);
                                                Toast.makeText(activity, "User unblocked Successfully", Toast.LENGTH_SHORT).show();
                                                item.setTitle("Block This User");
                                            } else {
                                                Toast.makeText(activity, "Could not unblock user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                            break;
                            case R.id.menu3_deleteUser : {

                            }
                            break;
                            case R.id.menu3_sendAMessage : {

                            }
                        }
                        return false;
                    }
                });
                menu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return allRestaurants.size();
    }


    private void setStatusStringForInt(int status, TextView textView) {
        switch (status) {
            case RestaurantDetails.STATUS_ALLOWED : {
                textView.setText("Allowed");
                textView.setTextColor(activity.getResources().getColor(R.color.successGreenColour));
            }
            break;
            case RestaurantDetails.STATUS_BLOCKED : {
                textView.setText("Blocked");
                textView.setTextColor(activity.getResources().getColor(R.color.warningRedColour));
            }
            break;
            default : {
                textView.setText("Not Specified");
                textView.setTextColor(activity.getResources().getColor(R.color.warningYellowColour));
            }
            break;
        }
    }

    public class RestaurantDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView restaurantName, ownerName, password, status, createdOn, email, smsCreditsLeft, menuOverflowIcon, contact;

        public RestaurantDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantName = itemView.findViewById(R.id.lic_tv3_restaurantName);
            ownerName = itemView.findViewById(R.id.lic_tv3_ownerName);
            password = itemView.findViewById(R.id.lic_tv3_password);
            status = itemView.findViewById(R.id.lic_tv3_lockStatus);
            createdOn = itemView.findViewById(R.id.lic_tv3_createdOn);
            email = itemView.findViewById(R.id.lic_tv3_email);
            smsCreditsLeft = itemView.findViewById(R.id.lic_tv3_smsCredits);
            menuOverflowIcon = itemView.findViewById(R.id.lic_tv3_menuOverflowIcon);
            contact = itemView.findViewById(R.id.lic_tv3_contact);
        }
    }

}
