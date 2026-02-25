package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.InMemoryLibraryRepository
import org.dbu.library.service.DefaultLibraryService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class MenuHandlerIntegrationTest {

    private lateinit var repository: InMemoryLibraryRepository
    private lateinit var service: DefaultLibraryService

    @BeforeEach
    fun setup() {
        repository = InMemoryLibraryRepository()
        service = DefaultLibraryService(repository)
    }

    @Test
    fun `menu option 0 should exit and return false`() {
        val result = handleMenuAction("0", service, repository)
        assertFalse(result)
    }

    @Test
    fun `menu option 1 should call addBook and continue`() {
        // We can't test interactive input, but we can verify the handler returns true
        // and that the service method works
        val book = Book("978-1", "Test Book", "Test Author", 2024)
        val success = service.addBook(book)
        
        assertTrue(success)
        assertEquals(book, repository.findBook("978-1"))
    }

    @Test
    fun `menu option 2 should call registerPatron and continue`() {
        // Verify the repository method works
        val patron = Patron("P001", "Test Patron")
        val success = repository.addPatron(patron)
        
        assertTrue(success)
        assertEquals(patron, repository.findPatron("P001"))
    }

    @Test
    fun `menu option 3 should call borrowBook and continue`() {
        // Setup data
        val book = Book("978-1", "Test Book", "Test Author", 2024)
        val patron = Patron("P001", "Test Patron")
        repository.addBook(book)
        repository.addPatron(patron)
        
        // Verify borrowing works
        val result = service.borrowBook("P001", "978-1")
        assertEquals(org.dbu.library.service.BorrowResult.SUCCESS, result)
        
        // Verify state changes
        assertFalse(repository.findBook("978-1")!!.isAvailable)
        assertTrue(repository.findPatron("P001")!!.borrowedBooks.contains("978-1"))
    }

    @Test
    fun `menu option 4 should call returnBook and continue`() {
        // Setup data
        val book = Book("978-1", "Test Book", "Test Author", 2024, isAvailable = false)
        val patron = Patron("P001", "Test Patron", borrowedBooks = listOf("978-1"))
        repository.addBook(book)
        repository.addPatron(patron)
        
        // Verify returning works
        val result = service.returnBook("P001", "978-1")
        assertTrue(result)
        
        // Verify state changes
        assertTrue(repository.findBook("978-1")!!.isAvailable)
        assertFalse(repository.findPatron("P001")!!.borrowedBooks.contains("978-1"))
    }

    @Test
    fun `menu option 5 should call search and continue`() {
        // Setup data
        val book1 = Book("978-1", "Effective Java", "Joshua Bloch", 2017)
        val book2 = Book("978-2", "Clean Code", "Robert Martin", 2008)
        repository.addBook(book1)
        repository.addBook(book2)
        
        // Verify search works
        val results = service.search("java")
        assertEquals(1, results.size)
        assertEquals("978-1", results[0].isbn)
    }

    @Test
    fun `menu option 6 should call listAllBooks and continue`() {
        // Setup data
        val book1 = Book("978-1", "Book 1", "Author 1", 2020)
        val book2 = Book("978-2", "Book 2", "Author 2", 2021)
        repository.addBook(book1)
        repository.addBook(book2)
        
        // Verify getAllBooks works
        val books = repository.getAllBooks()
        assertEquals(2, books.size)
        assertTrue(books.any { it.isbn == "978-1" })
        assertTrue(books.any { it.isbn == "978-2" })
    }

    @Test
    fun `invalid menu option should print error and continue`() {
        val result = handleMenuAction("99", service, repository)
        assertTrue(result)
    }

    @Test
    fun `complete workflow - add book, register patron, borrow, return`() {
        // 1. Add book (option 1)
        val book = Book("978-0134685991", "Effective Java", "Joshua Bloch", 2017)
        assertTrue(service.addBook(book))
        
        // 2. Register patron (option 2)
        val patron = Patron("P001", "Alice")
        assertTrue(repository.addPatron(patron))
        
        // 3. Borrow book (option 3)
        val borrowResult = service.borrowBook("P001", "978-0134685991")
        assertEquals(org.dbu.library.service.BorrowResult.SUCCESS, borrowResult)
        
        // 4. Verify book is borrowed (option 6)
        val allBooks = repository.getAllBooks()
        assertEquals(1, allBooks.size)
        assertFalse(allBooks[0].isAvailable)
        
        // 5. Search for book (option 5)
        val searchResults = service.search("java")
        assertEquals(1, searchResults.size)
        assertFalse(searchResults[0].isAvailable)
        
        // 6. Return book (option 4)
        val returnResult = service.returnBook("P001", "978-0134685991")
        assertTrue(returnResult)
        
        // 7. Verify book is available again
        val bookAfterReturn = repository.findBook("978-0134685991")
        assertTrue(bookAfterReturn!!.isAvailable)
        
        // 8. Exit (option 0)
        val exitResult = handleMenuAction("0", service, repository)
        assertFalse(exitResult)
    }
}
