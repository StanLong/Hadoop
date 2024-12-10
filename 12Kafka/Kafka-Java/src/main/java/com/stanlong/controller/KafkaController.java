package com.stanlong.controller;

import com.stanlong.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
    @Autowired
    private KafkaProducer kafkaProducer;

    @GetMapping("/send/{msg}")
    public void sendMsg(@PathVariable String msg){
        kafkaProducer.send(msg);
    }
}
