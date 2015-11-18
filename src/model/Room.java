package model;

/**
 * Created by haegyun on 11/15/15.
 */
public class Room {
    private int id;
    private String title;
    private int limit_num;
    private int current_num;
    private int is_playing;

    public Room(int id, String title, int limit_num, int current_num, int is_playing) {
        this.id = id;
        this.title = title;
        this.limit_num = limit_num;
        this.current_num = current_num;
        this.is_playing = is_playing;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", limit_num=" + limit_num +
                ", current_num=" + current_num +
                ", is_playing=" + is_playing +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLimit_num() {
        return limit_num;
    }

    public void setLimit_num(int limit_num) {
        this.limit_num = limit_num;
    }

    public int getCurrent_num() {
        return current_num;
    }

    public void setCurrent_num(int current_num) {
        this.current_num = current_num;
    }

    public int getIs_playing() {
        return is_playing;
    }

    public void setIs_playing(int is_playing) {
        this.is_playing = is_playing;
    }
}
