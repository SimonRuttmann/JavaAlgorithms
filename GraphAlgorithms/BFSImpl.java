import java.util.ArrayList;
import java.util.LinkedList;

//Breitensuche
public class BFSImpl implements BFS{

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

