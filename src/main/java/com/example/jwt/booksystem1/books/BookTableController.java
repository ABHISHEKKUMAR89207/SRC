package com.example.jwt.booksystem1.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booktables")
public class BookTableController {


    @Autowired
    private BookTableRepository bookTableRepository;

    // to get all the book tables
    @GetMapping
    public List<BookTable> getAllBookTables() {
        return bookTableRepository.findAll();
    }

    // to get book table by Id
    @GetMapping("/{id}")
    public BookTable getBookTableById(@PathVariable Long id) {
        return bookTableRepository.findById(id).orElse(null);
    }

    //to create the book table
    @PostMapping
    public BookTable createBookTable(@RequestBody BookTable bookTable) {
        return bookTableRepository.save(bookTable);
    }

    // to update the books table
    @PutMapping("/{id}")
    public BookTable updateBookTable(@PathVariable Long id, @RequestBody BookTable updatedBookTable) {
        System.out.println("bhosat" + updatedBookTable);
        if (bookTableRepository.existsById(id)) {
            BookTable existingBookTable = bookTableRepository.findById(id).orElse(null);

            if (existingBookTable != null) {
                // Update the properties of existingBookTable with the values from updatedBookTable
                existingBookTable.setTitle(updatedBookTable.getTitle());
                existingBookTable.setAuthor(updatedBookTable.getAuthor());
                existingBookTable.setYear(updatedBookTable.getYear());
                existingBookTable.setQuantity(updatedBookTable.getQuantity());
                existingBookTable.setPrice(updatedBookTable.getPrice());

                return bookTableRepository.save(existingBookTable);
            } else {
                // Handle not found scenario
                return null;
            }
        } else {
            // Handle not found scenario
            return null;
        }
    }

    // to delete the books table
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookTable(@PathVariable Long id) {
        if (bookTableRepository.existsById(id)) {
            bookTableRepository.deleteById(id);
            return ResponseEntity.ok("BookTable with ID " + id + " deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("BookTable with ID " + id + " not found");
        }
    }

    // to get the total amount of the book that are ordered
    @PostMapping("/totalAmount")
    public ResponseEntity<Double> calculateTotalAmount(@RequestBody CartRequest cartRequest) {
        List<String> cartEntries = cartRequest.getCart();
        double totalAmount = calculateTotalAmountFromDatabase(cartEntries);
        return ResponseEntity.ok(totalAmount);
    }

    // to calculate the total amount of the books
    private double calculateTotalAmountFromDatabase(List<String> cartEntries) {
        double totalAmount = 0;
        for (String cartEntry : cartEntries) {
            // Split the entry into book ID and quantity
            String[] parts = cartEntry.split("x");

            if (parts.length == 2) {
                // Extract book ID and quantity
                Long bookId = Long.parseLong(parts[0]);
                int quantity = Integer.parseInt(parts[1]);

                // Find the book by ID in the database
                BookTable dbBook = bookTableRepository.findById(bookId).orElse(null);

                if (dbBook != null) {
                    totalAmount += dbBook.getPrice() * quantity;
                }
                // If the book is not found, you might want to handle it accordingly
            }
        }
        return totalAmount;
    }
}
