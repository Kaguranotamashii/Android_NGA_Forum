package com.example.my_nga_fornums.Article;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.my_nga_fornums.R;

import java.util.List;

// ArticleAdapter类用于管理RecyclerView的数据和视图
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{
    private static final String TAG = "ArticleAdapter";
    private Context context;
    private List<Article> articleList;
    // ArticleAdapter的构造函数，传入文章列表数据
    public ArticleAdapter(List<Article> myarticleList) {
        articleList = myarticleList;
    }

    // ViewHolder内部类，用于绑定RecyclerView中的视图元素
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView articleImage;
        TextView articleTitle;
        TextView articleTime;

        // ViewHolder的构造函数，初始化视图元素
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            articleImage = (ImageView) view.findViewById(R.id.article_image);
            articleTitle = (TextView) view.findViewById(R.id.article_name);
            articleTime = (TextView) view.findViewById(R.id.article_time);
        }
    }
    // 创建ViewHolder并返回，关联item_article布局文件
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        // 加载item_article布局文件，创建一个新的View
        View view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Article article = articleList.get(position);

                // 创建一个新的Intent，跳转到ArticleDetailActivity，并传递文章相关数据
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                intent.putExtra(ArticleDetailActivity.ARTICLE_IMAGE_ID, article.getArticleImagePath());
                intent.putExtra(ArticleDetailActivity.ARTICLE_TITLE, article.getArticleTitle());
                intent.putExtra(ArticleDetailActivity.ARTICLE_TIME, article.getArticleTime());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    // 绑定ViewHolder，设置显示数据
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.articleTitle.setText(article.getArticleTitle());
        holder.articleTime.setText(article.getArticleTime());
        // 使用Glide加载图片
        Glide.with(context)
                .load(article.getArticleImagePath())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.articleImage);
    }

    // 返回文章数目
    @Override
    public int getItemCount() {
        return articleList.size();
    }
}