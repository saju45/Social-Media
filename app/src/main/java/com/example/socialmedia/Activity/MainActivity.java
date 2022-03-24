package com.example.socialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.socialmedia.Fragments.AddFragment;
import com.example.socialmedia.Fragments.HomeFragment;
import com.example.socialmedia.Fragments.NotificationFragment;
import com.example.socialmedia.Fragments.ProfileFragment;
import com.example.socialmedia.Fragments.SearchFragment;
import com.example.socialmedia.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MainActivity.this.setTitle("My Profile");
        toolbar.setVisibility(View.GONE);

        auth=FirebaseAuth.getInstance();

        navigationView=findViewById(R.id.bottomnavigation);

        if (savedInstanceState ==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,new HomeFragment()).commit();

        }

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment= null;

                if (item.getItemId()==R.id.home)
                {
                    toolbar.setVisibility(View.GONE);
                    fragment=new HomeFragment();
                }
                if (item.getItemId()==R.id.notification)
                {
                    toolbar.setVisibility(View.GONE);
                    fragment=new NotificationFragment();
                }
                if (item.getItemId()==R.id.add)
                {
                    toolbar.setVisibility(View.GONE);
                    fragment=new AddFragment();
                }
                if (item.getItemId()==R.id.search)
                {
                    toolbar.setVisibility(View.GONE);
                    fragment=new SearchFragment();
                }
               if (item.getItemId()==R.id.profile)
                {
                    toolbar.setVisibility(View.VISIBLE);
                    fragment=new ProfileFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,fragment).commit();

                return true;
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.setting:
                auth.signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}