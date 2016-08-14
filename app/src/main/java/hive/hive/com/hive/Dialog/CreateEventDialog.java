package hive.hive.com.hive.Dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hive.hive.com.hive.Activities.MainActivity;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.UserSessionUtils;

import static hive.hive.com.hive.Utils.Enums.CREATEEVENTDIALOG;


/**
 * Created by abhishekgupta on 22/05/16.
 */
public class CreateEventDialog extends DialogFragment implements View.OnClickListener, View.OnFocusChangeListener {

    Button createEventButton, cancelEventButton;
    private static View view;
    private static boolean clearAll = false;
    EditText etSetEventDate, etSetEventTime, etEventName, etNumOfPeople, etEventDesc;
    Spinner spEventType;
    private int mYear, mMonth, mDay, mHour, mMinute;

    UserSessionUtils userSession;


    public interface CreateEventDialogListener {
        void OnFinishCreateEventDialog();

        void OnErrorCreateEventErrorDialog();
    }

    public CreateEventDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(CREATEEVENTDIALOG.name(), String.valueOf(clearAll));
        userSession = MainActivity.getUserSession();

        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        try {
            view = inflater.inflate(R.layout.dialog_create_event, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        getDialog().setTitle("Hello");

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {
        createEventButton = (Button) view.findViewById(R.id.createEventButton_create_event);
        cancelEventButton = (Button) view.findViewById(R.id.cancelEventButton_create_event);
        etSetEventDate = (EditText) view.findViewById(R.id.etSetDate_create_event);
        etSetEventTime = (EditText) view.findViewById(R.id.etSetTime_create_event);
        etEventName = (EditText) view.findViewById(R.id.etEventName_create_event);
        etNumOfPeople = (EditText) view.findViewById(R.id.etNumOfPeople_create_event);
        etEventDesc = (EditText) view.findViewById(R.id.etEventDesc_create_event);
        spEventType = (Spinner) view.findViewById(R.id.spinnerEventType_create_event);
        createEventButton.setOnClickListener(this);
        cancelEventButton.setOnClickListener(this);
        etSetEventDate.setOnFocusChangeListener(this);
        etSetEventDate.setOnClickListener(this);
        etSetEventTime.setOnFocusChangeListener(this);
        etSetEventTime.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        CreateEventDialogListener activtiy = (CreateEventDialogListener) getActivity();
        switch (v.getId()) {
            case R.id.createEventButton_create_event:
                ContentValues cvEvent = new ContentValues();
                cvEvent.put("EVENT_NAME", etEventName.getText().toString());
                cvEvent.put("EVENT_TYPE", spEventType.getSelectedItemId());
                cvEvent.put("EVENT_LOCATION", "LOCATION FOR NOW... SET LATER");
                cvEvent.put("EVENT_DATE", etSetEventDate.getText().toString());
                cvEvent.put("EVENT_TIME", etSetEventTime.getText().toString());
                cvEvent.put("EVENT_NUM_OF_PEOPLE", etNumOfPeople.getText().toString());
                cvEvent.put("EVENT_DESC", etEventDesc.getText().toString());
                cvEvent.put("EVENT_USER_ID", userSession.getUserId());
                cvEvent.put("EVENT_STATUS", 0);
                JSONObject eventCreatedDetails = ConnectionUtils.createEvent(cvEvent);
                Log.d(CREATEEVENTDIALOG.name(),eventCreatedDetails.toString());
                boolean eventCreated = false;
                try {
                    eventCreated = eventCreatedDetails.getString("result").contentEquals("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (eventCreated) {
                    activtiy.OnFinishCreateEventDialog();
                    clearAll = true;
                } else {
                    clearAll = false;
                    activtiy.OnErrorCreateEventErrorDialog();
                }
                break;
            case R.id.cancelEventButton_create_event:
                clearAll = false;
                activtiy.OnErrorCreateEventErrorDialog();
                break;
            case R.id.etSetDate_create_event:
                DatePickerDialog datePickerDialog = getDatePicker();
                datePickerDialog.show();
                break;

            case R.id.etSetTime_create_event:
                TimePickerDialog timePickerDialog = getTimePicker();
                timePickerDialog.show();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (etSetEventDate.hasFocus()) {
            if (v.getId() == R.id.etSetDate_create_event) {
                DatePickerDialog datePickerDialog = getDatePicker();
                datePickerDialog.show();
            }
        }
        if (etSetEventTime.hasFocus()) {
            if (v.getId() == R.id.etSetTime_create_event) {
                TimePickerDialog timePickerDialog = getTimePicker();
                timePickerDialog.show();
            }
        }
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
                            etSetEventDate.setText(formattedDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);
        return datePickerDialog;

    }

    private TimePickerDialog getTimePicker() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        StringBuilder timeString = new StringBuilder();
                        timeString.append(hourOfDay);
                        timeString.append(":");
                        timeString.append(minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                        Date date;
                        try{
                            date = sdf.parse(timeString.toString());
                            String formattedTimeString = sdf.format(date);
                            etSetEventTime.setText(formattedTimeString);
                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                    }
                }, mHour, mMinute, false);
        return timePickerDialog;
    }
}
