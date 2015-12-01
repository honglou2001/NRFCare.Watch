package nrfCare.SerialPort;

public class DataSubjectConcrete extends DataSubject{
    
//    private byte[] bData;
//    
// 
//
//    public byte[] getbData() {
//		return bData;
//	}
//
//
//
//	public void setbData(byte[] bData) {
//		this.bData = bData;
//	}



	public void dealWithData(int type,byte[] receivedData){
//        this.bData = receivedData;
//        System.out.println("主题状态为：");
        //状态发生改变，通知各个观察者
        this.nodifyObservers(type,receivedData);
    }

}
