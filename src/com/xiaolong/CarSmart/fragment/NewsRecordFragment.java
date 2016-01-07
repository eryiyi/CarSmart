package com.xiaolong.CarSmart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.ItemNewsAdapter;
import com.xiaolong.CarSmart.base.BaseFragment;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.NewsDATA;
import com.xiaolong.CarSmart.library.PullToRefreshBase;
import com.xiaolong.CarSmart.library.PullToRefreshListView;
import com.xiaolong.CarSmart.module.News;
import com.xiaolong.CarSmart.ui.NewsDetailActivity;
import com.xiaolong.CarSmart.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/4/6.
 * ¶¯Ì¬ÐÂÎÅ
 */
public class NewsRecordFragment extends BaseFragment implements View.OnClickListener {
    private ItemNewsAdapter adapter;
    private static boolean IS_REFRESH = true;
    private PullToRefreshListView pklstv;
    List<News> lists  = new ArrayList<>();
    private int pageIndex = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_new_fragment, null);
        initView(view);
        getData();
        return view;
    }

    private void initView(View view) {
        pklstv = (PullToRefreshListView) view.findViewById(R.id.pk_new_lstv);
        adapter = new ItemNewsAdapter(lists, getActivity());
        pklstv.setMode(PullToRefreshBase.Mode.BOTH);
        pklstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getData();
            }
        });
        pklstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                News news = lists.get(position-1);
                intent.putExtra("news",news);
                startActivity(intent);
            }
        });
        pklstv.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GOODS_NEWS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            NewsDATA data = getGson().fromJson(s, NewsDATA.class);
                            if (data.getCode() == 200) {
                                if (IS_REFRESH) {
                                    lists.clear();
                                }
                                lists.addAll(data.getData());
                                pklstv.onRefreshComplete();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "category");
                params.put("cat_id", "1");
                params.put("page_index", String.valueOf(pageIndex));
                params.put("page_size", "10");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

}


