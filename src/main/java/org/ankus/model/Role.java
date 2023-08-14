package org.ankus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 역할 정보를 정의한 클래스
 */
@Entity
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 역할 이름
     */
    private String name;

    /**
     * 생성일시
     */
    @CreationTimestamp
    LocalDateTime createDateTime;

    /**
     * 갱신일시
     */
    @UpdateTimestamp
    LocalDateTime updateDateTime;

    /**
     * 관련 사용자 목록
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "roleList")
    private List<User> userList;

    @ManyToMany
    @JoinTable(
            name = "role_privilege",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private List<Privilege> privilegeList;


    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
//                ", userList=" + userList +
                ", privilegeList=" + privilegeList +
                '}';
    }

}
