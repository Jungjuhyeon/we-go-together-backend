package WeGoTogether.wegotogether.user.entity;


import WeGoTogether.wegotogether.domain.common.BaseEntity;
import WeGoTogether.wegotogether.domain.enums.UserRole;
import WeGoTogether.wegotogether.domain.enums.UserState;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Getter
@Setter
@Builder
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
    public UserState status;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ROLE_USER'")
    public UserRole role;

    private String refreshToken;  // 로그인 시 생성되는 refreshToken을 DB에 저장

    private String verifiedCode;

    private LocalDateTime verificationCodeGeneratedAt; // 인증번호 생성시간

    private LocalDateTime verificationCodeExpiration;   // 인증번호 만료시간

}
