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

public class AdvertListAdapter extends ArrayAdapter<Advert> {

  public AdvertListAdapter(Context context, int resource, List<Advert> items) {
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
      view = inflater.inflate(R.layout.list_item_advert, null);
    }

    Advert advert = getItem(position);

    if (advert != null) {
      TextView title = (TextView) view.findViewById(R.id.tvAdvertTitle);
      TextView address = (TextView) view.findViewById(R.id.tvAdvertAddress);
      TextView time = (TextView) view.findViewById(R.id.tvAdvertTime);
      TextView content = (TextView) view.findViewById(R.id.tvAdvertContent);

      if (title != null) {
        title.setText(advert.getTitle());
      }

      if (address != null) {
        address.setText(advert.getAddress());
      }

      if (time != null) {
        time.setText(advert.getTime());
      }

      if (content != null) {
        content.setText(advert.getContent());
      }
    }

    return view;
  }

}