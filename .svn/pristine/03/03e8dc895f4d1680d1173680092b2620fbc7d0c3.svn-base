package nrfCare.Utility;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ByteConverter {

	public ByteConverter()
	{
		
	}
	  //每个客户端建立ack值
	private static Map<String,Short> m_MapAck = new HashMap<String,Short>();
	  
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
	
	public static byte[] GetSendData(byte[] clientId,byte[] cmd,ByteConverter.AckWrapper ackWarpper,String sData,Boolean forReponse) throws UnsupportedEncodingException
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
          //byte[] clientId = { 0x01, 0x01, 0x4C, 0x4C };
          buffer.put(clientId);


          //byte[] cmd = { 0x00, 0x04 };
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
          
          //如果是主动发起则新算ack，如果是应答，使用发起方的ack
          if(forReponse==Boolean.FALSE){
        	 byte[] back=getAck(Hex.byteToArray(clientId));   
        	 
        	 ackWarpper.setByteAck(back);
        	 
        
        	 
        	 buffer.put(back);
        	 
          }else
          {
        	  byte[] back= ackWarpper.getByteAck();
        	  
        	  buffer.put(back);
          }
          //byte[] ack = { ack1[0], ack1[1] };
         
          byte[] crcCal = ComputeCRC(buffer.array(),length-4);
          byte[] crc = { crcCal[0], crcCal[1] };
          buffer.put(crc);

          byte[] end = { 0x0D, 0x0A };
          buffer.put(end);

          return buffer.array();
      }
	
		public static int ComputeCRCInt(byte[] val, int len)
		{
			
		    long crc;
		    long q;
		    byte c;
		    int i = 0;
			
		    crc = 0;
		    for (i = 0; i < len; i++)
		    {
		        c = val[i];
		        q = (crc ^ c) & 0x0f;
		        crc = (crc >> 4) ^ (q * 0x1081);
		        q = (crc ^ (c >> 4)) & 0xf;
		        crc = (crc >> 4) ^ (q * 0x1081);
		    }
		    return (byte)crc << 8 | (byte)(crc >> 8);
		}
		
		public static byte[] ComputeCRC(byte[] val, int len)
		{
			int d = ComputeCRCInt(val,len);
			
			byte[] result = new byte[2];   
			result[1] = (byte)((d >> 8) & 0xFF);
			result[0] = (byte)(d & 0xFF);
			
			return result;
		}
		
		  
		  private static byte[] getAck(String clinumber) 
		  {
			  Short sAck = Short.MIN_VALUE;
			  
			  if(m_MapAck.containsKey(clinumber)){			  
				  sAck = m_MapAck.get(clinumber);			  
				  if(sAck>=Short.MAX_VALUE)
				  {
					  sAck = Short.MIN_VALUE;
				  }			  
				  sAck++;			  
			  }			  
			  m_MapAck.put(clinumber, sAck) ;
			  
			  byte[] ack = shortToBytes(sAck,true);		  
			  return ack;
		  }
		  
		  public class AckWrapper
		  {
			private byte[] byteAck;

			public byte[] getByteAck() {
				return byteAck;
			}

			public void setByteAck(byte[] byteAck) {
				this.byteAck = byteAck;
			}
			  
		  }
}
