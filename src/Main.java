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
    }
}
