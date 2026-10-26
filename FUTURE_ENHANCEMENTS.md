# Future Enhancements

## 1. Introduction

The Library Management System currently supports basic library operations through a Kotlin console interface. Although the application works for its intended purpose, several improvements could make it more useful, secure, and reliable in the future.

## 2. Proposed Enhancements

### 2.1 Database Integration

The application currently stores books and patron records in memory. Integrating a database such as SQLite or MySQL would allow information to be saved permanently and remain available after the application closes.

### 2.2 Automated Testing

Automated unit tests could be added to verify important operations, including adding books, registering patrons, borrowing books, returning books, and searching. These tests would help identify errors when the code changes.

### 2.3 Extended Patron Information

The patron model could be extended to include email addresses and phone numbers. This would allow the library to maintain more complete patron records.

### 2.4 Patron Listing

A new menu option could display all registered patrons and the books each patron has borrowed. This would make it easier to monitor borrowing activity.

### 2.5 Improved Input Validation

Further validation could check the format of ISBNs, prevent unrealistic publication years, and provide clearer messages when users enter incorrect information.

### 2.6 Graphical User Interface

The console interface could eventually be replaced with a graphical user interface. Buttons, forms, and tables would make the application easier to use for people who are unfamiliar with command-line applications.

### 2.7 Borrowing History and Due Dates

The system could record borrowing dates, due dates, and return dates. This would support overdue-book tracking and help library staff manage book circulation.

### 2.8 User Roles and Authentication

The application could introduce librarian and administrator accounts with different permissions. Authentication would help protect library operations from unauthorized access.

## 3. Priority of Enhancements

The recommended implementation order is:

1. Add automated tests for existing functionality.
2. Introduce permanent database storage.
3. Add patron listing and extended patron information.
4. Implement due dates and borrowing history.
5. Develop a graphical interface and user authentication.

## 4. Conclusion

These enhancements would help transform the current console-based exercise into a more complete library management application. They are proposals for future development and are not claimed as features of the current implementation.
