package com.mule.demo.service;

public interface RabbitMQService {
    void sendInviteNotify(Long projectId, String projectName, Long targetUserId);
}
