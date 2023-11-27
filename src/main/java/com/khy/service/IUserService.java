package com.khy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.khy.dto.LoginFormDTO;
import com.khy.dto.Result;
import com.khy.entity.User;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author khy
 * @since 2021-12-22
 */
public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);

    Result sign();

    Result signCount();

}
