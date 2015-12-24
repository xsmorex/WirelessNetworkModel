
public class HandshakePacket extends Packet{

	public final String type = "Handshake";
	public String key;
	
	//constructor
	public HandshakePacket(String from, String to, String key) {
		super(from, to);
		this.key = key;
	}
	
}
