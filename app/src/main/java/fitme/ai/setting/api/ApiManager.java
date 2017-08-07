package fitme.ai.setting.api;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by blw on 2016/8/26.
 */
public class ApiManager {
    //fitme的api访问基础地址
    //外网
    //private static final String FITMEURL="http://app.fitme.ai:7001";

    //内网,mingNan
    private static final String FITMEURL = "http://172.16.11.27:7001";

    //青山湖
    //private static final String FITMEURL = "http://192.168.31.218:7001";

    //huangxuan 测试硬件音箱基础地址
    //private static final String FITMEURL = "http://172.16.11.20:5000";

    //private static final String FITMEURL = "http://172.16.1.43:7001";


    //ApiKey
    public static final String api_key = "testkey";
    //ApiSecret
    public static final String api_secret = "fb78fceaf5d445409610398d83088533";
    //okhttp客户端
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build();
    //创建用于fitme的api的retrofit服务接口
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(FITMEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    //设置成公共方法让model访问
    public static final ApiService fitmeApiService = retrofit.create(ApiService.class);


}
