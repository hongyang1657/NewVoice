package fitme.ai.bean;

/**
 * 注册账号返回结果的实体
 * Created by blw on 2016/8/28.
 */
public class AccountCreate {
    //公共参数
    private String status;
    //用户id
    private String user_id;

    public AccountCreate() {
    }

    public AccountCreate(String user_id, String status) {
        this.user_id = user_id;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "AccountCreate{" +
                "status='" + status + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
