import java.io.File;

public class ssp {
	/**
	 * Function to test project part 1 In this part we implemented Dijkstra's
	 * Single Source Shortest Path (ssp) algorithm for undirected graphs using
	 * Fibonacci heaps
	 * 
	 * @param args
	 *            : file_name source_node destination_node
	 */
	public static void main(String[] args) {
		try {
			// long curr = System.currentTimeMillis();
			// System.out.println("Start !!");;

			if (args.length < 3) {
				System.out
						.println("ERROR: Expected 3 arguments ($ssp file_name source_node destination_node)\n source_node & destination_node lies in [0, n-1]\nwhere n is the number of nodes");
				System.exit(-1);
			}

			String filename = args[0];
			// System.out.println("Input filename: "+filename);
			int srcV = Integer.parseInt(args[1]);
			// System.out.println("Source Vertex: "+srcV);
			int destV = Integer.parseInt(args[2]);
			// System.out.println("Destination Vertex: "+destV);

			File file = new File(filename);
			if (!file.exists()) {
				System.out.println("ERROR: Invalid File !!\n" + filename
						+ " does not exit !!");
				System.exit(-1);
			}

			Graph graph = new Graph(file);
			// System.out.println("Time taken in building graph: "+
			// (System.currentTimeMillis() - curr));

			graph.runDSP(srcV);
			// System.out.println("Time taken in running DSP: "+
			// (System.currentTimeMillis() - curr));

			graph.printPath(srcV, destV);

			// System.out.println("End !!");
			// System.out.println("Time taken: "+ (System.currentTimeMillis() -
			// curr));
		} catch (NumberFormatException nfe) {
			System.out
					.println("ERROR: Invalid Arguments !!\nExpected 3 arguments ($ssp file_name source_node destination_node)\n source_node & destination_node lies in [0, n-1]\nwhere n is the number of nodes");
		}
	}
}
