package com.example.my_nga_fornums.dongman;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DongmanFragment extends Fragment {
    private Banner banner;
    private List<String> banner_data;
    private SwipeRefreshLayout swipeRefreshLayout;

    // 动漫列表
    List<DongmanBean> mNewsList = new ArrayList<>();

    private DongmanAdapter mAdapter;
    private RecyclerView recyclerView;

    private List<DongmanInfoBean> dongmaninfos = new ArrayList<>();
    private static final int NUM_REQUESTS = 9;
    private boolean isLoading = false;

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

        // 初始化数据
        initImagesData(() -> {
            if (dongmaninfos != null) {
                for (DongmanInfoBean dongmaninfo : dongmaninfos) {
                    if (dongmaninfo.getImages() != null && dongmaninfo.getImages().getLarge() != null) {
                        DongmanBean item = new DongmanBean();
                        item.setName(dongmaninfo.getName());
                        item.setSummary(dongmaninfo.getSummary());
                        item.setImage(dongmaninfo.getImages().getLarge());
                        mNewsList.add(item);
                    }
                }
                getActivity().runOnUiThread(() -> mAdapter.notifyDataSetChanged());
            }
        });

        banner = view.findViewById(R.id.main_banner);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        initBannerData();
        banner.setAdapter(new BannerImageAdapter<String>(banner_data) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                // 使用 Glide 加载网络图片
                Glide.with(holder.itemView.getContext())
                        .load(data)
                        .into(holder.imageView);
            }
        });
        getLoadImages();
        // 开启循环轮播
        banner.isAutoLoop(true);
        banner.setIndicator(new CircleIndicator(getContext()));
        banner.setScrollBarFadeDuration(1000);
        // 设置指示器颜色(选中时的小点颜色)
        banner.setIndicatorSelectedColor(Color.GREEN);
        // 开始轮播
        banner.start();

        // 配置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(this::refreshBannerData);

        // 配置上拉加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    // 底部加载更多
                    if (!isLoading) {
                        isLoading = true;
                        loadMoreData();
                    }
                }
            }
        });

        return view;
    }

    private void initImagesData(Runnable callback) {
        String url = "https://api.bgm.tv/subject/";
        CountDownLatch latch = new CountDownLatch(NUM_REQUESTS);

        for (int i = 1; i <= NUM_REQUESTS; i++) {
            int a = new Random().nextInt(10000) + 1;
            String finalUrl = url + a;

            new Thread(() -> {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(finalUrl)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        DongmanInfoBean info = new Gson().fromJson(responseData, DongmanInfoBean.class);
                        synchronized (dongmaninfos) {
                            dongmaninfos.add(info);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        new Thread(() -> {
            try {
                latch.await();
                getActivity().runOnUiThread(callback);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void initBannerData() {
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

    private void getLoadImages() {
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

    private void refreshBannerData() {
        initBannerData();
        banner.getAdapter().notifyDataSetChanged();
        getLoadImages();
        swipeRefreshLayout.setRefreshing(false);  // 关闭刷新动画
    }

    private void loadMoreData() {
        initImagesData(() -> {
            if (dongmaninfos != null) {
                int startPosition = mNewsList.size();
                for (int i = startPosition; i < startPosition + 5 && i < dongmaninfos.size(); i++) {
                    DongmanInfoBean dongmaninfo = dongmaninfos.get(i);
                    if (dongmaninfo.getImages() != null && dongmaninfo.getImages().getLarge() != null) {
                        DongmanBean item = new DongmanBean();
                        item.setName(dongmaninfo.getName());
                        item.setSummary(dongmaninfo.getSummary());
                        item.setImage(dongmaninfo.getImages().getLarge());
                        mNewsList.add(item);
                    }
                }
                getActivity().runOnUiThread(() -> {
                    mAdapter.notifyDataSetChanged();
                    isLoading = false;
                });
            }
        });
    }



}
