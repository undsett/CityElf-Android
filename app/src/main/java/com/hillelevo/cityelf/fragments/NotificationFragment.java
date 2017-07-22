package com.hillelevo.cityelf.fragments;

import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.data.Notification;
import com.hillelevo.cityelf.data.NotificationListAdapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NotificationFragment extends Fragment {

  public static NotificationFragment newInstance(ArrayList<Notification> notifications) {
    Bundle args = new Bundle();
    args.putParcelableArrayList("Notifications", notifications);
    NotificationFragment fragment = new NotificationFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  // Inflate the fragment layout we defined above for this fragment
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_notification, container, false);
    ArrayList<Notification> notifications = getArguments().getParcelableArrayList("Notifications");

    ListView lvNotifications = (ListView) view.findViewById(R.id.lvNotificationList);
    NotificationListAdapter adapter = new NotificationListAdapter(getContext(),
        R.layout.list_item_notification, notifications);
    lvNotifications.setAdapter(adapter);

    return view;
  }
}