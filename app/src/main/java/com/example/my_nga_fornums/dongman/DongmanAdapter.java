package com.example.my_nga_fornums.dongman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_nga_fornums.R;

import java.util.List;

public class DongmanAdapter extends RecyclerView.Adapter<DongmanAdapter.DongmanViewHolder> {

    private List<DongmanBean> list;
    private Context context;

    public DongmanAdapter(List<DongmanBean> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DongmanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dongman, parent, false);
        return new DongmanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DongmanViewHolder holder, int position) {
        DongmanBean item = list.get(position);
        holder.name.setText(item.getName());
        holder.summary.setText(item.getSummary());
        holder.image.setImageResource(R.drawable.eva);
        // 使用 Glide 加载网络图片
        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.ic_vertical_align_top)  // 占位符
                .error(R.drawable.ic_edit_article)             // 加载失败时显示的图片
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class DongmanViewHolder extends RecyclerView.ViewHolder {
        TextView name, summary;
        ImageView image;

        public DongmanViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            summary = itemView.findViewById(R.id.textViewContent);
            image = itemView.findViewById(R.id.imageView);

        }
    }
}
