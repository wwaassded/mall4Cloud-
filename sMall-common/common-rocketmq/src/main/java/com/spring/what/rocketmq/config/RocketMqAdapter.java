package com.spring.what.rocketmq.config;

import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@RefreshScope
@Import({RocketMQAutoConfiguration.class})
public class RocketMqAdapter {

    @Resource
    private RocketMQMessageConverter rocketMQMessageConverter;

    @Value("${rocketmq.name-server:}")
    private String nameServer;

    public RocketMQTemplate getTemplate(String topic) {
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setNamesrvAddr(nameServer);
        defaultMQProducer.setSendMsgTimeout((int) RocketMqConstant.TIMEOUT);
        defaultMQProducer.setRetryTimesWhenSendFailed(2);
        rocketMQTemplate.setProducer(defaultMQProducer);
        rocketMQTemplate.setMessageConverter(rocketMQMessageConverter.getMessageConverter());
        return rocketMQTemplate;
    }
}
