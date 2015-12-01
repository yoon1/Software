package model;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

// User는 로그인 이후에 "로비", "룸", "게임"에서 모두 접근될 수 있는 싱글톤 데이터.
public class User {
    private int id;
    private String username;
    private int current_room;
    private Boolean isHost;

    private static User ourInstance = new User();

    public static User getUser() {
        return ourInstance;
    }

    public User() {}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", current_room=" + current_room +
                ", isHost=" + isHost +
                '}';
    }

    public void requestUser() {
        try {
            JSONObject user = null;
            HttpResponse<JsonNode> jsonResponse = Unirest.get("http://localhost:8080/users/"+User.getUser().getId())
                    .header("accept", "application/json")
                    .asJson();
            if (jsonResponse.getStatus()!=404) {
                Object jsonObj = JSONValue.parse(jsonResponse.getBody().toString());
//                JSONArray userArray = (JSONArray) jsonObj;

            } else {
                System.out.println("사용자가 없습니다.");
            }

        }catch (UnirestException e ) {}
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCurrent_room() {
        return current_room;
    }

    public void setCurrent_room(int current_room) {
        this.current_room = current_room;
    }

    public Boolean getIsHost() {
        return isHost;
    }

    public void setIsHost(Boolean isHost) {
        this.isHost = isHost;
    }

    public static User getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(User ourInstance) {
        User.ourInstance = ourInstance;
    }
}
