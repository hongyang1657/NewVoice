package fitme.ai.bean;

/**
 * Created by blw on 2017/1/12.
 */

public class City {

    //城市中文
    private String cityName;
    //拼音
    private String pinyin;
    //首字母
    private String sortLetter;

    public City(String cityName, String pinyin, String sortLetter) {
        this.cityName = cityName;
        this.pinyin = pinyin;
        this.sortLetter = sortLetter;
    }

    public City() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getSortLetter() {
        return sortLetter;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
    }
}
