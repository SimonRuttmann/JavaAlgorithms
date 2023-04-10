import java.io.*;

//DONE: 	GraphImpl
// 			WeightedGraphImpl
//			BFS Breadth first search
//			DFS depth first search + topological sorting
//			SCC Strong Coherent Components
//			MSF	Minimum Spanning Forest								Gewichteter Graph
// 			SP  Shortest Path Algorithm: 		Bellman and Ford	Gewichteter Graph
// 			SP  Shortest Path Algorithm: 		Dijkstra			Gewichteter Graph
//TODO Abfrage bei gewichte Graphen Algos:  Distanz = DOUBLE.MAXVALUE einbauen -> IF (... != Double.maxValue || bisheriges)
//TODO Testing und Debugging


/*
ich hab eine kurze frage zum msf Algorithmus, wenn die Knoten gleich gewichtet sind, welchen von den beiden muss man nehmen? Weil wenn wir das bsp. von der Folie Nr.152 betrachtet, dann nimmt der BinHeap bei uns immer den weg B->D und von D->C, weil B->D und E->C gleich gewichtet sind.
-> Antwort "Egal" ist "uns überlassen"
Die beiden Tiefensuchen liefern aber doch nicht direkt die Einteilung in components oder? Das muss man im Nachhinein doch aus dem sequ,fin und det berechnen?
-> Antwort: Sequenz durchlaufen -> An finisch und detection Werte herausfindne
 */




// Testprogramm für Graphalgorithmen.
class GraphTest {

    // Testgraphen.
    // (Für eigene Tests können beliebige weitere Testgraphen
    // hinzugefügt werden.)
    private static Graph [] graphs = {
	// Beispiel eines ungewichteten Graphen.
	new GraphImpl(new int [] [] {
	    { 1, 2 },	// Knoten 0 hat als Nachfolger Knoten 1 und 2.
	    { },	// Knoten 1 hat keine Nachfolger.
	    { 1, 2 }	// Knoten 2 hat als Nachfolger sich selbst.
	}),


//			{ 1, 2 },	// Knoten 0 hat als Nachfolger Knoten 1 und 2.
//			{ 1, 2, 0},		// Knoten 1 hat keine Nachfolger.
//			{ 2, 1 }	// Knoten 2 hat als Nachfolger sich selbst.
//	}),


	// Beispiel eines gewichteten Graphen.
	new WeightedGraphImpl(new int [] [] {
	    { 1, 2 },	// Knoten 0 hat als Nachfolger Knoten 1 und 2.
	    { },	// Knoten 1 hat keine Nachfolger.
	    { 2 }	// Knoten 2 hat als Nachfolger sich selbst.
	}, new double [] [] {
	    { 1.5, 0 },	// Gewichte der Kanten (0, 1) und (0, 2).
	    { },
	    { -3.7 }	// Gewicht der Kante (2, 0).
	}),

	// Eine ungewöhnliche Implementierung des Graphen 0 <-> 1
	// ohne Verwendung von Adjazenzlisten.
	new Graph () {
	    public int size () { return 2; }
	    public int deg (int v) { return 1; }
	    public int succ (int v, int i) { return 1 - v; }
	    public Graph transpose () { return this; }
	}
    };


    // name und value ausgeben.
    private static <T> void print (String name, T value) {
	System.out.print(" " + name + "=" + value);
    }

    // v sowie name1, value1, name2 und value2 ausgeben
    // (name2 und value2 nur, wenn sie nicht null sind).
    private static <T1, T2>
    void print (int v, String name1, T1 value1, String name2, T2 value2) {
	System.out.print(v + ":");
	print(name1, value1);
	if (name2 != null) print(name2, value2);
	System.out.println();
    }

    // v sowie name und value ausgeben.
    private static <T> void print (int v, String name, T value) {
	print(v, name, value, null, null);
    }

    // Hauptprogramm.
    // Auswahl des Algorithmus durch das erste Kommandozeilenargument:
    // bfs -> breadth first search
    // dfs -> depth first search
    // sort -> topological sort
    // scc -> strongly connected components
    // msf -> minimum spanning forest
    // bell -> Bellman-Ford
    // dijk -> Dijkstra
    // Auswahl des Testgraphen durch das zweite Kommandozeilenargument.
    // (Bei den Algorithmen msf, bell und dijk muss ein gewichteter
    // Graph ausgewählt werden.)
    // Auswahl des Startknotens durch das optionale dritte
    // Kommandozeilenargument (Standardwert 0).
    public static void main (String [] args) {
	// Kommandozeilenargumente.
	String algo = args[0];
	Graph graph = graphs[Integer.parseInt(args[1])];
	int s = args.length >= 3 ? Integer.parseInt(args[2]) : 0;

	// Gewünschten Algorithmus ausführen
	// und die von ihm ermittelte Information ausgeben.
	int n = graph.size();
	switch (algo) {
	case "bfs":
	    BFS bfs = new BFSImpl();
	    bfs.search(graph, s);
	    for (int v = 0; v < n; v++) {
		print(v, "dist", bfs.dist(v), "pred", bfs.pred(v));
	    }
	    break;
	case "dfs":
	case "sort":
	    DFS dfs = new DFSImpl();
	    if (algo.equals("dfs")) {
		dfs.search(graph);
	    }
	    else {
		if (!dfs.sort(graph)) {
		    System.out.println("cycle");
		    return;
		}
	    }
	    for (int i = 0; i < n; i++) {
		int v = dfs.sequ(i);
		print(v, "det", dfs.det(v), "fin", dfs.fin(v));
	    }
	    break;
	case "scc":
	    SCC scc = new SCCImpl();
	    scc.compute(graph);
	    for (int v = 0; v < n; v++) {
		print(v, "component", scc.component(v));
	    }
	    break;
	case "msf":
	    MSF msf = new MSFImpl();
	    msf.compute((WeightedGraph)graph, s);
	    for (int v = 0; v < n; v++) {
		print(v, "pred", msf.pred(v));
	    }
	    break;
	case "bell":
	case "dijk":
	    SP sp = new SPImpl();
	    if (algo.equals("bell")) {
		if (!sp.bellmanFord((WeightedGraph)graph, s)) {
		    System.out.println("negative cycle");
		    return;
		}
	    }
	    else {
		sp.dijkstra((WeightedGraph)graph, s);
	    }
	    for (int v = 0; v < n; v++) {
		print(v, "dist", sp.dist(v), "pred", sp.pred(v));
	    }
	    break;
	default:
	    System.out.println("unknown algorithm: " + algo);
	    return;
	}
    }


}
