package com.yyw;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author yyw
 * @date 2019/9/10
 **/
public class ActiveMqHelper {
    //MQ服务器地址:
    private static final String BROKER_URL = "tcp://47.106.193.40:61616";
    //队列名称
    private static final String QUEUE_NAME = "queue-test";
    //主题名称
    private static final String TOPIC_NAME = "topic-test";

    /**
     * 创建连接
     *
     * @return
     * @throws JMSException
     */
    public static Connection createConnection() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = factory.createConnection();
        connection.start();
        return connection;
    }

    /***
     * 创建会话
     * @param connection
     * @return
     * @throws JMSException
     */
    public static Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    /**
     * 创建一个队列目标
     *
     * @param session
     * @return
     */
    public static Queue createQueue(Session session) throws JMSException {
        return session.createQueue(QUEUE_NAME);
    }

    /**
     * 创建一个主题
     *
     * @param session
     * @return
     * @throws JMSException
     */
    public static Topic createTopic(Session session) throws JMSException {
        return session.createTopic(TOPIC_NAME);
    }
}
