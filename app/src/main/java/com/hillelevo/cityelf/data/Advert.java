package com.hillelevo.cityelf.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Advert implements Parcelable {

  private String title;
  private String address;
  private String time;
  private String content;

  public Advert(String title, String address, String time, String content) {
    this.title = title;
    this.address = address;
    this.time = time;
    this.content = content;
  }

  public Advert(Parcel in) {
    title = in.readString();
    address = in.readString();
    time = in.readString();
    content = in.readString();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(title);
    dest.writeString(address);
    dest.writeString(time);
    dest.writeString(content);
  }

  public static final Parcelable.Creator<Advert> CREATOR = new Parcelable.Creator<Advert>() {

    public Advert createFromParcel(Parcel in) {
      return new Advert(in);
    }

    public Advert[] newArray(int size) {

      return new Advert[size];
    }

  };

}