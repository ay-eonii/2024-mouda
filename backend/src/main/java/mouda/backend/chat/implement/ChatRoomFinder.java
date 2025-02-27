package mouda.backend.chat.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mouda.backend.bet.infrastructure.BetDarakbangMemberRepository;
import mouda.backend.chat.domain.Chat;
import mouda.backend.chat.domain.ChatRoom;
import mouda.backend.chat.domain.ChatRoomType;
import mouda.backend.chat.domain.Chats;
import mouda.backend.chat.domain.LastChat;
import mouda.backend.chat.entity.ChatEntity;
import mouda.backend.chat.entity.ChatRoomEntity;
import mouda.backend.chat.exception.ChatErrorMessage;
import mouda.backend.chat.exception.ChatException;
import mouda.backend.chat.infrastructure.ChatRepository;
import mouda.backend.chat.infrastructure.ChatRoomRepository;
import mouda.backend.darakbangmember.domain.DarakbangMember;
import mouda.backend.moim.infrastructure.ChamyoRepository;

@Component
@RequiredArgsConstructor
public class ChatRoomFinder {

	private final ChatRepository chatRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChamyoRepository chamyoRepository;
	private final BetDarakbangMemberRepository betDarakbangMemberRepository;

	public ChatRoom read(long darakbangId, long chatRoomId, DarakbangMember darakbangMember) {
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByIdAndDarakbangId(chatRoomId, darakbangId)
			.orElseThrow(() -> new ChatException(HttpStatus.NOT_FOUND, ChatErrorMessage.CHATROOM_NOT_FOUND));
		boolean isParticipated = checkParticipation(chatRoomEntity, darakbangMember);
		if (!isParticipated) {
			throw new ChatException(HttpStatus.FORBIDDEN, ChatErrorMessage.UNAUTHORIZED);
		}
		return new ChatRoom(chatRoomEntity.getId(), chatRoomEntity.getTargetId(), chatRoomEntity.getType());
	}

	private boolean checkParticipation(ChatRoomEntity chatRoomEntity, DarakbangMember darakbangMember) {
		ChatRoomType type = chatRoomEntity.getType();
		if (type == ChatRoomType.MOIM) {
			return chamyoRepository.existsByMoimIdAndDarakbangMemberId(chatRoomEntity.getTargetId(),
				darakbangMember.getId());
		}
		if (type == ChatRoomType.BET) {
			return betDarakbangMemberRepository.existsByBetIdAndDarakbangMemberId(chatRoomEntity.getTargetId(),
				darakbangMember.getId());
		}
		return false;
	}

	public ChatRoom readMoimChatRoom(long darakbangId, long chatRoomId) {
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByIdAndDarakbangId(chatRoomId, darakbangId)
			.orElseThrow(() -> new ChatException(HttpStatus.NOT_FOUND, ChatErrorMessage.CHATROOM_NOT_FOUND));

		ChatRoomType type = chatRoomEntity.getType();
		if (type.isNotMoim()) {
			throw new ChatException(HttpStatus.BAD_REQUEST, ChatErrorMessage.INVALID_CHATROOM_TYPE);
		}
		return new ChatRoom(chatRoomEntity.getId(), chatRoomEntity.getTargetId(), chatRoomEntity.getType());
	}

	public ChatRoom readChatRoomByTargetId(long targetId, ChatRoomType chatRoomType) {
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByTargetIdAndType(targetId, chatRoomType)
			.orElseThrow(() -> new ChatException(HttpStatus.NOT_FOUND, ChatErrorMessage.CHATROOM_NOT_FOUND));

		LastChat lastChat = chatRepository.findFirstByChatRoomIdOrderByIdDesc(chatRoomEntity.getId())
			.map(ChatEntity::toLastChat)
			.orElse(LastChat.empty());

		return new ChatRoom(chatRoomEntity.getId(), chatRoomEntity.getTargetId(), chatRoomEntity.getType(), lastChat);
	}

	@Transactional(readOnly = true)
	public Chats findAllUnloadedChats(long chatRoomId, long recentChatId) {
		List<Chat> chats = chatRepository.findAllUnloadedChats(chatRoomId, recentChatId).stream()
			.map(ChatEntity::toChat)
			.toList();
		return new Chats(chats);
	}

	public Long findChatRoomIdByTargetId(long targetId, ChatRoomType chatRoomType) {
		Optional<ChatRoomEntity> chatRoom = chatRoomRepository.findByTargetIdAndType(targetId, chatRoomType);
		return chatRoom.map(ChatRoomEntity::getId)
			.orElse(null);
	}
}
