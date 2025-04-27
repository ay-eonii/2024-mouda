package mouda.backend.chat.implement;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mouda.backend.chat.domain.ChatRoomType;
import mouda.backend.chat.entity.ChatRoomEntity;
import mouda.backend.chat.infrastructure.ChatRoomJdbcRepository;
import mouda.backend.chat.infrastructure.ChatRoomRepository;

@Component
@RequiredArgsConstructor
public class ChatRoomWriter {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomValidator chatRoomValidator;
	private final ChatRoomJdbcRepository chatRoomJdbcRepository;

	public long append(long targetId, long darakbangId, ChatRoomType chatRoomType) {
		chatRoomValidator.validateAlreadyExists(targetId, chatRoomType);

		ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
			.targetId(targetId)
			.darakbangId(darakbangId)
			.type(chatRoomType)
			.build();
		return chatRoomRepository.save(chatRoomEntity).getId();
	}

	public void append(Map<Long, Long> betInfos, ChatRoomType chatRoomType) {
		List<ChatRoomEntity> chatRoomEntities = betInfos.entrySet().stream()
			.map(entry -> ChatRoomEntity.builder()
				.targetId(entry.getKey())
				.darakbangId(entry.getValue())
				.type(chatRoomType)
				.build())
			.toList();

		chatRoomJdbcRepository.bulkInsertChatRooms(chatRoomEntities);
	}
}
