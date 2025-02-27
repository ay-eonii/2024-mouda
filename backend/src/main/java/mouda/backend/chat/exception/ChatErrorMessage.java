package mouda.backend.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatErrorMessage {

	CHATROOM_NOT_FOUND("존재하지 않는 채팅방입니다."),
	BET_DARAKBANG_MEMBER_NOT_FOUND("참여하지 않은 안내면진다입니다."),
	INVALID_CHATROOM_TYPE("잘못된 채팅 방 타입입니다."),
	UNAUTHORIZED("권한이 없습니다."),
	INVALID_DATE_TIME_FORMAT("날짜와 시간 형식이 올바르지 않습니다."),
	CHATROOM_ALREADY_EXISTS("이미 존재하는 채팅방입니다."), // 새로 추가된 메시지

	;

	private final String message;
}
