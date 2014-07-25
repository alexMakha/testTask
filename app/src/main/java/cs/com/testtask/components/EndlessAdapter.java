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
    private ArrayList<Week> itemList;
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

    public EndlessAdapter(Context ctx, ArrayList<Week> itemList, int layoutId, int cellWidth) {
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

        ViewHolder holder;
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

            holder.sunDay.setOnClickListener(this);
            holder.monDay.setOnClickListener(this);
            holder.tuesDay.setOnClickListener(this);
            holder.wednesDay.setOnClickListener(this);
            holder.thursDay.setOnClickListener(this);
            holder.friDay.setOnClickListener(this);
            holder.saturDay.setOnClickListener(this);


            holder.sunDay.getLayoutParams().height = mCellWidth;
            holder.sunDay.getLayoutParams().width = mCellWidth;

            holder.monDay.getLayoutParams().height = mCellWidth;
            holder.monDay.getLayoutParams().width = mCellWidth;

            holder.tuesDay.getLayoutParams().height = mCellWidth;
            holder.tuesDay.getLayoutParams().width = mCellWidth;

            holder.wednesDay.getLayoutParams().height = mCellWidth;
            holder.wednesDay.getLayoutParams().width = mCellWidth;

            holder.thursDay.getLayoutParams().height = mCellWidth;
            holder.thursDay.getLayoutParams().width = mCellWidth;

            holder.friDay.getLayoutParams().height = mCellWidth;
            holder.friDay.getLayoutParams().width = mCellWidth;

            holder.saturDay.getLayoutParams().height = mCellWidth;
            holder.saturDay.getLayoutParams().width = mCellWidth;

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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        List<Day> week = new ArrayList<Day>();
        week.addAll(itemList.get(position));
        Day day;

        if (week.get(0) != null) {
            day = week.get(0);
            holder.sunDayText.setText(String.valueOf(day.Day));
            holder.sunDay.setTag(day.Day + "_" + day.Month + "_" + day.Year);
            Picasso.with(mContext)
                    .load(day.imgURL)
                    .error(android.R.drawable.stat_notify_error)
                    .transform(mDayBackgroundTransformation)
                    .into(holder.sunDayImage);
            if (!day.isCurrentMonthDay()) {
                holder.sunDayImage.setAlpha(0.2f);
            } else {
                holder.sunDayImage.setAlpha(1.0f);
            }

        }
        if (week.get(1) != null) {
            day = week.get(1);
            holder.monDayText.setText(String.valueOf(day.Day));
            holder.monDay.setTag(day.Day + "_" + day.Month + "_" + day.Year);
            Picasso.with(mContext)
                    .load(day.imgURL)
                    .error(android.R.drawable.stat_notify_error)
                    .transform(mDayBackgroundTransformation)
                    .into(holder.monDayImage);
            if (!day.isCurrentMonthDay()) {
                holder.monDayImage.setAlpha(0.2f);
            } else {
                holder.monDayImage.setAlpha(1.0f);
            }
        }
        if (week.get(2) != null) {
            day = week.get(2);
            holder.tuesDayText.setText(String.valueOf(day.Day));
            holder.tuesDay.setTag(day.Day + "_" + day.Month + "_" + day.Year);
            Picasso.with(mContext)
                    .load(day.imgURL)
                    .error(android.R.drawable.stat_notify_error)
                    .transform(mDayBackgroundTransformation)
                    .into(holder.tuesDayImage);
            if (!day.isCurrentMonthDay()) {
                holder.tuesDayImage.setAlpha(0.2f);
            } else {
                holder.tuesDayImage.setAlpha(1.0f);
            }
        }
        if (week.get(3) != null) {
            day = week.get(3);
            holder.wednesDayText.setText(String.valueOf(day.Day));
            holder.wednesDay.setTag(day.Day + "_" + day.Month + "_" + day.Year);
            Picasso.with(mContext)
                    .load(day.imgURL)
                    .error(android.R.drawable.stat_notify_error)
                    .transform(mDayBackgroundTransformation)
                    .into(holder.wednesDayImage);
            if (!day.isCurrentMonthDay()) {
                holder.wednesDayImage.setAlpha(0.2f);
            } else {
                holder.wednesDayImage.setAlpha(1.0f);
            }
        }
        if (week.get(4) != null) {
            day = week.get(4);
            holder.thursDayText.setText(String.valueOf(day.Day));
            holder.thursDay.setTag(day.Day + "_" + day.Month + "_" + day.Year);
            Picasso.with(mContext)
                    .load(day.imgURL)
                    .error(android.R.drawable.stat_notify_error)
                    .transform(mDayBackgroundTransformation)
                    .into(holder.thursDayImage);
            if (!day.isCurrentMonthDay()) {
                holder.thursDayImage.setAlpha(0.2f);
            } else {
                holder.thursDayImage.setAlpha(1.0f);
            }
        }
        if (week.get(5) != null) {
            day = week.get(5);
            holder.friDayText.setText(String.valueOf(day.Day));
            holder.friDay.setTag(day.Day + "_" + day.Month + "_" + day.Year);
            Picasso.with(mContext)
                    .load(day.imgURL)
                    .error(android.R.drawable.stat_notify_error)
                    .transform(mDayBackgroundTransformation)
                    .into(holder.friDayImage);
            if (!day.isCurrentMonthDay()) {
                holder.friDayImage.setAlpha(0.2f);
            } else {
                holder.friDayImage.setAlpha(1.0f);
            }
        }
        if (week.get(6) != null) {
            day = week.get(6);
            holder.saturDayText.setText(String.valueOf(day.Day));
            holder.saturDay.setTag(day.Day + "_" + day.Month + "_" + day.Year);
            Picasso.with(mContext)
                    .load(day.imgURL)
                    .error(android.R.drawable.stat_notify_error)
                    .transform(mDayBackgroundTransformation)
                    .into(holder.saturDayImage);
            if (!day.isCurrentMonthDay()) {
                holder.saturDayImage.setAlpha(0.2f);
            } else {
                holder.saturDayImage.setAlpha(1.0f);
            }
        }
        return convertView;

    }

    public void addToEndWithGlue(List<Week> _week) {
        int firstNewDay = 0;
        for (int j = 0; j < 7; j++) {
            if (_week.get(0).get(j) != null) {
                firstNewDay = _week.get(0).get(j).getWeekDay();
                break;
            }
        }
        if (firstNewDay == 0) {
            itemList.addAll(_week);
        } else {
            Week week = _week.get(0);
            for (int i = firstNewDay; i < 7; i++) {
                itemList.get(itemList.size() - 1).add(i, week.get(i));
            }
            _week.remove(0);
            itemList.addAll(_week);
        }
    }

    public void addToStartWithGlue(List<Week> list) {
        int firstOldDay = 0;
        for (int j = 6; j >= 0; ) {
            if (list.get(list.size() - 1).get(j) != null) {
                firstOldDay = list.get(list.size() - 1).get(j).getWeekDay();
                break;
            }
            j--;
        }
        if (firstOldDay == 6) {
            for (int i = list.size() - 1; i >= 0; ) {
                itemList.add(0, list.get(i));
                i--;
            }
        } else {
            Week week = itemList.get(0);
            for (int i = firstOldDay + 1; i <= 6; i++) {

                list.get(list.size() - 1).add(i, week.get(i));
            }
            itemList.remove(0);
            for (int i = list.size() - 1; i >= 0; ) {
                itemList.add(0, list.get(i));
                i--;
            }
        }
    }

    public void setNextMonthToCurrent(final int firstItem, final int count) {
        if (itemList.get(firstItem).get(6) == null) {
            ((MyApplication) ((MainActivity) mContext).getApplication()).isLoad = false;
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
                    ((MyApplication) ((MainActivity) mContext).getApplication()).isLoad = false;
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
            ((MyApplication) ((MainActivity) mContext).getApplication()).isLoad = false;
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
    }


}