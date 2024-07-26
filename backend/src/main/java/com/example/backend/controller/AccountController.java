package com.example.backend.controller;

import com.example.backend.entity.RestBean;
import com.example.backend.entity.dto.Account;
import com.example.backend.entity.dto.AccountDetails;
import com.example.backend.entity.vo.request.DetailsSaveVO;
import com.example.backend.entity.vo.request.ModifyEmailVO;
import com.example.backend.entity.vo.response.AccountDetailsVO;
import com.example.backend.entity.vo.response.AccountVO;
import com.example.backend.service.AccountDetailsService;
import com.example.backend.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Resource
    AccountService service;
    @Resource
    AccountDetailsService detailsService;

    @GetMapping("/info")
    public RestBean<AccountVO> info(@RequestAttribute("id") int id){
        Account account = service.findAccountById(id);
        return RestBean.success(account.asViewObject(AccountVO.class));
    }

    @GetMapping("/details")
    public RestBean<AccountDetailsVO> details(@RequestAttribute("id") int id){
        AccountDetails details = Optional
                .ofNullable(detailsService.findAccountDetailsById(id))
                .orElseGet(AccountDetails::new);
        return RestBean.success(details.asViewObject(AccountDetailsVO.class));
    }

    @PostMapping("save-details")
    public RestBean<Void> saveDetails(@RequestAttribute("id") int id,
                                      @RequestBody @Valid DetailsSaveVO vo){
        boolean success = detailsService.saveAccountDetails(id,vo);
        return success ? RestBean.success():RestBean.failure(400,"此用户名已被其他用户使用");
    }

    @PostMapping("/modify-email")
    public  RestBean<Void> modifyEmail(@RequestAttribute("id") int id,
                                       @RequestBody @Valid ModifyEmailVO vo){
        String result = service.modifyEmail(id, vo);
        return result == null?RestBean.success():RestBean.failure(400,result);
    }
}
