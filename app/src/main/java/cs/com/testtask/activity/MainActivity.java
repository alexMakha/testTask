package cs.com.testtask.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import cs.com.testtask.R;
import cs.com.testtask.fragments.FragmentEndlessList;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addFragmentCalendar();
    }

    private void addFragmentCalendar() {
        Fragment calendar = new FragmentEndlessList();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameContainer_M, calendar);
        fragmentTransaction.commit();
    }

    public void setCustomActionBar(final View _customView) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            if (_customView == null)
                actionBar.setCustomView(R.layout.calendr_action_bar);
            else
                actionBar.setCustomView(_customView);
        }
    }
}
