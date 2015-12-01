package nrfCare.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class MapInfo {
   private static Map<String,PigInfo> mapPig = new HashMap<String,PigInfo>();
   private static Map<String,Porkinfo> mapPork = new HashMap<String,Porkinfo>();
   private static Map<String,String> mapPorKPig = new HashMap<String,String>();
   
   static{
	   
	   intialPigInfo();
	   intialPorkInfo();
	   intialMapPigPork();
   }
   
   private  static void intialMapPigPork()
   {
	   mapPorKPig.put("2200081", "1");
	   mapPorKPig.put("2200082", "2");
	   mapPorKPig.put("2200083", "3");
	   mapPorKPig.put("2200084", "4");
   
   }
   
   private  static void intialPigInfo()
   {
	   PigInfo pig = null;
	   Timestamp timestamp = Utility.getTimeStamp();
	   SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	   String slatime = sd.format(timestamp);
	   	   
	   pig = new PigInfo();
	   pig.setMadein("郴州桂阳·菖蒲F2");
	   pig.setInfourl("http://t.im/m7q4");
	   pig.setSlatime(slatime);
	   mapPig.put("1", pig);
	   
	   pig = new PigInfo();
	   pig.setMadein("郴州桂阳·丰塘F1");	 
	   pig.setInfourl("http://t.im/m79r");
	   pig.setSlatime(slatime);
	   mapPig.put("2", pig);
	   	   	   
	   pig = new PigInfo();
	   pig.setMadein("郴州栖枫渡·新湘农土猪F1");
	   pig.setInfourl("http://t.im/m7q4");
	   pig.setSlatime(slatime);
	   mapPig.put("3", pig);
	   
	   pig = new PigInfo();
	   pig.setMadein("郴州桂阳·燕塘F3");
	   pig.setInfourl("http://t.im/m79r");
	   pig.setSlatime(slatime);
	   mapPig.put("4", pig);
   }
   
   private  static void intialPorkInfo()
   {
	   Porkinfo pork = null;
	   
	   pork = new Porkinfo();	   
	   pork.setPorname("土猪·五花肉");
	   //pork.setImgurl(R.drawable.caipin2);
	   mapPork.put("2200081", pork);
	   
	   pork = new Porkinfo();	   
	   pork.setPorname("土猪·前排肉");
	   //pork.setImgurl(R.drawable.caipin1);
	   mapPork.put("2200082", pork);
	   
	   pork = new Porkinfo();	   
	   pork.setPorname("土猪·后腿肉");
	   //pork.setImgurl(R.drawable.caipin1);
	   mapPork.put("2200083", pork);
	   
	   pork = new Porkinfo();	   
	   pork.setPorname("土猪·弹子肉");
	   //pork.setImgurl(R.drawable.caipin2);
	   mapPork.put("2200084", pork);
	   
   }
   
	public static PigInfo getPigInfo(String pid)
	{
		PigInfo pigInfo = null;
				
		if (MapInfo.mapPig.containsKey(pid)) 
		{
			pigInfo= MapInfo.mapPig.get(pid);
		}else
		{
			pigInfo = new PigInfo();			
			pigInfo.setMadein("郴州桂阳·丰塘F10");
			pigInfo.setInfourl("http://mp.weixin.qq.com/s?__biz=MjM5MjM2OTc3MA==&mid=206646275&idx=1&sn=d2b1ef900b64f80c5a0f75f1b420c4bc#rd");
		}
		
		return pigInfo;
	}
	
	public static String getPigID(String porkid)
	{
		if(porkid!=null&&porkid.length()>0)
		{
			if(mapPorKPig.containsKey(porkid)){
				return mapPorKPig.get(porkid);
			}
		}
		return "1";
	
	}

	public static Porkinfo getPorkInfo(String pid)
	{
		Porkinfo porkInfo = null;
		
		if (MapInfo.mapPork.containsKey(pid)) 
		{
			porkInfo= MapInfo.mapPork.get(pid);
		}else
		{
			porkInfo = new Porkinfo();	   
			porkInfo.setPorname("肉品名称");
			//porkInfo.setImgurl(R.drawable.caipin1);
		}		
		return porkInfo;
	}
}