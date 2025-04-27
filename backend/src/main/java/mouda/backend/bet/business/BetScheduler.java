package mouda.backend.bet.business;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mouda.backend.bet.domain.Bet;
import mouda.backend.bet.domain.BetDetails;
import mouda.backend.bet.implement.BetFinder;
import mouda.backend.bet.implement.BetWriter;
import mouda.backend.chat.domain.ChatRoomType;
import mouda.backend.chat.implement.ChatRoomWriter;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BetScheduler implements Scheduler {

	private static final int SCHEDULE_LOOKAHEAD_MINUTES = 1;

	private final BetFinder betFinder;
	private final BetWriter betWriter;
	private final TaskScheduler taskScheduler;
	private final ChatRoomWriter chatRoomWriter;

	@Override
	@Scheduled(cron = "0 * * * * *")
	public void scheduleDraw() {
		Map<Instant, List<BetDetails>> scheduledBet = betFinder.findAllScheduledBet(SCHEDULE_LOOKAHEAD_MINUTES);

		scheduledBet.
			forEach((bettingTime, betDetails) -> {
				List<Long> betIds = betDetails.stream().map(BetDetails::getId).toList();
				taskScheduler.schedule(() -> performScheduledTask(betIds), bettingTime);
			});
	}

	private void performScheduledTask(List<Long> betIds) {
		List<Bet> bets = betFinder.findAllWithParticipants(betIds);
		bets.forEach(Bet::draw);
		betWriter.appendLoser(bets);

		Map<Long, Long> betInfos = bets.stream()
			.collect(Collectors.toMap(Bet::getId, Bet::getDarakbangId));
		chatRoomWriter.append(betInfos, ChatRoomType.BET);
	}
}
