package com.example.resumedemo.ui.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.resumedemo.BaseFragment;
import com.example.resumedemo.R;
import com.example.resumedemo.data.TvData;
import com.squareup.picasso.Picasso;

public class TvDetailFragment extends BaseFragment {
    private TvData mData;

    public TvDetailFragment(TvData mData) {
        this.mData = mData;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        ImageView back = view.findViewById(R.id.back_press);
        back.setOnClickListener(v -> gotoBack());

        setupView(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setupView(View view) {
        if (mData == null) return;
        ImageView mImage = view.findViewById(R.id.tv_detail_img);
        TextView mTitle = view.findViewById(R.id.tv_detail_title);
        TextView mRating = view.findViewById(R.id.tv_detail_rating);
        TextView mCreatedAt = view.findViewById(R.id.tv_detail_created_date);
        TextView mTotalViews = view.findViewById(R.id.tv_detail_total_views);

        Picasso.get().load(mData.getThumb()).into(mImage);
        mTitle.setText(getBaseActivity().getString(R.string.detail_name) + mData.getName());
        mRating.setText(getBaseActivity().getString(R.string.detail_rating) + getBaseActivity().parseFloat(mData.getRating()));
        mCreatedAt.setText(getBaseActivity().getString(R.string.detail_created_at) + getBaseActivity().parseISO8601(mData.getCreated_at()));
        mTotalViews.setText(getBaseActivity().getString(R.string.detail_total_views) + mData.getTotal_views());
    }
}
