package mouda.backend.auth.business;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mouda.backend.auth.implement.GoogleUserInfoProvider;
import mouda.backend.auth.implement.JoinManager;
import mouda.backend.auth.implement.jwt.AccessTokenProvider;
import mouda.backend.auth.presentation.request.GoogleLoginRequest;
import mouda.backend.auth.presentation.response.LoginResponse;
import mouda.backend.member.domain.Member;
import mouda.backend.member.domain.OauthType;
import mouda.backend.member.implement.MemberFinder;
import mouda.backend.member.implement.MemberWriter;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService {

	private final JoinManager joinManager;
	private final GoogleUserInfoProvider userInfoProvider;
	private final MemberFinder memberFinder;
	private final AccessTokenProvider accessTokenProvider;
	private final MemberWriter memberWriter;

	public LoginResponse login(GoogleLoginRequest request) {
		String name = userInfoProvider.getName(request.idToken());
		String identifier = userInfoProvider.getIdentifier(request.idToken());
		log.warn("{}  =  2 2 2", identifier);
		Member member = memberFinder.getByIdentifier(identifier);
		log.warn("{}  =  3 3 3", member.getIdentifier());

		if (member != null) {
			log.warn("{}  =  4 4 4", member.getIdentifier());

			joinManager.rejoin(member);
			memberWriter.updateName(member.getId(), name);
			return new LoginResponse(accessTokenProvider.provide(member));
		}
		Member joinedMember = joinManager.join(name, OauthType.GOOGLE, identifier);
		return new LoginResponse(accessTokenProvider.provide(joinedMember));
	}
}
