package runnershigh.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import runnershigh.project.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Query("select m from Member m where m.name= :name and m.memberPhone = :phoneNumber")
    Optional<Member> findByName(@Param("name") String name, @Param("phoneNumber") String phoneNumber);

    @Query("select m from Member m where m.email= :email and m.name = :name and m.memberPhone =:phoneNumber")
    Optional<Member> findByEmailAndName(@Param("email") String email, @Param("name") String name, @Param("phoneNumber") String phoneNumber);
}
