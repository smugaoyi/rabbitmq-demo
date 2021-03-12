package com.examples;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 发布订阅模式的消费者
 */
public class Consumer_publish_TwoTest {

    private static final String QUEUE_TWO = "test.queue_publish_1";
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
        try {
            conn = factory.newConnection();
            Channel channel = conn.createChannel();

            // 声明队列
            channel.queueDeclare(QUEUE_TWO, true, false, false, null);

            // 声明交换机
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.FANOUT);

            // 交换机和队列绑定
            channel.queueBind(QUEUE_TWO, EXCHANGE, "");

            // 消费
            channel.basicConsume(QUEUE_TWO, true, new MyConsumer(channel));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class MyConsumer extends DefaultConsumer {

        public MyConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            // 消息处理
            String exchange = envelope.getExchange();
            String msg = new String(body, "utf-8");

            System.out.println("exchange: " + exchange);
            System.out.println("msg: " + msg);


        }
    }
}
