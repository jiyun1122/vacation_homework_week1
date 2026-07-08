package mutsa_vacation_week1.demo.member.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import mutsa_vacation_week1.demo.member.entity.Member;

@Getter
@JsonPropertyOrder({"memberId", "loginId", "name", "credit"})
public class MemberInfo {

    private Long memberId;
    private String loginId;
    private String name;
    private int credit;

    public MemberInfo(Long memberId, String loginId, String name, int credit) {
        this.memberId = memberId;
        this.loginId = loginId;
        this.name = name;
        this.credit = credit;
    }

}
