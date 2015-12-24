
public class Packet {
	String from; // source address
	String to;   // destination address
	public final String type = "Packet"; 
	
	public Packet(String from, String to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public String toString(){
		return String.format("From: %s To: %s", from, to);
	}
}
