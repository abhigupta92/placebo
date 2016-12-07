package hive.hive.com.hive.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import hive.hive.com.hive.Activities.MainActivity;
import hive.hive.com.hive.ConnectionResults.LoginUserResultDetail;
import hive.hive.com.hive.ConnectionResults.RegisterUserResultDetails;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.FacebookUtils;
import hive.hive.com.hive.Utils.StringUtils;
import hive.hive.com.hive.Utils.UserSessionUtils;

import static hive.hive.com.hive.Utils.Enums.FACEBOOK_LOGIN;
import static hive.hive.com.hive.Utils.Enums.HIVE_LOGIN;
import static hive.hive.com.hive.Utils.Enums.REGISTRATIONFRAGMENT;
import static hive.hive.com.hive.Utils.FacebookUtils.setLoggedInStatus;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LoginButton fbLogin;
    TextView tvEmail, tvPassword;
    EditText etEmail, etPassword;
    CallbackManager callbackManager;
    UserSessionUtils session;

    final private String LOGIN_RESULT_SUCCESS = "success";
    final private String LOGIN_RESULT_FAIL = "fail";

    Button bRegister, bLogin;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlusOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {

        //Initialization of FaceBook button
        fbLogin = (LoginButton) view.findViewById(R.id.login_fb_button);
        fbLogin.setFragment(this);
        fbLogin.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_hometown"));
        fbLogin.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code

                                ContentValues params = new ContentValues();

                                try {
                                    params.put("USERID", object.getString("id"));
                                    params.put("NAME", object.getString("name"));
                                    params.put("EMAIL", object.getString("email"));
                                    params.put("DOB", object.getString("birthday"));
                                    JSONObject hometown = object.getJSONObject("hometown");
                                    params.put("HOMETOWN", hometown.getString("name"));
                                    params.put("NUM_OF_POSTS", "0");
                                    params.put("LAST_POSTED", "0");
                                    String salt = StringUtils.getSalt();
                                    params.put("SALT", salt);
                                    params.put("LOGIN_TYPE", FACEBOOK_LOGIN.getVal());

                                    Log.d("MainActivity", params.toString());
                                    RegisterUserResultDetails register;
                                    register = ConnectionUtils.registerUser(params);

                                    if (register.getResult().contentEquals("success")) {
                                        session = MainActivity.getUserSession();
                                        session.createUserLoginSession(object.getString("id"), salt, Long.parseLong(FACEBOOK_LOGIN.getVal()));
                                        FacebookUtils.setProfileId(object.getString("id"));
                                        setLoggedInStatus(AccessToken.getCurrentAccessToken());
                                        MainActivity.showLoader();
                                        UserProfileFragment userProfileFragment = new UserProfileFragment();
                                        MainActivity.smoothReplaceFragment(userProfileFragment, getActivity().getSupportFragmentManager());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,birthday,hometown");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.d("FB LOGIN", "Fail");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });

        bRegister = (Button) view.findViewById(R.id.bRegister_login);
        bRegister.setOnClickListener(this);
        bLogin = (Button) view.findViewById(R.id.bLogin_login);
        bLogin.setOnClickListener(this);

        tvEmail = (TextView) view.findViewById(R.id.tvEmail_login);
        tvPassword = (TextView) view.findViewById(R.id.tvPassword_login);
        etEmail = (EditText) view.findViewById(R.id.etEmail_login);
        etPassword = (EditText) view.findViewById(R.id.etPassword_login);

        MainActivity.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

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
            case R.id.bRegister_login:
                RegistrationFragment registrationFragment = new RegistrationFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, registrationFragment, REGISTRATIONFRAGMENT.name());
                fragmentTransaction.addToBackStack(REGISTRATIONFRAGMENT.name());
                fragmentTransaction.commit();
                break;

            case R.id.bLogin_login:
                clearErrors();
                hideKeyboard();
                if (checkFields()) {
                    ContentValues cvLogin = new ContentValues();
                    cvLogin.put("EMAILID", etEmail.getText().toString());
                    cvLogin.put("PASSWORD", etPassword.getText().toString());
                    LoginUserResultDetail loginRes = ConnectionUtils.loginUser(cvLogin);
                    if (loginRes.getResult().contentEquals(LOGIN_RESULT_SUCCESS)) {
                        UserSessionUtils userSession = MainActivity.getUserSession();
                        userSession.createUserLoginSession(loginRes.getUserId(), loginRes.getSalt(), Long.valueOf(HIVE_LOGIN.getVal()));
                        UserProfileFragment userProfileFragment = new UserProfileFragment();
                        MainActivity.showLoader();
                        MainActivity.smoothReplaceFragment(userProfileFragment, getActivity().getSupportFragmentManager());
                    } else if (loginRes.getResult().contentEquals(LOGIN_RESULT_FAIL)) {
                        Snackbar.make(getActivity().getCurrentFocus(), loginRes.getErrMsg(), Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        tvEmail.setTextColor(Color.RED);
                        tvPassword.setTextColor(Color.RED);
                    }
                } else {

                }
        }

    }

    private void hideKeyboard() {
        MainActivity.hideSoftKeyboard(getActivity());
    }

    private void clearErrors() {
        tvEmail.setTextColor(Color.BLACK);
        tvPassword.setTextColor(Color.BLACK);
    }

    /*
    Checks for valid login id and password entries
     */
    private boolean checkFields() {

        boolean fieldSet = true;
        if (etEmail.length() == 0) {
            tvEmail.setTextColor(Color.RED);
            fieldSet = false;
        }
        if (etPassword.length() == 0) {
            tvPassword.setTextColor(Color.RED);
            fieldSet = false;
        }

        return fieldSet;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

}
