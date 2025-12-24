package com.mule.demo.mq;
import com.mule.demo.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Map;
@Slf4j
@Component
public class NotifyListener {
   /*  
   监听消息队列notify.queue
   消息进入就被触发
   */ 

@RabbitListener(queues = RabbitConfig.NOTIFY_QUEUE)
public void handleInviteMessage(Map<String,Object> message) {
    log.info("RabbitMQ message:{}",message);
    String type=(String)message.get("type");
    if("invite".equals(type)) {
        //处理邀请消息
    Long targetUserId=Long.valueOf(message.get("UserId").toString());
    String projectName=(String)message.get("projectName");
    sendEmail(targetUserId,projectName);
    }
}
private void sendEmail(Long targetUserId,String projectName) {
    try {
        log.info("send email to user {} for project {}",targetUserId,projectName);
        //发送邮件
        Thread.sleep(2000);
        log.info("send email success{}",projectName);
    }catch (InterruptedException e) {
    e.printStackTrace();
    }
}
}
