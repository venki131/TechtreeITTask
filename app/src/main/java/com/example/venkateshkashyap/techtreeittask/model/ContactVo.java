package com.example.venkateshkashyap.techtreeittask.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Venkatesh Kashyap on 8/25/2018.
 */

public class ContactVo implements Parcelable {
    private Bitmap contactImage;
    private String contactName;
    private String contactNumber;
    private String contactEmail;
    private String contactLandLine;

    protected ContactVo(Parcel in) {
        contactImage = in.readParcelable(Bitmap.class.getClassLoader());
        contactName = in.readString();
        contactNumber = in.readString();
        contactEmail = in.readString();
        contactLandLine = in.readString();
    }

    public ContactVo() {

    }
    public static final Creator<ContactVo> CREATOR = new Creator<ContactVo>() {
        @Override
        public ContactVo createFromParcel(Parcel in) {
            return new ContactVo(in);
        }

        @Override
        public ContactVo[] newArray(int size) {
            return new ContactVo[size];
        }
    };

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactLandLine() {
        return contactLandLine;
    }

    public void setContactLandLine(String contactLandline) {
        this.contactLandLine = contactLandline;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(contactImage, flags);
        dest.writeString(contactName);
        dest.writeString(contactNumber);
        dest.writeString(contactEmail);
        dest.writeString(contactLandLine);
    }
}
