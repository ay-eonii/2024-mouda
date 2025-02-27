package mouda.backend.chat.domain;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatPreview implements Comparable<ChatPreview> {

	private final ChatRoom chatRoom;
	private final Target target;
	private final long lastReadChatId;
	private final List<Participant> participants;

	@Builder
	public ChatPreview(ChatRoom chatRoom, Target target, long lastReadChatId, List<Participant> participants) {
		this.chatRoom = chatRoom;
		this.target = target;
		this.lastReadChatId = lastReadChatId;
		this.participants = participants;
	}

	public String getLastContent() {
		return chatRoom.getLastChat().getContent();
	}

	public LocalDateTime getLastChatDateTime() {
		return chatRoom.getLastChatDateTime();
	}

	@Override
	public int compareTo(ChatPreview that) {
		Comparator<ChatPreview> chatRoomComparator = Comparator.comparing(
				ChatPreview::getLastChatDateTime,
				Comparator.nullsLast(Comparator.reverseOrder()))
			.thenComparing(chatPreview -> chatPreview.chatRoom.getTargetId());
		return chatRoomComparator.compare(this, that);
	}
}
