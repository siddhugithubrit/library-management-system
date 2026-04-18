package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    // ✅ Issue Book
    @PostMapping("/{bookId}/{memberId}")
    public String issueBook(@PathVariable int bookId, @PathVariable int memberId) {

        Book book = bookRepository.findById(bookId).orElse(null);

        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        if (book.getQuantity() <= 0) {
            return "Book not available";
        }

        // reduce quantity
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        // create issue (WITHOUT member)
        Issue issue = new Issue();
        issue.setBook(book);
        issue.setIssueDate(LocalDate.now());
        issue.setDueDate(LocalDate.now().plusDays(7));
        issue.setFine(0);

        issueRepository.save(issue);

        return "Book issued successfully";
    }
    // ✅ Return Book
    @DeleteMapping("/return/{issueId}")
    public String returnBook(@PathVariable int issueId) {

        System.out.println("Return API called"); // 👈 ADD THIS

        Issue issue = issueRepository.findById(issueId).orElse(null);

        if (issue == null) {
            return "Issue not found";
        }

        Book book = issue.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        issueRepository.delete(issue);

        return "Book returned successfully";
    }    // ✅ Get all issues
    @GetMapping
    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    // ✅ Get issues by member
    
}