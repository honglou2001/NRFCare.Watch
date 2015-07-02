package nrfCare.Netty;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import nrfCare.Component.Utility;
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

	 private static final Logger logger = Logger.getLogger(SimpleServerHandler.class);
	 
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    ByteBuf inbuf = (ByteBuf)msg;
    byte [] req = new byte[inbuf.readableBytes()];      
    
    
	byte[] bytes =inbuf.array();		
	String sInfo = Hex.encodeHexStr(bytes);

	logger.info(String.format("Server received:%s", sInfo));	
    
	inbuf.readBytes(req);
    
    String message = new String(req,"UTF-8");
    
    Timestamp times = Utility.getTimeStamp();
            
    System.out.println("Netty-Server:Receive Message,"+ message+times);
    
    String retstr = "Response from server" + message+times;
    
    byte [] req1 = retstr.getBytes();
    ByteBuf clientMessage = Unpooled.buffer(req1.length);
    clientMessage.writeBytes(req1);
    
    ctx.writeAndFlush(clientMessage);
  
  }
}