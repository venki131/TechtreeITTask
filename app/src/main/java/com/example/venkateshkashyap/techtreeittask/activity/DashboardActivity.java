package com.example.venkateshkashyap.techtreeittask.activity;

/**
 * Created by Venkatesh Kashyap on 08/25/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.venkateshkashyap.techtreeittask.R;
import com.example.venkateshkashyap.techtreeittask.constants.AppConstants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private de.hdodenhof.circleimageview.CircleImageView mProfileImage;
    private TextView mUserName;
    private TextView mEmail;
    private Bundle bundle;
    private Button mWebButton;
    private Button mContactsButton;
    private Button mLogoutButton;
    private JSONObject response, profilePicData, profilePicUrl;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mProfileImage = findViewById(R.id.img_profile_image);
        mUserName = findViewById(R.id.tv_profile_name);
        mEmail = findViewById(R.id.tv_email);
        mWebButton = findViewById(R.id.btn_web);
        mContactsButton = findViewById(R.id.btn_contacts);
        mLogoutButton = findViewById(R.id.btn_logout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent intent = getIntent();
        if (intent != null) {
            String jsondata = intent.getStringExtra(AppConstants.USER_PROFILE);
            //Log.w("Jsondata", jsondata);
            try {
                response = new JSONObject(jsondata);
                mEmail.setText(response.get("email").toString());
                mUserName.setText(response.get("name").toString());
                profilePicData = new JSONObject(response.get("picture").toString());
                profilePicUrl = new JSONObject(profilePicData.getString("data"));
                Glide.with(getApplicationContext())
                        .load(profilePicData.getString("url"))
                        .apply(new RequestOptions())
                        .thumbnail(0.5f)
                        .into(mProfileImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mWebButton.setOnClickListener(this);
        mContactsButton.setOnClickListener(this);
        mLogoutButton.setOnClickListener(this);

        bundle = this.getIntent().getExtras();
        if (bundle != null) {
            mUserName.setText(bundle.getString(AppConstants.USERNAME));
            mEmail.setText(bundle.getString(AppConstants.EMAIL));
            if (bundle.getString(AppConstants.IMAGE_URL) != null) {
                Glide.with(getApplicationContext())
                        .load(bundle.getString(AppConstants.IMAGE_URL))
                        .apply(new RequestOptions())
                        .thumbnail(0.5f)
                        .into(mProfileImage);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_web:
                Intent intent = new Intent(DashboardActivity.this, WebActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_contacts:
                Intent contactIntent = new Intent(DashboardActivity.this, ContactsListActivity.class);
                startActivity(contactIntent);
                break;
            case R.id.btn_logout:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //minimize app on back pressed
        this.moveTaskToBack(true);
    }
}
