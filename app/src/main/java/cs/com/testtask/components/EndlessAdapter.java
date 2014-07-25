package cs.com.testtask.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import cs.com.testtask.MyApplication;
import cs.com.testtask.R;
import cs.com.testtask.activity.MainActivity;
import cs.com.testtask.models.Day;
import cs.com.testtask.models.Week;

/**
 * Created by AlexCs on 6/19/2014.
 */
public class EndlessAdapter extends ArrayAdapter<Week> implements View.OnClickListener {

    int year;
    private List<Week> itemList;
    private Context mContext;
    private int mCellWidth;
    Transformation mDayBackgroundTransformation = new Transformation() {
        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth = mCellWidth;
            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);

            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "transformation_ " + " desiredWidth";
        }
    };
    private int mCurrentMonth;
    private String mCurrentMonthName;
    public int mCurrentYear;

    public EndlessAdapter(Context ctx, List<Week> itemList, int layoutId, int cellWidth) {
        super(ctx, layoutId, itemList);
        this.itemList = itemList;
        this.mContext = ctx;
        this.mCellWidth = cellWidth;
        if (itemList.size() > 0) {
            mCurrentMonth = itemList.get(0).get(6).Month;
            mCurrentYear = itemList.get(0).get(6).Year;
            mCurrentMonthName = itemList.get(8).get(6).getMonthName();
        }
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Week getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        List<DayHolder> daysHolder;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout, parent, false);

            daysHolder = new ArrayList<DayHolder>();

            int[] relIds = {R.id.rel0, R.id.rel1, R.id.rel2, R.id.rel3, R.id.rel4, R.id.rel5, R.id.rel6};
            int[] textIds = {R.id.day_text0, R.id.day_text1, R.id.day_text2, R.id.day_text3, R.id.day_text4, R.id.day_text5, R.id.day_text6};
            int[] imageIds = {R.id.day_image0, R.id.day_image1, R.id.day_image2, R.id.day_image3, R.id.day_image4, R.id.day_image5, R.id.day_image6};

            for (int i = 0; i <= 6; i++) {
                DayHolder dayHolder = createDateHolder(convertView, relIds, textIds, imageIds, i);
                daysHolder.add(i, dayHolder);
            }

            convertView.setTag(daysHolder);
        } else {
            daysHolder = (List<DayHolder>) convertView.getTag();
        }

        List<Day> week = new ArrayList<Day>();
        week.addAll(itemList.get(position));
        Day day;

        for (int idx = 0; idx <= 6; idx++) {
            if (week.get(idx) != null) {
                day = week.get(idx);
                daysHolder.get(idx).textView.setText(String.valueOf(day.Day));
                daysHolder.get(idx).relativeLayout.setTag(day.Day + "_" + day.Month + "_" + day.Year);

                Picasso.with(mContext)
                        .load(day.imgURL)
                        .error(android.R.drawable.stat_notify_error)
                        .transform(mDayBackgroundTransformation)
                        .into(daysHolder.get(idx).imageView);
                if (!day.isCurrentMonthDay()) {
                    daysHolder.get(idx).imageView.setAlpha(0.2f);
                } else {
                    daysHolder.get(idx).imageView.setAlpha(1.0f);
                }
            }
        }

        return convertView;
    }

    private DayHolder createDateHolder(View convertView, int[] relIds, int[] textIds, int[] imageIds, int i) {
        DayHolder dayHolder = new DayHolder();
        dayHolder.relativeLayout = (RelativeLayout) convertView.findViewById(relIds[i]);
        dayHolder.relativeLayout.setOnClickListener(this);
        dayHolder.relativeLayout.getLayoutParams().height = mCellWidth;
        dayHolder.relativeLayout.getLayoutParams().width = mCellWidth;
        dayHolder.textView = (TextView) convertView.findViewById(textIds[i]);
        dayHolder.imageView = (ImageView) convertView.findViewById(imageIds[i]);
        return dayHolder;
    }

    public void addToEndWithGlue(List<Week> _week) {
        int firstNewDay = 0;
        for (int day = 0; day < 7; day++) {
            if (_week.get(0).get(day) != null) {
                firstNewDay = _week.get(0).get(day).getWeekDay();
                break;
            }
        }
        if (firstNewDay == 0) {
            itemList.addAll(_week);
        } else {
            Week week = _week.get(0);
            for (int day = firstNewDay; day < 7; day++) {
                itemList.get(itemList.size() - 1).add(day, week.get(day));
            }
            _week.remove(0);
            itemList.addAll(_week);
        }
    }

    public void addToStartWithGlue(List<Week> _week) {
        int firstOldDay = 0;
        for (int day = 6; day >= 0; ) {
            if (_week.get(_week.size() - 1).get(day) != null) {
                firstOldDay = _week.get(_week.size() - 1).get(day).getWeekDay();
                break;
            }
            day--;
        }
        if (firstOldDay == 6) {
            for (int day = _week.size() - 1; day >= 0; ) {
                itemList.add(0, _week.get(day));
                day--;
            }
        } else {
            Week week = itemList.get(0);
            for (int day = firstOldDay + 1; day <= 6; day++) {

                _week.get(_week.size() - 1).add(day, week.get(day));
            }
            itemList.remove(0);
            for (int day = _week.size() - 1; day >= 0; ) {
                itemList.add(0, _week.get(day));
                day--;
            }
        }
    }

    public void setNextMonthToCurrent(final int firstItem, final int count) {
        if (itemList.get(firstItem).get(6) == null) {
            MyApplication.isLoad = false;
            return;

        }
        final int itemMonth = itemList.get(firstItem).get(6).Month;
        mCurrentYear = itemList.get(firstItem).get(6).Year;
        Log.v("cs_cs", "mCurrentMonth: " + mCurrentMonth + " ; item month: " + itemMonth);
        if (mCurrentMonth != itemMonth) {

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    notifyDataSetChanged();
                    ((TextView) (((MainActivity) mContext).findViewById(R.id.tv_current_month))).setText(getCurrentMonthName() + " " + year);
                    MyApplication.isLoad = false;
                }

                @Override
                protected Void doInBackground(Void... params) {
                    for (int weeksId = firstItem; weeksId <= firstItem + count - 1; weeksId++) {
                        if (itemList.size() == weeksId)
                            return null;
                        for (Day day : itemList.get(weeksId)) {
                            if (day != null) {
                                int visibleMonth = itemMonth + 1;
                                if (itemMonth == 11)
                                    visibleMonth = 0;

                                if (day.Month != visibleMonth) {
                                    day.setIsCorrentMonthDay(false);
                                } else {
                                    day.setIsCorrentMonthDay(true);
                                    year = day.Year;
                                    mCurrentMonthName = day.getMonthName();
                                }
                            }
                        }
                    }
                    mCurrentMonth = itemMonth;
                    return null;
                }
            }.execute();
        } else
            MyApplication.isLoad = false;
    }

    public String getCurrentMonthName() {
        return mCurrentMonthName;
    }

    public int getPrevMonth() {
        return mCurrentMonth;
    }

    public int getNextMonth() {
        return mCurrentMonth + 2 > 11 ? mCurrentMonth + 2 - 12 : mCurrentMonth + 2;
    }

    public int getPrevYear() {
        return mCurrentYear;
    }

    public int getNexYear() {
        return mCurrentMonth + 2 > 11 ? mCurrentYear + 1 : mCurrentYear;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(mContext, v.getTag().toString(), Toast.LENGTH_SHORT).show();
    }

    class DayHolder {
        TextView textView;
        ImageView imageView;
        RelativeLayout relativeLayout;
    }
}