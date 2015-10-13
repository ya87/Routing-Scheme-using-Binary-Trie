import java.io.File;

public class routing {
	/**
	 * Function to test project part 2 In this part we implemented a routing
	 * scheme (routing) for a network using Binary Trie.
	 * 
	 * @param args: file_name_1 file_name_2 source_node destination_node
	 */
	public static void main(String[] args) {
		try {
			// long curr = System.currentTimeMillis();
			// System.out.println("Start !!");

			if (args.length < 4) {
				System.out
						.println("ERROR: Expected 4 arguments ($routing file_name_1 file_name_2 source_node destination_node)\n source_node & destination_node lies in [0, n-1]\nwhere n is the number of nodes");
				System.exit(-1);
			}

			String graphFilename = args[0];
			// System.out.println("Input graph filename: "+graphFilename);
			String ipFilename = args[1];
			// System.out.println("Input ip filename: "+ipFilename);
			int srcV = Integer.parseInt(args[2]);
			// System.out.println("Source Vertex: "+srcV);
			int destV = Integer.parseInt(args[3]);
			// System.out.println("Destination Vertex: "+destV);

			File graphFile = new File(graphFilename);
			File ipFile = new File(ipFilename);
			if (!graphFile.exists()) {
				System.out.println("ERROR: Invalid File !!\n" + graphFilename
						+ " does not exit !!");
				System.exit(-1);
			}
			if (!ipFile.exists()) {
				System.out.println("ERROR: Invalid File !!\n" + ipFilename
						+ " does not exit !!");
				System.exit(-1);
			}

			RoutingEngine r = new RoutingEngine();
			r.createRoutingTables(graphFile, ipFile);
			// System.out.println("Time taken in building routing tables: "+(System.currentTimeMillis()-curr));

			r.findRoute(srcV, destV);

			// System.out.println("End !!");
			// System.out.println("Time taken: "+(System.currentTimeMillis()-curr));
		} catch (NumberFormatException nfe) {
			System.out
					.println("ERROR: Invalid Arguments !!\nExpected 4 arguments ($routing file_name_1 file_name_2 source_node destination_node)\n source_node & destination_node lies in [0, n-1]\nwhere n is the number of nodes");
		}
	}
}
