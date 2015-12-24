
public abstract class NetworkDevices {
	String address; //unique address of a device
	protected Channel channel; 
	
	public NetworkDevices(String address){
		this.address = address;
	}
	//adds channel to a network
	protected void addChannel(Channel channel){
		this.channel = channel;
	}
	
	//sends a packet
	public boolean sendPacket(Packet packet) {
		
		if(channel.writePacket(packet)) {
			return true;
		}		
		return false;
	}
	//receives a packet
	public Packet recievePacket() {
		Packet tmp = null;
		if(channel != null){
			tmp = channel.readPacket(address);
		}
		
		return tmp;
	}
}
