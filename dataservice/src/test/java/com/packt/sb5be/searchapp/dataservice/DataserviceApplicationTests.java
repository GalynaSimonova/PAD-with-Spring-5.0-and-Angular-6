package com.packt.sb5be.searchapp.dataservice;

import com.packt.sb5be.searchapp.dataservice.dbmodel.orm.Topic;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import sun.net.www.http.HttpClient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataserviceApplicationTests {

    @Before
    public void setup() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        this.restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testBasicSearch() {
        Topic[] topics = this.restTemplate.getForObject("/topics?searchString=spring", Topic[].class);
        assertTrue(topics.length >= 2);
        for (int i = 0; i < topics.length; i++) {
            assertTrue(topics[i].getName() != null);
        }
    }

    @Test
    public void testBasicGet() {
        Topic topic = doGetTopic(Long.valueOf(2));
        assertTrue(topic.getName().contains("Spring"));
    }

    @Test
    public void testBasicPost() {
        Topic topic = new Topic();
        String name = "The new topic";
        String desc = "New topic description";
        String tfield1 = "Text field 1";
        String tfield2 = "Text field 2";
        topic.setName(name);
        topic.setDescription(desc);
        topic.setTextField1(tfield1);
        topic.setTextField2(tfield2);
        Topic createdTopic = this.restTemplate.postForObject("/topics", topic, Topic.class);
        assertTrue(createdTopic.getId() != null);
        assertTrue(createdTopic.getName().equals(name));
    }

    private Topic doGetTopic(Long id) {
        Topic topic = this.restTemplate.getForObject("/topic/" + id, Topic.class);
        assertTrue(topic != null);
        assertTrue(topic.getId() != null);
        return topic;
    }

}
