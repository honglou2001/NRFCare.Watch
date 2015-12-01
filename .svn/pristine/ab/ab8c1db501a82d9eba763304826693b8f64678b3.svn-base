package nrfCare.Mq;

import java.math.BigDecimal;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.sf.json.JSONObject;
import nrfCare.Component.MapInfo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ActiveMqSender {

	private static final Logger logger = Logger.getLogger(ActiveMqSender.class);

	private Connection connection;
	private String subject = "QualityQueue";
	private ActiveMQConnectionFactory connectionFactory;
	private Session session;
	private Destination destination;
	private MessageProducer producer;

	public void startMQConn() throws JMSException {
		// 创建connection
		connectionFactory = new ActiveMQConnectionFactory("failover:(tcp://localhost:61616)?timeout=1000");
		connection = connectionFactory.createConnection();
		connection.start();
		// 创建session，设置消息确认机制
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);				
		// 创建destination
		destination = session.createQueue(subject);
		// 创建producer
		producer = session.createProducer(destination);
		// 设置JMS的持久性
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		
		//destination = session.createTopic("QualityQueue2");
	}

	private void send(String msg) {
		// 创建connectionFaction
		try {
			// JMS消息体
//			msg += "filter=quality123";
			TextMessage message = session.createTextMessage(msg);			
			message.setObjectProperty("filter", "quality");
			
			// 发生消息message
			producer.send(message);
			logger.info("消息已经发送。。。。"+msg);
			// 关闭资源
			message.clearProperties();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			logger.error("JMSException"+e.toString());
			try {
				this.close();
			} catch (Throwable ignore) {
			}
			e.printStackTrace();
		}
	}

	private void close() throws JMSException {
//		if (producer != null) {
//			producer.close();
//			producer=null;
//		}
		if (connection != null) {
			connection.stop();
			connection.close();
//			connection =null;
		}
//		if (session != null) {
//			session.close();
//			session =null;
//		}
		System.out.println("关闭资源。。。。");
	}

	
	public void sendToMq(String receiveddata)
	{
		String[] strArray = receiveddata.split("\t");
	    
	    if(strArray.length<8)
	    {
	    	logger.error("秤数据格式不对！"+receiveddata);
	    	return ;
	    }
	    	    
	    logger.info(String.format("商品编码：%s,部门编码：%s,重量：%s,单价：%s,总价：%s,称重时间：%s", strArray[2],strArray[3],strArray[5],strArray[6],strArray[7],strArray[8]));
	   	    
		//ActiveMqSender acSend = new ActiveMqSender();
		try {
			//acSend.startMQConn();
							
			MSMQInfo mqInfo = new MSMQInfo();
			mqInfo.setClientID("01014C4C");
			
			String porkid = strArray[2];
			String pigid=MapInfo.getPigID(porkid);
			
			mqInfo.setPigid(pigid);
			mqInfo.setPorkid(porkid);
			
			String sTotalPrice = strArray[7];
			mqInfo.setPrice(new BigDecimal(sTotalPrice));
			
			String sWeight = strArray[5];
			mqInfo.setWeight(new BigDecimal(sWeight));
			
			JSONObject jsonStu = JSONObject.fromObject(mqInfo);  			
			String Info =jsonStu.toString();
			
			this.send(Info);

//			}

		} catch (Exception e) {
				// TODO Auto-generated catch block
			   logger.error("Exception"+e.toString());
				e.printStackTrace();
				
			}
	}
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		//sendToMq("data123");
	}
}
