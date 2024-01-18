package WeGoTogether.wegotogether.member.model;

import WeGoTogether.wegotogether.domain.common.BaseEntity;
import WeGoTogether.wegotogether.member.model.Enum.UserRole;
import WeGoTogether.wegotogether.member.model.Enum.UserState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


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
    public String phoneNum;


    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
    public UserState status;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ROLE_USER'")
    public UserRole role;



}
