package com.yyw.topictest;

import javax.jms.*;

/**
 * @author yyw
 * @date 2019/9/10
 **/
public class AppConsumer {
    public static void main(String[] args) throws JMSException {
        Connection connection = ActiveMqHelper.createConnection();
        Session session = ActiveMqHelper.createSession(connection);
        Topic topic = ActiveMqHelper.createTopic(session);
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(message -> {
            TextMessage message1 = (TextMessage) message;
            try {
                System.out.println("消费者接受到的消息：" + message1.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }
}
