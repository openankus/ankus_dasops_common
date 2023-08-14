package org.ankus.controller;

import org.ankus.model.Role;
import org.ankus.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleController {
    @Autowired
    private RoleService roleService;

    public Role getRole(@RequestParam String name){
        return roleService.getRole(name);
    }

    public Long roleInsert(@RequestParam Role role){
        return roleService.save(role);
    }

    @RequestMapping(value = "getNameList",method = RequestMethod.POST)
    @ResponseBody
    public List<Role> getNameList(){
        return roleService.getNameList();
    }
}
