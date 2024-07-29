package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.entity.dto.Account;
import com.example.backend.entity.vo.request.*;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByNameOrEmail(String text);

    Account findAccountById(int id);

    String registerEmailVerifyCode(String type,String email,String ip);

    String registerEmailAccount(EmailRegisterVO vo);

    String resetConfirm(ConfirmResetVO vo);

    String resetEmailAccountPassword(EmailResetVO vo);

    String modifyEmail(int id, ModifyEmailVO vo);

    String changePassword(int id, ChangePasswordVO vo);
}
