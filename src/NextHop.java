//this class represents the next hop node in the shortest path from source
public class NextHop {
	int id;
	String ip;
	int cost;

	public NextHop(int id, int cost) {
		this.id = id;
		this.cost = cost;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}
}