package com.example.askchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.askchat.fragment.HomeFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imageViewFavor, imageViewChat, imageViewSetting;

    //Fragment
    private FragmentManager fragmentManager;
    private Fragment fragmentHome;

    //Navigation Drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CustomDrawerButton imageViewNavigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();  //action bar hidden
        fragmentManager = getSupportFragmentManager();
        bindView();
        setOnClickListener();
        initHomeFragment();
        setNavigationDrawer();
    }

    private void bindView() {
        imageViewNavigationDrawer = findViewById(R.id.drawerBTN);
        imageViewFavor = findViewById(R.id.favorBTN);
        imageViewChat = findViewById(R.id.chatBTN);
        imageViewSetting = findViewById(R.id.settingBTN);
    }

    private void setOnClickListener() {
        imageViewNavigationDrawer.setOnClickListener(this);
        imageViewFavor.setOnClickListener(this);
        imageViewChat.setOnClickListener(this);
        imageViewSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);

        switch (view.getId()){
            case R.id.drawerBTN:
                setAllSelectedFalse();
                imageViewNavigationDrawer.setSelected(true);
                break;

            case R.id.favorBTN:
                setAllSelectedFalse();
                imageViewFavor.setSelected(true);
                break;

            case R.id.chatBTN:
                setAllSelectedFalse();
                imageViewChat.setSelected(true);
                break;

            case R.id.settingBTN:
                setAllSelectedFalse();
                imageViewSetting.setSelected(true);
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
    }

    private void initHomeFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.fragment_container_view, homeFragment);
        fragmentTransaction.commitAllowingStateLoss();
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.drawer_menu_home:
                        break;

                    case R.id.drawer_menu_account:
                        break;

                    case R.id.drawer_menu_question:
                        break;

                    case R.id.drawer_menu_chat:
                        break;

                    case R.id.drawer_menu_favor:
                        break;
                }

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