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
import android.widget.Toast;
import com.hillelevo.cityelf.activities.setting_activity.SettingsActivity;
import com.hillelevo.cityelf.data.UserLocalStore;
import com.hillelevo.cityelf.webutils.AdvertsTask;
import com.hillelevo.cityelf.webutils.AdvertsTask.AdvertsResponse;
import com.hillelevo.cityelf.webutils.JsonMessageTask;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminAdvertFragment extends Fragment implements OnClickListener, AdvertsResponse {

  //  private ArrayList<Advert> adverts;
//  private ListView lvAdverts;
  private EditText etTitle;
  private EditText etContent;
  private Button btnAddAdvert;
  private String subject = null;
  private String description = null;

//  public static AdminAdvertFragment newInstance(ArrayList<Advert> adverts) {
//    Bundle args = new Bundle();
//    args.putParcelableArrayList("Adverts", adverts);
//    AdminAdvertFragment fragment = new AdminAdvertFragment();
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
    View view = inflater.inflate(R.layout.fragment_admin_advert, container, false);
//    adverts = getArguments().getParcelableArrayList("Adverts");

//    lvAdverts = (ListView) view.findViewById(R.id.lvAdminAdvertList);
//    AdvertListAdapter adapter = new AdvertListAdapter(getContext(), R.layout.list_item_advert,
//        adverts);
//    lvAdverts.setAdapter(adapter);

    etTitle = (EditText) view.findViewById(R.id.etAdminAdvertName);
    etContent = (EditText) view.findViewById(R.id.etAdminAdvertContent);
    btnAddAdvert = (Button) view.findViewById(R.id.btnAdminAdvertAdd);

    btnAddAdvert.setOnClickListener(this);

    return view;
  }

      @Override
      public void onClick(View view) {

        subject = etTitle.getText().toString();
        description = etContent.getText().toString();

        JSONObject addNewAdvertObject = new JSONObject();

        JSONObject addressObject = new JSONObject();
        try {
          addressObject.put("id", UserLocalStore
              .loadIntFromSharedPrefs(getActivity().getApplicationContext(), Prefs.ADDRESS_1_ID));

          addNewAdvertObject.put("address", addressObject);
          addNewAdvertObject.put("subject", subject);
          addNewAdvertObject.put("description", description);
        } catch (JSONException e) {
          e.printStackTrace();
        }

        String jsonData = addNewAdvertObject.toString();

        new AdvertsTask(AdminAdvertFragment.this)
            .execute(WebUrls.ADD_NEW_ADVERTS_URL, Constants.POST, jsonData, UserLocalStore
                .loadStringFromSharedPrefs(getActivity().getApplicationContext(), Prefs.AUTH_CERTIFICATE));
      }

  @Override
  public void advertsResponse(String output) {

    if (output.isEmpty()){
      Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
    }else{
      Toast.makeText(getContext(), "Ваше объявление было успешно добавленно", Toast.LENGTH_SHORT).show();
      etTitle.setText(null);
      etContent.setText(null);
    }

  }
}