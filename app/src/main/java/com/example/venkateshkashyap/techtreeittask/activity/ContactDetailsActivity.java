package com.example.venkateshkashyap.techtreeittask.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.venkateshkashyap.techtreeittask.R;
import com.example.venkateshkashyap.techtreeittask.constants.AppConstants;
import com.example.venkateshkashyap.techtreeittask.model.ContactVo;

import java.util.ArrayList;

public class ContactDetailsActivity extends AppCompatActivity {

    private ImageView mProfileImage;
    private RecyclerView mRecyclerView;
    ArrayList<String> contactList;
    private TextView mContactName;
    private TextView mContactNumber;
    private TextView mEmail;
    private TextView mLandline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        mProfileImage = findViewById(R.id.img_contact_image);
        mRecyclerView = findViewById(R.id.recycler_view);
        mContactName = findViewById(R.id.tv_value_name);
        mContactNumber = findViewById(R.id.tv_value_phone_number);
        mEmail = findViewById(R.id.tv_value_email);
        mLandline = findViewById(R.id.tv_value_landline);

        contactList = new ArrayList<String>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ContactVo contactVo = bundle.getParcelable(AppConstants.CONTACT_DETAILS_LIST);
            if (contactVo.getContactImage() != null) {
                mProfileImage.setImageBitmap(contactVo.getContactImage());
            } else {
                mProfileImage.setImageResource(R.drawable.contact);
            }
            if (contactVo.getContactName() != null) {
                mContactName.setText(contactVo.getContactName());
            }
            if (contactVo.getContactNumber() != null) {
                mContactNumber.setText(contactVo.getContactNumber());
            }
            if (contactVo.getContactEmail() != null) {
                mEmail.setText(contactVo.getContactEmail());
            }
            if (contactVo.getContactLandLine() != null) {
                mLandline.setText(contactVo.getContactLandLine());
            }
        }

    }
}
