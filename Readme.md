# 📘 README: hashing.java

## 🧩 Overview

This project is a basic Java implementation of hash tables, created for a university assignment. It includes:

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
