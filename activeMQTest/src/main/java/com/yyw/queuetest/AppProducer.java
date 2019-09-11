package com.yyw.queuetest;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author yyw
 * @date 2019/9/10
 **/
public class AppProducer {
    /**
     * MQ服务器地址
     */
    private static final String BROKER_URL = "tcp://47.106.193.40:61616";
    /**
     * 队列名称
     */
    private static final String QUEUE_NAME = "queue-test";

    public static void main(String[] args) throws JMSException {
        //1创建连接工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        //2创建连接
        Connection connection = factory.createConnection();
        //3启动连接
        connection.start();
        //4创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5创建一个目标
        Queue queue = session.createQueue(QUEUE_NAME);
        //6创建一个生产者
        MessageProducer producer = session.createProducer(queue);
        //循环发送消息
        for (int i = 0; i < 100; i++) {
            TextMessage textMessage = session.createTextMessage("【test-activemq-producer】" + i);
            producer.send(textMessage);
            System.out.println("发送消息成功:" + textMessage);
        }
        connection.close();
    }
}
