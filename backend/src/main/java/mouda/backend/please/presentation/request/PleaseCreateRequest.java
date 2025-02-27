package mouda.backend.please.presentation.request;

import jakarta.validation.constraints.NotNull;
import mouda.backend.please.domain.Please;

public record PleaseCreateRequest(

	@NotNull
	String title,

	@NotNull
	String description
) {

	public Please toEntity(long darakbangMemberId, long darakbangId) {
		return Please.builder()
			.title(title)
			.description(description)
			.authorId(darakbangMemberId)
			.darakbangId(darakbangId)
			.build();
	}
}
