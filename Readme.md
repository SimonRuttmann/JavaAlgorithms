# 📘 README: BinHeap.java

## 🧩 Overview

This project implements a generic minimum-priority queue using binomial heaps. 
It supports elements with any comparable priority type `P` and any data type `D`.

## ✅ Features

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

## 🚀 Usage

Compile the class:

```bash
javac BinHeap.java
```

Then run the interactive test program:

```bash
java BinHeapTest
```

### 🧪 Test Program Commands

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

## 📝 Notes

* 🧑‍💻 Pure Java, no external libraries
* 🛠 No `package` declarations used
* 📚 Follows binomial heap logic from lecture slides
* 📈 Uses helper methods for merging heaps and linking trees



# 📘 README: hashing.java

## 🧩 Overview

This project is a basic Java implementation of hash tables. It includes:

* 🔢 **Hash functions**: Division & Multiplication
* 🔁 **Probing strategies**: Linear, Quadratic, Double Hashing
* 📦 **Hash tables**:

    * With **chaining**
    * With **open addressing**
* 🧪 A test program: `WordCount` that counts word frequencies using different configurations

## ✅ Features

* ❌ No use of `java.util` or package declarations
* 🧠 Fully implemented methods: `compute`, `first`, `next`, `put`, `get`, `remove`, `dump`
* ➖ Correct handling of negative `hashCode()` values
* 📾 Method behavior follows the given specs exactly

## 🚀 Usage

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

## 📝 Notes

* ⚙️ 100% pure Java (no external libraries)
* 🔑 Can be used with different key types (not just strings)
