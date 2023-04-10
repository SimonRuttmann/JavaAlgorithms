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
