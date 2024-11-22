package mouda.backend.bet.business;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
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

	private final BetFinder betFinder;
	private final BetWriter betWriter;
	private final TaskScheduler taskScheduler;
	private final ChatRoomWriter chatRoomWriter;

	@Scheduled(cron = "${bet.schedule}")
	public void performScheduledTask() {
		List<Bet> bets = betFinder.findAllDrawableBet();

	public void scheduleDraw(Bet bet, long betId) {
		Instant startTime = bet.getBettingTime().toInstant(KST_OFFSET);
		taskScheduler.schedule(() -> performScheduledTask(betId), startTime);
	}

		betWriter.saveAll(bets);

	private void performScheduledTask(long betId) {
		Bet bet = betFinder.find(betId);
		bet.draw();
		betWriter.appendLoser(bet);

		chatRoomWriter.append(bet.getId(), bet.getDarakbangId(), ChatRoomType.BET);
	}
}
