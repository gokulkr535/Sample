
import java.io.*;
import java.net.*;

public class MyClient {
public static void main(String[] args) {
try{	
	Socket s=new Socket("localhost",3456);
		
	DataOutputStream dout=new DataOutputStream(s.getOutputStream());
	long startTime=System.currentTimeMillis();
	for (int i=1;i<=100 ;i++ )
	{
		dout.writeUTF("Hello Server "+i+"\r\n");
		dout.flush();
		//Thread.sleep(1);
	}
	long endTime=System.currentTimeMillis();
	long diff=endTime-startTime;
	System.out.println("Difference is "+diff);
	dout.close();
	s.close();

	}catch(Exception e){System.out.println(e);}
}
}
