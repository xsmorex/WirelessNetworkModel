import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static final Network net = new Network();
	static File log;
	static FileWriter fw;
	static final int clientCount = 97;
	//opening a class generates a log file
	static{
		log = new File("log.txt");
		try {
			fw = new FileWriter(log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void log(Packet p){
		if(p == null)return;
		try {
			fw.write(p.toString());
			fw.write("\n");
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//exemplary test
		AccessPoint ap = new AccessPoint("f7:88:g9:hi:j1:01", "1234");
		ap.joinNetwork(net);

		System.out.print(ap.address);
		System.out.println(((ap.curNetwork != null) ? " is " : " is not ")
				+ "connected");
		System.out.println("AP count in network = " + net.APCount());
		
		Client[] c = new Client[clientCount];
		String tmp = "";
		for(int i = 0; i < clientCount; i++){
			tmp = "f7:88:g9:hi:j1:";
			if(i < 8)tmp += "0" + (i + 2);
			else tmp += (i + 2);
			c[i] = new Client(tmp);
			net.connectClient(c[i], "1234");
			if(!c[i].conEstablished)System.out.println(c[i].address + " not connected");
		}
		

		Packet p = null;
		long count = 0;
		try {
			while (true) {
				for(int i = 0; i < clientCount; i++){
					p = new Packet(c[i].address, ap.address);
					log(p);
					c[i].sendPacket(p);
					p = c[i].recievePacket();
					log(p);
					Thread.sleep(100);
				}

				p = ap.recievePacket();
				log(p);
				Thread.sleep(100);


				ap.flushChannel();
				System.out.println("Burst no " + count++);
			}
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

}
