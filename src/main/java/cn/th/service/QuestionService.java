package cn.th.service;

import cn.th.Utils.HttpUtils;
import cn.th.entity.Question;
import cn.th.mapper.QuestionMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tianh
 */
@Slf4j
@Service
public class QuestionService {

    private static final String API="https://cx.icodef.com/v2/answer";
    @Resource
    QuestionMapper questionMapper;

    public List<Question> findQuestion(String question){
        return questionMapper.findAnswer(question);
    }
    public int insertQuestion(Question question){
        return questionMapper.insertQuestion(question);
    }
    public JSONObject getAnswer(String question) throws URISyntaxException, IOException {
        List<Question> list = findQuestion(question);
        if(list.size()==0) {
            List<NameValuePair> form = new ArrayList<>();
            form.add(new BasicNameValuePair("topic[0]", question));
            HttpPost post = HttpUtils.getPost(new URIBuilder(API), form);
            CloseableHttpResponse res = HttpUtils.getClient().execute(post);
            JSONArray json = JSON.parseArray(EntityUtils.toString(res.getEntity(), "utf-8"));
            JSONArray result = json.getJSONObject(0).getJSONArray("result");
            JSONObject content;
            JSONObject back=new JSONObject();
            if (result != null) {
                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        JSONObject temp = result.getJSONObject(i);
                        JSONArray correct = temp.getJSONArray("correct");
                        content = correct.getJSONObject(0);
                        JSONObject finalContent = content;
                        String answer=finalContent.getString("content");
                        if(answer.contains("javascript:void(0);")){
                            answer=answer.replaceAll("javascript:void\\(0\\);","");
                        }
                        String finalAnswer = answer;
                        if(insertQuestion(new Question(){{
                            this.setQuestion(temp.getString("topic"));
                            this.setAnswer(finalAnswer);
                        }})>0) {
                            log.info("插入题目成功---->" + content.toString());
                            back.put("question",temp.getString("topic"));
                            back.put("content",finalAnswer);
                        }else{
                            log.error("插入题目失败");
                        }
                    }
                } else {
                    return new JSONObject() {{
                        this.put("question","查询失败");
                        this.put("content", "无答案");
                    }};
                }
            }
            return back;
        }else{
            return new JSONObject(){{
                this.put("content",list.get(0).getAnswer());
                this.put("question",list.get(0).getQuestion());
            }};
        }
    }
}
