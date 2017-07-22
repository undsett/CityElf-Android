package com.hillelevo.cityelf.activities.authorization;


import static com.hillelevo.cityelf.Constants.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.WebUrls;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.MainActivity;
import com.hillelevo.cityelf.webutils.JsonMessageTask;
import com.hillelevo.cityelf.webutils.JsonMessageTask.JsonMessageResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment implements JsonMessageResponse, OnClickListener {

  EditText etLogEmail, etLogPassword;
  TextView tvRegisteraitUser;
  Button btnLogin;
  OnRegisteraitNewClickListener listner;

  UserLocalStore userLocalStore;
  User returnedUser = new User(null, null, 0, null, null);
  String responseMessage;


  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    listner = (OnRegisteraitNewClickListener) activity;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_fragment_login, container, false);

    etLogEmail = (EditText) view.findViewById(R.id.etLogEmail);
    etLogPassword = (EditText) view.findViewById(R.id.etLogPassword);
    tvRegisteraitUser = (TextView) view.findViewById(R.id.tvRegisteraitUser);
    btnLogin = (Button) view.findViewById(R.id.btnLogin);

    btnLogin.setOnClickListener(this);
    tvRegisteraitUser.setOnClickListener(this);

    userLocalStore = new UserLocalStore(getContext());
    return view;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnLogin:

        String email = etLogEmail.getText().toString();
        String password = etLogPassword.getText().toString();

//        returnedUser = new User(userLocalStore.getStoredToken(),email, password);

        String bodyParams = "email=" + email + "&password=" + password;

        if (!email.isEmpty() && !password.isEmpty()) {
//          try {
//            new JsonMessageTask(LoginFragment.this).execute(WebUrls.AUTHORIZATION_URL, Constants.POST, email, password);
            new JsonMessageTask(LoginFragment.this).execute(WebUrls.AUTHORIZATION_URL, Constants.POST, bodyParams);
//            checkResponse(responseMessage);
//          } catch (InterruptedException e) {
//            e.printStackTrace();
//          } catch (ExecutionException e) {
//            e.printStackTrace();
//          }

//          new JsonMassageTask(this).execute(Constants.AUTHORIZATION_URL, Constants.POST, user.authCertificate);
          break;
        } else if (email.equals("")) {
          Toast.makeText(getContext(), "Please enter email", Toast.LENGTH_SHORT).show();
          break;
        } else if (password.equals("")) {
          Toast.makeText(getContext(), "Please enter password", Toast.LENGTH_SHORT).show();
          break;
        }


      case R.id.tvRegisteraitUser:
        listner.onRegistraitClick();
        break;
    }
  }

  @Override
  public void messageResponse(String output) {
    checkResponse(output);
  }

  public void checkResponse(String output){
    if (output == null || output.isEmpty()) {

      showErrorMessage("LoggedIn failed");
    } else {try {
      JSONObject jsonObject = new JSONObject(output);
      if (jsonObject != null) {

        int code = jsonObject.getInt("code");
        String message = jsonObject.getString("message");

        showErrorMessage(message);

        if (code == 33 && message.equals("Your login and password is correct")) {

          authenticate(returnedUser);

        }else{
          showErrorMessage(message);
          returnedUser = null;
        }
      } else {
        showErrorMessage("Incorrect user details");
        returnedUser = null;
      }


    } catch (JSONException e) {
      e.printStackTrace();
    }

    }

  }

  private void authenticate(User returnedUser) {

    Log.d(TAG, returnedUser.getEmail() + " Logged In");

    showErrorMessage("All OK");

    userLocalStore.storeUserData(returnedUser);
    userLocalStore.setUserLoggedIn(true);

    Intent intent = new Intent(getContext(), MainActivity.class);
    LoginFragment.this.startActivity(intent);
  }

  private void showErrorMessage(String message) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
    dialogBuilder.setMessage(message);
    dialogBuilder.setPositiveButton("Ok", null);
    dialogBuilder.show();
  }


  public interface OnRegisteraitNewClickListener {

    void onRegistraitClick();
  }
}
