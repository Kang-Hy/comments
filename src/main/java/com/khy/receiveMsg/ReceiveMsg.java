package com.khy.receiveMsg;

import cn.hutool.json.JSONUtil;
import com.khy.entity.VoucherOrder;
import com.khy.service.IVoucherOrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * ClassName: ReceiveMsg
 * Package: com.khy.receiveMsg
 * Description:
 *
 * @Author 康海洋
 * @Create 2023/11/28 19:59
 * @Version 1.0
 */
@Component
@Slf4j
public class ReceiveMsg {
    @Autowired
    private IVoucherOrderService iVoucherOrderService;

    @RabbitListener(queues = {"queue.voucheroder.a1"})
    public void receiveMsg(Message message, Channel channel) throws IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        long deliveryTag = messageProperties.getDeliveryTag();//获取消息唯一标识
        try{
            //接收到消息之后的业务逻辑
            byte[] body = message.getBody();
            VoucherOrder bean = JSONUtil.toBean(new String(body), VoucherOrder.class);
            log.info("接收到的消息为{}",bean);
            iVoucherOrderService.save(bean);
            channel.basicAck(deliveryTag,false);//第二个参数 false一次只确认当前一条
        }catch (Exception e){
            log.error("接收消息出现问题"+e.getMessage());
            channel.basicNack(deliveryTag,false,true);//第三个参数 是否重新入队
            throw new RuntimeException(e);
        }
    }
}
