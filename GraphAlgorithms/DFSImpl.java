// Tiefensuche und topologische Sortierung.

import java.util.ArrayList;

public class DFSImpl implements DFS{

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
