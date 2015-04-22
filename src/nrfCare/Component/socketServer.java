package nrfCare.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * NIO服务端
 * 
 * @author shirdrn
 */
public class socketServer extends Thread {

	private static final Logger log = Logger.getLogger(socketServer.class);
	private InetSocketAddress inetSocketAddress;
	private Handler handler = new ServerHandler();
	public static List<SocketChannel> clients = new ArrayList<SocketChannel>();  
	//private ByteBuffer m_sendBuffer = ByteBuffer.allocate(1024);

	public socketServer(String hostname, int port) {
		inetSocketAddress = new InetSocketAddress(hostname, port);
	}

	@Override
	public void run() {

		try {
			// 定义一个事件选择器对象记录套接字通道的事件
			Selector selector = Selector.open(); // 打开选择器
			// 定义一个异步服务器socket对象
			ServerSocketChannel serverSocketChannel = ServerSocketChannel
					.open(); // 打开通道
			// 将此socket对象设置为异步
			serverSocketChannel.configureBlocking(false); // 非阻塞
			// 定义服务器socket对象-用来指定异步socket的监听端口等信息
			ServerSocket ss = serverSocketChannel.socket();
			ss.bind(inetSocketAddress);
			// serverSocketChannel.socket().bind(inetSocketAddress);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // 向通道注册选择器和对应事件标识
			log.info("Server: socket server started.");
			
									
			try {
				while (true) { // 轮询
					// //查询事件如果一个事件都没有就阻塞
					int nKeys = selector.select();
					if (nKeys > 0) {
						Set<SelectionKey> selectedKeys = selector
								.selectedKeys();
						Iterator<SelectionKey> it = selectedKeys.iterator();
						// 此循环遍例所有产生的事件
						while (it.hasNext()) {
							SelectionKey key = it.next();
							// 将本次事件删除
							it.remove();
							try {
								if (key.isAcceptable()) {
									log.info("Server: SelectionKey is acceptable.");
									handler.handleAccept(key);
								} else if (key.isReadable()) {

									//key.interestOps(key.interestOps() & (~SelectionKey.OP_READ)); 
									
									log.info("Server: SelectionKey is readable---."+selectedKeys.size());
									handler.handleRead(key);	
									//key.cancel();
								} else if (key.isWritable()) {
									log.info("Server: SelectionKey is writable.--"+selectedKeys.size());
									handler.handleWrite(key);
									key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE)); 
								}								
								//key.cancel();								
							} catch (IOException e) {
								log.error(String.format("handaleRead Err000%1$s", e.toString()));
								key.cancel();
								try {
									key.channel().close();
								} catch (IOException ioe) {
								}
								//删除socket
								if(clients.contains(key.channel())){
									clients.remove(key.channel());
								}
							}
						}
						
						
					}
				}
			} catch (IOException e) {
				e.printStackTrace();

			}

		} catch (IOException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
		}

	}

	/**
	 * 简单处理器接口
	 * 
	 * @author shirdrn
	 */
	interface Handler {
		/**
		 * 处理{@link SelectionKey#OP_ACCEPT}事件
		 * 
		 * @param key
		 * @throws IOException
		 */
		void handleAccept(SelectionKey key) throws IOException;

		/**
		 * 处理{@link SelectionKey#OP_READ}事件
		 * 
		 * @param key
		 * @throws IOException
		 */
		void handleRead(SelectionKey key) throws IOException;

		/**
		 * 处理{@link SelectionKey#OP_WRITE}事件
		 * 
		 * @param key
		 * @throws IOException
		 */
		void handleWrite(SelectionKey key) throws IOException;
	}
	
//	class MutliThread extends Thread{
//	    private int ticket=100;//每个线程都拥有100张票
//	    MutliThread(String name){
//	        super(name);//调用父类带参数的构造方法
//	    }
//	    public void run(){
//	        while(ticket>0){
//	            System.out.println(ticket--+" is saled by "+Thread.currentThread().getName());
//	        }
//	    }
//	}

	/**
	 * 服务端事件处理实现类
	 * 
	 * @author shirdrn
	 */
	class ServerHandler implements Handler {

		@Override
		public void handleAccept(SelectionKey key) throws IOException {
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
					.channel();
			SocketChannel socketChannel = serverSocketChannel.accept();
			log.info("Server: accept client socket " + socketChannel);
			socketChannel.configureBlocking(false);
			
			
			socketChannel.register(key.selector(), SelectionKey.OP_READ);
			//保留socket列表
			clients.add(socketChannel);
		}

		@Override
		public void handleRead(SelectionKey key) throws IOException {
			// 定义一个byte缓冲区来存储收发的数据
			SocketChannel socketChannel = null;

//			try {
				log.info("handleRead begin");
				ByteBuffer byteBuffer = ByteBuffer.allocate(512);

				socketChannel = (SocketChannel) key.channel();

				int readBytes = socketChannel.read(byteBuffer);
				if (readBytes > 0) {
					log.info("Server: readBytes = " + readBytes);
					log.info("Server: data = "
							+ new String(byteBuffer.array(), 0, readBytes));
					
					log.info(socketChannel.socket().getInetAddress());
					log.info(socketChannel.socket().getPort());
					
					
					//if (byteBuffer.hasRemaining()) {
						//socketChannel.write(byteBuffer);		
						key.attach(byteBuffer);
						key.interestOps(SelectionKey.OP_WRITE);
					//}
					// 将缓冲区复位以便于进行其他读写操作
					//byteBuffer.flip();
					//socketChannel.write(byteBuffer);
					// break;
					//key.interestOps(SelectionKey.OP_READ); // 接收 客户端发送数据事件 | 客户端读取服务端返回事件  
				}
//			}
				if (readBytes == -1) { // 从客户端读取到的数据为空（当客户端关闭通道时，会触发该读取事件），说明已关闭  
					socketChannel.close();  
		        } 
				 
//
//			catch (IOException e) {
//
//				try {
//					socketChannel.close();
//				} catch (IOException ioe) {
//				}
//			} catch (Exception e) {
//				log.error(String.format("handaleRead Err2%1$s", e.getMessage()));
//
//			}
			log.info("handleRead done");

			// socketChannel.close();
		}

		@Override
		public void handleWrite(SelectionKey key) throws IOException {
			ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
			byteBuffer.flip();
			SocketChannel socketChannel = (SocketChannel) key.channel();
			socketChannel.write(byteBuffer);
			//if (byteBuffer.hasRemaining()) {
				key.interestOps(SelectionKey.OP_READ);
			//}
			byteBuffer.compact();
		}
	}

	// public static void main(String[] args) {
	// NioTcpServer server = new NioTcpServer("localhost", 1000);
	// server.start();
	// }
}