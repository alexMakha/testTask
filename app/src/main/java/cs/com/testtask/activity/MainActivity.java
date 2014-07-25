package cs.com.testtask.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cs.com.testtask.R;
import cs.com.testtask.fragments.FragmentEndlessList;

public class MainActivity extends Activity {
    Fragment mCalendarFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addFragmentCalendar();
        setCustomActionBar();
    }

    private void addFragmentCalendar() {
        mCalendarFragment = new FragmentEndlessList();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameContainer_M, mCalendarFragment);
        fragmentTransaction.commit();
    }

    private View initializeActionBar() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendr_action_bar, null);
        if (view != null) {
            TextView tvCurrentName = (TextView) view.findViewById(R.id.tvActionCurrent);
            tvCurrentName.setText(getString(R.string.the_boys));
        }
        return view;
    }

    public void setCustomActionBar() {
        View customView   = initializeActionBar();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            if (customView == null)
                actionBar.setCustomView(R.layout.calendr_action_bar);
            else
                actionBar.setCustomView(customView);
        }
    }
 }
