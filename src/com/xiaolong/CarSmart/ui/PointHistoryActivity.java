package com.xiaolong.CarSmart.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.ItemPointHisAdapter;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.module.PointHistory;

import java.util.List;

/**
 * Created by Administrator on 2015/8/23.
 * 积分历史
 */
public class PointHistoryActivity extends BaseActivity implements View.OnClickListener {
    private List<PointHistory> pointHistories;

    private ImageView back;
    private ListView lstv;
    private ItemPointHisAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_history_activity);
        pointHistories = (List<PointHistory>) getIntent().getExtras().get("pointHistories");
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemPointHisAdapter(pointHistories,PointHistoryActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;
        }
    }
}
