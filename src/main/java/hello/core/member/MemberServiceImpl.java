package hello.core.member;

public class MemberServiceImpl implements MemberService{

    //구현에도 의존함(DIP 위반)
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
