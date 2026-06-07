# Data Structure Projects

This repository contains four Java and JavaFX data structure projects developed across multiple phases. Together, the phases demonstrate the progression from linked-list-based record management to tree, hashing, and heap-based solutions, presented through desktop graphical interfaces and file-driven workflows.

## Phases Overview

| Phase | Description | Data Structures | Technologies | Folder Link |
|---|---|---|---|---|
| Phase 1 | Manages martyr records organized by district and location, with navigation, statistics, search, and CRUD operations. | Circular doubly linked list, circular linked lists, sorted insertion | Java, JavaFX, file handling | [View Phase 1](phase1/) |
| Phase 2 | Extends district, location, and martyr-record management with JavaFX screens for navigation, searching, and statistical reporting. | Circular doubly linked list, circular singly linked lists | Java, JavaFX, CSS, file handling | [View Phase 2](phase2/) |
| Phase 3 | Organizes records hierarchically by district, location, and event date and supports tree traversal and record management. | Binary search trees, circular linked list, stack, queue | Java, JavaFX, CSS, file handling | [View Phase 3](phase3/) |
| Phase 4 | Organizes records by event date and supports efficient lookup, balanced storage, statistics, and age-based sorting. | Hash table, quadratic probing, rehashing, AVL tree, min heap, heap sort | Java, JavaFX, CSS, file handling | [View Phase 4](phase4/) |

## Phase Summaries

### Phase 1

Phase 1 is a JavaFX martyr-record management application. Records are grouped by district and location, allowing users to load data, navigate records, view statistics, search, and perform insertion, update, and deletion operations.

The phase uses a circular doubly linked list for districts and circular linked lists for locations and martyr records, with sorted insertion and custom traversal operations.

Detailed documentation: [Phase 1 README](phase1/README.md)

### Phase 2

Phase 2 provides a JavaFX application for district, location, and martyr management from a comma-separated input file. It includes navigation, record operations, date and name searches, and district/location statistics.

Its data model uses a circular doubly linked list for districts and circular singly linked lists for locations and martyr records.

Detailed documentation: [Phase 2 README](phase2/README.md)

### Phase 3

Phase 3 introduces hierarchical tree-based organization. Districts, locations, and event dates are stored in binary search trees, while records are maintained in a circular linked list.

The project also demonstrates stack-based traversal and queue-based level-order traversal through its JavaFX record-management interface.

Detailed documentation: [Phase 3 README](phase3/README.md)

### Phase 4

Phase 4 focuses on hashing, balanced trees, and heap operations. A hash table organizes records by event date using quadratic probing and rehashing. Each date stores its records in an AVL tree, while a min heap supports age-based sorting and heap sort operations.

Detailed documentation: [Phase 4 README](phase4/README.md)

## Technologies

- Java
- JavaFX
- CSS
- IntelliJ IDEA
- File handling
- Data structures

## How to Run

Each phase is an independent IntelliJ IDEA project and has its own `README.md` with the relevant main class, input format, project setup, and detailed run instructions.

### General Requirements

- JDK 26
- JavaFX SDK 26
- IntelliJ IDEA

Open the required phase folder in IntelliJ IDEA, configure the JDK and JavaFX SDK, and follow that phase's README:

- [Phase 1 run instructions](phase1/README.md#running-in-intellij-idea)
- [Phase 2 run instructions](phase2/README.md#running-in-intellij-idea)
- [Phase 3 run instructions](phase3/README.md#running-in-intellij-idea)
- [Phase 4 run instructions](phase4/README.md#running-in-intellij-idea)

### General JavaFX VM Options

```text
--enable-native-access=javafx.graphics --module-path "C:\javafx-sdk-26.0.1\lib" --add-modules javafx.controls,javafx.fxml
```

Update the module path if the JavaFX SDK is installed in a different location.

## Repository Structure

```text
DataStructure-Projects/
├── README.md
├── phase1/
├── phase2/
├── phase3/
└── phase4/
```

## Author

Julia Duaibes
