package com.hillelevo.cityelf.activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class LoginFragment extends Fragment {

  public static final String TAG = "FragmentLoginTag";
  TextView tvRegUser;
  OnRegisterNewClickListener listner;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    listner = (OnRegisterNewClickListener) activity;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_fragment_login, container, false);

    tvRegUser = (TextView) view.findViewById(R.id.registerait_user);
    tvRegUser.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listner.onRegistraitClick();
      }
    });
    return view;
  }



  public interface OnRegisterNewClickListener{


    public void onRegistraitClick();
  }
}
