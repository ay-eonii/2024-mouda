package mouda.backend.chat.implement;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mouda.backend.bet.domain.BetDetails;
import mouda.backend.bet.implement.BetFinder;
import mouda.backend.bet.infrastructure.BetDarakbangMemberRepository;
import mouda.backend.chat.domain.ChatPreview;
import mouda.backend.chat.domain.ChatRoom;
import mouda.backend.chat.domain.Target;
import mouda.backend.darakbangmember.domain.DarakbangMember;

@Component
@RequiredArgsConstructor
public class BetChatPreviewManager implements ChatPreviewManager {

	private final BetDarakbangMemberRepository betDarakbangMemberRepository;
	private final BetFinder betFinder;
	private final ChatRoomFinder chatRoomFinder;

	@Override
	public List<ChatPreview> create(DarakbangMember darakbangMember) {
		List<BetDetails> myBets = betFinder.readAllMyBets(darakbangMember);

		return myBets.stream()
			.map(this::getChatPreview)
			.toList();
	}

	private ChatPreview getChatPreview(BetDetails bet) {
		long targetId = bet.getId();
		ChatRoom chatRoom = chatRoomFinder.readChatRoomByTargetId(bet.getId());
		long lastReadChatId = betDarakbangMemberRepository.findLastReadChatIdByBetId(targetId);
		int participantSize = betDarakbangMemberRepository.countByBetId(targetId);

		return ChatPreview.builder()
			.chatRoom(chatRoom)
			.target(new Target(bet))
			.lastReadChatId(lastReadChatId)
			.currentPeople(participantSize)
			.build();
	}
}
