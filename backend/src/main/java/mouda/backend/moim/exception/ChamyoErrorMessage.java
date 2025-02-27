package mouda.backend.moim.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChamyoErrorMessage {

	NOT_FOUND("참여 정보가 존재하지 않습니다."),
	ALREADY_PARTICIPATED("이미 참여했어요!"),
	MOIM_FULL("모임이 꽉 찼어요!"),
	MOIMING_CANCLED("모임이 취소됐어요!"),
	MOIMING_COMPLETE("모집이 완료됐어요!"),
	CANNOT_CANCEL_CHAMYO("취소할 수 없어요!"),
	NOT_MOIMER("모이머가 아닙니다.");

	private final String message;
}
