import java.util.ArrayList;

//Algorithmen auf gewichtete Graphen
//Shortest paths
//Algorithmus von Bellman und Ford
//Algorithmus von Dijkstra
public class SPImpl implements SP{

    /**
     * Innere Klasse zum Abspeichern der Kanten
     * Bei gewichteten Graphen besitzt jede Kante zusätzlich ein Gewicht
     */
    private class Edge{
        public int aim;
        public double weight;
        public int from;

        /**
         * Konstruktor
         * @param from Der Index der Node, von dem die Kante ausgeht
         * @param aim Der Index der Node, zu dem die Kante geht
         * @param weight Das Gewicht des Graphen
         */
        private Edge (int from, int aim, double weight){
            this.from = from;
            this.aim = aim;
            this.weight = weight;
        }

    }


    private class Node {
        public int index;                                              //Index
        public Node predecessor;                                       //Vorgänger                                                        = pi(v)
        public ArrayList<Edge> successors = new ArrayList<>();         //alle Nachfolger
        public Double distance;                                        //Bei Belman-Ford: Abstand des Knotens bzw Gewicht eines kürzesten Weges nach v     = delta(v) bei Bellman-Ford
                                                                       //Bei Dijkstra:    Priorität des Knotens                                            = delta(v) bei Dijkstra

        private Node (int index){
            this.index = index;
        }
    }
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    /**
     * Algorithmus von Bellman-Ford auf dem Graphen g mit Startknoten s
     * ausführen.
     * Resultatwert true genau dann, wenn es im Graphen keinen vom
     * Startknoten aus erreichbaren Zyklus mit negativem Gewicht gibt.
     *
     * @param g Der gewichtete Graph g auf dem der Algorithmus angewendet wird
     * @param s Der Startknoten, von dem aus der Algorithmus startet
     * @return true, wenn kein Zyklus mit negativem Gewicht
     */
    @Override
    public boolean bellmanFord(WeightedGraph g, int s) {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();

        //Erstellung der Knoten aus dem Graphen g, sowie einfügen in die nodes-Liste
        for ( int i = 0; i < g.size(); i++){
            Node node = new Node(i);
            nodes.add(node);

            //Ausgehende Kanten (mit Gewicht hinzufügen), die Länge
            //der successorListe entspricht dem Grad des Knotens
            for ( int j = 0; j < g.deg(i); j++) {
                double weight = g.weight(i, j);
                int aim = g.succ(i, j);
                Edge edge = new Edge(node.index ,aim, weight);
                edges.add(edge);
                node.successors.add(edge);
            }
        }

        //Algorithmus start

          //Initialisierung
        //1. Für alle Knoten v:
        for (Node node : nodes){
            //Setze Distanz von v auf INF und den Vorgänger von v auf NIL
            node.distance = INF;
            node.predecessor = null;
        }
        //Setze dann Distanz von s = 0;
        nodes.get(s).distance = 0.0;

        //2. Wiederhole (|V| - 1) mal:
        for (int i = 0; i < nodes.size()-1; i++){
            //Für jede Kante (u,v) in ganz E:
            for (Edge edge : edges){

                //Verwerten einer Kante von u nach v:
                //Wenn Distanz von u + Gewicht der Kante von u nach v < Distanz (v) ist:

                //(d.h. wenn der Weg von s nach v über u kürzer als der kürzeste bis jetzt gefundene
                // Weg von s nach v ist)
                Node u = nodes.get(edge.from);
                Node v = nodes.get(edge.aim);
                if ( u.distance + edge.weight < v.distance){

                    //Setze Distanz von v = Distanz von u + Gewicht der Kante von u nach v
                    v.distance = u.distance + edge.weight;
                    //Setze Vorgänger von v auf u
                    v.predecessor = u;
                }
            }
        }

        //3. Für jede Kante von u nach v in ganz E:
        for (Edge edge : edges){
            Node u = nodes.get(edge.from);
            Node v = nodes.get(edge.aim);
            //Wenn Distanz von u + Gewicht der Kante von u nach v < Distanz von v
            if ( u.distance + edge.weight < v.distance ){

                //Abbruch, weil der Graph einen von s aus erreichbaren negativen Zyklus enthält
                //Logik dahinter: Der kürzeste Weg wurde schon ermittelt, wenn jetzt ein noch kürzerer
                //gefunden wird, so muss sich die Distanz über (nun erreichbaren) Zyklus negativ aufsummieren

                return false;
            }
        }

        //Algorithmus Ende
        //Wenn der Algorithmus terminiert, wurden keine Zyklen mit negativem Gesammtgewicht gefunden
        return true;
    }

    /**
     * Algorithmus von Dijkstra auf dem Graphen g mit Startknoten s
     * ausführen.
     * Die Kanten des Graphen dürfen keine negativen Gewichte besitzen.
     * (Dies muss nicht überprüft werden.)
     *
     * @param g Der gewichtete Graph g, auf dem der Algorithmus angewandt wird
     * @param s DerStartknoten, von dem aus der Algorithmus angewandt wird
     */
    @Override
    public void dijkstra(WeightedGraph g, int s) {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();

        //Erstellung der Knoten aus dem Graphen g, sowie einfügen in die nodes-Liste
        for ( int i = 0; i < g.size(); i++){
            Node node = new Node(i);
            nodes.add(node);

            //Ausgehende Kanten (mit Gewicht hinzufügen), die Länge
            //der successorListe entspricht dem Grad des Knotens
            for ( int j = 0; j < g.deg(i); j++) {
                double weight = g.weight(i, j);
                int aim = g.succ(i, j);
                Edge edge = new Edge(node.index ,aim, weight);
                edges.add(edge);
                node.successors.add(edge);
            }
        }

        //Anmerkung: Bei Dijkstra heißt die Priorität delta(v) hier distance

        //Algorithmus start
        BinHeap<Double, Node> Q = new BinHeap<>();
        ArrayList<BinHeap.Entry<Double, Node>> entries = new ArrayList<>();

        //1 Für alle Knoten v
        for ( Node node: nodes){
            //Setze Priorität von v auf INF und den Vorgänger auf NIL
            node.distance = INF;
            node.predecessor = null;
        }
            //Setze dann die Priorität von S auf 0
            nodes.get(s).distance = 0.0;

        //2. Für alle Knoten v
        for(Node node : nodes){
            //Füge v mit Priorität von v in eine Minimum-Vorrangwarteschlange
            entries.add(  Q.insert(node.distance, node)  );
        }

        //3. Solange die Warteschlange nicht leer ist:
        while ( !Q.isEmpty() ){
            //1 Entnimm einen Knoten u mit minimaler Priorität
            BinHeap.Entry<Double, Node> entry = Q.extractMin();
            Node node = entry.data();

            //2 Für jeden Nachfolger v von u, der sich noch in der Warteschlange befindet:
            for (int i = 0; i < node.successors.size(); i++){

                 Edge edgeToSuccessor = node.successors.get(i);
                 int indexOfSuccessor = edgeToSuccessor.aim;
                 Node successor = nodes.get(indexOfSuccessor);
                 BinHeap.Entry<Double, Node> entryOfSuccessor = entries.get(indexOfSuccessor);

                 if (  Q.contains( entryOfSuccessor )  ){
                     //1 Verwerten einer Kante von u nach v:
                     //  Wenn Distanz von u + Gewicht der Kante von u nach v < Distanz (v) ist:

                     //(d.h. wenn der Weg von s nach v über u kürzer als der kürzeste bis jetzt gefundene
                     // Weg von s nach v ist)

                     Node u = nodes.get(edgeToSuccessor.from);
                     Node v = nodes.get(edgeToSuccessor.aim);
                     if (/*u.distance != INF ||*/ u.distance + edgeToSuccessor.weight < v.distance){

                         //Setze Distanz von v = Distanz von u + Gewicht der Kante von u nach v
                         v.distance = u.distance + edgeToSuccessor.weight;
                         //Setze Vorgänger von v auf u
                         v.predecessor = u;

                     //2   Wenn die Priorität von v dadurch ernierigt wurde:
                         //Erniedrige die Priorität von v in der Warteschlange entsprechend

                         Q.changePrio(entryOfSuccessor, v.distance);
                     }
                 }

            } //Ende jeder Nachfolger v von u
        } //End While Warteschlange leer

    }


    // Von den Algorithmen ermittelte Information:

    /**
     *  Abstand des Knotens v zum Startknoten s (ggf. INF).
     *  double INF = Double.POSITIVE_INFINITY;
     * @param v Der Knoten v
     * @return Der Abstand des Knotens v
     */
    @Override
    public double dist(int v) {
        return nodes.get(v).distance;
    }

    /**
     * Vorgängerknoten des Knotens v auf dem kürzesten Weg zum
     * Startknoten (bzw. NIL, wenn es keinen Vorgänger gibt).
     *  int NIL = -1;
     * @param v Der Knoten v
     * @return Der Vorgänger des Knotens v
     */
    @Override
    public int pred(int v) {
        if(nodes.get(v).predecessor == null) return NIL;
        return nodes.get(v).predecessor.index;
    }



}
