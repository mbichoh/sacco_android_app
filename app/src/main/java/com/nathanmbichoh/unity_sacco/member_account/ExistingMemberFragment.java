package com.nathanmbichoh.unity_sacco.member_account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.nathanmbichoh.unity_sacco.Dashboard;
import com.nathanmbichoh.unity_sacco.ForgotPasswordActivity;
import com.nathanmbichoh.unity_sacco.R;
import com.nathanmbichoh.unity_sacco.controller.CheckInternetConnection;
import com.nathanmbichoh.unity_sacco.model.RequestHandler;
import com.nathanmbichoh.unity_sacco.model.SharedPrefManager;
import com.nathanmbichoh.unity_sacco.pojo.MemberData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class ExistingMemberFragment extends Fragment {

    private MaterialAlertDialogBuilder builder;

    private TextInputEditText txtNationalID, txtPassword;

    private MemberData memberData;

    private static final String URL_LOGIN = "http://adrometer.co.ke/unity/api/api/member/member_login.php?member_national_id=";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.already_a_member_fragment, container, false);

        CheckInternetConnection.checkConnection(Objects.requireNonNull(getContext()));

        txtNationalID   = view.findViewById(R.id.textNationalId);
        txtPassword     = view.findViewById(R.id.textPassword);

        Button btnToLogin = view.findViewById(R.id.loginBtn);
        Button btnForgotPassword = view.findViewById(R.id.forgotPassword);

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInternetConnection.checkConnection(Objects.requireNonNull(getContext()));
                loginMember();
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInternetConnection.checkConnection(Objects.requireNonNull(getContext()));
                startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
            }
        });

        return view;
    }

    public void loginMember(){
        final String nationalID         = Objects.requireNonNull(txtNationalID.getText()).toString().trim();
        final String password           = Objects.requireNonNull(txtPassword.getText()).toString().trim();

        /*
        *VALIDATION
        * check if fields are empty
        * */
        if (nationalID.isEmpty()){
            Snackbar.make(Objects.requireNonNull(getView()), "National ID and Password cannot be empty.\nBOTH FIELDS REQUIRED!", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (password.isEmpty()){
            Snackbar.make(Objects.requireNonNull(getView()), "National ID and Password cannot be empty.\nBOTH FIELDS REQUIRED!", Snackbar.LENGTH_LONG).show();
            return;
        }
        //work in login and get users data
        @SuppressLint("StaticFieldLeak")
        class LoginMember extends AsyncTask<Void, Void, String> {
            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = Objects.requireNonNull(getView()).findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }//end onPreExecute()

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.has("message")) {
                        Snackbar.make(Objects.requireNonNull(getView()), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                    }else {
                        JSONArray array = jsonObject.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jMemberData = array.getJSONObject(i);
                                memberData = new MemberData(
                                    jMemberData.getInt("id"),
                                    jMemberData.getString("member_name"),
                                    jMemberData.getString("member_national_id"),
                                    jMemberData.getString("member_phone")
                            );
                        }

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getActivity()).userLogin(memberData);

                        //set fields to null
                        txtNationalID.setText("");
                        txtPassword.setText("");

                        //start new activity
                        startActivity(new Intent(getActivity(), Dashboard.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }//end onPostExecute()
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("member_national_id", nationalID);
                params.put("member_pwd", password);

                //returning the response
                return requestHandler.sendPostRequest(URL_LOGIN +""+ nationalID +"&member_pwd="+ password, params);
            }
        }

        LoginMember loginMember = new LoginMember();
        loginMember.execute();
    }
}
