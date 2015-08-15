package com.kartheek.healthybillion.task2;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.kartheek.healthybillion.OnStringResponseListener;
import com.kartheek.healthybillion.R;
import com.kartheek.healthybillion.utils.ResponseUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwipeToRefreshActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnStringResponseListener {

    private static final String REQUEST_TAG = "items_request";
    private static final String TAG = SwipeToRefreshActivity.class.getSimpleName();
    private static final String REQUEST_URL = "http://thehealthybillion.com/assignment/q3.txt";
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
        initToolbar();
        tvNoData = (TextView) findViewById(R.id.tvNodata);
        recyclerView = (RecyclerView) findViewById(R.id.rv_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        adapter = new ItemsRecyclerAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);
        updateUI();
    }

    public void resetData(View view) {
        itemsList.clear();
        updateUI();
    }

    private void updateUI() {
        if (itemsList.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Healthy Billion");
    }

    @Override
    public void onRefresh() {
        ResponseUtilities.getInstance().getStringResponseFromUrl(this, 2011, this, REQUEST_URL, REQUEST_TAG);
    }

    @Override
    public void onResponse(String response, int requestId) {
        refreshLayout.setRefreshing(false);
        Log.d(TAG, "Response : " + response);
        String[] values = response.split("\\n");
        if (itemsList.size() < values.length) {
            int initCount = itemsList.size();
            for (int i = itemsList.size(); i < values.length && (itemsList.size() - initCount) < 3; i++) {
                itemsList.add(values[i]);
            }
            Collections.sort(itemsList);
            updateUI();
        } else {
            Toast.makeText(this, "All entries are upto data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError errorResponse, int requestId) {
        refreshLayout.setRefreshing(false);
        errorResponse.printStackTrace();
        Toast.makeText(this, "Error in getting the data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void parseNetworkResponse(NetworkResponse response, int requestId) {

    }

    @Override
    public Map<String, String> getParams(int requestId) {
        return new HashMap<>();
    }
}
