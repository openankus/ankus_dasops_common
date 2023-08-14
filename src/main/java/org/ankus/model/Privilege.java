package org.ankus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * 권한정보를 모델링한 클래스
 */
@Entity
@Getter
@Setter
public class Privilege {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 권한 이름
     */
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "privilegeList")
    private List<Role> roleList;


    @Override
    public String toString() {
        return "Privilege{" +
                "id=" + id +
                ", name='" + name + '\'' +
//                ", roleList=" + roleList +
                '}';
    }
}
