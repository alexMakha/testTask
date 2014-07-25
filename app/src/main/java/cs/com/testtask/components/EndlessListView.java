package cs.com.testtask.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import java.util.List;

import cs.com.testtask.MyApplication;
import cs.com.testtask.activity.MainActivity;
import cs.com.testtask.models.Week;

/**
 * Created by AlexCs on 6/19/2014.
 */
public class EndlessListView extends ListView implements AbsListView.OnScrollListener {

    private View footer;
    private EndlessListener listener;
    private EndlessAdapter mAdapter;
    private int oldVisebleItem;
    private Context ctx;

    public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnScrollListener(this);
        ctx = context;

    }

    public EndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(this);
        ctx = context;
    }

    public EndlessListView(Context context) {
        super(context);
        this.setOnScrollListener(this);
        ctx = context;
    }

    public void setListener(EndlessListener listener) {
        this.listener = listener;
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (getAdapter() == null)
            return;

        if (getAdapter().getCount() == 0)
            return;

        int l = visibleItemCount + firstVisibleItem;

        if (l >= totalItemCount &&
                !((MyApplication) ((MainActivity) ctx).getApplication()).isLoad) {
            // It is time to add new data. We call the listener
            this.addFooterView(footer);
            ((MyApplication) ((MainActivity) ctx).getApplication()).isLoad = true;
            listener.loadData(true);
        } else if (firstVisibleItem == 0 &&
                !((MyApplication) ((MainActivity) ctx).getApplication()).isLoad) {

            this.addHeaderView(footer);
            ((MyApplication) ((MainActivity) ctx).getApplication()).isLoad = true;
            listener.loadData(false);
        }

        if (!((MyApplication) ((MainActivity) ctx).getApplication()).isLoad) {
            ((MyApplication) ((MainActivity) ctx).getApplication()).isLoad = true;
            mAdapter.setNextMonthToCurrent(firstVisibleItem, visibleItemCount);

        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void setLoadingView(int resId) {
        LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = inflater.inflate(resId, null);
        this.addFooterView(footer);

    }

    @Override
    public void setSelectionFromTop(int position, int y) {
        super.setSelectionFromTop(position, y);
        oldVisebleItem = position;
    }

    public void setmAdapter(EndlessAdapter mAdapter) {
        super.setAdapter(mAdapter);
        this.mAdapter = mAdapter;
        this.removeFooterView(footer);
    }


    public void addNewDataBottom(List<Week> data) {

        this.removeFooterView(footer);
        mAdapter.addToEndWithGlue(data);
        mAdapter.notifyDataSetChanged();

        ((MyApplication) ((MainActivity) ctx).getApplication()).isLoad = false;

    }

    public void addNewDataTop(List<Week> data) {
        this.removeHeaderView(footer);
        mAdapter.addToStartWithGlue(data);


        int index = this.getFirstVisiblePosition() + data.size() - 1;
        View v = this.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
        mAdapter.notifyDataSetChanged();

        this.setSelectionFromTop(index, top);
        ((MyApplication) ((MainActivity) ctx).getApplication()).isLoad = false;

    }

    public EndlessListener setListener() {
        return listener;
    }


    public static interface EndlessListener {
        public void loadData(boolean isDownScroll);
    }
}
