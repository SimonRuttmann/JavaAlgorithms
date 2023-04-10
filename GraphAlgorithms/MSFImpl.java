import java.util.ArrayList;

//Minimum Spanning Forest Algorithmus
//Modifizierter Algorithmus von Prim
//Minimumgerüst-Algorithmus auf gewichtete Graphen
public class MSFImpl implements MSF{

    /**
     * Innere Klasse zum Abspeichern der Kanten
     * Bei gewichteten Graphen besitzt jede Kante zusätzlich ein Gewicht
     */
    private class Edge{
        public int aim;
        public double weight;

        /**
         * Konstruktor
         * @param aim Der Index der Node, zu dem die Kante geht
         * @param weight Das Gewicht des Graphen
         */
        private Edge (int aim, double weight){
            this.aim = aim;
            this.weight = weight;
        }

    }


    private class Node {
        public int index;                                              //Index
        public Node predecessor;                                       //Vorgänger                 = pi(v)
        public ArrayList<Edge> successors = new ArrayList<>();         //alle Nachfolger
        public Double prio;                                            //priorität des Knotens     = delta(v)

        private Node (int index){
            this.index = index;
        }
    }
    private ArrayList<Node> nodes;
    private final static Double INF = Double.POSITIVE_INFINITY;

    /**
     *Minimalgerüst des Graphen g mit dem (modifizierten) Algorithmus
     * von Prim mit vorgegebenem Startknoten s berechnen.
     * Der Graph muss ungerichtet sein, d. h. jede Kante muss
     * in beiden Richtungen mit dem gleichen Gewicht vorhanden sein.
     * (Dies muss nicht überprüft werden.)
     *
     * @param g Der gewichtete Graph g
     * @param s Der Startknoten s
     */
    @Override
    public void compute(WeightedGraph g, int s) {
        this.nodes = new ArrayList<>();

        //Erstellung der Knoten aus dem Graphen g, sowie einfügen in die nodes-Liste
        for ( int i = 0; i < g.size(); i++){
            Node node = new Node(i);
            nodes.add(node);

            //Ausgehende Kanten (mit Gewicht hinzufügen), die Länge der successorListe entspricht dem Grad des Knotens
            for ( int j = 0; j < g.deg(i); j++) {
                double weight = g.weight(i, j);
                int aim = g.succ(i, j);
                Edge edge = new Edge(aim, weight);
                node.successors.add(edge);
            }
        }

        //Start Algorithmus (v = Nachfolger/successor)
        Node startNode = nodes.get(s);
        ArrayList<BinHeap.Entry<Double, Node>> entries = new ArrayList<>();

        // 1. Für jeden Knoten v ohne s
        BinHeap<Double, Node> Q = new BinHeap<>();
        for ( Node node : nodes) {
            if (node == startNode) {
                entries.add(null);
                continue;
            }

            // 1. Füge v mit Priorität Delta(v) = INF in eine Minimum-Vorrangwarteschlange Q ein.
            node.prio = INF;
            entries.add(    Q.insert(node.prio, node)  );                       //Entrylist:       (Startknoten: 0) 1, 2, 3, 4 ,5 ,6
                                                                                //Nachfolger 6      -> Stelle 6 existiert nicht

            // 2. Setzet den Vorgänger von v auf NIL
            node.predecessor = null;
        }

        //2. Setzte Vorgänger von s auf NIL
        startNode.predecessor = null;
        startNode.prio = 0.0;

        //3. Setzte u = s
        Node uNode = startNode;

        //4. Solange Q nicht leer ist:
        while (!Q.isEmpty()){

            //1. Für jeden Nachfolger v von u:
            for (int i = 0; i < uNode.successors.size(); i++){

                Edge edgeToSuccessor = uNode.successors.get(i);
                Node successor = nodes.get(edgeToSuccessor.aim);
                BinHeap.Entry<Double,Node> successorEntry = entries.get(successor.index); //(i)


                // Wenn v in Q enthalten und rho(u,v) < delta(v)
                //if ( Q.contains(    entries.get(successor.index)    )  && (  edgeToSuccessor.weight < uNode.prio  )  ){
                if ( Q.contains(    entries.get(successor.index)    )  && (  edgeToSuccessor.weight < successorEntry.prio()  )  ){
                    //1. Erniedrige die Priorität delta(v) auf rho(u,v)
                    Q.changePrio(successorEntry, edgeToSuccessor.weight);

                    //Ändere Priorität der Node ebenso
                    successor.prio = edgeToSuccessor.weight;

                    //DEBUG
                 /*   if ( successorEntry!= null){
                        System.out.println( successor.index);
                        System.out.println("Entry " +successorEntry.prio());
                        System.out.println("Node " +successor.prio);

                    }*/
                    //DEBUG


                    //2. Setzte Vorgänger von v auf u
                    successor.predecessor = uNode;
                }
            }
            //2. Entnimm einen Knoten u mit minimaler Priorität
            //DEBUG
         /*   for (Node nodes : nodes) {
                if (nodes.predecessor == null){
                    System.out.println("Node = " + nodes.index + "   Priorität = " + nodes.prio + "   Vorgänger = " + null);
                }
                else {
                    System.out.println("Node = " + nodes.index + "   Priorität = " + nodes.prio + "   Vorgänger = " + nodes.predecessor.index);
                }
            }
            System.out.println( "\n\n\n");*/
            //DEBUG
            BinHeap.Entry<Double, Node> minPrioEntry = Q.extractMin();

            uNode = minPrioEntry.data();

            //DEBUG
            //System.out.println( "Nächster Knoten = " + uNode.index);
            //DEBUG
        }
        //Ende Algorithmus
    }

    // Vom Algorithmus ermittelte Information:

    /**
     *  Vorgängerknoten des Knotens v im Gerüst
     * (bzw. NIL, wenn es keinen Vorgänger gibt).
     *
     * @param v Der Knoten v
     * @return Vorgänger des Knotens v
     */
    @Override
    public int pred(int v) {

        if (nodes.get(v).predecessor == null) return NIL;

        return nodes.get(v).predecessor.index;

    }
}
