package nrfCare.FrontEnd;
import java.io.File;
import java.io.IOException;
import java.io.Console;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

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
		
		
	}

}


class LogThread implements Runnable {
	Logger logger = Logger.getLogger(nrfConsole.class); //Server为类名
    //…

	public void run() {
		while(true)
		{
			PropertyConfigurator.configure("log4j.properties");
			logger.info("hello!");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 睡眠100毫秒  
	
	
		}
      }
}
