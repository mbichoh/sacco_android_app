package com.nathanmbichoh.unity_sacco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private Button mNextButton;
    private Button mBackButton;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNextButton = findViewById(R.id.nextBtn);
        mBackButton = findViewById(R.id.prevBtn);
        Button mSkipButton = findViewById(R.id.skipBtn);

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.dotsLayout);

        SliderAdapter sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        //toLogin
        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        //onclick listener for back and next
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage -1);
            }
        });

    }

    public void addDotsIndicator(int position){
        mDots =new TextView[3];
        mDotLayout.removeAllViews();


        for(int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.coloMain));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener  = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
                addDotsIndicator(i);

                mCurrentPage = i;

                if(i == 0){

                    mNextButton.setEnabled(true);
                    mBackButton.setEnabled(false);
                    mBackButton.setVisibility(View.INVISIBLE);

                    mNextButton.setText("Next");
                    mBackButton.setText("");

                }else if(i == mDots.length - 1){

                    mNextButton.setEnabled(true);
                    mBackButton.setEnabled(true);
                    mBackButton.setVisibility(View.VISIBLE);

                    mNextButton.setText("");
                    mBackButton.setText("Back");

                }else{

                    mNextButton.setEnabled(true);
                    mBackButton.setEnabled(true);
                    mBackButton.setVisibility(View.VISIBLE);

                    mNextButton.setText("Next");
                    mBackButton.setText("Back");

                }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
