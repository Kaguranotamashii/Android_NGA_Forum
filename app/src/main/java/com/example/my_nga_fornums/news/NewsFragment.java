package com.example.my_nga_fornums.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.my_nga_fornums.MainActivity;
import com.example.my_nga_fornums.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class NewsFragment extends Fragment {
    private ListView newsListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<NewsBean.ResultBean.DataBean> contentItems = new ArrayList<>();
    private List<NewsBean.ResultBean.DataBean> allNewsItems = new ArrayList<>();
    private static final int UPNEWS_INSERT = 0;
    private String currentTabName = "top";
    private int pageNo = 0, pageSize = 10;
    private FloatingActionButton fab;
    private TabAdapter adapter;
    private boolean isLoading = false;

    @SuppressLint("HandlerLeak")
    private Handler newsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPNEWS_INSERT:
                    allNewsItems = ((NewsBean) msg.obj).getResult().getData();
                    loadMoreData();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_list, container, false);
        newsListView = view.findViewById(R.id.newsListView);
        fab = view.findViewById(R.id.fab);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        return view;
    }


    //
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onAttach(getContext());
        Bundle bundle = getArguments();
        final String category = bundle.getString("name", "top");
        currentTabName = category;

        fab.setOnClickListener(v -> newsListView.smoothScrollToPosition(0));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorRed);
        swipeRefreshLayout.setOnRefreshListener(() -> threadLoaderData(category));

        getDataFromNet();

        //跳转到web里面
        newsListView.setOnItemClickListener((parent, view, position, id) -> {
            String url = contentItems.get(position).getUrl();
            String uniquekey = contentItems.get(position).getUniquekey();
            String newsTitle = contentItems.get(position).getTitle();
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("pageUrl", url);
            intent.putExtra("uniquekey", uniquekey);
            intent.putExtra("news_title", newsTitle);
            startActivity(intent);
        });

        newsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && newsListView.getLastVisiblePosition() == contentItems.size() - 1 && !isLoading) {
                    swipeRefreshLayout.setRefreshing(true); // 显示刷新动画
                    loadMoreData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });
    }

    private void loadMoreData() {
        int start = contentItems.size();
        int end = Math.min(start + pageSize, allNewsItems.size());
        if (start < end) {
            isLoading = true;
            new Handler().postDelayed(() -> {
                contentItems.addAll(allNewsItems.subList(start, end));
                if (adapter == null) {
                    adapter = new TabAdapter(getActivity(), contentItems);
                    newsListView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false); // 隐藏刷新动画
                isLoading = false;
            }, 1500); // 模拟网络延迟
        } else {
            swipeRefreshLayout.setRefreshing(false); // 隐藏刷新动画
        }
    }

    private void threadLoaderData(final String category) {
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(() -> {
                getDataFromNet();
                swipeRefreshLayout.setRefreshing(false);
            });
        }).start();
    }

    private void getDataFromNet() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String path = "http://101.42.105.71:9999/article/articleAllInfo";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(path).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful() && response.body() != null) {
                        return response.body().string();
                    } else {
                        return "404";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "404";
                }
            }

            @Override
            protected void onPostExecute(final String result) {
                new Thread(() -> {
                    NewsBean newsBean = null;
                    if (!result.startsWith("404")) {
                        newsBean = new Gson().fromJson(result, NewsBean.class);
                        if ("0".equals("" + newsBean.getError_code())) {
                            Message msg = newsHandler.obtainMessage();
                            msg.what = UPNEWS_INSERT;
                            msg.obj = newsBean;
                            newsHandler.sendMessage(msg);
                        } else {
                            threadLoaderData(currentTabName);
                        }
                    } else {
                        threadLoaderData(result.substring(3));
                    }
                }).start();
            }
        };
        task.execute();
    }

    private String streamToString(InputStream inputStream, String charset) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                builder.append(s);
            }
            bufferedReader.close();
            inputStreamReader.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
