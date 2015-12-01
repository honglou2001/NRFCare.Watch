package nrfCare.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TooManyListenersException;

import nrfCare.Utility.ByteConverter;
import nrfCare.Utility.Hex;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialPortCls implements Runnable, SerialPortEventListener {

	private static final Logger logger = Logger.getLogger(SerialPortCls.class);

	private SerialPort serialPort = null;
	private InputStream iStream;
	private OutputStream outputStream;
	private String comName = "";
	public static ArrayList<Byte> m_buffer = new ArrayList<Byte>(4096);// 默认分配1页内存，并始终限制不允许超过

	private DataSubjectConcrete subject;
	private SocketHanlderConcrete observer;
	private byte[] m_bytesRead =null;

	public SerialPortCls(String cname) throws Exception {
		
		this.comName = cname;
		// 创建主题对象
		subject = new DataSubjectConcrete();
		// 创建观察者对象
		observer = new SocketHanlderConcrete();
		// 将观察者对象登记到主题对象上
		subject.attach(observer);

	}

	private void initSerialPort() throws Exception {

		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(this.comName);
		this.serialPort = (SerialPort) portIdentifier.open("安全模块", 5000);

		int rate = 115000;
		int dataBits = 8;
		int stopBits = 1;
		int parity = 0;

		this.serialPort.setSerialPortParams(rate, dataBits, stopBits, parity);
		this.serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

		this.outputStream = this.serialPort.getOutputStream();
		this.iStream = this.serialPort.getInputStream();

		this.initListener();
		
		logger.info(String.format("initSerialPort %s", this.comName));

//		if (this.comName.equals("COM1")) {
//			byte[] bytes = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
//					(byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x01, 0x22 };
//			for (int i = 0; i < 10; i++) {
//				Thread.sleep(1000);
//				this.WriteBytes(bytes);
//			}
//		}

	}

	// starts the event listener that knows whenever data is available to be
	// read
	// pre: an open serial port
	// post: an event listener for the serial port that knows when data is
	// recieved
	private void initListener() {
		try {
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (TooManyListenersException e) {
			logger.error(e.toString());
		}
	}

	// disconnect the serial port
	// pre: an open serial port
	// post: clsoed serial port
	public void disconnect() {
		// close the serial port
		try {
			serialPort.removeEventListener();
			serialPort.close();
			this.iStream.close();
			this.outputStream.close();

		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	public void WriteBytes(byte[] bytes) throws IOException {
		// byte[] bytes = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96,
		// 0x69, 0x00, 0x03, 0x20, 0x01, 0x22 };

		// SerialPortCls serialPort = new SerialPortCls("COM1");
		// serialPort.WriteBytes(bytes);

		this.outputStream.write(bytes);

		String info = Hex.byteToArray(bytes);

		logger.info(String.format("Write %s,%s", this.comName, info));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.initSerialPort();

		} catch (Exception ex) {
			logger.info(ex.toString());
		}

	}

	
	@Override
	// what happens when data is received
	// pre: serial event is triggered
	// post: processing on the data it reads
	public synchronized void serialEvent(SerialPortEvent evt) {
		if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				// byte singleData = (byte)this.iStream.read();
				// System.out.println(singleData);

				m_bytesRead = new byte[4096];
				int len = -1;
				//if (this.iStream.available() > 0){
					 len = this.iStream.read(m_bytesRead);
				 //}

				// int n = _serialPort.BytesToRead;
				//
				// if (n > 0)
				// {
				// byte[] buf = new byte[n];
				// _serialPort.Read(buf, 0, n);//读取缓冲数据
				// m_buffer.AddRange(buf);
				//
				// while (m_buffer.Count > 1)
				// {
				// byte[] byteLength = new byte[2];
				// m_buffer.CopyTo(0, byteLength, 0, 2);
				// int len = BitConverter.ToInt16(byteLength, 0);
				//
				// if (len <= m_buffer.Count)
				// {
				// byte[] readBuffer1 = new byte[len];
				// m_buffer.CopyTo(0, readBuffer1, 0, len);
				// m_buffer.RemoveRange(0, len);
				// DealWithReceiveData(readBuffer1); //處理數據
				// }
				// else
				// {
				// break;
				// }
				// }
				// }
				if (len > 0) {
					for (int i = 0; i < len; i++) {
						this.m_buffer.add(m_bytesRead[i]);
					}
					while (this.m_buffer.size() > 1) {
						byte[] byteLength = new byte[2];
						byteLength[0] = this.m_buffer.get(0);
						byteLength[1] = this.m_buffer.get(1);

						int packetlen = ByteConverter.byteToInt(byteLength);

						if (packetlen <= this.m_buffer.size()) {
							
							byte[] readBuffer1 = new byte[packetlen];

							Iterator<Byte> iterator = this.m_buffer.iterator();

							int i = 0;
							while (iterator.hasNext()) {

								readBuffer1[i] = (byte) iterator.next();// 这样转型成byte
								i = i + 1;
								iterator.remove();
							}

							subject.dealWithData(2, readBuffer1);
							String info = Hex.byteToArray(readBuffer1);
							
							logger.info(String.format("read %s and sent,%s",this.comName, info));
						} else {
							break;
						}

					}
				}

				// this.m_buffer.add(singleData);
				// String info1 =Hex.byteToArray(bytesRead);
				// logger.info(String.format("read %s,%s",this.comName,info1));
				//
				// if(totallen>9){
				//
				// //byte[] bytes = new byte[ totallen];
				// Iterator<Byte> iterator = this.m_buffer.iterator();
				//
				// while (iterator.hasNext()) {
				// int i = 0;
				// bytes[i] = (Byte) iterator.next();//这样转型成byte
				// i = i + 1;
				// iterator.remove();
				// }
				// String info =Hex.byteToArray(bytes);
				// logger.info(String.format("read %s,%s",this.comName,info));
				// }
			} 
			catch(java.lang.ArrayIndexOutOfBoundsException ex)
			{
				
			}
			catch (Exception e) {
				logger.error(e.toString());
			}
		}
	}

	public static void main(String[] args) throws Exception {

		PropertyConfigurator.configure("log4j.properties");

		SerialPortCls com2Thread = new SerialPortCls("COM2");
		Thread thread2 = new Thread(com2Thread);
		thread2.start();

		SerialPortCls com1Thread = new SerialPortCls("COM1");
		Thread thread1 = new Thread(com1Thread);
		thread1.start();

	}
}
