package hive.hive.com.hive.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hive.hive.com.hive.GSONEntities.ClosestHiveDetail;
import hive.hive.com.hive.GSONEntities.UserSettingsDetails;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Adapters.HiveSelectionCustomListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean haveGeoLocationDetails = false;
    EditText etName, etAbout;
    private String gender;
    Button bSubmit;
    public static View view;
    private static long selectedHiveId;
    private static long selectedClusterId;

    private List<ClosestHiveDetail> listOfHives = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    PlaceAutocompleteFragment autocompleteFragment;


    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        initializeViews(view);

        setViewsInformation(view);

        return view;
    }

    private void setViewsInformation(View view) {
        UserSettingsDetails userSettingInformation = ConnectionUtils.getUserSettings();
        etName.setText(userSettingInformation.getUserName());
        etAbout.setText(userSettingInformation.getUserAbout());
        if (userSettingInformation.getUserGender().contentEquals("1")) {
            RadioButton radioMale = (RadioButton) view.findViewById(R.id.radio_male);
            radioMale.setChecked(true);
        } else {
            RadioButton radioFemale = (RadioButton) view.findViewById(R.id.radio_female);
            radioFemale.setChecked(true);
        }
        String placeID = userSettingInformation.getUserPlaceID();

        String geoTag = ConnectionUtils.userGeoLocation(placeID);

        autocompleteFragment.setText(geoTag);

    }

    private void initializeViews(View view) {

        autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                String placeID = place.getId();


                try {
                    haveGeoLocationDetails = ConnectionUtils.getSelectedGeolocationDetails(placeID, place.getLatLng(), String.valueOf(place.getName()));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (haveGeoLocationDetails) {
                    listOfHives = ConnectionUtils.getExistingClosestHives();
                    if(listOfHives.size() == 0){
                        ClosestHiveDetail closestHiveDetail = new ClosestHiveDetail(1,0, String.valueOf(place.getName()),0);
                        listOfHives.add(closestHiveDetail);
                    }
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                    builderSingle.setIcon(R.drawable.ic_cast_light);
                    builderSingle.setTitle("Select A Hive :");

                    if (listOfHives != null) {
                        Log.d("Not Null","hives");

                        final HiveSelectionCustomListAdapter adapter = new HiveSelectionCustomListAdapter(getActivity().getApplicationContext(), R.layout.closest_hive_detail, listOfHives);

                        builderSingle.setNegativeButton(
                                "cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        builderSingle.setAdapter(
                                adapter,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ClosestHiveDetail strName = (ClosestHiveDetail) adapter.getItem(which);
                                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                                getContext());
                                        selectedHiveId = strName.getHiveID();
                                        selectedClusterId = strName.getClusterId();
                                        builderInner.setMessage(String.valueOf(strName.getHiveID()));
                                        builderInner.setTitle("Your Selected Item is");
                                        builderInner.setPositiveButton(
                                                "Ok",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        builderInner.show();
                                    }
                                });
                        builderSingle.show();
                    }
                }

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("PLACES", "An error occurred: " + status);
            }
        });

        etName = (EditText) view.findViewById(R.id.etProfileName);
        etAbout = (EditText) view.findViewById(R.id.etProfileAbout);

        bSubmit = (Button) view.findViewById(R.id.button_edit_profile_submit);
        bSubmit.setOnClickListener(this);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.gender_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()

                                              {
                                                  @Override
                                                  public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                      onGenderSelected(checkedId);
                                                  }
                                              }

        );

        gender = null;

    }

    public void onGenderSelected(int genderSelected) {

        switch (genderSelected) {
            case R.id.radio_male:
                gender = "Male";
                Log.d("Gender Selected", String.valueOf(String.valueOf(gender)));
                break;
            case R.id.radio_female:
                gender = "Female";
                Log.d("Gender Selected", String.valueOf(String.valueOf(gender)));
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_edit_profile_submit:
                String name = etName.getText().toString();
                String about = etAbout.getText().toString();
                if (gender != null)
                    ConnectionUtils.registerSelectedGeolocationDetails(selectedHiveId, selectedClusterId, name, about, gender);
                else {
                    Toast.makeText(getContext(), "Please select your Gender !", Toast.LENGTH_LONG).show();
                }
                break;

        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other layouts.fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
