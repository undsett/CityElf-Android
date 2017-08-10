package com.hillelevo.cityelf;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import rx.Observable;
import rx.subjects.PublishSubject;

public class RxEditText {

  public static Observable<String> getTextWatcherObserv(@NonNull final EditText editText) {

    final PublishSubject<String> subject = PublishSubject.create();
    final String LOG_TAG = "appTest";

    editText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        Log.i(LOG_TAG, s.toString());
        subject.onNext(s.toString());
      }
    });
    return subject;
  }
}
