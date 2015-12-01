package nrfCare.SerialPort;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import nrfCare.Component.Config;
import nrfCare.Component.XMLReader;
import nrfCare.Netty.NettyCardServer;

//cmd运行jar包，把 MqConsumer.jar放在此目录下
//cd G:\Java\workspace\web8\NRFCare.Watch
//java -jar MqConsumer.jar
public class SerialPortMain {
	
	public static SerialPortCls SerialThread;
	private static final Logger logger = Logger.getLogger(SocketHanlderConcrete.class);
	
	public static void main(String[] args) throws Exception {	
		PropertyConfigurator.configure("log4j.properties");	
	
	  	Runnable r = new SocketThread();
	    Thread t = new Thread(r);
	    t.start();	
	    
	    Config config = XMLReader.loadconfig();
		try {					
			SerialThread= new SerialPortCls(config.servercom);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.toString());
		}
	    Thread threadSerialPort= new Thread(SerialThread);
	    threadSerialPort.start();
	    
	    logger.info(config.servercom+"串口线程启动");
	  }	  
}

	class SocketThread implements Runnable {			 
	    @Override
	    public void run() {
	        System.out.println("NettyCardServer-线程：" + Thread.currentThread().getName());         
	      //tcp监听
	        NettyCardServer.StartSocketServer();			
	    }
	 }
