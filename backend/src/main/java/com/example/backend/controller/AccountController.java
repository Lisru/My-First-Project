package com.example.backend.controller;

import com.example.backend.entity.RestBean;
import com.example.backend.entity.dto.Account;
import com.example.backend.entity.dto.AccountDetails;
import com.example.backend.entity.vo.request.ChangePasswordVO;
import com.example.backend.entity.vo.request.DetailsSaveVO;
import com.example.backend.entity.vo.request.ModifyEmailVO;
import com.example.backend.entity.vo.request.PrivacySaveVO;
import com.example.backend.entity.vo.response.AccountDetailsVO;
import com.example.backend.entity.vo.response.AccountPrivacyVO;
import com.example.backend.entity.vo.response.AccountVO;
import com.example.backend.service.AccountDetailsService;
import com.example.backend.service.AccountPrivacyService;
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
    @Resource
    AccountPrivacyService privacyService;

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

    /**
     * 修改密码
     * @param id
     * @param vo
     * @return
     */
    @PostMapping("/change-password")
    public RestBean<Void> changePassword(@RequestAttribute("id") int id,
                                         @RequestBody @Valid ChangePasswordVO vo){
        String result = service.changePassword(id, vo);
        return result == null?RestBean.success():RestBean.failure(400,result);
    }

    @PostMapping("/save-privacy")
    public RestBean<Void> savePrivacy(@RequestAttribute("id") int id,
                                      @RequestBody @Valid PrivacySaveVO vo){
        privacyService.savePrivacy(id,vo);
        return RestBean.success();
    }

    @GetMapping("/privacy")
    public RestBean<AccountPrivacyVO> privacy(@RequestAttribute("id") int id){
        return RestBean.success(privacyService.accountPrivacy(id).asViewObject(AccountPrivacyVO.class));
    }
}
