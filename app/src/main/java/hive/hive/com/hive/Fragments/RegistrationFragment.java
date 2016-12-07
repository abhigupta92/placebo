package hive.hive.com.hive.Fragments;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hive.hive.com.hive.Activities.MainActivity;
import hive.hive.com.hive.ConnectionResults.RegisterUserResultDetails;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.EmailValidator;
import hive.hive.com.hive.Utils.PasswordValidator;
import hive.hive.com.hive.Utils.StringUtils;
import hive.hive.com.hive.Utils.UserSessionUtils;

import static hive.hive.com.hive.Utils.Enums.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    static View view;

    private int mYear, mMonth, mDay;
    EditText etName, etEmail, etPassword;
    TextView tvName, tvDOB, tvEmail, tvPassword, etDOB, tvHometown;
    Button bRegister;
    PlaceAutocompleteFragment etHometown;
    EmailValidator emailValidator;
    PasswordValidator passwordValidator;

    UserSessionUtils session;

    private String selectedHometown;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_registration, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        initializeViews(view);

        return view;

    }

    private void initializeViews(View view) {

        emailValidator = new EmailValidator();
        passwordValidator = new PasswordValidator();

        etDOB = (TextView) view.findViewById(R.id.etDOB_registration);
        etName = (EditText) view.findViewById(R.id.etName_registration);
        etEmail = (EditText) view.findViewById(R.id.etEmail_registration);
        etPassword = (EditText) view.findViewById(R.id.etPassword_registration);
        etHometown = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.etHometown_registration);

        tvDOB = (TextView) view.findViewById(R.id.tvDOB_registration);
        tvName = (TextView) view.findViewById(R.id.tvName_registration);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail_registration);
        tvPassword = (TextView) view.findViewById(R.id.tvPassword_registration);
        tvHometown = (TextView) view.findViewById(R.id.tvHometown_registration);

        etHometown.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                etHometown.setText(place.getName());
                selectedHometown = place.getName().toString();
            }

            @Override
            public void onError(Status status) {

            }
        });

        etDOB.setOnClickListener(this);

        bRegister = (Button) view.findViewById(R.id.bRegister_registration);

        bRegister.setOnClickListener(this);

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
            case R.id.bRegister_registration:
                MainActivity.hideSoftKeyboard(getActivity());
                clearErrors();
                if (checkForValidFields()) {

                    Log.d("Hometown Selected : ", selectedHometown);
                    ContentValues cvRegDetails = setUserDetails();

                    RegisterUserResultDetails result = ConnectionUtils.registerUser(cvRegDetails);
                    if (result != null) {
                        if (result.getResult().contentEquals("success")) {
                            session = MainActivity.getUserSession();
                            session.createUserLoginSession(result.getUserId(), result.getSalt(), Long.parseLong(HIVE_LOGIN.getVal()));
                            ConnectionUtils.setUserSessionDetails(session.getUserDetails());
                            EditProfileFragment editProfileFragment = new EditProfileFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, editProfileFragment, EDITPROFILEFRAGMENT.name());
                            transaction.addToBackStack(EDITPROFILEFRAGMENT.name());
                            transaction.commit();
                        } else if (result.getResult().contentEquals("fail")) {
                            if (result.getErrCd().contentEquals("DUPLICATE")) {
                                if (result.getErrMsg().contentEquals("EMAIL")) {
                                    tvEmail.setTextColor(Color.RED);
                                    tvEmail.setText("Email" + Html.fromHtml("<sup>*</sup>") + " : ");
                                } else if (result.getErrMsg().contentEquals("SALT")) {

                                }
                            }
                        }
                    }
                }

                break;

            case R.id.etDOB_registration:

                DatePickerDialog datePickerDialog = getDatePicker();
                datePickerDialog.show();
                break;
        }
    }

    private boolean checkForValidFields() {

        boolean validFields = true;

        if (etName.length() == 0) {
            tvName.setTextColor(Color.RED);
            validFields = false;
            return validFields;
        }
        if (!emailValidator.validate(etEmail.getText().toString())) {
            tvEmail.setTextColor(Color.RED);
            validFields = false;
            return validFields;
        }
        if (!passwordValidator.validate(etPassword.getText().toString())) {
            validFields = false;
            tvPassword.setTextColor(Color.RED);
            showPasswordErrorMessage();
            return validFields;
        }
        if (etDOB.getText().toString().length() == 0) {
            tvDOB.setTextColor(Color.RED);
            validFields = false;
            return validFields;
        }
        if (selectedHometown == null || selectedHometown.length() == 0) {
            validFields = false;
            tvHometown.setTextColor(Color.RED);
            return validFields;
        }

        return validFields;
    }

    private void showPasswordErrorMessage() {

        String password = etPassword.getText().toString();
        if (password.length() < 6) {
            Snackbar.make(getActivity().getCurrentFocus(), "Password must be 6 characters long at least", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        boolean hasUppercase = !password.equals(password.toLowerCase());
        if (!hasUppercase) {
            Snackbar.make(getActivity().getCurrentFocus(), "Password must contain Uppercase", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        if (password.contains("*")) {
            Snackbar.make(getActivity().getCurrentFocus(), "Password cannot contain * symbol", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        if (!containsDigit(password)) {
            Snackbar.make(getActivity().getCurrentFocus(), "Password must contain a number", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        boolean hasLowercase = !password.equals(password.toUpperCase());
        if (!hasLowercase) {
            Snackbar.make(getActivity().getCurrentFocus(), "Password must contain Lowercase character", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

    }

    private ContentValues setUserDetails() {

        ContentValues cvRegDetails = new ContentValues();
        cvRegDetails.put("USERID", StringUtils.generateUserId());
        cvRegDetails.put("PASSWORD", etPassword.getText().toString());
        cvRegDetails.put("NAME", etName.getText().toString());
        cvRegDetails.put("EMAIL", etEmail.getText().toString());
        cvRegDetails.put("DOB", etDOB.getText().toString());
        cvRegDetails.put("HOMETOWN", selectedHometown);
        cvRegDetails.put("SALT", StringUtils.getSalt());
        cvRegDetails.put("NUM_OF_POSTS", "0");
        cvRegDetails.put("LAST_POSTED", "0");
        cvRegDetails.put("LOGIN_TYPE", HIVE_LOGIN.getVal());

        return cvRegDetails;
    }

    private void clearErrors() {
        tvName.setTextColor(Color.BLACK);
        tvEmail.setTextColor(Color.BLACK);
        tvDOB.setTextColor(Color.BLACK);
        tvPassword.setTextColor(Color.BLACK);
        tvHometown.setTextColor(Color.BLACK);

        tvName.setText("Name : ");
        tvEmail.setText("Email : ");
        tvDOB.setText("Date Of Birth : ");
        tvPassword.setText("Password : ");
    }

    private DatePickerDialog getDatePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        StringBuilder dateString = new StringBuilder();
                        dateString.append(year);
                        dateString.append("/");
                        dateString.append(monthOfYear);
                        dateString.append("/");
                        dateString.append(dayOfMonth);
                        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                        Date selectedDate;
                        try {
                            selectedDate = df.parse(dateString.toString());
                            String formattedDateString = df.format(selectedDate);
                            etDOB.setText(formattedDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        return datePickerDialog;
    }

    public final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
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
