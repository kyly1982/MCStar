package com.mckuai.mcstar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.bean.MCUser;
import com.mckuai.mcstar.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by kyly on 2015/10/12.
 */
public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder>  {
    private ArrayList<MCUser> mUser;
    private Context mContext;
    private ImageLoader mLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public RankingAdapter(@NonNull Context context){
        this.mContext = context;
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
    }

    public void setData(@NonNull ArrayList<MCUser> mUser){
        this.mUser = mUser;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.item_ranking,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null != mUser && !mUser.isEmpty()){
            MCUser user = mUser.get(position);
            holder.ranking.setText((user.getRanking() +1)+"");
            holder.name.setText(user.getNickName());
            holder.score.setText(mContext.getString(R.string.scores,user.getAllScore()));
            if (null != user.getHeadImg() && 10 < user.getHeadImg().length()){
                mLoader.displayImage(user.getHeadImg(),holder.cover,options);
            }
            if (0 == user.getRanking()){
                holder.lab.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            } else {
                holder.lab.setBackgroundColor(mContext.getResources().getColor(R.color.white_overlay));
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == mUser ? 0:mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ranking;
        TextView name;
        TextView score;
        ImageView cover;
        RelativeLayout lab;

        public ViewHolder(View itemView) {
            super(itemView);
            ranking = (TextView) itemView.findViewById(R.id.item_userranking);
            name = (TextView) itemView.findViewById(R.id.item_username);
            score = (TextView) itemView.findViewById(R.id.item_userscore);
            cover = (ImageView) itemView.findViewById(R.id.item_usercover);
            lab = (RelativeLayout) itemView.findViewById(R.id.item_ranklab);
        }
    }
}
