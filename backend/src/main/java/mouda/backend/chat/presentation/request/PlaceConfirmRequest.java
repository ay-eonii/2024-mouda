package mouda.backend.chat.presentation.request;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import mouda.backend.chat.entity.ChatEntity;
import mouda.backend.chat.entity.ChatType;
import mouda.backend.darakbangmember.domain.DarakbangMember;

public record PlaceConfirmRequest(
	@NotBlank
	String place
) {
	public ChatEntity toEntity(long chatRoomId, DarakbangMember darakbangMember) {
		return ChatEntity.builder()
			.content(place)
			.chatRoomId(chatRoomId)
			.date(LocalDate.now())
			.time(LocalTime.now())
			.darakbangMember(darakbangMember)
			.chatType(ChatType.PLACE)
			.build();
	}
}
