package org.dbu.library.service

import org.dbu.library.model.Book
import org.dbu.library.repository.LibraryRepository

class DefaultLibraryService(
    private val repository: LibraryRepository
) : LibraryService {

    companion object {
        private const val MAX_BORROW_LIMIT = 3
    }

    override fun addBook(book: Book): Boolean {
        return repository.addBook(book)
    }

    override fun borrowBook(patronId: String, isbn: String): BorrowResult {
        // Validate patron exists
        val patron = repository.findPatron(patronId)
            ?: return BorrowResult.PATRON_NOT_FOUND
        
        // Validate book exists
        val book = repository.findBook(isbn)
            ?: return BorrowResult.BOOK_NOT_FOUND
        
        // Check book availability
        if (!book.isAvailable) {
            return BorrowResult.NOT_AVAILABLE
        }
        
        // Check patron borrow limit
        if (patron.borrowedBooks.size >= MAX_BORROW_LIMIT) {
            return BorrowResult.LIMIT_REACHED
        }
        
        // Update book availability to false
        val updatedBook = book.copy(isAvailable = false)
        repository.updateBook(updatedBook)
        
        // Add ISBN to patron's borrowedBooks list
        val updatedPatron = patron.copy(
            borrowedBooks = patron.borrowedBooks + isbn
        )
        repository.updatePatron(updatedPatron)
        
        return BorrowResult.SUCCESS
    }

    override fun returnBook(patronId: String, isbn: String): Boolean {
        // Validate patron and book exist
        val patron = repository.findPatron(patronId) ?: return false
        val book = repository.findBook(isbn) ?: return false
        
        // Check if patron has borrowed this book
        if (!patron.borrowedBooks.contains(isbn)) {
            return false
        }
        
        // Update book availability to true
        val updatedBook = book.copy(isAvailable = true)
        repository.updateBook(updatedBook)
        
        // Remove ISBN from patron's borrowedBooks list
        val updatedPatron = patron.copy(
            borrowedBooks = patron.borrowedBooks.filter { it != isbn }
        )
        repository.updatePatron(updatedPatron)
        
        return true
    }

    override fun search(query: String): List<Book> {
        val allBooks = repository.getAllBooks()
        val lowerQuery = query.lowercase()
        
        return allBooks.filter { book ->
            book.title.lowercase().contains(lowerQuery) || 
            book.author.lowercase().contains(lowerQuery)
        }.distinct()
    }
}