package fitme.ai.bean;

/**
 * 通用的应答参数只有一个status的实体
 * Created by blw on 2016/8/29.
 */
public class Status {
    //公共参数
    private String status;


    public Status() {
    }

    public Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Status{" +
                "status='" + status + '\'' +
                '}';
    }
}
