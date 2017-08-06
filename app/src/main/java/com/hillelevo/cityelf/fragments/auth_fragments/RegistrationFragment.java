package com.hillelevo.cityelf.fragments.auth_fragments;

import static com.hillelevo.cityelf.Constants.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.Constants.WebUrls;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.MainActivity;
import com.hillelevo.cityelf.data.UserLocalStore;
import com.hillelevo.cityelf.webutils.JsonMessageTask;
import com.hillelevo.cityelf.webutils.JsonMessageTask.JsonMessageResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RegistrationFragment extends Fragment implements JsonMessageResponse, OnClickListener{

  EditText etEmail, etPassword;
  Button btnRegister;

  private String email = null;
  private String password = null;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_fragment_registration, container, false);

    etEmail = (EditText) view.findViewById(R.id.etEmail);
    etPassword = (EditText) view.findViewById(R.id.etPassword);
    btnRegister = (Button) view.findViewById(R.id.btnRegister);

    btnRegister.setOnClickListener(this);

    return view;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnRegister:

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if (email.equals("")) {
          Toast.makeText(getContext(), "Введите email", Toast.LENGTH_SHORT).show();
          break;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
          Toast.makeText(getContext(), "Вы ввели неправильный email", Toast.LENGTH_SHORT).show();
          break;
        } else if (password.equals("")) {
          Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
          break;
        } else if (password.length() < 4) {
          Toast.makeText(getContext(), "Пароль должен содержать больше четырех символов",
              Toast.LENGTH_SHORT).show();
          break;
        } else {
          Toast.makeText(getContext(), "FIREBASE ID IS " + UserLocalStore.loadStringFromSharedPrefs(
              getActivity().getApplicationContext(), Prefs.FIREBASE_ID),
              Toast.LENGTH_SHORT).show();

          String bodyParams = "firebaseid=" + UserLocalStore.loadStringFromSharedPrefs(
              getActivity().getApplicationContext(), Prefs.FIREBASE_ID) + "&email=" + email +
                  "&password=" + password;

//          String bodyParams =
//              "firebaseid=" + "WEB" + "&email=" + email +
//                  "&password=" + password;
          new JsonMessageTask(RegistrationFragment.this)
              .execute(WebUrls.REGISTRATION_URL, Constants.POST, bodyParams);
          break;
        }
    }
  }

  @Override
  public void messageResponse(String output) {
    if (output == null || output.isEmpty()) {
      showMessage("Registration failed");
    } else {
      try {
        JSONObject jsonObject = new JSONObject(output);
        if (jsonObject != null) {
          JSONObject statusJsonObject = jsonObject.getJSONObject("status");

          int code = statusJsonObject.getInt("code");
          String message = statusJsonObject.getString("message");

          showMessage(message);

          if (code == 11 && message.equals("User registration OK")) {
            JSONObject userJsonObject = jsonObject.getJSONObject("user");

            int userId = userJsonObject.getInt("id");
            String email = userJsonObject.getString("email");
//            int phone = userJsonObject.getInt("phone");

            JSONArray addressJsonArray = (JSONArray) userJsonObject.get("addresses");
            JSONObject addressJsonObject = addressJsonArray.getJSONObject(0);
            int addressId = addressJsonObject.getInt("id");
            String address = addressJsonObject.getString("address");

            authenticate(userId, email, addressId, address, password);

          } else {
            showMessage(message);
          }
        } else {
          showMessage("Registration failed");
        }


      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  private void authenticate(int userId, String email, int addressId, String address, String password) {

    showMessage("All OK");

    UserLocalStore.saveIntToSharedPrefs(getActivity().getApplicationContext(), Prefs.USER_ID,
        userId);
    UserLocalStore.saveStringToSharedPrefs(getActivity().getApplicationContext(), Prefs.EMAIL,
        email);
    UserLocalStore.saveIntToSharedPrefs(getActivity().getApplicationContext(), Prefs.ADDRESS_ID,
        addressId);
    UserLocalStore.saveStringToSharedPrefs(getActivity().getApplicationContext(), Prefs.ADDRESS_1,
        address);
    UserLocalStore.saveStringToSharedPrefs(getActivity().getApplicationContext(), Prefs.PASSWORD,
        password);
    UserLocalStore.saveBooleanToSharedPrefs(getActivity().getApplicationContext(), Prefs.REGISTERED,
        true);

    showMessage("На Ваш email выслано письмо для подтверждения регистрации.");

    Intent intent = new Intent(getContext(), MainActivity.class);
    RegistrationFragment.this.startActivity(intent);
  }

  private void showMessage(String massage) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
    dialogBuilder.setMessage(massage);
    dialogBuilder.setPositiveButton("Ok", null);
    dialogBuilder.show();
  }
}

