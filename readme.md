# Library Management System — Kotlin

## Project Overview

This is a Kotlin-based console application for managing a library. It was developed as part of a Kotlin and Gradle programming exercise.

The application allows users to add books, register patrons, borrow and return books, search for books, and view the library's books.

## Features

- Add books using ISBN, title, author, and publication year.
- Register library patrons using an ID and name.
- Borrow available books.
- Enforce a maximum borrowing limit of three books per patron.
- Return books borrowed by patrons.
- Search for books by title or author, without case sensitivity.
- List books and display their availability.
- Reject duplicate book ISBNs and patron IDs.
- Validate user input and reject empty required fields.
- Validate book publication years using the selected range of 1–2026.

## Technologies Used

- Kotlin
- Gradle
- Java Development Kit (JDK)

## Project Structure

The project follows a layered architecture:

- `model/` — contains data classes such as `Book` and `Patron`.
- `repository/` — manages the storage and retrieval of books and patrons.
- `service/` — implements library business rules, including borrowing and returning books.
- `ui/` — provides the console menu and handles user input.

## Prerequisites

Install a compatible JDK and use the Gradle Wrapper included in the project.

Useful learning resources:

- [Gradle Getting Started Guide](https://docs.gradle.org/current/userguide/part1_gradle_init.html)
- [Kotlin Getting Started Guide](https://kotlinlang.org/docs/getting-started.html)

## How to Build and Run

Open a terminal in the project root directory.

### Build the project

On Windows PowerShell:

```powershell
.\gradlew.bat clean build
```

### Run the application

```powershell
.\gradlew.bat run
```

Follow the numbered menu displayed in the terminal to use the application.

## Data Storage

The application uses an in-memory repository. Books and patron records are stored while the application is running. They are not permanently saved to a database or file, so the data is lost when the application stops.

## Improvements in This Version

This version improves input validation in the console interface:

- Rejects blank ISBNs, titles, and author names.
- Rejects invalid publication years outside the selected range of 1–2026.
- Rejects blank patron IDs and names.
- Rejects blank patron IDs and ISBNs during borrowing and returning.
- Rejects empty or whitespace-only search queries.

The three-book borrowing limit and the core library operations were already present in the original student implementation and have been preserved.

## Future Enhancements

Possible future improvements include persistent database storage, automated unit tests, patron email and phone fields, and a menu option to view all patrons and their borrowed books.

## Submission

This repository contains the improved version of the Library Management System for the Kotlin project challenge.
