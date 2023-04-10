import java.util.Arrays;


// Knoten für den Huffman-Trie
class HNode{
	// chars enthält bei Blattknoten ein Zeichen, ansonsten alle Zeichen der darunterliegenden Knoten
	// Beispiel:
	// 			ab
	//         /  \
	//        a    b
	public String chars;
	// Linkes Kind
	public HNode leftChild;
	// Rechtes Kind
	public HNode rightChild;
	// Kodewort
	public String codeWord;
}
class Huffman {
	enum Direction {left, right, root}

	// Feld mit Huffman-Codes zu den einzelnen Zeichen.
	// Wenn char c = 'a', dann ist codes[c] ein Code, der aus Nullen und Einsen besteht, mit dem das Zeichen a kodiert werden soll.
	private String[] codes;

	// Wurzelknoten des Präfix-Codebaums
	private HNode root;


//Variablen initialisieren
	// Konstruktor
	public Huffman() {
		this.codes = null;		//
		this.root = null;		//
	}
	
	// Prüfen, ob ein Text mit dem aktuell erstellten Huffman-Code kodiert werden kann, ob also alle Zeichen einen Präfix-Code besitzen. Wenn ja, return true, wenn nein, return false.
	public boolean canEncode(String text){
		//Fehlerabfrage
		if ( text == null ) {
			System.out.println("[canEncode] Kein Text übergeben");
			return false;
		}

		if ( codes == null) {
			System.out.println("[canEncode] Keine Kodierung vorhanden" );
			return false;
		}

		char[] chars = text.toCharArray();

		try {
			for (char character : chars) {
				if (codes[character] == null)
					return false;
			}
		} catch (IndexOutOfBoundsException e){
			System.out.println("[encode] Es sind Zeichen enthalten, welche nicht in einer ASCII-Tabelle mit 256 Zeichen dargestellt werden kann");
			return false;
		}
		return true;
	}

	// Vor dem eigentlichen Algorithmus kann mit dieser Funktion die Häufigkeit der einzelnen Zeichen aus dem übergebenen Text errechnet werden.
	// Hierzu kann die Anzahl des Vorkommens eines Zeichens berechnet werden und in einem Array gespeichert werden.
	// Für jedes Zeichen c enthält das Array f an Stelle c die Häufigkeit (also f['a'] ist die Häufigkeit von a im Text. Kommt das Zeichen nicht vor, ist die Häufigkeit 0.)
	// Zur Erinnerung: ein char kann wie eine Ganzzahl verwendet werden, daher funktioniert f[c] für jedes char c.
	public Integer[] calculateFrequencies(String text){
		//Fehlerabfrage
		if ( text == null ) {
			System.out.println("[calculateFrequencies] Kein Text übergeben");
			return null;
		}

		Integer[] f = new Integer[256];
		char[] charText = text.toCharArray();

		Arrays.fill(f, 0);

		try {
			for (char character : charText) {
				f[character] = f[character] + 1;
			}
		}
		catch( IndexOutOfBoundsException e){
			System.out.println("[calculateFrequencies] Es sind Zeichen enthalten, welche nicht in einer ASCII-Tabelle mit 256 Zeichen dargestellt werden kann");
			return null;
		}
		return f;
	}

	// Iterativer Algorithmus zur Erstellung des Präfix-Codes (Skript S.115) mithilfe von BinHeap.
	// frequencies enthält die Häufigkeiten (siehe calculateFrequencies). Häufigkeit von 0 bedeutet, das entsprechende Zeichen ist nicht im Text vorhanden und wir brauchen keinen Präfixcode dafür.
	// Die Funktion setzt den Knoten root auf den Wurzelknoten des PräfixCode-Baums und gibt diesen Wurzelknoten außerdem zurück
	public HNode constructPrefixCode(Integer[] frequencies){

		//Fehlerabfrage
		if (frequencies == null){
			System.out.println("[constuctPrefixCode] Keine Häufigkeitstabelle übergeben");
			return null;
		}

		BinHeap<Integer, HNode> binHeap = new BinHeap<>();

		//1. Für jedes Zeichen c
		for ( int i = 0; i < frequencies.length; i++){
			int amountOfCharacter = frequencies[i];

			// Erzeuge einen zugehörigen Blattknoten
			// und füge ihn mit Priorität f(c) in eine Minimum-Vorrangwar teschlange ein.

			if ( amountOfCharacter == 0) continue;
			HNode hNode = new HNode();
			hNode.chars = Character.toString((char)i);

			binHeap.insert(amountOfCharacter, hNode);
		}

		//2 Solange die Warteschlange mindestens zwei Elemente enthält:
		while (binHeap.size() >= 2){

			//1 Entnimm die Knoten x und y mit minimaler Priorität/Häufigkeit.
			BinHeap.Entry<Integer, HNode> entryX = binHeap.extractMin();
			BinHeap.Entry<Integer, HNode> entryY = binHeap.extractMin();

			//2 Erzeuge einen inneren Knoten mit x und y als Nachfolgern
			//und füge ihn mit Priorität f(x) + f(y) in die Warteschlange ein.
			HNode parent = new HNode();
			parent.leftChild = entryX.data();						  //"ab"
			parent.rightChild = entryY.data();						  //"cf"
			parent.chars = entryX.data().chars + entryY.data().chars; //"abcf"
			binHeap.insert(entryX.prio()+entryY.prio(), parent);

		}

		//3 Entnimm den verbleibenden Knoten aus der War teschlange,
		//der den Wurzelknoten des gesamten Kodebaums darstellt.
		this.root = binHeap.extractMin().data();

		//4 Weise jedem Zeichen c im Alphabet sein Kodewort anhand dieses Baums zu (vgl. § 4.3.2).
		//Pre-Order zum Durchlaufen des Baumes
		preorderTreeWalkAssignCodeword(root, Direction.root, "");

		this.codes = new String[frequencies.length];
		for (int i = 0; i < frequencies.length; i++){
			char character = (char)i;

			preorderTreeWalkGetCodeword(root, character);

		}

		return root;
	}


	/**
	 * Zuweisen der Kodewörter zu den Knoten des Präfixbaums
	 * @param node Der Wurzelknoten des Teilbaums
	 * @param direction Hilfsvariable, ob von links, rechts aus der Knoten erreicht wurde (root, wenn der Knoten Wurzelknoten des Baumes ist)
	 * @param codeword Das bisher ermittelte Kodewort
	 */
	private void preorderTreeWalkAssignCodeword(HNode node, Direction direction, String codeword){
		if (node == null) return;
		String newCodeword = "";
		switch (direction){
			case root:  node.codeWord = newCodeword; 					break;		//Root hat leeres Codewort
			case left:  node.codeWord = newCodeword = codeword + "0"; 	break;		//Left 	Codeword + 0
			case right: node.codeWord = newCodeword = codeword + "1";				//Right Codeword + 1
		}

		preorderTreeWalkAssignCodeword(node.leftChild, Direction.left, newCodeword);
		preorderTreeWalkAssignCodeword(node.rightChild, Direction.right, newCodeword);

	}


	/**
	 * Hilfsmethode um das Kodewort des Zeichens in Codes[] zu setzen
	 * @param node Der Wurzelknoten des Teilbaums
	 * @param character Das gesuchte Zeichen
	 */
	private void preorderTreeWalkGetCodeword(HNode node, char character){
		if (node == null) return;

		if ( node.chars.equals(	(String.valueOf(character))	)  ) {
			this.codes[character] = node.codeWord;
			return;
		}

		preorderTreeWalkGetCodeword(node.leftChild, character);
		preorderTreeWalkGetCodeword(node.rightChild, character);
	}


	// Kodierung einer Zeichenkette text (Skript S.108)
	// Die Ergebnis-Zeichenkette enthält nur Nullen und Einsen
	// (der Einfachheit halber wird dennoch ein String-Objekt verwendet)
	// Kodierung: linker Teilbaum -> 0, rechter Teilbaum -> 1
	// Erster Parameter: Zu kodierender Text
	// Zweiter Parameter zeigt an, ob ein neuer Präfixcode erzeugt werden soll (true) oder mit dem aktuellen Präfixcode gearbeitet werden soll (false)
	public String encode(String text, boolean newPrefixCode){
		//Fehlerabfrage
		if (text == null ){
			System.out.println( "[encode] Kein Text übergeben");
			return null;
		}


		StringBuilder stringBuilder = new StringBuilder();

		//Häufigkeit der Buchstaben feststellen und neuen Präfixcode erstellen
		if (newPrefixCode) constructPrefixCode(calculateFrequencies(text));
		else{
			if (!canEncode(text)) {
				System.out.println("[encode] Text kann mit altem Präfixcode nicht codiert werden");
				return null;
			}
		}

		//Fehleabfrage
		if (codes == null ) {
			System.out.println( "[encode] Keine Kodewörter für Kodierung erstellt");
			return null;
		}

		try {
			char[] charArrayText = text.toCharArray();
			String codeword;
			for (char character : charArrayText) {
				codeword = this.codes[character];
				stringBuilder.append(codeword);
			}
			return stringBuilder.toString();
		} catch ( ArrayIndexOutOfBoundsException e){
			System.out.println("[encode] Es sind Zeichen enthalten, welche nicht in einer ASCII-Tabelle mit 256 Zeichen dargestellt werden kann");
			return null;
		}
	}


	// Dekodierung eines Huffman-Kodierten Textes. (Skipt S.107)
	// Die Ergebnis-Zeichenkette ist der ursprüngliche Text vor der Huffman-Kodierung
	public String decode(String huffmanEncoded){
		return decode(huffmanEncoded, this.root);
	}

	// Dekodierung eines Huffman-Kodierten Textes mithilfe des übergebenen Präfix-Codebaums. (Skipt S.107) Der aktuelle Baum soll dabei nicht überschrieben werden.
	// Die Ergebnis-Zeichenkette ist der ursprüngliche Text vor der Huffman-Kodierung
	public String decode(String huffmanEncoded, HNode rootNode){

		//Fehlerabfrage
		if ( huffmanEncoded == null || rootNode == null) {
			if (huffmanEncoded == null) System.out.println("[decode] Kein Huffman Code übergeben");
			if (rootNode == null) System.out.println("[decode] Kein Wurzelknoten übergeben");
			return null;
		}

		char[] huffmanEncodedChars = huffmanEncoded.toCharArray();
		StringBuilder decodedStringBuilder = new StringBuilder();
		HNode currentNode = rootNode;

		//0101010101010101 -> Das ist ein Text
		for ( char character : huffmanEncodedChars ){

			if (character == '0') {
				currentNode = currentNode.leftChild;
			}
			else if(character == '1'){
				currentNode = currentNode.rightChild;
			}
			//Fehlerabfrage
			else{
				System.out.println("[decode] Zu decodierende Sequenz darf nur 0en und 1en enthalten");
				return null;
			}

			//Fehlerfall
			if (currentNode == null){
				System.out.println( "[decode] Aktueller Knoten ist null, obwohl kein Blattknoten erreicht wurde");
				return null;
			}
			//Ist Blattknoten
			if(currentNode.leftChild == null && currentNode.rightChild == null){
				// 0010 0010 0001 0001
				// A    | B  |
				decodedStringBuilder.append(currentNode.chars);
				currentNode = rootNode;
			}
		}

		return decodedStringBuilder.toString();

	}

	// Präfixcodes ausgeben
	// Reihenfolge: preOrder, also WLR, zuerst Wurzel, dann linker Teilbaum, dann rechter Teilbaum
	public void dumpPrefixCodes(boolean modus){
		if(modus){
			newPreorderDump(this.root);
		}
		else {
			preorderTreeWalkDump(this.root);
		}
	}

	/**
	 * Hilfsmethode, gibt Präfixbaum im Pre-Order-Tree-Walk aus
	 * @param node Der Wurzelknoten des Teilbaums
	 */
	private void preorderTreeWalkDump(HNode node){

		if (node == null) return;
		System.out.println(node.chars);
		preorderTreeWalkDump(node.leftChild);
		preorderTreeWalkDump(node.rightChild);

	}

	/**
	 * Neue dump-Ausgabe
	 * ABRACADABRA
	 * 						ABRCD
	 * 			A						BRCD
	 * 							B					RCD
	 * 											R			CD
	 * 													C		D
	 *
	 *
	 * 		A: 0
	 * 		B: 10
	 * 		R: 110
	 * 		C: 1110
	 * 		D: 1111
	 *
	 */
	private void newPreorderDump(HNode node){

		if ( node == null) return;

		//Ausgabe, wenn Blattknoten:
		if(node.leftChild == null && node.rightChild == null){
			System.out.println(node.chars + ": " + node.codeWord);
		}

		newPreorderDump(node.leftChild);
		newPreorderDump(node.rightChild);

	}


	/*
	Order-Walks

						A
			B						C
		D		E				F		G


	Inorder: 	(links, root, rechts)		D B E A F C G
	Preorder: 	(root, links, rechts)		A B D E C F G
	Postorder: 	(links, rechts, root) 		D E B F G C A
	 */

}

// Interaktives Testprogramm für die Klasse Huffman.
class HuffmanTest {
	public static void main(String[] args) throws java.io.IOException {
		// Häufigkeiten (ASCII-Tabelle)
		Integer[] exampleFrequencies = new Integer[]{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 3676, 3, 160, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 472, 1, 252, 8, 39, 38,
				18, 12, 13, 14, 13, 9, 9, 15, 45, 12,
				1, 1, 1, 18, 1, 23, 76, 110, 160, 62, // 65 = A
				66, 104, 19, 33, 9, 74, 148, 108, 402, 11,
				44, 1, 300, 270, 270, 12, 40, 62, 2, 11,
				42, 0, 0, 0, 0, 0, 0, 1164, 389, 593, // 97 = a
				832, 3186, 332, 525, 908, 1666, 46, 370, 739, 541,
				2010, 560, 220, 5, 1508, 1382, 1348, 648, 199, 313,
				13, 56, 214, 1, 1, 1, 1, 1
		};

		HNode lastPrefixCode = null;

		// Standardeingabestrom System.in als InputStreamReader
		// und diesen wiederum als BufferedReader "verpacken",
		// damit man bequem zeilenweise lesen kann.
		java.io.BufferedReader r = new java.io.BufferedReader(
				new java.io.InputStreamReader(System.in));
		Huffman h = new Huffman();
		// Endlosschleife.
		while (true) {

			// Eingabezeile vom Benutzer lesen, ggf. ausgeben (wenn das
			// Programm nicht interaktiv verwendet wird) und in einzelne
			// Wörter zerlegen.
			// Abbruch bei Ende der Eingabe oder leerer Eingabezeile.
			System.out.print(">>> ");
			String line = r.readLine();
			if (line == null || line.equals("")) return;
			if (System.console() == null) System.out.println(line);
			String[] cmd = line.split(" ");

			String funct = cmd[0];
			String text = null;
			if (cmd.length == 1) {
				if (funct == "dec") {
					System.out.println("Nothing to decode.");
					continue;
				} else
					if (funct.startsWith("enc")){
						System.out.println("Nothing to encode.");
						continue;
					}
			} else {
				text = line.substring(line.indexOf(' ')+1);
			}
			String result;
			switch (funct) {
				case "enc0": // Kodieren ohne neue Präfixcodes zu errechnen
					result = h.encode(text, false);
					if (result!=null)
						System.out.println("Kodierter Text: "+result);
					break;
				case "enc1": // Kodieren mit Berechnung neuer Präfixcodes
					result = h.encode(text, true);
					if (result!=null)
						System.out.println("Kodierter Text: "+result);
					break;
				case "dec": // Dekodieren eines Textes mit aktuellem Präfixcode
					result = h.decode(text);
					if (result!=null)
						System.out.println("Dekodierter Text: "+result);
					break;
				case "decpref": // Dekodieren mit übergebenem Präfixcode
					result = h.decode(text, lastPrefixCode);
					if (result!=null)
						System.out.println("Dekodierter Text: "+result);
					break;
				case "prefixes": // Präfix-Codes erstellen mit Häufigkeiten aus vorgeg. Feld
					lastPrefixCode = h.constructPrefixCode(exampleFrequencies);
					break;
				case "dump": // Präfix-Codes ausgeben
					h.dumpPrefixCodes(true);
					break;
				case "dumptree": //Präfix-Codebaum-Knoten ausgeben
					h.dumpPrefixCodes(false);
					break;
				default:
					System.out.println("Unknown Function: " + funct);
					System.out.println("	Possible values: ");
					System.out.println("		enc0 - Encode using current prefix codes");
					System.out.println("		enc1 - Construct new prefix codes and then encode");
					System.out.println("		dec - Decode using current prefix codes");
					System.out.println("		decpref - Decode using a given prefix code");
					System.out.println("		prefixes - Construct new prefix codes");
					System.out.println("		dump - Dump prefix code tree");
					return;
			}
		}
	}
}

class Tests {
	public static void main(String[] args) {

		//assert(false);
		try {
			assert (false) : "";
			System.out.println( "Asserts nicht aktiviert");
		}catch ( AssertionError e){
			System.out.println( "Test startet");
		}


		Huffman test = new Huffman();

		String text1 = "Chr. Die chinesische Schrift (chinesisch 中文字, Pinyin zhōngwénzì, Zhuyin ㄓㄨㄥ ㄨ";
		String text2 = "12";
		String text3 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwx ,";
		String text4 = "Ein Toller text";
		String text5 = "Ein anderer toller Text mit allen MOEGLICHEN Zeichen, die in drei enhalten sind";
		String text6 = "ETto";
	/*	String text3 = "ab1cdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ().:;,&$ßäöüielen wie Federn vom Himmel herab. Da saß eine Königin an einem Fenster, das ÄÖÜe Es war einmal mitten im Winter, und die Schneeflocken fielen wie Federn vom Himmel herab. Da saß eine Königin an einem Fenster, das einen Rahmen von schwarzem Ebenholz hatte, und nähte. Und wie sie so nähte und nach dem Schnee aufblickte, stach sie sich mit der Nadel in den Finger, und es fielen drei Tropfen Blut in den Schnee. Und weil das Rote im weißen Schnee so schön aussah, dachte sie bei sich: Hätt' ich ein Kind, so weiß wie Schnee, so rot wie Blut und so schwarz wie das Holz an dem Rahmen! Bald darauf bekam sie ein Töchterlein, das war so weiß wie Schnee, so rot wie Blut und so schwarzhaarig wie Ebenholz und ward darum Schneewittchen (Schneeweißchen) genannt. Und wie das Kind geboren war, starb die Königin. Über ein Jahr nahm sich der König eine andere Gemahlin. Es war eine schöne Frau, aber sie war stolz und übermütig und konnte nicht leiden, daß sie an Schönheit von jemand sollte übertroffen werden. Sie hatte einen wunderbaren Spiegel wenn sie vor den trat und sich darin beschaute, sprach sie:";
		String text4 = "Die chinesische Schrift oder Hanzì fixiert die chinesischen Sprachen, vor allem das Hochchinesische, mit chinesischen Schriftzeichen. Sie ist damit ein zentraler Träger der chinesischen Kultur und diente als Grundlage der japanischen Schriften, einer der koreanischen Schriften und einer der vietnamesischen Schriften.";
		String text5 = "Es war einmal mitten im Winter, und die Schneeflocken fielen wie Federn vom Himmel herab. Da saß eine Königin an einem Fenster, das einen Rahmen von schwarzem Ebenholz hatte, und nähte. Und wie sie so nähte und nach dem Schnee aufblickte, stach sie sich mit der Nadel in den Finger, und es fielen drei Tropfen Blut in den Schnee. Und weil das Rote im weißen Schnee so schön aussah, dachte sie bei sich: Hätt' ich ein Kind, so weiß wie Schnee, so rot wie Blut und so schwarz wie das Holz an dem Rahmen! Bald darauf bekam sie ein Töchterlein, das war so weiß wie Schnee, so rot wie Blut und so schwarzhaarig wie Ebenholz und ward darum Schneewittchen (Schneeweißchen) genannt. Und wie das Kind geboren war, starb die Königin. Über ein Jahr nahm sich der König eine andere Gemahlin. Es war eine schöne Frau, aber sie war stolz und übermütig und konnte nicht leiden, daß sie an Schönheit von jemand sollte übertroffen werden. Sie hatte einen wunderbaren Spiegel wenn sie vor den trat und sich darin beschaute, sprach sie:";
*/
		String result1;
		String result2;
		String result3;
		String result4;
		String result5;

		HNode root1;
		HNode root2;


		assert(!test.canEncode(text1)): "Kein Präfixcode vorhanden, kann nicht codiert werden";

		result1 = test.encode(text1, false);
		assert(result1 == null): "Result muss NULL sein, kein Präfixcode vorhanden";

		result2 = test.encode(text1, true);
		assert(result2 == null): "Text kann nicht in der ASCII 256er Tabelle kodiert werden";

		result1 = test.encode(text2, false);
		assert(result1 == null): "Text kann nicht kodiert werden, bisher kein Präfixbaum erstellt";

		result2 = test.encode(text2, true);
		assert(result2 != null && result2.length() == 2): "Text muss kodierbar sein";

		assert(test.canEncode(text2)): "Text kann nicht codiert werden, wurde aber bereits codiert!?";

		result1 = test.encode(text3, true);
		result2 = test.decode(result1);

		assert (text3.equals(result2)):"Texte müssen gleich sein";


		Integer[] freq = test.calculateFrequencies(text3);
		root1 = test.constructPrefixCode(freq);
		assert (freq != null && root1 != null): "Freq oder Wurzel ist null";

		result1 = test.encode(text3, false);
		result2 = test.encode(text3, true);
		assert ((result1.equals(result2))):"Codierte Texte müssen gleich sein, da gleiche Präfixbäume (neu erstellt aber gleich) verwendet wurden";

		result4 = test.decode(result2);
		result5 = test.decode(result1, root1);
		assert(result4.equals(result5)): "Texte müssen gleich sein, unterschiedliche Kodierung";

		root1 = test.constructPrefixCode(test.calculateFrequencies(text4));
		//Präfixcode erstellen
		root2 = test.constructPrefixCode(test.calculateFrequencies(text3));

		assert((test.decode(text3, root2))==null): "Test Dekodierung eines nicht kodierten Textes";

		test.encode(text3, true);


		assert((test.decode(test.encode(text4, false)).equals(text4))): "Texte nicht gleich1";

		assert(test.decode(test.encode(text5, false)).equals(text5)): "Texte nicht gleich2";

		//Testfall: Präfixbaum -> encode Text -> Decodieren mit root -> Decode Text
		result1 = test.encode(text3, true);
		assert( test.decode(test.encode(text4, false), root2).equals(text4)): "Texte nicht gleich3";
		result2 = test.decode(result1);
		assert (result2.equals(text3)): "Kodierung + Dekodierung != gleicher Text";



		Huffman neu = new Huffman();
		neu.encode(text4, true);
		//Präfixbaum vom Text 4
		HNode neuerPraefixbaum= neu.constructPrefixCode(neu.calculateFrequencies(text4));

		String encWithPra = neu.encode(text6, false);

		//neuen Präfixbaum erstellen
		String encWithPraeT3 =neu.encode(text3, true);
		String Texttt = neu.decode(encWithPraeT3);
		String neuerText = neu.decode(encWithPra, neuerPraefixbaum);
		assert(neuerText.equals(text6));
		assert (Texttt.equals(text3));


		System.out.println( "\n\n\n Baumausgabe");

		test.dumpPrefixCodes(true);

	}
}