# Routing Scheme using BinaryTrie
Implementation of a routing scheme for a network: 

1) Each router has an IP address and packets are forwarded to the next hop router by longest prefix matching using a binary trie.  

2) Implemented Dijkstra's Single Source Shortest Path (ssp) algorithm for undirected graphs using Fibonacci heaps. 

3) For each router R in the network, called ssp to obtain shortest path from R to each destination router Y. To construct the router table for R, for each destination Y, examined the shortest path from R to Y and determine the router Z just after R on this path. This gives a set of pairs <IP address of Y, next-hop router Z>. Inserted these pairs into a binary trie.  

4) Finally, did a postorder traversal, removing subtries in which the next hop is the same for all destinations. Thus, multiple destinations having a prefix match and the same next hop were grouped together in the trie.
