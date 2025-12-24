 package com.mule.demo.config;
    
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter; 
import org.springframework.amqp.support.converter.MessageConverter;
         
     @Configuration
     public class RabbitConfig {

        // 定义一个队列的名字
        public static final String NOTIFY_QUEUE = "teamsync.notify.queue";
   
        @Bean
        public Queue notifyQueue() {
            // 创建一个持久化的队列
            return new Queue(NOTIFY_QUEUE, true);
        }
        @Bean
        public MessageConverter jsonMessageConverter() {
            return new Jackson2JsonMessageConverter();
        }
}