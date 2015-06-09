package com.rorlig.babylog.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rorlig.babylog.model.diaper.DiaperChangeColorType;
import com.rorlig.babylog.model.diaper.DiaperChangeEnum;
import com.rorlig.babylog.model.diaper.DiaperChangeTextureType;
import com.rorlig.babylog.model.diaper.DiaperIncident;

/**
 * table of the diaper changes
 */
@DatabaseTable
public class DiaperChangeDao extends BaseDao implements Parcelable {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    DiaperChangeEnum diaperChangeEventType;

    @DatabaseField
    DiaperChangeTextureType poopTexture;

    @DatabaseField
    DiaperChangeColorType poopColor;

    @DatabaseField
    DiaperIncident diaperChangeIncidentType;

    @DatabaseField
    java.lang.String diaperChangeNotes;


    public DiaperChangeDao() {
    }


    public DiaperChangeDao(DiaperChangeEnum diaperChangeEventType,
                           DiaperChangeTextureType poopTexture,
                           DiaperChangeColorType poopColor,
                           DiaperIncident diaperChangeIncidentType,
                           java.lang.String diaperChangeNotes, Long time) {
        this.diaperChangeEventType = diaperChangeEventType;
        this.poopTexture = poopTexture;
        this.poopColor = poopColor;
        this.diaperChangeIncidentType = diaperChangeIncidentType;
        this.diaperChangeNotes = diaperChangeNotes;
        this.time = time;
    }

    @Override
    public java.lang.String toString() {
        return "DiaperChangeDao{" +
                "id=" + id +
                ", diaperChangeEventType=" + diaperChangeEventType +
                ", poopTexture=" + poopTexture +
                ", poopColor=" + poopColor +
                ", diaperChangeIncidentType=" + diaperChangeIncidentType +
                ", diaperChangeNotes='" + diaperChangeNotes + '\'' +
                ", time=" + time +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.diaperChangeEventType == null ? -1 : this.diaperChangeEventType.ordinal());
        dest.writeInt(this.poopTexture == null ? -1 : this.poopTexture.ordinal());
        dest.writeInt(this.poopColor == null ? -1 : this.poopColor.ordinal());
        dest.writeInt(this.diaperChangeIncidentType == null ? -1 : this.diaperChangeIncidentType.ordinal());
        dest.writeString(this.diaperChangeNotes);
        dest.writeValue(this.time);
    }

    private DiaperChangeDao(Parcel in) {
        this.id = in.readInt();
        int tmpDiaperChangeEventType = in.readInt();
        this.diaperChangeEventType = tmpDiaperChangeEventType == -1 ? null : DiaperChangeEnum.values()[tmpDiaperChangeEventType];
        int tmpPopTexture = in.readInt();
        this.poopTexture = tmpPopTexture == -1 ? null : DiaperChangeTextureType.values()[tmpPopTexture];
        int tmpPopColor = in.readInt();
        this.poopColor = tmpPopColor == -1 ? null : DiaperChangeColorType.values()[tmpPopColor];
        int tmpDiaperChangeIncidentType = in.readInt();
        this.diaperChangeIncidentType = tmpDiaperChangeIncidentType == -1 ? null : DiaperIncident.values()[tmpDiaperChangeIncidentType];
        this.diaperChangeNotes = in.readString();
        this.time = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<DiaperChangeDao> CREATOR = new Creator<DiaperChangeDao>() {
        public DiaperChangeDao createFromParcel(Parcel source) {
            return new DiaperChangeDao(source);
        }

        public DiaperChangeDao[] newArray(int size) {
            return new DiaperChangeDao[size];
        }
    };

    public DiaperChangeEnum getDiaperChangeEventType() {
        return diaperChangeEventType;
    }

    public DiaperChangeTextureType getPoopTexture() {
        return poopTexture;
    }

    public DiaperChangeColorType getPoopColor() {
        return poopColor;
    }

    public DiaperIncident getDiaperChangeIncidentType() {
        return diaperChangeIncidentType;
    }

    public java.lang.String getDiaperChangeNotes() {
        return diaperChangeNotes;
    }


}
