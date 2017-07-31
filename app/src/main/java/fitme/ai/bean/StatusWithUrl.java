package fitme.ai.bean;

/**
 * 通用的应答参数只有一个status的实体
 * Created by blw on 2016/8/29.
 */
public class StatusWithUrl {
    //公共参数
    private String status;
    //url
    private String url;


    public StatusWithUrl() {
    }

    public StatusWithUrl(String status,String url) {
        this.status = status;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusWithUrl{" +
                "status='" + status + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
