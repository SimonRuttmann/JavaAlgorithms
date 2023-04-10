import java.util.ArrayList;


/**
 * Gerichteter gewichteter Graph.
 * (Ein ungerichteter gewichteter Graph kann als gerichteter gewichteter
 * Graph repräsentiert werden, bei dem jede Kante in beiden Richtungen
 * mit dem gleichen Gewicht vorhanden ist.)
 */
public class WeightedGraphImpl implements WeightedGraph {
public int[][] adjacencyListOfConst;   //Adjazenzliste welche dem Konstruktor übergeben wird (nur Adjazenzliste, nicht die dazugehörigen Gewichte)
private final ArrayList<Node> nodesList = new ArrayList<>();

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
        public Edge (int aim, double weight){
            this.aim = aim;
            this.weight = weight;
        }

    }

    /**
     * Innere Klassen zum Abspeichern der Informationen der Nodes
     */
    private class Node {
        public int index;
        public int degree;
        public ArrayList<Edge> successorList = new ArrayList<>();

        /**
         * Konstruktor
         * @param index Der Index des Knotens
         * @param degree Der Grad des Knotens
         * @param successors Alle ausgehenden Kanten des Knotens
         */
        public Node(int index, int degree, Edge[] successors) {
            this.index = index;
            this.degree = degree;
            for (Edge value : successors) {
                successorList.add(value);
            }

        }
    }

    /**
     * Der öffentliche Konstruktor von WeightedGraphIml erhält als Parameter jeweil die
     * Adjazenzlistendarstellung des Graphen als zweidimensionales Feld von int-Werten.
     *
     * Der Konstruktor von WeightedGraphImpl erhält als zweiten Parameter die zugehörigen
     * Kantengewichte als zweidimensionales Feld von double-Werten.
     *
     * @param adjacencyList Der Graph dargestellt als 2D-Int-Array
     *
     *                      Bsp:
     *                      0: { 1, 2 }     Knoten 0 -> 1 und 2
     *                      1: { }          Knoten 1 -> nichts
     *                      2: { 2 }        Knoten 2 -> 2
     *                      3: { 1, 2 }     Knoten 3 -> 1 und 2
     *
     *                      0: { 1.5, 0}    Kante 0 -> 1 Gewicht 1.5 Kante 0 -> 2 Gewicht 0
     *                      1: { }          Keine Kanten
     *                      2: { -3.7 }     Kante 2 -> 2 Gewicht -3.7
     *                      3: { 10, 4 }    Kante 3 -> 1 Gewicht 10  Kante 3 -> 2 Gewicht 4
     *
     *                      Implementierung:
     *                      Ein Graph ist eine Sammlung von Knoten und Kanten -> Implementiere Knoten als innere Klasse und speichere diese in einer ArrayList
     *
     *                      Jeder Knoten besitzt:
     *                      Einen Index          (Integer)
     *                      Einen Grad           (Integer)
     *                      Mehrere Nachfolger   (Edge-Array)
     *
     *                      Jede Kante besitzt nun:
     *                      Einen Zielknoten     (Integer)
     *                      Ein Gewicht          (Double)
     *
     */

    public WeightedGraphImpl(int[][] adjacencyList, double[][] weights) {
        this.adjacencyListOfConst = adjacencyList;
/*
        //DEBUG
        System.out.println("Eingefügter Graph:");
        for (int i = 0; i < adjacencyList.length; i++){
            System.out.print( "Node "+ i +" = ");
            for (int a = 0; a < adjacencyList[i].length; a++){
                System.out.print("" + adjacencyList[i][a]+" ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        //END DEBUG
*/

        //Erstelle Nodes und füge dieser der Nodes-Liste hinzu
        for (int nodeIterator = 0; nodeIterator < adjacencyList.length; nodeIterator++) {
            //EdgeListe erstellen:
            Edge[] edgesOfNode = new Edge[adjacencyList[nodeIterator].length];
            for ( int edgeIterator = 0; edgeIterator < adjacencyList[nodeIterator].length; edgeIterator++){
                edgesOfNode[edgeIterator] = new Edge ( adjacencyList[nodeIterator][edgeIterator], weights[nodeIterator][edgeIterator]);
            }
            Node newNode = new Node(nodeIterator, edgesOfNode.length, edgesOfNode);
            nodesList.add(newNode);

        }


    }

    // Beschreibungen siehe GraphImpl

    @Override
    public int size() {
        return nodesList.size();
    }

    @Override
    public int deg(int v) {
        return nodesList.get(v).degree;
    }

    @Override
    public int succ(int v, int i) {
        Node node = nodesList.get(v);
        ArrayList<Edge> edgesOfNode = node.successorList;
        return edgesOfNode.get(i).aim;
    }

    @Override
    public Graph transpose() {
        return new GraphImpl (this.adjacencyListOfConst).transpose();
    }

    /**
     * Gewicht der Kante von v zu seinem i-ten direkten Nachfolger.
     * i muss im selben Bereich wie bei der Methode succ liegen.
     * (Dies muss nicht überprüft werden.)
     * @param v Der Knoten, von dem die Kante ausgeht
     * @param i Die i-te Kante zum i-ten Nachfolger des Knotens v
     * @return Das Gewicht der Kante
     */
    @Override
    public double weight(int v, int i) {
        Node node = nodesList.get(v);
        ArrayList<Edge> edgesOfNode = node.successorList;
        return edgesOfNode.get(i).weight;
    }
}

