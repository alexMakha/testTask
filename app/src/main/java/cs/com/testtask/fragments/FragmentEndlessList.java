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
public class FragmentEndlessList extends Fragment implements EndlessListView.EndlessListener{

    private EndlessListView mListView;
    private Activity mActivity;
    private EndlessAdapter mListAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_endlase_list, container, false);
        mListView = (EndlessListView) view.findViewById(R.id.el);
        mListView.setFastScrollEnabled(false);
        mActivity = getActivity();
        loadDefaultList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) ((mActivity).findViewById(R.id.tv_current_month))).setText(mListAdapter.getCurrentMonthName() + " " + mListAdapter.mCurrentYear);
    }

    private void loadDefaultList() {
        mListAdapter = new EndlessAdapter(mActivity, initFirstList(), R.layout.row_layout, getScreenWidth() / 7);
        mListView.setLoadingView(R.layout.loading_layout);
        mListView.setmAdapter(mListAdapter);
        mListView.setSelectionFromTop(2, 0);
        mListView.setListener(this);
    }

    private List<Week> initFirstList() {
        CalendarGenerator.toCurrentMonth(mActivity);
        return CalendarGenerator.getInitData();
    }

    @Override
    public void loadData(boolean isDownScroll) {
        isDawnScroll = isDownScroll;
        AddMonthTask fl = new AddMonthTask();
        fl.execute();
    }

    private List<Week> extendList() {
        List<Week> result;
        if (isDawnScroll) {
            result = CalendarGenerator
                    .getNextWeekMonthList(mListAdapter.getNextMonth(), mListAdapter.getNexYear());
        } else {
            result = CalendarGenerator
                    .getPrevWeekMonthList(mListAdapter.getPrevMonth(), mListAdapter.getPrevYear());
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
                mListView.addNewDataBottom(result);
            } else {
                mListView.addNewDataTop(result);
            }
        }
    }


}