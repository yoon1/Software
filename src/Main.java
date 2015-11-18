/**
 * Created by haegyun on 11/7/15.
 */

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;

import com.sun.javafx.fxml.builder.URLBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import model.Room;

import javax.net.ssl.HttpsURLConnection;

import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;


public class Main {
    public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException {

//        Room room = new Room(1, "test", 4, 2, 0);
//
//        System.out.println(room);
//
//        String url = "http://localhost:8080/user/login";
//        URL obj = new URL(url);
//
//        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
//
//        con.setRequestMethod("POST");

//        String urlParameters  = "param1=a&param2=b&param3=c";
//        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
//        int    postDataLength = postData.length;
//        URL    url            = new URL("http://example.com/index.php");
//        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
//        conn.setDoOutput( true );
//        conn.setInstanceFollowRedirects( false );
//        conn.setRequestMethod( "POST" );
//        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
//        conn.setRequestProperty( "charset", "utf-8");
//        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
//        conn.setUseCaches( false );
//        DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
//        wr.write(postData);


        //        String urlParameters = "param1=a&param2=b&param3=c";
//        URL url = new URL("http://localhost.com/index.php");
//        URLConnection conn = url.openConnection();
//
//        conn.setDoOutput(true);
//
//        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
//
//        writer.write(urlParameters);
//        writer.flush();
//
//        String line;
//        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
//        }
//
//        writer.close();
//        reader.close();




//        URI uri = new URI("http://localhost:8080/greeting");
//        uri = new URIBuilder(uri).addParameter("aaa", "bbb").addParameter("ccc", "ddd").build();
//
//        HttpClient httpClient = HttpClientBuilder.create().build();
//        HttpResponse response = httpClient.execute(new HttpGet(uri)); // post 요청은 HttpPost()를 사용하면 된다.
//        HttpEntity entity = response.getEntity();
//        String content = EntityUtils.toString(entity);
//        System.out.println(content);

    }
}
