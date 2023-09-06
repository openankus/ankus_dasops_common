package org.ankus.service;

import org.ankus.model.User;
import org.ankus.repository.UserRepository;
import org.ankus.repository.UserRepositorySupport;
import org.ankus.util.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepositorySupport userRepositorySupport;

    @Autowired
    private UserRepository userRepository;

    /**
     * 사용자 패스워드 암호화 객체
     */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${user.password}")
    String defaultPassword;

    /**
     * 유저 목록 가져오기
     * @param dto
     * @param formData
     * @return
     */
    public DataTable userSelectPagealbe(DataTable dto, MultiValueMap<String, String> formData){
        System.out.println(formData);

        int draw = Integer.parseInt(formData.get("draw").get(0));
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));

        String column = null;
        if(Integer.parseInt(formData.get("order[0][column]").get(0))==1){
            column="loginId";
        }else if(Integer.parseInt(formData.get("order[0][column]").get(0))==2){
            column="name";
        }else if(Integer.parseInt(formData.get("order[0][column]").get(0))==3){
            column="roleList";
        }else if(Integer.parseInt(formData.get("order[0][column]").get(0))==4){
            column="enabled";
        }else if(Integer.parseInt(formData.get("order[0][column]").get(0))==5){
            column="updateDateTime";
        }
        String sort = formData.get("order[0][dir]").get(0);


        String state = null;

        if(!formData.get("columns[1][search][value]").get(0).equals("")){
            state="loginId";
        }else if(!formData.get("columns[2][search][value]").get(0).equals("")){
            state="name";
        }else if(!formData.get("columns[4][search][value]").get(0).equals("")){
            state="enabled";
        }else{
            state = "*";
        }
        String text = formData.get("columns[1][search][value]").get(0)+formData.get("columns[2][search][value]").get(0);
        String udate = formData.get("columns[5][search][value]").get(0);
        udate = udate.equals("") ? LocalDate.now().minusYears(1)+" "+ LocalDate.now() : udate;

        String enabled = formData.get("columns[4][search][value]").get(0).equals("") ? "*":formData.get("columns[4][search][value]").get(0);

        Page<User> data = userRepositorySupport.list(start, length, column, sort, state, text,enabled, udate);
        long total = userRepositorySupport.total(state,text,udate);
        dto.setDraw(draw);
        dto.setRecordsFiltered((int) total);
        dto.setRecordsTotal((int) total);
        dto.setData(data);
        dto.setUdate(udate);
        return dto;
    }
    
    public void enabledUpdate(boolean enabled, String loginId){
        User user = userRepository.findByLoginId(loginId).get(0);
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    public void userDelete(List<String> loginId) {
        for(int i=0;i<loginId.size();i++){
            userRepository.deleteByloginId(loginId.get(i));
        }
    }

    public String passwordUpdate(String userId, String password){
        User user = userRepository.findByLoginId(userId).get(0);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return "완료";
    }

    public String passwordUpdate(String userId){
        User user = userRepository.findByLoginId(userId).get(0);
        user.setPassword(bCryptPasswordEncoder.encode(defaultPassword));
        userRepository.save(user);
        return "완료";
    }

    public void userRoleUpdate(String userId, List<String> role) {
    }
}
