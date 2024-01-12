package WeGoTogether.wegotogether.domain;

import WeGoTogether.wegotogether.domain.Enum.UserState;
import WeGoTogether.wegotogether.domain.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

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

    @Column(nullable = false)
    public String pw;

    @Column(nullable = false)
    public String phoneNum;

    public String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
    public UserState status;



}
