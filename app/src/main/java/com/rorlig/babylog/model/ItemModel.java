package com.rorlig.babylog.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaurav
 */
public class ItemModel implements Parcelable {

    private String itemName;
    private boolean itemChecked;

    public ItemModel() {
    }

    public ItemModel(String itemName, boolean itemChecked) {
        this.itemName = itemName;
        this.itemChecked = itemChecked;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isItemChecked() {
        return itemChecked;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemChecked(boolean itemChecked) {
        this.itemChecked = itemChecked;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeByte(itemChecked ? (byte) 1 : (byte) 0);
    }

    private ItemModel(Parcel in) {
        this.itemName = in.readString();
        this.itemChecked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ItemModel> CREATOR = new Parcelable.Creator<ItemModel>() {
        public ItemModel createFromParcel(Parcel source) {
            return new ItemModel(source);
        }

        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    @Override
    public String toString() {
        return "ItemModel{" +
                "itemName='" + itemName + '\'' +
                ", itemChecked=" + itemChecked +
                '}';
    }
}
