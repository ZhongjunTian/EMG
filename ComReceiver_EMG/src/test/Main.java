package test;

public class Main {
	public static void main(String[] args)
    {
		 Window w = new Window();
		 while(true){
			 w.label.setText("Tyepe in COM port number\n");
			 while(w.readStart == false){
				 try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			 }
			 SerialBean.baudRate = 460800;
			 SerialBean sb = new SerialBean(w.comPort); //set COM port 8, you can change it
			 Reader reader = new Reader(100); //101 is the length of data that will be shown.
			 w.dataList = reader.dataList;
			 w.jp.repaint();
			 w.label.setText("Wait 30s for initializing\n");
			 sb.Initialize();
			 sb.WritePort("A");
			 w.label.setText("Complete\n");

	    	 while(w.readStart){
				 reader.readOnePackage(sb);
				 w.jp.repaint();
	    	 }
	     	 sb.ClosePort();
	    	 
		 }
    	 
    }
}
