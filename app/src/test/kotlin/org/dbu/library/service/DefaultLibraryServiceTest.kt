package org.dbu.library.service

import org.dbu.library.model.Book
import org.dbu.library.repository.InMemoryLibraryRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class DefaultLibraryServiceTest {

    private lateinit var repository: InMemoryLibraryRepository
    private lateinit var service: DefaultLibraryService

    @BeforeEach
    fun setup() {
        repository = InMemoryLibraryRepository()
        service = DefaultLibraryService(repository)
    }

    @Test
    fun `addBook should delegate to repository and return true for unique ISBN`() {
        val book = Book("978-0134685991", "Effective Java", "Joshua Bloch", 2017)
        
        val result = service.addBook(book)
        
        assertTrue(result)
        assertEquals(book, repository.findBook("978-0134685991"))
    }

    @Test
    fun `addBook should delegate to repository and return false for duplicate ISBN`() {
        val book1 = Book("978-0134685991", "Effective Java", "Joshua Bloch", 2017)
        val book2 = Book("978-0134685991", "Different Title", "Different Author", 2020)
        
        service.addBook(book1)
        val result = service.addBook(book2)
        
        assertFalse(result)
        assertEquals(book1, repository.findBook("978-0134685991"))
    }

    @Test
    fun `borrowBook should return SUCCESS when patron borrows available book`() {
        val book = Book("978-0134685991", "Effective Java", "Joshua Bloch", 2017, isAvailable = true)
        val patron = org.dbu.library.model.Patron("P001", "John Doe")
        
        repository.addBook(book)
        repository.addPatron(patron)
        
        val result = service.borrowBook("P001", "978-0134685991")
        
        assertEquals(BorrowResult.SUCCESS, result)
        assertFalse(repository.findBook("978-0134685991")!!.isAvailable)
        assertTrue(repository.findPatron("P001")!!.borrowedBooks.contains("978-0134685991"))
    }

    @Test
    fun `borrowBook should return PATRON_NOT_FOUND when patron does not exist`() {
        val book = Book("978-0134685991", "Effective Java", "Joshua Bloch", 2017)
        repository.addBook(book)
        
        val result = service.borrowBook("P999", "978-0134685991")
        
        assertEquals(BorrowResult.PATRON_NOT_FOUND, result)
        assertTrue(repository.findBook("978-0134685991")!!.isAvailable)
    }

    @Test
    fun `borrowBook should return BOOK_NOT_FOUND when book does not exist`() {
        val patron = org.dbu.library.model.Patron("P001", "John Doe")
        repository.addPatron(patron)
        
        val result = service.borrowBook("P001", "978-9999999999")
        
        assertEquals(BorrowResult.BOOK_NOT_FOUND, result)
        assertTrue(repository.findPatron("P001")!!.borrowedBooks.isEmpty())
    }

    @Test
    fun `borrowBook should return NOT_AVAILABLE when book is already borrowed`() {
        val book = Book("978-0134685991", "Effective Java", "Joshua Bloch", 2017, isAvailable = false)
        val patron = org.dbu.library.model.Patron("P001", "John Doe")
        
        repository.addBook(book)
        repository.addPatron(patron)
        
        val result = service.borrowBook("P001", "978-0134685991")
        
        assertEquals(BorrowResult.NOT_AVAILABLE, result)
        assertTrue(repository.findPatron("P001")!!.borrowedBooks.isEmpty())
    }

    @Test
    fun `borrowBook should return LIMIT_REACHED when patron has 3 books`() {
        val book1 = Book("978-1", "Book 1", "Author 1", 2020, isAvailable = false)
        val book2 = Book("978-2", "Book 2", "Author 2", 2020, isAvailable = false)
        val book3 = Book("978-3", "Book 3", "Author 3", 2020, isAvailable = false)
        val book4 = Book("978-4", "Book 4", "Author 4", 2020, isAvailable = true)
        val patron = org.dbu.library.model.Patron("P001", "John Doe", borrowedBooks = listOf("978-1", "978-2", "978-3"))
        
        repository.addBook(book1)
        repository.addBook(book2)
        repository.addBook(book3)
        repository.addBook(book4)
        repository.addPatron(patron)
        
        val result = service.borrowBook("P001", "978-4")
        
        assertEquals(BorrowResult.LIMIT_REACHED, result)
        assertTrue(repository.findBook("978-4")!!.isAvailable)
        assertEquals(3, repository.findPatron("P001")!!.borrowedBooks.size)
    }

    @Test
    fun `returnBook should return true and update state when patron returns borrowed book`() {
        val book = Book("978-0134685991", "Effective Java", "Joshua Bloch", 2017, isAvailable = false)
        val patron = org.dbu.library.model.Patron("P001", "John Doe", borrowedBooks = listOf("978-0134685991"))
        
        repository.addBook(book)
        repository.addPatron(patron)
        
        val result = service.returnBook("P001", "978-0134685991")
        
        assertTrue(result)
        assertTrue(repository.findBook("978-0134685991")!!.isAvailable)
        assertFalse(repository.findPatron("P001")!!.borrowedBooks.contains("978-0134685991"))
    }

    @Test
    fun `returnBook should return false when patron has not borrowed the book`() {
        val book = Book("978-0134685991", "Effective Java", "Joshua Bloch", 2017, isAvailable = false)
        val patron = org.dbu.library.model.Patron("P001", "John Doe", borrowedBooks = emptyList())
        
        repository.addBook(book)
        repository.addPatron(patron)
        
        val result = service.returnBook("P001", "978-0134685991")
        
        assertFalse(result)
        assertFalse(repository.findBook("978-0134685991")!!.isAvailable)
    }

    @Test
    fun `returnBook should return false when patron does not exist`() {
        val book = Book("978-0134685991", "Effective Java", "Joshua Bloch", 2017, isAvailable = false)
        repository.addBook(book)
        
        val result = service.returnBook("P999", "978-0134685991")
        
        assertFalse(result)
    }

    @Test
    fun `returnBook should return false when book does not exist`() {
        val patron = org.dbu.library.model.Patron("P001", "John Doe", borrowedBooks = listOf("978-9999999999"))
        repository.addPatron(patron)
        
        val result = service.returnBook("P001", "978-9999999999")
        
        assertFalse(result)
    }

    @Test
    fun `search should return books matching title case-insensitively`() {
        val book1 = Book("978-1", "Effective Java", "Joshua Bloch", 2017)
        val book2 = Book("978-2", "Java Concurrency", "Brian Goetz", 2006)
        val book3 = Book("978-3", "Clean Code", "Robert Martin", 2008)
        
        repository.addBook(book1)
        repository.addBook(book2)
        repository.addBook(book3)
        
        val results = service.search("java")
        
        assertEquals(2, results.size)
        assertTrue(results.any { it.isbn == "978-1" })
        assertTrue(results.any { it.isbn == "978-2" })
    }

    @Test
    fun `search should return books matching author case-insensitively`() {
        val book1 = Book("978-1", "Effective Java", "Joshua Bloch", 2017)
        val book2 = Book("978-2", "Java Concurrency", "Brian Goetz", 2006)
        val book3 = Book("978-3", "Clean Code", "Robert Martin", 2008)
        
        repository.addBook(book1)
        repository.addBook(book2)
        repository.addBook(book3)
        
        val results = service.search("martin")
        
        assertEquals(1, results.size)
        assertEquals("978-3", results[0].isbn)
    }

    @Test
    fun `search should return empty list when no books match`() {
        val book1 = Book("978-1", "Effective Java", "Joshua Bloch", 2017)
        repository.addBook(book1)
        
        val results = service.search("python")
        
        assertTrue(results.isEmpty())
    }

    @Test
    fun `search should return book once even if both title and author match`() {
        val book = Book("978-1", "Martin Fowler on Refactoring", "Martin Fowler", 1999)
        repository.addBook(book)
        
        val results = service.search("martin")
        
        assertEquals(1, results.size)
        assertEquals("978-1", results[0].isbn)
    }
}
