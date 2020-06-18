package com.nathanmbichoh.unity_sacco;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nathanmbichoh.unity_sacco.adapters.MyPaymentAdapter;
import com.nathanmbichoh.unity_sacco.controller.CheckInternetConnection;
import com.nathanmbichoh.unity_sacco.controller.VolleySingleton;
import com.nathanmbichoh.unity_sacco.model.RequestHandler;
import com.nathanmbichoh.unity_sacco.model.SharedPrefManager;
import com.nathanmbichoh.unity_sacco.pojo.PaymentData;
import com.nathanmbichoh.unity_sacco.pojo.PaymentInvestmentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvestmentActiveActivity extends AppCompatActivity {

    private RecyclerView paymentRecyclerView;
    private List<PaymentData> paymentDataList;

    private FloatingActionButton mMainFab, mAddUserFab, mAddContactFab;
    private TextView txtAddUser, txtAddContact;

    private TextView txtPaymentInvestmentStatus;
    private TextView txtPaymentInvestmentDate;
    private TextView txtPaymentInvestmentMaturityDate;
    private TextView txtPaymentInvestmentAmount;
    private TextView txtPaymentInvestmentRefunded;
    private TextView txtPaymentInvestmentBalance;
    private TextView txtPaymentInvestmentRate;
    private TextView txtPaymentInvestmentInterest;
    private TextView txtPaymentInvestmentBank;
    private TextView txtPaymentInvestmentAccountNo;

    private boolean isOpen;

    private Animation mFabOpen, mFabClose;

    private static final String URL_PAYMENT = "http://adrometer.co.ke/unity/api/api/payment/payment.php?id=";
    private static final String URL__PAYMENT_INVESTMENT = "http://adrometer.co.ke/unity/api/api/investment/investment_single.php?id=";

    private static String PAYMENT_INVESTMENT_ = "";
    private static String PAYMENT_ = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_active);

        //if connected
        CheckInternetConnection.checkConnection(getApplicationContext());

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Login.class));
        }

        String investment_id = getIntent().getStringExtra("INVESTMENT_ID");

        PAYMENT_INVESTMENT_ = URL__PAYMENT_INVESTMENT +investment_id;
        PAYMENT_ = URL_PAYMENT +investment_id;

        //paymentInvestments
        txtPaymentInvestmentStatus      = findViewById(R.id.paymentInvestmentStatus);
        txtPaymentInvestmentDate        = findViewById(R.id.paymentInvestmentDate);
        txtPaymentInvestmentMaturityDate    = findViewById(R.id.paymentInvestmentMaturityDate);
        txtPaymentInvestmentAmount      = findViewById(R.id.paymentInvestmentAmount);
        txtPaymentInvestmentRefunded    = findViewById(R.id.paymentInvestmentRefunded);
        txtPaymentInvestmentBalance     = findViewById(R.id.paymentInvestmentBalance);
        txtPaymentInvestmentRate        = findViewById(R.id.paymentInvestmentRate);
        txtPaymentInvestmentInterest    = findViewById(R.id.paymentInvestmentInterest);
        txtPaymentInvestmentBank        = findViewById(R.id.paymentInvestmentBank);
        txtPaymentInvestmentAccountNo   = findViewById(R.id.paymentInvestmentAccountNo);

        getInvestment();
        //fetch investment
        paymentRecyclerView = findViewById(R.id.paymentRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        paymentRecyclerView.setLayoutManager(layoutManager);

        paymentDataList = new ArrayList<>();
        loadPayments();

        //bottom bar
        BottomAppBar mBottomAppBar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(mBottomAppBar);
        mBottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInternetConnection.checkConnection(getApplicationContext());
                startActivity(new Intent(InvestmentActiveActivity.this, Dashboard.class));
            }
        });

        mMainFab = findViewById(R.id.main_fab);
        mAddUserFab = findViewById(R.id.add_user_fab);
        mAddContactFab = findViewById(R.id.add_contact_fab);

        txtAddUser = findViewById(R.id.txt_add_user);
        txtAddContact = findViewById(R.id.txt_add_contact);
        mFabOpen = AnimationUtils.loadAnimation(InvestmentActiveActivity.this, R.anim.fab_open);
        mFabClose = AnimationUtils.loadAnimation(InvestmentActiveActivity.this, R.anim.fab_close);

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

    //load payments
    private void loadPayments() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PAYMENT_,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                                JSONArray array = jsonObject.getJSONArray("data");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject paymentData = array.getJSONObject(i);
                                    paymentDataList.add(new PaymentData(
                                            paymentData.getString("id"),
                                            paymentData.getString("payment_name"),
                                            paymentData.getString("payment_amount"),
                                            paymentData.getString("payment_date")
                                    ));

                                   // investment_amount += Integer.parseInt(paymentData.getString("payment_amount"));
                                }
                                MyPaymentAdapter myPaymentAdapter = new MyPaymentAdapter(paymentDataList);
                                paymentRecyclerView.setAdapter(myPaymentAdapter);
                           // }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       // Toast.makeText(getApplicationContext(), String.valueOf(investment_amount), Toast.LENGTH_LONG).show();
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

    private void getInvestment() {
        final String id = "0";
    @SuppressLint("StaticFieldLeak")
    class FetchClickedInvestmentDetails extends AsyncTask<Void, Void, String>{
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    //creating Investment object
                    PaymentInvestmentData paymentInvestmentData = new PaymentInvestmentData(
                            jsonObject.getString("investment_date"),
                            jsonObject.getString("investment_maturity_date"),
                            jsonObject.getString("investment_amount"),
                            jsonObject.getString("investment_rate"),
                            jsonObject.getString("investment_status"),
                            jsonObject.getString("investment_refunds"),
                            jsonObject.getString("investment_balance"),
                            jsonObject.getString("investment_interest"),
                            jsonObject.getString("member_bank_name"),
                            jsonObject.getString("member_bank_account_no")
                    );
//                    System.out.println(jsonObject);



                    txtPaymentInvestmentAccountNo.setText(paymentInvestmentData.getMember_bank_account_no());
                    txtPaymentInvestmentAmount.setText(paymentInvestmentData.getInvestment_amount());
                    txtPaymentInvestmentBalance.setText(paymentInvestmentData.getInvestment_balance());
                    txtPaymentInvestmentBank.setText(paymentInvestmentData.getMember_bank_name());
                    txtPaymentInvestmentInterest.setText(paymentInvestmentData.getInvestment_interest());
                    txtPaymentInvestmentRate.setText(paymentInvestmentData.getInvestment_rate());
                    txtPaymentInvestmentDate.setText(paymentInvestmentData.getInvestment_date());
                    txtPaymentInvestmentMaturityDate.setText(paymentInvestmentData.getInvestment_maturity_date());
                    txtPaymentInvestmentStatus.setText(paymentInvestmentData.getInvestment_status());
                    txtPaymentInvestmentRefunded.setText(paymentInvestmentData.getInvestment_refunds());

                    if (paymentInvestmentData.getInvestment_status().equalsIgnoreCase("Active")){
                        txtPaymentInvestmentStatus.setTextColor(Color.parseColor("#00E676"));
                        txtPaymentInvestmentStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_success, 0);
                    }else if(paymentInvestmentData.getInvestment_status().equalsIgnoreCase("Complete")){
                        txtPaymentInvestmentStatus.setTextColor(Color.parseColor("#FF1744"));
                        txtPaymentInvestmentStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id", id);

            //returning the response
            return requestHandler.sendPostRequest(PAYMENT_INVESTMENT_, params);
        }
    }
        FetchClickedInvestmentDetails fetchClickedInvestmentDetails = new FetchClickedInvestmentDetails();
        fetchClickedInvestmentDetails.execute();
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
