package com.stanlong.datasource;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class Test {
    public static void main(String[] args) {
        ArrayList<String> userList = new ArrayList<String>();
        userList.add("Mary");
        userList.add("Bob");
        userList.add("Alice");
        userList.add("Cary");


        ArrayList<String> urlList = new ArrayList<String>();
        urlList.add("./home");
        urlList.add("./cart");
        urlList.add( "./fav");
        urlList.add("./prod?id=1");
        urlList.add("./prod?id=2");


        Random random = new Random();


        for(int i = 0; i <100; i++){
            Event event = new Event(
                    userList.get(random.nextInt(userList.size())),
                    urlList.get(random.nextInt(urlList.size())),
                    Calendar.getInstance().getTimeInMillis()
                    );
            String jsonEvent = JSON.toJSONString(event);
            System.out.println(jsonEvent);


        }


    }
}

class Event{
    public String user;
    public String url;
    public Long timestamp;

    public Event(String user, String url, Long timestamp){
        this.user = user;
        this.url = url;
        this.timestamp = timestamp;
    }

}
