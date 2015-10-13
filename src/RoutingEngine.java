import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

public class RoutingEngine {
	Map<Integer, String> routers;
	Map<String, BinaryTrie<NextHop>> routingTables;

	public RoutingEngine() {
		routers = new HashMap<Integer, String>();
		routingTables = new HashMap<String, BinaryTrie<NextHop>>();
	}

	/**
	 * creates a graph from a list of vertices, edges and weights read from
	 * graphFile, use that graph and IP addresses read from ipFile to create
	 * routing tables for all routers
	 * 
	 * @param graphFile
	 * @param ipFile
	 */
	public void createRoutingTables(File graphFile, File ipFile) {
		loadRouterIPsFromFile(ipFile);
		Graph graph = new Graph(graphFile);
		int numRouters = routers.size();

		for (Entry<Integer, String> v : routers.entrySet()) {
			int srcV = v.getKey();
			// System.out.println("DSP running for V"+srcV);
			graph.runDSP(srcV);
			BinaryTrie<NextHop> bt = new BinaryTrie<NextHop>();
			for (int destV = 0; destV < numRouters; destV++) {
				if (destV != srcV) {
					// System.out.println("Inserting entry for dest V"+destV);
					NextHop nh = graph.getNextHop(srcV, destV);

					if (-1 != nh.id) {
						nh.setIP(routers.get(nh.id));
					} else {// nh.id == -1 means there is no path between srcV
							// and destV
						nh.setIP(null);
					}
					bt.insert(ipToBinary(routers.get(destV)), nh);
				}
			}
			bt.compress();
			// System.out.println("Routing table (before POT) for V"+srcV);
			// bt.print();
			bt.potCompress();
			// System.out.println("Routing table for V"+srcV);
			// bt.print();
			routingTables.put(v.getValue(), bt);
		}
	}

	/**
	 * loads a list of router IP address from a file
	 * 
	 * @param file
	 */
	private void loadRouterIPsFromFile(File file) {
		BufferedReader br = null;
		String line = null;
		try {
			br = new BufferedReader(new FileReader(file));
			int num = 0;
			while (null != (line = br.readLine())) {
				routers.put(num++, line);
				// br.readLine();
			}
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != br) {
					br.close();
				}
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
		// System.out.println("Routers: "+routers);
	}

	/**
	 * find shortest root from source IP to destination IP and prints prefixes
	 * of the routers matched in the path
	 * 
	 * @param srcV
	 * @param destV
	 */
	public void findRoute(int srcV, int destV) {
		String destRouter = routers.get(destV);
		String currRouter = routers.get(srcV);

		int cost = 0;
		StringBuilder path = new StringBuilder();
		boolean isPathExist = true;

		String destRouterBinary = ipToBinary(destRouter);
		while (!currRouter.equals(destRouter)) {
			// System.out.println(currRouter+" "+destRouter);
			BinaryTrie<NextHop> currRoutingTable = routingTables
					.get(currRouter);
			BTNode<NextHop> n = currRoutingTable.search(destRouterBinary);
			currRouter = n.data.ip;
			// if next hop router is null that means there is not path between
			// srcV and destV
			if (null == currRouter) {
				isPathExist = false;
				break;
			}
			cost += n.data.cost;
			path.append(n.key.substring(0, n.parent.bitNum + 1) + " ");
			// System.out.print(n.key.substring(0, n.parent.bitNum+1)+" ");
		}

		if (isPathExist) {
			System.out.println(cost);
			System.out.println(path.substring(0, path.length() - 1));
		}
	}

	/**
	 * converts IPv4 address from dot-decimal notation to its binary value
	 * 
	 * @param ip
	 * @return
	 */
	public String ipToBinary(String ip) {

		StringTokenizer st = new StringTokenizer(ip, ".");
		int count = 0;
		StringBuilder sb = new StringBuilder();
		while (st.hasMoreTokens()) {
			count++;
			if (count > 4) {
				System.out.println("Error : Invalid IP Address " + ip);
				System.exit(-1);
			}

			int temp = Integer.parseInt(st.nextToken());
			if (temp < 0 || temp > 255) {
				System.out.println("Error : Invalid IP Address " + ip);
				System.exit(-1);
			}

			sb.append(String.format("%8s", Integer.toBinaryString(temp))
					.replace(" ", "0"));
		}

		return sb.toString();
	}
}