package com.mckuai.mcstar.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.activity.ExaminationActivity;

public class ReadyFragment extends BaseFragment {
    TextView paperName;
    TextView timeCount;
    int time = 10;

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
        loadData();
    }

    private void initView(View view) {
        paperName = (TextView) view.findViewById(R.id.paper_name);
        timeCount = (TextView) view.findViewById(R.id.time);
    }

    private void loadData() {
        showData();
    }

    private void showData() {
        handler.sendMessage(handler.obtainMessage(1));
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    timeCount.setText(time+"");
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
}
