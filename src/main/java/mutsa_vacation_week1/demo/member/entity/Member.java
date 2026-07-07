package mutsa_vacation_week1.demo.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int credit;

    public Member(String loginId, String password, String name, int credit) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.credit = credit;
    }

    public void chargeCredit(int amount) {
        this.credit += amount;
    }

    public void deductCredit(int amount) {
        this.credit -= amount;
    }

}
