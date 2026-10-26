package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService

fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {

    return when (choice) {

        "1" -> {
            addBook(service)
            true
        }

        "2" -> {
            registerPatron(repository)
            true
        }

        "3" -> {
            borrowBook(service)
            true
        }

        "4" -> {
            returnBook(service)
            true
        }

        "5" -> {
            search(service)
            true
        }

        "6" -> {
            listAllBooks(repository)
            true
        }

        "0" -> false

        else -> {
            println("Invalid option")
            true
        }
    }
}

/*private fun addBook(service: LibraryService) {
    println("\n=== Add Book ===")
    print("Enter ISBN: ")
    val isbn = readLine() ?: return
    print("Enter title: ")
    val title = readLine() ?: return
    print("Enter author: ")
    val author = readLine() ?: return
    print("Enter year: ")
    val year = readLine()?.toIntOrNull() ?: return
    
    val book = Book(isbn, title, author, year)
    val success = service.addBook(book)
    
    if (success) {
        println("✓ Book added successfully")
    } else {
        println("✗ Book with ISBN $isbn already exists")
    }
}*/

private fun addBook(service: LibraryService) {
    println("\n=== Add Book ===")

    print("Enter ISBN: ")
    val isbn = readLine()?.trim()
    if (isbn.isNullOrBlank()) {
        println("✗ ISBN cannot be empty")
        return
    }

    print("Enter title: ")
    val title = readLine()?.trim()
    if (title.isNullOrBlank()) {
        println("✗ Title cannot be empty")
        return
    }

    print("Enter author: ")
    val author = readLine()?.trim()
    if (author.isNullOrBlank()) {
        println("✗ Author cannot be empty")
        return
    }

    print("Enter year: ")
    val yearInput = readLine()?.trim()
    val year = yearInput?.toIntOrNull()

    if (year == null || year !in 1..2026) {
        println("✗ Please enter a valid year between 1 and 2026")
        return
    }

    val book = Book(isbn, title, author, year)
    val success = service.addBook(book)

    if (success) {
        println("✓ Book added successfully")
    } else {
        println("✗ Book with ISBN $isbn already exists")
    }
}


/*private fun registerPatron(repository: LibraryRepository) {
    println("\n=== Register Patron ===")
    print("Enter patron ID: ")
    val id = readLine() ?: return
    print("Enter name: ")
    val name = readLine() ?: return
    
    val patron = Patron(id, name)
    val success = repository.addPatron(patron)
    
    if (success) {
        println("✓ Patron registered successfully")
    } else {
        println("✗ Patron with ID $id already exists")
    }
}*/


private fun registerPatron(repository: LibraryRepository) {
    println("\n=== Register Patron ===")

    print("Enter patron ID: ")
    val id = readLine()?.trim()
    if (id.isNullOrBlank()) {
        println("✗ Patron ID cannot be empty")
        return
    }

    print("Enter name: ")
    val name = readLine()?.trim()
    if (name.isNullOrBlank()) {
        println("✗ Patron name cannot be empty")
        return
    }

    val patron = Patron(id, name)
    val success = repository.addPatron(patron)

    if (success) {
        println("✓ Patron registered successfully")
    } else {
        println("✗ Patron with ID $id already exists")
    }
}


/*private fun borrowBook(service: LibraryService) {
    println("\n=== Borrow Book ===")
    print("Enter patron ID: ")
    val patronId = readLine() ?: return
    print("Enter book ISBN: ")
    val isbn = readLine() ?: return
    
    val result = service.borrowBook(patronId, isbn)
    
    when (result) {
        BorrowResult.SUCCESS -> println("✓ Book borrowed successfully")
        BorrowResult.PATRON_NOT_FOUND -> println("✗ Patron not found")
        BorrowResult.BOOK_NOT_FOUND -> println("✗ Book not found")
        BorrowResult.NOT_AVAILABLE -> println("✗ Book is not available")
        BorrowResult.LIMIT_REACHED -> println("✗ Patron has reached borrow limit (3 books)")
    }
}*/


private fun borrowBook(service: LibraryService) {
    println("\n=== Borrow Book ===")

    print("Enter patron ID: ")
    val patronId = readLine()?.trim()
    if (patronId.isNullOrBlank()) {
        println("✗ Patron ID cannot be empty")
        return
    }

    print("Enter book ISBN: ")
    val isbn = readLine()?.trim()
    if (isbn.isNullOrBlank()) {
        println("✗ Book ISBN cannot be empty")
        return
    }

    val result = service.borrowBook(patronId, isbn)

    when (result) {
        BorrowResult.SUCCESS -> println("✓ Book borrowed successfully")
        BorrowResult.PATRON_NOT_FOUND -> println("✗ Patron not found")
        BorrowResult.BOOK_NOT_FOUND -> println("✗ Book not found")
        BorrowResult.NOT_AVAILABLE -> println("✗ Book is not available")
        BorrowResult.LIMIT_REACHED ->
            println("✗ Patron has reached borrow limit (3 books)")
    }
}


/*private fun returnBook(service: LibraryService) {
    println("\n=== Return Book ===")
    print("Enter patron ID: ")
    val patronId = readLine() ?: return
    print("Enter book ISBN: ")
    val isbn = readLine() ?: return
    
    val success = service.returnBook(patronId, isbn)
    
    if (success) {
        println("✓ Book returned successfully")
    } else {
        println("✗ Return failed (patron or book not found, or book not borrowed by patron)")
    }
}*/


private fun returnBook(service: LibraryService) {
    println("\n=== Return Book ===")

    print("Enter patron ID: ")
    val patronId = readLine()?.trim()
    if (patronId.isNullOrBlank()) {
        println("✗ Patron ID cannot be empty")
        return
    }

    print("Enter book ISBN: ")
    val isbn = readLine()?.trim()
    if (isbn.isNullOrBlank()) {
        println("✗ Book ISBN cannot be empty")
        return
    }

    val success = service.returnBook(patronId, isbn)

    if (success) {
        println("✓ Book returned successfully")
    } else {
        println("✗ Return failed (patron or book not found, or book not borrowed by patron)")
    }
}


/*private fun search(service: LibraryService) {
    println("\n=== Search Books ===")
    print("Enter search query (title or author): ")
    val query = readLine() ?: return
    
    val results = service.search(query)
    
    if (results.isEmpty()) {
        println("No books found matching '$query'")
    } else {
        println("\nFound ${results.size} book(s):")
        results.forEach { book ->
            val status = if (book.isAvailable) "Available" else "Borrowed"
            println("  [${book.isbn}] ${book.title} by ${book.author} (${book.year}) - $status")
        }
    }
}*/


private fun search(service: LibraryService) {
    println("\n=== Search Books ===")
    print("Enter search query (title or author): ")

    val query = readLine()?.trim()

    if (query.isNullOrBlank()) {
        println("✗ Search query cannot be empty")
        return
    }

    val results = service.search(query)

    if (results.isEmpty()) {
        println("No books found matching '$query'")
    } else {
        println("\nFound ${results.size} book(s):")
        results.forEach { book ->
            val status = if (book.isAvailable) "Available" else "Borrowed"
            println("  [${book.isbn}] ${book.title} by ${book.author} (${book.year}) - $status")
        }
    }
}


private fun listAllBooks(repository: LibraryRepository) {
    println("\n=== All Books ===")
    val books = repository.getAllBooks()
    
    if (books.isEmpty()) {
        println("No books in the library")
    } else {
        books.forEach { book ->
            val status = if (book.isAvailable) "Available" else "Borrowed"
            println("  [${book.isbn}] ${book.title} by ${book.author} (${book.year}) - $status")
        }
    }
}