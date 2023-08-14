package org.ankus.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 정보를 모델링한 클래스
 */
@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 사용자 로그인 ID
     */
    private String loginId;

    /**
     * 사용자 이름
     */
    private String name;

    /**
     * 패스워드
     */
    private String password;

    /**
     * 활성화 여부
     */
    private boolean enabled;

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
     * 사용자의 역할 목록
     */
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roleList;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginId='" + loginId + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                ", roleList=" + roleList +
                '}';
    }
}
