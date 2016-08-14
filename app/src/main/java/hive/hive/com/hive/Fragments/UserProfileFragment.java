package hive.hive.com.hive.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hive.hive.com.hive.Activities.MainActivity;
import hive.hive.com.hive.GSONEntities.UserDetails;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.UserSessionUtils;

import static hive.hive.com.hive.Utils.Enums.FACEBOOK_LOGIN;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Profile FBProfile;
    UserDetails userDetails;

    UserSessionUtils userSession;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProfilePictureView profilePic;
    TextView tvProfileDetails, tvProfilePostDetails, tvStatusDetails;

    private OnFragmentInteractionListener mListener;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
        userSession = MainActivity.getUserSession();
        if (userSession.loginType() == FACEBOOK_LOGIN.getVal()) {
            FacebookSdk.sdkInitialize(getContext());
        }
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        initializeViews(view);

        return view;
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

    private void initializeViews(View view) {

        profilePic = (ProfilePictureView) view.findViewById(R.id.profilePic);
        tvProfileDetails = (TextView) view.findViewById(R.id.tvProfileDetails);
        tvProfilePostDetails = (TextView) view.findViewById(R.id.tvProfilePostDetails);
        tvStatusDetails = (TextView) view.findViewById(R.id.tvStatusDetails);

        if (userSession.loginType() == FACEBOOK_LOGIN.getVal()) {
            FBProfile = Profile.getCurrentProfile();
            userDetails = ConnectionUtils.getProfileDetails(FBProfile.getId());
        } else {
            userDetails = ConnectionUtils.getProfileDetails(userSession.getUserId());
        }

        if (userDetails != null) {
            userSession.setUserName(userDetails.getUserName());
            ConnectionUtils.setUserSessionDetails(userSession.getUserDetails());
            String dob = userDetails.getUserBirthday();
            profilePic.setProfileId(userDetails.getUserId());
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int age = getAge(date);
            tvProfileDetails.setText(userDetails.getUserName() + "," + age + "," + userDetails.getUserHomeTown());
            tvProfilePostDetails.setText("Total Posts : " + userDetails.getUserNumOfPosts() + "\n" + "Last Posted : " + userDetails.getUserLastPosted());

        }

    }

    public static int getAge(Date dateOfBirth) {

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;

        birthDate.setTime(dateOfBirth);
        if (birthDate.after(today)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
        if ((birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
                (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;

            // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
        } else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) &&
                (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }


}
