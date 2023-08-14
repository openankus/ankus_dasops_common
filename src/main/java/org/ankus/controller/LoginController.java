package org.ankus.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ankus.model.User;
import org.ankus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping({"/"})
    public String login(HttpServletRequest request){
        if(userService.security_yn(request)) return "redirect:workflow_list";
        return "login/login";
    }

    @GetMapping(value = "join")
    public String join(){
        return "login/signup";
    }

    @PostMapping(value = "join/save")
    @ResponseBody
    public String joinSave(User user){
        userService.registerNewUser(user);
        return "완료";
    }

    @PostMapping(value = "join/id_check")
    @ResponseBody
    public String getloginId(@RequestParam String loginId){
        return userService.getloginId(loginId);
    }


    @GetMapping(value = "mypage")
    public String mypage(Model model,Principal principal){
        model.addAttribute("user",userService.userInfo(principal.getName()).get(0));
        model.addAttribute("server_name",userService.getServer_name());
        return "login/mypage";
    }

    @RequestMapping(value = "passwordChange", method = RequestMethod.POST)
    @ResponseBody
    public boolean id_pw(@RequestParam String loginId,@RequestParam String password,@RequestParam String newPassword){
        return userService.passwordChange(loginId,password,newPassword);
    }

    @GetMapping(value="userDelete")
    public String userDelete(@RequestParam String loginId){
        userService.userDelete(loginId);
        return "redirect:/";
    }
    @RequestMapping(value="userListDelete",method = RequestMethod.POST)
    @ResponseBody
    public String userListDelete(@RequestParam(value = "ids[]") List<Long> ids){
        return userService.userListDelete(ids);
    }

    @RequestMapping(value = "userInfo",method = RequestMethod.POST)
    @ResponseBody
    public User userInfo(@RequestParam String loginId){
        return userService.userInfo(loginId).get(0);
    }

    @RequestMapping(value="userUpdate",method = RequestMethod.POST)
    @ResponseBody
    public String userUpdate(@RequestBody User user){
        return userService.userUpdate(user);
    }
}
