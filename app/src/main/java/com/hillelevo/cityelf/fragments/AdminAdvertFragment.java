package com.hillelevo.cityelf.fragments;

import com.hillelevo.cityelf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminAdvertFragment extends Fragment {

//  private ArrayList<Advert> adverts;
//  private ListView lvAdverts;
  private EditText etTitle;
  private EditText etContent;
  private Button btnAddAdvert;

//  public static AdminAdvertFragment newInstance(ArrayList<Advert> adverts) {
//    Bundle args = new Bundle();
//    args.putParcelableArrayList("Adverts", adverts);
//    AdminAdvertFragment fragment = new AdminAdvertFragment();
//    fragment.setArguments(args);
//    return fragment;
//  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  // Inflate the fragment layout we defined above for this fragment
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_admin_advert, container, false);
//    adverts = getArguments().getParcelableArrayList("Adverts");

//    lvAdverts = (ListView) view.findViewById(R.id.lvAdminAdvertList);
//    AdvertListAdapter adapter = new AdvertListAdapter(getContext(), R.layout.list_item_advert,
//        adverts);
//    lvAdverts.setAdapter(adapter);

    etTitle = (EditText) view.findViewById(R.id.etAdminAdvertName);
    etContent = (EditText) view.findViewById(R.id.etAdminAdvertContent);
    btnAddAdvert = (Button) view.findViewById(R.id.btnAdminAdvertAdd);

    btnAddAdvert.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO Send new advert data to server
        Toast.makeText(getContext(), "New Advert sent", Toast.LENGTH_SHORT).show();
      }
    });

    return view;
  }

}