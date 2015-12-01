package nrfCare.Netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class DiscardClient {
    private static final Logger logger = Logger.getLogger(DiscardClient.class);
    public static String HOST = "127.0.0.1";
    public static int PORT = 8012;
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));
    public static Bootstrap bootstrap = getBootstrap();
    public static Channel channel = getChannel(HOST, PORT);

   /**
     * 初始化Bootstrap
     * 
     * @return
     */
    public static final Bootstrap getBootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
//                pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,4, 0, 4));
//                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                pipeline.addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
//                pipeline.addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));
                
//				  pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8192, false,Delimiters.lineDelimiter()));
				  pipeline.addLast(new ByteToMessageDec());
                  pipeline.addLast(new MessageToByteEnc()); 
                
                  pipeline.addLast("handler", new DiscardClientHandler());
            }
        });
        b.option(ChannelOption.SO_KEEPALIVE, true);
        return b;
    }

   public static final Channel getChannel(String host, int port) {
        Channel channel = null;
        try {
            channel = bootstrap.connect(host, port).sync().channel();
        } catch (Exception e) {
            logger.error(
                    String.format("连接Server(IP[%s],PORT[%s])失败", host, port), e);
            return null;
        }
        return channel;
    }

   public static void sendMsg(String msg) throws Exception {
        if (channel != null) {
        	
        	byte[] bytes = {0x01,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02};
        	
        	
        	ByteBuf buffer= PooledByteBufAllocator.DEFAULT.heapBuffer(10);
            buffer.writeShort(Short.MIN_VALUE);//包长占2字节
            buffer.writeByte(1);
            buffer.writeByte(0);
            buffer.setShort(0,buffer.writerIndex()-0x2);

        	
        	//ByteBuf  bybuf =Unpooled.wrappedBuffer(bytes);
        	
            channel.writeAndFlush(bytes).sync();
        } else {
            logger.warn("消息发送失败,连接尚未建立!");
        }
    }

   public static void main(String[] args) throws Exception {
	   
	   PropertyConfigurator.configure("log4j.properties");
		
        try {
            long t0 = System.nanoTime();
            for (int i = 0; i < 2; i++) {
            	DiscardClient.sendMsg(i + "你好1");
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
