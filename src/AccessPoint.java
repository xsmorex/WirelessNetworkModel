import java.util.ArrayList;

public class AccessPoint extends NetworkDevices {

	ArrayList<Client> authorized;
	private String key;
	private Channel channel;
	Network curNetwork;

	{
		this.authorized = new ArrayList<Client>();
		this.channel = null;
		this.curNetwork = null;
	}
	
	//constructor of an access point
	public AccessPoint(String address, String key) {
		super(address);
		this.key = key;

	}
	//access point joining a network
	public boolean joinNetwork(Network temp) {
		if (temp.requestJoin(this)) {
			this.addNetwork(temp);
			channel = temp.requestChannel(this);
			return true;
		}
		return false;
	}
	
	//validating a client to be added
	public boolean addClient(Client temp, String key) {
		if (this.key.equals(key)) {
			authorized.add(temp); //checks if the key matches to authorize the client
			return true;
		}
		return false;
	}
	//adding to a network
	private void addNetwork(Network temp) {
		this.curNetwork = temp;
	}

	
	public Channel getChannel() {
		// TODO Auto-generated method stub
		return channel;
	}

	//handshake function
	public boolean handshake(Client c) {
		Packet tmp = null;
		HandshakePacket handshake = null;
		boolean f = false;

		for (int i = 0; i < channel.traffic.size() && !f; i++) {
			tmp = channel.traffic.get(i);
			if (tmp instanceof HandshakePacket && tmp.to.equalsIgnoreCase(address)
					&& tmp.from.equalsIgnoreCase(c.address)) {
				
				handshake = (HandshakePacket) tmp;
				channel.traffic.remove(i); //removes a packet
				f = true;
			}
		}
		if(!f)return false;

		if (!key.equals(handshake.key))
			return false;

		authorized.add(c);
		channel.writePacket(new HandshakePacket(address, c.address, key));
		
		return true;
	}
	
	@Override
	public Packet recievePacket() {
		Packet tmp = super.recievePacket();
		if(tmp == null) return null;
		boolean f = false;
		
		for(int i = 0; i < authorized.size() && !f; i++){
			if(authorized.get(i).equals(tmp.from)){
				f = true;
			}
		}
		
		if(f){
			sendPacket(new Packet(address, tmp.from));
			return tmp;
		}
		return null;
	}
	//empties a channel
	public void flushChannel(){
		channel.flush();
	}
	//disconnects the client
	public void disconnectClient(Client c){
		boolean f = false;
		int i;
		for(i = 0; i < authorized.size() && !f; i++){
			if(authorized.get(i).address.equals(c.address)){
				f = true;
				i--;
			}
		}
		if(f)authorized.remove(i);
	}

}
