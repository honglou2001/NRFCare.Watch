package nrfCare.FrontEnd;
import java.io.File;
import java.io.IOException;
import java.io.Console;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import nrfCare.Component.socketServer;

public class nrfConsole {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub	
//		System.out.println("1122");  
//				File f = null;
//				String path;
//				boolean bool = false;
//
//
//				f = new File("c:");
//		            
//		         // true if the file path is directory, else false
//		         bool = f.isDirectory();
//		         
//		         // get the path
//		         path = f.getPath();
//		         
//		         // prints
//		         System.out.println(path+" is directory? "+ bool);
//		               
//		         // create new file
//		         f = new File("c:/test11.txt");
//		         
//		         // true if the file path is directory, else false
//		         bool = f.isDirectory();
//		         
//		         // get the path
//		         path = f.getPath();
//		         
//		         // prints
//		         System.out.print(path+" is directory? "+bool);
//		         
//		         if  (!f.exists()  && !f.isDirectory())      
//					{       
//						 System.out.println("1122");  
//						 f.mkdir();    
//					} else   
//					     {  
//							System.out.println("2122");  
//						 }  
//				
//		
		
		LogThread ds1 = new LogThread();
		Thread t1 = new Thread(ds1);

        t1.start(); 
        
//        try {  
//            TimeUnit.MINUTES.sleep(1);  
//        } catch (InterruptedException e) {  
//            // TODO Auto-generated catch block  
//            e.printStackTrace();  
//        }  

        
//        try {
//        	System.in.read();
//        	}
//        	catch(Exception e) {
//
//        	}

//		Logger logger = Logger.getLogger(nrfConsole.class); //Server为类名
//		PropertyConfigurator.configure("log4j.properties");
//		logger.info("hello!");
        
        SendThread  ds2 = new SendThread();
		Thread t2 = new Thread(ds2);
        t2.start(); 
		
		
	}

}


class LogThread implements Runnable {
	Logger logger = Logger.getLogger(nrfConsole.class); //Server为类名
	
    //…

	public void run() {
		
		PropertyConfigurator.configure("log4j.properties");
		
		socketServer server = new socketServer("localhost", 7799);  
        server.start();  
		 
//		while(true)
//		{
//			PropertyConfigurator.configure("log4j.properties");
//			logger.info("hello!");
//			
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}// 睡眠100毫秒  	
//		}
      }
}


class SendThread implements Runnable {

	Logger logger = Logger.getLogger(SendThread.class); //Server为类名
	
	public void run() {

		 
		while(true)
		{
			
			try {
				
				Thread.sleep(1000);
				
				for(int i=0;i<socketServer.clients.size();i++)
				{
					
					DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
					Date jdkDate = new Date();  
					String dateStr =format.format(jdkDate);  
					
					SocketChannel socketChannel = socketServer.clients.get(i);
					
					String str ="content"+dateStr+"id:"+socketChannel.socket().getPort();
					ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
					
					
					//key.attach(byteBuffer);
					handleWrite(socketChannel,byteBuffer);
					
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 睡眠100毫秒  	
			   catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
      }
	
	
	public void handleWrite(SocketChannel socketChannel,ByteBuffer byteBuffer) throws IOException {
//		ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
//		byteBuffer.flip();
//		SocketChannel socketChannel = (SocketChannel) key.channel();
		try{
			socketChannel.write(byteBuffer);
		
		//socketChannel.
		//key.interestOps(SelectionKey.OP_WRITE);
			byteBuffer.compact();
		
		}
		catch(java.nio.channels.ClosedChannelException e)
		{
			logger.error(String.format("handleWrite %1$s", e.toString()));
		}
	}
}
