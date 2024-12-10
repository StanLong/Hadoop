package com.stanlong.kafka;

import com.alibaba.fastjson.JSON;
import org.junit.Test;


import java.util.*;

public class TestKafkaJson {

    @Test
    public void testKafkaProducer() throws InterruptedException {

        KafkaProducer kafkaProducer = new KafkaProducer();

        // 模拟测试数据
        ArrayList<String> itemList = new ArrayList<>();
        ArrayList<String> actionList = new ArrayList<>();
        Random random = new Random();

        itemList.add("Milk");
        itemList.add("Bread");
        itemList.add("Rice");

        actionList.add("click");
        actionList.add("buy");

        Map<String, Object> map = new HashMap<>();

        while(true){
            map.put("userid", random.nextInt(10));
            map.put("item", random.nextInt(itemList.size()));
            map.put("actions", random.nextInt(actionList.size()));
            map.put("times", Calendar.getInstance().getTimeInMillis());
            String jsonString = JSON.toJSONString(map);
            System.out.println(jsonString);
            kafkaProducer.send(jsonString);
            Thread.sleep(2000);
        }
    }
}
