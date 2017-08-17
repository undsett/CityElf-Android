package com.hillelevo.cityelf.data;

import com.hillelevo.cityelf.R;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NotificationListAdapter extends ArrayAdapter<Notification> {

  public NotificationListAdapter(Context context, int resource, List<Notification> items) {
    super(context, resource, items);
  }

  @SuppressLint("InflateParams")
  @NonNull
  @Override
  public View getView(int position, View convertView, @NonNull ViewGroup parent) {

    View view = convertView;

    if (view == null) {
      LayoutInflater inflater;
      inflater = LayoutInflater.from(getContext());
      view = inflater.inflate(R.layout.list_item_notification, null);
    }

    Notification notification = getItem(position);

    if (notification != null) {
      TextView title = (TextView) view.findViewById(R.id.tvNotifTitle);
      TextView address = (TextView) view.findViewById(R.id.tvNotifAddress);
      TextView duration = (TextView) view.findViewById(R.id.tvNotifDuration);
      TextView time = (TextView) view.findViewById(R.id.tvNotifTime);
      TextView content = (TextView) view.findViewById(R.id.tvNotifContent);

      if (title != null) {
        title.setText(notification.getTitle());
      }

      if (address != null) {
        address.setText(notification.getAddress());
      }

      if (duration != null) {
        duration.setText(notification.getDuration());
      }

      if (time != null) {
        time.setText(notification.getTime());
      }

      if (content != null) {
        content.setText(notification.getContent());
      }

      if (notification.getIconType() == 1) {
        content.append("Об отключении сообщил жилец дома");
      }
    }

    return view;
  }

}