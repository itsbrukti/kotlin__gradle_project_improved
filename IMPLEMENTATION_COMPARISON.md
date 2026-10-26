# Implementation Comparison

## 1. Introduction

This document compares the original student implementation of the Kotlin Library Management System with the improved version. The purpose of the comparison is to identify existing functionality, explain the changes made, and describe how the changes improve the application's reliability.

## 2. Feature Comparison

| Feature | Original Implementation | Improved Implementation |
|---|---|---|
| Add books | Accepts book information without checking for blank fields | Rejects blank ISBN, title, and author fields |
| Publication year | Checks whether the year is an integer | Also checks that the year is between 1 and 2026 |
| Register patrons | Accepts patron details without checking for blank fields | Rejects blank patron IDs and names |
| Borrow books | Supports borrowing validation and a three-book limit | Preserves existing business rules and rejects blank patron IDs and ISBNs |
| Return books | Supports returning previously borrowed books | Preserves existing return logic and rejects blank patron IDs and ISBNs |
| Search books | Searches titles and authors without case sensitivity | Preserves case-insensitive searching and rejects empty or whitespace-only queries |
| Duplicate records | Prevents duplicate book ISBNs and patron IDs | Preserves this behavior |

## 3. Main Improvements

### 3.1 Input Validation

The improved console interface uses `trim()` to remove unnecessary spaces from user input and checks whether required values are empty.

For example, a book cannot be added with an empty title or author. Patron registration also rejects empty IDs and names.

### 3.2 Publication Year Validation

The improved version checks that the publication year is a valid integer within the selected range of 1–2026. Invalid input is rejected with an explanatory message.

### 3.3 Borrowing and Returning Validation

The improved version checks that patron IDs and ISBNs are not blank before calling the library service. This prevents incomplete input from being passed to the business logic.

### 3.4 Search Validation

The improved version rejects empty and whitespace-only search queries. Valid searches continue to match book titles and authors without case sensitivity.

## 4. Existing Functionality Preserved

The following features were already present in the original student implementation and were preserved:

- The maximum borrowing limit of three books per patron.
- Checks for missing patrons and books.
- Book availability checks during borrowing.
- Return-book validation.
- Case-insensitive searching by title or author.
- Prevention of duplicate book ISBNs and patron IDs.

These features were tested during development to verify that the improvements did not break the existing functionality.

## 5. Testing

The following behaviors were checked manually during development:

- Building the project successfully with Gradle.
- Running the console application.
- Rejecting blank book information.
- Rejecting invalid publication years.
- Rejecting blank patron information.
- Searching for books by title or author.
- Enforcing the existing three-book borrowing limit.
- Returning a borrowed book and borrowing another book afterward.

The build succeeded after the input-validation changes, and the manual checks described above passed.

## 6. Conclusion

The improved implementation focuses on making the console interface more reliable by validating user input before processing it. The changes reduce invalid entries while preserving the original library management features and business rules.
