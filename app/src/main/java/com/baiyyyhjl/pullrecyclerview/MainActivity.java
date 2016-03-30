package com.baiyyyhjl.pullrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baiyyyhjl.pullrecyclerview.recyclerview.SimpleAdapter;
import com.baiyyyhjl.pullrecyclerview.recyclerview.XRecylcerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, XRecylcerView.LoadingListener {

    private XRecylcerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<String> data = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        recyclerView = (XRecylcerView) findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
//        layoutManager = new GridLayoutManager(this, 3);
//        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//        TextView titleView = new TextView(this);
//        titleView.setText("title");
//        titleView.setTextSize(30f);
//        recyclerView.addHeaderView(titleView);
        for (int i = 0; i < 20; i++) {
            data.add("data:" + i);
        }
        adapter = new SimpleAdapter();
        recyclerView.setLoadingListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String data) {
                Toast.makeText(MainActivity.this, "positino:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
    }

    /**
     * 下拉刷新请求
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 判断当前是否处于上拉加载状态
                if (!recyclerView.isLoadData()) {
                    // 表示刷新完成
                    swipeRefreshLayout.setRefreshing(false);
                    // 设置当前刷新数，当下拉刷新的时候置为0，上拉加载时已经初始化
                    recyclerView.setPreviousTotal(0);
                    // 设置是否还有更多
                    recyclerView.setIsnomore(false);
                    adapter.clear();
                    adapter.addAll(data);
                } else {
                    Toast.makeText(MainActivity.this, "当前正在刷新中", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 2000);

    }

    @Override
    public void onLoadMore() {
        Toast.makeText(MainActivity.this, "loadmore", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.addAll(data);
                recyclerView.loadMoreComplete();
            }
        }, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
