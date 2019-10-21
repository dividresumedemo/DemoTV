package com.example.resumedemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.resumedemo.callBack.OnFragmentBackPressed;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseFragment extends Fragment implements OnFragmentBackPressed {
    private String TAG = BaseFragment.class.getSimpleName();
    private BaseActivity mActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        if (BaseActivity.class.isInstance(activity)) {
            mActivity = (BaseActivity) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mActivity != null){
            mActivity.setBackPressedListener(this);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void gotoBack() {
        if (getBaseActivity() != null) {
            getBaseActivity().getSupportFragmentManager().popBackStack();
        } else {
            Log.d(TAG, "mActivity is null(gotoBackPage)");
        }
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    @Override
    public boolean onFragmentBackPressed() {
        return true;
    }

    public void gotoNextPage(BaseFragment fragment, boolean add) {
        if (mActivity != null) {
            mActivity.gotoFragment(fragment, add);
        } else {
            Log.d(TAG, "mActivity is null(gotoNextPage)");
        }
    }
}
