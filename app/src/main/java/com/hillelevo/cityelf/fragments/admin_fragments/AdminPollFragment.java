package com.hillelevo.cityelf.fragments.admin_fragments;

import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.Constants.WebUrls;
import com.hillelevo.cityelf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hillelevo.cityelf.data.UserLocalStore;
import com.hillelevo.cityelf.webutils.AdvertsTask;
import com.hillelevo.cityelf.webutils.PollsTask;
import com.hillelevo.cityelf.webutils.PollsTask.PollsResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminPollFragment extends Fragment implements OnClickListener, PollsResponse {

  int variants = 1;

  //  private ArrayList<Poll> polls;
//  private ListView lvPolls;
  private EditText etTitle;
  private EditText etContent;
  private EditText etVariant1;
  private EditText etVariant2;
  private EditText etVariant3;
  private EditText etVariant4;
  private TextView tvAddVariant;
  private Button btnAddPoll;
  private String answerVariant1 = null;
  private String answerVariant2 = null;
  private String answerVariant3 = null;
  private String answerVariant4 = null;

  private JSONArray answerVariantArray = new JSONArray();
  private JSONObject answerVariantObject1 = new JSONObject();
  private JSONObject answerVariantObject2 = new JSONObject();
  private JSONObject answerVariantObject3 = new JSONObject();
  private JSONObject answerVariantObject4 = new JSONObject();

//  public static AdminPollFragment newInstance(ArrayList<Poll> polls) {
//    Bundle args = new Bundle();
//    args.putParcelableArrayList("Polls", polls);
//    AdminPollFragment fragment = new AdminPollFragment();
//    fragment.setArguments(args);
//    return fragment;
//  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  // Inflate the fragment layout we defined above for this fragment
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_admin_poll, container, false);
//    ArrayList<Poll> polls = getArguments().getParcelableArrayList("Polls");
//
//    ListView lvPolls = (ListView) view.findViewById(R.id.lvPollList);
//    PollListAdapter adapter = new PollListAdapter(getContext(), R.layout.list_item_poll, polls);
//    lvPolls.setAdapter(adapter);

    etTitle = (EditText) view.findViewById(R.id.etAdminPollName);
    etContent = (EditText) view.findViewById(R.id.etAdminPollContent);
    etVariant1 = (EditText) view.findViewById(R.id.etAdminPollVariant1);
    etVariant2 = (EditText) view.findViewById(R.id.etAdminPollVariant2);
    etVariant3 = (EditText) view.findViewById(R.id.etAdminPollVariant3);
    etVariant4 = (EditText) view.findViewById(R.id.etAdminPollVariant4);
    tvAddVariant = (TextView) view.findViewById(R.id.tvAdminPollAddVariant);
    btnAddPoll = (Button) view.findViewById(R.id.btnAdminPollAdd);

    tvAddVariant.setOnClickListener(this);
    btnAddPoll.setOnClickListener(this);

    return view;
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tvAdminPollAddVariant:
        if (variants == 1) {
          etVariant2.setVisibility(View.VISIBLE);
          variants++;
        } else if (variants == 2) {
          etVariant3.setVisibility(View.VISIBLE);
          variants++;
        } else if (variants == 3) {
          etVariant4.setVisibility(View.VISIBLE);
          variants++;
        } else {
          Toast.makeText(getContext(), "No more variants", Toast.LENGTH_SHORT).show();
        }
        break;

      case R.id.btnAdminPollAdd:

        String subject = etTitle.getText().toString();
        String description = etContent.getText().toString();

        switch (variants) {

          case 2:
            answerVariant1 = etVariant1.getText().toString();
            answerVariant2 = etVariant2.getText().toString();
            try {
              answerVariantObject1.put("answer", answerVariant1);
              answerVariantObject1.put("voted", 100);
              answerVariantObject2.put("answer", answerVariant2);
              answerVariantObject2.put("voted", 200);
              answerVariantArray.put(answerVariantObject1);
              answerVariantArray.put(answerVariantObject2);
            } catch (JSONException e) {
              e.printStackTrace();
            }
            break;
          case 3:
            answerVariant1 = etVariant1.getText().toString();
            answerVariant2 = etVariant2.getText().toString();
            answerVariant3 = etVariant3.getText().toString();
            try {
              answerVariantObject1.put("answer", answerVariant1);
              answerVariantObject1.put("voted", 100);
              answerVariantObject2.put("answer", answerVariant2);
              answerVariantObject2.put("voted", 200);
              answerVariantObject3.put("answer", answerVariant3);
              answerVariantObject3.put("voted", 300);
              answerVariantArray.put(answerVariantObject1);
              answerVariantArray.put(answerVariantObject2);
              answerVariantArray.put(answerVariantObject3);
            } catch (JSONException e) {
              e.printStackTrace();
            }
            break;
          case 4:
            answerVariant1 = etVariant1.getText().toString();
            answerVariant2 = etVariant2.getText().toString();
            answerVariant3 = etVariant3.getText().toString();
            answerVariant4 = etVariant4.getText().toString();
            try {
              answerVariantObject1.put("answer", answerVariant1);
              answerVariantObject1.put("voted", 100);
              answerVariantObject2.put("answer", answerVariant2);
              answerVariantObject2.put("voted", 200);
              answerVariantObject3.put("answer", answerVariant3);
              answerVariantObject3.put("voted", 300);
              answerVariantObject4.put("answer", answerVariant4);
              answerVariantObject4.put("voted", 400);
              answerVariantArray.put(answerVariantObject1);
              answerVariantArray.put(answerVariantObject2);
              answerVariantArray.put(answerVariantObject3);
              answerVariantArray.put(answerVariantObject4);
            } catch (JSONException e) {
              e.printStackTrace();
            }
            break;
        }

        JSONObject addNewPollObject = new JSONObject();

        JSONObject addressObject = new JSONObject();
        try {
          addressObject.put("id", UserLocalStore
              .loadIntFromSharedPrefs(getActivity().getApplicationContext(), Prefs.ADDRESS_1_ID));

          addNewPollObject.put("address", addressObject);
          addNewPollObject.put("subject", subject);
          addNewPollObject.put("description", description);
          addNewPollObject.put("pollsAnswers", answerVariantArray);
        } catch (JSONException e) {
          e.printStackTrace();
        }

        String jsonData = addNewPollObject.toString();

        new PollsTask(AdminPollFragment.this)
            .execute(WebUrls.ADD_NEW_POLLS_URL, Constants.POST, jsonData, UserLocalStore
                .loadStringFromSharedPrefs(getActivity().getApplicationContext(),
                    Prefs.AUTH_CERTIFICATE));
    }
  }

  @Override
  public void pollsResponse(String output) {

    if (output.isEmpty()) {
      Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
    } else {

      Toast.makeText(getContext(), "Ваше опрос был успешно добавлен", Toast.LENGTH_SHORT).show();
      etTitle.setText(null);
      etContent.setText(null);
      switch (variants) {

        case 2:
          etVariant1.setText(null);
          etVariant2.setText(null);

        case 3:
          etVariant1.setText(null);
          etVariant2.setText(null);
          etVariant3.setText(null);

        case 4:
          etVariant1.setText(null);
          etVariant2.setText(null);
          etVariant3.setText(null);
          etVariant4.setText(null);
      }
    }

  }
}