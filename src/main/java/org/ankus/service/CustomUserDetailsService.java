package org.ankus.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ankus.model.Privilege;
import org.ankus.model.Role;
import org.ankus.model.User;
import org.ankus.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security 사용자 인증을 위해 사용자 상세정보를 제공하는 서비스
 *
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Spring Security의 사용자 이름(여기서는 loginId)으로 사용자 상세정보를 제공하는 메서드
     * (사용자 인증시, Spring Security에 의해 호출되는 Callback 메서드)
     *
     *
     * @param loginId
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findUserByLoginId(loginId);

        // 해당하는 사용자가 없을 경우, 사용자 이름 없음 Exception 발생
        if (user == null){
            throw new UsernameNotFoundException("Login ID에 해당하는 사용자가 없습니다.");
        }



        // 해당하는 사용자가 있을 경우, Spring Security 사용자 정보를 생성하여 반환
        return new org.springframework.security.core.userdetails.User(
                user.getLoginId(), user.getPassword(), user.isEnabled(), true, true,
                true, this.getAuthorityList(user.getRoleList()));
    }


    /**
     * 역할에 대한 Spring Security의 인증된 권한 목록 반환
     *
     * @param roleList 역할 목록
     * @return
     */
    private List<? extends GrantedAuthority> getAuthorityList(
            List<Role> roleList) {

        //  사용자 역할을 기반으로 Spring의 인증된 권한 이름 생성
        List<String> privilegeNameList = new ArrayList<String>();
        List<Privilege> collection = new ArrayList<Privilege>();
        for (Role role : roleList) {
            //  역할 이름 관련 Spring의 인증된 관한이름 추가
            privilegeNameList.add("ROLE_"+role.getName());
            //  역할의 권한 관련 Spring의 인증된 관한이름 추가
            collection.addAll(role.getPrivilegeList());
        }
        for (Privilege item : collection) {
            privilegeNameList.add(item.getName());
        }

        //  권한이름 목록 기반 Spring의 인증된 권한 생성
        List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
        for (String privilege : privilegeNameList) {
            authorityList.add(new SimpleGrantedAuthority(privilege));
        }

        return authorityList;
    }

}
