package com.hillelevo.cityelf;


public class TimeUtils {

  public static String getDate(String time) {
    StringBuilder sb = new StringBuilder();
    sb.append(time);
    sb.charAt(10);
    sb.replace(sb.lastIndexOf("T"), sb.length(), "");
    for (int i=0; i<sb.length(); i++) {
      if (sb.charAt(i) == '-') {
        sb.setCharAt(i, '.');
      }
    }
    return sb.toString();
  }

  public static String getTime(String time) {
    StringBuilder sb = new StringBuilder();
    sb.append(time);
    sb.replace(0, sb.lastIndexOf("T") + 1, "");
    return sb.toString();
  }

  public static String getHours(String time) {
    StringBuilder sb = new StringBuilder();
    sb.append(time);
    sb.replace(sb.indexOf(":"), sb.length(), "");
    return sb.toString();
  }

  public static String getHoursMinutes(String time) {
    StringBuilder sb = new StringBuilder();
    sb.append(time);
    sb.replace(sb.lastIndexOf(":"), sb.length(), "");
    return sb.toString();
  }

  public static String getDuration (String start, String end) {
    StringBuilder sb = new StringBuilder();
    sb.append(getHoursMinutes(start)).append(" - ").append(getHoursMinutes(end));
    return sb.toString();
  }

}
