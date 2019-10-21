package com.example.resumedemo.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resumedemo.BaseActivity;
import com.example.resumedemo.R;
import com.example.resumedemo.callBack.TvDataCallBack;
import com.example.resumedemo.data.TvData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TvDataAdapter extends RecyclerView.Adapter<TvDataAdapter.ViewHolder> {
    private final List<TvData> mDataList;
    private final TvDataCallBack mCallBack;
    private LayoutInflater mInflater;
    private Context mConText;

    public TvDataAdapter(Context context, List<TvData> mDataList, TvDataCallBack mCallBack) {
        this.mDataList = mDataList;
        this.mCallBack = mCallBack;
        this.mConText = context;
        mInflater = (LayoutInflater) mConText.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TvDataAdapter.ViewHolder(mInflater.inflate(R.layout.tv_data_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaseActivity activity = (BaseActivity) mConText;
        TvData data = mDataList.get(position);
        holder.title.setText(data.getName());
        holder.date.setText(activity.parseISO8601(data.getCreated_at()));
        holder.rating.setText(activity.parseFloat(data.getRating()));
        holder.mainLayout.setOnClickListener(v -> mCallBack.onDataCallBack(data));
        Picasso.get()
                .load(data.getThumb())
                .resize(200, 600)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        ImageView imageView;
        TextView title;
        TextView date;
        TextView rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.main_layout);
            imageView = itemView.findViewById(R.id.tv_img);
            title = itemView.findViewById(R.id.tv_title);
            date = itemView.findViewById(R.id.tv_date);
            rating = itemView.findViewById(R.id.tv_rating);
        }
    }
}
