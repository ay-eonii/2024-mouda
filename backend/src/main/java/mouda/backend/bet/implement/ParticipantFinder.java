package mouda.backend.bet.implement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mouda.backend.bet.domain.Participant;
import mouda.backend.bet.entity.BetDarakbangMemberEntity;
import mouda.backend.bet.entity.BetEntity;
import mouda.backend.bet.infrastructure.BetDarakbangMemberRepository;
import mouda.backend.darakbangmember.domain.DarakbangMember;

@Component
@RequiredArgsConstructor
public class ParticipantFinder {

	private final BetDarakbangMemberRepository betDarakbangMemberRepository;

	public List<Participant> findAllByBetEntity(BetEntity betEntity) {
		List<DarakbangMember> darakbangMembers = betDarakbangMemberRepository.findAllDarakbangMemberByBetId(
			betEntity.getId());
		return darakbangMembers.stream()
			.map(darakbangMember -> new Participant(darakbangMember.getId(), darakbangMember.getNickname(),
				darakbangMember.getProfile()))
			.toList();
	}

	public Map<BetEntity, List<Participant>> findAllByBetEntity(List<Long> betIds) {
		List<BetDarakbangMemberEntity> all = betDarakbangMemberRepository.findAllWithBet(betIds);
		Map<BetEntity, List<BetDarakbangMemberEntity>> participants = all.stream()
			.collect(Collectors.groupingBy(BetDarakbangMemberEntity::getBet));

		return participants.entrySet()
			.stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> toParticipants(entry.getValue())
			));
	}

	private List<Participant> toParticipants(List<BetDarakbangMemberEntity> entities) {
		return entities.stream()
			.map(BetDarakbangMemberEntity::getDarakbangMember)
			.map(darakbangMember -> new Participant(
				darakbangMember.getId(),
				darakbangMember.getNickname(),
				darakbangMember.getProfile()))
			.toList();
	}
}
