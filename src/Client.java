import java.util.ArrayList;
import java.util.Random;

public class Client extends NetworkDevices{

	
	boolean conEstablished;
	ArrayList<AccessPoint> accessPoints; //a record of which access point is the Client connected to
	Network net;
	private String key;
	
	
	{
		this.conEstablished = false;
		this.accessPoints = new ArrayList<AccessPoint>();
		net = null;
		key = null;
	}
	
	public void disconnect(){
		for(int i = 0; i < accessPoints.size(); i++)accessPoints.get(i).disconnectClient(this);
		accessPoints.clear();
		net = null;
		conEstablished = false;
		channel = null;
	}
	
	public Client(String address) {
		super(address);		
	}
	
//	private boolean requestConnect(Network net, String address, String key) {
//		AccessPoint temp = net.findAP(address);
//		if(temp != null && temp.addClient(this, key)) {
//			connectTo(temp, key);
//			return true;
//		}
//		return false;
//	}
//	
//	private void connectTo(AccessPoint temp, String key) {
//		
//		handshake(temp, address);
//		this.addChannel(temp.getChannel());
//		accessPoints.add(temp);
//		this.conEstablished = true;
//		
//	}
	
	public void handshakeStart(Channel c, String key, AccessPoint ap, Network net) {
		this.net = net;
		c.writePacket(new HandshakePacket(address, ap.address, key));
	}
	
	public boolean handshakeFinalize(Channel c, String key, AccessPoint ap){
		boolean f = false;
		Packet tmp = null;
		HandshakePacket handshake = null;
		
		if(c.traffic.size() == 0) return false;
		
//		System.out.println(address + " trafic size = " + c.traffic.size());
		for (int i = 0; i < c.traffic.size() && !f; i++) {
			tmp = c.traffic.get(i);
			if (tmp instanceof HandshakePacket && tmp.to.equals(address)) {
				
				handshake = (HandshakePacket) tmp;
				if(handshake.key.equals(key)){
					c.traffic.remove(i);
					accessPoints.add(ap);
					conEstablished = true;
					channel = c;
					this.key = key;
				}
				f = true;
			}
		}
		return conEstablished;
	}
	
	public Channel requestChannel(AccessPoint temp) {
		return temp.getChannel();
	}
	
	public void addAccessPoint(AccessPoint temp) {
		this.accessPoints.add(temp);
	}
	
	public boolean sendPocket(Packet p){
		randomDisconnect();
		if(!conEstablished && key != null){
			net.connectClient(this, key);
		}
		if(!conEstablished)return false;
		
		return super.sendPacket(p);
		
	}
	
	public Packet receivePacket(){
		randomDisconnect();
		if(!conEstablished && key != null){
			net.connectClient(this, key);
		}
		if(!conEstablished)return null;
		
		return super.recievePacket();
	}

	private void randomDisconnect(){
		Random r = new Random();
		if(conEstablished && (r.nextInt(10) % 10) == 0)disconnect();
	}
	
}
