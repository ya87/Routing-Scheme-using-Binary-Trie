srcpath = ./src
classpath = .

sourcefiles = \
Node.java \
FibonacciHeap.java \
NextHop.java \
Graph.java \
ssp.java \
BTNode.java \
BinaryTrie.java \
RoutingEngine.java \
routing.java

classfiles = $(patsubst %.java, $(classpath)/%.class, $(sourcefiles))
#classfiles=$(sourcefiles:.java=.class)
 
all: $(classfiles)

$(classpath)/%.class: $(srcpath)/%.java
	javac -d $(classpath) -classpath $(classpath) $<

clean:
	rm -f $(classpath)/*.class