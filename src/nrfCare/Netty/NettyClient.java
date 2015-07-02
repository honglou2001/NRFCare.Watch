package nrfCare.Netty;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * <p>
 * 	NettyClient  实现
 * </p>
 * 
 * @author 卓轩
 * @创建时间：2014年7月7日
 * @version： V1.0
 */
public class NettyClient {

    private static final Logger logger = Logger.getLogger(NettyClient.class);
    
	private static Channel channel  = null;
	private static ChannelFuture channelFuture; 

    
  
  public void connect(int port,String host){
    
    EventLoopGroup group = new NioEventLoopGroup();
    
    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(group)
      .channel(NioSocketChannel.class)
      .option(ChannelOption.TCP_NODELAY, true)
      .handler(new ChannelInitializer<SocketChannel>() {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
          ch.pipeline().addLast(new SimpleClientHandler());
        }
      });
      //发起异步链接操作
      channelFuture = bootstrap.connect(host, port).sync();
      
      channel = channelFuture.channel();
      
      //channelFuture.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally{
      //关闭，释放线程资源
      //group.shutdownGracefully();
    }
  }
  

  public void nettyClient(){  
    connect(8012, "localhost");
  }
  
  

  
  public static void sendMsg(String msg) throws Exception {
      if (channel != null) {
      	         
          byte [] req = "Call-User-Service".getBytes();
          ByteBuf clientMessage = Unpooled.buffer(req.length);
          clientMessage.writeBytes(req);
      	
          channel.writeAndFlush(clientMessage).sync();
      } else {
          logger.warn("消息发送失败,连接尚未建立!");
      }
  }
  
  public void stop() {  
      channelFuture.awaitUninterruptibly();  
      if (!channelFuture.isSuccess()) {  
          channelFuture.cause().printStackTrace();  
      }  
      channelFuture.channel().closeFuture().awaitUninterruptibly();  
      //bootstrap.();  
  }
  
  public static void main(String[] args) throws Exception {
	  
	  PropertyConfigurator.configure("log4j.properties");		
	  new NettyClient().nettyClient();
	  
      try {
          long t0 = System.nanoTime();
          for (int i = 0; i < 2; i++) {
        	NettyClient.sendMsg(i + "你好1");
          	Thread.sleep(1000);                 
          }
          long t1 = System.nanoTime();
          System.out.println((t1 - t0) / 1000000.0);
      } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
  }
}