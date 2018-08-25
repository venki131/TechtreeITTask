package com.example.venkateshkashyap.techtreeittask.model;

import android.graphics.Bitmap;

/**
 * Created by Venkatesh Kashyap on 8/25/2018.
 */

public class ContactVo {
    private Bitmap contactImage;
    private String contactName;
    private String contactNumber;

    public Bitmap getContactImage() {
        return contactImage;
    }

    public void setContactImage(Bitmap contactImage) {
        this.contactImage = contactImage;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
