package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Window extends JFrame{
	public int comPort;
	public LinkedList<Data> dataList = new LinkedList<Data>();
	public boolean readStart = false;
	public boolean readStop = true;
	String msg = "";
	TextField tField = new TextField(); 
	JButton button = new JButton("Start");
	JButton button2 = new JButton("Stop");
	JLabel label;
	JLabel labelX,labelY,labelZ,labelW;
	JP jp = new JP();
	public Window() { 
        this.setLayout(null);
        jp.setDoubleBuffered(true); //double buffer!
        jp.setBounds(50, 50, 800, 700);
        jp.setVisible(true);
        this.add(jp);
        tField.setBounds(50, 0, 150, 30);
        button.setBounds(200, 0, 100, 30);
        button.addActionListener(new ActionListener()
		{
		      public void actionPerformed(ActionEvent e)
		      {     
		    	  comPort = Integer.parseInt(tField.getText());
		          readStart = true;
		          readStop = false;
		      }
		});
        
        button2.setBounds(300, 0, 100, 30);
        button2.addActionListener(new ActionListener()
		{
		      public void actionPerformed(ActionEvent e)
		      {     
		          readStart = false;
		          readStop = false;
		      }
		});

        label = new JLabel();
        label.setBounds(410, 0, 200, 30);
        add(label);
        labelX = new JLabel("C1 axis");
        labelY = new JLabel("C2 axis");
        labelZ = new JLabel("C3 axis");
        labelW = new JLabel("C4 axis");
        add(labelX);
        add(labelY);
        add(labelZ);
        add(labelW);

        final int ybias = -45;
        final int xbias = 15;
        labelX.setBounds(xbias, 150 + ybias, 50, 30);
        labelY.setBounds(xbias, 300 + ybias, 50, 30);
        labelZ.setBounds(xbias, 450 + ybias, 50, 30);
        labelW.setBounds(xbias, 600 + ybias, 50, 30);
        
        add(tField);
        add(button, "North");
        add(button2, "North");
        this.setBackground(new Color(180,255,255));
        this.setTitle("www.essp.utdallas.edu");
        this.setSize(900, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
    }
	protected void processWindowEvent(WindowEvent e) { 
	    super.processWindowEvent(e); 
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) { 
	    	System.out.print("exit");
	    	System.exit(0); 
	    } 
	} 
	  // private Image offScreenImage;  
	   //private Graphics gImage;   

	class JP extends JPanel{

        final int xOffset = 10;
         final int yOffset = -80;
         final int hight = 64;
         
	    public void paint(Graphics g) {
	    	
	        super.paint(g); 

	        paintBackground(g,0);
	        paintBackground(g,150);
	        paintBackground(g,300);
	        paintBackground(g,450);
	         
	         for(int i = 0; i < dataList.size()-2; i++){
	        	 Data p = dataList.get(i);
	        	 Data c = dataList.get(i+1);
	        	 if(p!=null && c!=null){
	        	 paintAxis(g, p.x, c.x, i, 0,Color.RED);
	        	 paintAxis(g, p.y, c.y, i, 150,Color.GREEN);
	        	 paintAxis(g, p.z, c.z, i, 300,Color.BLUE);
	        	 paintAxis(g, p.w, c.w, i, 450,Color.GRAY);
	        	 }
	         }
	    }
	    
	    public void paintBackground(Graphics g, int deltaH){
	         
	         g.setColor(Color.WHITE);
	         g.fillRect(xOffset, 150-hight +yOffset + deltaH, 800, hight*2 );
	         g.setColor(Color.BLACK);
	         g.drawLine(xOffset, 150 +yOffset + deltaH, xOffset + 800, 150 +yOffset +deltaH);
	    }
	    public void paintAxis(Graphics g,int x1, int x2, int i , int deltaH, Color color){
	    	
	    	int Xy1 = (int)(x1/32768.0 *hight *(-1) + 150) +yOffset + deltaH;
         	int Xy2 = (int)(x2/32768.0 *hight *(-1) + 150 +yOffset) + deltaH;
             g.setColor(color);
             g.drawLine(8*i+xOffset, Xy1, 8*(i+1)+xOffset, Xy2);
	    }
	    
	    
	    
	    
	}
	
	
}
     

