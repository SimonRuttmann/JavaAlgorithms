// Schnittstelle einer Streuwertfunktion mit Einschränkung des
// Wertebereichs, die zu jedem Schlüssel einen gültigen Streuwert/Index
// liefert.
interface HashFunction {
    // Größe N der Streuwerttabelle (Anzahl der Plätze).
    int size ();

    // Streuwert für Schlüssel key liefern, indem key.hashCode()
    // berechnet und geeignet in den Wertebereich von 0 bis N-1
    // abgebildet wird.
    int compute (Object key);
}




// Abstrakte Oberklasse für Implementierungen von Streuwertfunktionen.
abstract class AbstractHashFunction implements HashFunction {
    // Größe der Streuwerttabelle (Anzahl der Plätze).
    protected int size;

    // Größe mit N initialisieren.
    protected AbstractHashFunction (int N) {
        size = N;
    }

    // Größe liefern.
    public int size () {
        return size;
    }
}


// Streuwertfunktion gemäß Divisionsrestmethode.
class DivisionMethod extends AbstractHashFunction {

    @Override
    public int compute(Object key) {
        int key_hash = key.hashCode();
        return (key_hash % this.size + this.size)% this.size;         //Mathe richtig
        //return (key_hash >= 0 ? key_hash : -key_hash ) %this.size;      //Skript richtig gleiches bei Multiplikationsmethode compute
    }

    // Divisionsrestmethode für Tabellengröße N.
    public DivisionMethod (int N) {
        super(N);
    }

}



// Streuwertfunktion gemäß Multiplikationsmethode
// (Implementierung mit 32-Bit-Ganzzahlarithmetik).
class MultiplicationMethod extends AbstractHashFunction {
    // Anzahl p von Bits.
    private int bits;

    @Override
    public int compute(Object key) {
        int key_hash = key.hashCode();
        return (key_hash * this.seed) >>> (32 - this.bits);
    }

    // Parameter s = A'.
    private int seed;

    // Multiplikationsmethode für Tabellengröße N = 2 hoch p
    // mit Parameter s.
    public MultiplicationMethod (int p, int s) {
        super(1 << p);	// 1 << p entspricht 2 hoch p.
        bits = p;
        seed = s;
    }
}



// Schnittstelle einer Sondierungsfunktion für offene Adressierung,
// die zu jedem Schlüssel eine gültige Sondierungssequenz liefert.
interface HashSequence {
    // Größe N der Streuwerttabelle (Anzahl der Plätze).
    int size ();

    // Ersten bzw. nächsten Streuwert für Schlüssel key liefern.
    // Alle Werte liegen zwischen 0 und N-1.
    // Ein Aufruf von first und N-1 nachfolgende Aufrufe von next
    // liefern jeden Wert zwischen 0 und N-1 jeweils genau einmal.
    int first (Object key);
    int next ();
}




// Abstrakte Oberklasse für Implementierungen von Sondierungsfunktionen.
abstract class AbstractHashSequence implements HashSequence {
    // Zugrundeliegende Streuwertfunktion.
    protected HashFunction func;

    // Letzter von first oder next gelieferter Streuwert.
    protected int prev;

    // My j
    protected int j;

    // Streuwertfunktion mit f initialisieren.
    protected AbstractHashSequence (HashFunction f) {
        func = f;
    }

    // Tabellengröße liefern.
    public int size () {
        return func.size();
    }

    @Override
    public int first(Object key) {
        this.prev = func.compute(key); //returns Complained Hashcode via multipilcation or division method
        this.j = 0;
        return this.prev;
    }
}



// Sondierungssequenz gemäß linearer Sondierung.
class LinearProbing extends AbstractHashSequence {
    // Lineare Sondierung mit Streuwertfunktion f.
    public LinearProbing (HashFunction f) {
        super(f);
    }
    // Hallo -> 234234  ->  prev 2432 -> next 2433 -> next 2434
    // World -> 3243w23424 -> prev 23233 -> next 23234 -> ...

    @Override
    public int next() {
        this.prev =  (this.prev + 1 ) % this.size();
        return this.prev;
    }
}


// Sondierungssequenz gemäß quadratischer Sondierung
// (Implementierung nur mit Ganzzahlarithmetik).
class QuadraticProbing extends AbstractHashSequence {

    //word first -> 23 -> next 24 -> next 26 -> next 29

    //word first -> 23 -> next 24


    //1. Aufruf +1
    //2. Aufruf +2
    //3. Aufruf +3

    //Test -> 12 -> next 12 +1 = 13 -> next (2.mal) = 12 +3 = 13(prev) +2
    @Override
    public int next() {
        this.j = this.j +1 ;
        this.prev = (prev + this.j)%this.size();
        return this.prev;
    }

    // Quadratische Sondierung mit Streuwertfunktion f.
    public QuadraticProbing (HashFunction f) {
        super(f);
    }
}



// Sondierungssequenz gemäß doppelter Streuung.
class DoubleHashing extends AbstractHashSequence {
    // Zweite Streuwertfunktion.
    private HashFunction func2;
    private int hashcode_complaint1;
    private int hashcode_complaint2;
    // 1. Aufruf  test -> 345 -> hc1 = 213 und hc2 = 2 return 213
    // 1. NextAufruf test -> 213 (1. HC) + 1* 2 (2. HC)
    // 1. NextAufruf test -> 213 (1. HC) + 1* 2 (2. HC)
    // 1. NextAufruf test -> 213 (1. HC) + 1* 2 (2. HC)
    // 1. NextAufruf test -> 213 (1. HC) + 1* 2 (2. HC)


    @Override
    public int next() {
        j = j+1;
        this.prev = (hashcode_complaint1 + j*hashcode_complaint2)%this.size();
        return this.prev;
    }

    // Doppelte Streuung mit Streuwertfunktionen f1 und f2.
    public DoubleHashing (HashFunction f1, HashFunction f2) {
        super(f1);
        func2 = f2;
    }

    @Override
    public int first(Object key) {
        super.first(key);   //Hashcode 1 berechnen und in prev speichern
        this.hashcode_complaint1 = func.compute(key);
        this.hashcode_complaint2 = func2.compute(key);
        this.j = 0;
        return this.prev;
    }
}


// Schnittstelle einer Streuwerttabelle.
interface HashTable {
    // Eintrag mit Schlüssel key und Wert val einfügen, sofern key und
    // val nicht null sind und die Tabelle bei offener Adressierung
    // noch nicht voll ist.
    // Wenn es bereits einen Eintrag mit dem gleichen Schlüssel gibt,
    // wird er durch diesen neuen Eintrag ersetzt.
    // Resultatwert true, falls erfolgreich (Eintrag hinzugefügt oder
    // ersetzt), sonst false (Tabelle unverändert).
    // Bei Verkettung muss am Anfang der jeweiligen Liste eingefügt
    // werden.
    // Die als Schlüssel verwendeten Objekte müssen konsistente
    // Implementierungen von hashCode und equals besitzen, d. h.
    // Objekte, die gemäß equals "gleich" sind, müssen den gleichen
    // hashCode-Wert besitzen. Außerdem darf sich der hashCode-Wert
    // eines Schlüssels nicht ändern, solange es einen Tabelleneintrag
    // mit diesem Schlüssel gibt.
    // key und val können jeweils einen beliebigen dynamischen Typ
    // besitzen.
    boolean put (Object key, Object val);

    // Wert zum Schlüssel key liefern, falls vorhanden, sonst null.
    // (Resultatwert null, wenn key gleich null ist.)
    Object get (Object key);

    // Eintrag mit Schlüssel key entfernen, falls vorhanden.
    // Resultatwert true, falls erfolgreich (Eintrag war vorhanden),
    // sonst false (Eintrag war nicht vorhanden).
    // (Resultatwert false, wenn key gleich null ist.)
    boolean remove (Object key);

    // Inhalt der Tabelle zu Testzwecken ausgeben:
    // Pro Eintrag eine Zeile bestehend aus der Nummer des Platzes,
    // Schlüssel und Wert, jeweils getrennt durch genau ein Leerzeichen.
    // Dieses Ausgabeformat muss exakt eingehalten werden.
    // Leere Plätze und Plätze mit Löschmarkierung werden nicht
    // ausgegeben.
    void dump ();

}




// Implementierung von Streuwerttabellen mit Verkettung.
class HashTableChaining implements HashTable {
    private HashFunction func;
    private Element[] tab;

    @Override
    public boolean put(Object key, Object val) {
        //Key oder Value sind null
        if (key == null || val == null) return false;
        Element neuesElem = new Element(key, val);

        //Berechne Index
        int index_insert = func.compute(key);

        //Freies Platz in der Tabelle -> Füge hinzu
        if (tab[index_insert] == null){
            tab[index_insert] = neuesElem;
            return true;
        }

        //Platz belegt, druchsuche die Liste nach einem Element mit dem gleichen Schlüssel
        //TO beim erstezen von existing durch neuesElem zeigen die alten Elemente noch auf das alte existing
        if (tab[index_insert] != null){

            Element existing;
            Element prev = null;
            for (existing = tab[index_insert]; existing.next != null; existing = existing.next){
                //Wenn Element mit gleichem Key gefunden, ersetzet es (Next Zeiger wird übernommen)
                if ( existing.key.equals(neuesElem.key) ) {
                    neuesElem.next = existing.next;
                    existing = neuesElem;
                    //TO vorherigen Zeiger auf existing zeigen lassen!
                    if ( prev != null){
                        prev.next = existing;
                    }
                    //TO erstes Element ersetzen -> existing (Element 498) = neuesElement (Element 503), aber Tab binhaltet danach noch 498 !
                    if ( prev == null){
                        tab[index_insert] = existing;
                    }
                    return true;
                }
                prev = existing;
            }
            //Letztes Element in der Schleife wird nicht erreicht, da das letzte Element ein Nullelement als next hat -> existing.next == null
            if ( existing.key.equals(neuesElem.key) ) {
                existing = neuesElem;
                if ( prev != null) {
                    prev.next = existing;
                }
                if ( prev == null){
                    tab[index_insert] = existing;
                }
                return true;
            }

            //Kein Element mit gleichem Key vorhanden, neues Element am Listenanfang einfügen
            // Hier wird ein Element nicht ersetzt sondern neu eingefügt -> Next Zeiger muss gesetzt werden
            if(get(key) == null){
                Element e = tab[index_insert];
                tab[index_insert] = neuesElem;
                neuesElem.next = e;
                return true;
            }

        }

        return false;
    }

    @Override
    public Object get(Object key) {
        if ( key == null) return null;
        int index_search = func.compute(key);
        //debugg
        if (tab[index_search] == null) return null;

        //Suche Element in der Liste
        Element searchElem;
        for (searchElem = tab[index_search]; searchElem.next != null; searchElem = searchElem.next){
            if ( searchElem.key.equals(key)){
                return searchElem.value;
            }
        }
        //Letztes Element muss seperat geprüft werden, da letzes Element kein .next hat
        if ( searchElem.key.equals(key)){
            return searchElem.value;
        }

        return null;

    }

    @Override
    public boolean remove(Object key) {
        if (key == null) return false;

        int index_remove = func.compute(key);
        Element prevElem = tab[index_remove];
        Element remElem;
        if(tab[index_remove] == null) return false;

        for (remElem = tab[index_remove]; remElem.next != null; remElem = remElem.next) {
            if (remElem.key.equals(key)) {

                //Erstes Element ist das zu entfernende -> Setze nächstes Element als Listenanfang
                if ( prevElem.equals(tab[index_remove])) {
                    tab[index_remove] = remElem.next;
                    return true;
                }

                //Anderes Element ist das zu entfernende -> Überspringe das Element
                prevElem.next = remElem.next;
                return true;
            }
            prevElem = remElem;
        }

        //TO "Letzes und Erstes" Element soll entfernt werden -> for schleife wird nicht erreicht, da 1. Element
        //und da erstes Element ist prevElem gleich dem 1. Elem
        if(tab[index_remove].key.equals(key)){
            tab[index_remove] = null;
            return true;
        }

        //TO Letzes Element wird nicht erreicht, da letzees Element.next == null
        // in prevElem vorletzes Elem
        // in remELem letzes Elem
        if (remElem.key.equals(key)){
            prevElem.next = null;   //Vorletztes Element überspringt das letzte Element -> Zeigt auf null.
            return true;
        }




        //Kein Element gefunden
        return false;
    }
    // Inhalt der Tabelle zu Testzwecken ausgeben:
    // Pro Eintrag eine Zeile bestehend aus der Nummer des Platzes,
    // Schlüssel und Wert, jeweils getrennt durch genau ein Leerzeichen.
    // Dieses Ausgabeformat muss exakt eingehalten werden.
    // Leere Plätze und Plätze mit Löschmarkierung werden nicht
    // ausgegeben.
    @Override
    public void dump() {
        for (int i = 0; i < tab.length; i++) {
            if(tab[i] != null)
            {
                Element ausgabe;
                //Liste (außer Letzes Elem) ausgeben
                for (ausgabe = tab[i]; ausgabe.next != null; ausgabe = ausgabe.next) {
                    if (ausgabe.value != null && ausgabe.key != null)
                        System.out.println(i + " " + ausgabe.key.toString() + " " + ausgabe.value.toString());
                }
                //letzes Element ausgeben
                if (ausgabe.value != null && ausgabe.key != null)
                    System.out.println(i + " " + ausgabe.key.toString() + " " + ausgabe.value.toString());
            }
        }
    }

    // Streuwerttabelle mit Streuwertfunktion f.
    public HashTableChaining (HashFunction f) {
        //Aufruf mit Multiplikatinonsmethoden Objekt oder Divisionsmethoden Objekt
        this.func = f;
        this.tab = new Element[f.size()];
    }
}

// Implementierung von Streuwerttabellen mit offener Adressierung.
class HashTableOpenAddressing implements HashTable {

    private HashSequence s;
    private Element[] tab;

    // Streuwerttabelle mit Sondierungsfunktion s.
    public HashTableOpenAddressing (HashSequence s) {
        this.s = s;
        tab = new Element[s.size()];
    }

    //Element Key 234234 Value Hallo -> first index , wenn frei -> setzen
    //                                -> solange next, bis wir des setzen können
    @Override
    public boolean put(Object key, Object val) {
        if (key == null) return false;
        if (val == null) return false;
        Element putElem = new Element ( key, val);

        HelpElem help = helpoperation(key);
        if ( help.index != s.size()){
            tab[help.index] = putElem;
            return true;
        }

        //Tabelle voll, Ausgabe!?!?
        if ( help.index == s.size() ) {
            return false;
        }

        return false;
    }


    @Override
    public Object get(Object key) {
        if ( key == null) return null;
        HelpElem help = helpoperation(key);
        if ( help.index != s.size() && help.status == 1) return tab[help.index].value;
        return null;
    }

    @Override
    public boolean remove(Object key) {
        if ( key == null) return false;
        HelpElem help = helpoperation(key);
        if ( help.index != s.size() && help.status == 1){
            tab[help.index].deleted = true;
            return true;
        }
        return false;
    }

    @Override
    public void dump() {
        for (int i = 0; i < tab.length; i++){
            Element ausgabe = tab[i];
            if ( ausgabe != null && ausgabe.value != null && ausgabe.key != null && !ausgabe.deleted){
                System.out.println(i + " " + ausgabe.key.toString() + " " + ausgabe.value.toString());
            }
        }
    }

    // Status
    // 1 == vorhanden
    // 2 == nicht vorhanden
    // 3 == Tabelle voll
    /**
     @return
     1. param = index (Index = s.size() == ungültig)
     2. param = Status
     1 = vorhanden,
     2 = nicht vorhanden,
     3 = tabelle voll
     */
    public HelpElem helpoperation(Object key){
        int merkeIndex = s.size();

        // 1
        int index = s.size();
        for ( int j = 0; j < s.size(); j++){
            //Schritt 1
            //index noch nicht gesetzt
            if(index == s.size()){
                index = s.first(key);
            }
            //weitere male index setzen
            else{
                index = s.next();
            }

            //Schritt 2
            if ( tab[index] == null ){
                if(merkeIndex != s.size()) return new HelpElem(merkeIndex, 2);
                if(merkeIndex == s.size()) return new HelpElem(index, 2);
            }

            //Schritt 3
            if ( tab[index].deleted && merkeIndex == s.size()) merkeIndex = index;

            //Schritt 4
            if (tab[index] != null && tab[index].key.equals(key) && !tab[index].deleted) return new HelpElem(index, 1);

        }
        //2
        if(merkeIndex != s.size() ) return new HelpElem(merkeIndex, 2);

        //3
        return new HelpElem(s.size(), 3);
    }
}



class HelpElem {
    public int index;
    public int status;

    public HelpElem(int index, int status){
        this.index = index;
        this.status = status;
    }
}

class Element {
    public Object key;
    public Object value;
    public Element next;    //notwendig für Verkettete HashMap
    public boolean deleted;

    public Element(Object key, Object value){
        this.value = value;
        this.key = key;
    }

}





// Einfaches Testprogramm.
class WordCount {
    // Aufruf mit folgenden Kommandozeilenargumenten:
    // 1) Buchstabe c (chaining), l (linear probing),
    //    q (quadratic probing) oder d (double hashing).
    // 2) Buchstabe d (Divisionsrestmethode)
    //    oder m (Multiplikationsmethode).
    // 3) Größe N der Streuwerttabelle (bei Divisionsrestmethode)
    //    oder Anzahl p von Bits (bei Multiplikationsmethode).
    // 4) Parameter s (nur bei Multiplikationsmethode).
    public static void main (String [] args) throws java.io.IOException {
        // Größe N der Streuwerttabelle oder Anzahl p von Bits.
        int Np = Integer.parseInt(args[2]);

        // Streuwertfunktion mit Einschränkung des Wertebereichs
        // gemäß Divisionsrest- oder Multiplikationsmethode.
        HashFunction f;
        switch (args[1]) {
            case "d":
                f = new DivisionMethod(Np);
                break;
            case "m":
                int s = Integer.parseInt(args[3]);
                f = new MultiplicationMethod(Np, s);
                break;
            default:
                return;
        }

        // Streuwerttabelle mit Verkettung oder offener Adressierung.
        HashTable tab;
        if (args[0].equals("c")) {
            tab = new HashTableChaining(f);
        }
        else {
            HashSequence s;
            switch (args[0]) {
                case "l":
                    s = new LinearProbing(f);
                    break;
                case "q":
                    s = new QuadraticProbing(f);
                    break;
                case "d":
                    // Als zweite Streuwertfunktion für doppelte Streuung
                    // wird eine Funktion verwendet, die immer eine
                    // ungerade Zahl zwischen 1 und N-1 liefert.
                    // Wenn die Tabellengröße N dann entweder eine Primzahl
                    // (ratsam bei Divisionsrestmethode) oder eine
                    // Zweierpotenz (automatisch bei Multiplikationsmethode)
                    // ist, sind alle Werte der Funktion teilerfremd zu N.
                    class HashFunction2 extends AbstractHashFunction {
                        public HashFunction2 (int N) {
                            super(N);
                        }
                        public int compute (Object key) {
                            int h = Math.abs(key.hashCode()) % (size - 1);
                            if (h % 2 == 0) h++;
                            return h;
                        }
                    }
                    s = new DoubleHashing(f, new HashFunction2(f.size()));
                    break;
                default:
                    return;
            }
            tab = new HashTableOpenAddressing(s);
        }

        // Standardeingabestrom System.in als InputStreamReader
        // und diesen wiederum als BufferedReader "verpacken",
        // damit man bequem zeilenweise lesen kann.
        java.io.BufferedReader r = new java.io.BufferedReader(
                new java.io.InputStreamReader(System.in));

        // Standardeingabe zeilenweise lesen
        // (Annahme: ein Wort pro Zeile).
        // (readLine kann java.io.IOException werfen.)
        while (true) {
            String word = r.readLine();
            if (word == null || word.equals("")) break;

            //Original
            //if (word == null) break;

            // Überprüfen, ob die Tabelle bereits einen Eintrag mit
            // Schlüssel word enthält.
            // Wenn nicht, wird word mit Wert 1 neu eingetragen.
            // Ansonsten wird der vorhandene Eintrag durch einen neuen
            // Eintrag mit altem Wert plus 1 ersetzt.
            // (int-Werte werden bei Bedarf automatisch in Integer-
            // Objekte umgewandelt und umgekehrt.)

            Integer count = (Integer)tab.get(word);
            if (count == null) count = 0;
            tab.put(word, count + 1);
        }


       // Object keytoremove = "eins";
       // System.out.println(tab.remove(keytoremove));

        //Object keytoremove2 = "eins";
        //System.out.println(tab.remove(keytoremove2));
        //System.out.println(tab.get(keytoremove2));
        //tab.put("eins", 89);
        //tab.put("zwei", 100);
        //tab.put("acht", 100);
        //tab.put("neu", 100);
        //tab.put("neu", 100);

        //Object keytoremove3 = "drei";
        //System.out.println(tab.remove(keytoremove3));

        // Inhalt der Tabelle ausgeben.
        tab.dump();

    }
}

// die get methode gibt momentan den key zurück, nicht den value, deswegen fehler beim casten eines strings zum integer

