package fitme.ai.bean;

/**
 * 判断手机号是否占用的实体类
 * Created by blw on 2016/8/28.
 */
public class IsOccupiedMobile {
    //公共参数
    private String status;
    //是否被占用
    private boolean is_occupied;


    public IsOccupiedMobile(String status, boolean is_occupied) {
        this.status = status;
        this.is_occupied = is_occupied;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean is_occupied() {
        return is_occupied;
    }

    public void setIs_occupied(boolean is_occupied) {
        this.is_occupied = is_occupied;
    }

    @Override
    public String toString() {
        return "IsOccupiedMobile{" +
                "status='" + status + '\'' +
                ", is_occupied=" + is_occupied +
                '}';
    }
}
