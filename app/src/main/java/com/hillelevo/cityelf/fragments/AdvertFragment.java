package com.hillelevo.cityelf.fragments;

import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.data.Advert;
import com.hillelevo.cityelf.data.AdvertListAdapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class AdvertFragment extends Fragment {

  private ArrayList<Advert> adverts;
  private ListView lvAdverts;
  private TextView emptyAdverts;

  public static AdvertFragment newInstance(ArrayList<Advert> adverts) {
    Bundle args = new Bundle();
    args.putParcelableArrayList("Adverts", adverts);
    AdvertFragment fragment = new AdvertFragment();
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
    View view = inflater.inflate(R.layout.fragment_advert, container, false);
    emptyAdverts = (TextView) view.findViewById(R.id.empty_adverts);
    adverts = getArguments().getParcelableArrayList("Adverts");
    if (adverts.isEmpty()) {
      emptyAdverts.setVisibility(View.VISIBLE);
    }

    lvAdverts = (ListView) view.findViewById(R.id.lvAdvertList);
    AdvertListAdapter adapter = new AdvertListAdapter(getContext(), R.layout.list_item_advert,
        adverts);
    lvAdverts.setAdapter(adapter);

    return view;
  }
}