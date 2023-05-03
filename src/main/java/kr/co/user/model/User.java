package kr.co.user.model;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long userId;

    //소셜 로그인시 유저 정보키
    @Column(nullable = false)
    private String userUid;

    private String userName;

    @Column(length = 20)
    private String userNickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String teamId;

    private int statusEnergy;

    private int statusRelation;

    private int statusStress;

    private String statusKeyword;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createTime;

    private Timestamp lastLoginTime;

    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }
}
