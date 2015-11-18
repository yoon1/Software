/**
 * Created by haegyun on 11/13/15.
 */
package controller;

public class LoginService{

    public static void main(String[]args){
        boolean test = loginTest("test", "1234");

        System.out.println("로그인 결과 :"+test);
    }

    public static boolean loginTest(String id, String password) {
        System.out.println("id : " + id + "...  password : " + password);

        return true;
    }
}