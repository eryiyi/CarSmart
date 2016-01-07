package com.xiaolong.CarSmart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaolong.CarSmart.R;
import com.xiaolong.CarSmart.adapter.AnimateFirstDisplayListener;
import com.xiaolong.CarSmart.base.BaseFragment;
import com.xiaolong.CarSmart.ui.MipcaActivityCapture;

/**
 * Created by Administrator on 2015/7/30.
 * faxian
 */
public class ThirdFragment extends BaseFragment implements View.OnClickListener {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    //实例化控件
    private RelativeLayout relati_one;//动态
    private TextView saoasao;//扫啊扫
//    private TextView paizhao;//拍照购

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        relati_one = (RelativeLayout) view.findViewById(R.id.relati_one);
        saoasao = (TextView) view.findViewById(R.id.saoasao);
//        paizhao = (TextView) view.findViewById(R.id.paizhao);

        relati_one.setOnClickListener(this);
        saoasao.setOnClickListener(this);
//        paizhao.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.relati_one:
                //动态
                //调用广播，刷新主页
                Intent intent2 = new Intent("switch_type");
                getActivity().sendBroadcast(intent2);
                break;
            case R.id.saoasao:
                //扫一扫
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
//            case R.id.paizhao:
//                //拍照
//                //调用广播，刷新主页
//                Intent intent1 = new Intent("switch_type");
//                getActivity().sendBroadcast(intent1);
//                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == getActivity().RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
//                    mTextView.setText(bundle.getString("result"));
                    String result = bundle.getString("result");

                    //显示
                    // mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }
}
