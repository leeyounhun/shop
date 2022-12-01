package com.lyh.shop.controller;

import com.lyh.shop.dto.MemberFormDto;
import com.lyh.shop.entity.Member;
import com.lyh.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberRestController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ModelAndView memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        if(bindingResult.hasErrors()){
            modelAndView.setViewName("member/memberForm");
            return modelAndView;
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            modelAndView.setViewName("member/memberForm");
            modelAndView.addObject("errorMessage", e.getMessage());
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
