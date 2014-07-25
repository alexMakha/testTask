package cs.com.testtask.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cs.com.testtask.R;
import cs.com.testtask.activity.MainActivity;
import cs.com.testtask.components.EndlessAdapter;
import cs.com.testtask.components.EndlessListView;
import cs.com.testtask.models.Week;
import cs.com.testtask.utils.CalendarGenerator;

/**
 * Created by AlexCs on 6/19/2014.
 */
public class FragmentEndlessList extends Fragment implements EndlessListView.EndlessListener {

    private EndlessListView lv;
    private View vi;
    private Activity mActivity;
    private EndlessAdapter adp;
    private boolean isDawnScroll;


    private int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point point = new Point();
        point.x = display.getWidth();
        point.y = display.getHeight();
        columnWidth = point.x;
        return columnWidth;
    }

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);
        mActivity = _activity;
        ((MainActivity) mActivity).setCustomActionBar(initializeActionBar());
    }

    private View initializeActionBar() {
        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.calendr_action_bar, null);
        if (v != null) {
            TextView tvCurrentName = (TextView) v.findViewById(R.id.tvActionCurrent);

            tvCurrentName.setText(mActivity.getString(R.string.the_boys));
        }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDefaultList();
            }
        });
        return v;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vi = inflater.inflate(R.layout.fragment_endlase_list, container, false);
        lv = (EndlessListView) vi.findViewById(R.id.el);
        lv.setFastScrollEnabled(false);
        loadDefaultList();
        return vi;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) ((mActivity).findViewById(R.id.tv_current_month))).setText(adp.getCurrentMonthName() + " " + adp.mCurrentYear);
    }

    private void loadDefaultList() {
        adp = new EndlessAdapter(mActivity, initFirstList(), R.layout.row_layout, getScreenWidth() / 7);
        lv.setLoadingView(R.layout.loading_layout);
        lv.setmAdapter(adp);
        lv.setSelectionFromTop(2, 0);
        lv.setListener(this);
    }

    private ArrayList<Week> initFirstList() {
        CalendarGenerator.toCurrentMonth(mActivity);
        return CalendarGenerator.getInitData();
    }

    @Override
    public void loadData(boolean isDownScroll) {
        isDawnScroll = isDownScroll;
        AddMonthTask fl = new AddMonthTask();
        fl.execute();
    }

    private ArrayList<Week> extendList() {
        ArrayList<Week> result;
        if (isDawnScroll) {
            result = CalendarGenerator.getNextWeekMonthList(adp.getNextMonth(), adp.getNexYear());
        } else {
            result = CalendarGenerator.getPrevWeekMonthList(adp.getPrevMonth(), adp.getPrevYear());
        }
        return result;
    }

    private class AddMonthTask extends AsyncTask<Void, Void, List<Week>> {

        @Override
        protected List<Week> doInBackground(Void... params) {
            return extendList();
        }

        @Override
        protected void onPostExecute(List<Week> result) {
            super.onPostExecute(result);
            if (isDawnScroll) {
                lv.addNewDataBottom(result);
            } else {
                lv.addNewDataTop(result);
            }
        }
    }


}