package fitme.ai.setting.api;



import java.util.Map;

import fitme.ai.bean.MessageGet;
import fitme.ai.bean.StatusWithUrl;
import fitme.ai.bean.Test;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by blw on 2016/8/26.
 */
public interface ApiService {


    //message/from_customer/create   minnan
    //message/from_device    huanxuan
    //音箱消息请求
    @POST("message/from_customer/create")
    Observable<MessageGet> messageCreateVB(@Query("api_key") String apiKey,
                                           @Query("timestamp") String timestamp,
                                           @Query("sign") String sign,
                                           @Body Map<String, Object> httpBody);

    //设备绑定,绑定智能音箱
    @POST("account/device/create")
    Observable<ResponseBody> deviceBind(@Query("api_key") String apiKey,
                                        @Query("timestamp") String timestamp,
                                        @Query("sign") String sign,
                                        @Body Map<String, Object> http_body);

    /*//新增来自消费者的消息请求
    @POST("message/from_customer/create")
    Observable<StatusWithUrl> messageCreate(@Query("api_key") String apiKey,
                                            @Query("timestamp") String timestamp,
                                            @Query("sign") String sign,
                                            @Body Map<String, Object> http_body);

    //获取发给消费者的新消息
    @GET("message/to_customer/not_arrived")
    Observable<MessageGet> messageGet(@Query("api_key") String apiKey,
                                      @Query("timestamp") String timestamp,
                                      @Query("user_id") String user_id,
                                      @Query("max_count") int max_count,
                                      @Query("password") String password,
                                      @Query("sign") String sign);*/

}