package hive.hive.com.hive.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hive.hive.com.hive.GSONEntities.HivePostDetails;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Adapters.AllPostCustomListAdapter;
import hive.hive.com.hive.Utils.InfiniteScrollListener;

import static hive.hive.com.hive.Utils.ConnectionUtils.getHivePosts;


public class AllPostsFragment extends Fragment implements AdapterView.OnItemClickListener {
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


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
        View view = inflater.inflate(R.layout.fragment_all_posts, container, false);

        listViewHivePosts = (ListView) view.findViewById(R.id.listViewHivePosts);
        listViewHivePosts.setOnItemClickListener(this);
        setFirstTenPosts();


        listViewHivePosts.setOnScrollListener(new InfiniteScrollListener(3) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                if (!justLoaded) {
                    start += 10;
                    end += 10;
                    Log.d("Start : ", String.valueOf(start));
                    Log.d("End : ", String.valueOf(end));
                    newPosts.clear();

                    newPosts = getHivePosts(AllPostsFragment.this, start, end);
                    hivePosts.addAll(newPosts);
                    Log.d("HivePostSize", String.valueOf(hivePosts.size()));
                    if (newPosts.size() >= 1) {
                        adapter.addAll(newPosts);
                        adapter.notifyDataSetChanged();
                    }
                    else
                        Log.d("NewPosts", "Empty");
                } else {
                    justLoaded = false;
                }

            }
        });


        return view;

    }

    private void setFirstTenPosts() {
        hivePosts = getHivePosts(AllPostsFragment.this, start, end);
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
