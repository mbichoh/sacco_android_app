package com.nathanmbichoh.unity_sacco;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nathanmbichoh.unity_sacco.adapters.MyInvestmentAdapter;
import com.nathanmbichoh.unity_sacco.controller.CheckInternetConnection;
import com.nathanmbichoh.unity_sacco.controller.VolleySingleton;
import com.nathanmbichoh.unity_sacco.model.SharedPrefManager;
import com.nathanmbichoh.unity_sacco.pojo.InvestmentData;
import com.nathanmbichoh.unity_sacco.pojo.MemberData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private RecyclerView investmentRecyclerView;
    private List<InvestmentData> investmentDataList;

    private MaterialAlertDialogBuilder builder;

    private FloatingActionButton mMainFab, mAddUserFab, mAddContactFab;
    private TextView txtAddUser;
    private TextView txtAddContact;

    private boolean isOpen;

    private Animation mFabOpen, mFabClose;
    private MemberData memberData;

    private static final String URL_INVESTMENT = "http://adrometer.co.ke/unity/api/api/investment/investment.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        CheckInternetConnection.checkConnection(getApplicationContext());

        //check if member session is active: if not, return to login
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Login.class));
        }

        //getting the current user
        memberData = SharedPrefManager.getInstance(this).getUser();

        TextView textMemberName = findViewById(R.id.txtMemberName);
        String welcome = "Hello, "+memberData.getMember_name();
        textMemberName.setText(welcome);

        TextView textMemberPhone = findViewById(R.id.txtMemberPhone);
        textMemberPhone.setText(memberData.getMember_phone());

        //bottom bar
        BottomAppBar mBottomAppBar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(mBottomAppBar);

        mMainFab = findViewById(R.id.main_fab);
        mAddUserFab = findViewById(R.id.add_user_fab);
        mAddContactFab = findViewById(R.id.add_contact_fab);

        txtAddUser = findViewById(R.id.txt_add_user);

        txtAddContact = findViewById(R.id.txt_add_contact);


        //fetch investment
        investmentRecyclerView = findViewById(R.id.investmentsRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        investmentRecyclerView.setLayoutManager(layoutManager);

        investmentDataList = new ArrayList<>();
        loadInvestments();

        //fabs
        mFabOpen = AnimationUtils.loadAnimation(Dashboard.this, R.anim.fab_open);
        mFabClose = AnimationUtils.loadAnimation(Dashboard.this, R.anim.fab_close);

        isOpen = false;

        mAddContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Making a new investment request", Snackbar.LENGTH_LONG).show();
            }
        });
        mAddUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "In case of an issue or question", Snackbar.LENGTH_LONG).show();
            }
        });

        mMainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    mMainFab.setRotation(0);
                    mAddUserFab.setAnimation(mFabClose);
                    mAddContactFab.setAnimation(mFabClose);

                    txtAddUser.setVisibility(View.INVISIBLE);
                    txtAddContact.setVisibility(View.INVISIBLE);

                    isOpen = false;

                }else{

                    mMainFab.setRotation(45);
                    mAddUserFab.setAnimation(mFabOpen);
                    mAddContactFab.setAnimation(mFabOpen);

                    txtAddUser.setVisibility(View.VISIBLE);
                    txtAddContact.setVisibility(View.VISIBLE);

                    isOpen = true;
                }
            }
        });
    }
    //load investments
    private void loadInvestments() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_INVESTMENT+memberData.getMember_id(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.has("message")){
                                builder = new MaterialAlertDialogBuilder(Dashboard.this);
                                builder.setIcon(R.drawable.ic_baseline_warning_24);
                                builder.setBackground(getResources().getDrawable(R.drawable.warning_card, null));
                                builder.setTitle("ERROR:");
                                builder.setMessage(jsonObject.getString("message"));
                                builder.show();
                            }else{
                            JSONArray array = jsonObject.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject investmentData = array.getJSONObject(i);
                                investmentDataList.add(new InvestmentData(
                                        investmentData.getString("id"),
                                        investmentData.getString("investment_date"),
                                        investmentData.getString("investment_maturity_date"),
                                        investmentData.getString("investment_amount"),
                                        investmentData.getString("investment_rate"),
                                        investmentData.getString("investment_status")
                                ));
                            }

                            MyInvestmentAdapter myInvestmentAdapter = new MyInvestmentAdapter(investmentDataList);
                            investmentRecyclerView.setAdapter(myInvestmentAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Oops! sorry an error occurred, try again!", Toast.LENGTH_LONG).show();
                    }
                }
        );
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                CheckInternetConnection.checkConnection(getApplicationContext());
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case R.id.logout:
                startActivity(new Intent(this, Login.class));
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                return true;
            case R.id.notification:
                Toast.makeText(getApplicationContext(), "Notification will show here", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
