package com.hillelevo.cityelf.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hillelevo.cityelf.R;


public class RegistrationFragment extends Fragment {

  public static final String TAG = "FragmentRegistrationTag";

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.layout_fragment_registration, container, false);
  }
}

