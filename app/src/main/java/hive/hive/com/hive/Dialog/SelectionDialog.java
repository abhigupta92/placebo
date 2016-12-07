package hive.hive.com.hive.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hive.hive.com.hive.R;

/**
 * Created by abhishekgupta on 16/10/16.
 */

public class SelectionDialog extends DialogFragment implements View.OnClickListener, View.OnFocusChangeListener {

    Button bCreatePost, bCreateEvent;

    @Override
    public void onClick(View v) {

        SelectionDialogListener activity = (SelectionDialogListener) getActivity();

        switch (v.getId()) {

            case R.id.bCreateEvent_dialog_selection:
                activity.onCreateEventSelected();
                break;

            case R.id.bShareView_dialog_selection:
                activity.onShareViewSelected();
                break;

        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = null;
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        try {
            view = inflater.inflate(R.layout.dialog_selection, container, false);
        } catch (InflateException e) {
        }

        getDialog().setTitle("Yes ?");
        getDialog().getWindow().setLayout(500, 500);

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {

        bCreateEvent = (Button) view.findViewById(R.id.bCreateEvent_dialog_selection);
        bCreatePost = (Button) view.findViewById(R.id.bShareView_dialog_selection);

        bCreatePost.setOnClickListener(this);
        bCreateEvent.setOnClickListener(this);
    }

    public interface SelectionDialogListener {
        void onCreateEventSelected();

        void onShareViewSelected();
    }
}
