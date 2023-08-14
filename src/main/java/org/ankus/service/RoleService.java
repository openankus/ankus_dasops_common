package org.ankus.service;

import org.ankus.model.Role;
import org.ankus.repository.RoleRepository;
import org.ankus.repository.RoleRepositorySupport;
import org.ankus.util.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleRepositorySupport roleRepositorySupport;


    /**
     * 유저 목록 가져오기
     * @param dto
     * @param formData
     * @return
     */
    public DataTable roleSelectPagealbe(DataTable dto, MultiValueMap<String, String> formData){
        int draw = Integer.parseInt(formData.get("draw").get(0));
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));

        String column = null;
        if(Integer.parseInt(formData.get("order[0][column]").get(0))==1){
            column="name";
        }else if(Integer.parseInt(formData.get("order[0][column]").get(0))==2){
            column="userId";
        }else if(Integer.parseInt(formData.get("order[0][column]").get(0))==5){
            column="updateDateTime";
        }
        String sort = formData.get("order[0][dir]").get(0);


        String state = null;

        if(Integer.parseInt(formData.get("columns[5][search][value]").get(0))==1){
            column="name";
        }else if(Integer.parseInt(formData.get("columns[5][search][value]").get(0))==2){
            column="userId";
        }

        String text = formData.get("columns[1][search][value]").get(0)+formData.get("columns[5][search][value]").get(0);
        String udate = formData.get("columns[6][search][value]").get(0);
        udate = udate.equals("") ? LocalDate.now().minusYears(1)+" "+ LocalDate.now() : udate;
//        Page<Workflow> data = roleRepositorySupport.list(start, length, column, sort, state, text, udate);
//        long total = roleRepositorySupport.total(state,text,udate);

        dto.setDraw(draw);
//        dto.setRecordsFiltered((int) total);
//        dto.setRecordsTotal((int) total);
//        dto.setData(data);
        dto.setUdate(udate);
        return dto;
    }

    public Role getRole(String name){
        return roleRepository.getByName(name);
    }

    public Long save(Role role) {
        return roleRepository.save(role).getId();
    }

    public List<Role> getNameList(){
        return roleRepository.findAll();
    }
}
