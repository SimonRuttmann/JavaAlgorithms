import java.lang.reflect.Array;
import java.util.ArrayList;

public class DefaultTestGNeu {
    public static void main(String[] args) {
        //Testet ob Inserts aktiv sind
        int [] ArrayList;
        int [] [] ArrayList2;
        Graph graph;
        WeightedGraph Wgraph,Wgraph2;
        try {
            assert (false);
            System.out.println("Fehler: Asserts nicht aktiv");
            return;
        } catch (AssertionError f) {
            System.out.println("Test beginnt");
        }

        String testsToDo = "GraphImpl WeightedGraph Breitensuche Zusammenhangskomponenten Tiefensuche minPrim BellmanFordDijkstra Bellman2 Dijkstra2 Bellman3 Dijkstra3";//Put in the tests you want to perform (test1 test2 test3 usw)
        for (String list : testsToDo.split(" ")) {
            System.out.println("Running Test:"+list);
            switch (list) {

                case "GraphImpl":
                    int [][] Muster = new int [][] {
                            {2},
                            {0,2,4},
                            {},
                            {0,1},
                            {0,3},
                            {4,6},
                            {3,5,6},
                    };
                    graph = new GraphImpl(Muster);
                    assert (graph.size() == 7) : "Size sollte 7 sein, ist aber " + graph.size();
                    ArrayList = new int [] {1,3,0,2,2,2,3};
                    for (int i = 0; i < ArrayList.length; i++) {
                        assert (graph.deg(i) == ArrayList[i]) : "Der Knoten " + i + " sollte " + ArrayList[i] + " Nachfolger haben, ist aber " + graph.deg(i);
                    }
                    for (int i = 0; i < graph.size(); i++) {
                        for (int k = 0; k < graph.deg(i); k++) {
                            for (int j = 0; j < graph.deg(i) ;j++) {
                                if (graph.succ(i, j) == Muster[i][k]) break;
                            }
                            assert (true) : "Der Knoten " + i + " sollte " + ArrayList[i] + " als direkten Nachfolger haben, ist aber " + graph.succ(i, i);
                        }
                    }
                    int [][] Muster2 = new int[][] {
                            {1,3,4},
                            {3},
                            {0,1},
                            {4,6},
                            {1,5},
                            {6},
                            {5,6},
                    };
                    Graph grapht=graph.transpose();

                    Integer[] line;
                    String stringMuster=new String();
                    String stringOut=new String();


                    for (int h=0;h<Muster2.length;h++){
                        line=new Integer[graph.size()];
                        for(int t=0;t<Muster2[h].length;t++){
                            line[Muster2[h][t]]=1;
                        }
                        for(int i=0;i<line.length;i++){
                            if(line[i]==null) stringMuster=stringMuster+"0 ";
                            else stringMuster=stringMuster+"1 ";
                        }
                        stringMuster=stringMuster+"\n";
                    }

                    for (int h=0;h<grapht.size();h++){
                        line=new Integer[grapht.size()];
                        for(int t=0;t<grapht.deg(h);t++){
                            line[grapht.succ(h,t)]=1;
                        }
                        for(int i=0;i<line.length;i++){
                            if(line[i]==null) stringOut=stringOut+"0 ";
                            else stringOut=stringOut+"1 ";
                        }
                        stringOut=stringOut+"\n";
                    }

                    if(!stringMuster.equals(stringOut)){
                        System.out.println("Eingegebener Graph:");
                        for (int h=0;h<Muster.length;h++){
                            line=new Integer[graph.size()];
                            for(int t=0;t<Muster[h].length;t++){
                                line[Muster[h][t]]=1;
                            }
                            for(int i=0;i<line.length;i++){
                                if(line[i]==null) System.out.print("0 ");
                                else System.out.print("1 ");
                            }
                            System.out.println();
                        }

                        System.out.println("\nSo soll der Transpose Graph aussehen:");
                        System.out.println(stringMuster);

                        System.out.println("\nSo sieht die Rückgabe des Algorithmus aus:");
                        System.out.println(stringOut);
                        assert (false) : "transpose ist nicht korrekt";
                    }



                    break;

                // Gerichteter gewichteter Graph
                case "WeightedGraph":
                    Wgraph = new WeightedGraphImpl(new int [][] {
                            {1,3,4},
                            {0,2,3,4,5},
                            {1,4,5},
                            {0,1,4},
                            {0,1,2,3,5},
                            {1,2,4},
                    }, new double[][]{
                            {3,5,1},
                            {3,8,5,2,7},
                            {8,7,5},
                            {5,5,4},
                            {1,2,7,4,8},
                            {7,5,8},
                    });
                    double [][] DListe = new double [][] {
                            {3,5,1},
                            {3,8,5,2,7},
                            {8,7,5},
                            {5,5,4},
                            {1,2,7,4,8},
                            {7,5,8},
                    };
                    for (int i = 0; i < DListe.length; i++) {
                        for (int j = 0; j < DListe[i].length; j++) {
                            assert (Wgraph.weight(i, j) == DListe[i][j]) : "Das Gewicht von " + Wgraph.weight(i, j) + " beträgt " + DListe.length;
                        }
                    }
                    break;

                // Breitensuche
                case "Breitensuche":
                    graph = new GraphImpl(new int [][] {
                            {2},
                            {0,2,4},
                            {},
                            {0,1},
                            {0,3},
                            {4,6},
                            {3,5,6},
                    });
                    BFS graphBFS = new BFSImpl();
                    graphBFS.search(graph, 1);
                    ArrayList = new int [] {1,0,1,2,1,-1,-1};
                    for (int i = 0; i < ArrayList.length; i++) {
                        assert (graphBFS.dist(i) == ArrayList[i]) : "Distance des Knotens " + i + " sollte " + ArrayList[i] + " sein, ist aber " + graphBFS.dist(i);
                    }
                    ArrayList = new int [] {1,-1,1,4,1,-1,-1};
                    for (int i = 0; i < ArrayList.length; i++) {
                        assert (graphBFS.pred(i) == ArrayList[i]) : "Vorgänger des Knotens " + i + " sollte " + ArrayList[i] + " sein, ist aber " + graphBFS.dist(i);
                    }
                    break;

                // Tiefensuche einschließlich topologischer Sortierung
                case "Tiefensuche":
                    graph = new GraphImpl(new int [][] {
                            {2},
                            {0,2,4},
                            {},
                            {0,1},
                            {0,3},
                            {4,6},
                            {3,5,6},
                    });
                    DFS graphDFS = new DFSImpl();
                    graphDFS.search(graph);
                    assert (!graphDFS.sort(graph)):"Topologische Sortierung gibt falschen Wert zurück";
                    ArrayList = new int[] {1,5,2,7,6,11,12};
                    for (int i = 0; i < ArrayList.length; i++) {
                       // System.out.println("SequenzEnteckungszeit " +  graphDFS.det(i) + " == " + ArrayList[i]);
                        assert (graphDFS.det(i) == ArrayList[i]) : "Entdeckungszeit sollte "+ArrayList[i]+" betragen, ist stattdessen "+graphDFS.det(i);
                    }
                    ArrayList = new int[] {4,10,3,8,9,14,13};
                    for (int i = 0; i < ArrayList.length; i++) {
                       // System.out.println("SequenzAbschlusszeit " +  graphDFS.fin(i) + " == " + ArrayList[i]);
                        assert (graphDFS.fin(i) == ArrayList[i]) : " ";
                    }
                    // Zwei Methoden?
                    ArrayList = new int[] {2,0,3,4,1,6,5};
                    for (int i = 0; i < ArrayList.length; i++) {
                      //  System.out.println("Sequenz " +  graphDFS.sequ(i) + " == " + ArrayList[i] + " Abschlusszeit = " + graphDFS.fin(graphDFS.sequ(i)));
                        assert (graphDFS.sequ(i) == ArrayList[i]) : "Die Eingabe stimmt nicht mit dem Knoten überein.";
                    }
                    break;

                // Bestimmung starker Zusammenhangskomponenten
                case "Zusammenhangskomponenten":
                    SCC SCC = new SCCImpl();
                    graph = new GraphImpl((new int [][] {
                        {2},
                        {0,2,4},
                        {},
                        {0,1},
                        {0,3},
                        {4,6},
                        {3,5,6},
                    }));
                    SCC.compute(graph);

                    ArrayList2 = new int [] [] {
                            {0},
                            {2},
                            {1,3,4},
                            {5,6}
                    };
                    Integer[] IArrayList = new Integer[ArrayList2.length];
                    for(int i=0;i<IArrayList.length;i++){
                        IArrayList[i]=SCC.component(ArrayList2[i][0]);
                    }

                    //for(int i=0;i< graph.size();i++) System.out.println(SCC.component(i));

                    for (int j = 0; j < ArrayList2.length; j++) {
                        for (int k = 0; k < ArrayList2[j].length; k++) {
                            assert (SCC.component(ArrayList2[j][k])==IArrayList[j]):"component sollte "+IArrayList[j]+" sein, ist aber "+ SCC.component(ArrayList2[j][k]);
                            }
                        }
                    break;

                // Bestimmung minimaler Gerüste nach Prim
                case "minPrim":
                    MSF graphMSF = new MSFImpl();
                    Wgraph = new WeightedGraphImpl((new int [][] {
                            {1,3},
                            {0,2,3,4},
                            {1,4},
                            {0,1,4,5,6},
                            {1,2,3,6},
                            {3,6},
                            {3,4,5}
                    }), new double[][]{
                            {14,10},
                            {14,16,18,13},
                            {16,9},
                            {10,18,30,17,12},
                            {13,9,30,16},
                            {17,22},
                            {12,16,22}
                    });
                    /*Wgraph = new WeightedGraphImpl((new int[][]{
                            {1,4,5},
                            {0,2,3,4,5},
                            {1,3,4},
                            {1,2,4},
                            {0,1,2,3,5},
                            {0,1,4}}),
                            new double[][]{
                                    {3,-0.789,5},
                                    {3,8,7,2,5},
                                    {8,5,7},
                                    {7,5,8},
                                    {-0.789,2,7,8,4},
                                    {5,5,4}
                    });*/
                    graphMSF.compute(Wgraph, 0);
                    ArrayList = new int [] {-1,0,4,0,1,3,3};
                    //ArrayList = new int [] {-1,4,4,4,0,2};
                    for (int i = 0; i < ArrayList.length; i++) {
                        assert (graphMSF.pred(i) == ArrayList[i]) : "Vorgängerknoten an der Stelle "+i+" soll "+ArrayList[i]+" sein, ist aber "+graphMSF.pred(i);
                    }
                    break;

                // Bestimmung kürzester Wege nach Bellman-Ford und Dijkstra
                case "BellmanFordDijkstra":
                    SP SPgraph = new SPImpl();
                    Wgraph = new WeightedGraphImpl((new int [][] {
                            {1},
                            {2},
                            {4},
                            {3,5},
                            {1},
                            {}
                    }), new double[][]{
                            {50},
                            {-10},
                            {-5},
                            {-1,5},
                            {-1},
                            {}
                    });
                    double [][] SPListe = new double [][] {
                            {50},
                            {-10},
                            {-5},
                            {-1,5},
                            {-1},
                            {},
                    };
                    ArrayList = new int [] {};
                    assert (!SPgraph.bellmanFord(Wgraph, 0)) : "BellmenFord liefert true zurück liefer sollte aber false zurück";
                    Wgraph = new WeightedGraphImpl((new int [][] {
                            {1},
                            {2,3},
                            {4},
                            {4},
                            {5},
                            {}
                    }), new double[][]{
                            {50},
                            {-10,-1},
                            {-5},
                            {-1},
                            {5},
                            {}
                    });
                    double [][] AbstandListe = new double [][] {
                            {0},
                            {50},
                            {40},
                            {49},
                            {35},
                            {40},
                    };
                    double [][] VorgängerListe = new double [][] {
                            {-1},
                            {0},
                            {1},
                            {1},
                            {2},
                            {4},
                    };
                    assert (SPgraph.bellmanFord(Wgraph, 0)) : "BellmenFord liefert false zurück";

                    for (int i = 0; i < AbstandListe.length; i++) {
                        assert (SPgraph.dist(i) == AbstandListe[i][0]) : "Der Abstand " + i + " sollte " + AbstandListe[i][0] + " sein, ist aber " + SPgraph.dist(i);
                    }
                    for (int i = 0; i < VorgängerListe.length ; i++) {
                        assert (SPgraph.pred(i) == VorgängerListe[i][0]) : "Der Vorgänger " + i + " sollte " + VorgängerListe[i][0] + " sein, ist aber " + SPgraph.pred(i);
                    }
                    // Test für dijsdjfls-Algorithmus
                    Wgraph2 = new WeightedGraphImpl((new int [][] {
                            {1,3},
                            {2,4},
                            {4},
                            {1,4},
                            {}
                    }), new double[][]{
                            {100,50},
                            {100,250},
                            {50},
                            {100,250},
                            {}
                    });
                    double [][] AbstandListeD = new double [][] {
                            {0},
                            {100},
                            {200},
                            {50},
                            {250},

                    };
                    double [][] VorgängerListeD = new double [][] {
                            {-1},
                            {0},
                            {1},
                            {0},
                            {2},
                    };

                    SPgraph = new SPImpl();
                    SPgraph.dijkstra(Wgraph2, 0);
                    // dür dijiisdf noch test einfügen
                    for(int i=0;i<Wgraph2.size();i++){ // Abstände
                        assert (SPgraph.dist(i) == AbstandListeD[i][0]) : "Der Abstand " + i + " sollte " + AbstandListeD[i][0] + " sein, ist aber " + SPgraph.dist(i);
                    }
                    for(int i=0;i<Wgraph2.size();i++){ // Vorgänge
                        assert (SPgraph.pred(i) == VorgängerListeD[i][0]) : "Der Abstand " + i + " sollte " + VorgängerListeD[i][0] + " sein, ist aber " + SPgraph.pred(i);
                    }
                    break;

                case "Bellman2":

                    Wgraph = new WeightedGraphImpl((new int [][] {
                            {1,4},
                            {2,3,4},
                            {1},
                            {0,2},
                            {2,3},

                    }), new double[][]{
                            {6,7},
                            {5,-4,8},
                            {-2},
                            {2,7},
                            {-3,9},

                    });
                    double [][] AbstandListe2 = new double [][] {
                            {0},
                            {2},
                            {4},
                            {-2},
                            {7},

                    };
                    double [][] VorgängerListe2 = new double [][] {
                            {-1},
                            {2},
                            {4},
                            {1},
                            {0},

                    };
                    SPgraph = new SPImpl();

                    SPgraph.bellmanFord(Wgraph, 0);
                    assert (SPgraph.bellmanFord(Wgraph, 0)) : "BellmenFord liefert false zurück";

                    for (int i = 0; i < AbstandListe2.length; i++) {
                        //System.out.println("Knoten "+i+" Abstand: "+SPgraph.dist(i)+" und Vorgänger"+SPgraph.pred(i));
                        assert (SPgraph.dist(i) == AbstandListe2[i][0]) : "Der Abstand " + i + " sollte " + AbstandListe2[i][0] + " sein, ist aber " + SPgraph.dist(i);
                    }
                    for (int i = 0; i < VorgängerListe2.length ; i++) {
                        assert (SPgraph.pred(i) == VorgängerListe2[i][0]) : "Der Vorgänger " + i + " sollte " + VorgängerListe2[i][0] + " sein, ist aber " + SPgraph.pred(i);
                    }
                    break;
                case "Dijkstra2":
                    Wgraph = new WeightedGraphImpl((new int [][] {
                            {1,4},
                            {2,4},
                            {3},
                            {0,2},
                            {1,2,3},

                    }), new double[][]{
                            {10,5},
                            {1,2},
                            {4},
                            {7,6},
                            {3,9,2},

                    });
                    double [][] AbstandListe3 = new double [][] {
                            {0},
                            {8},
                            {9},
                            {7},
                            {5},

                    };
                    double [][] VorgängerListe3 = new double [][] {
                            {-1},
                            {4},
                            {1},
                            {4},
                            {0},

                    };
                    SPgraph = new SPImpl();
                    SPgraph.dijkstra(Wgraph, 0);


                    for (int i = 0; i < AbstandListe3.length; i++) {
                      //System.out.println("Knoten "+i+" Abstand: "+SPgraph.dist(i)+" und Vorgänger"+SPgraph.pred(i));
                        assert (SPgraph.dist(i) == AbstandListe3[i][0]) : "Der Abstand " + i + " sollte " + AbstandListe3[i][0] + " sein, ist aber " + SPgraph.dist(i);
                    }
                    for (int i = 0; i < VorgängerListe3.length ; i++) {
                        assert (SPgraph.pred(i) == VorgängerListe3[i][0]) : "Der Vorgänger " + i + " sollte " + VorgängerListe3[i][0] + " sein, ist aber " + SPgraph.pred(i);
                    }
                    break;
                case "Bellman3":
                    Wgraph = new WeightedGraphImpl((new int [][] {
                            {1,3,8,10},
                            {2},
                            {4},
                            {2,4,7},
                            {5},
                            {6},
                            {7},
                            {2,4,9},
                            {2,7},
                            {},
                            {9},

                    }), new double[][]{
                            {100,20,10,76},
                            {67},
                            {1},
                            {33,3,20},
                            {2},
                            {71},
                            {72},
                            {-20,-2,4},
                            {10,50},
                            {},
                            {2},

                    });
                    double [][] AbstandListe4 = new double [][] {
                            {0},
                            {100},
                            {20},
                            {20},
                            {21},
                            {23},
                            {94},
                            {40},
                            {10},
                            {44},
                            {76},

                    };
                    double [][] VorgängerListe4 = new double [][] {
                            {-1},
                            {0},
                            {7}, // hier gibt es möglichkeit 8 oder 7
                            {0},
                            {2},
                            {4},
                            {5},
                            {3},
                            {0},
                            {7},
                            {0},

                    };
                    SPgraph = new SPImpl();

                    SPgraph.bellmanFord(Wgraph, 0);
                    assert (SPgraph.bellmanFord(Wgraph, 0)) : "BellmenFord liefert false zurück";

                    for (int i = 0; i < AbstandListe4.length; i++) {
                        //System.out.println("Knoten "+i+" Abstand: "+SPgraph.dist(i)+" und Vorgänger"+SPgraph.pred(i));
                        assert (SPgraph.dist(i) == AbstandListe4[i][0]) : "Der Abstand " + i + " sollte " + AbstandListe4[i][0] + " sein, ist aber " + SPgraph.dist(i);
                    }
                    for (int i = 0; i < VorgängerListe4.length ; i++) {
                        assert (SPgraph.pred(i) == VorgängerListe4[i][0]) : "Der Vorgänger " + i + " sollte " + VorgängerListe4[i][0] + " sein, ist aber " + SPgraph.pred(i);
                    }
                    break;
                case "Dijkstra3":
                    Wgraph = new WeightedGraphImpl((new int [][] {
                            {1,2,3},
                            {2,5,6},
                            {},
                            {4},
                            {},
                            {4,6},
                            {},

                    }), new double[][]{
                            {19,98,10},
                            {20,74,86},
                            {},
                            {55},
                            {},
                            {45,50},
                            {},

                    });
                    double [][] AbstandListe5 = new double [][] {
                            {0},
                            {19},
                            {39},
                            {10},
                            {65},
                            {93},
                            {105},

                    };
                    double [][] VorgängerListe5 = new double [][] {
                            {-1},
                            {0},
                            {1},
                            {0},
                            {3},
                            {1},
                            {1},

                    };
                    SPgraph = new SPImpl();
                    SPgraph.dijkstra(Wgraph, 0);


                    for (int i = 0; i < AbstandListe5.length; i++) {
                        //  System.out.println("Knoten "+i+" Abstand: "+SPgraph.dist(i)+" und Vorgänger"+SPgraph.pred(i));
                        assert (SPgraph.dist(i) == AbstandListe5[i][0]) : "Der Abstand " + i + " sollte " + AbstandListe5[i][0] + " sein, ist aber " + SPgraph.dist(i);
                    }
                    for (int i = 0; i < VorgängerListe5.length ; i++) {
                        assert (SPgraph.pred(i) == VorgängerListe5[i][0]) : "Der Vorgänger " + i + " sollte " + VorgängerListe5[i][0] + " sein, ist aber " + SPgraph.pred(i);
                    }
                    break;


/*
        Graph graph=new GraphImpl(new int [] [] {
                { 1, 2 },	// Knoten 0 hat als Nachfolger Knoten 1 und 2.
                { },	// Knoten 1 hat keine Nachfolger.
                { 2 }	// Knoten 2 hat als Nachfolger sich selbst.
        });

        assert (graph.size()==3):"Die size des Graphen stimmt nicht. Wirkliche Größe ist 3 vs."+graph.size();

        assert (graph.deg(0)==2):"Deg von v=0 sollte 2 sein, ist aber "+graph.deg(0);
        assert (graph.deg(1)==0):"Deg von v=1 sollte 0 sein, ist aber "+graph.deg(1);
        assert (graph.deg(2)==1):"Deg von v=2 sollte 1 sein, ist aber "+graph.deg(2);

        //assert (graph.succ(1,5)==0):"Fehler in succ, sollte 0 sein, ist aber "+graph.succ(1,0);
       // assert (graph.succ(2,0)==2):"Fehler in succ, sollte 2 sein, ist aber "+graph.succ(2,0);
        System.out.println("Alle Tests wurden bestanden");
         */

                default:
                    System.out.println("Fehlerhafte Eingabe bei der Testfallwahl");
                    }
            System.out.println("Test bestanden");
            System.out.println();
        }
    }
}
