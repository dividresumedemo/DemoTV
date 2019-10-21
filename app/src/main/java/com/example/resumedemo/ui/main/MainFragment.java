package com.example.resumedemo.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resumedemo.BaseFragment;
import com.example.resumedemo.R;
import com.example.resumedemo.callBack.ConnectListener;
import com.example.resumedemo.callBack.TvDataCallBack;
import com.example.resumedemo.data.DataRepository;
import com.example.resumedemo.data.JsonData;
import com.example.resumedemo.data.ResultHttpData;
import com.example.resumedemo.data.SharedPerferenceWorker;
import com.example.resumedemo.data.TvData;
import com.example.resumedemo.database.TvDataBase;
import com.example.resumedemo.netWorker.NetWorkerClass;
import com.example.resumedemo.ui.detail.TvDetailFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

public class MainFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private ContentLoadingProgressBar mProgressBar;
    private SearchView mSearchView;
    private ImageView mSearchButton;
    private DataRepository mRepository;
    private SharedPerferenceWorker mDatamanager;
    private LinearLayout mNodataLayout;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatamanager = SharedPerferenceWorker.getInstance(getBaseActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.main_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.search_data_recycler);
        mProgressBar = view.findViewById(R.id.search_progress);
        mSearchView = view.findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(textListener);
        mSearchView.setOnCloseListener(closeListener);
        mNodataLayout = view.findViewById(R.id.no_data_layout);
        mSearchButton = view.findViewById(R.id.open_search_button);
        mSearchButton.setOnClickListener(v -> openSearchView());
        mRepository = DataRepository.getInstance(getBaseActivity().getApplication());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getBaseActivity(),2));

        getWebData();

        return view;
    }

    private void openSearchView() {
        mSearchView.setVisibility(View.VISIBLE);
        mSearchButton.setVisibility(View.GONE);
        mSearchView.setIconified(false);
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchButton.setVisibility(View.VISIBLE);
                mSearchView.setVisibility(View.GONE);
                return true;
            }
        });
    }

    private void setDataShow(List<TvData> value) {
        if (mDatamanager.getData("userquery") != null) {
            String query = (String)mDatamanager.getData("userquery");
            if (!query.isEmpty()) {
                mSearchView.setVisibility(View.VISIBLE);
                mSearchButton.setVisibility(View.GONE);
                mSearchView.setIconified(false);
                Snackbar.make(Objects.requireNonNull(getView()), getBaseActivity().getString(R.string.open_last_time_search), Snackbar.LENGTH_LONG)
                        .show();
            }
            mSearchView.setQuery((String)mDatamanager.getData("userquery"),false);
        } else {
            TvDataAdapter adapter = new TvDataAdapter(getBaseActivity(), value, tvCallBack);
            mRecyclerView.setAdapter(adapter);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.hide();
    }

    private void insertDataToDataBase(List<TvData> value){
        TvDataBase database = TvDataBase.getInstance(getBaseActivity());
        for (TvData data : value) {
            new Thread(() ->{
                database.tvDataDao().insert(data);
            }).start();
        }
    }

    private void getWebData() {
        NetWorkerClass.getInstance().getWebData("https://static.linetv.tw/interview/dramas-sample.json", connectListener);
    }

    private TvDataCallBack tvCallBack = new TvDataCallBack() {

        @Override
        public void onDataCallBack(TvData data) {
            mSearchView.clearFocus();
            gotoNextPage(new TvDetailFragment(data),true);
        }
    };

    private SearchView.OnCloseListener closeListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            mSearchView.setVisibility(View.GONE);
            mSearchButton.setVisibility(View.VISIBLE);
            return true;
        }
    };

    private SearchView.OnQueryTextListener textListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            getDataFromDb(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            mDatamanager.setData("userquery",newText);
            getDataFromDb(newText);
            return true;
        }
    };

    private void getDataFromDb(String query) {
        query = "%" + query + "%";
        mRepository.getDealsListInfo(query).observe(getBaseActivity(), new Observer<List<TvData>>() {
            @Override
            public void onChanged(List<TvData> tvData) {
                if (tvData != null && tvData.size() > 0) {
                    TvDataAdapter adapter = new TvDataAdapter(getBaseActivity(), tvData, tvCallBack);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.setAdapter(adapter);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mNodataLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setTvData() {
        List<TvData> tvDataList = mRepository.getAll();
        if (tvDataList != null && tvDataList.size() > 0) {
            setDataShow(tvDataList);
        }
    }

    private ConnectListener connectListener = new ConnectListener() {
        @Override
        public void onResult(ResultHttpData data) {
            getBaseActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (data.getRtnCode() == 200 && data.getRtnData() != null) {
                        final JsonData value = (JsonData) data.getRtnData();
                        try {
                            insertDataToDataBase(value.getDataArray());
                            setDataShow(value.getDataArray());
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    } else {
                        if (mRepository != null) {
                            setTvData();
                        }
                    }
                }
            });
        }
    };
}
