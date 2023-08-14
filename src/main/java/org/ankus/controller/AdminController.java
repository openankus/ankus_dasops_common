package org.ankus.controller;

import org.ankus.service.AdminService;
import org.ankus.service.UserService;
import org.ankus.util.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  사용자 관리 기능을 다루는 컨트롤러
 */
@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String userList(Model model){
        model.addAttribute("server_name",userService.getServer_name());
        return "admin/userList";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "common/accessDenied";
    }


    // 유저 목록
    @RequestMapping(value = "/user_select_pageable", method = RequestMethod.POST)
    @ResponseBody
    public DataTable userSelectPageable(DataTable dto, @RequestBody MultiValueMap<String, String> formData){

        return adminService.userSelectPagealbe(dto,formData);
    }

    // 유저 활성화
    @RequestMapping(value = "enabledUpdate", method = RequestMethod.POST)
    @ResponseBody
    public void enabledUpdate(@RequestParam boolean enabled, @RequestParam String loginId){
        adminService.enabledUpdate(enabled,loginId);
    }

    // 유저 삭제
    @RequestMapping(value="userDelete")
    @ResponseBody
    public void userDelete(@RequestParam(value="loginIds[]") List<String> loginIds){
        adminService.userDelete(loginIds);
    }

    // 패스워드 수정
    @RequestMapping(value="passwordUpdate")
    @ResponseBody
    public String passwordUpdate(@RequestParam String userId,@RequestParam String password){
        return adminService.passwordUpdate(userId,password);
    }

    // 사용자 권한 수정
    @RequestMapping(value="userRoleUpdate")
    @ResponseBody
    public void userRoleUpdate(@RequestParam String userId, @RequestParam(value = "role[]") List<String> role){
        adminService.userRoleUpdate(userId,role);
    }


}
