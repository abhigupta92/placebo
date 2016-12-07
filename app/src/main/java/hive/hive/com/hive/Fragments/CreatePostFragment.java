package hive.hive.com.hive.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import hive.hive.com.hive.Activities.MainActivity;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.LocationUtils;
import hive.hive.com.hive.Utils.MediaUtils;
import hive.hive.com.hive.Utils.UserSessionUtils;

import static hive.hive.com.hive.Utils.LocationUtils.getLatitude;
import static hive.hive.com.hive.Utils.LocationUtils.getLongitude;
import static hive.hive.com.hive.Utils.LocationUtils.locationValue;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreatePostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreatePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePostFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_IMAGE_CAPTURE = 1888;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    UserSessionUtils userSession;

    ImageView imgView;
    EditText etTitle, etContent;
    Spinner spCategory;
    Button bSubmit;

    private OnFragmentInteractionListener mListener;

    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePostFragment newInstance(String param1, String param2) {
        CreatePostFragment fragment = new CreatePostFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        userSession = MainActivity.getUserSession();
        initialiseViews(view);

        return view;
    }

    private void initialiseViews(View view) {

        imgView = (ImageView) view.findViewById(R.id.img_view_create_post);
        spCategory = (Spinner) view.findViewById(R.id.spinnerCategory_create_post);
        etTitle = (EditText) view.findViewById(R.id.etTitle_create_post);
        etContent = (EditText) view.findViewById(R.id.etContent_create_post);
        bSubmit = (Button) view.findViewById(R.id.bSubmit_create_post);

        bSubmit.setOnClickListener(this);
        imgView.setOnClickListener(this);

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
            case R.id.bSubmit_create_post:

                LocationUtils.getCurrentLocation(getActivity(), locationValue);
                Log.d("LOCATION : ", getLatitude() + " : " + getLongitude());

                Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
                ByteArrayOutputStream bao = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);

                ContentValues contentValues = new ContentValues();
                contentValues.put("profileID", userSession.getUserId());
                contentValues.put("name", userSession.getUserName());
                contentValues.put("title", etTitle.getText().toString());
                contentValues.put("content", etContent.getText().toString());
                contentValues.put("category", spCategory.getSelectedItem().toString());

                ConnectionUtils.setBitmap(bitmap);

                boolean postSuccess = ConnectionUtils.postToHive(contentValues);

                if (postSuccess) {
                    Toast.makeText(getContext(), "Posted To Hive !", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Sorry ! Something Went Wrong :| ! Try Again Maybe ? :)", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.img_view_create_post:

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
        }

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = MediaUtils.addWaterMark(imageBitmap);
            imgView.setImageBitmap(imageBitmap);
        }
    }
}
