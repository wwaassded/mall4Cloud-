package com.spring.what.search.listener;

import cn.throwx.canal.gule.CanalGlue;
import com.spring.what.rocketmq.config.RocketMqConstant;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

@RocketMQMessageListener(topic = RocketMqConstant.CANAL_TOPIC, consumerGroup = RocketMqConstant.CANAL_TOPIC)
public class CanalListenter implements RocketMQListener<String> {

    @Resource
    private CanalGlue canalGlue;

    @Override
    public void onMessage(String message) {
        canalGlue.process(message);
    }
}
