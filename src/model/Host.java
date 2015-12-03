package model;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import Service.Url;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import view.RoomBackground;

import java.io.IOException;

public class Host extends User{

    private String currentAnswer;

    public String getCurrentAnswer() {
        return this.currentAnswer;
    }

    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer = currentAnswer;
    }

    public String requestAnswer() {
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(Url.GET_QUESTION)
                    .header("accept", "application/json")
                    .asJson();
            if (jsonResponse.getStatus()==200) {
                Object obj = JSONValue.parse(jsonResponse.getBody().toString());
                JSONObject jsonObject = (JSONObject) obj;
                System.out.println("랜덤문제는 " + jsonObject.get("name").toString());
                return jsonObject.get("name").toString();
//                System.out.println();
//                return ;
            }
        }catch (UnirestException e ) {}
        return "잘못된 요청. 잠시만 기다려주세요.";
    }

    public void sendGameStartSignal() {
        try {
            RoomBackground.getDos().writeUTF("/gamestart/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameEndSignal() {
        try {
            RoomBackground.getDos().writeUTF("/gameend/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void correctAnswerSignal(String username, String curAns) {
        try {
            RoomBackground.getDos().writeUTF("/correctanswer/" + username + "/" + curAns);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
