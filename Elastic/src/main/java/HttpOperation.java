import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author yyw
 * @version 1.0
 * @date 2019/4/8 20:25
 **/
public class HttpOperation {

    public void init() {
        StringBuilder sb = new StringBuilder();
        CloseableHttpClient client = HttpClients.createDefault();
        String url = "http://47.106.193.40:9200/_cat/indices?v";
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);
                String s = new String(bytes);
                List<String> strings = Splitter.on("\n").omitEmptyStrings().splitToList(s);
                strings.forEach(item -> {
                    List<String> strings1 = Splitter.on(" ").omitEmptyStrings().splitToList(item);
                    sb.append(Joiner.on(",").join(strings1)).append("\n");
                });
                System.out.println(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HttpOperation operation = new HttpOperation();
        operation.init();
    }
}
