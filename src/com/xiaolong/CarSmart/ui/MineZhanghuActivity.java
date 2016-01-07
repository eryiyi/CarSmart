package com.xiaolong.CarSmart.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.CarSmart.CarSmartApplication;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.AnimateFirstDisplayListener;
import com.xiaolong.CarSmart.base.BaseActivity;
import com.xiaolong.CarSmart.base.InternetURL;
import com.xiaolong.CarSmart.module.Member;
import com.xiaolong.CarSmart.module.User;
import com.xiaolong.CarSmart.upload.CommonUtil;
import com.xiaolong.CarSmart.util.CompressPhotoUtil;
import com.xiaolong.CarSmart.util.Constants;
import com.xiaolong.CarSmart.util.StringUtil;
import com.xiaolong.CarSmart.widget.DateTimePickDialogUtil;
import com.xiaolong.CarSmart.widget.SelectPhoPopWindow;
import com.xiaolong.CarSmart.widget.SexPopWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/25.
 * zhagnhu
 */
public class MineZhanghuActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ImageView mine_cover;
    private TextView mine_nickname;
    private EditText mine_nicheng;
    private EditText mine_qq;
    private EditText mine_msn;
    private EditText mine_zip;
    private EditText mine_tel;
    private EditText mine_area;
    private EditText mine_mobile;
    private TextView dateline;
    private TextView mine_sex;
    private LinearLayout mine_address_mng;
    private LinearLayout squite;
    private User user;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private SelectPhoPopWindow deleteWindow;

    private String pics = "";
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/liangxun/PhotoCache");
    private ProgressDialog progressDialog;
    private String txpic = "";

    private SexPopWindow sexPopWindow;

    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        user= (User) getIntent().getExtras().get("user");
        setContentView(R.layout.mine_zhanghu_activity);
        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() throws Exception {
        back = (ImageView) this.findViewById(R.id.back);
        mine_cover = (ImageView) this.findViewById(R.id.mine_cover);
        mine_nickname = (TextView) this.findViewById(R.id.mine_nickname);
        mine_nicheng = (EditText) this.findViewById(R.id.mine_nicheng);
        mine_nicheng.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!StringUtil.isNullOrEmpty(mine_nicheng.getText().toString())){
                            Resources res = getBaseContext().getResources();
                            String message = res.getString(R.string.please_wait).toString();
                            progressDialog = new ProgressDialog(MineZhanghuActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage(message);
                            progressDialog.show();
                            reset();
                    }
                }
            }
        });
        mine_qq = (EditText) this.findViewById(R.id.mine_qq);
        mine_qq.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!StringUtil.isNullOrEmpty(mine_qq.getText().toString())){
                            Resources res = getBaseContext().getResources();
                            String message = res.getString(R.string.please_wait).toString();
                            progressDialog = new ProgressDialog(MineZhanghuActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage(message);
                            progressDialog.show();
                            reset();
                    }
                }
            }
        });
        mine_msn = (EditText) this.findViewById(R.id.mine_msn);
        mine_msn.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!StringUtil.isNullOrEmpty(mine_msn.getText().toString())){
                            Resources res = getBaseContext().getResources();
                            String message = res.getString(R.string.please_wait).toString();
                            progressDialog = new ProgressDialog(MineZhanghuActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage(message);
                            progressDialog.show();
                            reset();
                    }
                }
            }
        });
        mine_zip = (EditText) this.findViewById(R.id.mine_zip);
        mine_zip.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!StringUtil.isNullOrEmpty(mine_zip.getText().toString())){
                            Resources res = getBaseContext().getResources();
                            String message = res.getString(R.string.please_wait).toString();
                            progressDialog = new ProgressDialog(MineZhanghuActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage(message);
                            progressDialog.show();
                            reset();
                    }
                }
            }
        });
        mine_area = (EditText) this.findViewById(R.id.mine_area);
        mine_area.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!StringUtil.isNullOrEmpty(mine_area.getText().toString())){
                            Resources res = getBaseContext().getResources();
                            String message = res.getString(R.string.please_wait).toString();
                            progressDialog = new ProgressDialog(MineZhanghuActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage(message);
                            progressDialog.show();
                            reset();
                    }
                }
            }
        });
        mine_tel = (EditText) this.findViewById(R.id.mine_tel);
        mine_tel.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!StringUtil.isNullOrEmpty(mine_tel.getText().toString())){
                            Resources res = getBaseContext().getResources();
                            String message = res.getString(R.string.please_wait).toString();
                            progressDialog = new ProgressDialog(MineZhanghuActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage(message);
                            progressDialog.show();
                            reset();
                    }
                }
            }
        });
        mine_mobile = (EditText) this.findViewById(R.id.mine_mobile);
        mine_mobile.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!StringUtil.isNullOrEmpty(mine_mobile.getText().toString())){
                            Resources res = getBaseContext().getResources();
                            String message = res.getString(R.string.please_wait).toString();
                            progressDialog = new ProgressDialog(MineZhanghuActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage(message);
                            progressDialog.show();
                            reset();
                    }
                }
            }
        });
        mine_sex = (TextView) this.findViewById(R.id.mine_sex);
        dateline = (TextView) this.findViewById(R.id.dateline);
        mine_address_mng = (LinearLayout) this.findViewById(R.id.mine_address_mng);
        squite = (LinearLayout) this.findViewById(R.id.squite);

        back.setOnClickListener(this);

        mine_cover.setOnClickListener(this);
        mine_sex.setOnClickListener(this);
        squite.setOnClickListener(this);
        mine_address_mng.setOnClickListener(this);

        imageLoader.displayImage(InternetURL.INTERNAL + user.getHead_ico(), mine_cover, CarSmartApplication.options, animateFirstListener);
        mine_nickname.setText(user.getUsername());
        if(user != null){
            mine_nicheng.setText(user.getTrue_name()==null?"":user.getTrue_name());
            if("1".equals(user.getSex())){
                mine_sex.setText(getResources().getString(R.string.sexnan));
            }else if("2".equals(user.getSex())){
                mine_sex.setText(getResources().getString(R.string.sexnv));
            }else {
                mine_sex.setText(getResources().getString(R.string.baomi));
            }

            mine_qq.setText(user.getQq() == null ? "" : user.getQq());
            mine_msn.setText(user.getMsn() == null ? "" : user.getMsn());
            mine_area.setText(user.getArea() == null ? "" : user.getArea());
            mine_mobile.setText(user.getMobile() == null ? "" : user.getMobile());
            mine_tel.setText(user.getTelephone() == null ? "" : user.getTelephone());
            mine_zip.setText(user.getZip() == null ? "" : user.getZip());

            if(!StringUtil.isNullOrEmpty(user.getBirthday())){
                dateline.setText(user.getBirthday()==null?"":user.getBirthday());
            }else {
                dateline.setText(StringUtil.getFrontBackStrDate(String.valueOf(df.format(new Date())),"yyyyMMdd",-1));
            }
            dateline.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DateTimePickDialogUtil dateTimePicKDialog = null;
                    try {
                        dateTimePicKDialog = new DateTimePickDialogUtil(
                                MineZhanghuActivity.this,  StringUtil.getFrontBackStrDate(String.valueOf(dateline.getText().toString()), "yyyyMMdd",0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dateTimePicKDialog.dateTimePicKDialog(dateline);
                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.mine_cover:
                //点击头像
                ShowPickDialog();
                break;
            case R.id.mine_sex:
                //性别
                ShowSexDialog();
                break;
            case R.id.mine_address_mng:
                Intent addressView =  new Intent(MineZhanghuActivity.this, MineAddressActivity.class);
                startActivity(addressView);
                //地址管理
                break;
            case R.id.squite:
                //账户安全
                Intent resetPwr = new Intent(MineZhanghuActivity.this, MineResetPwrActivity.class);
                startActivity(resetPwr);
                break;
        }
    }
    // 性别选择
    private void ShowSexDialog() {
        sexPopWindow = new SexPopWindow(MineZhanghuActivity.this, itemsOnClickTwo);
        //显示窗口
        sexPopWindow.showAtLocation(MineZhanghuActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClickTwo = new View.OnClickListener() {
        public void onClick(View v) {
            sexPopWindow.dismiss();
            switch (v.getId()) {
                case R.id.sex_man: {
                    reset();
                    mine_sex.setText(getResources().getString(R.string.sexnan));
                }
                break;
                case R.id.sex_woman: {
                    reset();
                    mine_sex.setText(getResources().getString(R.string.sexnv));
                }
                break;
                default:
                    break;
            }
        }
    };

    // 选择相册，相机
    private void ShowPickDialog() {
        deleteWindow = new SelectPhoPopWindow(MineZhanghuActivity.this, itemsOnClick);
        //显示窗口
        deleteWindow.showAtLocation(MineZhanghuActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            deleteWindow.dismiss();
            switch (v.getId()) {
                case R.id.camera: {
                    Intent camera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(),
                                    "ppCover.jpg")));
                    startActivityForResult(camera, 2);
                }
                break;
                case R.id.mapstorage: {
                    Intent mapstorage = new Intent(Intent.ACTION_PICK, null);
                    mapstorage.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(mapstorage, 1);
                }
                break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/ppCover.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            if (photo != null) {
                pics = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);
                mine_cover.setImageBitmap(photo);

                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.is_dengluing).toString();
                progressDialog = new ProgressDialog(MineZhanghuActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(message);
                progressDialog.show();
                sendCover();
            }
        }
    }


    public void sendCover(){
        File file = new File(pics);
        Map<String, File> files = new HashMap<String, File>();
        files.put("head_ico", file);
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "member_update");
        params.put("username", getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class));
        CommonUtil.addPutUploadFileRequest(
                this,
                InternetURL.PERSON_URL,
                files,
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
//                                    UploadData data = getGson().fromJson(s, UploadData.class);
//                                    txpic = data.getData();
//                                    setProfile();
                                    Toast.makeText(MineZhanghuActivity.this, R.string.send_cover_success, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                null);
    }


    private void reset() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.PERSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    //发通知
                                    Intent intent = new Intent("edit_user_success");
                                    sendBroadcast(intent);
                                }
                                else {
                                    Toast.makeText(MineZhanghuActivity.this, "", Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(MineZhanghuActivity.this, "", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MineZhanghuActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "member_update" );
                params.put("username", getGson().fromJson(getSp().getString(Constants.MOBILE, ""), String.class));
                params.put("true_name", mine_nicheng.getText().toString());
                if("男".equals(mine_sex.getText().toString())){
                    params.put("sex", "1");
                }else {
                    params.put("sex", "2");
                }
                params.put("birthday", dateline.getText().toString());
                params.put("area", mine_area.getText().toString());
                params.put("contact_addr", "");
                params.put("mobile", mine_mobile.getText().toString());
                params.put("telephone", mine_tel.getText().toString());
                params.put("zip", mine_zip.getText().toString());
                params.put("qq", mine_qq.getText().toString());
                params.put("msn", mine_msn.getText().toString());
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


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("datetime_success")) {
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.please_wait).toString();
                progressDialog = new ProgressDialog(MineZhanghuActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(message);
                progressDialog.show();
                reset();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("datetime_success");//
        //注册广播
        this.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBroadcastReceiver);
    }

}
