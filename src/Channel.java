import java.util.ArrayList;

public class Channel {
	int chan_no;
	ArrayList<Packet> traffic;
	
	{
		traffic = new ArrayList<Packet>();
	}
	
	public Channel(int chan_no) {
		this.chan_no = chan_no;
	}

	public boolean writePacket(Packet p){
		try {
			traffic.add(p);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public Packet readPacket(String addr){
		Packet tmp = null;
		int i;
		boolean f = false;
		for(i = 0; i < traffic.size() && !f; i++){
			tmp = traffic.get(i);
			if(tmp.to.equals(addr)){
				f= false;
				traffic.remove(i);
			}
		}
		
		if(!f)return null;
			
		return tmp;
	}
	
	public void flush(){
		traffic.clear();
	}
}
