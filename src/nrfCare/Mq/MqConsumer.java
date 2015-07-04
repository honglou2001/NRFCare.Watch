package nrfCare.Mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import nrfCare.Netty.NettyServer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.PropertyConfigurator;

public class MqConsumer {

	private static String brokerURL = "tcp://localhost:61616";
	private static transient ConnectionFactory factory;
	private transient Connection connection;
	private transient Session session;
	MessageConsumer consumer;
	Destination destination;


	public MqConsumer() throws JMSException {
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();		
		connection.setClientID("guangdong3"); 		  
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	public void close() throws JMSException {
		if (connection != null) {
			connection.close();
		}
	}

	public void setListener() {
		try {
			
//			destination = session.createTopic("QualityQueue2");		
//			consumer = session.createDurableSubscriber((Topic)destination,"guangdong1");
			destination = session.createQueue("QualityQueue");
			MessageConsumer messageConsumer = this.getSession().createConsumer(destination, "filter='quality'");
			messageConsumer.setMessageListener(new Listener());

//			MessageListener ml = new MessageListener() {
//				@Override
//				// 设置监听器
//				public void onMessage(Message m) {
//					TextMessage textMsg = (TextMessage) m;
//					try {
//						System.out.println("收到消息:" + textMsg.getText());
//					} catch (JMSException e) {
//						e.printStackTrace();
//					}
//				}
//			};
//
//			consumer.setMessageListener(ml);
//			 
//			 while (true) {
//			 }


		} catch (JMSException e) {
			// TODO Auto-generated catch block
			
			try {
	            this.close();
	        } catch (Throwable ignore) {
	        }
			e.printStackTrace();
		}
		

	}

	public static void main(String[] args) throws JMSException {

		//初始化log配置
		PropertyConfigurator.configure("log4j.properties");		  	  		  
		
		
		 new Thread() {
           @Override
           public void run() {
        	   MqConsumer consumer;
			try {
				consumer = new MqConsumer();
				consumer.setListener();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       		
           }
       }.start();

		
//		//tcp监听
//		NettyServer.StartSocketServer();
		
		   //这是主进程
        System.out.println("主线程：" + Thread.currentThread().getName());
        Runnable r = new MyThread();
        Thread t = new Thread(r);
        t.start();



		// MqConsumer consumer = new MqConsumer();
		// for (String stock : args) {
		// Destination destination = consumer.getSession().createTopic("STOCKS."
		// + stock);
		// MessageConsumer messageConsumer =
		// consumer.getSession().createConsumer(destination);
		// messageConsumer.setMessageListener(new Listener());
		// }
	}

	public Session getSession() {
		return session;
	}
	
    

}

class MyThreadConsumer implements Runnable {
	 
    @Override
    public void run() {
        System.out.println("第2个线程：" + Thread.currentThread().getName());
         
      //tcp监听
		NettyServer.StartSocketServer();

 
    }
}

class MyThread implements Runnable {
	 
    @Override
    public void run() {
        System.out.println("第2个线程：" + Thread.currentThread().getName());
         
      //tcp监听
		NettyServer.StartSocketServer();
		
//        new Thread() {
//            @Override
//            public void run() {
//                System.out.println("第3个线程：" + Thread.currentThread().getName());
//            }
//        }.start();
 
    }
}
