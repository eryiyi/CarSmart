package com.xiaolong.CarSmart.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.data.DaohangDATA;
import com.xiaolong.CarSmart.module.Daohang;
import com.xiaolong.CarSmart.util.StringUtil;
import com.xiaolong.CarSmart.widget.CharacterParser;
import com.xiaolong.CarSmart.widget.PinyinComparator;
import com.xiaolong.CarSmart.widget.SideBar;
import com.xiaolong.CarSmart.widget.SortAdapter;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Administrator on 2015/8/18.
 */
public class DaohangActivity extends BaseActivity implements View.OnClickListener {
    private List<Daohang> SourceDateList = new ArrayList<Daohang>();
    private ImageView back;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daohang_activity);
        initView();
        initViews();

        Resources res =getResources();
        String message = res.getString(R.string.is_dengluing).toString();
        progressDialog = new ProgressDialog(DaohangActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();

        getData();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);


    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;
        }
    }
    public void back(){
        finish();
    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.DAOHANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    DaohangDATA data = getGson().fromJson(s, DaohangDATA.class);
                                    if (data.getCode() == 200) {
                                        SourceDateList.clear();
                                        SourceDateList.addAll(data.getData());
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(DaohangActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DaohangActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(DaohangActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "allBrand");
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

    private void initViews() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent detailView = new Intent(DaohangActivity.this, SearchGoodsByBrandIdActivity.class);
                detailView.putExtra("brand_id", ((Daohang) adapter.getItem(position)).getId());
                startActivity(detailView);
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                Toast.makeText(getApplication(), ((Daohang) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        SourceDateList = filledData(SourceDateList);

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
    }


    /**
     * 为ListView填充数据
     * @param date
     * @return
     */
    private List<Daohang> filledData(List<Daohang> date){
        for(int i=0; i<date.size(); i++){
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                date.get(i).setSortLetters(sortString.toUpperCase());
            }else{
                date.get(i).setSortLetters("#");
            }

        }
        return date;
    }

}
