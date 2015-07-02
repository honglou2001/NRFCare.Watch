package nrfCare.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;


public class Utility {
	
	public static  ArrayList<String> GetLocalIP() throws SocketException { 
	     Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces(); 
	     InetAddress ip = null; 
	     ArrayList<String> iplists = new ArrayList<String>();
	     
	     while (allNetInterfaces.hasMoreElements()) { 
	         NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement(); 
	         // System.out.println(netInterface.getName());  
	         Enumeration<InetAddress> addresses = netInterface.getInetAddresses(); 
	         while (addresses.hasMoreElements()) { 
	             ip = (InetAddress) addresses.nextElement(); 
	             if (ip != null && ip instanceof Inet4Address) { 
	                 System.out.println("本机的IP = " + ip.getHostAddress()); 
	                 iplists.add(ip.getHostAddress());
	             } 
	         } 
	     } 	     
	     return iplists;
	 } 	
	
    public static Date getNowDate() {  
    	  Date currentTime = new Date(); 
    	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    	  String dateString = formatter.format(currentTime);  
    	  ParsePosition pos = new ParsePosition(8);  
    	  Date currentTime_2 = formatter.parse(dateString, pos);  
    	  return currentTime_2;  
    	}  
      
      public static Timestamp getTimeStamp()
      {
//      	Date date = new Date();
//      	Timestamp nousedate = new Timestamp(date.getTime());
//      	return nousedate;
      	
      	Timestamp nowdate1 = new Timestamp(System.currentTimeMillis());
      	return nowdate1;
      
      }
}
