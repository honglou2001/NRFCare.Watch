package nrfCare.Netty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import nrfCare.Component.Utility;
import nrfCare.Utility.ByteConverter;
import nrfCare.Utility.Hex;

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
	
  private static Map<String, Channel> map = new HashMap<String, Channel>();
  private static final Logger logger = Logger.getLogger(SimpleServerHandler.class);	 
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    ByteBuf inbuf = (ByteBuf)msg;
    byte [] req = new byte[inbuf.readableBytes()];      
    
    int length = inbuf.readableBytes();
    
    String head =byteToArray(inbuf.readBytes(2).array());// inbuf.readBytes(2).toString(Charset.defaultCharset());
   
    ByteBuf bytesLength= inbuf.readBytes(2);
    
    short slength = bytesToShort(bytesLength.array());
    
    String clientNum = byteToArray(inbuf.readBytes(4).array());
    
    String cmd =  byteToArray(inbuf.readBytes(2).array());
    
    logger.info(String.format("slength:%s,head:%s,clientNum:%s,cmd:%s", slength,head,clientNum,cmd));	
    
	byte[] bytes =inbuf.array();		
	String sInfo = Hex.encodeHexStr(bytes);
	logger.info(String.format("Server received:%s", sInfo));	
    
//	inbuf.readBytes(req);
    
    String message = new String(req,"UTF-8");    
    Timestamp times = Utility.getTimeStamp();
            
    System.out.println("Netty-Server:Receive Message,"+ message+times);    
    String retstr = "Response from server" + message+times;
    
    //byte [] req1 = retstr.getBytes();
    String senddatastring = "Response from server" + times;;
    byte[] req1 = ByteConverter.GetSendData(senddatastring);
    ByteBuf clientMessage = Unpooled.buffer(req1.length);
    clientMessage.writeBytes(req1);
    
	if (!map.containsKey(clientNum)) {
		map.put(clientNum, ctx.channel());				
	}	
    
    ctx.writeAndFlush(clientMessage);
  
  }
  
  public static void SendToClient(String clientNum, byte [] req)
  {
	  //byte [] req1 = retstr.getBytes();
	    ByteBuf clientMessage = Unpooled.buffer(req.length);
	    clientMessage.writeBytes(req);
	    
//		if (map.containsKey(clientNum)) {
//			Channel ctx = map.get(clientNum);
//			 ctx.writeAndFlush(clientMessage);
//			
//		}
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Channel ctx = map.get(key);
//			Channel ctx = map.get("2D557365");
			ctx.writeAndFlush(clientMessage);
			System.out.println("map.size():" + map.size() +"key:"+key);
//			if (key.equals(serialNumber)) {
//				it.remove();
//				map.remove(serialNumber);						
//				System.out.println("map.size():" + map.size());
//				
//			}
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
	
  
//  public static short getShort(byte[] bytes)  
//    {  
//        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));  
//    }  
 

	public static String byteToArray(byte[] data) {
		String result = "";
		for (int i = 0; i < data.length; i++) {
			result += Integer.toHexString((data[i] & 0xFF) | 0x100)
					.toUpperCase().substring(1, 3);
		}
		return result;
	}

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      // Close the connection when an exception is raised.
      cause.printStackTrace();
      ctx.close();
  }
//  
//  @Override
//  public void channelActive(ChannelHandlerContext ctx) throws Exception {
//      //ctx.fireChannelActive();
//  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
	  if (evt instanceof IdleStateEvent) {
			String key = null;
			if (map.containsValue(ctx.channel())) {
				for (Entry<String, Channel> entries : map.entrySet()) {
					if (entries.getValue() == ctx.channel()) {
						key = entries.getKey();
					}
				}
			} 
			if (key != null) {
				Iterator<String> it = map.keySet().iterator();
				while (it.hasNext()) {
					String serialNumber = it.next();
					if (key.equals(serialNumber)) {
						it.remove();
						map.remove(serialNumber);						
						System.out.println("map.size():" + map.size());
						//判断设备下线
						//OnLineService.getOffLine(key);						
					}
				}
			}
			ctx.channel().close();
		} else {
			super.userEventTriggered(ctx, evt);
		}
  }
}