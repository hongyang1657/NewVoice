package fitme.ai.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 登录成功后的用户信息类
 * Created by blw on 2016/8/30.
 */
public class User implements Serializable{
    //公共参数
    private String status;
    //用户id
    private String user_id;
    //手机号
    private String mobile;
    //被称呼的名称，比如张大人
    private String be_called;
    //性别
    private String gender;
    //出生年
    private String born_year;
    //出生月
    private String born_month;
    //出生日
    private String born_day;
    //家庭地址
    private String home_address;
    //公司地址
    private String company_address;
    //个人喜好，暂时保留
    private JSONObject preferences;
    //设置用户是否已经登录
    private boolean is_login;
    //密码
    private String password;
    //用户是否开启声音播报消息
    private boolean is_play_voice;

    public User() {
    }

    public User(String status, String user_id, String mobile, String be_called, String gender, String born_year, String born_month,
                String born_day,String home_address, String company_address, JSONObject preferences,boolean is_login,String password,boolean is_play_voice) {
        this.status = status;
        this.user_id = user_id;
        this.mobile = mobile;
        this.be_called = be_called;
        this.gender = gender;
        this.born_year = born_year;
        this.born_month = born_month;
        this.born_day=born_day;
        this.home_address = home_address;
        this.company_address = company_address;
        this.preferences = preferences;
        this.password=password;
        //用来检验当前是否已经登录
        this.is_login=is_login;
        this.is_play_voice=is_play_voice;

    }

    public boolean is_play_voice() {
        return is_play_voice;
    }

    public void setIs_play_voice(boolean is_play_voice) {
        this.is_play_voice = is_play_voice;
    }

    public String getBorn_day() {
        return born_day;
    }

    public void setBorn_day(String born_day) {
        this.born_day = born_day;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBe_called() {
        return be_called;
    }

    public void setBe_called(String be_called) {
        this.be_called = be_called;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBorn_year() {
        return born_year;
    }

    public void setBorn_year(String born_year) {
        this.born_year = born_year;
    }

    public String getBorn_month() {
        return born_month;
    }

    public void setBorn_month(String born_month) {
        this.born_month = born_month;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public JSONObject getPreferences() {
        return preferences;
    }

    public void setPreferences(JSONObject preferences) {
        this.preferences = preferences;
    }

    public boolean is_login() {
        return is_login;
    }

    public void setIs_login(boolean is_login) {
        this.is_login = is_login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "status='" + status + '\'' +
                ", user_id='" + user_id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", be_called='" + be_called + '\'' +
                ", gender='" + gender + '\'' +
                ", born_year='" + born_year + '\'' +
                ", born_month='" + born_month + '\'' +
                ", born_day='" + born_day + '\'' +
                ", home_address='" + home_address + '\'' +
                ", company_address='" + company_address + '\'' +
                ", preferences=" + preferences +
                ", is_login=" + is_login +
                ", password='" + password + '\'' +
                ", is_play_voice=" + is_play_voice +
                '}';
    }
}
