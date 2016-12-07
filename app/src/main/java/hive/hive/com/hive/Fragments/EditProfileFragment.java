package hive.hive.com.hive.Fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hive.hive.com.hive.Activities.MainActivity;
import hive.hive.com.hive.Adapters.HiveSelectionCustomListAdapter;
import hive.hive.com.hive.GSONEntities.ClosestHiveDetail;
import hive.hive.com.hive.GSONEntities.UserSettingsDetails;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.UserSessionUtils;

import static hive.hive.com.hive.Utils.Enums.FACEBOOK_LOGIN;
import static hive.hive.com.hive.Utils.Enums.HIVE_LOGIN;


public class EditProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_IMAGE_CAPTURE = 1888;

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int PIC_CROP = 2;
    static UserSessionUtils userSession;

    private String mParam1;
    private String mParam2;
    private boolean haveGeoLocationDetails = false;
    EditText etName, etAbout;
    ImageView imgProfile;
    private String gender;
    Button bSubmit, bLogout;
    public static View view;
    private static long selectedHiveId;
    private static long selectedClusterId;

    private List<ClosestHiveDetail> listOfHives = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    PlaceAutocompleteFragment autocompleteFragment;

    CharSequence imageOptions[] = new CharSequence[]{"Take Photo", "Upload Photo"};


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


        userSession = MainActivity.getUserSession();
        autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        bLogout = (Button) view.findViewById(R.id.edit_profile_logout);
        bLogout.setOnClickListener(this);

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
                    if (listOfHives.size() == 0) {
                        ClosestHiveDetail closestHiveDetail = new ClosestHiveDetail(1, 0, String.valueOf(place.getName()), 0);
                        listOfHives.add(closestHiveDetail);
                    }
                    if (listOfHives.size() == 1 && listOfHives.get(0).getHiveRegion().contentEquals("notset")) {
                        listOfHives.get(0).setHiveRegion(String.valueOf(place.getName()));
                    }
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                    builderSingle.setIcon(R.drawable.ic_cast_light);
                    builderSingle.setTitle("Select A Hive :");

                    if (listOfHives != null) {

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
                                        ClosestHiveDetail hiveDetail = (ClosestHiveDetail) adapter.getItem(which);
                                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                                getContext());
                                        selectedHiveId = hiveDetail.getHiveID();
                                        selectedClusterId = hiveDetail.getClusterId();
                                        builderInner.setMessage(String.valueOf(hiveDetail.getHiveRegion()));
                                        builderInner.setTitle("Your Selected Hive is : ");
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
        imgProfile = (ImageView) view.findViewById(R.id.edit_profile_profile_pic);
        imgProfile.setOnClickListener(this);

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
            case R.id.edit_profile_logout:
                if (userSession.getUserDetails().getKEY_LOGIN_TYPE() == FACEBOOK_LOGIN.getVal()) {
                    LoginManager.getInstance().logOut();
                } else if (userSession.getUserDetails().getKEY_LOGIN_TYPE() == HIVE_LOGIN.getVal()) {

                }
                userSession.logoutUser();
                break;

            case R.id.edit_profile_profile_pic:
                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setIcon(R.drawable.ic_cast_light);
                builderSingle.setTitle("Select Method :");
                builderSingle.setNegativeButton(
                        "cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setItems(imageOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                //takePictureIntent.putExtra("crop", "true");
                                takePictureIntent.putExtra("aspectX", 1);
                                takePictureIntent.putExtra("aspectY", 1);
                                takePictureIntent.putExtra("outputX", 50);
                                takePictureIntent.putExtra("outputY", 50);
                                takePictureIntent.putExtra("return-data", true);
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        } else if (which == 1) {
                            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            pickImageIntent.setType("image/*");
                            //pickImageIntent.putExtra("crop", "true");
                            pickImageIntent.putExtra("outputX", 200);
                            pickImageIntent.putExtra("outputY", 200);
                            pickImageIntent.putExtra("aspectX", 1);
                            pickImageIntent.putExtra("aspectY", 1);
                            pickImageIntent.putExtra("scale", true);
                            //File profileImgFile = createNewFile("CROP_");
                            /*try {
                                profileImgFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
                            /*pickImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(profileImgFile));
                            pickImageIntent.putExtra("outputFormat",

                                    Bitmap.CompressFormat.JPEG.toString());*/
                            startActivityForResult(pickImageIntent, RESULT_LOAD_IMAGE);
                        }
                    }
                });
                builderSingle.show();
                break;


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //imageBitmap = MediaUtils.addWaterMark(imageBitmap);
            imgProfile.setImageBitmap(imageBitmap);
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            imgProfile.setImageBitmap(bitmap);

            /*Intent intent = new Intent("com.android.camera.action.CROP");
            Uri imageUri = Uri.fromFile(new File(picturePath));
            File profileImgFile = createNewFile("CROP_");
            try {
                profileImgFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent.setDataAndType(imageUri, "image*//*");
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(profileImgFile));
            intent.putExtra("output", Uri.fromFile(profileImgFile));
            intent.putExtra("return-data",true);
            PackageManager packageManager = getActivity().getPackageManager();
            List<ResolveInfo> listGall = packageManager.queryIntentActivities(intent, 0);
            Intent cropIntent = null;
            for (ResolveInfo res : listGall) {
                cropIntent = new Intent(intent);
                cropIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                break;
            }
            startActivityForResult(cropIntent, PIC_CROP);*/
        } else if (requestCode == PIC_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                imgProfile.setImageBitmap(selectedBitmap);
                imgProfile.setScaleType(ImageView.ScaleType.FIT_XY);
                //boolean uploadedProfilePic = ConnectionUtils.setUserProfilePic(selectedBitmap);
            }
        }


    }

    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setClassName("com.android.camera", "com.android.camera.CropImage");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {

        }
    }

    private File createNewFile(String prefix) {
        if (prefix == null || "".equalsIgnoreCase(prefix)) {
            prefix = "IMG_";
        }
        File newDirectory = new File(Environment.getExternalStorageDirectory() + "/hive_pics/");
        if (!newDirectory.exists()) {
            if (newDirectory.mkdir()) {
                Log.d("File created", "File Created");
            }
        }
        File file = new File(newDirectory, (prefix + System.currentTimeMillis() + ".jpg"));
        if (file.exists()) {
            //this wont be executed
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
