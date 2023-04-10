public class GraphImplTester {
    public static void main(String[] args) {
        System.out.println( "ungewichteter Graph Test");
        System.out.println();

        GraphImpl a = new GraphImpl(new int [] [] {
                { 1, 2 },	// Knoten 0 hat als Nachfolger Knoten 1 und 2.
                { },	// Knoten 1 hat keine Nachfolger.
                { 2 }	// Knoten 2 hat als Nachfolger sich selbst.
        });
        Graph atransposed = a.transpose();
     //   BinHeap<String, Integer> a = new BinHeap<>();
     //   a.extractMin()

        System.out.println("Size: " + a.size());
        System.out.println("Node 0 Deg: " + a.deg(0));
        System.out.println("Node 1 Deg: " + a.deg(1));
        System.out.println("Node 2 Deg: " + a.deg(2));
        System.out.println();
        System.out.println("Node 0 suc 1 : " + a.succ(0,0));
        System.out.println("Node 0 suc 2 : " + a.succ(0,1));
        System.out.println("Node 2 suc 1 : " + a.succ(2,0));

        System.out.println("Transponiere transponierten Graph ");
        atransposed.transpose();

        System.out.println();
        System.out.println();
        System.out.println("Gewichteter Graph Test");
        System.out.println();

        WeightedGraphImpl b = new WeightedGraphImpl(new int [] [] {
                { 1, 2 },	// Knoten 0 hat als Nachfolger Knoten 1 und 2.
                { },	// Knoten 1 hat keine Nachfolger.
                { 2 }	// Knoten 2 hat als Nachfolger sich selbst.
        }, new double [] [] {
                { 1.5, 0 },	// Gewichte der Kanten (0, 1) und (0, 2).
                { },
                { -3.7 }	// Gewicht der Kante (2, 0).
        });

        System.out.println("Size: " + b.size());
        System.out.println("Node 0 Deg: " + b.deg(0));
        System.out.println("Node 1 Deg: " + b.deg(1));
        System.out.println("Node 2 Deg: " + b.deg(2));
        System.out.println();
        System.out.println("Node 0 suc 1 : " + b.succ(0,0));
        System.out.println("Node 0 suc 2 : " + b.succ(0,1));
        System.out.println("Node 2 suc 1 : " + b.succ(2,0));
        System.out.println();

        Graph bTransposed = b.transpose();

        System.out.println("Transponiere transponierten Graph ");
        bTransposed.transpose();
    }
}
