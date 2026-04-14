package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable int id, @RequestBody Book newBook) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(newBook.getTitle());
            book.setAuthor(newBook.getAuthor());
            book.setCategory(newBook.getCategory());
            book.setQuantity(newBook.getQuantity());
            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Book not found"));
    }
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id) {
        bookRepository.deleteById(id);
        return "Book deleted successfully";
    }
    @GetMapping("/search/{title}")
    public List<Book> searchBook(@PathVariable String title) {
        return bookRepository.findByTitle(title);
    }
}