package com.examples;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布/订阅模式的生产者
 * 一个交换机绑定多个队列，即一个消息发给多个consumer
 * 如果多个consumer监听一个队列，即可实现work queue模式（轮询）
 */
public class Producter_publishTest {
    private static final String QUEUE_ONE = "test.queue_publish_1";
    private static final String QUEUE_TWO = "test.queue_publish_2";
    private static final String EXCHANGE = "test.exchange";

    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);

        factory.setUsername("guest");
        factory.setPassword("guest");

        // 设置虚拟机
        factory.setVirtualHost("/");

        Connection conn = null;
        Channel channel = null;
        try {
            conn = factory.newConnection();
            channel = conn.createChannel();
            // 声明两个队列
            channel.queueDeclare(QUEUE_ONE, true, false, false, null);
            channel.queueDeclare(QUEUE_TWO, true, false, false, null);

            // 声明交换机
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.FANOUT);

            // 交换机和队列绑定
            channel.queueBind(QUEUE_ONE, EXCHANGE, "");
            channel.queueBind(QUEUE_TWO, EXCHANGE, "");

            // 发送消息至交换机
            String msg = "哈喽，小明";
            channel.basicPublish(EXCHANGE, "", null, msg.getBytes());

            System.out.println("send msg successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 生产者发送完消息即可关闭连接
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
