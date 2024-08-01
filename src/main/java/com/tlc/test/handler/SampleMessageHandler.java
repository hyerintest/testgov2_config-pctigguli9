package com.tlc.test.handler;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.tlc.test.model.CustomEventMessage;

@Service
public class SampleMessageHandler {

    @RabbitListener(queues = "test_queue")
    public void messageHandler(CustomEventMessage message) {
        try {
            System.out.println("Message : " + message.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
