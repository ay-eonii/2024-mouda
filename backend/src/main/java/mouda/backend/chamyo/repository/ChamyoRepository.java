package mouda.backend.chamyo.repository;

import java.util.List;
import java.util.Optional;
import mouda.backend.chamyo.domain.Chamyo;
import mouda.backend.member.domain.Member;
import mouda.backend.moim.domain.Moim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamyoRepository extends JpaRepository<Chamyo, Long> {

    Optional<Chamyo> findByMoimIdAndMemberId(Long moimId, Long id);

    List<Chamyo> findAllByMoimId(Long moimId);

    int countByMoim(Moim moim);

    boolean existsByMoimAndMember(Moim moim, Member member);

    void deleteAllByMoimId(Long moimId);

    List<Chamyo> findAllByMemberId(Long memberId);
}
