/*
 * ================================================
ReadSerial
ReadSerial是一个进程，它不断的从指定的串口读取数据并将其存放到缓冲区中。public ReadSerial(SerialBuffer SB, InputStreamPort) 
本函数构造一个ReadSerial进程，参数SB指定存放传入数据的缓冲区，参数Port指定从串口所接收的数据流。 
public void run() 
ReadSerial进程的主函数，它不断的从指定的串口读取数据并将其存放到缓冲区中。
 */
	package test;
    import java.io.*;
    /**
     *
     * This class reads message from the specific serial port and save
     * the message to the serial buffer.
     *
     */
    public class ReadSerial extends Thread
    {
        private SerialBuffer ComBuffer;
        private InputStream ComPort;
            /**
             *
             * Constructor
             *
             * @param SB The buffer to save the incoming messages.
             * @param Port The InputStream from the specific serial port.
             *
             */
        public ReadSerial(SerialBuffer SB, InputStream Port)
        {
            ComBuffer = SB;
            ComPort = Port;
        }
        public void run()
        {
            int c;
            try
            {
                while (true)
                {
                    c = ComPort.read();
                    ComBuffer.PutChar(c);
                }
            } catch (IOException e) {}
        }
    }