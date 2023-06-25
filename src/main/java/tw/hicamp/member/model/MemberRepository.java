package tw.hicamp.member.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	
	Member findByMemberEmail(String email);

}
