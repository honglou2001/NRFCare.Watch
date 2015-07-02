package nrfCare.Netty;

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import nrfCare.Component.Config;
import nrfCare.Component.XMLReader;
import nrfCare.FrontEnd.nrfConsole;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Discards any incoming data.
 */
public final class DiscardServer {

	static final boolean SSL = System.getProperty("ssl") != null;
//	static final int PORT = Integer
//			.parseInt(System.getProperty("port", "8009"));
	static Logger logger = Logger.getLogger(DiscardServer.class); // Server为类名

	protected static final int BIZGROUPSIZE = Runtime.getRuntime()
			.availableProcessors() * 2; // 默认
	/** 业务出现线程大小 */
	protected static final int BIZTHREADSIZE = 4;
	/*
	 * NioEventLoopGroup实际上就是个线程池,
	 * NioEventLoopGroup在后台启动了n个NioEventLoop来处理Channel事件,
	 * 每一个NioEventLoop负责处理m个Channel,
	 * NioEventLoopGroup从NioEventLoop数组里挨个取出NioEventLoop来处理Channel
	 */
	private static final EventLoopGroup bossGroup = new NioEventLoopGroup(
			BIZGROUPSIZE);
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup(
			BIZTHREADSIZE);

	public static void main(String[] args) throws Exception {

		PropertyConfigurator.configure("log4j.properties");

		logger.info("开始启动TCP服务器...");
		DiscardServer.run();

		// // Configure SSL.
		// final SslContext sslCtx;
		// if (SSL) {
		// SelfSignedCertificate ssc = new SelfSignedCertificate();
		// sslCtx = SslContext.newServerContext(ssc.certificate(),
		// ssc.privateKey());
		// } else {
		// sslCtx = null;
		// }
		//
		// EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		// EventLoopGroup workerGroup = new NioEventLoopGroup();
		// try {
		// ServerBootstrap b = new ServerBootstrap();
		// b.group(bossGroup, workerGroup)
		// .channel(NioServerSocketChannel.class)
		// //.handler(new LoggingHandler(LogLevel.INFO))
		// .childHandler(new ChannelInitializer<SocketChannel>() {
		// @Override
		// public void initChannel(SocketChannel ch) {
		// ChannelPipeline p = ch.pipeline();
		// if (sslCtx != null) {
		// p.addLast(sslCtx.newHandler(ch.alloc()));
		// }
		// p.addLast(new DiscardServerHandler());
		// }
		// });
		//
		//
		// // Bind and start to accept incoming connections.
		// ChannelFuture f = b.bind(PORT).sync();
		// logger.info(String.format("监听端口 %1$s", PORT));
		//
		// // Wait until the server socket is closed.
		// // In this example, this does not happen, but you can do that to
		// gracefully
		// // shut down your server.
		// f.channel().closeFuture().sync();
		// } finally {
		// workerGroup.shutdownGracefully();
		// bossGroup.shutdownGracefully();
		// }
	}

	protected static void run() throws Exception {

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline(); 
//                    p.addLast(new IdleStateHandler(0, 0, 150, TimeUnit.SECONDS));
//                    p.addLast("framer",new DelimiterBasedFrameDecoder(8192, false,Delimiters.lineDelimiter()));
                    p.addLast(new ByteToMessageDec());
                    p.addLast(new MessageToByteEnc());             
                    p.addLast(ServiceHandlerFactory.getDiscardServerHandler());
				}
			});

			Logger logger = Logger.getLogger(DiscardServer.class); // Server为类名
			Config config = XMLReader.loadconfig();
			String socketip = config.socketip;
			String socketport = config.socketport;

			ChannelFuture f = b.bind(socketip, Integer.parseInt(socketport))
					.sync();			
			//b.bind(socketip, Integer.parseInt(socketport)).sync();
			logger.info("TCP服务器已启动，"+socketip+":"+socketport);

			// Wait until the server socket is closed.
			f.channel().closeFuture().sync();
		} finally {
			//shutdown();
		}
	}

	protected static void shutdown() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}

}
