package mouda.backend.notification.implement.filter;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import mouda.backend.common.fixture.DarakbangSetUp;
import mouda.backend.notification.domain.NotificationPayload;
import mouda.backend.notification.domain.NotificationSendEvent;
import mouda.backend.notification.domain.NotificationType;
import mouda.backend.notification.domain.Recipient;
import mouda.backend.notification.implement.subscription.SubscriptionWriter;

@SpringBootTest
class ChatRoomSubscriptionFilterTest extends DarakbangSetUp {

	@Autowired
	private ChatRoomSubscriptionFilter chatRoomSubscriptionFilter;

	@Autowired
	private SubscriptionWriter subscriptionWriter;

	@DisplayName("채팅 알림을 허용하지 않아도 확정 채팅인 경우에는 알림을 받는다.")
	@Test
	void filter_WhenTypeIsConfirmed() {
		// given
		subscriptionWriter.changeChatRoomSubscription(hogee, darakbang.getId(), 1L);

		// when
		NotificationPayload payload = NotificationPayload.createChatPayload(
			NotificationType.MOIM_PLACE_CONFIRMED,
			"모임 제목",
			"메시지",
			"url",
			List.of(Recipient.builder()
				.memberId(hogee.getId())
				.darakbangMemberId(darakbangHogee.getId())
				.build()
			),
			darakbang.getId(),
			1L
		);
		NotificationSendEvent notificationSendEvent = NotificationSendEvent.from(payload);

		// then
		List<Recipient> filteredRecipient = chatRoomSubscriptionFilter.filter(notificationSendEvent);
		assertThat(filteredRecipient).hasSize(1);
		assertThat(filteredRecipient).extracting(Recipient::getMemberId).containsExactly(darakbangHogee.getMemberId());
	}

	@DisplayName("채팅 알림을 허용하지 않는 경우에는 알림을 받지 않는다.")
	@Test
	void filter_WhenUnsubscribed() {
		// given
		subscriptionWriter.changeChatRoomSubscription(hogee, darakbang.getId(), 1L);

		// when
		NotificationPayload payload = NotificationPayload.createChatPayload(
			NotificationType.NEW_CHAT,
			"모임 제목",
			"메시지",
			"url",
			List.of(Recipient.builder()
				.memberId(hogee.getId())
				.darakbangMemberId(darakbangHogee.getId())
				.build()
			),
			darakbang.getId(),
			1L
		);
		NotificationSendEvent notificationSendEvent = NotificationSendEvent.from(payload);

		// then
		List<Recipient> filteredRecipient = chatRoomSubscriptionFilter.filter(notificationSendEvent);
		assertThat(filteredRecipient).isEmpty();
	}

	@DisplayName("채팅 알림을 허용하는 경우에는 알림을 받는다.")
	@Test
	void filter_WhenSubscribed() {
		// when
		NotificationPayload payload = NotificationPayload.createChatPayload(
			NotificationType.NEW_CHAT,
			"모임 제목",
			"메시지",
			"url",
			List.of(Recipient.builder()
				.memberId(hogee.getId())
				.darakbangMemberId(darakbangHogee.getId())
				.build()
			),
			darakbang.getId(),
			1L
		);
		NotificationSendEvent notificationSendEvent = NotificationSendEvent.from(payload);

		// then
		List<Recipient> filteredRecipient = chatRoomSubscriptionFilter.filter(notificationSendEvent);
		assertThat(filteredRecipient).hasSize(1);
		assertThat(filteredRecipient).extracting(Recipient::getMemberId).containsExactly(darakbangHogee.getMemberId());
	}
}
