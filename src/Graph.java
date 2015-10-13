import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Graph {
	class Vertex {
		int key, minDist; // minimum distance of this vertex from some source
							// node
		LinkedList<Edge> adjList; // adjacency list
		Vertex p; // parent vertex of this vertex in shortest path from some
					// source node to some destination node

		public Vertex(int key) {
			this.key = key;
			this.adjList = new LinkedList<Edge>();
			minDist = Integer.MAX_VALUE;
			p = null;
		}

		// adds edge e to adjacency list of this node
		public void addToAdjList(Edge e) {
			adjList.add(e);
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(key + " -> ");
			for (Edge e : adjList) {
				sb.append(e + " ");
			}
			return sb.toString();
		}
	}

	class Edge {
		Vertex target;
		int w;

		public Edge(Vertex target, int w) {
			this.target = target;
			this.w = w;
		}

		public String toString() {
			return target.key + "(" + w + ")";
		}
	}

	boolean directed;
	Vertex[] vertices;
	int numV, numE;

	public Graph(File file) {
		this.directed = false;
		this.numV = 0;
		this.numE = 0;

		loadGraphFromFile(file);
	}

	/**
	 * loads list of vertices, edges and weights from file and creates a graph
	 * 
	 * @param file
	 */
	private void loadGraphFromFile(File file) {
		// System.out.println("Loading graph from file");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line);
			this.numV = Integer.parseInt(st.nextToken());
			this.numE = Integer.parseInt(st.nextToken());
			// System.out.println("numV: "+numV+" numE: "+numE);

			this.vertices = new Vertex[numV];
			for (int i = 0; i < this.numV; i++) {
				this.vertices[i] = new Vertex(i);
			}

			int countE = 0;
			Edge e = null;
			while (null != (line = br.readLine())) {
				/*
				 * st = new StringTokenizer(line); int v1 =
				 * Integer.parseInt(st.nextToken()); int v2 =
				 * Integer.parseInt(st.nextToken()); int w =
				 * Integer.parseInt(st.nextToken());
				 */

				// System.out.println(line);
				int i1 = line.indexOf(' ');
				int i2 = line.indexOf(' ', i1 + 1);
				int v1 = Integer.parseInt(line.substring(0, i1));
				int v2 = Integer.parseInt(line.substring(i1 + 1, i2));
				int w = Integer.parseInt(line.substring(i2 + 1));

				e = new Edge(vertices[v2], w);
				vertices[v1].addToAdjList(e);

				e = new Edge(vertices[v1], w);
				vertices[v2].addToAdjList(e);

				countE++;
			}

			if (numE != countE) {
				System.out
						.println("Error: Invalid file !!\nNumber of edges in line 1 does not match the number of edges in file");
				System.exit(-1);
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

		// System.out.println("Loaded graph from file");
	}

	/**
	 * runs dijkstra's shortest path algorithm with srcV as the source node to
	 * find shortest path from srcV to every other node in the graph, if that
	 * path exits
	 * 
	 * @param srcV
	 */
	public void runDSP(int srcV) {
		// System.out.println("Running DSP");
		initialize(srcV);

		FibonacciHeap<Vertex> fh = new FibonacciHeap<Vertex>();
		List<Node<Vertex>> nodes = new ArrayList<Node<Vertex>>(numV);
		for (Vertex v : vertices) {
			Node<Vertex> n = new Node<Vertex>(v.minDist, v);
			fh.insert(n);
			nodes.add(n);
		}

		while (!fh.isEmpty()) {
			Vertex u = fh.removeMin().data;

			// if the distance of this vertex from srcV is less than Inf then
			// only it can relax vertices in its adjList
			if (Integer.MAX_VALUE > u.minDist) {
				if (!fh.isEmpty()) {
					for (Edge e : u.adjList) {
						Vertex v = e.target;
						if (relax(u, v, e.w)) {
							fh.decreaseKey(nodes.get(v.key), v.minDist);
						}
					}
				}
			}
		}

		// System.out.println("DSP done");
	}

	/**
	 * does some initialization before running DSP on graph
	 * 
	 * @param srcV
	 */
	private void initialize(int srcV) {
		for (int i = 0; i < numV; i++) {
			vertices[i].minDist = Integer.MAX_VALUE;
			vertices[i].p = null;
		}
		vertices[srcV].minDist = 0;
	}

	/**
	 * relaxes edge u-v if minDist[v] is greater than minDist[u] + w
	 * 
	 * @param u
	 * @param v
	 * @param w
	 * @return
	 */
	private boolean relax(Vertex u, Vertex v, int w) {
		if (v.minDist > u.minDist + w) {
			v.minDist = u.minDist + w;
			v.p = u;
			return true;
		}
		return false;
	}

	/**
	 * returns next hop vertex in the shortest path from srcV to destV
	 * 
	 * @param srcV
	 * @param destV
	 * @return
	 */
	public NextHop getNextHop(int srcV, int destV) {
		Vertex source = vertices[srcV];
		Vertex target = vertices[destV];
		Vertex nextHop = target;

		boolean isPathExist = true;
		for (Vertex v = target; v != source; v = v.p) {
			if (null == v) {
				isPathExist = false;
				break;
			}
			nextHop = v;
		}

		if (isPathExist)
			return new NextHop(nextHop.key, nextHop.minDist);
		else
			return new NextHop(-1, Integer.MAX_VALUE);
	}

	/**
	 * prints the shortest path from srcV to destV if the path exits
	 * 
	 * @param srcV
	 * @param destV
	 */
	public void printPath(int srcV, int destV) {
		Vertex source = vertices[srcV];
		Vertex target = vertices[destV];
		List<Integer> path = new ArrayList<Integer>();

		boolean isPathExist = true;
		for (Vertex v = target; v != source; v = v.p) {
			if (null == v) {
				isPathExist = false;
				break;
			}
			path.add(v.key);
		}

		if (isPathExist) {
			path.add(srcV);
			Collections.reverse(path);

			System.out.println(target.minDist);
			if (0 < path.size()) {
				System.out.print(path.get(0));
				for (int i = 1; i < path.size(); i++) {
					System.out.print(" " + path.get(i));
				}
			}
		}
		// System.out.println("Path: "+path);
		// System.out.println("Total Weight: "+target.minDist);
	}

	/*
	 * public void print(){ for(Vertex v: vertices){ System.out.println(v); } }
	 */
}