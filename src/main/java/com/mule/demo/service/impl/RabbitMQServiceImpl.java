package com.mule.demo.service.impl;

import com.mule.demo.config.RabbitConfig;
import com.mule.demo.service.RabbitMQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendInviteNotify(Long projectId, String projectName, Long targetUserId) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", "INVITE");
        msg.put("projectId", projectId);
        msg.put("projectName", projectName);
        msg.put("UserId", targetUserId);
        rabbitTemplate.convertAndSend(RabbitConfig.NOTIFY_QUEUE, msg);
    }
}
