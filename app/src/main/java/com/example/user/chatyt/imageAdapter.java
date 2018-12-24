package com.example.user.chatyt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class imageAdapter extends RecyclerView.Adapter<com.example.user.chatyt.imageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<SellerInfo> mUploads;
    private OnItemClickListener mListener;




    public imageAdapter(Context context, List<SellerInfo> uploads)
    {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview,viewGroup,false);
        return  new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder,final int i) {
        SellerInfo si = mUploads.get(i);
        imageViewHolder.textViewname.setText(si.getTitle());
        imageViewHolder.textViewDept.setText(si.getDepartment());
        imageViewHolder.textViewprice.setText("INR "+Integer.toString(si.getPrice()));
        Picasso.with(mContext)
                .load(si.getImageUri())
                .fit()
                .centerCrop()
                .into(imageViewHolder.imageView);


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        public TextView textViewname, textViewprice, textViewDept;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewname = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            textViewprice = itemView.findViewById(R.id.textViewPrice);
            textViewDept= itemView.findViewById(R.id.textViewShortDesc);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if(mListener != null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION)
                {
                    mListener.onItemClick(position);
                }
            }
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }




}

