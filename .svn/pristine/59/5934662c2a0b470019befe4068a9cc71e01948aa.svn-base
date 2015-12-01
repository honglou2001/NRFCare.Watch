package nrfCare.Netty;

import java.util.concurrent.TimeUnit;

import nrfCare.Component.Config;
import nrfCare.Component.XMLReader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 * <p>
 * 	Netty Server Simple
 * </p>
 * 
 * @author 卓轩
 * @创建时间：2014年7月7日
 * @version： V1.0
 */

public class NettyServer {
  
  private final int port = 8012;
  private static Logger logger = Logger.getLogger(NettyServer.class);
  
//  //设置6秒检测chanel是否接受过心跳数据
//  private static final int READ_WAIT_SECONDS = 6;
//	
//  //定义客户端没有收到服务端的pong消息的最大次数
//  private static final int MAX_UN_REC_PING_TIMES = 3;

  public void nettyServer(){
    
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    
    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup,workerGroup)
        .channel(NioServerSocketChannel.class)
//        .option(ChannelOption.SO_BACKLOG, 1024)
         .option(ChannelOption.SO_KEEPALIVE, true)
         .option(ChannelOption.SO_REUSEADDR, true)
        .childHandler(new ChildChannelHandler());
      
      //绑定端口、同步等待
      
		Config config = XMLReader.loadconfig();
		String socketip = config.socketip;
		String socketport = config.socketport;
		
		ChannelFuture futrue = serverBootstrap.bind(socketip, Integer.parseInt(socketport)).sync();	
		 
      //ChannelFuture futrue = serverBootstrap.bind(port).sync();
      logger.info("server 启动");
      //等待服务监听端口关闭
      futrue.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally{
      //退出，释放线程等相关资源
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
      logger.info("server shutdownGracefully");
    }    
  }

  private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
    	ChannelPipeline p = ch.pipeline(); 	
    	p.addLast(new IdleStateHandler(120, 0, 0, TimeUnit.SECONDS));	
    	p.addLast(new SimpleServerHandler());
    	
    	logger.info(String.format("remoteAddress:%s,port:%s", ch.remoteAddress().getAddress(),ch.remoteAddress().getPort()));
    }
  }
    
  public static void StartSocketServer()
  {	  	  
	  new NettyServer().nettyServer();	
  }
  
  public static void main(String[] args) throws Exception {	
	  PropertyConfigurator.configure("log4j.properties");	
	  StartSocketServer();
  }
}