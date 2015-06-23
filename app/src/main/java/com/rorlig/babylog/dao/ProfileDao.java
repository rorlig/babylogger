package com.rorlig.babylog.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author gaurav gupta
 * Profile Dao -- not used -- yet...
 */
@DatabaseTable
public class ProfileDao implements Parcelable {


    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    boolean isSend;

    //    @DatabaseField
//    String networkType;
//
    @DatabaseField(defaultValue = "0")
    Long cardBuyTime;

    @DatabaseField(defaultValue = "0")
    Integer cardValue;

    @DatabaseField
    String merchantName;

    @DatabaseField
    String contactName;

    @DatabaseField
    String imageURL;

    public ProfileDao() {

    }

    public ProfileDao(String merchantName,
                      int cardValue,
                      long cardBuyTime,
                      boolean isSend,
                      String contactName, String imageURL) {
        this.merchantName = merchantName;
        this.cardValue = cardValue;
        this.cardBuyTime  = cardBuyTime;
        this.isSend = isSend;
        this.contactName = contactName;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getId() {
        return id;
    }

    public boolean isSend() {
        return isSend;
    }

    public Long getCardBuyTime() {
        return cardBuyTime;
    }

    public Integer getCardValue() {
        return cardValue;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getContactName() {
        return contactName;
    }


    @Override
    public String toString() {
        return "CardsDao{" +
                "id=" + id +
                ", isSend=" + isSend +
                ", cardBuyTime=" + cardBuyTime +
                ", cardValue=" + cardValue +
                ", merchantName='" + merchantName + '\'' +
                ", contactName='" + contactName + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeByte(isSend ? (byte) 1 : (byte) 0);
        dest.writeValue(this.cardBuyTime);
        dest.writeValue(this.cardValue);
        dest.writeString(this.merchantName);
        dest.writeString(this.contactName);
        dest.writeString(this.imageURL);
    }

    private ProfileDao(Parcel in) {
        this.id = in.readInt();
        this.isSend = in.readByte() != 0;
        this.cardBuyTime = (Long) in.readValue(Long.class.getClassLoader());
        this.cardValue = (Integer) in.readValue(Integer.class.getClassLoader());
        this.merchantName = in.readString();
        this.contactName = in.readString();
        this.imageURL = in.readString();
    }

    public static final Creator<ProfileDao> CREATOR = new Creator<ProfileDao>() {
        public ProfileDao createFromParcel(Parcel source) {
            return new ProfileDao(source);
        }

        public ProfileDao[] newArray(int size) {
            return new ProfileDao[size];
        }
    };
}
