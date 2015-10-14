package com.mckuai.mcstar.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mckuai.mcstar.R;
import com.mckuai.mcstar.activity.ExaminationActivity;
import com.mckuai.mcstar.adapter.UserListAdapter;
import com.mckuai.mcstar.bean.Paper;
import com.mckuai.mcstar.bean.Questin;
import com.mckuai.mcstar.utils.NetInterface;

import java.util.ArrayList;

public class ReadyFragment extends BaseFragment  implements NetInterface.OnGetQrestionListener{
    private TextView mTimeCount;
    private SuperRecyclerView mList;
    private RelativeLayout mHint;
    private UserListAdapter mAdapter;
    private Paper mPaper;
    private int index = 0;
    private int time = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ready, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("RF","onResume");
        if (null == mPaper){
            loadData();
        } else {
            showData();
        }
    }

    private void initView(View view) {
        mTimeCount = (TextView) view.findViewById(R.id.time);
        mList = (SuperRecyclerView) view.findViewById(R.id.userlist);
        mHint= (RelativeLayout) view.findViewById(R.id.layout_hint);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
        mList.setLayoutManager(manager);
        mList.setLoadingMore(false);
    }

    private void loadData() {
        NetInterface.getQuestions(getActivity(),this);
    }

    private void showData() {
        if (null != mPaper.getUser() && !mPaper.getUser().isEmpty()){
            if (null == mAdapter){
                mAdapter = new UserListAdapter(getActivity());
                mList.setAdapter(mAdapter);
            }
            mAdapter.setData(mPaper.getUser());
        } else {
//            mHint.setVisibility(View.GONE);
//            mList.setVisibility(View.GONE);
        }

        handler.sendMessage(handler.obtainMessage(1));
    }


    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mTimeCount.setText(getActivity().getString(R.string.time_countdown,time));
                    time--;
                    if (1 == time){
                        handler.sendMessageDelayed(handler.obtainMessage(2), 1000);
                    }
                    else {
                        handler.sendMessageDelayed(handler.obtainMessage(1),1000);
                    }
                    break;
                case 2:
                    ((ExaminationActivity)getActivity()).showNextFragment();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        handler.removeMessages(1);
        handler.removeMessages(2);
        super.onDestroy();
    }

    @Override
    public void onSuccess(Paper paper) {
        this.mPaper = paper;
        showData();
    }

    @Override
    public void onFalse(String msg) {

    }
}
