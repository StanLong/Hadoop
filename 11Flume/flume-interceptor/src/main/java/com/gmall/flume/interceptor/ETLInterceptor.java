package com.gmall.flume.interceptor;

import com.gmall.flume.utils.JSONUtil;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.nio.charset.StandardCharsets;

import java.util.List;

public class ETLInterceptor implements Interceptor {
    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        // # 校验数据格式是否是一个完整的JSON
        byte[] body = event.getBody();
        String log = new String(body, StandardCharsets.UTF_8);

        if (JSONUtil.isJSONValidate(log)){
            return event;
        }

        return null;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        list.removeIf(event -> intercept(event) == null);
        return list;
    }

    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new ETLInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
