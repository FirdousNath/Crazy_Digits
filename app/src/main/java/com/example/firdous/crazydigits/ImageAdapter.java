package com.example.firdous.crazydigits;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by encrypted on 13/4/17.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>  {
    private Context mContext;
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;
    private  ArrayList<LevelStatus> resultList;
    // Keep all Images in array

    // Constructor
    public ImageAdapter(Context c, ArrayList <LevelStatus> result){
        mContext = c;
        resultList=result;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.level, parent, false);
        return new ViewHolder(view);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView levelno, besttime;
        ImageView star1,star2,star3;
        RelativeLayout root;

        public ViewHolder(View itemView) {
            super(itemView);
            levelno = (TextView) itemView.findViewById(R.id.levelno);
            besttime = (TextView) itemView.findViewById(R.id.besttime);
            star1 =(ImageView)itemView.findViewById(R.id.star1);
            star2 =(ImageView)itemView.findViewById(R.id.star2);
            star3 =(ImageView)itemView.findViewById(R.id.star3);
            root =(RelativeLayout)itemView.findViewById(R.id.root);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        if(resultList.get(position).isLocked())
        {
            holder.levelno.setText("");
            holder.besttime.setText("");
            holder.star1.setImageDrawable(null);
            holder.star2.setImageDrawable(null);
            holder.star3.setImageDrawable(null);
            holder.root.setBackgroundResource(R.drawable.lock);
        }
        else {
            holder.root.setBackgroundResource(R.drawable.rectangle);
            holder.levelno.setText("" + (position + 1));
            holder.besttime.setText("Best: " +
                    resultList.get(position).getTotaltime() + " sec");
            holder.star1.setImageResource(R.drawable.ratestar);
            holder.star2.setImageResource(R.drawable.ratestar);
            holder.star3.setImageResource(R.drawable.ratestar);

            switch (resultList.get(position).getStarperlevel())
            {
                case 0 : holder.star1.setImageDrawable(null);
                         holder.star2.setImageDrawable(null);
                         holder.star3.setImageDrawable(null);
                         break;
                case 1: holder.star2.setImageDrawable(null);
                        holder.star3.setImageDrawable(null);
                        break;
                case 2: holder.star3.setImageDrawable(null);
                        break;

            }
        }
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


}
