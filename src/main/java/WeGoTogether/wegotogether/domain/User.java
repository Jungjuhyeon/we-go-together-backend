package WeGoTogether.wegotogether.domain;

import WeGoTogether.wegotogether.domain.Enum.UserRole;
import WeGoTogether.wegotogether.domain.Enum.UserState;
import WeGoTogether.wegotogether.domain.common.BaseEntity;

import WeGoTogether.wegotogether.validation.annotation.ExistPhone;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Setter
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


    @Column(nullable = false,length = 20)
    public String email;

    @Column(nullable = false, name = "pw")
    public String pw;


    @Column(nullable = false)
    private String phoneNum;

    public String refreshToken;

    public String acessToken;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
    public UserState status;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ROLE_USER'")
    public UserRole role;



}
