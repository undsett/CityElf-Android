package com.hillelevo.cityelf.data;

import static com.hillelevo.cityelf.Constants.TAG;

import com.hillelevo.cityelf.R;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PollListAdapter extends ArrayAdapter<Poll> {

  private int pollResult = 0;

  public PollListAdapter(Context context, int resource, List<Poll> items) {
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
      view = inflater.inflate(R.layout.list_item_poll, null);
    }

    Poll poll = getItem(position);

    if (poll != null) {
      TextView title = (TextView) view.findViewById(R.id.tvPollTitle);
      TextView address = (TextView) view.findViewById(R.id.tvPollAddress);
      TextView duration = (TextView) view.findViewById(R.id.tvPollDuration);
      TextView time = (TextView) view.findViewById(R.id.tvPollTime);
      TextView content = (TextView) view.findViewById(R.id.tvPollContent);
      TextView peopleCount = (TextView) view.findViewById(R.id.tvPollPeopleCount);
      Button vote = (Button) view.findViewById(R.id.btnPollVote);
      RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rgPoll);
      RadioButton rButtonOne = (RadioButton) view.findViewById(R.id.rbPoll1);
      RadioButton rButtonTwo = (RadioButton) view.findViewById(R.id.rbPoll2);
      RadioButton rButtonThree = (RadioButton) view.findViewById(R.id.rbPoll3);
      RadioButton rButtonFour = (RadioButton) view.findViewById(R.id.rbPoll4);


      vote.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          Toast.makeText(getContext(), "Vote clicked", Toast.LENGTH_SHORT).show();
          Log.d(TAG, "Poll list item onClick: Vote clicked");
          //TODO: Launch request with pollResult to server
        }
      });

      radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
          switch (checkedId) {
            case R.id.rbPoll1:
              Toast.makeText(getContext(), "Variant 1 chosen", Toast.LENGTH_SHORT).show();
              Log.d(TAG, "Poll list poll item onChecked change: Variant 1 chosen");
              pollResult = 1;
              break;
            case R.id.rbPoll2:
              Toast.makeText(getContext(), "Variant 2 chosen", Toast.LENGTH_SHORT).show();
              Log.d(TAG, "Poll list poll item onChecked change: Variant 2 chosen");
              pollResult = 2;
              break;
            case R.id.rbPoll3:
              Toast.makeText(getContext(), "Variant 3 chosen", Toast.LENGTH_SHORT).show();
              Log.d(TAG, "Poll list poll item onChecked change: Variant 3 chosen");
              pollResult = 3;
              break;
            case R.id.rbPoll4:
              Toast.makeText(getContext(), "Variant 4 chosen", Toast.LENGTH_SHORT).show();
              Log.d(TAG, "Poll list poll item onChecked change: Variant 4 chosen");
              pollResult = 4;
              break;
          }
        }
      });

      if (title != null) {
        title.setText(poll.getTitle());
      }

      if (address != null) {
        address.setText(poll.getAddress());
      }

      if (duration != null) {
        duration.setText(poll.getDuration());
      }

      if (time != null) {
        time.setText(poll.getTime());
      }

      if (content != null) {
        content.setText(poll.getContent());
      }

      if (peopleCount != null) {
        StringBuilder sb = new StringBuilder(100);
        peopleCount.setText(sb
            .append("Проголосовало ")
            .append(poll.getPeopleCount())
            .append(" человек(а) из вашего дома")
            .toString());
      }

      if (rButtonOne != null && !poll.getVariant1().equals("")) {
        rButtonOne.setVisibility(View.VISIBLE);
        rButtonOne.setText(poll.getVariant1());
      }

      if (rButtonTwo != null && !poll.getVariant2().equals("")) {
        rButtonTwo.setVisibility(View.VISIBLE);
        rButtonTwo.setText(poll.getVariant2());
      }

      if (rButtonThree != null && !poll.getVariant3().equals("")) {
        rButtonThree.setVisibility(View.VISIBLE);
        rButtonThree.setText(poll.getVariant3());
      }

      if (rButtonFour != null && !poll.getVariant4().equals("")) {
        rButtonFour.setVisibility(View.VISIBLE);
        rButtonFour.setText(poll.getVariant4());
      }
    }

    return view;
  }

}