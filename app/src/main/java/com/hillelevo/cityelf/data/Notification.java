package com.hillelevo.cityelf.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements Parcelable {

  private String title;
  private String address;
  private String duration;
  private String time;
  private String content;
  private int iconType;

  public Notification(String title, String address, String duration, String time,
      String content, int iconType) {
    this.title = title;
    this.address = address;
    this.duration = duration;
    this.time = time;
    this.content = content;
    this.iconType = iconType;
  }

  public Notification(Parcel in) {
    title = in.readString();
    address = in.readString();
    duration = in.readString();
    time = in.readString();
    content = in.readString();
    iconType = in.readInt();
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

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
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

  public int getIconType() {
    return iconType;
  }

  public void setIconType(int iconType) {
    this.iconType = iconType;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(title);
    dest.writeString(address);
    dest.writeString(duration);
    dest.writeString(time);
    dest.writeString(content);
    dest.writeInt(iconType);
  }

  public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {

    public Notification createFromParcel(Parcel in) {
      return new Notification(in);
    }

    public Notification[] newArray(int size) {

      return new Notification[size];
    }

  };

}
