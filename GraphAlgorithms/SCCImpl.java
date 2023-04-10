//Starke Zusammenhangskomponenten
//Strong Coherent Components

import java.util.ArrayList;

public class SCCImpl implements SCC{

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
