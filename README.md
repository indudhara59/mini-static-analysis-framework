# Mini Static Analysis Framework

A small Maven-based Java project for experimenting with classical dataflow analyses on a simple hand-built control flow graph.

## Project Status

Work in progress.

## Technologies

- Java 17
- Maven
- Java collections: `Set`, `Map`, `List`

## What is dataflow analysis?

Dataflow analysis is a compiler technique for computing facts about a program at different program points. An analysis repeatedly propagates information through a control flow graph until the information stops changing. This project demonstrates that idea with small, readable implementations rather than a full compiler front end.

## Current Features

- Simple intermediate representation for instructions
- Basic blocks with predecessor and successor links
- A small control flow graph API
- Generic dataflow analysis interface
- Reaching Definitions Analysis
- Live Variable Analysis
- Built-in example CFG that runs without external input

## Planned Features

- A tiny parser for example programs
- More analyses, such as available expressions
- Unit tests for the fixed-point implementations
- DOT export for visualizing control flow graphs

## How to Build

```bash
mvn compile
```

## How to Run

```bash
mvn exec:java
```

You can also run the main class directly from an IDE:

```text
com.indudhara.staticanalysis.Main
```

## Example Output

The program prints the sample control flow graph and then reports `IN` and `OUT` sets for each block:

```text
=== Reaching Definitions Analysis ===
B1
  IN : []
  OUT: [x@B1.1, y@B1.2]

=== Live Variable Analysis ===
B1
  IN : []
  OUT: [x, y]
```

The exact ordering may differ because the analyses use sets.

## Limitations

- The CFG is constructed manually in Java.
- Instructions model only defined variables, used variables, and display text.
- Reaching definitions are represented as strings such as `x@B1.1`.
- There is no parser, type system, alias analysis, or interprocedural analysis.
- The project is intended for learning dataflow concepts, not for analyzing real Java source code.
