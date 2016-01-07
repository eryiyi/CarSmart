package com.xiaolong.CarSmart;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.message.PushAgent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2015/7/30.
 */
public class CarSmartApplication  extends Application {
    private ExecutorService lxThread;
    private Gson gson;
    private RequestQueue requestQueue;
    private SharedPreferences sp;


    public static DisplayImageOptions options;
    public static DisplayImageOptions txOptions;//ͷ��ͼƬ

    private static final String TAG = CarSmartApplication.class.getName();

    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        // ��ʹ�� SDK �����֮ǰ��ʼ�� context ��Ϣ������ ApplicationContext
        SDKInitializer.initialize(this);
        initImageLoader(getApplicationContext());
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);

        requestQueue = Volley.newRequestQueue(this);
        gson = new Gson();
        lxThread = Executors.newFixedThreadPool(20);
        sp = getSharedPreferences("university_manage", Context.MODE_PRIVATE);
        imageLoader = new com.android.volley.toolbox.ImageLoader(requestQueue, new BitmapCache());
        initImageLoader(this);

    }

    public CarSmartApplication() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.hctp)
                .showImageForEmptyUri(R.drawable.hctp)	// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
                .showImageOnFail(R.drawable.hctp)		// ����ͼƬ���ػ��������з���������ʾ��ͼƬ
                .cacheInMemory(true)                           // �������ص�ͼƬ�Ƿ񻺴����ڴ���
                .cacheOnDisc(true)                             // �������ص�ͼƬ�Ƿ񻺴����ڴ濨��
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)          //ͼƬ�Ľ�������
//                .displayer(new RoundedBitmapDisplayer(5))
                .build();									   // �������ù���DisplayImageOption����

        txOptions = new DisplayImageOptions.Builder()//ͷ��
                .showImageOnLoading(R.drawable.txhc)
                .showImageForEmptyUri(R.drawable.txhc)	// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
                .showImageOnFail(R.drawable.txhc)		// ����ͼƬ���ػ��������з���������ʾ��ͼƬ
                .cacheInMemory(true)                           // �������ص�ͼƬ�Ƿ񻺴����ڴ���
                .cacheOnDisc(true)                             // �������ص�ͼƬ�Ƿ񻺴����ڴ濨��
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)          //ͼƬ�Ľ�������ͷ��
                .build();
    }

    /**
     * ��ȡ�Զ����̳߳�
     *
     * @return
     */
    public ExecutorService getLxThread() {
        if (lxThread == null) {
            lxThread = Executors.newFixedThreadPool(20);
        }
        return lxThread;
    }

    /**
     * ��ȡGson
     *
     * @return
     */
    public Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    /**
     * ��ȡVolley�������
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }

    /**
     * ��ȡSharedPreferences
     *
     * @return
     */
    public SharedPreferences getSp() {
        if (sp == null) {
            sp = getSharedPreferences("university_manage", Context.MODE_PRIVATE);
        }
        return sp;
    }

    /**
     * ��ʼ��ͼƬ�������ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private com.android.volley.toolbox.ImageLoader imageLoader;

    private class BitmapCache implements com.android.volley.toolbox.ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }

    public com.android.volley.toolbox.ImageLoader getImageLoader() {
        return imageLoader;
    }

}

