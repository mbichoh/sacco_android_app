package com.nathanmbichoh.unity_sacco;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nathanmbichoh.unity_sacco.controller.CheckInternetConnection;
import com.nathanmbichoh.unity_sacco.member_account.ExistingMemberFragment;
import com.nathanmbichoh.unity_sacco.member_account.NewMemberFragment;
import com.nathanmbichoh.unity_sacco.model.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CheckInternetConnection.checkConnection(getApplicationContext());

        //check if member session is active: if not, return to login
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Login.class));
        }

        ViewPager viewPager = findViewById(R.id.viewPagerAccount);
        TabLayout tabLayout = findViewById(R.id.tabLayoutAccount);

        ExistingMemberFragment existingMemberFragment = new ExistingMemberFragment();
        NewMemberFragment newMemberFragment = new NewMemberFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(existingMemberFragment, "Already A Member");
        viewPagerAdapter.addFragment(newMemberFragment, "New Member");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}
