import java.util.ArrayList;



/**
 * Implementierung eines ungewichteten Graphen
 */
public class GraphImpl implements Graph {


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
