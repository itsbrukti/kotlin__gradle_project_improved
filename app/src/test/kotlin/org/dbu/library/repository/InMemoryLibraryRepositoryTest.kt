package org.dbu.library.repository

import org.dbu.library.model.Patron
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class InMemoryLibraryRepositoryTest {

    private lateinit var repository: InMemoryLibraryRepository

    @BeforeEach
    fun setup() {
        repository = InMemoryLibraryRepository()
    }

    @Test
    fun `addPatron should store patron with unique ID and return true`() {
        val patron = Patron("P001", "John Doe")
        
        val result = repository.addPatron(patron)
        
        assertTrue(result)
        assertEquals(patron, repository.findPatron("P001"))
    }

    @Test
    fun `addPatron should reject duplicate patron ID and return false`() {
        val patron1 = Patron("P001", "John Doe")
        val patron2 = Patron("P001", "Jane Smith")
        
        repository.addPatron(patron1)
        val result = repository.addPatron(patron2)
        
        assertFalse(result)
        assertEquals(patron1, repository.findPatron("P001"))
    }

    @Test
    fun `findPatron should return patron if ID exists`() {
        val patron = Patron("P001", "John Doe", listOf("ISBN123"))
        repository.addPatron(patron)
        
        val result = repository.findPatron("P001")
        
        assertEquals(patron, result)
    }

    @Test
    fun `findPatron should return null if ID does not exist`() {
        val result = repository.findPatron("P999")
        
        assertNull(result)
    }

    @Test
    fun `updatePatron should replace existing patron`() {
        val patron = Patron("P001", "John Doe")
        repository.addPatron(patron)
        
        val updatedPatron = Patron("P001", "John Doe", listOf("ISBN123", "ISBN456"))
        repository.updatePatron(updatedPatron)
        
        val result = repository.findPatron("P001")
        assertEquals(updatedPatron, result)
        assertEquals(2, result?.borrowedBooks?.size)
    }
}
