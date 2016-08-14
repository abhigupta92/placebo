package hive.hive.com.hive.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.login.widget.ProfilePictureView;

import hive.hive.com.hive.GSONEntities.EventDetails;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EVENT_ID = "EVENT_ID";

    ToggleButton toggleButtonUserInterest;
    Button backButton;
    TextView tvUserName, tvEventName, tvEventDesc, tvNumOfPeople, tvEventType, tvEventLocation, tvEventDate, tvEventTime;
    ProfilePictureView profilePictureView;

    // TODO: Rename and change types of parameters
    private long eventId;

    private OnFragmentInteractionListener mListener;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EventDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventDetailFragment newInstance(long param1) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putLong(EVENT_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getLong(EVENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);

        initializeViews(view);

        EventDetails eventDetails = ConnectionUtils.getEventDetails(eventId);

        setViews(eventDetails);

        return view;
    }

    private void setViews(EventDetails eventDetails) {

        Log.d("Event details", eventDetails.eventLocation);
        profilePictureView.setProfileId(eventDetails.getEventUserId());
        tvUserName.setText("Name for now..");
        tvEventName.setText("Event Name : " + eventDetails.getEventName());
        tvEventDesc.setText("Event Desc : " + eventDetails.getEventDesc());
        tvNumOfPeople.setText("Num of people expected : " + eventDetails.getEventNumOfPpl());
        tvEventLocation.setText("Get Location here later from tag");
        tvEventDate.setText("Event Date : " + eventDetails.getEventDate());
        tvEventTime.setText("Event Time : " + eventDetails.getEventTime());
        if (eventDetails.eventInterest == 1) {
            toggleButtonUserInterest.setChecked(true);
        } else {
            toggleButtonUserInterest.setChecked(false);
        }
    }

    private void initializeViews(View view) {

        toggleButtonUserInterest = (ToggleButton) view.findViewById(R.id.toggleUserInterest_event_detail);
        backButton = (Button) view.findViewById(R.id.buttonBack_event_detail);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName_event_detail);
        tvEventName = (TextView) view.findViewById(R.id.tvEventName_event_detail);
        tvEventDesc = (TextView) view.findViewById(R.id.tvEventDesc_event_detail);
        tvNumOfPeople = (TextView) view.findViewById(R.id.tvNumOfPeople_event_detail);
        tvEventType = (TextView) view.findViewById(R.id.tvEventType_event_detail);
        tvEventLocation = (TextView) view.findViewById(R.id.tvEventLocation_event_detail);
        tvEventDate = (TextView) view.findViewById(R.id.tvEventDate_event_detail);
        tvEventTime = (TextView) view.findViewById(R.id.tvEventTime_event_detail);
        profilePictureView = (ProfilePictureView) view.findViewById(R.id.eventProfilePic_event_detail);

        backButton.setOnClickListener(this);
        toggleButtonUserInterest.setOnCheckedChangeListener(this);
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
            case R.id.buttonBack_event_detail:
                this.getFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            ConnectionUtils.setEventInterest(eventId, 1);
        }else{
            ConnectionUtils.setEventInterest(eventId, 0);
        }

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
