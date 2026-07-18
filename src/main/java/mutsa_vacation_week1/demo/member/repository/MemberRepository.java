package mutsa_vacation_week1.demo.member.repository;

import mutsa_vacation_week1.demo.member.entity.Member;
import mutsa_vacation_week1.demo.member.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByProviderAndProviderId(Provider provider, String providerId);

}

