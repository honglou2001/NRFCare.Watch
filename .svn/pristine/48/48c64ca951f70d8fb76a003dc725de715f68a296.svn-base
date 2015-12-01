package nrfCare.SerialPort;

import java.util.ArrayList;
import java.util.List;

	public abstract class DataSubject {
	    /**
	     * 用来保存注册的观察者对象
	     */
	    //private List<SocketHanlder> list = new ArrayList<SocketHanlder>();
		private SocketHanlder SerialPortObserver = null;

	    /**
	     * 注册观察者对象
	     * @param observer    观察者对象
	     */
	    public void attach(SocketHanlder observer){
	    	SerialPortObserver = observer;
	        //list.add(observer);
	        System.out.println("Attached an observer");
	    }
	    
	    /**
	     * 删除观察者对象
	     * @param observer    观察者对象
	     */
	    public void detach(SocketHanlder observer){
	    	SerialPortObserver = null;
	        //list.remove(observer);
	    }

	    /**
	     * 通知所有注册的观察者对象
	     */
	    public void nodifyObservers(int type,byte[] receivedData){
	        
	    	SerialPortObserver.dealWithData(type,receivedData);
	    	
//	        for(SocketHanlder observer : list){
//	            observer.dealWithData(receivedData);
//	        }
	    }
	}