package mouda.backend.darakbang.implement;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class InvitationCodeGeneratorTest {

	@SpyBean
	private InvitationCodeGenerator invitationCodeGenerator;

	@DisplayName("7자리 랜덤코드를 생성한다.")
	@Test
	void success() {
		String invitationCode = invitationCodeGenerator.generate();

		assertThat(invitationCode).hasSize(7);
	}
}
