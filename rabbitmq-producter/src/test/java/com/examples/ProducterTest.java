package com.examples;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * work queue模式
 * 	一个生产者多个消费者
 * 	消息不会重复消费
 * 	采用轮询的方式发给各个consumer
 */
public class ProducterTest {
    private static final String QUEUE = "test.queue";

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
            channel.queueDeclare(QUEUE, true, false, false, null);
            // String exchange, String routingKey, BasicProperties props, byte[] body

            String msg = "hello world";
            channel.basicPublish("", QUEUE, null, msg.getBytes());
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
