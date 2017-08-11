package com.hillelevo.cityelf.fragments.auth_fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.WebUrls;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.MainActivity;
import com.hillelevo.cityelf.webutils.JsonMessageTask;
import com.hillelevo.cityelf.webutils.JsonMessageTask.JsonMessageResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class ForgotPasswordFragment extends Fragment implements JsonMessageResponse,
    OnClickListener {

  EditText etForgotEmail;
  Button btnRestorePass;

  private String email = null;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_fragment_forgot_password, container, false);

    etForgotEmail = (EditText) view.findViewById(R.id.etForgotEmail);
    btnRestorePass = (Button) view.findViewById(R.id.btnRestorePass);

    btnRestorePass.setOnClickListener(this);
    return view;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnRestorePass:

        email = etForgotEmail.getText().toString();

        if (email.equals("")) {
          Toast.makeText(getContext(), "Введите email", Toast.LENGTH_SHORT).show();
          break;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
          Toast.makeText(getContext(), "Вы ввели неправильный email", Toast.LENGTH_SHORT).show();
          break;
        } else {
          String bodyParams = "";
          new JsonMessageTask(ForgotPasswordFragment.this).execute(WebUrls.FORGOT_PASSWORD_URL + email, Constants.POST, bodyParams);
          break;
        }
    }
  }

  @Override
  public void messageResponse(String output) {

    if (output == null || output.isEmpty() || output == "") {
      showMessage("На указанный Вами e-mail будет отправлено письмо со ссылкой на страницу сброса пароля");
    } else {
//      try {
//        JSONObject jsonObject = new JSONObject(output);
//        if (jsonObject != null) {
//          int code = jsonObject.getInt("code");
//          String message = jsonObject.getString("message");
//
//          showMessage(message + code);
//
//          if (code == 11 && message.equals("User registration OK")) {
//
//          showMessage(message);
//
//          } else {
//            showMessage(message);
//
//          }
//        } else {
//          showMessage("Registration failed");
//        }
//      } catch (JSONException e) {
//        e.printStackTrace();
//      }
    }
    Intent intent = new Intent(getContext(), MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
   ForgotPasswordFragment.this.startActivity(intent);
  }

  private void showMessage(String massage) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
    dialogBuilder.setMessage(massage);
    dialogBuilder.setPositiveButton("Ok", null);
    dialogBuilder.show();
  }
}

