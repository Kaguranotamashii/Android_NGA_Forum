package com.example.my_nga_fornums.dongman;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.my_nga_fornums.R;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DongmanFragment extends Fragment {
    private Banner banner;
    private List<String> banner_data;
    private SwipeRefreshLayout swipeRefreshLayout;

    //动漫列表
    List<DongmanBean> mNewsList = new ArrayList<>();

    private DongmanAdapter mAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dongman_images, container, false);
        recyclerView = view.findViewById(R.id.follow_list);

        mAdapter = new DongmanAdapter(mNewsList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        for (int i = 0; i < 50; i++) {
            DongmanBean item = new DongmanBean();
            item.setName("新闻" + i);
            item.setSummary("摘要" + i);

            mNewsList.add(item);
        }

        banner = view.findViewById(R.id.main_banner);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        initData();
        banner.setAdapter(new BannerImageAdapter<String>(banner_data) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                // 使用 Glide 加载网络图片
                Glide.with(holder.itemView.getContext())
                        .load(data)
                        .into(holder.imageView);
            }
        });
        getLoadImgaes();
        // 开启循环轮播
        banner.isAutoLoop(true);
        banner.setIndicator(new CircleIndicator(getContext()));
        banner.setScrollBarFadeDuration(1000);
        // 设置指示器颜色(TODO 即选中时那个小点的颜色)
        banner.setIndicatorSelectedColor(Color.GREEN);
        // 开始轮播
        banner.start();

        // 配置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 重新加载数据
                refreshData();
            }
        });

        return view;
    }

    private void initData() {
        String url = "https://gitcode.net/qq_44112897/images/-/raw/master/comic/";
        banner_data = new ArrayList<>();
        Set<Integer> uniqueNums = new HashSet<>();
        // 生成5个唯一的1到49之间的随机数
        while (uniqueNums.size() < 5) {
            uniqueNums.add((int) (Math.random() * 49 + 1));
        }
        // 将生成的数字以数字.jpg形式添加到banner_data中
        for (int num : uniqueNums) {
            banner_data.add(url + num + ".jpg");
        }
    }

    private void getLoadImgaes() {
        banner.setAdapter(new BannerImageAdapter<String>(banner_data) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                // 使用 Glide 加载网络图片
                Glide.with(holder.itemView.getContext())
                        .load(data)
                        .into(holder.imageView);
            }
        });
    }

    private void refreshData() {
        // 这里重新生成数据并更新 banner
        initData();
        banner.getAdapter().notifyDataSetChanged();
        getLoadImgaes();
        swipeRefreshLayout.setRefreshing(false);  // 关闭刷新动画
    }
}