package com.hillelevo.cityelf.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Poll implements Parcelable {

  private String title;
  private String address;
  private String duration;
  private String time;
  private String content;
  private String variant1;
  private String variant2;
  private String variant3;
  private String variant4;
  private int peopleCount;

  public Poll(String title, String address, String duration, String time, String content,
      String variant1, String variant2, String variant3, String variant4, int peopleCount) {
    this.title = title;
    this.address = address;
    this.duration = duration;
    this.time = time;
    this.content = content;
    this.variant1 = variant1;
    this.variant2 = variant2;
    this.variant3 = variant3;
    this.variant4 = variant4;
    this.peopleCount = peopleCount;
  }

  public Poll(Parcel in) {
    title = in.readString();
    address = in.readString();
    duration = in.readString();
    time = in.readString();
    content = in.readString();
    variant1 = in.readString();
    variant2 = in.readString();
    variant3 = in.readString();
    variant4 = in.readString();
    peopleCount = in.readInt();
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

  public String getVariant1() {
    return variant1;
  }

  public void setVariant1(String variant1) {
    this.variant1 = variant1;
  }

  public String getVariant2() {
    return variant2;
  }

  public void setVariant2(String variant2) {
    this.variant2 = variant2;
  }

  public String getVariant3() {
    return variant3;
  }

  public void setVariant3(String variant3) {
    this.variant3 = variant3;
  }

  public String getVariant4() {
    return variant4;
  }

  public void setVariant4(String variant4) {
    this.variant4 = variant4;
  }

  public int getPeopleCount() {
    return peopleCount;
  }

  public void setPeopleCount(int peopleCount) {
    this.peopleCount = peopleCount;
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
    dest.writeString(variant1);
    dest.writeString(variant2);
    dest.writeString(variant3);
    dest.writeString(variant4);
    dest.writeInt(peopleCount);
  }

  public static final Parcelable.Creator<Poll> CREATOR = new Parcelable.Creator<Poll>() {

    public Poll createFromParcel(Parcel in) {
      return new Poll(in);
    }

    public Poll[] newArray(int size) {

      return new Poll[size];
    }

  };

}
