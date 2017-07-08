package com.hillelevo.cityelf.activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.webutils.JsonMassageTask;
import com.hillelevo.cityelf.webutils.JsonMassageTask.JsonMassageResponse;

public class LoginFragment extends Fragment implements JsonMassageResponse {

  private static String jsonResult;
  private String returnClass = "LoginFragment";

  public static final String TAG = "FragmentLoginTag";
  TextView tvRegUser;
  Button btnLogin;
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
    btnLogin = (Button) view.findViewById(R.id.btnLogin);

    btnLogin.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
//         new JsonMassageTask(AuthorizationActivity.this).execute(Constants.TEST_URL, returnClass);

        }
    });

    tvRegUser.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listner.onRegistraitClick();
      }
    });
    return view;
  }

  public static void receiveResult(String jsonOutput) {
    jsonResult = jsonOutput;
  }

  @Override
  public void massageResponse(String output) {

  }


  public interface OnRegisterNewClickListener{


    void onRegistraitClick();
  }
}
