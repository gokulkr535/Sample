import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.io.IOException;
import java.util.*;
//public class Reactor implements Runnable 
public class Reactor extends Thread
{
	final Selector selector;
	final ServerSocketChannel serverSocket;
	public static Reactor obj;
	public Reactor(int port) throws IOException {
		obj=this;
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(
		new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk =
		serverSocket.register(selector,
		SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor());
	}
	public void run(){
		try {
			//long startTime=System.currentTimeMillis();
			int i=0;
			while (!Thread.interrupted()) {
				selector.select();
				Set selected = selector.selectedKeys();
				Iterator it = selected.iterator();
				while (it.hasNext()){
					i++;
					dispatch((SelectionKey)(it.next()));
				}
				selected.clear();
			}
			System.out.println("Iterator count is "+i);
			//long endTime=System.currentTimeMillis();
			//long diff=endTime-startTime;
			//System.out.println("Difference is "+diff);
		} catch (IOException ex) { /* ... */ }
	}

	void dispatch(SelectionKey k) {
		Runnable r = (Runnable)(k.attachment());
		if (r != null)
		r.run();
	}
	public static void main(String[] args) 
	{
		try
		{
				new Reactor(3456).start();
		}
		catch (Exception e)
		{
		}
		System.out.println("Hello World!");
	}

	class Acceptor implements Runnable { // inner
		public void run() {
		try {
			SocketChannel c = serverSocket.accept();
			if (c != null)
			new Handler(selector, c);
		}
		catch(IOException ex) { /* ... */ }
		}
	}
}

final class Handler implements Runnable {
	final SocketChannel socket;
	final SelectionKey sk;
	//ByteBuffer input = ByteBuffer.allocate(MAXIN);
	//ByteBuffer output = ByteBuffer.allocate(MAXOUT);
	ByteBuffer input = ByteBuffer.allocate(1024);
	ByteBuffer output = ByteBuffer.allocate(1024);
	static final int READING = 0, SENDING = 1;
	int state = READING;
	static int eventCount=0;
	static long startTime=-1;
	static long startTimeCount=-1;
	Handler(Selector sel, SocketChannel c)throws IOException {
		if (startTime==-1){
			startTimeCount++;
			startTime=System.currentTimeMillis();
			System.out.println("Start Time Set Successfully");
		}
		socket = c; c.
		configureBlocking(false);
		// Optionally try first read now
		sk = socket.register(sel, 0);
		sk.attach(this);
		sk.interestOps(SelectionKey.OP_READ);
		sel.wakeup();
	}
	boolean inputIsComplete() { /* ... */ return true;}
	boolean outputIsComplete() { /* ... */ return true;}
	void process(){}
	// class Handler continued
	public void run() {
		try {
			if (state == READING) read();
			else if (state == SENDING) send();
		} 
		catch (IOException ex) { /* ... */ }
	}
	void read() throws IOException {
		
		int i=socket.read(input);
		if (i == -1){			
             socket.close();
			long endTime=System.currentTimeMillis();
			long diff=endTime-startTime;
			System.out.println("Gokul Difference is "+diff+" && eventCount is"+eventCount);
			Reactor.obj. interrupt();
			return;
		}
		eventCount++;
		String outputString = new String(input.array());
		outputString =outputString.trim();
		outputString=outputString+"\r\n";
		System.out.println("Got Input From Client : "+eventCount + "  String is "+outputString);
		input.clear();
		/*if (inputIsComplete()) {
			//process(in);
			byte[] data = new byte[i];
			System.arraycopy(input.array(), 0, data, 0, i);
			

			state = SENDING;
			// Normally also do first write now
			sk.interestOps(SelectionKey.OP_WRITE);
		}*/
	}
	void send() throws IOException {
		socket.write(output);
		if (outputIsComplete()) sk.cancel();
	}
}
