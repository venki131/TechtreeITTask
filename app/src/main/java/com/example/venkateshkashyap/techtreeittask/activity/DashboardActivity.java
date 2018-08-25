package com.example.venkateshkashyap.techtreeittask.activity;

/**
 * Created by Venkatesh Kashyap on 08/25/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.venkateshkashyap.techtreeittask.R;
import com.example.venkateshkashyap.techtreeittask.constants.AppConstants;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    private de.hdodenhof.circleimageview.CircleImageView mProfileImage;
    private TextView mUserName;
    private TextView mEmail;
    private Bundle bundle;
    private Button mWebButton;
    private Button mContactsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mProfileImage = findViewById(R.id.img_profile_image);
        mUserName = findViewById(R.id.tv_profile_name);
        mEmail = findViewById(R.id.tv_email);
        mWebButton = findViewById(R.id.btn_web);
        mContactsButton = findViewById(R.id.btn_contacts);


        mWebButton.setOnClickListener(this);
        mContactsButton.setOnClickListener(this);

        bundle = this.getIntent().getExtras();
        if (bundle != null) {
            mUserName.setText(bundle.getString(AppConstants.USERNAME));
            mEmail.setText(bundle.getString(AppConstants.EMAIL));
            Glide.with(getApplicationContext())
                    .load(bundle.getString(AppConstants.IMAGE_URL))
                    .apply(new RequestOptions())
                    .thumbnail(0.5f)
                    .into(mProfileImage);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_web:
                Intent intent = new Intent(DashboardActivity.this, WebActivity.class);
                startActivity(intent);
            break;
        }
    }
}
