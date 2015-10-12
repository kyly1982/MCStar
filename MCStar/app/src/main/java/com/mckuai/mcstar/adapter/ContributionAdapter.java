package com.mckuai.mcstar.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.AndroidCharacter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.bean.Questin;

import java.util.ArrayList;

/**
 * Created by kyly on 2015/10/12.
 */
public class ContributionAdapter extends RecyclerView.Adapter<ContributionAdapter.ViewHolder>  {
    private ArrayList<Questin> mQuestins;
    private Context mContext;

    public ContributionAdapter(@NonNull Context context){
        this.mContext = context;
    }

    public void setData(@NonNull ArrayList<Questin> questins){
        this.mQuestins = questins;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.item_contribution,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null != mQuestins && !mQuestins.isEmpty()){
            Questin questin = mQuestins.get(position);
            holder.ratio.setText(questin.getTopic()+"");
            holder.userCount.setText(questin.getUseCount()+"");
            holder.time.setText(questin.getTime()+"");

            Drawable drawable =null;

            switch (questin.getStatus()){
                case Pass:
                    //审核通过
                    mContext.getResources().getDrawable(android.support.design.R.drawable.abc_ic_menu_copy_mtrl_am_alpha);
                    holder.userCount.setText(questin.getUseCount()+"");
                    holder.ratio.setText(questin.getCorrectCount() * 100 / questin.getUseCount() + "%");
                    break;
                case False:
                    //审核未通过
                    drawable =  mContext.getResources().getDrawable(android.support.design.R.drawable.abc_ic_menu_paste_mtrl_am_alpha);
                    holder.userCount.setText(mContext.getResources().getString(R.string.status_audit_false));
                    break;
                default:
                    //审核中
                    drawable =  mContext.getResources().getDrawable(android.support.design.R.drawable.abc_ic_menu_share_mtrl_alpha);
                    holder.userCount.setText(mContext.getString(R.string.status_auditing));
                    break;
            }
            drawable.setBounds(0,0,drawable.getMinimumWidth(), drawable.getMinimumHeight());
            //holder.userCount.setCompoundDrawables(drawable,null,null,null);
        }
    }

    @Override
    public int getItemCount() {
        return null == mQuestins ? 0:mQuestins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView topic;
        TextView userCount;
        TextView time;
        TextView ratio;

        public ViewHolder(View itemView) {
            super(itemView);
            topic = (TextView) itemView.findViewById(R.id.topic);
            userCount = (TextView) itemView.findViewById(R.id.status);
            time = (TextView) itemView.findViewById(R.id.time);
            ratio = (TextView) itemView.findViewById(R.id.ratio);
        }
    }
}
