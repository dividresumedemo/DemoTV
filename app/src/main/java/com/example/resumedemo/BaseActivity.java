package com.example.resumedemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.resumedemo.callBack.OnFragmentBackPressed;
import com.example.resumedemo.data.DataRepository;
import com.example.resumedemo.data.TvData;
import com.example.resumedemo.ui.detail.TvDetailFragment;
import com.example.resumedemo.ui.main.MainFragment;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseActivity extends AppCompatActivity {
    private OnFragmentBackPressed mBackListener;
    private DataRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mRepository = DataRepository.getInstance(getApplication());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.basefragment, MainFragment.newInstance()).commit();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            String type = appLinkData.toString();
            String dataNum = type.replace("http://www.example.com/dramas/","");
            getAppLinkData(dataNum);
        }
    }

    private void getAppLinkData(String dataNum) {
        new Thread(() -> {
            TvData data = mRepository.getItem(dataNum);
            if (data != null) {
                gotoFragment(new TvDetailFragment(data), false);
            }
        }).start();
    }

    public void setBackPressedListener(OnFragmentBackPressed listener) {
        mBackListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mBackListener != null) {
            if (mBackListener.onFragmentBackPressed()) {
                try {
                    super.onBackPressed();
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                return;
            }
        } else {
            super.onBackPressed();
        }
    }

    public void gotoFragment(BaseFragment fragment, boolean add) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_right);

        try {
            if (add) {
                if (!fragment.isAdded()) {
                    fragmentTransaction.add(R.id.basefragment, fragment);
                } else {
                    return;
                }
            } else {
                fragmentTransaction.replace(R.id.basefragment, fragment);
            }
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("SimpleDateFormat")
    public String parseISO8601(String time) {
        if (time != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date date = format.parse(time);
                 SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format));
                return sdf.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String parseFloat(float count) {
        DecimalFormat fnum = new DecimalFormat("##0.0");
        return fnum.format(count);
    }
}
