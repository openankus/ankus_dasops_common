package org.ankus.service;

import lombok.extern.slf4j.Slf4j;
import org.ankus.model.Privilege;
import org.ankus.model.Role;
import org.ankus.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 기본 사용자 정보를 적재하는 클래스
 */
@Slf4j
@Component
public class DefaultUserDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserService userService;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        log.debug("---<기본 관리자 사용자 등록>---");

        //---<애플리케이션 컨텍스트 초기화 및 갱신 시, 기본 사용자, 역할, 권한 등을 등록>---

        // 권한 정보를 얻기 혹은 등록
        Privilege fileMngPrivilege = userService.getOrCreatePrivilegeIfNotFound("파일 관리");
        Privilege workflowMngPrivilege = userService.getOrCreatePrivilegeIfNotFound("워크플로우 관리");
        Privilege dbRetrievalPrivilege = userService.getOrCreatePrivilegeIfNotFound("DB 조회");
        Privilege userMngPrivilege = userService.getOrCreatePrivilegeIfNotFound("사용자 관리");

        // 역할 정보를 얻기 혹은 등록
        List<Privilege> activeUserPrivilegeList =
                Arrays.asList(fileMngPrivilege, workflowMngPrivilege, dbRetrievalPrivilege);
        Role activeUserRole = userService.getOrCreateRoleIfNotFound("사용자", activeUserPrivilegeList);
        List<Privilege> adminPrivilegeList = new ArrayList<Privilege>(activeUserPrivilegeList);
        adminPrivilegeList.add(userMngPrivilege);
        Role adminRole = userService.getOrCreateRoleIfNotFound("관리자", adminPrivilegeList);

        // 기본 관리자 사용자를 얻기 혹은 등록
        String id = "admin";
        String name = "기본 관리자";
        String password = "ankus";
        boolean enabled = true;
        List<Role> roleList = Arrays.asList(adminRole);
        User adminUser = userService.getOrCreateUserInfNotFound(id, name, password, enabled, roleList);


        //---</애플리케이션 컨텍스트 초기화 및 갱신 시, 기본 사용자, 역할, 권한 등을 등록>---

        log.debug("---</기본 관리자 사용자 등록>---");

    }


}
