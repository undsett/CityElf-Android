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
import android.widget.TextView;
import android.widget.Toast;

public class AdminPollFragment extends Fragment implements OnClickListener {

  int variants = 1;

  //  private ArrayList<Poll> polls;
//  private ListView lvPolls;
  private EditText etTitle;
  private EditText etContent;
  private EditText etVariant1;
  private EditText etVariant2;
  private EditText etVariant3;
  private EditText etVariant4;
  private TextView tvAddVariant;
  private Button btnAddPoll;

//  public static AdminPollFragment newInstance(ArrayList<Poll> polls) {
//    Bundle args = new Bundle();
//    args.putParcelableArrayList("Polls", polls);
//    AdminPollFragment fragment = new AdminPollFragment();
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
    View view = inflater.inflate(R.layout.fragment_admin_poll, container, false);
//    ArrayList<Poll> polls = getArguments().getParcelableArrayList("Polls");
//
//    ListView lvPolls = (ListView) view.findViewById(R.id.lvPollList);
//    PollListAdapter adapter = new PollListAdapter(getContext(), R.layout.list_item_poll, polls);
//    lvPolls.setAdapter(adapter);

    etTitle = (EditText) view.findViewById(R.id.etAdminPollName);
    etContent = (EditText) view.findViewById(R.id.etAdminPollContent);
    etVariant1 = (EditText) view.findViewById(R.id.etAdminPollVariant1);
    etVariant2 = (EditText) view.findViewById(R.id.etAdminPollVariant2);
    etVariant3 = (EditText) view.findViewById(R.id.etAdminPollVariant3);
    etVariant4 = (EditText) view.findViewById(R.id.etAdminPollVariant4);
    tvAddVariant = (TextView) view.findViewById(R.id.tvAdminPollAddVariant);
    btnAddPoll = (Button) view.findViewById(R.id.btnAdminPollAdd);

    tvAddVariant.setOnClickListener(this);
    btnAddPoll.setOnClickListener(this);

    return view;
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tvAdminPollAddVariant:
        if(variants == 1) {
          etVariant2.setVisibility(View.VISIBLE);
          variants++;
        }
        else if(variants == 2) {
          etVariant3.setVisibility(View.VISIBLE);
          variants++;
        }
        else if(variants == 3) {
          etVariant4.setVisibility(View.VISIBLE);
          variants++;
        }
        else {
          Toast.makeText(getContext(), "No more variants", Toast.LENGTH_SHORT).show();
        }
        break;

      case R.id.btnAdminPollAdd:
        //TODO Send new poll data to server
        Toast.makeText(getContext(), "New Poll sent", Toast.LENGTH_SHORT).show();
    }
  }
}