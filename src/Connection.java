import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
	private String host = "";
	private int port = 8090;
	Socket client;
	private DataInputStream reader;
	private DataOutputStream writer;
	
	public boolean isWaiting;
	
	public Connection(String host,int port) {
		super();
		this.host = host;
		this.port = port;
	}
	
	public void connect() throws IOException{
		client = new Socket(host,port);
		reader = new DataInputStream(client.getInputStream());
		writer = new DataOutputStream(client.getOutputStream());
		isWaiting = true;
		
		
	}
	
	public void disConnect() throws IOException {
		client.close();
		reader.close();
		writer.close();
		isWaiting = false;
	}
	
	public void sendMessage(String xml) throws IOException{
		writer.writeUTF(xml);
	}
	
	private class WaitThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(isWaiting) {
				try {
					String xml = reader.readUTF();
					System.out.println(xml);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
	
}
