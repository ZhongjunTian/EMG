package test;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class Reader {
	char cData = 0;
	char pData = 0;
	char dataCount = 0;
	final char DLE = 0x10;
	final char SOH = 0x01;
	final char  EOT = 0x04;
	char packet[] = new char[300];
	int packetNumber=0;
	LinkedList<Data> dataList = new LinkedList<Data>();
	int dataMaxLen = 100;
	BufferedWriter out;
	DecimalFormat df = new DecimalFormat("0.0000000000000");
	
	public Reader(int length){
		super();
		dataMaxLen = length;
		for(int i=0;i<length;i++){
			Data dataTemp = new Data();
			dataList.add(dataTemp);
		}
		
		try {
			Date now = new Date(); 
			Calendar cal = Calendar.getInstance(); 		
			DateFormat df = DateFormat.getDateInstance();

			
			String folder = df.format(now);
			String fileName ="" + cal.get(Calendar.HOUR_OF_DAY) +"-"+cal.get(Calendar.MINUTE)+".txt";
			
			File dirFile  =   new  File(folder);
			dirFile.mkdirs();
			out = new BufferedWriter(new FileWriter(folder+"\\"+ fileName));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	int counter = 0;
	double[] sum = {0,0,0,0};
	double[] avg = {0,0,0,0};
	public void readOnePackage(SerialBean sb) {
		
		cData = 0;
		pData = 0;
		dataCount = 0;
		while (((pData != DLE) || (cData != SOH))){	// find the start of packet
			pData = cData;
			cData = (char) sb.ReadPort(1).toCharArray()[0];
			if ((pData == DLE) && (cData == DLE))
			{
			pData = cData;
			cData = (char) sb.ReadPort(1).toCharArray()[0];
			}
		}
		//data<<"srart detected\r\n";
		while (((pData != DLE) || (cData != EOT)) ){	
			pData = cData;
			cData = (char) sb.ReadPort(1).toCharArray()[0];
		
			if (cData != DLE)
				packet [dataCount++] = cData;
			else
			{
				pData = cData;
				cData = (char) sb.ReadPort(1).toCharArray()[0];
				if (cData == DLE)
				{
					packet [dataCount++] = cData;
				}
			}
		}
		if(dataCount == 11+1){ // 12 bytes data for every package
			int amplify = 10;
			int i=0;
			double c1 = (dataParser(packet[i+0],packet[i+1],packet[i+2]) )* amplify;	
			i+=3;
			double c2 = (dataParser(packet[i+0],packet[i+1],packet[i+2])) * amplify;	
			i+=3;	
			double c3 = (dataParser(packet[i+0],packet[i+1],packet[i+2])) * amplify;
			i+=3;
			double c4 = (dataParser(packet[i+0],packet[i+1],packet[i+2])) * amplify;
			i+=3;
			counter++;
			String str = df.format(c1)+"\t" +df.format(c2)+"\t" +df.format(c3)+"\t" +df.format(c4)+"\n";
			try {
				out.write(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(counter % 64 == 0){
				try {
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.print(str);
			}
			
			if(counter%128!=0){
				sum[0]+=c1;
				sum[1]+=c2;
				sum[2]+=c3;
				sum[3]+=c4;
			}else{
				avg[0] = sum[0]/128;
				avg[1] = sum[1]/128;
				avg[2] = sum[2]/128;
				avg[3] = sum[3]/128;
				sum[0]=0;
				sum[1]=0;
				sum[2]=0;
				sum[3]=0;
			}
			
			if(counter%512>512-124){
				pushData(c1 - avg[0]/1024, 
						c2- avg[1]/1024, 
						c3- avg[2]/1024, 
						c4 - avg[3]/1024,
						50);//amplifier
			}
				
		}
		
	}
	public void setData(double c1, double c2, double c3,double c4, int i, int amplifier){

		int x = (int)(c1*amplifier);
		int y = (int)(c2*amplifier);
		int z = (int)(c3*amplifier);
		int w = (int)(c4*amplifier);
		dataList.set(i, new Data(x,y,z,w));
	}

	public void pushData(double c1, double c2, double c3,double c4, int amplifier){

		int x = (int)(c1*amplifier);
		int y = (int)(c2*amplifier);
		int z = (int)(c3*amplifier);
		int w = (int)(c4*amplifier);
		dataList.add(new Data(x,y,z,w));
		dataList.remove(0);
	}

	public double dataParser(char packet2, char packet3, char packet4){
		
			long num;
			double temp_voltage;

			num = packet2*65536L + packet3*256L + packet4 ;
					
			if (num < 8388607){ 
				temp_voltage = (num * 3.3 * 1000) / 8388607.0;
			} else{
	            num = (16777215 - num) + 1;
	            temp_voltage = num;
	            temp_voltage = (num * 3.3 * 1000) / 8388607.0;
	
	            temp_voltage *= -1;
			}
			return temp_voltage;
	}
}
