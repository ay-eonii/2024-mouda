package mouda.backend.bet.business;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mouda.backend.bet.domain.Bet;
import mouda.backend.bet.implement.BetFinder;
import mouda.backend.bet.implement.BetWriter;
import mouda.backend.chat.domain.ChatRoomType;
import mouda.backend.chat.implement.ChatRoomWriter;

@Service
@Transactional
@RequiredArgsConstructor
public class BetScheduler {

	private static final ZoneOffset KST_OFFSET = ZoneOffset.ofHours(9);
	private static final int SCHEDULE_LOOKAHEAD_MINUTES = 1;

	private final BetFinder betFinder;
	private final BetWriter betWriter;
	private final TaskScheduler taskScheduler;
	private final ChatRoomWriter chatRoomWriter;

	@Scheduled(cron = "0 * * * * *")
	public void scheduleDraw() {
		List<Bet> scheduledBet = betFinder.findAllScheduledBet(SCHEDULE_LOOKAHEAD_MINUTES);
		scheduledBet
			.forEach(bet -> {
				Instant startTime = bet.getBettingTime().toInstant(KST_OFFSET);
				taskScheduler.schedule(() -> performScheduledTask(bet.getId()), startTime);
			});
	}

	private void performScheduledTask(long betId) {
		Bet bet = betFinder.find(betId);
		if (bet.hasLoser()) {
			return;
		}
		bet.draw();
		betWriter.appendLoser(bet);

		chatRoomWriter.append(bet.getId(), bet.getDarakbangId(), ChatRoomType.BET);
	}
}
