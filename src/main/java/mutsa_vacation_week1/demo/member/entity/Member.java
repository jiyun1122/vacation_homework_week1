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

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int credit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider = Provider.LOCAL;

    @Column(name = "provider_id")
    private String providerId;

    public Member(String loginId, String password, String name, int credit) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.credit = credit;
        this.provider = Provider.LOCAL;
    }

    public static Member ofKakao(String providerId, String nickname) {
        Member member = new Member();
        member.loginId = "kakao_" + providerId;
        member.password = null;
        member.name = nickname;
        member.credit = 0;
        member.provider = Provider.KAKAO;
        member.providerId = providerId;
        return member;
    }

    public void chargeCredit(int amount) {
        this.credit += amount;
    }

    public void deductCredit(int amount) {
        this.credit -= amount;
    }

}
