package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    // ✅ Add Member
    @PostMapping
    public Member addMember(@RequestBody Member member) {
        return memberRepository.save(member);
    }

    // ✅ Get All Members
    @GetMapping
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}