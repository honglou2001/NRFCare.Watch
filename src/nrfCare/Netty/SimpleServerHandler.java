package nrfCare.Netty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import nrfCare.Component.MapInfo;
import nrfCare.Component.Msgpacket;
import nrfCare.Component.PigInfo;
import nrfCare.Component.Utility;
import nrfCare.Mq.ActiveMqSender;
import nrfCare.Utility.ByteConverter;
import nrfCare.Utility.Hex;
import nrfCare.Utility.ByteConverter.AckWrapper;

/**
 * 
 * <p>
 * 	Server接收消息处理Handler
 * </p>
 * 
 * @author 卓轩
 * @创建时间：2014年7月7日
 * @version： V1.0
 */
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
	
  // 每个chanel对应一个线程，此处用来存储对应于每个线程的一些基础数据，此处不一定要为KeepAliveMessage对象
  ThreadLocal<ByteBuf> localMsgInfo = new ThreadLocal<ByteBuf>(); 	
  private static Map<String, Channel> m_map = new HashMap<String, Channel>();
//  public static ChannelGroup channels = new DefaultChannelGroup(null);
  private static final Logger logger = Logger.getLogger(SimpleServerHandler.class);	   

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

//	  if(IdleStateEvent.class.isAssignableFrom(evt.getClass())){
//			IdleStateEvent event = (IdleStateEvent) evt;
//			if(event.state() == IdleState.READER_IDLE)
//				System.out.println("read idle");
//			else if(event.state() == IdleState.WRITER_IDLE)
//				System.out.println("write idle");
//			else if(event.state() == IdleState.ALL_IDLE)
//				System.out.println("all idle");
//		}
	  
	  if (evt instanceof IdleStateEvent) {	
		  
			IdleStateEvent event = (IdleStateEvent) evt;
			if(event.state() == IdleState.READER_IDLE){
				System.out.println("read idle");				
				RemoveChannel(ctx);				
				ctx.channel().close();
				logger.info("userEventTriggered:ctx.channel().close()");
			}
			else if(event.state() == IdleState.WRITER_IDLE)
				System.out.println("write idle");
			else if(event.state() == IdleState.ALL_IDLE)
				System.out.println("all idle");
		  
//			RemoveChannel(ctx);
//			String key = null;
//			if (map.containsValue(ctx.channel())) {
//				for (Entry<String, Channel> entries : map.entrySet()) {
//					if (entries.getValue() == ctx.channel()) {
//						key = entries.getKey();
//					}
//				}
//			} 
//			if (key != null) {
//				Iterator<String> it = map.keySet().iterator();
//				while (it.hasNext()) {
//					String clinumber = it.next();
//					if (key.equals(clinumber)) {
//						it.remove();
//						map.remove(clinumber);						
//						System.out.println("map.size():" + map.size());				
//					}
//				}
//			}

		} else {
			super.userEventTriggered(ctx, evt);
		}
  }
  
  private void RemoveChannel(ChannelHandlerContext ctx)
  {
	  String key = null;
		if (m_map.containsValue(ctx.channel())) {
			for (Entry<String, Channel> entries : m_map.entrySet()) {
				if (entries.getValue() == ctx.channel()) {
					key = entries.getKey();
				}
			}
		} 
		if (key != null) {
			Iterator<String> it = m_map.keySet().iterator();
			while (it.hasNext()) {
				String clinumber = it.next();
				if (key.equals(clinumber)) {
					it.remove();
					m_map.remove(clinumber);											
					logger.info("m_map.remove,map.size():"+ m_map.size());
				}
			}
		}
  }
//  @Override
//  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
//      Channel incoming = ctx.channel();
//      for (Channel channel : channels) {
//          channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
//      }
//      channels.add(ctx.channel());
//  }
//
//  @Override
//  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
//      Channel incoming = ctx.channel();
//      for (Channel channel : channels) {
//          channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
//      }
//      channels.remove(ctx.channel());
//  }

  
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

	  	ByteBuf inbuf = (ByteBuf)msg;
	  	
		//byte[] bytes =inbuf.array();	
	  	
//	  	byte[] bytes = new byte[50];
//
//		
//		if(!inbuf.hasArray()){     
//			int len = inbuf.readableBytes();           
//			 bytes = new byte[len];     
//			inbuf.getBytes(0, bytes); 
//		
//		}
	  	
	    int length = inbuf.readableBytes();	 
	    //byte[] bytes = new byte[length];
	    ByteBuffer buffer = ByteBuffer.allocate(length); 
	    	    
	    byte[] byteHead = inbuf.readBytes(2).array();

	    
	    if(byteHead[0] == 0x09)
	    {
	    	readFromScale(ctx,length,byteHead,inbuf,buffer);
	    }
	    else
	    {
	    	readFromPad(ctx,length,byteHead,inbuf,buffer);
	    }
	    	   
  }
  
  //来自电子秤
  private void readFromScale(ChannelHandlerContext ctx, int length,byte[] byteHead,ByteBuf inbuf,ByteBuffer buffer) throws Exception 
  {
	    String head =byteToArray(byteHead);// inbuf.readBytes(2).toString(Charset.defaultCharset());	   
	    buffer.put(byteHead);
	    
	    ByteBuf bytesOther= inbuf.readBytes(length-2);
	    byte[] byteOther = bytesOther.array();
	    buffer.put(byteOther);
	    	 
	    String receiveddata = new String(buffer.array(),"UTF-8");
	    logger.info(String.format("收到秤信息：%s", receiveddata));	 
	    
	    //发送到屏幕显示的队列
	    NettyServer.activeSender.sendToMq(receiveddata);	    	
	    String[] strArray = receiveddata.split("\t");
	    
	    String url = "http://t.im/m7q4";
		String porkid = strArray[2];
		String pigid=MapInfo.getPigID(porkid);
		PigInfo pigInfo = MapInfo.getPigInfo(pigid);
		if(pigInfo!=null)
		{
			url = pigInfo.getInfourl();
		}
			    	    
	    logger.info(String.format("商品编码：%s,部门编码：%s,重量：%s,单价：%s,总价：%s,称重时间：%s", strArray[2],strArray[3],strArray[5],strArray[6],strArray[7],strArray[8]));	   	    	    
	    
	    String qrCodeInfo = "0\t"+url+"\n\0";	    
	    byte[] req1 = qrCodeInfo.getBytes("utf8");
	    
	    ByteBuf clientMessage = Unpooled.buffer(req1.length);
	    clientMessage.writeBytes(req1);
	    ctx.writeAndFlush(clientMessage);
	    
  }
  
  //来自平板电脑
  private void readFromPad(ChannelHandlerContext ctx, int length,byte[] byteHead,ByteBuf inbuf,ByteBuffer buffer) throws Exception 
  {
	    String head =byteToArray(byteHead);// inbuf.readBytes(2).toString(Charset.defaultCharset());	   
	    buffer.put(byteHead);
	    
	    ByteBuf bytesLength= inbuf.readBytes(2);
	    byte[] byteLength = bytesLength.array();
	    buffer.put(byteLength);
	    
	    short slength = bytesToShort(byteLength);
	    
	    byte[] byteClient = inbuf.readBytes(4).array();
	    String clientNum = byteToArray(byteClient);
	    buffer.put(byteClient);
	    
	    byte[] cmdByte = inbuf.readBytes(2).array();
	    String cmd =  byteToArray(cmdByte);
	    short scmd = bytesToShort(cmdByte);
	    buffer.put(cmdByte);
	    
	    byte[] byteTotal = inbuf.readBytes(1).array();
	    String frametotal = byteToArray(byteTotal);	   
	    buffer.put(byteTotal);
	    
	    byte[] byteIndex = inbuf.readBytes(1).array();
	    String frameindex = byteToArray(byteIndex);
	    buffer.put(byteIndex);
	    	    
	    int datalength = slength-25;
	    ByteBuf inbufdata = inbuf.readBytes(datalength);
	    byte [] reqdata = new byte[inbufdata.readableBytes()];	
	    inbufdata.readBytes(reqdata);		    
	    buffer.put(reqdata);
	    
	    byte[] timeByte = inbuf.readBytes(7).array();
	    buffer.put(timeByte);
	    
	    byte[] ackByte = inbuf.readBytes(2).array();
	    String ackStr =  byteToArray(ackByte);
	    buffer.put(ackByte);
	    	    
	    byte[] crcByte = inbuf.readBytes(2).array();
	    String crcStr =  byteToArray(crcByte);
	    buffer.put(crcByte);
	    
	    
	    byte[] crcCal=ByteConverter.ComputeCRC(buffer.array(),slength-4);
	    String crcCalStr =  byteToArray(crcCal);
	    
	    this.AddCilentToMap(ctx,clientNum);
	    
		String sInfo = Hex.encodeHexStr(buffer.array());		
		logger.info(String.format("Server received:%s", sInfo.substring(0,slength*2)));		
	    logger.info(String.format("slength:%s,head:%s,clientNum:%s,cmd:%s,ackStr:%s,crcStr:%s，校验CRC:%s", slength,head,clientNum,cmd,ackStr,crcStr,crcCalStr));
	    String receiveddata = new String(reqdata,"UTF-8");		    	    	    	    	   		    
	    logger.info("收到客户端返回信息："+receiveddata);  
    
	    switch(scmd)
	    {
	    	case 1:
	    		//收到应答，回信息给发起方
	    		this.HandleCmd1(ctx,byteClient,ackByte,receiveddata);
		    	break;	
		    case 4:
		    	//发起后，收到接收方的返回应答
		    	this.HandleCmd4(ctx,clientNum,cmd,ackStr,receiveddata);
		    	break;	
		    default:
		    	break;
	    }  
  }
  
  private void AddCilentToMap(ChannelHandlerContext ctx,String clientNum)
  {
		if (!m_map.containsKey(clientNum)) {
			m_map.put(clientNum, ctx.channel());	
			
			logger.info("new clientkey, m_map.put,map.size():"+ m_map.size());
			
		}else
		{
			Channel channel= m_map.get(clientNum);
			if(channel.hashCode() != ctx.channel().hashCode()
					|| !channel.equals(ctx.channel()))
			{
				m_map.put(clientNum, ctx.channel());	
				
				logger.info("exist clientkey,m_map.put,map.size():"+ m_map.size());
			}
		}
  }
  
  //心跳包
  private void HandleCmd1(ChannelHandlerContext ctx,byte[] cliNum,byte[] ack,String receiveddata) throws Exception
  {	    	  
    byte[] cmdReq = {0x00,0x01};
    Timestamp time = Utility.getTimeStamp();
    String senddatastring = "ping ack" + time;
    
//    Byte[] back = new Byte[2];
//    back[0] = ack[0];
//    back[1] = ack[1];
    
   
    
    ByteConverter.AckWrapper ackWrapper = new ByteConverter().new AckWrapper();
    ackWrapper.setByteAck(ack);
    
    byte[] req1 = ByteConverter.GetSendData(cliNum,cmdReq,ackWrapper,senddatastring,true);
    ByteBuf clientMessage = Unpooled.buffer(req1.length);
    clientMessage.writeBytes(req1);
    ctx.writeAndFlush(clientMessage);
  }

  
  //收到0x00 0x04应答，对应 服务器发过来的重量金额手机等信息
  private void HandleCmd4(ChannelHandlerContext ctx,String cliNum,String cmd,String ack,String receiveddata) throws Exception
  {	    
	  NettyServerHandler.DeleteSetForAck(cliNum, cmd, ack,true);
  }

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 关闭，等待重连
	    ctx.close();

	    RemoveChannel(ctx);	    
	    //System.out.println("===服务端===(客户端失效)");
	    
	    logger.info("===服务端===(客户端失效)channelInactive");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	    // Close the connection when an exception is raised.
		ctx.close();		  
		logger.info("exceptionCaught ctx.close()"+cause.toString());		  
	    cause.printStackTrace();
	    
	}

  public static void SendToClient(String clientNum, String cmd,String ack,byte [] req,Boolean isWaitForAck)
  {
	    ByteBuf clientMessage = Unpooled.buffer(req.length);
	    clientMessage.writeBytes(req);
	    
	    logger.info(String.format("Begin sent to clent ,m_map.size:%s", m_map.size()));
	    
	    boolean exist = false;
		Iterator<String> it = m_map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Channel ctx = m_map.get(key);
			if(ctx!=null ){
				exist = true;
				ctx.writeAndFlush(clientMessage);	
				
				if(isWaitForAck==Boolean.TRUE){
				//增加到重发队列，等待ack返回
					NettyServerHandler.AddToMapSet(clientNum, cmd, ack,req);	
				}
				
				logger.info(String.format("Ok sent to client:%s,cmd:%s,ack:%s，isforack:%s", key, cmd, ack,isWaitForAck));
			}
			
		}
		//如不存在，抛出异常，activemq回滚
		if(!exist)
		{
			//throw new RuntimeException("客户端channel不存在");
			logger.info(String.format("客户端不在线， client:%s,Cmd：%s,ACK：%s", clientNum, cmd, ack));
		}
	    
  }
  public short bytesToShort(byte[] bytes) {
	     return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getShort();
	}

	public byte[] shortToBytes(short value) {
	    byte[] returnByteArray = new byte[2];
	    returnByteArray[0] = (byte) (value & 0xff);
	    returnByteArray[1] = (byte) ((value >>> 8) & 0xff);
	    return returnByteArray;
	}
	
  
  
	public static String byteToArray(byte[] data) {
		String result = "";
		for (int i = 0; i < data.length; i++) {
			result += Integer.toHexString((data[i] & 0xFF) | 0x100)
					.toUpperCase().substring(1, 3);
		}
		return result;
	}
//  
//  @Override
//  public void channelActive(ChannelHandlerContext ctx) throws Exception {
//      //ctx.fireChannelActive();
//  }

}