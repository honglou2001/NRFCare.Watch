package nrfCare.SerialPort;

import java.io.IOException;

import nrfCare.Netty.NettyCardServerHandler;

import org.apache.log4j.Logger;

public class SocketHanlderConcrete implements SocketHanlder  {
	private static final Logger logger = Logger.getLogger(SocketHanlderConcrete.class);	
	@Override
	public void dealWithData(int type,byte[] receivedData) {
		// TODO Auto-generated method stub		
		if(type==1){
			try {
				SerialPortMain.SerialThread.WriteBytes(receivedData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.toString());
			}
		}else if (type==2){			
			NettyCardServerHandler.SendToClient("", receivedData);
		}		
	}
}
