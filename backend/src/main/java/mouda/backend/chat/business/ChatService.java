package mouda.backend.chat.business;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mouda.backend.chat.domain.ChatRoom;
import mouda.backend.chat.domain.ChatWithAuthor;
import mouda.backend.chat.domain.Chats;
import mouda.backend.chat.implement.ChatRoomFinder;
import mouda.backend.chat.implement.ChatWriter;
import mouda.backend.chat.presentation.request.ChatCreateRequest;
import mouda.backend.chat.presentation.request.DateTimeConfirmRequest;
import mouda.backend.chat.presentation.request.LastReadChatRequest;
import mouda.backend.chat.presentation.request.PlaceConfirmRequest;
import mouda.backend.chat.presentation.response.ChatFindUnloadedResponse;
import mouda.backend.darakbangmember.domain.DarakbangMember;
import mouda.backend.moim.domain.Moim;
import mouda.backend.moim.implement.finder.MoimFinder;
import mouda.backend.moim.implement.writer.MoimWriter;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

	private final ChatRoomFinder chatRoomFinder;
	private final ChatWriter chatWriter;
	private final MoimWriter moimWriter;
	private final MoimFinder moimFinder;

	public void createChat(
		long darakbangId,
		long chatRoomId,
		ChatCreateRequest request,
		DarakbangMember darakbangMember
	) {
		ChatRoom chatRoom = chatRoomFinder.read(darakbangId, chatRoomId, darakbangMember);

		chatWriter.append(chatRoom.getId(), request.content(), darakbangMember);
		// 알림을 발생한다.
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

		Moim moim = moimFinder.read(chatRoom.getTargetId(), darakbangId);
		moimWriter.confirmPlace(moim, darakbangMember, request.place());

		chatWriter.appendPlaceTypeChat(chatRoom.getId(), request.place(), darakbangMember);
		// notificationService.notifyToMembers(NotificationType.MOIM_PLACE_CONFIRMED, darakbangId, moim, darakbangMember);
	}

	public void confirmDateTime(long darakbangId, long chatRoomId, DateTimeConfirmRequest request,
		DarakbangMember darakbangMember) {
		ChatRoom chatRoom = chatRoomFinder.readMoimChatRoom(darakbangId, chatRoomId);

		Moim moim = moimFinder.read(chatRoom.getTargetId(), darakbangId);
		moimWriter.confirmDateTime(moim, darakbangMember, request.date(), request.time());

		chatWriter.appendDateTimeTypeChat(chatRoom.getId(), request.date(), request.time(), darakbangMember);

		// notificationService.notifyToMembers(NotificationType.MOIM_TIME_CONFIRMED, darakbangId, moim, darakbangMember);
	}

	public void updateLastReadChat(
		long darakbangId, long chatRoomId, LastReadChatRequest request, DarakbangMember darakbangMember
	) {
		ChatRoom chatRoom = chatRoomFinder.read(darakbangId, chatRoomId, darakbangMember);

		chatWriter.updateLastReadChat(chatRoom, darakbangMember, request.lastReadChatId());
	}
}
