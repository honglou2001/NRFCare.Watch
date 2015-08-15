package nrfCare.Mq;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import nrfCare.Netty.NettyServerHandler;
import nrfCare.Netty.SimpleServerHandler;
import nrfCare.Utility.ByteConverter;
import nrfCare.Utility.Hex;


import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;

public class Listener implements MessageListener {

	private static final Logger logger = Logger.getLogger(Listener.class);	   
	
	public void onMessage(Message message) {

		TextMessage textMsg = (TextMessage) message;
		
	
			try {
				System.out.println(textMsg.getText());
				
				String jsonStr = textMsg.getText();
				
				Listener.SendMsg(jsonStr);
				
				
			} catch (JMSException e) {
				logger.error("mq consume JMSException："+e.toString());  
				// TODO Auto-generated catch block				
				e.printStackTrace();

			} catch (UnsupportedEncodingException e) {
				logger.error("mq consume UnsupportedEncodingException："+e.toString()); 
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
	

	public static void SendMsg(String jsonStr) throws UnsupportedEncodingException
	{
		@SuppressWarnings("static-access")
		JSONObject obj=new JSONObject().fromObject(jsonStr);
		MSMQInfo info=(MSMQInfo)JSONObject.toBean(obj, MSMQInfo.class);
		
		System.out.println(String.format("weight:%s,price：%s,phone:%s,pigid:%s,porkid:%s,paytype:%s", info.getWeight(),info.getPrice(), info.getPhone(), info.getPigid(),info.getPorkid(),info.getPaytype()));

		String senddatastring =String.format("%s|%s|%s|%s|%s|%s", info.getWeight(),info.getPrice(), info.getPhone(), info.getPigid(),info.getPorkid(),info.getPaytype());
		
		byte[] bytecmd = { 0x00, 0x04 };	
		String clientNum = info.getClientID();
		byte[] clientByte = Hex.HexString2Bytes(clientNum);
		//Byte[] byteack= new Byte[2];
		//List<Byte> byteList = new ArrayList<Byte>();
		
		ByteConverter.AckWrapper ackWrapper = new ByteConverter().new AckWrapper();
		
		byte[] byteToSend = ByteConverter.GetSendData(clientByte,bytecmd,ackWrapper,senddatastring,false);	
		
		String cmd = Hex.encodeHexStr(bytecmd);
		
		byte[] byteAck =ackWrapper.getByteAck();
		
		String ack = Hex.encodeHexStr(byteAck);
		
		//发送
		SimpleServerHandler.SendToClient(clientNum, cmd, ack, byteToSend,true);		
		
		SendRepeatByAck(clientNum, cmd, ack, byteToSend,true);
		
		logger.info("mq consume send to netty："+senddatastring);  
	}
	
	private static void SendRepeatByAck(String clientNum, String cmd,String ack,byte [] byteToSend,Boolean isWaitForAck)
	  {
		boolean bkeyExist  = Boolean.FALSE;
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bkeyExist = NettyServerHandler.DeleteSetForAck(clientNum, cmd, ack);
		//如ack还没返回
		if(bkeyExist == Boolean.TRUE)
		{
			//发送
			SimpleServerHandler.SendToClient(clientNum, cmd, ack, byteToSend,false);	
			logger.info(String.format("一次ACK没返回：%s,%s,%s", clientNum, cmd, ack));  
		}
		else
		{
			return ;
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bkeyExist = NettyServerHandler.DeleteSetForAck(clientNum, cmd, ack);
		if(bkeyExist == Boolean.TRUE)
		{
			//发送
			SimpleServerHandler.SendToClient(clientNum, cmd, ack, byteToSend,false);	
			logger.info(String.format("二次ACK没返回：%s,%s,%s", clientNum, cmd, ack));  
		}
		else
		{
			return ;
		}
		
		
	  }
}
