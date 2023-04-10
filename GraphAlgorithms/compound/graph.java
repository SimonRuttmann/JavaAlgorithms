import java.util.ArrayList;
import java.util.LinkedList;

/********************************************Interfaces****************************************************************/

/*
 * Graphen
 */

// Gerichteter Graph.
// (Ein ungerichteter Graph kann als gerichteter Graph repräsentiert
// werden, bei dem jede Kante in beiden Richtungen vorhanden ist.)
interface Graph {
    // Größe des Graphen, d. h. Anzahl seiner Knoten.
    // Die Knoten werden direkt durch Nummern zwischen 0 einschließlich
    // und size() ausschließlich repräsentiert, das heißt:
    // Alle Parameter von Methoden dieser und anderer Schnittstellen,
    // die Knoten bezeichnen (z. B. v), müssen Werte in diesem Bereich
    // besitzen. (Dies muss nicht überprüft werden.)
    // Methoden, die Knoten als Resultat liefern (z. B. succ), müssen
    // Werte in diesem Bereich liefern.
    int size ();

    // Grad des Knotens v, d. h. Anzahl seiner ausgehenden Kanten
    // bzw. direkten Nachfolger.
    int deg (int v);

    // i-ter direkter Nachfolger des Knotens v.
    // i muss zwischen 0 einschließlich und deg(v) ausschließlich
    // liegen. (Dies muss nicht überprüft werden.)
    int succ (int v, int i);

    // Transponierter Graph, d. h. ein neuer Graph mit denselben Knoten
    // wie der aktuelle Graph, der für jede Kante (u, v) des aktuellen
    // Graphen die entgegengesetzte Kante (v, u) enthält.
    Graph transpose ();
}

// Gerichteter gewichteter Graph.
// (Ein ungerichteter gewichteter Graph kann als gerichteter gewichteter
// Graph repräsentiert werden, bei dem jede Kante in beiden Richtungen
// mit dem gleichen Gewicht vorhanden ist.)
interface WeightedGraph extends Graph {
    // Gewicht der Kante von v zu seinem i-ten direkten Nachfolger.
    // i muss im selben Bereich wie bei der Methode succ liegen.
    // (Dies muss nicht überprüft werden.)
    double weight (int v, int i);

    // Achtung:
    // Wenn man für einen gewichteten Graphen transpose() aufruft,
    // erhält man einen Graphen des Typs Graph ohne Gewichte.
}

/*
 * Anmerkungen zu allen Algorithmen:
 *
 * Der übergebene Graph g darf nicht null sein.
 * (Dies muss nicht überprüft werden.)
 *
 * Die Nachfolger eines Knotens u müssen immer in der Reihenfolge
 * g.succ(u, 0) bis g.succ(u, g.deg(u) - 1) durchlaufen werden.
 *
 * Auf einem Objekt einer Implementierungsklasse XYZImpl muss als
 * erstes ein Algorithmus (d. h. je nach Klasse eine der Methoden
 * search, sort, compute, bellmanFord, dijkstra) ausgeführt werden.
 * Anschließend kann die vom Algorithmus ermittelte Information
 * abgefragt werden (je nach Klasse mit den Methoden dist, pred,
 * det, fin, sequ, component; d. h. wenn eine dieser Methoden vor der
 * Ausführung eines Algorithmus aufgerufen wird, darf ihr Verhalten
 * undefiniert sein).
 * Eine mehrmalige Ausführung eines Algorithmus auf demselben Objekt
 * muss nicht unterstützt werden (d. h. man sollte für jede Ausführung
 * eines Algorithmus ein neues Objekt der Klasse XYZImpl erzeugen).
 */

/*
 * Algorithmen auf ungewichteten Graphen
 */

// Breitensuche.
interface BFS {
    // Breitensuche im Graphen g mit Startknoten s durchführen.
    void search (Graph g, int s);

    // Vom Algorithmus ermittelte Information:

    // Abstand des Knotens v zum Startknoten s der Suche
    // (bzw. INF, wenn v von s aus nicht erreichbar ist).
    int INF = -1;
    int dist (int v);

    // Vorgängerknoten von v auf dem Weg von s nach v
    // (bzw. NIL, wenn es keinen Vorgänger gibt).
    int NIL = -1;
    int pred (int v);
}

// Tiefensuche und topologische Sortierung.
interface DFS {
    // Tiefensuche im Graphen g durchführen.
    // In der Hauptschleife des Algorithmus werden die Knoten in der
    // Reihenfolge 0 bis g.size() - 1 durchlaufen.
    void search (Graph g);

    // Tiefensuche im Graphen g durchführen.
    // In der Hauptschleife des Algorithmus werden die Knoten in der
    // Reihenfolge d.sequ(g.size() - 1) bis d.sequ(0) durchlaufen.
    void search (Graph g, DFS d);

    // Topologische Sortierung des Graphen g durchführen.
    // Resultatwert true, wenn dies möglich ist,
    // false, wenn der Graph einen Zyklus enthält.
    boolean sort (Graph g);

    // Von den Algorithmen ermittelte Information:

    // Entdeckungs- bzw. Abschlusszeit des Knotens v,
    // jeweils zwischen 1 und 2 * g.size().
    int det (int v);
    int fin (int v);

    // Für i von 0 bis g.size() - 1 liefert sequ(i) die Knoten
    // des Graphen g sortiert nach aufsteigenden Abschlusszeiten.
    // Das heißt: sequ(0) ist der Knoten mit der kleinsten
    // Abschlusszeit, sequ(g.size() - 1) der mit der größten.
    int sequ (int i);
}

// Starke Zusammenhangskomponenten.
interface SCC {
    // Starke Zusammenhangskomponenten des Graphen g bestimmen.
    void compute (Graph g);

    // Vom Algorithmus ermittelte Information:

    // Eindeutige Nummer der starken Zusammenhangskomponente,
    // zu der der Knoten v gehört.
    // Das heißt: component(u) muss genau dann gleich component(v) sein,
    // wenn u und v zur gleichen starken Zusammenhangskomponente gehören.
    // Abgesehen davon, können die Nummern beliebig sein.
    int component (int v);
}

/*
 * Algorithmen auf gewichteten Graphen
 */

// Minimalgerüste.
interface MSF {
    // Minimalgerüst des Graphen g mit dem (modifizierten) Algorithmus
    // von Prim mit vorgegebenem Startknoten s berechnen.
    // Der Graph muss ungerichtet sein, d. h. jede Kante muss
    // in beiden Richtungen mit dem gleichen Gewicht vorhanden sein.
    // (Dies muss nicht überprüft werden.)
    void compute (WeightedGraph g, int s);

    // Vom Algorithmus ermittelte Information:

    // Vorgängerknoten des Knotens v im Gerüst
    // (bzw. NIL, wenn es keinen Vorgänger gibt).
    int NIL = -1;
    int pred (int v);
}

// Kürzeste Wege.
interface SP {
    // Algorithmus von Bellman-Ford auf dem Graphen g mit Startknoten s
    // ausführen.
    // Resultatwert true genau dann, wenn es im Graphen keinen vom
    // Startknoten aus erreichbaren Zyklus mit negativem Gewicht gibt.
    boolean bellmanFord (WeightedGraph g, int s);

    // Algorithmus von Dijkstra auf dem Graphen g mit Startknoten s
    // ausführen.
    // Die Kanten des Graphen dürfen keine negativen Gewichte besitzen.
    // (Dies muss nicht überprüft werden.)
    void dijkstra (WeightedGraph g, int s);

    // Von den Algorithmen ermittelte Information:

    // Abstand des Knotens v zum Startknoten s (ggf. INF).
    double INF = Double.POSITIVE_INFINITY;
    double dist (int v);

    // Vorgängerknoten des Knotens v auf dem kürzesten Weg zum
    // Startknoten (bzw. NIL, wenn es keinen Vorgänger gibt).
    int NIL = -1;
    int pred (int v);
}

/********************************************Graph Impl****************************************************************/



/**
 * Implementierung eines ungewichteten Graphen
 */
class GraphImpl implements Graph {


    /**
     * Innere Klassen zum Abspeichern der Informationen der Nodes
     */
    private class Node {
        public int index;
        public int degree;
        public ArrayList<Integer> successorList = new ArrayList<>();

        /**
         * Konstruktor
         * @param index Der Index des Knotens
         * @param degree Der Grad des Knotens
         * @param successors Alle ausgehenden Kanten des Knotens
         */
        public Node(int index, int degree, int[] successors) {
            this.index = index;
            this.degree = degree;
            for (int value : successors) {
                successorList.add(value);
            }

        }
    }

    private final ArrayList<Node> nodesList = new ArrayList<>();

    /**
     * Der öffentliche Konstruktor von GraphIml erhält als Parameter jeweil die
     * Adjazenzlistendarstellung des Graphen als zweidimensionales Feld von int-Werten.
     *
     * @param adjacencyList Der Graph dargestellt als 2D-Int-Array
     *
     *                      Bsp:
     *                      0: { 1, 2 }  Knoten 0 -> 1 und 2
     *                      1: { }       Knoten 1 -> nichts
     *                      2: { 2 }     Knoten 2 -> 2
     *                      3: { 1, 2 }  Knoten 3 -> 1 und 2
     *
     *                      Implementierung:
     *                      Ein Graph ist eine Sammlung von Knoten und Kanten -> Implementiere Knoten als innere Klasse und speichere diese in einer ArrayList
     *                      Jeder Knoten besitzt:
     *                      Einen Index          (Integer)
     *                      Einen Grad           (Integer)
     *                      Mehrere Nachfolger   (Integer-Array)
     */
    public GraphImpl(int[][] adjacencyList) {
        int[] edgesOfNode;
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
            edgesOfNode = adjacencyList[nodeIterator];
            Node newNode = new Node(nodeIterator, edgesOfNode.length, edgesOfNode);
            nodesList.add(newNode);
        }


    }

    /**
     * Größe des Graphen, d. h. Anzahl seiner Knoten.
     * Die Knoten werden direkt durch Nummern zwischen 0 einschließlich
     * und size() ausschließlich repräsentiert, das heißt:
     * Alle Parameter von Methoden dieser und anderer Schnittstellen,
     * die Knoten bezeichnen (z. B. v), müssen Werte in diesem Bereich
     * besitzen. (Dies muss nicht überprüft werden.)
     * Methoden, die Knoten als Resultat liefern (z. B. succ), müssen
     * Werte in diesem Bereich liefern.
     *
     * @return Die Anzahl der Knoten im Graphen
     **/
    @Override
    public int size() {
        return nodesList.size();
    }

    /**
     * Grad des Knotens v, d. h. Anzahl seiner ausgehenden Kanten
     * bzw. direkten Nachfolger.
     *
     * @param v Der Knoten des Graphen
     * @return Der Grad des Knotens
     */
    @Override
    public int deg(int v) {
        return nodesList.get(v).degree;
    }

    /**
     * i-ter direkter Nachfolger des Knotens v.
     * i muss zwischen 0 einschließlich und deg(v) ausschließlich
     * liegen. (Dies muss nicht überprüft werden.)
     *
     * @param v Der Knoten des Graphen
     * @param i Der i-te Nachfolger -> Die Kante mit
     * @return Der Integer-Wert auf welche, die Kante Nr. i hinzeigt
     *
     * Bsp:
     * 0: { 1, 2 }  Knoten 0 -> 1 und 2
     * 1: { }       Knoten 1 -> nichts
     * 2: { 2 }     Knoten 2 -> 2
     * 3: { 1, 2 }  Knoten 3 -> 1 und 2
     *
     * deg(3) = 2
     * An Stelle 0 -> 1
     * An Stelle 1 -> 2
     *
     * succ(3, 0) = 1
     * succ(3, 1) = 2
     */
    @Override
    public int succ(int v, int i) {
        Node node = nodesList.get(v);
        ArrayList<Integer> edgesOfNode = node.successorList;
        return edgesOfNode.get(i);
    }

    /**
     * Idee, stelle den Graphen als Adjazenz-Matrix dar
     * 1. Baue die Adjazenz-Matrix
     * 2. Transponiere die Matrix
     * 3. Überführe die Matrix in eine Liste
     * 4. Konstruktoraufruf mit der Liste
     *
     * @return Der transponierte Graph (Ursprungsgraph mit umgedrehten Kanten)
     *
     * Ursprungsmatrix:
     *          1   2   3   4   5
     *
     * 1        0   1   1   0   0
     * 2        0   0   0   1   1
     * 3        0   0   0   1   0
     * 4        0   0   0   0   1
     * 5        0   0   0   0   0
     *
     * Transponierte Matrix:
     *          1   2   3   4   5
     *
     * 1        0   0   0   0   0
     * 2        1   0   0   0   0
     * 3        1   0   0   0   0
     * 4        0   1   1   0   0
     * 5        0   1   0   1   0
     *
     *
     *
     * Herstellen einer Adjazenz-Liste nach der Folgenden Form
     *
     *    0: { 1, 2 }  Knoten 0 -> 1 und 2
     *    1: { }       Knoten 1 -> nichts
     *    2: { 2 }     Knoten 2 -> 2
     *    3: { 1, 2 }  Knoten 3 -> 1 und 2
     *
     */
    @Override
    public Graph transpose() {


        int[][] adjacencyMatrix = new int[nodesList.size()][nodesList.size()];
        int[][] adjacencyMatrixTransposed = new int[nodesList.size()][nodesList.size()];
        Node node;

        //1. Ursprungsadjazentmatrix
        //2. Transponierte Matrix

        //Iteriere durch die Nodes
        for (int y = 0; y < adjacencyMatrix.length; y++) {
            node = nodesList.get(y);
            //Das ist die Zeile, für jede Node
            //Füge eine 1 ein, wenn die Kante besteht, sonst eine 0
            for (int x = 0; x < adjacencyMatrix[y].length; x++) {

                if (node.successorList.contains(x)) {
                    adjacencyMatrix[y][x] = 1;
                    adjacencyMatrixTransposed[x][y] = 1;        //  <- 2
                } else {
                    adjacencyMatrix[y][x] = 0;
                    adjacencyMatrixTransposed[x][y] = 0;        //  <- 2
                }
            }
        }
        /*
        //DEBUG
        System.out.println("Adjazentmatrix");
        for (int y = 0; y < adjacencyMatrix.length; y++){
            System.out.println();
            System.out.print(" Node "+ y + " = ");
            for ( int x = 0; x < adjacencyMatrix[y].length; x++){
                System.out.print( adjacencyMatrix[y][x] + " ");
            }

        }
        System.out.println();
        System.out.println();

        System.out.println("Transponierte Adjazentmatrix");
        for (int y = 0; y < adjacencyMatrixTransposed.length; y++){
            System.out.println();
            System.out.print(" Node "+ y + " = ");
            for ( int x = 0; x < adjacencyMatrixTransposed[y].length; x++){
                System.out.print( adjacencyMatrixTransposed[y][x] + " ");
            }

        }
        System.out.println();
        System.out.println();
        System.out.println();
        //END DEBUG
*/
        //3. Erzeuge die transponierte Listen-Adjazent

        int[][] transposedAdjacencyList = new int[nodesList.size()][0];
        int[] edgeBigArray; //Array mit allen Edge einträgen und weiteren nulls
        int[] edgeArray;    //Array mit allen Edge einträgen

        //Für jede Node, erstelle das dazugehörige edge-Array
        for ( int iteratorNode = 0; iteratorNode < nodesList.size(); iteratorNode++){

            //Erstelle Array mit genug Platz und speichere die Edges ab
            edgeBigArray= new int[nodesList.size()];
            int i = 0;
            for (int temp = 0; temp < adjacencyMatrixTransposed.length; temp++){
                if (adjacencyMatrixTransposed[iteratorNode][temp] == 1){
                    edgeBigArray[i] = temp;
                    i++;
                }
            }

            //Hinweis: i ist hier z.B. 10, Elemente 0 - 9 hinzugfügt

            //Schneide Nullen ab
            edgeArray = new int[i];
            for ( int p = 0; p < i; p++){
                edgeArray[p] = edgeBigArray[p];
            }

            //Füge das edgeArray der entsprechenden Node, zu dem 2D-Array hinzu
            transposedAdjacencyList[iteratorNode] = edgeArray;
        }

        return new GraphImpl(transposedAdjacencyList);
    }
}

/********************************************Weighted Graph Impl*******************************************************/

/**
 * Gerichteter gewichteter Graph.
 * (Ein ungerichteter gewichteter Graph kann als gerichteter gewichteter
 * Graph repräsentiert werden, bei dem jede Kante in beiden Richtungen
 * mit dem gleichen Gewicht vorhanden ist.)
 */
class WeightedGraphImpl implements WeightedGraph {
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

/********************************************BFS Impl******************************************************************/

//Breitensuche
class BFSImpl implements BFS{

    public ArrayList<Node> nodes = new ArrayList<>();
    public LinkedList<Node> fifo = new LinkedList<>();

    private class Node {

        public int distance;
        public int predecessor;
        public int index;

        public Node (int index, int distance, int predecessor){
            this.index = index;
            this.distance = distance;
            this.predecessor = predecessor;
        }

    }

    // Breitensuche im Graphen g mit Startknoten s durchführen.
    @Override
    public void search(Graph g, int s) {



        //Fehlerhaften Aufruf abfangen
        if(  g == null || g.size()-1 < s || s < 0) return;

        for ( int i = 0; i < g.size(); i++){
            // if (i == s ) continue;

            //StartNode mit Distanz 0 und Vorgänger 0 erstellen
            if ( i == s ){
                nodes.add( new Node (i , 0, NIL));
                continue;
            }
            Node node = new Node (i, INF, NIL);
            nodes.add(node);
        }

        //StartNode mit Distanz 0 und Vorgänger 0 erstellen
        //Node start = new Node ( s, 0 , NIL);
        //nodes.add(s, start);

        //Node start = new Node(0,0, NIL);
        //nodes.add(start);

        fifo.addFirst(nodes.get(s));

        //Solange der Stack nicht leer ist
        while (!fifo.isEmpty()){
            //Entnimm der ersten Knoten u aus der Warteschlange
            Node node = fifo.getFirst();
            fifo.removeFirst();

            //Für jeden Nachfolger v von u:
            for ( int i = 0; i < g.deg(node.index); i++ ) {

                //Erhalte den Nachfolger aus der Knotenliste durch Zugriff mit dem Index, welcher g.succ(Knoten, Degree) liefert
                Node successor = nodes.get(g.succ(node.index, i));

                //Wenn Distanz gleich Unendlich ist
                if ( successor.distance == INF){
                    //Setze Distanz V = Distanz U + 1 und Vorgänger = u
                    successor.distance = node.distance+1;
                    successor.predecessor = node.index;

                    //Füge V am Ende der Warteschlange an
                    fifo.addLast(successor);
                }
            }
        }

    }

    @Override
    public int dist(int v) {
        return nodes.get(v).distance;
    }

    @Override
    public int pred(int v) {
        return nodes.get(v).predecessor;
    }

    /*
    // Vom Algorithmus ermittelte Information:

    // Abstand des Knotens v zum Startknoten s der Suche
    // (bzw. INF, wenn v von s aus nicht erreichbar ist).
    int INF = -1;
    int dist (int v);

    // Vorgängerknoten von v auf dem Weg von s nach v
    // (bzw. NIL, wenn es keinen Vorgänger gibt).
    int NIL = -1;
    int pred (int v);
    */

}

/********************************************DFS Impl****************************************************************/

class DFSImpl implements DFS{

    /**
     * Damit besitzt jeder Knoten außerdem zu jedem Zeitpunkt eine Farbe:
     * - Ein Knoten ist weiss, wenn er noch nicht entdeckt wurde, d. h. wenn er noch keine
     *   Entdeckungs- und Abschlusszeit besitzt.
     * - Ein Knoten ist grau, wenn er gerade bearbeitet wird, d. h. wenn er eine
     *   Entdeckungs-, aber noch keine Abschlusszeit besitzt.
     * - Ein Knoten ist schwarz, wenn seine Bearbeitung abgeschlossen ist, d. h. wenn er
     *   sowohl eine Entdeckungs- als auch eine Abschlusszeit besitzt.
     */
    enum Color {white, grey, black}

    /**
     * Innere Klasse zur Repräsentation der Knoten
     */
    private class Node {
        public int index;          //Index ("Name" der Knotens)
        public int discoveryTime;  //Entdeckungszeit
        public int finishTime;     //Abschlusszeit
        public Node predecessor;   //Vorgänger
        public Color color;        //Farbe des Knotens

        public Node (int index){
            this.index = index;
        }

    }
    private ArrayList<Node> nodes;
    //private static final int NIL = -1;

    //Repräsentiert einen einfachen Zähler, der immer hochgesetzt wird,
    //wenn die Entdeckungs- oder Abschlusszeit mit diesem Wert gesetzt wird
    private int currentTime;

    //Wert, der angibt, ob der Graph g einen Zyklus enthält,
    //dieser wird durch search(Graph g) gesetzt
    private boolean cycle;

    //Liste an Knoten, welche nach aufsteigenden Abschlusszeiten sortiert ist
    private ArrayList<Node> finishTimeList;

    /**
     * Tiefensuche im Graphen g durchführen.
     * In der Hauptschleife des Algorithmus werden die Knoten in der
     * Reihenfolge 0 bis g.size() - 1 durchlaufen.
     *
     * @param g Der zu durchlaufende Graph
     */
    @Override
    public void search(Graph g) {
        this.nodes = new ArrayList<>();
        this.currentTime = 1;
        this.cycle = false;
        this.finishTimeList = new ArrayList<>();


        //Füge alle Knoten der Liste hinzu und färbe die Knoten weiss ein
        for ( int i = 0; i < g.size(); i++){
            Node node = new Node(i);
            node.color = Color.white;
            nodes.add(node);
        }

        //Für jeden Knoten u in G
        for (Node node : nodes){

            //Wenn u weiss ist:
            if ( node.color == Color.white){

                //1. Setze Vorgänger von u auf NIL
                node.predecessor = null;

                //2. Durchsuche den zu u gehörenden Teilgraphen
                searchSubgraphU(g, node);

            }
        }
    }

    /**
     * Hilsmethode, benutzt um den Schritt 2:
     * Durchsuche den zu u gehörenden Teilgraphen
     * @param g Graph g, auf dem die Tiefensuche ausgeführt wird
     * @param node Der Knoten u
     */
    private void searchSubgraphU(Graph g, Node node){
        //1. Setze Entdeckungszeit von u auf den nächsten Zeitwert aus der Menge {1,...,2*|V|}
        node.discoveryTime = currentTime;
        currentTime++;
        node.color = Color.grey;

        //2. Für jeden Nachfolger v von u
        for (int i = 0; i < g.deg(node.index); i++){
            Node successor = nodes.get(g.succ(node.index ,i));

            //Erweiterung für Topologische Sortierung:
            //Für jeden Nachfolger v von u wird in Schritt 2.2 der Tiefensuche zusätzlich überprüft,
            //ob er grau ist. Wenn ja, handelt es sich bei der Kante (u, v ) um eine Rückwärtskante.
            //In diesem Fall enthält der Graph einen Zyklus und kann daher nicht topologisch
            //sortiert werden.
            if ( successor.color == Color.grey){
                this.cycle = true;
            }

            //Wenn v weiss ist
            if ( successor.color == Color.white){

                //1 Setze Vorgänger von v auf u
                successor.predecessor = node;

                //2 Durchsuche rekursiv den zu v gehörenden Teilgraphen
                searchSubgraphU(g, successor);

            }
        }

        //3. Setzte Abschlusszeit von u auf den nächsten Zeitwert aus der Menge {1,...,2*|V|}
        node.finishTime = currentTime;
        currentTime++;
        node.color = Color.black;
        this.finishTimeList.add(node);
    }



    /**
     * Tiefensuche im Graphen g durchführen.
     * In der Hauptschleife des Algorithmus werden die Knoten in der
     * Reihenfolge d.sequ(g.size() - 1) bis d.sequ(0) durchlaufen.
     *
     * @param g Der zu durchlaufende Graph g
     * @param d Die davor ausgeführte Tiefensuche
     */
    @Override
    public void search(Graph g, DFS d) {
        this.finishTimeList = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.currentTime = 1;
        this.cycle = false;

        ArrayList<Node> nodesInvertedSequence = new ArrayList<>();


        //Füge alle Knoten der Liste hinzu und färbe die Knoten weiss ein
        for ( int i = 0; i < g.size(); i++){
            Node node = new Node(i);
            node.color = Color.white;
            nodes.add(node);
        }

        //Füge alle Knoten in der richtigen Reihenfolge der Liste hinzu und färbe die Knoten weiss ein
        //Die richtige Reihenfolge enspricht dabei:
        //  Node 1  Abschlusszeit: 108
        //  Node 3  Abschlusszeit: 17
        //  Node 2  Abschlusszeit: 8
        //  Node 4  Abschlusszeit: 2
        //  Da      DFS.sequ(0), DFS.sequ(1), DFS.sequ(2) ... die Abschlusszeiten aufsteigend sortiert zurückgibt, muss hier in nach unten iteriert werden
        //  Also:   DFS.sequ(g.size()-1), DFS.sequ(g.size()-2), ... , DFS.sequ(1), DFS.sequ(0).

        //Erstelle eine invertierte Squenzliste, in der die Knoten stehen
        //Bsp: Sequenz:  2 0 3 4 1 6 5
        //     Inverted: 5 6 1 4 0 3 2
        for ( int i = g.size()-1; i >= 0; i--) {
            Node node = nodes.get(d.sequ(i));
            node.color = Color.white;
            nodesInvertedSequence.add(node);
        }


        //Für jeden Knoten u in G
        for (Node node : nodesInvertedSequence){

            //Wenn u weiss ist:
            if ( node.color == Color.white){

                //1. Setze Vorgänger von u auf NIL
                node.predecessor = null;

                //2. Durchsuche den zu u gehörenden Teilgraphen
                searchSubgraphU(g, node);

            }
        }
    }

    /**
     * Topologische Sortierung des Graphen g durchführen.
     * Resultatwert true, wenn dies möglich ist,
     * false, wenn der Graph einen Zyklus enthält.
     * @param g Der zu sortierende Graph g
     * @return  true, wenn möglich
     *          false, wenn mind. ein Zyklus enthalten ist
     */
    @Override
    public boolean sort(Graph g) {
        search(g);
        return !this.cycle;
    }



    /*
     * Von den Algorithmen ermittelte Information:
     * Entdeckungs- bzw. Abschlusszeit des Knotens v,
     * jeweils zwischen 1 und 2 * g.size().
     */

    /**
     * Entdeckungszeit
     * @param v Der Knoten V
     * @return Die Entdeckungszeit des Knotens V
     */
    @Override
    public int det(int v) {
        return nodes.get(v).discoveryTime;
    }

    /**
     * Abschlusszeit
     * @param v Der Knoten V
     * @return Die Abschlusszeit des Knotens V
     */
    @Override
    public int fin(int v) {
        return nodes.get(v).finishTime;
    }



    /**
     * Für i von 0 bis g.size() - 1 liefert sequ(i) die Knoten
     * des Graphen g sortiert nach aufsteigenden Abschlusszeiten.
     * Das heißt: sequ(0) ist der Knoten mit der kleinsten
     * Abschlusszeit, sequ(g.size() - 1) der mit der größten.
     *
     * @param i Der Knoten mit der i-t kleinsten Abschlusszeit Bsp: seq(0) = kleinste, seq(1) = zweitkleinste, ... , seq(g.size()-1) = größte Abschlusszeit
     * @return Der Knotens, welcher die i-t kleinste Abschlusszeit besitzt
     */
    @Override
    public int sequ(int i) {
        if ( finishTimeList != null) return finishTimeList.get(i).index;
        return 404;
    }
}

/********************************************SCC Impl****************************************************************/

class SCCImpl implements SCC{

    private class Node {
        private final static int NIL = -1;
        public int component =      NIL;        //Die starke Zusammenhangskomponente des Knoten
        public int index;                       //Der Index des Knoten, wird beim erstellen gesetzt
        public int detectionTime =  NIL;        //Die Entdeckungszeit des Knoten (nach der 2.Tiefensuche)
        public int finishTime =     NIL;        //Die Abschlusszeit des Knotens  (nach der 2. Tiefensuche)

        public Node (int index){
            this.index = index;
        }

    }

    //Nodes, wie sie vom Graphen übergeben werden (Index = Pos. in der Liste), mit dem compute Algorithmus,
    //werden die Entdeckungs- und Abschlusszeiten, sowie die Zusammenhangskomponenten gesetzt
    private ArrayList<Node> nodes;

    /**
     * Starke Zusammenhangskomponenten des Graphen g bestimmen
     * @param g Der Graph g
     */
    @Override
    public void compute(Graph g) {
        //Fehlerabfrage
        if ( g == null ) return;

        nodes = new ArrayList<>();
        //Nodes, sortiert nach den Abschlusszeiten der 2. Tiefensuche, die Nodes darin enthalten:
        //Index, detectionTime, finishTime und nach Abschluss des Algorithmus auch die Component
        ArrayList<Node> secondFinishTimeList = new ArrayList<>();

        for ( int i = 0; i < g.size(); i++){
            Node node = new Node(i);
            nodes.add(node);
        }
        //Führe eine erste Tiefensuche auf G aus, um die Abschlusszeiten
        //aller Knoten zu bestimmen.
        DFS first_depthFirstSearch = new DFSImpl();
        first_depthFirstSearch.search(g);

        //DEBUG
      /*  for  (int i = 0; i < g.size(); i++){
            System.out.println( "Sequenz 1. Tiefensuche: " + first_depthFirstSearch.sequ(i));
        }*/

        //Führe eine zweite Tiefensuche auf G Transponiert aus, in der die Knoten u in der äußeren
        //Schleife in absteigender Reihenfolge dieser Abschlusszeiten j(u) durchlaufen
        //werden.
        DFS second_depthFirstSearch = new DFSImpl();
        second_depthFirstSearch.search(g.transpose(), first_depthFirstSearch);


        int index;
        int detectionTime;
        int finishTime;
        //Speichere alle Entdeckunugs- sowie Abschlusszeiten der zweiten Tiefensuche
        for ( int i = 0; i < g.size(); i++) {
            //Sequenz (Knoten nach aufsteigenden Abschlusszeiten):
            index = second_depthFirstSearch.sequ(i);                    //  7 3 2 1 6 0 4

            //DEBUG
            // System.out.println( "Sequenz 2. Tiefensuche: " + index );

            detectionTime = second_depthFirstSearch.det(index);         // Entdeckungszeit von z.B. 7
            finishTime = second_depthFirstSearch.fin(index);            // Abschlusszeit von z.B. 7

            Node node = nodes.get(index);
            node.detectionTime = detectionTime;
            node.finishTime = finishTime;
            secondFinishTimeList.add(node);

            //secondFinishTimeList.add(new Node(index, detectionTime, finishTime));

        }

        //Jeder Baum des resultierenden Tiefensuchewalds der zweiten Tiefensuche
        //entspricht einer starken Zusammenhangskomponente von G (und von GT).

        //Ausgangssituation:
        //Ich habe: Eine Liste mit Knoten vom Graphen g
        //          Die Operationen vom Graphen g
        //          Eine Liste mit den Knoten soriert nach Abschlusszeiten, der 2. Tiefensuche (Entdeckungs- und Abschlusszeit abfragbar)
        //Ich muss: Die Zusammenhangskomponenten herausfinden und sie den Knoten hinzufügen

        //Abschlussliste:                       8 3 2 1 3 9 10 11 12
        //Abschlusswerte:                       2 6 9 13 14 16 19 24
        //Entdeckerwerte:                       1 4 7 10 11 3  17 23
        //Mögliche Zusammenhangskomponenten:   |-----||----------||-|

        //Anordnung muss nach dem Schema von links nach rechts gehen, dies gilt aufgrund der Sortierung nach Abschlusszeiten,
        //da ein Teilgraph terminiert und die Abschusszeit der Wurzel gesetzt wird -> Nächst höhere Abschlusszeit > Abschlusszeit der Wurzel.

        //Idee:         Kante bestimmen: Fall Vorwärtskante:     U start -> V Start -> ... V Ende -> U Ende
        //                                     Daraus folgt:     U start < V Start < V Ende < U Ende
        //                               In allen anderen Fällen: -> Es liegt eine BackEdge oder CrossEdge vor
        //                                                        -> Neue Zusammenhangskomponente beginnt



        //Vorsicht: Die Liste muss von ! HINTEN ! durchiteriert werden, da die Wurzeln die höchste Abschlusszeit besitzen und damit hinten anzufinden sind!
        int componentCount = 0;
        int indexOfNextRoot = 0;

        // Beim ersten Knoten die Zusammenhangskomponente 0 setzen
        if (secondFinishTimeList.size() > 0) {
            Node firstNode = secondFinishTimeList.get(secondFinishTimeList.size()-1);
            firstNode.component = componentCount;
        }

        // Für alle weiteren Knoten in der sortierten Liste, ermitteln ob eine Vorwärtskante vorliegt, wenn ja sind sie in der gleichen Zusammenhangskomponente
        // Wenn nein, nimm den Knoten an der Stelle i und, verwende ihn als nächste RootNode der nächsten Zusammenhangskomponente und vergleiche alle verbleibenden Knoten damit
        for (int i = secondFinishTimeList.size()-2; i >= 0; i--){
            Node nextNode = secondFinishTimeList.get(i);
            Node currentRootNode = secondFinishTimeList.get(indexOfNextRoot);

            //entspricht: if ( currentRootNode.detectionTime < nextNode.detectionTime < nextNode.finishTime < currentRootNode.finishTime)
            if (        (currentRootNode.detectionTime < nextNode.detectionTime) && (nextNode.detectionTime < nextNode.finishTime)
                    &&  (nextNode.finishTime < currentRootNode.finishTime)  ){

                // Es liegt eine Vorwärtskante vor, dadurch ist der nächste Knoten in der gleichen Zusammenhangskomponente wie der vorherige
                nextNode.component = currentRootNode.component;
            }
            else{
                //Der nächste Knoten ist nicht Teil der Zusammenhangskomponente:
                //Setze die Komponente des Knotens auf den nächsten Wert in der Menge {0...|V|}
                //Verwende nun diesen Knoten als neue Wurzel für die nächste Zusammenhangskomponente und untersuche alle weitere verbleibende Knoten
                componentCount++;
                nextNode.component = componentCount;
                indexOfNextRoot = i;

                //Hier wird i um eins erniedrigt -> currentRootNode ist dadurch NIE nextNode
            }

        }



    }


    // Vom Algorithmus ermittelte Information:

    /**
     * Eindeutige Nummer der starken Zusammenhangskomponente,
     * zu der der Knoten v gehört.
     * Das heißt: component(u) muss genau dann gleich component(v) sein,
     * wenn u und v zur gleichen starken Zusammenhangskomponente gehören.
     * Abgesehen davon, können die Nummern beliebig sein.
     *
     * @param v Der Knoten v
     * @return  Die starke Zusammenhangskomponente des Knoten v
     */
    @Override
    public int component(int v) {
        return nodes.get(v).component;
    }


}

/********************************************MSF Impl****************************************************************/

//Minimum Spanning Forest Algorithmus
//Modifizierter Algorithmus von Prim
//Minimumgerüst-Algorithmus auf gewichtete Graphen
class MSFImpl implements MSF{

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


/********************************************SP Impl****************************************************************/

//Algorithmen auf gewichtete Graphen
//Shortest paths
//Algorithmus von Bellman und Ford
//Algorithmus von Dijkstra
class SPImpl implements SP{

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

/***********************************************ENDE****************************************************************/