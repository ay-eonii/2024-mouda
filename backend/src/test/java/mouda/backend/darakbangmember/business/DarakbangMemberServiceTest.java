package mouda.backend.darakbangmember.business;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import mouda.backend.common.fixture.DarakbangMemberFixture;
import mouda.backend.common.fixture.DarakbangSetUp;
import mouda.backend.common.fixture.MemberFixture;
import mouda.backend.darakbang.infrastructure.DarakbangRepository;
import mouda.backend.darakbangmember.domain.DarakbangMember;
import mouda.backend.darakbangmember.infrastructure.DarakbangMemberRepository;
import mouda.backend.darakbangmember.presentation.response.DarakbangMemberResponses;
import mouda.backend.darakbangmember.presentation.response.DarakbangMemberRoleResponse;
import mouda.backend.member.domain.Member;
import mouda.backend.member.infrastructure.MemberRepository;
import mouda.backend.member.presentation.response.DarakbangMemberInfoResponse;

@SpringBootTest
class DarakbangMemberServiceTest extends DarakbangSetUp {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private DarakbangMemberRepository darakbangMemberRepository;

	@Autowired
	private DarakbangRepository darakbangRepository;

	@Autowired
	private DarakbangMemberService darakbangMemberService;

	@DisplayName("다락방 멤버 권한 조회 테스트")
	@Nested
	class DarakbangMemberRoleReadTest {

		@DisplayName("다락방 멤버가 아니라면 OUTSIDER를 반환한다.")
		@Test
		void success() {
			Member chico = MemberFixture.getChico();
			memberRepository.save(chico);
			DarakbangMember darakbangMember = DarakbangMemberFixture.getDarakbangOutsiderWithWooteco(darakbang, chico);
			darakbangMemberRepository.save(darakbangMember);
			DarakbangMemberRoleResponse response = darakbangMemberService.findDarakbangMemberRole(
				darakbang.getId(), chico);

			assertThat(response.role()).isEqualTo("OUTSIDER");
		}
	}

	@DisplayName("다락방 멤버 조회 테스트")
	@Nested
	class DarakbangMemberReadTest {

		@DisplayName("매니저는 다락방 멤버 목록을 조회할 수 있다.")
		@Test
		void managerCanReadDarakbangMembers() {
			DarakbangMemberResponses responses = darakbangMemberService.findAllDarakbangMembers(
				darakbang.getId(), darakbangManager);

			assertThat(responses.responses()).hasSize(3);
		}

		@DisplayName("다락방 멤버는 다락방 멤버 목록을 조회할 수 있다.")
		@Test
		void memberCanReadDarakbangMembers() {
			DarakbangMemberResponses responses = darakbangMemberService.findAllDarakbangMembers(
				darakbang.getId(), darakbangAnna);

			assertThat(responses.responses()).hasSize(3);
		}
	}

	@DisplayName("내 정보를 조회한다.")
	@Test
	void findMyInfo() {
		DarakbangMemberInfoResponse response = darakbangMemberService.findMyInfo(darakbangHogee);

		assertThat(response.name()).isEqualTo("hogee");
		assertThat(response.nickname()).isEqualTo("소소파파");
		assertThat(response.profile()).isEqualTo("profile");
		assertThat(response.description()).isEqualTo("description");
	}
}
