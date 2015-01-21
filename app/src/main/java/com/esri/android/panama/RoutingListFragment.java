package com.esri.android.panama;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;



/*
 * This fragment populates the Navigation Drawer with
 * a customized listview and also provides the interface
 * for communicating with the activity.
 */
public class RoutingListFragment extends Fragment implements
        ListView.OnItemClickListener, TextToSpeech.OnInitListener {
    public static ListView mDrawerList;
    onDrawerListSelectedListener mCallback;
    private TextToSpeech tts;
    private boolean isSoundOn = true;
    TextView directionsLabel;
    // Container Activity must implement this interface
    public interface onDrawerListSelectedListener {
        public void onSegmentSelected(String segment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        try {
            mCallback = (onDrawerListSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onDrawerListSelectedListener");
        }

        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tts = new TextToSpeech(getActivity(), this);

        MyAdapter adapter = new MyAdapter(getActivity(),
                WifiPanama.curDirections);
        mDrawerList = (ListView) getActivity().findViewById(R.id.right_drawer);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(this);

        directionsLabel = (TextView) getActivity().findViewById(R.id.Textswitch);
        directionsLabel.setText(getString(R.string.sound_label));

        Switch sound_toggle = (Switch) getActivity().findViewById(R.id.switch1);
        sound_toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                isSoundOn = isChecked;
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        TextView segment = (TextView) view.findViewById(R.id.segment);
        WifiPanama.mDrawerLayout.closeDrawers();
        if (isSoundOn)
            speakOut(segment.getText().toString());
        mCallback.onSegmentSelected(segment.getText().toString());
    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_layout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.direction:
                if (WifiPanama.mDrawerLayout.isDrawerOpen(Gravity.END)) {
                    WifiPanama.mDrawerLayout.closeDrawers();
                } else {
                    WifiPanama.mDrawerLayout.openDrawer(Gravity.END);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onStop() {
        super.onStop();
        tts.shutdown();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

