package cn.th.Utils;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class HttpUtils {

    private static final Header[] HEADERS;
    private static final CloseableHttpClient CLIENT;

    static {
        HEADERS =new Header[1];
        HEADERS[0] =new BasicHeader("Content-Type","application/x-www-form-urlencoded");
        CLIENT =HttpClients.createDefault();
    }

    public static HttpPost getPost(URIBuilder uriBuilder, List<NameValuePair>form) throws UnsupportedEncodingException, URISyntaxException {
        HttpPost post=new HttpPost();
        post.setHeaders(HEADERS);
        post.setEntity(new UrlEncodedFormEntity(form,"utf-8"));
        post.setURI(uriBuilder.build());
        return post;
    }

    public static CloseableHttpClient getClient(){
        return CLIENT;
    }
}
