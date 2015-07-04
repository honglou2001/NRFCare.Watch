package nrfCare.Mq;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import nrfCare.Netty.SimpleServerHandler;
import nrfCare.Utility.ByteConverter;


import org.apache.activemq.command.ActiveMQObjectMessage;

public class Listener implements MessageListener {

	public void onMessage(Message message) {

		TextMessage textMsg = (TextMessage) message;
		
	
			try {
				System.out.println(textMsg.getText());
				
				String jsonStr = textMsg.getText();
				
				@SuppressWarnings("static-access")
				JSONObject obj=new JSONObject().fromObject(jsonStr);
				MSMQInfo info=(MSMQInfo)JSONObject.toBean(obj, MSMQInfo.class);
				
				System.out.println(String.format("weight:%s,price：%s.phone:%s", info.getWeight(),info.getPrice(), info.getPhone()));

				String senddatastring =String.format("%s|%s|%s", info.getWeight(),info.getPrice(), info.getPhone());
				
				byte[] byteToSend = ByteConverter.GetSendData(senddatastring);
				
				SimpleServerHandler.SendToClient("01014C4C", byteToSend);
				
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
									
//MSMQInfo obj = (MSMQInfo)JSONSerializer.d(jsonStr);
//		if (message instanceof ObjectMessage) {
//			ObjectMessage oMsg = (ObjectMessage) message;
//
//			if (oMsg instanceof ActiveMQObjectMessage) {
//				ActiveMQObjectMessage aMsg = (ActiveMQObjectMessage) oMsg;
//				try {
//					MSMQInfo info = (MSMQInfo) aMsg.getObject();
//
//					System.out.println(String.format(
//							"weight:%s,price：%s.phone:%s", info.get_weight(),
//							info.get_price(), info.get_phone()));
//
//				} catch (JMSException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
	}
	

}
