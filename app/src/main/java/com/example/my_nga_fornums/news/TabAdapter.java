package com.example.my_nga_fornums.news;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.my_nga_fornums.R;
import com.example.my_nga_fornums.news.NewsBean;

import java.util.List;

//自定义新闻列表的适配器
public class TabAdapter extends BaseAdapter {

    private List<NewsBean.ResultBean.DataBean> list;

    private Context context;

    //设置正常加载图片的个数
    private int IMAGE_01 = 0;

    private int VIEW_COUNT = 3;

    public TabAdapter(Context context, List<NewsBean.ResultBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //得到不同item的总数
    @Override
    public int getViewTypeCount() {
        return VIEW_COUNT;
    }

    //得到当前新闻子项item的类型
    @Override
    public int getItemViewType(int position) {
        return IMAGE_01;
    }

    //提升ListView的运行效率，参数convertView用于将之前加载好的布局进行缓存，以便以后可以重用：https://blog.csdn.net/xiao_ziqiang/article/details/50812471
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == IMAGE_01) {
            Image01_ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_layout01, null);
                holder = new Image01_ViewHolder();
                //查找控件
                holder.author_name = (TextView) convertView.findViewById(R.id.author_name);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.image = (ImageView) convertView.findViewById(R.id.image);

                convertView.setTag(holder);
            } else {
                holder = (Image01_ViewHolder) convertView.getTag();
            }

            //获取数据重新赋值
            holder.title.setText(list.get(position).getTitle());
            holder.author_name.setText(list.get(position).getAuthor_name());

            /**
             * DiskCacheStrategy.NONE： 表示不缓存任何内容。
             */
            Glide.with(context)
                    .load(list.get(position).getThumbnail_pic_s())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.image);


        }
        return convertView;
    }

    //新增3个内部类
    class Image01_ViewHolder {
        TextView title, author_name;
        ImageView image;
    }

}