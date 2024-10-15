package mouda.backend.chat.business;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mouda.backend.chat.domain.ChatRoom;
import mouda.backend.chat.domain.ChatRoomType;
import mouda.backend.chat.domain.ChatWithAuthor;
import mouda.backend.chat.domain.Chats;
import mouda.backend.chat.implement.ChatRoomFinder;
import mouda.backend.chat.implement.ChatWriter;
import mouda.backend.chat.implement.sender.ChatNotificationSender;
import mouda.backend.chat.presentation.request.ChatCreateRequest;
import mouda.backend.chat.presentation.request.DateTimeConfirmRequest;
import mouda.backend.chat.presentation.request.LastReadChatRequest;
import mouda.backend.chat.presentation.request.PlaceConfirmRequest;
import mouda.backend.chat.presentation.response.ChatFindUnloadedResponse;
import mouda.backend.chat.util.DateTimeFormatter;
import mouda.backend.darakbangmember.domain.DarakbangMember;
import mouda.backend.moim.domain.Moim;
import mouda.backend.moim.implement.finder.MoimFinder;
import mouda.backend.moim.implement.writer.MoimWriter;
import mouda.backend.notification.domain.NotificationType;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

	private final ChatRoomFinder chatRoomFinder;
	private final ChatWriter chatWriter;
	private final MoimWriter moimWriter;
	private final MoimFinder moimFinder;
	private final ChatNotificationSender chatNotificationSender;

	public void createChat(
		long darakbangId,
		long chatRoomId,
		ChatCreateRequest request,
		DarakbangMember darakbangMember
	) {
		ChatRoom chatRoom = chatRoomFinder.read(darakbangId, chatRoomId, darakbangMember);

		// todo: 베팅에 대한 알림 처리는 베팅 관련 채팅 기능이 구현된 후 처리
		if (chatRoom.getType() == ChatRoomType.BET) {
			return;
		}

		String content = request.content();
		chatWriter.append(chatRoom.getId(), content, darakbangMember);
		Moim moim = moimFinder.read(chatRoom.getTargetId(), darakbangId);

		chatNotificationSender.sendChatNotification(moim, content, chatRoomId, darakbangMember,
			NotificationType.NEW_CHAT);
	}

	@Transactional(readOnly = true)
	public ChatFindUnloadedResponse findUnloadedChats(
		long darakbangId, long recentChatId, long chatRoomId, DarakbangMember darakbangMember
	) {
		ChatRoom chatRoom = chatRoomFinder.read(darakbangId, chatRoomId, darakbangMember);

		Chats chats = chatRoomFinder.findAllUnloadedChats(chatRoom.getId(), recentChatId);
		List<ChatWithAuthor> chatsWithAuthor = chats.getChatsWithAuthor(darakbangMember);

		return ChatFindUnloadedResponse.toResponse(chatsWithAuthor);
	}

	public void confirmPlace(long darakbangId, long chatRoomId, PlaceConfirmRequest request,
		DarakbangMember darakbangMember) {
		ChatRoom chatRoom = chatRoomFinder.readMoimChatRoom(darakbangId, chatRoomId);

		// todo: 베팅에 대한 알림 처리는 베팅 관련 채팅 기능이 구현된 후 처리
		if (chatRoom.getType() == ChatRoomType.BET) {
			return;
		}

		Moim moim = moimFinder.read(chatRoom.getTargetId(), darakbangId);
		moimWriter.confirmPlace(moim, darakbangMember, request.place());

		chatWriter.appendPlaceTypeChat(chatRoom.getId(), request.place(), darakbangMember);

		chatNotificationSender.sendChatNotification(moim, request.place(), chatRoomId, darakbangMember,
			NotificationType.MOIM_PLACE_CONFIRMED);
	}

	public void confirmDateTime(long darakbangId, long chatRoomId, DateTimeConfirmRequest request,
		DarakbangMember darakbangMember) {
		ChatRoom chatRoom = chatRoomFinder.readMoimChatRoom(darakbangId, chatRoomId);

		// todo: 베팅에 대한 알림 처리는 베팅 관련 채팅 기능이 구현된 후 처리
		if (chatRoom.getType() == ChatRoomType.BET) {
			return;
		}

		Moim moim = moimFinder.read(chatRoom.getTargetId(), darakbangId);
		LocalDate date = request.date();
		LocalTime time = request.time();
		moimWriter.confirmDateTime(moim, darakbangMember, date, time);

		chatWriter.appendDateTimeTypeChat(chatRoom.getId(), date, time, darakbangMember);

		chatNotificationSender.sendChatNotification(moim, DateTimeFormatter.formatDateTime(date, time), chatRoomId,
			darakbangMember, NotificationType.MOIM_TIME_CONFIRMED);
	}

	public void updateLastReadChat(
		long darakbangId, long chatRoomId, LastReadChatRequest request, DarakbangMember darakbangMember
	) {
		ChatRoom chatRoom = chatRoomFinder.read(darakbangId, chatRoomId, darakbangMember);

		chatWriter.updateLastReadChat(chatRoom, darakbangMember, request.lastReadChatId());
	}
}
