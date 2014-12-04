package com.imersaovisual.budgetcontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by LuizCarlos on 03/12/2014.
 */
public class Transaction implements Parcelable {
    private Integer id;
    private String description;
    private double value;
    private Date date;


    public Transaction() {

    }

    public Transaction(String description, Date date, double value) {
        this.description = description;
        this.date = date;
        this.value = value;
    }

    public Transaction(Integer id, String description, Date date, double value) {
        this(description, date, value);
        this.id = id;
    }

    public Transaction(Parcel in) {
        this(in.readInt(), in.readString(), new Date(in.readLong()), in.readDouble());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Type getType() {
        return value > 0 ? Type.INCOME : Type.DEBIT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;

        Transaction that = (Transaction) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(id);
        }
        dest.writeString(description);
        dest.writeLong(date.getTime());
        dest.writeDouble(value);
    }

    public enum Type {INCOME, DEBIT}

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}
