package com.hillelevo.cityelf.fragments;

import com.hillelevo.cityelf.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class BottomDialogFragment extends DialogFragment {

  boolean isRegistered;

  /**
   * Create a new instance of MyDialogFragment
   */
  static BottomDialogFragment newInstance(boolean reg) {
    BottomDialogFragment f = new BottomDialogFragment();

    // Supply num input as an argument.
    Bundle args = new Bundle();
    args.putBoolean("registered", reg);
    f.setArguments(args);

    return f;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    isRegistered = getArguments().getBoolean("num");

    if (isRegistered) {

    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_dialog, container, false);

    // Watch for button clicks.
//    Button button = (Button) v.findViewById(R.id.);
//    button.setOnClickListener(new OnClickListener() {
//      public void onClick(View v) {
        // When button is clicked, call up to owning activity.
//        ((BottomDialogFragment) getActivity()).showDialog();
//      }
//    });

    return v;
  }

}
