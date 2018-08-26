package com.example.venkateshkashyap.techtreeittask.activity;

/**
 * Created by Venkatesh Kashyap on 08/25/2018.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.venkateshkashyap.techtreeittask.R;
import com.example.venkateshkashyap.techtreeittask.adapter.ContactsAdapter;
import com.example.venkateshkashyap.techtreeittask.constants.AppConstants;
import com.example.venkateshkashyap.techtreeittask.interfaces.OnItemClickedListener;
import com.example.venkateshkashyap.techtreeittask.model.ContactVo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class ContactsListActivity extends AppCompatActivity implements OnItemClickedListener {

    private RecyclerView mRecyclerView;
    // Identifier for the permission request
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    List<ContactVo> contactVoList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        mRecyclerView = findViewById(R.id.recycler_view);
        askForContactPermission();
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.access_needed);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage(R.string.confirm_contact_access);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , READ_CONTACTS_PERMISSIONS_REQUEST);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            READ_CONTACTS_PERMISSIONS_REQUEST);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                getAllContacts();
            }
        } else {
            getAllContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAllContacts();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, R.string.no_permission, Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getAllContacts() {
        contactVoList = new ArrayList<>();
        ContactVo contactVo;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVo = new ContactVo();
                    contactVo.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);

                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactVo.setContactNumber(phoneNumber);
                    }
                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);

                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        contactVo.setContactEmail(emailId);
                    }

                    Bitmap photo = null;

                    try {
                        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                        if (inputStream != null) {
                            photo = BitmapFactory.decodeStream(inputStream);
                            contactVo.setContactImage(photo);
                            inputStream.close();
                        }

                        assert inputStream != null;
                        //inputStream.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    contactVoList.add(contactVo);
                }
            }
            ContactsAdapter contactsAdapter = new ContactsAdapter(contactVoList, this, this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(contactsAdapter);
            //draw divider line to recycler view
            DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), VERTICAL);
            mRecyclerView.addItemDecoration(itemDecor);
        }
    }

    @Override
    public void onClick(View view, int position) {
        // check if item still exists
        if (position != RecyclerView.NO_POSITION) {
            ContactVo clickedDataItem = contactVoList.get(position);
            Intent intent = new Intent(ContactsListActivity.this, ContactDetailsActivity.class);
            intent.putExtra(AppConstants.CONTACT_DETAILS_LIST, clickedDataItem);
            startActivity(intent);
        }
    }
}
