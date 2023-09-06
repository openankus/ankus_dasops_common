package org.ankus.service;

import lombok.extern.slf4j.Slf4j;
import org.ankus.model.Privilege;
import org.ankus.model.Role;
import org.ankus.model.User;
import org.ankus.repository.PersistentLoginsRepository;
import org.ankus.repository.PrivilegeRepository;
import org.ankus.repository.RoleRepository;
import org.ankus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;

/**
 * 사용자 정보 서비스를 모델링한 클래스
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PersistentLoginsRepository persistentLoginsRepository;


    @Value("${server.name}")
    String server_name;

    @Value("${user.password}")
    String defaultPassword;

    /**
     * 사용자 패스워드 암호화 객체
     */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * 새로운 사용자 등록
     * @param user
     * @return
     */
    public void registerNewUser(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.getRoleList().get(0).setId(roleRepository.getByName(user.getRoleList().get(0).getName()).getId());
        userRepository.save(user);
    }

    /**
     * 기존 사용자 수정
     * @param user
     * @return
     */
    public void registerUser(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.getRoleList().get(0).setId(roleRepository.getByName(user.getRoleList().get(0).getName()).getId());
        userRepository.save(user);
    }

    public String getloginId(String loginId){
        return userRepository.findByLoginId(loginId).size()!=0 ? userRepository.findByLoginId(loginId).get(0).getLoginId() : "";
    }

    /**
     * 사용자 반환 혹은 생성
     * (로그인ID에 해당하는 사용자가 존재하지 않을 경우, 해당 사용자를 생성하여 반환)
     *
     * @param loginId 로그인 ID
     * @param name 이름
     * @param password 패스워드
     * @param enabled 활성화 여부
     * @param roleList 역할 목록
     * @return
     */
    @Transactional
    User getOrCreateUserInfNotFound(String loginId, String name, String password, boolean enabled, List<Role> roleList){

        User user = userRepository.findUserByLoginId(loginId);

        if (user == null){
            user = new User();
            user.setLoginId(loginId);
            user.setName(name);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setRoleList(roleList);
            user.setEnabled(enabled);

            userRepository.save(user);
        }

        return user;
    }


    /**
     * 권한 반환 혹은 생성
     * (권한이 존재하지 않을 경우, 권한을 생성하여 반환)
     *
     * @param name 권한 이름
     * @return 생성된 권한
     */
    @Transactional
    Privilege getOrCreatePrivilegeIfNotFound(String name){

        Privilege privilege = privilegeRepository.findPrivilegeByName(name);
        if (privilege == null){
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }

        return privilege;
    }

    /**
     * 역할을 반환 혹은 생성
     * (역할이 존재하지 않을 경우, 역할을 생성하여 반환)
     *
     * @param name 역할 이름
     * @param privilegeList
     * @return
     */
    @Transactional
    Role getOrCreateRoleIfNotFound(String name, List<Privilege> privilegeList){

        Role role = roleRepository.findRoleByName(name);
        if (role == null){
            role = new Role();
            role.setName(name);
            role.setPrivilegeList(privilegeList);
            roleRepository.save(role);
        }

        return role;
    }


    public boolean security_yn(HttpServletRequest request){
        CookieService cookieService = new CookieService();
        Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(cookieService.cookieSelect("remember-me",request));
        if(decodedBytes.length > 0) {
            String series = new String(decodedBytes).replaceAll("%3D", "=").split(":")[0];
            String token = new String(decodedBytes).replaceAll("%3D", "=").split(":")[1];
            if (persistentLoginsRepository.findBySeriesAndToken(series, token) == null) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    public List<User> userInfo(String loginId){
        return userRepository.findByLoginId(loginId);
    }

    public String userUpdate(User user){
        User newuser = userRepository.findByLoginId(user.getLoginId()).get(0);

        if(user.getPassword().equals("true")){
            newuser.setPassword(bCryptPasswordEncoder.encode(defaultPassword));
        }
        newuser.setEnabled(user.isEnabled());
        List<Role> roleList = new ArrayList<Role>();
        for(int i=0;i<user.getRoleList().size();i++) {
            roleList.add(roleRepository.getByName(user.getRoleList().get(i).getName()));
        }
        newuser.setRoleList(roleList);
        return userRepository.save(newuser).getId().toString();
    }

    public boolean passwordChange(String loginId,String password, String newPassword){
        User user = userRepository.findByLoginId(loginId).get(0);
        if(bCryptPasswordEncoder.matches(password,user.getPassword())){
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }else{
            return false;
        }

    }

    public void userDelete(String loginId) {
        userRepository.deleteByloginId(loginId);
    }
    public String userListDelete(List<Long> ids) {
        ids.forEach(id -> {});
        for(int i=0;i<ids.size();i++){
            userRepository.deleteById(ids.get(i));
        }

        return "완료";
    }

    public String getServer_name(){
        return server_name;
    }
}
