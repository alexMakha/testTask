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

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout, parent, false);

            holder.sunDay = (RelativeLayout) convertView.findViewById(R.id.rel0);
            holder.monDay = (RelativeLayout) convertView.findViewById(R.id.rel1);
            holder.tuesDay = (RelativeLayout) convertView.findViewById(R.id.rel2);
            holder.wednesDay = (RelativeLayout) convertView.findViewById(R.id.rel3);
            holder.thursDay = (RelativeLayout) convertView.findViewById(R.id.rel4);
            holder.friDay = (RelativeLayout) convertView.findViewById(R.id.rel5);
            holder.saturDay = (RelativeLayout) convertView.findViewById(R.id.rel6);

            holder.sunDayText = (TextView) convertView.findViewById(R.id.day_text0);
            holder.monDayText = (TextView) convertView.findViewById(R.id.day_text1);
            holder.tuesDayText = (TextView) convertView.findViewById(R.id.day_text2);
            holder.wednesDayText = (TextView) convertView.findViewById(R.id.day_text3);
            holder.thursDayText = (TextView) convertView.findViewById(R.id.day_text4);
            holder.friDayText = (TextView) convertView.findViewById(R.id.day_text5);
            holder.saturDayText = (TextView) convertView.findViewById(R.id.day_text6);


            holder.sunDayImage = (ImageView) convertView.findViewById(R.id.day_image0);
            holder.monDayImage = (ImageView) convertView.findViewById(R.id.day_image1);
            holder.tuesDayImage = (ImageView) convertView.findViewById(R.id.day_image2);
            holder.wednesDayImage = (ImageView) convertView.findViewById(R.id.day_image3);
            holder.thursDayImage = (ImageView) convertView.findViewById(R.id.day_image4);
            holder.friDayImage = (ImageView) convertView.findViewById(R.id.day_image5);
            holder.saturDayImage = (ImageView) convertView.findViewById(R.id.day_image6);

            holder.weekDates = new ArrayList<TextView>(){{
                add(holder.sunDayText);
                add(holder.monDayText);
                add(holder.tuesDayText);
                add(holder.wednesDayText);
                add(holder.thursDayText);
                add(holder.friDayText);
                add(holder.saturDayText);
            }};
            holder.weekEntry = new ArrayList<RelativeLayout>(){{
                add(holder.sunDay);
                add(holder.monDay);
                add(holder.tuesDay);
                add(holder.wednesDay);
                add(holder.thursDay);
                add(holder.friDay);
                add(holder.saturDay);
            }};
            holder.weekImages = new ArrayList<ImageView>(){{
                add(holder.sunDayImage);
                add(holder.monDayImage);
                add(holder.tuesDayImage);
                add(holder.wednesDayImage);
                add(holder.thursDayImage);
                add(holder.friDayImage);
                add(holder.saturDayImage);
            }};

            for (RelativeLayout cell:  holder.weekEntry) {
                cell.setOnClickListener(this);
                cell.getLayoutParams().height =
                        cell.getLayoutParams().width = mCellWidth;
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<Day> week = new ArrayList<Day>();
        week.addAll(itemList.get(position));
        Day day;
        for (int dayNumb = 0; dayNumb < 7; dayNumb ++){
            if (week.get(dayNumb) != null) {
                day = week.get(dayNumb);
                holder.weekDates.get(dayNumb).setText(String.valueOf(day.Day));
                holder.weekEntry.get(dayNumb).setTag(day.Day + "_" + day.Month + "_" + day.Year);
                Picasso.with(mContext)
                        .load(day.imgURL)
                        .error(android.R.drawable.stat_notify_error)
                        .transform(mDayBackgroundTransformation)
                        .into(holder.weekImages.get(dayNumb));
                if (!day.isCurrentMonthDay()) {
                    holder.weekImages.get(dayNumb).setAlpha(0.2f);
                } else {
                    holder.weekImages.get(dayNumb).setAlpha(1.0f);
                }
            }
        }

        return convertView;

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

    public static class ViewHolder {
        TextView sunDayText, monDayText, tuesDayText, wednesDayText, thursDayText, friDayText, saturDayText;
        ImageView sunDayImage, monDayImage, tuesDayImage, wednesDayImage, thursDayImage, friDayImage, saturDayImage;
        RelativeLayout sunDay, monDay, tuesDay, wednesDay, thursDay, friDay, saturDay;
        List<TextView> weekDates;
        List<RelativeLayout> weekEntry;
        List<ImageView> weekImages;

    }

}