package fitme.ai.bean;

/**
 * Created by hongy on 2017/6/3.
 */

public class Music{
    private String name;
    private String singer;
    private String business_url;
    private String photo_url;
    private String album;
    private String song_url;
    private String source;
    private int my_favorite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getBusiness_url() {
        return business_url;
    }

    public void setBusiness_url(String business_url) {
        this.business_url = business_url;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSong_url() {
        return song_url;
    }

    public void setSong_url(String song_url) {
        this.song_url = song_url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getMy_favorite() {
        return my_favorite;
    }

    public void setMy_favorite(int my_favorite) {
        this.my_favorite = my_favorite;
    }
}
