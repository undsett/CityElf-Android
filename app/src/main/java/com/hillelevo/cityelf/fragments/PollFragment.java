package com.hillelevo.cityelf.fragments;

import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.data.Poll;
import com.hillelevo.cityelf.data.PollListAdapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PollFragment extends Fragment {

  private TextView emptyPoll;

  public static PollFragment newInstance(ArrayList<Poll> polls) {
    Bundle args = new Bundle();
    args.putParcelableArrayList("Polls", polls);
    PollFragment fragment = new PollFragment();
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
    View view = inflater.inflate(R.layout.fragment_poll, container, false);
    emptyPoll = (TextView) view.findViewById(R.id.empty_poll);
    ArrayList<Poll> polls = getArguments().getParcelableArrayList("Polls");
    if (polls.isEmpty()) {
      emptyPoll.setVisibility(View.VISIBLE);
    }

    ListView lvPolls = (ListView) view.findViewById(R.id.lvPollList);
    PollListAdapter adapter = new PollListAdapter(getContext(), R.layout.list_item_poll, polls);
    lvPolls.setAdapter(adapter);

    return view;
  }
}