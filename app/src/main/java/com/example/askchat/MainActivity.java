package com.example.askchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.askchat.fragment.ChatFragment;
import com.example.askchat.fragment.HomeFragment;
import com.example.askchat.fragment.MyAccountActivity;
import com.example.askchat.fragment.MyFavorFragment;
import com.example.askchat.fragment.MyQuestionFragment;
import com.example.askchat.fragment.SettingFragment;
import com.example.askchat.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imageViewFavor, imageViewChat, imageViewSetting;
    CardView cardView_logoutButton;
    ProgressBar progressBar;

    //Fragment
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private Fragment fragmentHome, fragmentMyQuestion, fragmentSetting, fragmentMyFavor, fragmentChat;

    //Navigation Drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CustomDrawerButton imageViewNavigationDrawer;

    //Firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    //User information


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();  //action bar hidden
        fragmentManager = getSupportFragmentManager();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getUserInformation();
        bindView();
        setOnClickListener();
        initHomeFragment();
        setNavigationDrawer();
    }

    private void getUserInformation() {
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("Users");
        userID = firebaseUser.getUid();
    }

    private void bindView() {
        imageViewNavigationDrawer = findViewById(R.id.drawerBTN);
        imageViewFavor = findViewById(R.id.favorBTN);
        imageViewChat = findViewById(R.id.chatBTN);
        imageViewSetting = findViewById(R.id.settingBTN);
        cardView_logoutButton = findViewById(R.id.mainActivity_logoutBTN);
        progressBar = findViewById(R.id.mainActivity_progress_bar);
    }

    private void setOnClickListener() {
        imageViewNavigationDrawer.setOnClickListener(this);
        imageViewFavor.setOnClickListener(this);
        imageViewChat.setOnClickListener(this);
        imageViewSetting.setOnClickListener(this);
        cardView_logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);

        switch (view.getId()){
            case R.id.drawerBTN:
                setAllSelectedFalse();
                imageViewNavigationDrawer.setSelected(true);
                break;

            case R.id.favorBTN:
                setAllSelectedFalse();
                imageViewFavor.setSelected(true);
                if (fragmentMyFavor == null) {
                    fragmentMyFavor = new MyFavorFragment();
                    fragmentTransaction.add(R.id.fragment_container_view, fragmentMyFavor);
                } else {
                    fragmentTransaction.show(fragmentMyFavor);
                }
                break;

            case R.id.chatBTN:
                setAllSelectedFalse();
                imageViewChat.setSelected(true);
                if (fragmentChat == null) {
                    fragmentChat = new ChatFragment();
                    fragmentTransaction.add(R.id.fragment_container_view, fragmentChat);
                } else {
                    fragmentTransaction.show(fragmentChat);
                }
                break;

            case R.id.settingBTN:
                setAllSelectedFalse();
                imageViewSetting.setSelected(true);
                if (fragmentSetting == null) {
                    fragmentSetting = new SettingFragment();
                    fragmentTransaction.add(R.id.fragment_container_view, fragmentSetting);
                } else {
                    fragmentTransaction.show(fragmentSetting);
                }
                break;

            case R.id.mainActivity_logoutBTN:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void setAllSelectedFalse() {
        imageViewNavigationDrawer.setSelected(false);
        imageViewFavor.setSelected(false);
        imageViewChat.setSelected(false);
        imageViewSetting.setSelected(false);
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (fragmentHome != null) fragmentTransaction.hide(fragmentHome);
        if (fragmentChat != null) fragmentTransaction.hide(fragmentChat);
        if (fragmentMyQuestion != null) fragmentTransaction.hide(fragmentMyQuestion);
        if (fragmentMyFavor != null) fragmentTransaction.hide(fragmentMyFavor);
        if (fragmentSetting != null) fragmentTransaction.hide(fragmentSetting);
    }

    private void initHomeFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentHome = new HomeFragment();
        fragmentTransaction.add(R.id.fragment_container_view, fragmentHome);
        fragmentTransaction.commitAllowingStateLoss();
        progressBar.setVisibility(View.GONE);
    }

    protected void setNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_drawer);

        imageViewNavigationDrawer.setDrawerLayout(drawerLayout);
        imageViewNavigationDrawer.getDrawerLayout().addDrawerListener(imageViewNavigationDrawer);

        imageViewNavigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewNavigationDrawer.changeState();
            }
        });

        //drawer header user information setup
        View headerView = navigationView.getHeaderView(0);
        ImageView imageViewBackground = headerView.findViewById(R.id.drawer_header_background);
        ImageView imageViewIcon = headerView.findViewById(R.id.drawer_header_icon);
        TextView textViewUserName = headerView.findViewById(R.id.drawer_header_user_name);
        TextView textViewUserEmail = headerView.findViewById(R.id.drawer_header_user_email);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userProfile = snapshot.getValue(UserModel.class);

                if (userProfile != null) {
                    String userName = userProfile.getUserName();
                    String userEmail = userProfile.getEmail();
                    textViewUserName.setText(userName);
                    textViewUserEmail.setText(userEmail);

                    //decode image
                    if (userProfile.getEncodedUserIcon() != "") {
                        byte[] bytes = Base64.decode(userProfile.getEncodedUserIcon(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageViewIcon.setImageBitmap(bitmap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Unable to get user information", Toast.LENGTH_LONG).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragmentTransaction = fragmentManager.beginTransaction();
                hideAllFragment(fragmentTransaction);
                setAllSelectedFalse();

                switch (item.getItemId()) {
                    case R.id.drawer_menu_home:
                        if (fragmentHome == null) {
                            fragmentHome = new HomeFragment();
                            fragmentTransaction.add(R.id.fragment_container_view, fragmentHome);
                        } else {
                            fragmentTransaction.show(fragmentHome);
                        }
                        break;

                    case R.id.drawer_menu_account:
                        startActivity(new Intent(MainActivity.this, MyAccountActivity.class));
                        finish();
                        break;

                    case R.id.drawer_menu_question:
                        if (fragmentMyQuestion == null) {
                            fragmentMyQuestion = new MyQuestionFragment();
                            fragmentTransaction.add(R.id.fragment_container_view, fragmentMyQuestion);
                        } else {
                            fragmentTransaction.show(fragmentMyQuestion);
                        }
                        break;

                    case R.id.drawer_menu_chat:
                        if (fragmentChat == null) {
                            fragmentChat = new ChatFragment();
                            fragmentTransaction.add(R.id.fragment_container_view, fragmentChat);
                        } else {
                            fragmentTransaction.show(fragmentChat);
                        }
                        break;

                    case R.id.drawer_menu_favor:
                        if (fragmentMyFavor == null) {
                            fragmentMyFavor = new SettingFragment();
                            fragmentTransaction.add(R.id.fragment_container_view, fragmentMyFavor);
                        } else {
                            fragmentTransaction.show(fragmentMyFavor);
                        }
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentTransaction.commitAllowingStateLoss();
                return false;
            }
        });
    }

    //Handle navigation view item clicks in activity method
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}