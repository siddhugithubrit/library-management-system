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
        Member member = memberRepository.findById(memberId).orElse(null);

        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        if (book.getQuantity() <= 0) {
            return "Book not available";
        }

        // reduce quantity
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        // create issue
        Issue issue = new Issue();
        issue.setBook(book);
        issue.setMember(member);
        issue.setIssueDate(LocalDate.now()); // ✅ only once
        issue.setDueDate(LocalDate.now().plusDays(7)); // due after 7 days
        issue.setFine(0);

        issueRepository.save(issue);

        return "Book issued successfully";
    }

    // ✅ Return Book
    @DeleteMapping("/return/{issueId}")
    public String returnBook(@PathVariable int issueId) {

        Issue issue = issueRepository.findById(issueId).orElse(null);

        if (issue == null) {
            return "Issue not found";
        }

        // increase book quantity
        Book book = issue.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        // calculate fine
     // calculate fine
        LocalDate today = LocalDate.now();

        issue.setReturnDate(today); // ✅ set return date

        if (today.isAfter(issue.getDueDate())) {
            long daysLate = java.time.temporal.ChronoUnit.DAYS
                    .between(issue.getDueDate(), today);

            double fine = daysLate * 10;
            issue.setFine(fine);
            System.out.println("Late return. Fine: " + fine);
        }

        // delete issue
        issueRepository.save(issue);

        return "Book returned successfully";
    }

    // ✅ Get all issues
    @GetMapping
    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    // ✅ Get issues by member
    @GetMapping("/member/{memberId}")
    public List<Issue> getIssuesByMember(@PathVariable int memberId) {
        return issueRepository.findByMember_MemberId(memberId);
    }
}