 /*
      *   SerialBuffer
SerialBuffer是本类库中所定义的串口缓冲区，它定义了往该缓冲区中写入数据和从该缓冲区中读取数据所需要的函数。public synchronized String GetMsg(int Length) 
本函数从串口(缓冲区)中读取指定长度的一个字符串。参数Length指定所返回字符串的长度。 
public synchronized void PutChar(int c) 
本函数望串口缓冲区中写入一个字符，参数c 是需要写入的字符。 
在往缓冲区写入数据或者是从缓冲区读取数据的时候，必须保证数据的同步，因此GetMsg和PutChar函数均被声明为synchronized并在具体实现中采取措施实现的数据的同步。

      */
package test;

public class SerialBuffer {
	 public String Content = "";
     private String CurrentMsg, TempContent;
     private boolean available = false;
     private int LengthNeeded = 1;
         /**
          *
          * This function returns a string with a certain length from the incomin
          * messages.
          *
          * @param Length The length of the string to be returned.
          *
          */
    
     public synchronized String GetMsg(int Length)
     {
         LengthNeeded = Length;
         notifyAll();
 
         if (LengthNeeded > Content.length())
         {
             available = false;
             while (available == false)
             {
                 try
                 {
                     wait();
                 } catch (InterruptedException e) { }
             }
         }
        
         CurrentMsg = Content.substring(0, LengthNeeded);
         TempContent = Content.substring(LengthNeeded);
         Content = TempContent;
         LengthNeeded = 1;
         notifyAll();
         return CurrentMsg;
     }
         /**
          *
          * This function stores a character captured from the serial port to the
          * buffer area.
          *
          * @param t The char value of the character to be stored.
          *
          */
     public synchronized void PutChar(int c)
     {
         Character d = new Character((char) c);
         Content = Content.concat(d.toString());
         if (LengthNeeded < Content.length())
         {
             available = true;
         }
         notifyAll();
     }
}
