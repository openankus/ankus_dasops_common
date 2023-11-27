package org.ankus.model;

import lombok.Getter;
import lombok.Setter;
import org.ankus.model.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ServiceKeyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 서비스키를 소유한 사용자
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    /**
     * 서비스 키 값
     */
    private String serviceKey;

    /**
     * 생성일시
     */
    @CreationTimestamp
    private LocalDateTime createDateTime;

    /**
     * 삭제여부
     */
    private Boolean deleted;

    /**
     * 삭제일시
     */
    @UpdateTimestamp
    private LocalDateTime deleteDateTime;

}
