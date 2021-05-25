package com.andeptrai.datn_restaurant_app_manager.Activity.ui.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andeptrai.datn_restaurant_app_manager.Activity.ui.list_food.ListFoodAdapter;
import com.andeptrai.datn_restaurant_app_manager.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CmtAdapter extends RecyclerView.Adapter {

    ArrayList<Comment> commentArrayList;
    Context mContext;

    public CmtAdapter(ArrayList<Comment> commentArrayList, Context mContext) {
        this.commentArrayList = commentArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View cmtView = layoutInflater.inflate(R.layout.iteam_cmt,parent, false);
        return new CmtViewHolder(cmtView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Comment comment = commentArrayList.get(position);
        CmtViewHolder cmtViewHolder = (CmtViewHolder) holder;
        cmtViewHolder.cmter_name.setText(comment.getNameCmter());
        cmtViewHolder.txtCmtContent.setText(comment.getContent());
        if (comment.getPointReview() != -1){
            cmtViewHolder.cmter_ratingBar.setRating((float) comment.getPointReview());
        }

        String listIdLike = comment.getListLike();
        int likeNumber = 0;
        for (int j = 0; j < listIdLike.length(); j++){
            if (listIdLike.charAt(j) >= '0' && listIdLike.charAt(j) <= '9'){
                likeNumber++;
                int k;
                for (k = j + 1; k < listIdLike.length(); k++){
                    if (listIdLike.charAt(k) >= '0' &&  listIdLike.charAt(k) <= '9') {
                    }
                    else break;
                }
                j = k - 1;
            }
        }
        comment.setLikeNumber(likeNumber);

        cmtViewHolder.txtLikeCmtNumber
                .setText(comment.getLikeNumber() + " Thích - " + comment.getCmtNumber() + " Trả lời - "
                        + comment.getShareNumber() + " Chia sẻ");
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    class CmtViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout item_cmt_relative;
        TextView cmter_name, txtCmtContent, txtLikeCmtNumber;
        RatingBar cmter_ratingBar;
        CircleImageView img_avt_cmt;

        public CmtViewHolder(@NonNull View itemView) {
            super(itemView);

            item_cmt_relative = itemView.findViewById(R.id.item_cmt_relative);
            cmter_name = itemView.findViewById(R.id.cmter_name);
            txtCmtContent = itemView.findViewById(R.id.txtCmtContent);
            txtLikeCmtNumber = itemView.findViewById(R.id.txtLikeCmtNumber);
            cmter_ratingBar = itemView.findViewById(R.id.cmter_ratingBar);
            img_avt_cmt = itemView.findViewById(R.id.img_avt_cmt);
        }
    }
}
