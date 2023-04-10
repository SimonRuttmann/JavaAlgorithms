import java.util.ArrayList;
/*
import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.nio.file.*;

class DefaultTest {

    static Integer prio = 0, data = 0;

    static void getNewRand() {
        Random rand = new Random();
        prio = rand.nextInt();
        data = rand.nextInt();
    }

    public static void main(String[] args) throws IOException {
        int testdurchfuehrungen=1;
        int i;
        Integer tmp=null;
        BinHeap.Entry<Integer,Integer> e = null;
        BinHeap<Integer,Integer> H = new BinHeap();
        BinHeap<Integer,Integer> K = new BinHeap();
        String filedata = "";

        File fileout = new File("DefaultTestFile");
        if (!fileout.exists()) fileout.createNewFile();
        Scanner scanFile = new Scanner(fileout);
        BufferedWriter write = new BufferedWriter(new FileWriter(fileout.getAbsoluteFile()));


        //Testet ob Inserts aktiv sind
        try{
            assert(false);
            System.out.println("Fehler: Asserts nicht aktiv");
            return ;
        }
        catch(AssertionError f){
            System.out.println("Test beginnt");
        }

        //Schreibt zufällige prio und data in Textdatei und insert'ed
        for (int j = 0; j < testdurchfuehrungen; j++) { //male wie oft der Test durchgeführt wird
            H = new BinHeap();
            scanFile = new Scanner(fileout);
            write = new BufferedWriter(new FileWriter(fileout.getAbsoluteFile()));

            assert (H.isEmpty()) : "Heap sollte leer sein";
            if (!fileout.exists()) fileout.createNewFile();

            for (i = 0; i < 5; i++) {
                getNewRand();
                System.out.println(prio + " " + data + " ");
                write.write(prio + " " + data + " ");
                System.out.println("Eingefügt wird "+prio + " " + data + " ");
                assert (H.contains(H.insert(prio, data)));
                assert (!H.isEmpty()) : "Heap sollte Elemente enthalten";
            }
            assert (i == H.size()) : "size stimmt nicht mit Anzahl Elemente im Baum ueberein| size=" + H.size() + " anzahl" + i;

            write.flush();

            //Checkt mithilfe der Textfile ob alle inserts auch im Baum wiedergefunden werden können
            while (scanFile.hasNext()) {
                prio = scanFile.nextInt();
                data = scanFile.nextInt();
                e= H.insert(prio,data);
                if (!H.contains(e)) {
                    H.dump();
                }
                getNewRand();

                assert (H.contains(e)) : "Elemente p:" + prio + " & d:" + data + " fehlen im Heap";
                assert(!H.contains(K.insert(prio,data))) : "H enthealt einen entry den es nicht im Heap gibt";
            }

            e=null;
            assert (!H.contains(e)): "Der Binheap enthealt einen null-Entry";



            H.dump();
            // Test changePrio
            for (int k = 0; k < 100; k++) {
                getNewRand();
                System.out.println("Change Prio beginnt, Initialinsert: prio:"+prio+" data:"+data);
                e = H.insert(prio, data);
                getNewRand();
                System.out.println("Changed to prio:"+prio);
                H.changePrio(e,prio);
                assert((int)e.prio()==prio):"changePrio ist fehlerhaft";
                System.out.println("Change Prio endet\n");
            }



            write.close();
            scanFile.close();
            //System.out.println(fileout.delete());
        }
        System.out.println("Es wurden keine Fehler gefunden");
    }
}

*/
//Funktionierende aktuellste Version vom 29.11.2020 18Uhr

// Als Binomial-Halde implementierte Minimum-Vorrangwarteschlange
// mit Prioritäten eines beliebigen Typs P (der die Schnittstelle
// Comparable<P> oder Comparable<P'> für einen Obertyp P' von P
// implementieren muss) und zusätzlichen Daten eines beliebigen Typs D.
class BinHeap<P extends Comparable<? super P>, D> {

    // Student - Unimensch - Person - Object -> Student kommt rein -> Student muss sich mit sich selbst vergleichen können ODER mit Unimensch ODER Person ODER Object, d.h. (EINES dieser Interfaces implementieren)
    // Nochmal verständlicher: Er muss sich nicht als Student vergleichen können, es reicht, wenn er sich als Person mit anderen Personen vergleichen kann
    // Ohne das <? super T> müsste das übergebe Object T das Interface Comparable<T> implementieren (Student implements Comparable<Student>), mit dem Wildcardausdruck, muss es nur eines der Interfaces der Oberklassen implementieren
    // Deswegen geht auch Integer, da Interger Comparable Implementiert und Number nicht Comparable
    // Auto Comparable -> Mercedes, BMW extends Auto -> Auto und Mercedes geht, obwohl sie sich als ihren Typ nicht comparen können, so können sie sich über Auto comparen -> Bmw Modelle als Priority möglich!
    //Wurzelknoten des Heaps
    private Node<P, D> rootnode;

    // Leere Halde erzeugen.
    public BinHeap() {
        this.rootnode = null;
    }


    /**
     * Einfügen eines Objekts mit einer bestimmten Priorität
     * Erzeuge eine temporäre Halde mit einem einzigen Baum mit Grad 0, die das Objekt
     * enthält, und vereinige sie mit der aktuellen Halde.
     *
     * @param p Priorität des Objekts
     * @param d Zusätzliche Daten des Objekts
     * @return Der eingefügte Eintrag
     */
    public Entry<P, D> insert(P p, D d) {
        if (p == null ) return null;
        Entry<P, D> newEntry = new Entry<>(p, d);
        Node<P, D> newHeapRoot = new Node<>(newEntry);
        newEntry.node = newHeapRoot;
        //Rückverweis herstellen
        newHeapRoot.entry = newEntry;
        this.rootnode = mergeHeaps(rootnode, newHeapRoot);

        return newEntry;
    }


    /**
     * Auslesen eines Objekts mit minimaler Prior ität
     * Suche in der Liste der Wurzelknoten ein Objekt mit minimaler Priorität.
     *
     * @return Eintrag mit minimaler Priorität
     */
    public Entry<P, D> minimum() {
        //Wenn Halde leer return null
        if (this.rootnode == null) return null;

        Node<P, D> getMinNode;
        Node<P, D> tempMinNode = this.rootnode;
        for (getMinNode = this.rootnode; getMinNode != null; getMinNode = getMinNode.sibling) {
            if (getMinNode.prio().compareTo(tempMinNode.prio()) < 0)    //Prio der Node kleiner als Minimum
            {
                tempMinNode = getMinNode;
            }
        }
        return tempMinNode.entry;
    }


    /**
     * Einen Eintrag mit minimaler Priorität liefern und aus der Halde entfernen.
     *
     * @return Entfernter Eintrag
     */
    public Entry<P, D> extractMin() {

        // 1
        //Suche in der Liste der Wurzelknoten ein Objekt mit minimaler Priorität
        //und entferne diesen Knoten aus der Liste.

        Entry<P, D> getMinEntry = this.minimum();
        if (getMinEntry == null) return null;

        Node<P, D> getMinNode = getMinEntry.node;
        if (getMinNode == null) return null;


        //kleinsten Wurzelknoten eines Binominalbauems entfernen
        for (Node<P, D> tempNode = this.rootnode; tempNode != null; tempNode = tempNode.sibling) {

            //Speziallfall, die Rootnode ist die minimale Node -> Kein Vorgänger ermittlerbar
            if (this.rootnode.equals(getMinNode)) {
                this.rootnode = rootnode.sibling;
            }

            //Vorgänger ermittlen -> mit Sibling des Vorgängers, getMinNode überspringen
            if (tempNode.sibling != null && tempNode.sibling.equals(getMinNode)) {
                tempNode.sibling = getMinNode.sibling;
            }
        }

        // 2
        //Wenn dieser Knoten Nachfolger besitzt:
        //Vereinige die Liste seiner Nachfolger (beginnend mit dem Nachfolger mit dem
        //kleinsten Grad, der über child ® sibling direkt zugreifbar ist)
        //mit der verbleibenden Halde.

        if (getMinNode.child != null) {
            //Zirkuläre Verkettung auflösen
            Node<P, D> Listenanfang = getMinNode.child.sibling;
            (getMinNode.child).sibling = null;
            this.rootnode = mergeHeaps(this.rootnode, Listenanfang);
        }
        //Entry-Node Beziehung löschen

        Entry<P, D> returnValue = getMinNode.entry;

        if (getMinNode.entry != null) {
            getMinNode.entry.node = null;
        }
        getMinNode.entry = null;
        //getMinNode = null;

        return returnValue;
    }


    /**
     * Priorität des Eintrags e auf p verändern.
     * (Dabei darf auf keinen Fall ein neuer Eintrag entstehen, selbst wenn
     * die Operation intern als Entfernen und Neu−Einfügen implementiert wird!)
     *
     * @param e Der zu entfernende Eintrag
     * @param p Die Priorität des Ent
     * @return True, wenn Priorität verändert
     */
    public boolean changePrio(Entry<P, D> e, P p) {

        if (this.rootnode == null || e == null || p == null || e.node == null || !this.contains(e)) return false;

        // 1
        // 	Wenn die neue Priorität des Objekts kleiner oder gleich der alten ist
        if (p.compareTo(e.prio) <= 0) {
            // 1
            // Ändere die Priorität des Objekts
            e.prio = p;

            // 2
            // Solange die Priorität des Objekts kleiner als die seines Vorgängers ist:
            // Vertausche die entry-Verweise der beiden Knoten
            // und aktualisiere die zugehörigen Rückverweise der Objekte auf diese Knoten.
            //TODO Aufbubblen Funktioniert, aber im Auge behalten
            while (e.node.parent != null && e.node.parent.entry != null && e.prio().compareTo(e.node.parent.prio()) < 0) {
                Entry<P, D> childEntry = e;
                Entry<P, D> parentEntry = e.node.parent.entry;
                Node<P, D> eChildNode = e.node;
                Node<P, D> eParentNode = e.node.parent;

                //Vertausche die Einträge miteinander
                eParentNode.entry = childEntry;
                eChildNode.entry = parentEntry;

                //Rückverweise -> Entry.node muss auf die neue Node zeigen
                childEntry.node = eParentNode;
                parentEntry.node = eChildNode;

                //Aktualisiere e Zeiger auf childEntry, welches jetzt oben ist
                //e = childEntry;
                //e.node = eChildNode;

            }
        } else {
            // Andernfalls, sofern sich das Objekt nicht in einem Blattknoten befindet:
            // Entferne das Objekt und füge es mit der neuen Priorität wieder ein.
            if (e.node.child != null) {
                this.remove(e);

                //Einfügen mit neuer Priorität
                e.prio = p;

                //Der Eintrag muss nach Spezifikation erhalten bleiben, deswegen wird die Hilfsmethode InsertWithEntry augerufen, welche keinen neuen Eintrag erzeugt
                this.insertWithEntry(e);
                //anstatt mit this.insert(p, e.data); welche einen neunen Eintrag erzeugen würde
            } else {
                e.prio = p;
            }
        }
        return true;
    }


    /**
     * Hilfsmethode
     * Diese Metode erhält im Gegensatz zu Insert, einen Eintrag als Parameter übergeben
     * Laut Spezifikation darf kein neuer Eintrag bei ChangePrio angelegt werden,
     * sondern der Eintrag erhält in diesem Fall nur einen neuen Knoten
     * <p>
     * Einfügen eines Eintrags mit Priorität p und Daten d
     * Erzeuge eine temporäre Halde mit einem einzigen Baum mit Grad 0, die das Objekt
     * enthält, und vereinige sie mit der aktuellen Halde.
     *
     * @param newEntry der neue einzufügende Eintrag
     * @return Der eingefügte Eintrag
     */
    private Entry<P, D> insertWithEntry(Entry<P, D> newEntry) {
        if (newEntry == null) return null;
        Node<P, D> newHeapRoot = new Node<>(newEntry);
        newEntry.node = newHeapRoot;
        //Rückverweis herstellen
        newHeapRoot.entry = newEntry;
        this.rootnode = mergeHeaps(rootnode, newHeapRoot);

        return newEntry;
    }


    /**
     * Entfernen eines Objekts
     * Bei dieser Methode wird die changePriority und Extract-Min abgeändert eingesetzt
     *
     * @param e Eintrag des zu entfernenden Objekts
     */
    public boolean remove(Entry<P, D> e) {

        if (e == null || this.rootnode == null || e.node == null || !this.contains(e)) return false;

        //1 Ändere die Priorität des Objekts quasi auf −inf.
        //Ist hier nicht direkt umsetzbar, deswegen wird die changePrio Anweisung abgeändert
        // Mit der While-Schleife wird der Eintrag bis zum Parent hochgepusht

        //Vertausche die entry-Verweise der beiden Knoten
        //und aktualisiere die zugehörigen Rückverweise der Objekte auf diese Knoten.
        while (e.node.parent != null && e.node.parent.entry != null) {

            Entry<P, D> childEntry = e;
            Entry<P, D> parentEntry = e.node.parent.entry;
            Node<P, D> eChildNode = e.node;
            Node<P, D> eParentNode = e.node.parent;

            //Vertausche die Einträge miteinander
            eParentNode.entry = childEntry;
            eChildNode.entry = parentEntry;

            //Rückverweise -> Entry.node (das Attribut von Entry) muss auf die neue Node zeigen
            childEntry.node = eParentNode;
            parentEntry.node = eChildNode;
        }

        //2 Führe dann die Operation „Entnehmen“ aus.
        //Die Entnehmen (Extract-Min) Methode wird hier ebenso umgeschreiben, da Prio nicht auf -inf gesetzt werden kann


        // 1
        // Entferne Knoten aus Liste
        Node<P, D> iteratorNode;
        for (iteratorNode = this.rootnode; iteratorNode != null; iteratorNode = iteratorNode.sibling) {

            //Spezialfall entry ist der Wurzelknoten -> Überspringe den Wurzelknoten
            if (this.rootnode.equals(e.node)) {
                this.rootnode = this.rootnode.sibling;
                break;
            }

            //Entferne Knoten aus Liste
            //Überspringe den Eintrag entry, welcher in der Wurzelknotenliste ist.
            if (iteratorNode.sibling != null && iteratorNode.sibling.equals(e.node)) {
                iteratorNode.sibling = e.node.sibling;
                break;
            }
        }

        // 2
        //Wenn dieser Knoten Nachfolger besitzt:
        //Vereinige die Liste seiner Nachfolger (beginnend mit dem Nachfolger mit dem
        //kleinsten Grad, der über child . sibling direkt zugreifbar ist)
        //mit der verbleibenden Halde.
        if (e.node.child != null) {

            //beim Mergen, darf einen Node mit sibling Beziehung nicht auf sich selbst zeigen
            //beim Mergen allgemein ist das Problem, das hier ein Knoten übergeben wird, der eine zirkuläre Liste hat -> Es muss diese zirkuläre liste unterbrochen werden

            Node<P, D> Listenanfang = e.node.child.sibling;
            //Zirkuläre Verkettung auflösen
            (e.node.child).sibling = null;
            this.rootnode = mergeHeaps(this.rootnode, Listenanfang);
        }

        //Entry-Node Beziehung löschen
        (e.node).entry = null;
        e.node = null;
        return true;
    }

    /**
     * Größe der Halde, d. h. Anzahl momentan gespeicherter Einträge ermmitteln
     *
     * @return Anzahl der Einträge in der Halde
     */
    public int size() {
        Node<P, D> iteratingNode;
        int degree;
        int sum = 0;

        //if ( this.rootnode == null) return sum;

        for (iteratingNode = this.rootnode; iteratingNode != null; iteratingNode = iteratingNode.sibling) {
            degree = iteratingNode.degree;

            sum = sum + (int) Math.pow(2, degree);
        }
        return sum;
    }

    /**
     * Ermittelt, ob die Halde leer ist
     *
     * @return True, wenn die Halde leer ist
     */
    public boolean isEmpty() {
        return this.rootnode == null;
    }

    /**
     * Prüft ob ein Eintrag in der Halde enthalten ist
     *
     * @param e Eintrag, bei das Enthaltensein in dieser Halde geprüft wird
     * @return True, wenn der Eintrag in dieser Halde enthalten ist
     */
    public boolean contains(Entry<P, D> e) {
        //Damit funktioniert der rest
        //if ( e == null) return false;
        //return e.node !=null;

        //Neue Contains, diese funktioniert noch nicht richtig, weshalb alle Methoden, welche diese aufrufen auch nicht funktionieren

        //Wenn der Eintrag nicht existiert oder die Node des Eintrags nicht existiert,
		//kann der Eintrag nicht in der Halde vorkommen
		//Wenn die Rootnode null ist, exisiert kein Eintrag in der Halde
		if (e == null || e.node == null || this.rootnode == null) return false;

		//Untersuche ob die Node des Eintrags in der Halde enthalten ist

		//Wandere die Parent-Verknüpfung des Nodes nach oben, sodass die Rootnode des Baumes in einer Halde erreicht wird
		Node<P,D> iteratingParentsNode = e.node;
		while ( iteratingParentsNode.parent != null){
			iteratingParentsNode = iteratingParentsNode.parent;
		}

		//Durchsuche die einfach verkettete Liste nach dieser Node, wenn sie enthalten ist, ist auch der Eintrag e enthalten

        //Vorsicht bei der Bedingung: iteratingNode.sibling != null -> der letzte Punkt wird nicht erreicht, da bei der neuen Node
        //dann silbing null ist und beendet wird ohne diese letzte Node noch zu überprüfen

		for ( Node<P,D> iteratingNodeList = this.rootnode; iteratingNodeList != null ;iteratingNodeList = iteratingNodeList.sibling)
		{
			if ( iteratingNodeList.equals(iteratingParentsNode)){
				return true;
			}

        }

		//Wenn die Rootnode des Baumes, welches den Eintrag e enthält nicht in der verketteten Liste ist,
        //so muss der Eintrag Teil einer anderen Halde sein und ist somit nicht in der aktuellen Halde enthalten
        return false;

    }

    /**
     * Inhalt der Halde zu Testzwecken ausgeben
     */
    public void dump() {
        //Einrückungsstring, wird jede Ebene erweitert
        String intendation = "";

        //Für jeden Wurzelknoten eines Binominalbaumes
        for (Node<P, D> iteratorNode = this.rootnode; iteratorNode != null; iteratorNode = iteratorNode.sibling) {

            //Wurzelknoten ausgeben
            Entry<P, D> iteratorEntry = iteratorNode.entry;
            System.out.println(intendation + iteratorEntry.prio() + " " + iteratorEntry.data());

            //Rerkusive Funktion Baum mit Wurzelknoten iteratorNode ausgeben
            if (iteratorNode.child != null) {
                this.treeDisplay(iteratorNode.child.sibling, intendation, iteratorNode.degree);
            }
        }
    }

    /**
     * Hilfsoperation
     * Rekursive Funktion, welche einen Binominalbaum ausgibt
     */
    private void treeDisplay(Node<P, D> activeNode, String intendation, int cyclicListLength) {
        String intendation2 = intendation + "  ";

        //Rekursionsauflösung
        if (activeNode == null || cyclicListLength == 0) return;


        System.out.println(intendation2 + activeNode.entry.prio() + " " + activeNode.entry.data());

        //Backtrackingschritte
        //Kinder ausgeben
        if (activeNode.child != null) treeDisplay(activeNode.child.sibling, intendation2, activeNode.degree);

        //Alle Kinder abgefrühstückt -> nächste Liste

        treeDisplay(activeNode.sibling, intendation, cyclicListLength - 1);

    }


    // Eintrag einer solchen Warteschlange bzw. Halde, bestehend aus
    // einer Priorität prio mit Typ P und zusätzlichen Daten data mit
    // Typ D.
    // Wenn der Eintrag momentan tatsächlich zu einer Halde gehört,
    // verweist node auf den zugehörigen Knoten eines Binomialbaums
    // dieser Halde.
    public static class Entry<P, D> {
        // Priorität, zusätzliche Daten und zugehöriger Knoten.
        private P prio;
        private D data;
        private Node<P, D> node;

        // Eintrag mit Priorität p und zusätzlichen Daten d erzeugen.
        private Entry(P p, D d) {
            prio = p;
            data = d;
        }

        // Priorität bzw. zusätzliche Daten liefern.
        public P prio() {
            return prio;
        }

        public D data() {
            return data;
        }
    }

    // Knoten eines Binomialbaums innerhalb einer solchen Halde.
    // Neben den eigentlichen Knotendaten (degree, parent, child,
    // sibling), enthält der Knoten einen Verweis auf den zugehörigen
    // Eintrag.
    private static class Node<P, D> {
        // Zugehöriger Eintrag.
        private Entry<P, D> entry;

        // Grad des Knotens.
        private int degree;

        // Vorgänger (falls vorhanden; bei einem Wurzelknoten null).
        private Node<P, D> parent;

        // Nachfolger mit dem größten Grad
        // (falls vorhanden; bei einem Blattknoten null).
        private Node<P, D> child;

        // Zirkuläre Verkettung aller Nachfolger eines Knotens
        // bzw. einfache Verkettung aller Wurzelknoten einer Halde,
        // jeweils sortiert nach aufsteigendem Grad.
        private Node<P, D> sibling;

        // Knoten erzeugen, der auf den Eintrag e verweist
        // und umgekehrt.
        private Node(Entry<P, D> e) {
            entry = e;
            e.node = this;
        }

        // Priorität des Knotens, d. h. des zugehörigen Eintrags
        // liefern.
        private P prio() {
            return entry.prio;
        }
    }


    /**
     * Hilfsmethode
     * Zusammenfassen zweier Bäume B1 und B2 mit Grad k
     * zu einem Baum mit Grad k + 1
     *
     * @param b1 Wurzelknoten des Binominalbaums B1
     * @param b2 Wurzelknoten des Binominalbaums B2
     * @return Wurzelknoten des zusammengefügten Binominalbaums
     */
    private Node<P, D> mergeTree(Node<P, D> b1, Node<P, D> b2) {
        //Wenn die Priorität des Wurzelknotens von B1 größer als die Priorität des Wurzelknotens
        //von B2 ist, mache B1 zum Nachfolger mit dem größten Grad von B2
        if (b1.prio().compareTo(b2.prio()) > 0) {
            b2.sibling = null;
            b2.degree = b2.degree + 1;
            b1.parent = b2;

            if (b2.child == null) {
                b2.child = b1.sibling = b1;
            } else {
                b1.sibling = b2.child.sibling;
                b2.child = b2.child.sibling = b1;
            }
            return b2;
        }
        //2 Andernfalls mache B2 zum Nachfolger mit dem größten Grad von B1
        else {
            b1.sibling = null;
            b1.degree = b1.degree + 1;
            b2.parent = b1;

            if (b1.child == null) {
                b1.child = b2.sibling = b2;
            } else {
                b2.sibling = b1.child.sibling;
                b1.child = b1.child.sibling = b2;
            }
            return b1;
        }
    }

    /**
     * Hilfsmethode
     * Vereinigt zwei Binominalhalden h1 und h2 zur Binominalhalde h
     *
     * @param h1 Erster Wurzelknoten der erste Binominalhalde
     * @param h2 Erster Wurzelknoten der zweite Binominalhalde
     * @return h Der Wurzelknoten der vereinigten Binominalhalde
     */
    private Node<P, D> mergeHeaps(Node<P, D> h1, Node<P, D> h2) {
        //Node<P,D>[] cache = new Node<P, D>[3]; //Generic array creation not allowed

        //Erstelle einen leeren Zwischenspeicher für bis zu drei Bäume / Wurzelknoten.
        ArrayList<Node<P, D>> cache = new ArrayList<>();
        Node<P, D> h = null;
        //Setze k = 0
        int k = 0;

        //Solange H1 oder H2 oder der Zwischenspeicher nicht leer sind
        while ((!cache.isEmpty()) || h1 != null || h2 != null) {
            // h1=null -> h1 !=null = false
            //1
            //Wenn der erste (verbleibende) Baum von H1 Grad k besitzt,
            //entnimm ihn aus H1 und füge ihn zum Zwischenspeicher hinzu.

            if (h1 != null && h1.degree == k) {
                cache.add(h1);
                h1 = h1.sibling;
            }

            //2
            //Entsprechend für H2
            if (h2 != null && h2.degree == k) {
                cache.add(h2);
                h2 = h2.sibling;
            }

            //3
            //Wenn der Zwischenspeicher jetzt einen oder drei Bäume enthält,
            //entnimm einen von ihnen und füge ihn am Ende von H an.
            if (cache.size() == 1 || cache.size() == 3) {
                Node<P, D> tempTree = cache.get(0);
                cache.remove(0);

                //Der Wurzelknoten des Baumes aus dem Zwischenspeicher, kann noch eine Siblingsbeziehung (aus früherer Zeit) haben.
                //Diese muss entfernt werden, wird dies nicht gemacht, so wird beim hinzufügen neuer Bäume, die Siblingsbeziehung beachtet.
                tempTree.sibling = null;

                //Parent-Verweis des Baumes auf null setzen (aus früherer Zeit möglich, das dieser noch einen parentverweis hat -> die würde zu Fehlern der Change-Prio Anweisung führen)
                tempTree.parent = null;

                //Wenn der Heap noch nicht existiert, erstelle ihn
                if (h == null) {
                    h = tempTree;
                }
                //Ansonsten füge ihn am Ende ein
                //Die Wurzelknoten sind einfach verkettet
                else {
                    Node<P, D> treeToInsert;
                    for (treeToInsert = h; true; treeToInsert = treeToInsert.sibling) {    //true = treeToInsert != null
                        //An das Ende der Liste gehen und den Baum einfügen
                        if (treeToInsert.sibling == null) {                //Darunterstehendes nach oben ausgelagert! Es ist besser den einzufügenden Baum von vorneherein "Sauber" zu halten
                            treeToInsert.sibling = tempTree;            //tempTree == a -> null //treeToInsert = g->e->null XX Sollte so sein, aber e -> a schon bevor die Zeile ausgeführt wird
                            //Extrem wichtig, der tempTree (Ursprünglicher Baum) kann davor schon eine siblings-Beziehung zu einem anderen Baum haben, diese muss gekappt werden	//tempTree.sibling = null;
                            break;
                        }
                    }
                }
            }

            //4
            //Wenn der Zwischenspeicher jetzt noch zwei Bäume enthält,
            //fasse sie zu einem Baum mit Grad k + 1 zusammen,
            //der als „Übertrag“ für den nächsten Schritt im Zwischenspeicher verbleibt.
            if (cache.size() == 2) {
                Node<P, D> mergedTree = this.mergeTree(cache.get(0), cache.get(1));
                cache.remove(0);
                cache.remove(0);
                cache.add(mergedTree);
            }

            //5 Erhöhe k um 1
            k = k + 1;

        }
        return h;
    }


}
//ENDE Binoniminal Heap
/*


// Interaktives Testprogramm für die Klasse BinHeap.
class BinHeapTest {
    public static void main (String [] args) throws java.io.IOException {
	// Leere Halde mit Prioritäten des Typs String und zugehörigen
	// Daten des Typs Integer erzeugen.
	// (Die Implementierung muss aber natürlich auch mit anderen
	// Typen funktionieren.)
	BinHeap<String, Integer> heap = new BinHeap<String, Integer>();

	// Feld mit allen eingefügten Einträgen, damit sie später
	// für remove und changePrio verwendet werden können.
	// Achtung: Obwohl die Klasse BinHeap ebenfalls Typparameter
	// besitzt, schreibt man "BinHeap.Entry<String, Integer>" und
	// nicht "BinHeap<String, Integer>.Entry<String, Integer>".
	// Achtung: "new BinHeap.Entry [100]" führt zu einem Hinweis
	// über "unchecked or unsafe operations"; die eigentlich "korrekte"
	// Formulierung "new BinHeap.Entry<String, Integer> [100]"
	// führt jedoch zu einem Übersetzungsfehler!
	BinHeap.Entry<String, Integer> [] entrys = new BinHeap.Entry [100];

	// Anzahl der bis jetzt eingefügten Einträge.
	int n = 0;

	// Standardeingabestrom System.in als InputStreamReader
	// und diesen wiederum als BufferedReader "verpacken",
	// damit man bequem zeilenweise lesen kann.
	java.io.BufferedReader r = new java.io.BufferedReader(
			    new java.io.InputStreamReader(System.in));

	// Endlosschleife.
	while (true) {
	    // Inhalt und Größe der Halde ausgeben.
	    heap.dump();
	    System.out.println(heap.size() + " entry(s)");

	    // Eingabezeile vom Benutzer lesen, ggf. ausgeben (wenn das
	    // Programm nicht interaktiv verwendet wird) und in einzelne
	    // Wörter zerlegen.
	    // Abbruch bei Ende der Eingabe oder leerer Eingabezeile.
	    System.out.print(">>> ");
	    String line = r.readLine();
	    if (line == null || line.equals("")) return;
	    if (System.console() == null) System.out.println(line);
	    String [] cmd = line.split(" ");

	    // Fallunterscheidung anhand des ersten Worts.
	    switch (cmd[0]) {
	    case "+": // insert prio
		// Die laufende Nummer n wird als zusätzliche Daten
		// verwendet.
		entrys[n] = heap.insert(cmd[1], n);
		n++;
		break;
	    case "-": // remove entry
		heap.remove(entrys[Integer.parseInt(cmd[1])]);
		break;
	    case "?": // minimum
		BinHeap.Entry<String, Integer> e = heap.minimum();
		System.out.println("--> " + e.prio() + " " + e.data());
		break;
	    case "!": // extractMin
		e = heap.extractMin();
		System.out.println("--> " + e.prio() + " " + e.data());
		break;
	    case "=": // changePrio entry prio
		heap.changePrio(entrys[Integer.parseInt(cmd[1])], cmd[2]);
		break;
	    }
	}
    }
}



*/


//ERWEITERTES TESTPROGRAMM
//Befehle	Insert 				+ a
//			Remove				- 1
// 			Minimum				?
//			Extract Minimum		!
//			Change Prio			= 1 a
//			Is Emtpy			#
//			Contains			& 1

// a Steht für beliebige Prio in diesem Fall Buchstaben
// 1 Steht für beliebigen Eintrag, in diesem Fall Zahlen (ArrayIndizes)

// Interaktives Testprogramm für die Klasse BinHeap.
class BinHeapTest {
    public static void main(String[] args) throws java.io.IOException {
        // Leere Halde mit Prioritäten des Typs String und zugehörigen
        // Daten des Typs Integer erzeugen.
        // (Die Implementierung muss aber natürlich auch mit anderen
        // Typen funktionieren.)
        BinHeap<String, Integer> heap = new BinHeap<String, Integer>();

        // Feld mit allen eingefügten Einträgen, damit sie später
        // für remove und changePrio verwendet werden können.
        // Achtung: Obwohl die Klasse BinHeap ebenfalls Typparameter
        // besitzt, schreibt man "BinHeap.Entry<String, Integer>" und
        // nicht "BinHeap<String, Integer>.Entry<String, Integer>".
        // Achtung: "new BinHeap.Entry [100]" führt zu einem Hinweis
        // über "unchecked or unsafe operations"; die eigentlich "korrekte"
        // Formulierung "new BinHeap.Entry<String, Integer> [100]"
        // führt jedoch zu einem Übersetzungsfehler!
        BinHeap.Entry<String, Integer>[] entrys = new BinHeap.Entry[100];

        // Anzahl der bis jetzt eingefügten Einträge.
        int n = 0;

        // Standardeingabestrom System.in als InputStreamReader
        // und diesen wiederum als BufferedReader "verpacken",
        // damit man bequem zeilenweise lesen kann.
        java.io.BufferedReader r = new java.io.BufferedReader(
                new java.io.InputStreamReader(System.in));

        // Endlosschleife.
        while (true) {
            // Inhalt und Größe der Halde ausgeben.
            heap.dump();
            System.out.println(heap.size() + " entry(s)");

            // Eingabezeile vom Benutzer lesen, ggf. ausgeben (wenn das
            // Programm nicht interaktiv verwendet wird) und in einzelne
            // Wörter zerlegen.
            // Abbruch bei Ende der Eingabe oder leerer Eingabezeile.
            System.out.print(">>> ");
            String line = r.readLine();
            if (line == null || line.equals("")) return;
            if (System.console() == null) System.out.println(line);
            String[] cmd = line.split(" ");

            // Fallunterscheidung anhand des ersten Worts.
            switch (cmd[0]) {
                case "+": // insert prio
                    // Die laufende Nummer n wird als zusätzliche Daten
                    // verwendet.
                    entrys[n] = heap.insert(cmd[1], n);
                    n++;
                    break;
                case "-": // remove entry
                    System.out.println(heap.remove(entrys[Integer.parseInt(cmd[1])]));
                    break;
                case "?": // minimum
                    BinHeap.Entry<String, Integer> e = heap.minimum();
                    //System.out.println("--> " + e.prio() + " " + e.data());
                    System.out.println(e);
                    break;
                case "!": // extractMin
                    e = heap.extractMin();
                    //System.out.println("--> " + e.prio() + " " + e.data());
                    System.out.println(e);
                    break;
                case "=": // changePrio entry prio
                    System.out.println(heap.changePrio(entrys[Integer.parseInt(cmd[1])], cmd[2]));
                    break;
                case "#": //is empty
                    System.out.println(heap.isEmpty());
                    break;
                case "&": // contains
                    System.out.println(heap.contains(entrys[Integer.parseInt(cmd[1])]));
                    break;

            }
        }
    }
}


/*
class BinHeapTest {
    public static void main (String [] args) throws java.io.IOException {
        // Leere Halde mit Prioritäten des Typs String und zugehörigen
        // Daten des Typs Integer erzeugen.
        // (Die Implementierung muss aber natürlich auch mit anderen
        // Typen funktionieren.)
        BinHeap<String, Integer> heap1 = new BinHeap<String, Integer>();
        BinHeap<String, Integer> heap2 = new BinHeap<String, Integer>();
        BinHeap<String, Integer> heap3 = new BinHeap<String, Integer>();
        // Feld mit allen eingefügten Einträgen, damit sie später
        // für remove und changePrio verwendet werden können.
        // Achtung: Obwohl die Klasse BinHeap ebenfalls Typparameter
        // besitzt, schreibt man "BinHeap.Entry<String, Integer>" und
        // nicht "BinHeap<String, Integer>.Entry<String, Integer>".
        // Achtung: "new BinHeap.Entry [100]" führt zu einem Hinweis
        // über "unchecked or unsafe operations"; die eigentlich "korrekte"
        // Formulierung "new BinHeap.Entry<String, Integer> [100]"
        // führt jedoch zu einem Übersetzungsfehler!
        BinHeap.Entry<String, Integer> [] entrys = new BinHeap.Entry [100];

        // Anzahl der bis jetzt eingefügten Einträge.
        int n = 0;

        // Standardeingabestrom System.in als InputStreamReader
        // und diesen wiederum als BufferedReader "verpacken",
        // damit man bequem zeilenweise lesen kann.
        java.io.BufferedReader r = new java.io.BufferedReader(
                new java.io.InputStreamReader(System.in));

        // Endlosschleife.
        while (true) {

            // Inhalt und Größe der Halde ausgeben.
            heap1.dump();
            System.out.println(heap1.size() + " entry(s)");

            heap2.dump();
            System.out.println(heap2.size() + " entry(s)");

            heap3.dump();
            System.out.println(heap3.size() + " entry(s)");

            // Eingabezeile vom Benutzer lesen, ggf. ausgeben (wenn das
            // Programm nicht interaktiv verwendet wird) und in einzelne
            // Wörter zerlegen.
            // Abbruch bei Ende der Eingabe oder leerer Eingabezeile.
            System.out.print(">>> ");
            String line = r.readLine();
            if (line == null || line.equals("")) return;
            if (System.console() == null) System.out.println(line);
            String [] cmd = line.split(" ");

            // Fallunterscheidung anhand des ersten Worts.
            switch (cmd[0]) {
                //-------------------------------------------------Heap 1 --------------------------------------------------
                case "+1": // insert prio
                    // Die laufende Nummer n wird als zusätzliche Daten
                    // verwendet.
                    entrys[n] = heap1.insert(cmd[1], n);
                    n++;
                    break;
                case "-1": // remove entry
                    heap1.remove(entrys[Integer.parseInt(cmd[1])]);
                    break;
                case "?1": // minimum
                    BinHeap.Entry<String, Integer> e1 = heap1.minimum();
                    if (e1 != null) System.out.println("--> " + e1.prio() + " " + e1.data());
                    break;
                case "!1": // extractMin
                    e1 = heap1.extractMin();
                    //System.out.println("--> " + e.prio() + " " + e.data());
                    break;
                case "=1": // changePrio entry prio
                    heap1.changePrio(entrys[Integer.parseInt(cmd[1])], cmd[2]);
                    break;
                case "#1": //is empty
                    System.out.println(heap1.isEmpty());
                    break;
                case "&1": // contains
                    System.out.println(heap1.contains(entrys[Integer.parseInt(cmd[1])]));
                    break;
                    //------------------------------------------------Heap 2---------------------------------------
                case "+2": // insert prio
                    // Die laufende Nummer n wird als zusätzliche Daten
                    // verwendet.
                    entrys[n] = heap2.insert(cmd[1], n);
                    n++;
                    break;
                case "-2": // remove entry
                    heap2.remove(entrys[Integer.parseInt(cmd[1])]);
                    break;
                case "?2": // minimum
                    BinHeap.Entry<String, Integer> e2 = heap2.minimum();
                    System.out.println("--> " + e2.prio() + " " + e2.data());
                    break;
                case "!2": // extractMin
                    e2 = heap1.extractMin();
                    System.out.println("--> " + e2.prio() + " " + e2.data());
                    break;
                case "=2": // changePrio entry prio
                    heap2.changePrio(entrys[Integer.parseInt(cmd[1])], cmd[2]);
                    break;
                case "#2": //is empty
                    System.out.println(heap2.isEmpty());
                    break;
                case "&2": // contains
                    System.out.println(heap2.contains(entrys[Integer.parseInt(cmd[1])]));
                    break;
                    //-------------------------------------------------Heap 3 ------------------------------------------------
                case "+3": // insert prio
                    // Die laufende Nummer n wird als zusätzliche Daten
                    // verwendet.
                    entrys[n] = heap3.insert(cmd[1], n);
                    n++;
                    break;
                case "-3": // remove entry
                    heap3.remove(entrys[Integer.parseInt(cmd[1])]);
                    break;
                case "?3": // minimum
                    BinHeap.Entry<String, Integer> e3 = heap3.minimum();
                    System.out.println("--> " + e3.prio() + " " + e3.data());
                    break;
                case "!3": // extractMin
                    e3 = heap1.extractMin();
                    if(e3 != null) System.out.println("--> " + e3.prio() + " " + e3.data());
                    break;
                case "=3": // changePrio entry prio
                    heap3.changePrio(entrys[Integer.parseInt(cmd[1])], cmd[2]);
                    break;
                case "#3": //is empty
                    System.out.println(heap3.isEmpty());
                    break;
                case "&3": // contains
                    System.out.println(heap3.contains(entrys[Integer.parseInt(cmd[1])]));
                    break;

            }
        }
    }
}
*/