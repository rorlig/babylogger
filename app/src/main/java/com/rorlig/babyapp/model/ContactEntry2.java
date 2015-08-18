package com.rorlig.babyapp.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by gaurav
 */
public class ContactEntry2 implements Parcelable, ContactItem {

    private String email;
    private String entryId ;
    private String name;
    private int pictureResId;
    private String photoUri;



    public ContactEntry2(String name, String email, int pictureResId) {
        this.name = name;
        this.pictureResId = pictureResId;
        this.email = email;
    }


    public Uri getPictureUri() {
        if(photoUri == null) return null;
        return Uri.parse(photoUri);
    }

    public String getName() {
        return name;
    }

    public String getEntryId() {
        return entryId;
    }





    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public int getPictureResId() {
        return pictureResId;
    }

    @Override
    public String toString() {
        return "ContactEntry2{" +
                "email='" + email + '\'' +
                ", entryId='" + entryId + '\'' +
                ", name='" + name + '\'' +
                ", pictureResId=" + pictureResId +
                ", photoUri='" + photoUri + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.entryId);
        dest.writeString(this.name);
        dest.writeInt(this.pictureResId);
        dest.writeString(this.photoUri);
    }

    private ContactEntry2(Parcel in) {
        this.email = in.readString();
        this.entryId = in.readString();
        this.name = in.readString();
        this.pictureResId = in.readInt();
        this.photoUri = in.readString();
    }

    public static final Creator<ContactEntry2> CREATOR = new Creator<ContactEntry2>() {
        public ContactEntry2 createFromParcel(Parcel source) {
            return new ContactEntry2(source);
        }

        public ContactEntry2[] newArray(int size) {
            return new ContactEntry2[size];
        }
    };

    @Override
    public int getViewType() {
        return 0;
    }
}
