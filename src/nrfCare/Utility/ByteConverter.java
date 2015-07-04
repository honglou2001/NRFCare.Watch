package nrfCare.Utility;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Calendar;

public class ByteConverter {

	public static byte[] shortToBytes(short x,boolean endian)
	{
		byte[] ret = new byte[2];
		//Little Endian		
		if(endian == Boolean.TRUE){
		    ret[0] = (byte) x;
		    ret[1] = (byte) (x >> 8);
		}
		else{
		    //Big Endian
		    ret[0] = (byte) (x >> 8);
		    ret[1] = (byte) x;
		}	   
		
	
	    return ret;
	}
	
    public static Timestamp getTimeStamp()
    {    	   
    	Timestamp nowdate1 = new Timestamp(System.currentTimeMillis());
    	return nowdate1;
    
    }
        	
	public static byte[] GetDateTime()
    {
    	//Timestamp dtime = ByteConverter.getTimeStamp();
    	Calendar cal1 = Calendar.getInstance(); 


        byte[] year = ByteConverter.shortToBytes((short)cal1.YEAR,true);

        byte[] bdatetime = new byte[7];
        bdatetime[0] = year[0];
        bdatetime[1] = year[1];
         bdatetime[2] = (byte)cal1.MONTH;
         bdatetime[3]= (byte)cal1.DAY_OF_MONTH;
         bdatetime[4]= (byte)cal1.HOUR;
         bdatetime[5] = (byte)cal1.MINUTE;
         bdatetime[6]= (byte)cal1.SECOND;

         return bdatetime;
    }
	
	public static byte[] GetSendData(String sData) throws UnsupportedEncodingException
      {
        
          //String sData = String.format("%s|%s|%s", 1, 2, 3);

          byte[] bData = sData.getBytes("UTF-8");;
          short length = (short)(25 + bData.length);

          
          ByteBuffer buffer = ByteBuffer.allocate(length); 
          
          //ByteBuffer 
          byte[] head = { 0x5A, 0x4C };
          buffer.put(head);
                               
          //总帧长度
          byte[] blength = ByteConverter.shortToBytes(length, false);
          buffer.put(blength);
          

          //客户端编号
          byte[] clientId = { 0x01, 0x01, 0x4C, 0x4C };
          buffer.put(clientId);


          byte[] cmd = { 0x10, 0x01 };
          buffer.put(cmd);


          //总帧数
          byte[] framecount = { 0x01 };
          buffer.put(framecount);

          //第几帧
          byte[] frameindex = { 0x01 };
          buffer.put(frameindex);

          //数据内容 bData
          buffer.put(bData);

          byte[] datetime = ByteConverter.GetDateTime();
          buffer.put(datetime);

          byte[] ack = { 0x01, 0x01 };
          buffer.put(ack);

          byte[] crc = { 0x02, 0x04 };
          buffer.put(crc);

          byte[] end = { 0x0D, 0x0A };
          buffer.put(end);

          return buffer.array();
      }
}
