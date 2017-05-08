import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.Set;
import java.util.Iterator;
import java.net.InetSocketAddress;

public class SelectorExample {
   static int countkey=0;
   static int count =0;
    public static void main (String [] args)
            throws IOException {

        // Get selector
        Selector selector = Selector.open();

        System.out.println("Selector open: " + selector.isOpen());

        // Get server socket channel and register with selector
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
		//serverSocket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5454);
        serverSocket.socket().bind(hostAddress,0);
		serverSocket.configureBlocking(false);
        int ops = serverSocket.validOps();
        SelectionKey selectKy = serverSocket.register(selector, ops, null);

        for (;;) {

      //      System.out.println("Waiting for select...");
	   
            int noOfKeys = selector.select();
			

          //  System.out.println("["+new java.sql.Timestamp(System.currentTimeMillis())+"]"+"Number of selected keys: " + noOfKeys);

            Set selectedKeys = selector.selectedKeys();
            Iterator iter = selectedKeys.iterator();

            while (iter.hasNext()) {

                SelectionKey ky = (SelectionKey)iter.next();
								
					++count;
					System.out.println("["+new java.sql.Timestamp(System.currentTimeMillis())+"]"+"Count of Connetions key -----------> "+count);
                if (ky.isAcceptable()) {
					 //System.out.println("====================================================================");
                    // Accept the new client connection
                    SocketChannel client = serverSocket.accept();
                    client.configureBlocking(false);

                    // Add the new connection to the selector
                    client.register(selector, SelectionKey.OP_READ);

                   System.out.println("["+new java.sql.Timestamp(System.currentTimeMillis())+"]"+"Accepted new connection from client: " + client+"Connection Number ------->  "+count);
					  //System.out.println("====================================================================");
                }
                else if (ky.isReadable()) {
					// System.out.println("====================================================================");
                    // Read the data from client

                    SocketChannel client = (SocketChannel) ky.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(2048);
                    int i =client.read(buffer);
                    String output = new String(buffer.array());
					  if(count>1995 )
                        System.out.println("Message read from client: " + output);
					output =output.trim();
					output=output+"\r\n";

                   // System.out.println("Message read from client: " + output);
				//	 System.out.println("====================================================================");
				//	System.out.println("Message sent	:	"+new String(buffer.array()));
					byte[] a=output.getBytes();
					ByteBuffer bb=ByteBuffer.wrap(a);
					// client.write(bb);
					//  buffer.clear();
					//  System.out.println("	Sent	Message	");
                    if (i == -1) {					
                        client.close();
                      //  System.out.println("Client messages are complete; close.");
                    }

                } // end if (ky...)
				//countkey++;
                iter.remove();
					if(count == 1 || count == 2000)
				    System.out.println("["+new java.sql.Timestamp(System.currentTimeMillis())+"]"+"no of job processed" + count);

            } // end while loop

        } // end for loop
    }
}
