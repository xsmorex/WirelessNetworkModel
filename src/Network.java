import java.util.ArrayList;
import java.util.Random;

public class Network {
	
	private ArrayList<Channel> channels;	
	private ArrayList<AccessPoint> accessPoints;
	private ArrayList<Channel> used;
	Random r = new Random();
	
	public Network(){
		accessPoints = new ArrayList<AccessPoint>();
		channels = new ArrayList<Channel>();
		used = new ArrayList<Channel>();
		
		//creates channels
		int num = r.nextInt(10) + 4;
		for(int i = 0; i < num; i++)
			channels.add(new Channel(i + 1));
	}
	//counts access points
	public int APCount(){
		return accessPoints.size();
	}
	//assigns access point
	public AccessPoint findAP(String address){
		for(int i = 0; i < accessPoints.size(); i++){
			if(accessPoints.get(i).address.equals(address)) return accessPoints.get(i);
		}
		return null;
	}
	//adds an access point
	public boolean requestJoin(AccessPoint accessPoint) {
		accessPoints.add(accessPoint);
		return true;
	}
	//adds a randomly generated channel to the network
	public Channel requestChannel(AccessPoint accessPoint) {
		Channel tmp = channels.get((int)(r.nextInt(channels.size())));
		used.add(tmp);
		return tmp;
	}
	
	//connects a client using a handshake
	public AccessPoint connectClient(Client c, String key){
		AccessPoint ap = accessPoints.get(r.nextInt(accessPoints.size()));
		Channel ch = ap.getChannel();
		c.handshakeStart(ch, key, ap, this);
		ap.handshake(c);
		if(c.handshakeFinalize(ch, key, ap))return ap;
		
		return null;
	}
}
