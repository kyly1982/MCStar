package com.mckuai.mcstar.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.activity.ExaminationActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {


    public AnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answer, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        ((RadioGroup)view.findViewById(R.id.rg)).setOnCheckedChangeListener(this);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    ((ExaminationActivity)getActivity()).showNextFragment();
                    break;
                default:
                    handler.sendEmptyMessageDelayed(1,3000);
                    break;
            }
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.answerA:
                break;
            case R.id.answerB:
                break;
            case R.id.answerC:
                break;
            case R.id.answerD:
                break;
        }
        handler.sendEmptyMessage(0);
    }
}
