package com.nathanmbichoh.unity_sacco.member_account;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nathanmbichoh.unity_sacco.Login;
import com.nathanmbichoh.unity_sacco.R;
import com.nathanmbichoh.unity_sacco.controller.CheckInternetConnection;
import com.nathanmbichoh.unity_sacco.model.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class NewMemberFragment extends Fragment {

    private MaterialAlertDialogBuilder builder;

    private TextInputEditText txtNationalID, txtPassword, txtRetypePassword;

    private TextInputLayout retypePasswordLogin, usernameLogin, passwordLogin;

        private static final String URL_REGISTER_MEMBER = "http://adrometer.co.ke/unity/api/api/member/member_exists.php?member_national_id=";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_member_fragment, container, false);



        txtNationalID       = view.findViewById(R.id.txtRegisteredNationalId);
        txtPassword         = view.findViewById(R.id.txtRegisterPassword);
        txtRetypePassword   = view.findViewById(R.id.txtRetypePassword);

        retypePasswordLogin = view.findViewById(R.id.retypePasswordLogin);
        passwordLogin       = view.findViewById(R.id.passwordLogin);
        usernameLogin       = view.findViewById(R.id.usernameLogin);

        Button btnCreateAccount = view.findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInternetConnection.checkConnection(Objects.requireNonNull(getContext()));
                registerMember();
            }
        });
        return view;

    }

    public void registerMember(){
        final String nationalID         = Objects.requireNonNull(txtNationalID.getText()).toString().trim();
        final String password           = Objects.requireNonNull(txtPassword.getText()).toString().trim();
        final String retype_password    = Objects.requireNonNull(txtRetypePassword.getText()).toString().trim();

        //VALIDATION
        //check if username is empty
        if (nationalID.isEmpty()){
            usernameLogin.setError("This field is required.");
            return;
        }else{
            usernameLogin.setError(null);
            usernameLogin.setBoxStrokeColor(Color.parseColor("#4CAF50"));
            usernameLogin.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }

        //check if password is empty
        if (password.isEmpty()) {
            passwordLogin.setError("This field is required.");
            return;
        }else if(password.length() < 5){
            passwordLogin.setError("Password must be atleast 5 characters.");
            return;
        }else{
            passwordLogin.setError(null);
            passwordLogin.setBoxStrokeColor(Color.parseColor("#4CAF50"));
            passwordLogin.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }

        //check if password matches
        if(!password.equals(retype_password)) {
            retypePasswordLogin.setError("Password Mismatch");
            return;
        }else{
            retypePasswordLogin.setError(null);
            retypePasswordLogin.setBoxStrokeColor(Color.parseColor("#4CAF50"));
            retypePasswordLogin.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
        //END VALIDATION

        @SuppressLint("StaticFieldLeak")
        class RegisterMember extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("national_id", nationalID);
                params.put("password", password);

                //returning the response
                return requestHandler.sendPostRequest(URL_REGISTER_MEMBER + "" + nationalID + "&member_password=" + password, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = Objects.requireNonNull(getView()).findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                //converting response to json object
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    //if error in response
                    if(jsonObject.has("message")) {
                        if(jsonObject.getString("message").equals("404")){
                            builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()));
                            builder.setBackground(getResources().getDrawable(R.drawable.error_card, null));
                            builder.setMessage("Member not found. Please check your National ID again.");
                            builder.setTitle("Error");
                            builder.setIcon(R.drawable.ic_baseline_warning_24);
                            //negative button
                            builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }else if(jsonObject.getString("message").equals("409")){
                            builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()));
                            builder.setBackground(getResources().getDrawable(R.drawable.warning_card, null));
                            builder.setMessage("Sorry! Seems the user already exists.");
                            builder.setTitle("Warning");
                            builder.setIcon(R.drawable.ic_baseline_warning_24);
                            //negative button
                            builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }else if(jsonObject.getString("message").equals("201")){
                            View v = Objects.requireNonNull(getView()).findViewById(R.id.btnCreateAccount);
                            //set fields to empty
                            txtNationalID.setText("");
                            txtPassword.setText("");
                            txtRetypePassword.setText("");

                            Snackbar snackbar = Snackbar.make(v, "SUCCESS. Member Created successful!", Snackbar.LENGTH_LONG);
                            snackbar.setDuration(10000);
                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                            snackbar.setAction("OKAY", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getActivity(), Login.class));
                                }
                            });
                            snackbar.show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        RegisterMember registerMember = new RegisterMember();
        registerMember.execute();
    }
}
