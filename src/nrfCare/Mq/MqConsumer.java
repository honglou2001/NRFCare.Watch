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

import org.apache.activemq.ActiveMQConnectionFactory;

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

		MqConsumer consumer = new MqConsumer();
		consumer.setListener();

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
