package mouda.backend.chat.implement;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import mouda.backend.bet.entity.BetDarakbangMemberEntity;
import mouda.backend.bet.entity.BetEntity;
import mouda.backend.bet.infrastructure.BetDarakbangMemberRepository;
import mouda.backend.bet.infrastructure.BetRepository;
import mouda.backend.chat.domain.ChatPreview;
import mouda.backend.chat.entity.ChatRoomEntity;
import mouda.backend.chat.infrastructure.ChatRoomRepository;
import mouda.backend.common.fixture.BetEntityFixture;
import mouda.backend.common.fixture.ChatRoomEntityFixture;
import mouda.backend.common.fixture.DarakbangSetUp;

class BetChatPreviewManagerTest extends DarakbangSetUp {

	@Autowired
	BetChatPreviewManager betChatPreviewManager;

	@Autowired
	BetRepository betRepository;

	@Autowired
	BetDarakbangMemberRepository betDarakbangMemberRepository;

	@Autowired
	ChatRoomRepository chatRoomRepository;

	@DisplayName("참여한 안내면 진다가 없는 경우 빈 리스트를 반환한다.")
	@Test
	void createWithoutBet() {
		// when
		List<ChatPreview> chatPreviews = betChatPreviewManager.create(darakbangAnna);

		// then
		assertThat(chatPreviews).isEmpty();
	}

	@DisplayName("채팅방 목록에서 추첨 후 채팅방이 열린 안내면진다만 조회한다.")
	@Test
	void create() {
		// given
		BetEntity betEntity1 = BetEntityFixture.getDrawedBetEntity(darakbangAnna.getId(), 1L);
		BetEntity betEntity2 = BetEntityFixture.getBetEntity(darakbangAnna.getId(), 1L);
		BetEntity savedBet1 = betRepository.save(betEntity1);
		BetEntity savedBet2 = betRepository.save(betEntity2);
		betDarakbangMemberRepository.save(new BetDarakbangMemberEntity(darakbangHogee, savedBet1));
		betDarakbangMemberRepository.save(new BetDarakbangMemberEntity(darakbangHogee, savedBet2));

		ChatRoomEntity chatRoom1 = ChatRoomEntityFixture.getChatRoomEntityOfBet(savedBet1.getId(), darakbang.getId());
		chatRoomRepository.save(chatRoom1);

		// when
		List<ChatPreview> chatPreviews = betChatPreviewManager.create(darakbangHogee);

		// then
		assertThat(chatPreviews)
			.isNotEmpty()
			.hasSize(1);
	}
}
