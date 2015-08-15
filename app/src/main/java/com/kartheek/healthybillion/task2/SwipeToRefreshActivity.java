package com.kartheek.healthybillion.task2;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kartheek.healthybillion.R;

import java.util.ArrayList;
import java.util.List;

public class SwipeToRefreshActivity extends ActionBarActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private ItemsRecyclerAdapter adapter;
    private List<String> itemsList = new ArrayList<>();
    private TextView tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_to_refresh);
        initViews();
    }

    private void initViews() {
        tvNoData = (TextView) findViewById(R.id.tvNodata);
        recyclerView = (RecyclerView) findViewById(R.id.rv_items);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        adapter = new ItemsRecyclerAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);
    }

    public void resetData(View view) {
        itemsList.clear();
        updateUI();

    }

    private void updateUI() {
        if (itemsList.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
        } else {
            tvNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(true);
        }
    }
}
