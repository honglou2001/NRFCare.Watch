package nrfCare.Netty;
/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.channel.ChannelHandler.Sharable;

import nrfCare.Component.Utility;
import nrfCare.Utility.BDTransUtil;
import nrfCare.Utility.Hex;
import org.apache.log4j.Logger;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {

	private static final Logger logger = Logger.getLogger(DiscardServerHandler.class);  	
	private ExecutorService executorService;
	private static Map<String, Channel> map = new HashMap<String, Channel>();
	
	public DiscardServerHandler()
	{
		
	}
	
	public DiscardServerHandler(ExecutorService executorService) {
		this.executorService = executorService;
	}

	
//    @Override
//    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        // discard
////    	logger.info(String.format("server :接收到消息 %1$s", msg.toString()));   	
//    	String msgSend ="\nserver : yes, server is accepted you ,nice !"+msg+Utility.getTimeStamp();
//    	System.out.print(msgSend);
//    	byte[] tBytes=msgSend.getBytes("US-ASCII");
//    	ctx.writeAndFlush(tBytes);  
//    	
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        // Close the connection when an exception is raised.
//    	logger.warn("Unexpected exception from downstream.", cause);  
//        cause.printStackTrace();
//        ctx.close();
//    }
//    @Override
//	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
//			throws Exception {
//		if (evt instanceof IdleStateEvent) {
//			System.out.println(ctx.channel().toString());
//			String key = null;
//			if (map.containsValue(ctx.channel())) {
//				for (Entry<String, Channel> entries : map.entrySet()) {
//					if (entries.getValue() == ctx.channel()) {
//						key = entries.getKey();
//					}
//				}
//			} else {
//				System.out.println("555555555");
//				System.out.println("map.size():" + map.size());
//			}
//			if (key != null) {
//				Iterator<String> it = map.keySet().iterator();
//				while (it.hasNext()) {
//					String serialNumber = it.next();
//					if (key.equals(serialNumber)) {
//						it.remove();
//						map.remove(serialNumber);
//						System.out.println("踢下线:" + key);
//						System.out.println("map.size():" + map.size());
////						SerialNumber sn = sndao.findBySerialNumber(key);
////						sn.setOnline("0");
////						sndao.updateSerialNumber(sn);
//					}
//				}
//			}
//			ctx.channel().close();
//		} else {
//			super.userEventTriggered(ctx, evt);
//		}
//	}

	@Override
	public void channelRead0(final ChannelHandlerContext ctx, Object msg)
			  {
		
//		logger.info("SERVER接收到消息:"+msg);  
//		ctx.channel().writeAndFlush("yes, server is accepted you ,nice !"+msg); 

		
		
//		String msg1 = (String) msg;
//		
//		System.out.print(msg1);
		
		ByteBuf in =(ByteBuf) msg;
		System.out.println(in.capacity());
		// 接收到包的长度
		int length = in.readableBytes();
		System.out.println("字节数:" + length);
//		
//		byte[] bytes =in.array();
//		
//		String s = "";
//		try {
//			s = new String(bytes, "US-ASCII");
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//    	String msgSend ="\nserver : yes, server is accepted you ,nice !"+ s +Utility.getTimeStamp();
//    	      	
//    	System.out.print(msgSend);
//    	byte[] tBytes;
//		try {
//			tBytes = msgSend.getBytes("US-ASCII");
//			//ctx.writeAndFlush(tBytes); 
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//		logger.info(msgSend);  
//		
//		// 终端设备上报到服务器位始 $$
//		String head = in.readBytes(2).toString(Charset.defaultCharset());
//		System.out.println(head);
//		if ("$$".equals(head)) {
//			// 从包的字段得到包的长度
//			short l = in.readShort();
//			System.out.println(l);
//			if(length==l){
//				// 得到产品id 序列号
//				final String serialNumber = byteToArray(in.readBytes(7).array());
//				System.out.println(serialNumber);
//				// 得到协议号
//				final String agreement = byteToArray(in.readBytes(2).array());
//				System.out.println(agreement);
//				// 得到数据
//				try {					
//				final String content = operation(serialNumber, head, length, l,agreement, in, ctx);							
//				// 得到校验位 checksum
//				String checksum = byteToArray(in.readBytes(2).array());
//				System.out.println(checksum);
//				// 得到结束符\r\n
//				final String end = byteToArray(in.readBytes(2).array());
//				System.out.println(end);
//				executorService.execute(new Runnable() {
//					public void run() {
//						try {
//							serviceHandler(ctx, serialNumber, agreement, content,end);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				});
//				} catch (ParseException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				System.out.println(sdf.format(new Date()));
////				SerialNumber serialNum = sndao.findBySerialNumber(serialNumber);
////				if (!map.containsKey(serialNumber)) {
////					map.put(serialNumber, ctx.channel());
////					System.out.println(ctx.channel().toString());
////					System.out.println("map.size():" + map.size());
////					System.out.println("666666");
////					System.out.println("设备:" + serialNumber + "上线了");
////					serialNum.setOnline("1");
////					sndao.updateSerialNumber(serialNum);
////				}	
//			}							
//		} else if ("@@".equals(head)) {
//			// 从包的字段得到包的长度
//			short l = in.readShort();
//			System.out.println(l);
//			// 得到产品id 序列号
//			final String serialNumber = byteToArray(in.readBytes(7).array());
//			System.out.println(serialNumber);
//			// 得到协议号
//			final String agreement = byteToArray(in.readBytes(2).array());
//			System.out.println(agreement);
//			// 得到数据
//			final String content = byteToArray(in.readBytes(l - 17).array());
//			System.out.println(content);
//			// 得到校验位 checksum
//			String checksum = byteToArray(in.readBytes(2).array());
//			System.out.println(checksum);
//			// 得到结束符\r\n
//			String end = byteToArray(in.readBytes(2).array());
//			System.out.println(end);
//			System.out.println(map.containsKey(serialNumber));			
//			if (map.containsKey(serialNumber)) {
//				executorService.execute(new Runnable() {					
//					public void run() {
//						System.out.println("123456123456");
//						String info = "";//"40400012" + serialNumber + agreement + content+ "0032" + "0d0a";
//						byte b[] = HexString2Bytes(info);
//						System.out.println("b.toString:" + Arrays.toString(b));
//						System.out.println("b.length:" + b.length);
////						map.get(serialNumber).writeAndFlush(b);
//						System.out.println("654321654321");						
//					}
//				});				
//			}
//		}
	}

	protected void serviceHandler(ChannelHandlerContext ctx,
			String serialNumber, String agreement, String content, String end)
			throws Exception {
		if ("0001".equals(agreement)) {
			// 心跳包
			String info = "40400012" + serialNumber + agreement + "01";
			// 返回消息给终端
			returnMessage(info, ctx, end);
		} else if ("9991".equals(agreement)) {
			java.util.Calendar cal = java.util.Calendar.getInstance();

			// 2、取得时间偏移量：

			int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

			// 3、取得夏令时差：

			int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

			// 、从本地时间里扣除这些差量，即可以取得UTC时间：

			cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
			String info = "40400023" + serialNumber + agreement
					+ Hex.encodeHexStr(sdf.format(cal.getTime()).getBytes());
			// 返回消息给终端
			returnMessage(info,ctx, end);
			getOnLine(ctx, serialNumber, content);
		} else if ("9982".equals(agreement)) {
			getRealTimePosition(serialNumber, content);
		} else if ("4130".equals(agreement)) {
			getListen(serialNumber, content);
		} else if ("7101".equals(agreement)) {			
			getPedoMeter(serialNumber, content);
		} else if("7200".equals(agreement)){
			getOffline(serialNumber);
		} else if("9955".equals(agreement)){
			getLocation(ctx, serialNumber, content, end);
		}
	}

	private void getLocation(ChannelHandlerContext ctx, String serialNumber,
			String content, String end) {
		String lon1 = null;
		String mlat1 = null;
		//SerialNumber serialNum=sndao.findBySerialNumber(serialNumber);
		String info = new String(Hex.decodeHex(content.toCharArray()));
		if (info.contains("|")) {
			System.out.println("info:" + info);
			String[] ml = info.split("\\|");
			String battery = null;
			if (ml.length == 8) {
				battery = ml[6];
			}
			if (!"".equals(ml[0])) {
				String lat = ml[0].split(",")[2].replace(".", "");
				String lon = ml[0].split(",")[4].replace(".", "");
				System.out.println(lat);
				System.out.println(lon);
				lon1 = String.valueOf(Float.valueOf(lon.substring(0, 3))
						+ (Float.valueOf(lon.substring(3, 5)) / 60)
						+ (Float.valueOf(lon.substring(5)) / 600000));
				mlat1 = String.valueOf(Float.valueOf(lat.substring(0, 2))
						+ (Float.valueOf(lat.substring(2, 4)) / 60)
						+ (Float.valueOf(lat.substring(4)) / 600000));
				System.out.println(lon1 + "," + mlat1);
				if (lon1 != null) {
					// gps转高德坐标存入location字段
					double[] gcj = BDTransUtil.wgs2gcj(
							Double.parseDouble(mlat1),
							Double.parseDouble(lon1));
					String location = String.valueOf(gcj[1]).substring(0,
							String.valueOf(gcj[1]).indexOf(".") + 6)
							+ ","
							+ String.valueOf(gcj[0])
									.substring(
											0,
											String.valueOf(gcj[0]).indexOf(
													".") + 7);
					System.out.println("高德经纬度:" + location);
					// gps转百度坐标
					double[] bd =BDTransUtil.wgs2bd(
							Double.parseDouble(mlat1),
							Double.parseDouble(lon1));
					String locationbd = String.valueOf(bd[1]).substring(0,
							String.valueOf(bd[1]).indexOf(".") + 6)
							+ ","
							+ String.valueOf(bd[0]).substring(0,
									String.valueOf(bd[0]).indexOf(".") + 7);
					System.out.println("百度经纬度:" + locationbd);
//					saveLocation(ctx, serialNumber, serialNum, end,
//							location, locationbd, battery);
				}
			} else {
				// 不是基站定位10秒不转换
//				if (!"1".equals(serialNum.getLbs())
//						&& "10".equals(serialNum.getSetGps())) 
				if (!"1".equals("1")
						&& "10".equals("")) 
				{

				} else {
					// 是基站定位都转换
					// |10|100|1cc,0,24a8,e3b,91,24a8,e44,91,24a8,e45,89,24a8,e3a,88,24a8,e43,85,24a8,e39,76,24a8,f2b,71|0000|00|078|100
					System.out.println(ml[3]);
					System.out.println("121212121212121212121");
					//String[] ss1 = ml[3].split(",");
					// String mcc = new BigInteger(ss1[0], 16).toString();//
					// 460
					// String mnc = new BigInteger(ss1[1], 16).toString();//
					// 00
//					String lac = new BigInteger(ss1[2], 16).toString();// 9384
//					String cid = new BigInteger(ss1[3], 16).toString();// 3643
//					System.out.println("lac:" + lac);
//					System.out.println("cid:" + cid);
					// 得到gps坐标
//					String lbsInfo = LbsUtil.getLbsInfo(lac, cid,
//							serialNumber);
//					System.out.println("lbsInfo" + lbsInfo);
					String lbs=getLbsInfo(ml[3]);
					
					String lbsInfo=null;
				    if(lbs!=null){
				    	lbs="x="+lbs+"&p=1&mt=0&ta=1";
				    	lbsInfo="";//MinigpsUtil.getLbsInfo(lbs,serialNumber);
				    	System.out.println("基站");
				    }
				    System.out.println("lbsInfo" + lbsInfo);
					String lbsInfo2 =getLbs(ml[3]);
					System.out.println("lbsInfo2" + lbsInfo2);					
					if (lbsInfo != null) {
						double[] bd = BDTransUtil.wgs2bd(
								Double.parseDouble(lbsInfo.split(",")[1]),
								Double.parseDouble(lbsInfo.split(",")[0]));
						String locationbd = String
								.valueOf(bd[1])
								.substring(
										0,
										String.valueOf(bd[1]).indexOf(".") + 6)
								+ ","
								+ String.valueOf(bd[0])
										.substring(
												0,
												String.valueOf(bd[0])
														.indexOf(".") + 7);
						System.out.println("百度经纬度:" + locationbd);

						double[] gcj = BDTransUtil.wgs2gcj(
								Double.parseDouble(lbsInfo.split(",")[1]),
								Double.parseDouble(lbsInfo.split(",")[0]));
						String location = String.valueOf(gcj[1]).substring(
								0, String.valueOf(gcj[1]).indexOf(".") + 6)
								+ ","
								+ String.valueOf(gcj[0])
										.substring(
												0,
												String.valueOf(gcj[0])
														.indexOf(".") + 7);
						System.out.println("高德经纬度：" + location);
//						saveLocation(ctx, serialNumber, serialNum, end,
//								location, locationbd, battery);
					}
                    if("30020000000001".equals(serialNumber)){
                    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    	String filePath="F:"+File.separator+"lszk"+File.separator+"30020000000001.txt";
						writerFile(filePath, ml[3], true);
						writerFile(filePath, lbsInfo, true);
						writerFile(filePath, lbsInfo2, true);
						writerFile(filePath, sdf.format(new Date()), true);
					}
					if (lbsInfo2 != null) {
						double[] bd =BDTransUtil.wgs2bd(
								Double.parseDouble(lbsInfo.split(",")[1]),
								Double.parseDouble(lbsInfo.split(",")[0]));
						String locationbd = String
								.valueOf(bd[1])
								.substring(
										0,
										String.valueOf(bd[1]).indexOf(".") + 6)
								+ ","
								+ String.valueOf(bd[0])
										.substring(
												0,
												String.valueOf(bd[0])
														.indexOf(".") + 7);
						System.out.println("百度经纬度2:" + locationbd);						
					}
				}
			}
		}
	}

	private String getLbsInfo(String lbs) {
		//1cc,0,24a8,e3b,91,24a8,e44,91,24a8,e45,89,24a8,e3a,88,24a8,e43,85,24a8,e39,76,24a8,f2b,71
		String msg[]=lbs.split(",");
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<msg.length;i++){
			if(i>1&&Integer.parseInt(msg[i], 16)==0){
				break;
			}			
			sb=sb.append(msg[i]+"-");					
		}				
		if(sb.toString()!=null){
			return sb.toString().substring(0, sb.toString().length()-1);
		}
		return null;
	}

	private String getLbs(String lbs) {
		//1cc,0,24a8,e3b,91,24a8,e44,91,24a8,e45,89,24a8,e3a,88,24a8,e43,85,24a8,e39,76,24a8,f2b,71
		String msg[]=lbs.split(",");
		int count=0;
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<msg.length;i++){
			i=i+2;
			if(Integer.parseInt(msg[i], 16)==0){
				break;
			}			
			String message="";//LbsUtil.getLbsInfo(msg[i], msg[i+1]);
			if(message!=null){
				sb=sb.append(message+":");
				count++;
			}
			if(count==3){
				break;
			}
		}
		String lbsInfo[]=sb.toString().split(":");
		if(lbsInfo.length==3){
			return getAvg(Double.parseDouble(lbsInfo[0].split(",")[0]), Double.parseDouble(lbsInfo[1].split(",")[0]), Double.parseDouble(lbsInfo[2].split(",")[0]),3)
					+","+getAvg(Double.parseDouble(lbsInfo[0].split(",")[1]), Double.parseDouble(lbsInfo[1].split(",")[1]), Double.parseDouble(lbsInfo[2].split(",")[1]),3);
		}else if(lbsInfo.length==2){
			return getAvg(Double.parseDouble(lbsInfo[0].split(",")[0]), Double.parseDouble(lbsInfo[1].split(",")[0]), 0.0d,2)
					+","+getAvg(Double.parseDouble(lbsInfo[0].split(",")[1]), Double.parseDouble(lbsInfo[1].split(",")[1]), 0.0d,2);
		}else{
			if(lbsInfo[0].length()>0){
				return getAvg(Double.parseDouble(lbsInfo[0].split(",")[0]), 0.0d, 0.0d,1)
						+","+getAvg(Double.parseDouble(lbsInfo[0].split(",")[1]), 0.0d, 0.0d,1);
			}
		}
		return null;				
	}

	private void getOffline(String serialNumber) {
		if (map.containsKey(serialNumber)) {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (serialNumber.equals(key)) {
					it.remove();
					map.remove(serialNumber);
					System.out.println("踢下线:" + key);
					System.out.println("map.size():" + map.size());
//					SerialNumber sn = sndao.findBySerialNumber(key);
//					sn.setOnline("0");
//					sndao.updateSerialNumber(sn);
				}
			}
		}
	}

	private void getPedoMeter(String serialNumber, String content) {
		System.out.println("7101");
		String info  = new String(Hex.decodeHex(content.toCharArray()));
		System.out.println(content);
		System.out.println(info);
//		PedoMeter pedoMeter = pedoMeterDao.findBySerialNumber(serialNumber);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		String distance = "";
//		if (pedoMeter == null) {
//			System.out.println("11111111");
//			String s[] = new String[7];
//			s[day - 1] = info.trim() + ","
//					+ (Math.round((Integer.parseInt(info.trim())) * (0.2)))
//					+ "," + sdf.format(calendar.getTime());
//			for (int i = 0; i < s.length; i++) {
//				distance += s[i] + "/";
//			}
//			System.out.println("distance:" + distance);
//			PedoMeter pm = new PedoMeter();
//			pm.setSerialNumber(serialNumber);
//			pm.setDistance(distance);
//			pedoMeterDao.addPedoMeter(pm);
//		} else if (pedoMeter != null) {
//			System.out.println("222222");
//			String s[] = pedoMeter.getDistance().split("/");
//			s[day - 1] = info.trim() + ","
//					+ (Math.round((Integer.parseInt(info.trim())) * (0.2)))
//					+ "," + sdf.format(calendar.getTime());
//			for (int i = 0; i < s.length; i++) {
//				distance += s[i] + "/";
//			}
//			System.out.println("distance:" + distance);
//			pedoMeter.setDistance(distance);
//			pedoMeterDao.updatePedoMeter(pedoMeter);
//		}
	}

	private void getListen(String serialNumber, String content) {
		System.out.println("4130");
//		SerialNumber serialNum = sndao.findBySerialNumber(serialNumber);
		String info = new String(Hex.decodeHex(content.toCharArray()));
		System.out.println(content);
		System.out.println(info);
//		serialNum.setListenStatus("2");
//		sndao.updateSerialNumber(serialNum);
	}

	private void getRealTimePosition(String serialNumber, String content)
			throws ParseException {
//		RtPosition rtp = rtpDao.findBySerialNumber(serialNumber);
		System.out.println("9982");
		Calendar cal = Calendar.getInstance();
		Date nowTime = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 经纬度内容
		String lon1 = null;
		String mlat1 = null;
		// |10|100|1cc,0,24a8,e3b,91,24a8,e44,91,24a8,e45,89,24a8,e3a,88,24a8,e43,85,24a8,e39,76,24a8,f2b,71|0000|00|078|100
		String info = new String(Hex.decodeHex(content.toCharArray()));
		if (info.contains("|")) {
			System.out.println("info:" + info);
			String[] ml = info.split("\\|");
			if (!"".equals(ml[0])) {
				String lat = ml[0].split(",")[2].replace(".", "");
				String lon = ml[0].split(",")[4].replace(".", "");
				System.out.println(lat);
				System.out.println(lon);
				lon1 = String.valueOf(Float.valueOf(lon.substring(0, 3))
						+ (Float.valueOf(lon.substring(3, 5)) / 60)
						+ (Float.valueOf(lon.substring(5)) / 600000));
				mlat1 = String.valueOf(Float.valueOf(lat.substring(0, 2))
						+ (Float.valueOf(lat.substring(2, 4)) / 60)
						+ (Float.valueOf(lat.substring(4)) / 600000));
				System.out.println(lon1 + "," + mlat1);
				if (lon1 != null) {
					// gps转百度坐标
					double[] bd =BDTransUtil.wgs2bd(Double.parseDouble(mlat1),Double.parseDouble(lon1));
					System.out.println("bd[0]:" + bd[0]);
					System.out.println("bd[1]:" + bd[1]);
					String lng = String.valueOf(bd[1]).substring(0,
							String.valueOf(bd[1]).indexOf(".") + 6);
					lat = String.valueOf(bd[0]).substring(0,
							String.valueOf(bd[0]).indexOf(".") + 7);
//					if (nowTime.getTime()
//							- sdf.parse(rtp.getCreateTime()).getTime() < 150000) {
//						System.out.println("456789");
//						rtp.setLng(lng);
//						rtp.setLat(lat);
//						rtp.setQuery("1");
//					} else {
//						System.out.println("123456789");
//						rtp.setLng(lng);
//						rtp.setLat(lat);
//						rtp.setQuery("0");
//					}
//					rtpDao.updateRtPosition(rtp);
				}
			} else {
				// 是基站定位都转换
				// |10|100|1cc,0,24a8,e3b,91,24a8,e44,91,24a8,e45,89,24a8,e3a,88,24a8,e43,85,24a8,e39,76,24a8,f2b,71|0000|00|078|100
				System.out.println(ml[3]);
				System.out.println("121212121212121212121");

				String lbs=getLbsInfo(ml[3]);
				String lbsInfo=null;
			    if(lbs!=null){
			    	lbs="x="+lbs+"&p=1&mt=0&ta=1";
//			    	lbsInfo=MinigpsUtil.getLbsInfo(lbs,serialNumber);
			    }
				//String lbsInfo =getLbs(ml[3]);
				System.out.println("lbsInfo" + lbsInfo);
				if (lbsInfo != null) {
					double[] bd = BDTransUtil.wgs2bd(
							Double.parseDouble(lbsInfo.split(",")[1]),
							Double.parseDouble(lbsInfo.split(",")[0]));
					System.out.println("gps转百度:"
							+ String.valueOf(bd[1]).substring(0,
									String.valueOf(bd[1]).indexOf(".") + 6)
							+ ","
							+ String.valueOf(bd[0]).substring(0,
									String.valueOf(bd[0]).indexOf(".") + 7));
					String lng = String.valueOf(bd[1]).substring(0,
							String.valueOf(bd[1]).indexOf(".") + 6);
					String lat = String.valueOf(bd[0]).substring(0,
							String.valueOf(bd[0]).indexOf(".") + 7);
					System.out.println(nowTime.getTime());
//					System.out
//							.println(sdf.parse(rtp.getCreateTime()).getTime());
//					System.out.println(nowTime.getTime()
//							- sdf.parse(rtp.getCreateTime()).getTime());
//					if (nowTime.getTime()
//							- sdf.parse(rtp.getCreateTime()).getTime() < 300000) {
//						System.out.println("456");
//						rtp.setLng(lng);
//						rtp.setLat(lat);
//						rtp.setQuery("1");
//					} else {
//						System.out.println("789");
//						rtp.setLng(lng);
//						rtp.setLat(lat);
//						rtp.setQuery("0");
//					}
//					rtpDao.updateRtPosition(rtp);
				}
			}
		}
	}

	private void getOnLine(ChannelHandlerContext ctx, String serialNumber,
			String content) {
		String info = new String(Hex.decodeHex(content.toCharArray()));
		String setGps = null;
		System.out.println(content);
		System.out.println(info);
		// gps_state=1,verno=F020.V1.004,interval=600,pwd=000000,sos_num1=13714366539,sos_num2=,sos_num3=,sos_num4=,,listen_num=,firewall=1,alm=0:0:0;0:0:0;0:0:0;0:0:0;0:0:0;0:0:0;0:0:0;0:0:0;,prof=-1;0:0:-267970868;0:0:0;0:0:0;0:0:0;
		String[] s = info.split(",");
		System.out.println(s[2]);
		if (s[2].contains("=")) {
			int index = s[2].indexOf("=");
			setGps = s[2].substring(index + 1);
			System.out.println(setGps);
		}
//		SerialNumber serialNum = sndao.findBySerialNumber(serialNumber);
//		if (setGps == null) {
//			// 进不去
//			serialNum.setLbs("0");
//			serialNum.setSetGps(setGps);
//		} else if ("180".equals(setGps)) {
//			serialNum.setLbs("1");
//			serialNum.setSetGps(serialNum.getSetGps());
//		} else {
//			serialNum.setLbs("0");
//			serialNum.setSetGps(setGps);
//		}
//		serialNum.setSerialNumber(serialNumber);
//		serialNum.setEf("3");
//		serialNum.setStatus("5");
//		serialNum.setGpsStatus(serialNum.getGpsStatus());
//		serialNum.setListenStatus("2");
//		sndao.updateSerialNumber(serialNum);
		if (map.containsKey(serialNumber)) {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (serialNumber.equals(key)) {
					it.remove();
					map.remove(serialNumber);
				}
			}
		} else {
			map.put(serialNumber, ctx.channel());
			System.out.println(ctx.channel().toString());
			System.out.println("map.size():" + map.size());
			System.out.println("99919991");
			System.out.println("设备:" + serialNumber + "上线了");
//			serialNum.setOnline("1");
//			sndao.updateSerialNumber(serialNum);
		}
	}
	
	private String operation(String serialNumber, String head, int length,
			short l, String agreement, ByteBuf in, ChannelHandlerContext ctx)
			throws ParseException {
		String content = null;
		if ("7101".equals(agreement) && length == l) {
			
			content = byteToArray(in.readBytes(l - 17).array());	
		} else if ("5100".equals(agreement) || "4106".equals(agreement)
				|| "9965".equals(agreement) || "3104".equals(agreement)
				|| "2001".equals(agreement) || "2002".equals(agreement)
				|| "2003".equals(agreement) || "9990".equals(agreement)
				|| "9992".equals(agreement) && length == l) {
			byteToArray(in.readBytes(1).array());			
			content = byteToArray(in.readBytes(l - 18).array());
			String info = new String(Hex.decodeHex(content.toCharArray()));
			System.out.println(content);
			System.out.println(info);			
		} else if ("9991".equals(agreement) && "$$".equals(head) && length == l) {

			content = byteToArray(in.readBytes(l - 17).array());
		} else if ("9982".equals(agreement) && "$$".equals(head) && length == l) {

			content = byteToArray(in.readBytes(l - 17).array());
		} else if ("4130".equals(agreement) && "$$".equals(head)) {

			byteToArray(in.readBytes(1).array());
			content = byteToArray(in.readBytes(l - 18).array());
		} else {	
			
			content = byteToArray(in.readBytes(l - 17).array());
			String info = new String(Hex.decodeHex(content.toCharArray()));
			System.out.println(content);
			System.out.println(info);			
		}
		return content;
	}	

//	private void saveLocation(ChannelHandlerContext ctx, String serialNumber,
//			SerialNumber serialNum, String end, String location,
//			String locationbd, String battery) throws DAOException,
//			ParseException {
//		//LocationInfo li = lidao.findBySeriaNumber(serialNumber);
//		Calendar c = Calendar.getInstance();
//		Date date = c.getTime();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String time = sdf.format(date);
//		// 设置一个时间点去触发修改存储的位置
//		System.out.println(time);
//		String mm = time.substring(14, 16);
//		System.out.println("mm:" + mm);
//		// 假如每个小时的30或者59分钟定到位就去查看是否超过7天了
//		if ("30".equals(mm) || "59".equals(mm)) {
//			System.out.println("ooo");
//			if (li.getText() != null) {
//				String[] ss = li.getText().split("/");
//				System.out.println(ss.length);
//				int a = 0;
//				// 806400000 10天 保存用户7天的数据
//				for (int k = 0; k < ss.length; k++) {
//					if (date.getTime()
//							- sdf.parse(ss[k].split(",")[2]).getTime() < 806400000) {
//						System.out.println("k:" + k);
//						if (k > 0) {
//							System.out.println("222222");
//							String s1 = ss[k - 1];
//							System.out.println("s1" + s1);
//							String s2 = li.getText().substring(
//									li.getText().indexOf(s1) + s1.length() + 1);
//							System.out.println("s2" + s2);
//							String s3 = li.getText().substring(0,
//									li.getText().indexOf(s1) + s1.length() + 1);
//							System.out.println("s3:" + s3);
//
//							li.setText(s2);
//						}
//						a++;
//						if (a == 1) {
//							break;
//						}
//					}
//				}
//			} else {
//				li.setText(locationbd + "," + time);
//			}
//			if (li.getLocation() != null) {
//				li.setLocation(li.getLocation() + "/" + location + "," + time);
//				String[] ss = li.getLocation().split("/");
//				System.out.println(ss.length);
//				int a = 0;
//				// 806400000 10天 保存用户7天的数据
//				for (int k = 0; k < ss.length; k++) {
//					if (date.getTime()
//							- sdf.parse(ss[k].split(",")[2]).getTime() < 806400000) {
//						System.out.println("k:" + k);
//						if (k > 0) {
//							System.out.println("222222");
//							String s1 = ss[k - 1];
//							System.out.println("s1" + s1);
//							String s2 = li.getLocation().substring(
//									li.getLocation().indexOf(s1) + s1.length()
//											+ 1);
//							System.out.println("s2" + s2);
//							String s3 = li.getLocation().substring(
//									0,
//									li.getLocation().indexOf(s1) + s1.length()
//											+ 1);
//							System.out.println("s3:" + s3);
//							li.setLocation(s2);
//						}
//						a++;
//						if (a == 1) {
//							break;
//						}
//					}
//				}
//			} else {
//				li.setLocation(location + "," + time);
//			}
//		} else {
//			if (li.getText() != null) {
//				li.setText(li.getText() + "/" + locationbd + "," + time);
//			} else {
//				li.setText(locationbd + "," + time);
//			}
//			if (li.getLocation() != null) {
//				li.setLocation(li.getLocation() + "/" + location + "," + time);
//			} else {
//				li.setLocation(location + "," + time);
//			}
//		}
//		li.setLng(locationbd.split(",")[0]);
//		li.setLat(locationbd.split(",")[1]);
//		if (battery != null) {
//			li.setBattery(battery);
//			System.out.println("battery:" + battery);
//		}
//		//lidao.update(li);
//		// 短信模块
//		controlSMS(ctx, serialNumber, serialNum, locationbd, end);
//
//	}
//
//	private void controlSMS(ChannelHandlerContext ctx, String serialNumber,
//			SerialNumber serialNum, String locationbd, String end)
//			throws DAOException {
//		List<ElectFence> list = electFenceDao.findBySerialNumber(serialNumber);
//		if (list.isEmpty()) {
//			System.out.println("没有设置电子围栏");
//		} else {
//			String mlon = locationbd.split(",")[0];
//			String mlat = locationbd.split(",")[1];
//			String info = "40400012" + serialNumber + "9993";
//			if (list.size() == 1) {
//				ElectFence ef = list.get(0);
//				System.out.println("areaNum" + ef.getAreaNum());
//				String locationInfo = ef.getLocationbd();
//				if (1 == ef.getAreaNum()) {
//					// 家
//					System.out.println("locationInfo:" + locationInfo);
//					if ("3".equals(serialNum.getEf())) {
//						if (FNUtil.GetShortDistance(
//								Double.parseDouble(locationInfo.split(",")[0]),
//								Double.parseDouble(locationInfo.split(",")[1]),
//								Double.parseDouble(mlon),
//								Double.parseDouble(mlat)) < ef.getScope()) {
//							// 在家
//							serialNum.setEf("0");
//							serialNum.setSerialNumber(serialNumber);
//							serialNum.setStatus(serialNum.getStatus());
//							sndao.updateSerialNumber(serialNum);
//						} else {
//							// 在危险区域
//							serialNum.setEf("2");
//							serialNum.setSerialNumber(serialNumber);
//							serialNum.setStatus(serialNum.getStatus());
//							sndao.updateSerialNumber(serialNum);
//						}
//					} else if ("0".equals(serialNum.getEf())) {
//						// 已经在家
//						// 0 离开进入模式 1离开模式 2进入模式
//						if ("0".equals(ef.getModel())
//								|| "1".equals(ef.getModel())) {
//							if (FNUtil
//									.GetShortDistance(Double
//											.parseDouble(locationInfo
//													.split(",")[0]), Double
//											.parseDouble(locationInfo
//													.split(",")[1]), Double
//											.parseDouble(mlon), Double
//											.parseDouble(mlat)) < ef.getScope()) {
//								// 进入家了
//
//							} else {
//								// 离开家了
//								serialNum.setEf("2");
//								serialNum.setSerialNumber(serialNumber);
//								serialNum.setStatus(serialNum.getStatus());
//								sndao.updateSerialNumber(serialNum);
//								info = info + "01";
//								// 返回消息给终端
//								returnMessage(info, ctx, end);
//							}
//						} else if ("2".equals(ef.getModel())) {
//							if (FNUtil
//									.GetShortDistance(Double
//											.parseDouble(locationInfo
//													.split(",")[0]), Double
//											.parseDouble(locationInfo
//													.split(",")[1]), Double
//											.parseDouble(mlon), Double
//											.parseDouble(mlat)) < ef.getScope()) {
//								// 进入家了
//
//							} else {
//								// 离开家了
//								serialNum.setEf("2");
//								serialNum.setSerialNumber(serialNumber);
//								serialNum.setStatus(serialNum.getStatus());
//								sndao.updateSerialNumber(serialNum);
//							}
//						}
//					} else if ("2".equals(serialNum.getEf())) {
//						// 已经在危险区域
//						if ("0".equals(ef.getModel())
//								|| "2".equals(ef.getModel())) {
//							if (FNUtil
//									.GetShortDistance(Double
//											.parseDouble(locationInfo
//													.split(",")[0]), Double
//											.parseDouble(locationInfo
//													.split(",")[1]), Double
//											.parseDouble(mlon), Double
//											.parseDouble(mlat)) < ef.getScope()) {
//								// 进入家了
//								serialNum.setEf("0");
//								serialNum.setSerialNumber(serialNumber);
//								serialNum.setStatus(serialNum.getStatus());
//								sndao.updateSerialNumber(serialNum);
//								info = info + "04";
//								// 返回消息给终端
//								returnMessage(info, ctx, end);
//							}
//						} else if ("1".equals(ef.getModel())) {
//							if (FNUtil
//									.GetShortDistance(Double
//											.parseDouble(locationInfo
//													.split(",")[0]), Double
//											.parseDouble(locationInfo
//													.split(",")[1]), Double
//											.parseDouble(mlon), Double
//											.parseDouble(mlat)) < ef.getScope()) {
//								// 进入家了
//								serialNum.setEf("0");
//								serialNum.setSerialNumber(serialNumber);
//								serialNum.setStatus(serialNum.getStatus());
//								sndao.updateSerialNumber(serialNum);
//							}
//						}
//					}
//				} else if (2 == ef.getAreaNum()) {
//					// 学校
//					if ("3".equals(serialNum.getEf())) {
//						if (FNUtil.GetShortDistance(
//								Double.parseDouble(locationInfo.split(",")[0]),
//								Double.parseDouble(locationInfo.split(",")[1]),
//								Double.parseDouble(mlon),
//								Double.parseDouble(mlat)) < ef.getScope()) {
//							// 发送在学校的指令给终端
//							serialNum.setEf("1");
//							serialNum.setSerialNumber(serialNumber);
//							serialNum.setStatus(serialNum.getStatus());
//							sndao.updateSerialNumber(serialNum);
//						} else {
//							// 发送在危险区域的指令给终端
//							serialNum.setEf("2");
//							serialNum.setSerialNumber(serialNumber);
//							serialNum.setStatus(serialNum.getStatus());
//							sndao.updateSerialNumber(serialNum);
//						}
//					} else if ("1".equals(serialNum.getEf())) {
//						// 在学校
//						if ("0".equals(ef.getModel())
//								|| "1".equals(ef.getModel())) {
//							if (FNUtil
//									.GetShortDistance(Double
//											.parseDouble(locationInfo
//													.split(",")[0]), Double
//											.parseDouble(locationInfo
//													.split(",")[1]), Double
//											.parseDouble(mlon), Double
//											.parseDouble(mlat)) < ef.getScope()) {
//								// 进入学校
//
//							} else {
//								// 离开学校
//								serialNum.setEf("2");
//								serialNum.setSerialNumber(serialNumber);
//								serialNum.setStatus(serialNum.getStatus());
//								sndao.updateSerialNumber(serialNum);
//								info = info + "03";
//								// 返回消息给终端
//								returnMessage(info, ctx, end);
//							}
//						} else if ("2".equals(ef.getModel())) {
//							if (FNUtil
//									.GetShortDistance(Double
//											.parseDouble(locationInfo
//													.split(",")[0]), Double
//											.parseDouble(locationInfo
//													.split(",")[1]), Double
//											.parseDouble(mlon), Double
//											.parseDouble(mlat)) < ef.getScope()) {
//								// 进入学校
//							} else {
//								// 离开学校
//								serialNum.setEf("2");
//								serialNum.setSerialNumber(serialNumber);
//								serialNum.setStatus(serialNum.getStatus());
//								sndao.updateSerialNumber(serialNum);
//							}
//						}
//					} else if ("2".equals(serialNum.getEf())) {
//						// 在危险区域
//						if ("0".equals(ef.getModel())
//								|| "2".equals(ef.getModel())) {
//							if (FNUtil
//									.GetShortDistance(Double
//											.parseDouble(locationInfo
//													.split(",")[0]), Double
//											.parseDouble(locationInfo
//													.split(",")[1]), Double
//											.parseDouble(mlon), Double
//											.parseDouble(mlat)) < ef.getScope()) {
//								// 进入学校
//								serialNum.setEf("1");
//								serialNum.setSerialNumber(serialNumber);
//								serialNum.setStatus(serialNum.getStatus());
//								sndao.updateSerialNumber(serialNum);
//								info = info + "02";
//								// 返回消息给终端
//								returnMessage(info, ctx, end);
//							} else {
//								// 离开学校
//							}
//						} else if ("1".equals(ef.getModel())) {
//							if (FNUtil
//									.GetShortDistance(Double
//											.parseDouble(locationInfo
//													.split(",")[0]), Double
//											.parseDouble(locationInfo
//													.split(",")[1]), Double
//											.parseDouble(mlon), Double
//											.parseDouble(mlat)) < ef.getScope()) {
//								// 进入学校
//								serialNum.setEf("1");
//								serialNum.setSerialNumber(serialNumber);
//								serialNum.setStatus(serialNum.getStatus());
//								sndao.updateSerialNumber(serialNum);
//							}
//						}
//					}
//				}
//			} else if (list.size() == 2) {
//				List<ElectFence> lists = new ArrayList<ElectFence>();
//				// 家
//				ElectFence ef1 = list.get(0);
//				// 学校
//				ElectFence ef2 = list.get(1);
//				if (ef1.getAreaNum() == 1) {
//					lists.add(ef1);
//					lists.add(ef2);
//				} else {
//					lists.add(ef2);
//					lists.add(ef1);
//				}
//				if ("3".equals(serialNum.getEf())) {
//					System.out.println("okokokokok");
//					if (FNUtil.GetShortDistance(
//							Double.parseDouble(lists.get(0).getLocationbd()
//									.split(",")[0]),
//							Double.parseDouble(lists.get(0).getLocationbd()
//									.split(",")[1]), Double.parseDouble(mlon),
//							Double.parseDouble(mlat)) < lists.get(0).getScope()) {
//						// 发送在家的指令给终端
//						serialNum.setEf("0");
//						serialNum.setSerialNumber(serialNumber);
//						serialNum.setStatus(serialNum.getStatus());
//						sndao.updateSerialNumber(serialNum);
//					} else if (FNUtil.GetShortDistance(
//							Double.parseDouble(lists.get(1).getLocationbd()
//									.split(",")[0]),
//							Double.parseDouble(lists.get(1).getLocationbd()
//									.split(",")[1]), Double.parseDouble(mlon),
//							Double.parseDouble(mlat)) < lists.get(1).getScope()) {
//						// 发送在学校的指令给终端
//						serialNum.setEf("1");
//						serialNum.setSerialNumber(serialNumber);
//						serialNum.setStatus(serialNum.getStatus());
//						sndao.updateSerialNumber(serialNum);
//					} else {
//						// 发送在危险区域的指令给终端
//						serialNum.setEf("2");
//						serialNum.setSerialNumber(serialNumber);
//						serialNum.setStatus(serialNum.getStatus());
//						sndao.updateSerialNumber(serialNum);
//					}
//				} else if ("0".equals(serialNum.getEf())) {
//					// 在家
//					if (FNUtil.GetShortDistance(
//							Double.parseDouble(lists.get(0).getLocationbd()
//									.split(",")[0]),
//							Double.parseDouble(lists.get(0).getLocationbd()
//									.split(",")[1]), Double.parseDouble(mlon),
//							Double.parseDouble(mlat)) < lists.get(0).getScope()) {
//						// 发送在家的指令给终端
//					} else if (FNUtil.GetShortDistance(
//							Double.parseDouble(lists.get(1).getLocationbd()
//									.split(",")[0]),
//							Double.parseDouble(lists.get(1).getLocationbd()
//									.split(",")[1]), Double.parseDouble(mlon),
//							Double.parseDouble(mlat)) < lists.get(1).getScope()) {
//						// 发送在学校的指令给终端
//						serialNum.setEf("1");
//						serialNum.setSerialNumber(serialNumber);
//						serialNum.setStatus(serialNum.getStatus());
//						sndao.updateSerialNumber(serialNum);
//					} else {
//						// 发送在危险区域的指令给终端
//						serialNum.setEf("2");
//						serialNum.setSerialNumber(serialNumber);
//						serialNum.setStatus(serialNum.getStatus());
//						sndao.updateSerialNumber(serialNum);
//						if ("0".equals(lists.get(0).getModel())) {
//							// 离开进入模式
//							info = info + "01";
//							returnMessage(info, ctx, end);
//						} else if ("1".equals(lists.get(0).getModel())) {
//							// 离开模式
//							info = info + "01";
//							returnMessage(info, ctx, end);
//						} else if ("2".equals(lists.get(0).getModel())) {
//							// 进入模式
//						}
//					}
//				} else if ("1".equals(serialNum.getEf())) {
//					// 在学校
//					if (FNUtil.GetShortDistance(
//							Double.parseDouble(lists.get(0).getLocationbd()
//									.split(",")[0]),
//							Double.parseDouble(lists.get(0).getLocationbd()
//									.split(",")[1]), Double.parseDouble(mlon),
//							Double.parseDouble(mlat)) < lists.get(0).getScope()) {
//						// 发送在家的指令给终端
//						serialNum.setEf("0");
//						serialNum.setSerialNumber(serialNumber);
//						serialNum.setStatus(serialNum.getStatus());
//						sndao.updateSerialNumber(serialNum);
//					} else if (FNUtil.GetShortDistance(
//							Double.parseDouble(lists.get(1).getLocationbd()
//									.split(",")[0]),
//							Double.parseDouble(lists.get(1).getLocationbd()
//									.split(",")[1]), Double.parseDouble(mlon),
//							Double.parseDouble(mlat)) < lists.get(1).getScope()) {
//						// 发送在学校的指令给终端
//
//					} else {
//						// 发送在危险区域的指令给终端
//						serialNum.setEf("2");
//						serialNum.setSerialNumber(serialNumber);
//						serialNum.setStatus(serialNum.getStatus());
//						sndao.updateSerialNumber(serialNum);
//						if ("0".equals(lists.get(1).getModel())) {
//							// 离开进入模式
//							info = info + "03";
//							returnMessage(info, ctx, end);
//						} else if ("1".equals(lists.get(1).getModel())) {
//							// 离开模式
//							info = info + "03";
//							returnMessage(info, ctx, end);
//						} else if ("2".equals(lists.get(1).getModel())) {
//							// 进入模式
//						}
//					}
//				} else if ("2".equals(serialNum.getEf())) {
//					// 在危险区域
//					if (FNUtil.GetShortDistance(
//							Double.parseDouble(lists.get(0).getLocationbd()
//									.split(",")[0]),
//							Double.parseDouble(lists.get(0).getLocationbd()
//									.split(",")[1]), Double.parseDouble(mlon),
//							Double.parseDouble(mlat)) < lists.get(0).getScope()) {
//						// 发送在家的指令给终端
//						serialNum.setEf("0");
//						serialNum.setSerialNumber(serialNumber);
//						serialNum.setStatus(serialNum.getStatus());
//						sndao.updateSerialNumber(serialNum);
//						if ("0".equals(lists.get(0).getModel())) {
//							// 离开进入模式
//							info = info + "04";
//							returnMessage(info, ctx, end);
//						} else if ("1".equals(lists.get(0).getModel())) {
//							// 离开模式
//
//						} else if ("2".equals(lists.get(0).getModel())) {
//							// 进入模式
//							info = info + "04";
//							returnMessage(info, ctx, end);
//						}
//					} else if (FNUtil.GetShortDistance(
//							Double.parseDouble(lists.get(1).getLocationbd()
//									.split(",")[0]),
//							Double.parseDouble(lists.get(1).getLocationbd()
//									.split(",")[1]), Double.parseDouble(mlon),
//							Double.parseDouble(mlat)) < lists.get(1).getScope()) {
//						// 发送在学校的指令给终端
//						serialNum.setEf("1");
//						serialNum.setSerialNumber(serialNumber);
//						serialNum.setStatus(serialNum.getStatus());
////						sndao.updateSerialNumber(serialNum);
//						if ("0".equals(lists.get(1).getModel())) {
//							// 离开进入模式
//							info = info + "02";
//							returnMessage(info, ctx, end);
//						} else if ("1".equals(lists.get(1).getModel())) {
//							// 离开模式
//						} else if ("2".equals(lists.get(1).getModel())) {
//							// 进入模式
//							info = info + "02";
//							returnMessage(info, ctx, end);
//						}
//					} else {
//						// 发送在危险区域的指令给终端
//					}
//				}
//			}
//		}
//
//	}

	private void returnMessage(String info, ChannelHandlerContext ctx,
			String end) {
		byte[] b2 = HexString2Bytes(info);
		String checksum = Integer.toHexString(CRC_XModem(b2)).toUpperCase();
		System.out.println("checksum1:" + checksum);
		if (checksum.length() == 4) {

		} else if (checksum.length() == 3) {
			checksum = "0" + checksum;
		} else if (checksum.length() == 2) {
			checksum = "00" + checksum;
		} else if (checksum.length() == 1) {
			checksum = "000" + checksum;
		} else {
			checksum = checksum.substring(0, 4);
		}
		System.out.println("checksum2:" + checksum);
		byte b[] = HexString2Bytes(info + checksum + end);
		System.out.println("b.toString:" + Arrays.toString(b));
		System.out.println("b.length:" + b.length);
		System.out.println("send_message:" + Hex.byteToArray(b));
		/*
		 * 对于消息发送，通常需要用户自己构造ByteBuf并编码， 例如通过如下工具类Unpooled创建消息发送缓冲区
		 * 使用非堆内存分配器创建的直接内存缓冲区
		 */
		// ByteBuf buf=Unpooled.buffer(b.length);
		// buf.writeBytes(b);
		// ctx.writeAndFlush(buf);
		/*
		 * 使用内存池分配器创建直接内存缓冲区
		 */
		// ByteBuf buf=PooledByteBufAllocator.DEFAULT.directBuffer(b.length);
		// buf.writeBytes(b);
		// buf.release();
		// ctx.writeAndFlush(buf);

		ctx.writeAndFlush(b);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		// ctx.close();
	}

	public static String byteToArray(byte[] data) {
		String result = "";
		for (int i = 0; i < data.length; i++) {
			result += Integer.toHexString((data[i] & 0xFF) | 0x100)
					.toUpperCase().substring(1, 3);
		}
		return result;
	}

	// 从十六进制字符串到字节数组转换
	private static byte[] HexString2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}

	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	public static int CRC_XModem(byte[] bytes) {
		int crc = 0x00; // initial value
		int polynomial = 0x1021;
		for (int index = 0; index < bytes.length; index++) {
			byte b = bytes[index];
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit)
					crc ^= polynomial;
			}
		}
		crc &= 0xffff;
		return crc;
	}
	public static double getAvg(double d1,double d2,double d3,int n){		
		return (d1+d2+d3)/n;	
	}
	private void writerFile(String filePath, String message, boolean flag) {
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				System.out.println("file1");
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			OutputStreamWriter osw = null;
			BufferedWriter bw = null;
			if (filePath != null && !"".equals(filePath)) {
				System.out.println("file2");
				osw = new OutputStreamWriter(new FileOutputStream(file, flag));
				bw = new BufferedWriter(osw);
			}
			if (message != null && !"".equals(message)) {
				bw.write(message);
				System.out.println("file3");
				bw.newLine();
				bw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
