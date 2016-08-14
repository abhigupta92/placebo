package hive.hive.com.hive.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hive.hive.com.hive.Adapters.AllEventCustomListAdapter;
import hive.hive.com.hive.GSONEntities.EventListDetails;
import hive.hive.com.hive.R;

import static hive.hive.com.hive.Utils.ConnectionUtils.getEvents;
import static hive.hive.com.hive.Utils.Enums.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText etSearchEvent;
    ListView lvEvents;
    private long start, end;
    Button buttonSearchEvents;
    List<EventListDetails> allEvents = new ArrayList<EventListDetails>();
    AllEventCustomListAdapter adapter;
    private boolean searchList = false;

    private OnFragmentInteractionListener mListener;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Events.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
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
        start = 0;
        end = 10;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {

        etSearchEvent = (EditText) view.findViewById(R.id.etSearchEvents);
        lvEvents = (ListView) view.findViewById(R.id.listViewEvents);
        buttonSearchEvents = (Button) view.findViewById(R.id.buttonEventSearch);
        buttonSearchEvents.setOnClickListener(this);
        lvEvents.setOnItemClickListener(this);

        setInitialEvents();

    }

    private void setInitialEvents() {
        allEvents = getEvents(EventsFragment.this, "", start, end);
        Log.d("Size of allEvents", String.valueOf(allEvents.size()));
        adapter = new AllEventCustomListAdapter(getActivity().getApplicationContext(), R.layout.event_item, allEvents);
        lvEvents.setAdapter(adapter);
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
            case R.id.buttonEventSearch:
                start = 0;
                end = 10;
                String searchString = etSearchEvent.getText().toString();
                searchList = true;
                List<EventListDetails> eventsList = getEvents(EventsFragment.this, searchString, start, end);
                //Log.d("EVENTSFRAGMENT",eventsList.get(0).eventTitle.toString());
                adapter.clear();
                adapter.addAll(eventsList);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventListDetails eventListDetails = (EventListDetails) adapter.getItem(position);
        EventDetailFragment eventDetailFragment = EventDetailFragment.newInstance(eventListDetails.eventId);
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, eventDetailFragment, EVENTDETAILFRAGMENT.name());
        transaction.addToBackStack(EVENTDETAILFRAGMENT.name());
        transaction.commit();
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
