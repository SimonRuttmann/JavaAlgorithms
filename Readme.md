# Java Algorithms

A collection of algorithms implemented during my studies at **Aalen University**.

<!-- TOC -->
* [Java Algorithms](#java-algorithms)
  * [📘 Graph Algorithms](#-graph-algorithms)
    * [🧩 Overview](#-overview)
    * [✅ Implemented Interfaces and Classes](#-implemented-interfaces-and-classes)
    * [⚙️ How to Use](#-how-to-use)
    * [📝 Notes](#-notes)
  * [📘 Hashtable](#-hashtable)
    * [🧩 Overview](#-overview-1)
    * [✅ Features](#-features)
    * [🚀 Usage](#-usage)
    * [📝 Notes](#-notes-1)
  * [📘 BinHeap](#-binheap)
    * [🧩 Overview](#-overview-2)
    * [✅ Features](#-features-1)
    * [🚀 Usage](#-usage-1)
      * [🧪 Test Program Commands](#-test-program-commands)
    * [📝 Notes](#-notes-2)
  * [📦 HuffmanEncoding](#-huffmanencoding)
    * [🚀 Overview](#-overview-3)
    * [⚙️ Key Methods](#-key-methods)
    * [📝 Notes](#-notes-3)
    * [Example Usage](#example-usage)
* [📜 License](#-license)
<!-- TOC -->

## 📘 Graph Algorithms

### 🧩 Overview

This project implements several classic graph algorithms in Java, including:

- 🔁 Breadth-First Search (BFS)
- 🧭 Depth-First Search (DFS), incl. topological sorting
- 🔗 Strongly Connected Components (SCC)
- 🌲 Minimum Spanning Forests (Prim's algorithm)
- 🛣️ Shortest Paths (Bellman-Ford & Dijkstra)

All implementations conform to the interfaces provided in `graph.java`, and follow a clear `XYZImpl` naming convention for each algorithm and graph structure.

### ✅ Implemented Interfaces and Classes

| Interface        | Implementation        | Purpose                             |
|------------------|------------------------|-------------------------------------|
| `Graph`          | `GraphImpl`            | Basic directed graph representation |
| `WeightedGraph`  | `WeightedGraphImpl`    | Graph with edge weights             |
| `BFS`            | `BFSImpl`              | Breadth-First Search                |
| `DFS`            | `DFSImpl`              | Depth-First Search + topo sort      |
| `SCC`            | `SCCImpl`              | Strongly Connected Components       |
| `MSF`            | `MSFImpl`              | Minimum Spanning Forest             |
| `SP`             | `SPImpl`               | Shortest Paths (Bellman & Dijkstra) |

### ⚙️ How to Use

Compile all Java files:

```bash
javac *.java
```

Then use the provided test program:

```bash
java GraphTest dfs            # Runs DFS on default graph
java GraphTest bfs            # Runs BFS
java GraphTest scc            # Detects strongly connected components
java GraphTest msf            # Finds minimal spanning forest (for undirected graphs)
java GraphTest dijkstra 0     # Dijkstra from node 0
java GraphTest bellman 2      # Bellman-Ford from node 2
```

Graphs are defined via adjacency lists like this:

```java
Graph g = new GraphImpl(new int[][] {
    {1, 2}, // Node 0 → 1, 2
    {},     // Node 1 → none
    {2}     // Node 2 → 2 (self-loop)
});
```

For weighted graphs:

```java
WeightedGraph wg = new WeightedGraphImpl(
    new int[][] { {1}, {2}, {} },
    new double[][] { {2.5}, {1.0}, {} }
);
```

### 📝 Notes

- 🧪 Tested with the `GraphTest` program
- 📦 No external libraries, no package declarations
- 💡 Uses `BinHeap` (see Aufgabe 2) for priority-based algorithms
- 🔒 Respects all method constraints and interface boundaries



## 📘 Hashtable

### 🧩 Overview

This project is a basic Java implementation of hash tables. It includes:

* 🔢 **Hash functions**: Division & Multiplication
* 🔁 **Probing strategies**: Linear, Quadratic, Double Hashing
* 📦 **Hash tables**:

  * With **chaining**
  * With **open addressing**
* 🧪 A test program: `WordCount` that counts word frequencies using different configurations

### ✅ Features

* ❌ No use of `java.util` or package declarations
* 🧠 Fully implemented methods: `compute`, `first`, `next`, `put`, `get`, `remove`, `dump`
* ➖ Correct handling of negative `hashCode()` values
* 📾 Method behavior follows the given specs exactly

### 🚀 Usage

Compile all classes (they are in a single file or zip):

```bash
javac hashing.java
```

Run the `WordCount` program with different table types and methods. Example:

```bash
java WordCount c d 4 < words
```

with words beeing a simple text file like:
```bash
eins zwei drei zwei eins eins
```

Explanation of parameters:

* First letter: table type (`c` = chaining, `l` = linear probing, `q` = quadratic probing, `d` = double hashing)
* Second letter: hash function (`d` = division, `m` = multiplication)
* Number(s): table size and (for multiplication) constant

🔄 Try combinations like:

```bash
java WordCount q m 2 3 < words
java WordCount l d 4 < words
```

### 📝 Notes

* ⚙️ 100% pure Java (no external libraries)
* 🔑 Can be used with different key types (not just strings)


## 📘 BinHeap

### 🧩 Overview

This project implements a generic minimum-priority queue using binomial heaps. 
It supports elements with any comparable priority type `P` and any data type `D`.

### ✅ Features

* 🧮 Generic class: `BinHeap<P extends Comparable<? super P>, D>`
* 📦 Supports the following operations:

  * `insert(p, d)`: Add new entry with priority `p` and data `d`
  * `minimum()`: Return the entry with the smallest priority
  * `extractMin()`: Remove and return the minimum entry
  * `changePrio(e, p)`: Change the priority of an entry
  * `remove(e)`: Remove a specific entry
  * `contains(e)`, `isEmpty()`, `size()`
  * `dump()`: Print structure of the heap
* 🧠 Fully handles null safety and invalid operations as specified
* ⚙️ Efficient operations (logarithmic runtime where required)

### 🚀 Usage

Compile the class:

```bash
javac BinHeap.java
```

Then run the interactive test program:

```bash
java BinHeapTest
```

#### 🧪 Test Program Commands

The test program supports the following commands:

| Command | Description                       |
| ------- | --------------------------------- |
| `+ a`   | Insert entry with priority `a`    |
| `- 1`   | Remove entry at index 1           |
| `?`     | Show entry with minimum priority  |
| `!`     | Extract and remove minimum entry  |
| `= 1 a` | Change priority of entry 1 to `a` |
| `#`     | Check if heap is empty            |
| `& 1`   | Check if heap contains entry 1    |

🔢 Priorities can be strings (e.g., `a`, `b`, `z`) and data values are integers (auto-assigned during insert).

Example input sequence:

```text
+ a
+ b
+ c
?
!
= 0 z
```

### 📝 Notes

* 🧑‍💻 Pure Java, no external libraries
* 🛠 No `package` declarations used
* 📚 Follows binomial heap logic from lecture slides
* 📈 Uses helper methods for merging heaps and linking trees


## 📦 HuffmanEncoding

### 🚀 Overview

This Java class **Huffman** implements Huffman coding for lossless text compression.

It can:

- 🔢 Calculate character frequencies
- 🌲 Build a Huffman prefix code tree
- 🔐 Encode texts
- 🔓 Decode encoded texts
- 🛡️ Handle errors (e.g., null inputs)

### ⚙️ Key Methods

- `calculateFrequencies(String text)` – Count frequencies
- `constructPrefixCode(Integer[] frequencies)` – Build tree
- `canEncode(String text)` – Check if text can be encoded
- `encode(String text, boolean newPrefixCode)` – Encode text
- `decode(String encodedText)` – Decode text
- `dumpPrefixCodes()` – Print prefix codes

### 📝 Notes

- Uses BinHeap as helper (not included in submission)
- Recursive helpers for tree traversal
- Proper error handling with messages

### Example Usage

```bash
enc1 Abracadabra
Encoded Text: 101011111001011010001111100

dec 101011111001011010001111100
Decoded Text: Abracadabra
```

---

# 📜 License

This project is licensed under the [Apache 2.0 License](LICENSE). Feel free to use it – just give proper credit. 🤝
