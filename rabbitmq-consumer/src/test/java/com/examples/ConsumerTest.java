package com.examples;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ConsumerTest {
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
        try {
            conn = factory.newConnection();
            Channel channel = conn.createChannel();
            channel.queueDeclare(QUEUE, true, false, false, null);

            // 消费
            channel.basicConsume(QUEUE,true, new MyConsumer(channel));

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
