package nrfCare.Component;


public class PigInfo {
	private String pigid = "";
	//产地
	private String madein = "";
	//追溯信息url
	private String infourl = "";
	//屠宰时间
	private String slatime = "";

	public String getPigid() {
		return pigid;
	}
	public void setPigid(String pigid) {
		this.pigid = pigid;
	}

	 public String getMadein() {
		return madein;
	}
	public void setMadein(String madein) {
		this.madein = madein;
	}
	
	public String getInfourl() {
		return infourl;
	}
	public void setInfourl(String infourl) {
		this.infourl = infourl;
	}
	
	
	public String getSlatime() {
		return slatime;
	}
	public void setSlatime(String slatime) {
		this.slatime = slatime;
	}
	
//	private String feedbatch = "";
//	private String strain ="";	
//	private String videourl = "";

	 
}
