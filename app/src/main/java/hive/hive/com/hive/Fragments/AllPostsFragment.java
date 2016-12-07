package hive.hive.com.hive.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import hive.hive.com.hive.Activities.MainActivity;
import hive.hive.com.hive.Adapters.AllPostCustomListAdapter;
import hive.hive.com.hive.GSONEntities.HiveOnMapDtl;
import hive.hive.com.hive.GSONEntities.HivePostDetails;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.InfiniteScrollListener;
import hive.hive.com.hive.Utils.UserSessionUtils;

import static hive.hive.com.hive.Utils.ConnectionUtils.getHivePosts;


public class AllPostsFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static boolean justLoaded = true;

    ListView listViewHivePosts;
    private long start, end;
    List<HivePostDetails> hivePosts = new ArrayList<HivePostDetails>();
    List<HivePostDetails> newPosts = new ArrayList<>();
    AllPostCustomListAdapter adapter;
    Button switchView, switchView2;
    RelativeLayout view1, view2;
    UserSessionUtils userSessionUtils;
    UserSessionUtils.UserSessionDetails userSessionDetails;

    SupportMapFragment hivePostsMap;
    GoogleMap hiveMap;
    ArrayList<Marker> hivesMarker;

    TouchableWrapper touchableWrapper;

    boolean mapTouched;

    Marker NE, SW, CE, SE, NW;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View mainView;
    int currentViewId = -1;

    public AllPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllPostsFragment newInstance(String param1, String param2) {
        AllPostsFragment fragment = new AllPostsFragment();
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
        // Inflate the layout for this fragment
        if (mainView != null) {
            ViewGroup parent = (ViewGroup) mainView.getParent();
            if (parent != null)
                parent.removeView(mainView);
        }
        try {
            mainView = inflater.inflate(R.layout.fragment_all_posts, container, false);
        } catch (InflateException e) {

        }
        currentViewId = R.layout.fragment_all_posts;

        touchableWrapper = new TouchableWrapper(getActivity());

        userSessionUtils = MainActivity.getUserSession();
        userSessionDetails = userSessionUtils.getUserDetails();

        listViewHivePosts = (ListView) mainView.findViewById(R.id.listViewHivePosts);
        listViewHivePosts.setOnItemClickListener(this);
        setFirstTenPosts();

        switchView = (Button) mainView.findViewById(R.id.switchView);
        switchView.setOnClickListener(this);
        switchView2 = (Button) mainView.findViewById(R.id.switchView2);
        switchView2.setOnClickListener(this);

        initializeHiveMap();

        view1 = (RelativeLayout) mainView.findViewById(R.id.posts_list);
        view2 = (RelativeLayout) mainView.findViewById(R.id.posts_map);
        listViewHivePosts.setOnScrollListener(new InfiniteScrollListener(3) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                if (!justLoaded) {
                    start += 10;
                    end += 10;
                    Log.d("Start : ", String.valueOf(start));
                    Log.d("End : ", String.valueOf(end));
                    newPosts.clear();

                    newPosts = getHivePosts(AllPostsFragment.this, userSessionUtils.getUserId(), start, end);
                    hivePosts.addAll(newPosts);
                    Log.d("HivePostSize", String.valueOf(hivePosts.size()));
                    if (newPosts.size() >= 1) {
                        adapter.addAll(newPosts);
                        adapter.notifyDataSetChanged();
                    } else
                        Log.d("NewPosts", "Empty");
                } else {
                    justLoaded = false;
                }

            }
        });

        touchableWrapper.addView(mainView);

        return touchableWrapper;

    }

    private void initializeHiveMap() {
        hivePostsMap = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.hive_posts_map);
        hivePostsMap.getMapAsync(this);
        hivesMarker = new ArrayList<>();

    }

    private void setFirstTenPosts() {
        hivePosts = getHivePosts(AllPostsFragment.this, userSessionUtils.getUserId(), start, end);
        Log.d("Size of hivePosts", String.valueOf(hivePosts.size()));
        adapter = new AllPostCustomListAdapter(getActivity().getApplicationContext(), R.layout.hive_post_item, hivePosts);
        listViewHivePosts.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        start = 0;
        end = 10;
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
        Log.d("TESTFRAGMENT", "DETACHED");
        adapter.imageLoader.clearCache();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.switchView:
                switchView(true);
                break;
            case R.id.switchView2:
                switchView(false);
                break;

        }
    }

    private void switchView(boolean mapView) {
        if (mapView) {
            view1.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.VISIBLE);
        } else {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.INVISIBLE);
        }
        String currScreen = MainActivity.getActiveFragment(getActivity().getSupportFragmentManager());
        /*if (currScreen.contentEquals(ALLPOSTSFRAGMENT.getVal())) {
            switch (currentViewId) {
                case R.layout.fragment_all_posts:
                    setViewLayout(R.layout.fragment_all_posts_map);
                    switchView2 = (Button) mainView.findViewById(R.id.switchView2);
                    switchView2.setOnClickListener(this);
                    currentViewId = R.layout.fragment_all_posts_map;
                    break;
                case R.layout.fragment_all_posts_map:
                    setViewLayout(R.layout.fragment_all_posts);
                    currentViewId = R.layout.fragment_all_posts;
                    break;
            }
        }*/
    }

    private void setViewLayout(int id) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double longitude, latitude;
        longitude = userSessionDetails.getLONGITUDE();
        latitude = userSessionDetails.getLATITUDE();
        LatLng latLng = new LatLng(latitude, longitude);



        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("First Marker"));
        hivesMarker.add(marker);
        googleMap.setOnMarkerClickListener(this);
        googleMap.addCircle(new CircleOptions().center(latLng).radius(50.0).fillColor(Color.RED));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        googleMap.setOnCameraChangeListener(this);

        setMapInfoWindow(googleMap);

        hiveMap = googleMap;
    }

    private void setMapInfoWindow(GoogleMap googleMap) {

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            TextView tvClusterName, tvHiveName;

            @Override
            public View getInfoWindow(Marker marker) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.hive_info_window, null);

                tvClusterName = (TextView) v.findViewById(R.id.clusterName_hive_info_window);
                tvHiveName = (TextView) v.findViewById(R.id.hiveName_hive_info_window);

                tvClusterName.setText(marker.getTitle());
                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (!mapTouched) {
            LatLngBounds latLngBounds = hiveMap.getProjection().getVisibleRegion().latLngBounds;
            LatLng northeast = latLngBounds.northeast;
            LatLng southwest = latLngBounds.southwest;
            LatLng center = latLngBounds.getCenter();
            LatLng northwest = new LatLng(northeast.latitude, southwest.longitude);
            LatLng southeast = new LatLng(southwest.latitude, northeast.longitude);

            ContentValues contentValues = new ContentValues();
            contentValues.put("SW", convertLatLngReqParam(southwest));
            contentValues.put("NE", convertLatLngReqParam(northeast));
            contentValues.put("NW", convertLatLngReqParam(northwest));
            contentValues.put("SE", convertLatLngReqParam(southeast));

            ArrayList<HiveOnMapDtl> hivesOnMapCoords = ConnectionUtils.getHivesOnMap(contentValues);

            resetMapHiveMarkers(hivesOnMapCoords);

        }


        /*if (NE != null) {
            NE.remove();
            CE.remove();
            SW.remove();
            NW.remove();
            SE.remove();
        }
        LatLng northwest = new LatLng(northeast.latitude, southwest.longitude);
        LatLng southeast = new LatLng(southwest.latitude, northeast.longitude);

        NE = hiveMap.addMarker(new MarkerOptions().position(northeast).title("NorthEast"));
        CE = hiveMap.addMarker(new MarkerOptions().position(center).title("Center"));
        SW = hiveMap.addMarker(new MarkerOptions().position(southwest).title("SouthWest"));
        SE = hiveMap.addMarker(new MarkerOptions().position(southeast).title("SouthEast"));
        NW = hiveMap.addMarker(new MarkerOptions().position(northwest).title("NorthWest"));

        NE.showInfoWindow();
        CE.showInfoWindow();
        SW.showInfoWindow();
        NW.showInfoWindow();
        SE.showInfoWindow();*/

    }

    private void resetMapHiveMarkers(ArrayList<HiveOnMapDtl> hivesOnMapCoords) {

        ListIterator<Marker> hiveListItr = hivesMarker.listIterator();

        while (hiveListItr.hasNext()) {
            Marker hiveMarker = hiveListItr.next();
            hiveMarker.remove();
            hiveListItr.remove();
        }

        for (HiveOnMapDtl hiveMarkerDtl : hivesOnMapCoords) {
            LatLng hiveLatLng = new LatLng(hiveMarkerDtl.getMidLat(), hiveMarkerDtl.getMidLng());
            Marker hiveMarker = hiveMap.addMarker(new MarkerOptions().position(hiveLatLng).title("Cluster : " + hiveMarkerDtl.getClusterId()
                    + "\nHive : " + hiveMarkerDtl.getSubpartId()));
            hivesMarker.add(hiveMarker);
        }

    }

    private String convertLatLngReqParam(LatLng latlng) {
        return latlng.latitude + "|" + latlng.longitude;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class TouchableWrapper extends FrameLayout {

        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mapTouched = true;
                    break;
                case MotionEvent.ACTION_UP:
                    mapTouched = false;
                    onCameraChange(hiveMap.getCameraPosition());
                    break;
            }

            return super.dispatchTouchEvent(ev);

        }

    }
}
