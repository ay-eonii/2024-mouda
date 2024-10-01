package mouda.backend.auth.implement;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mouda.backend.auth.Infrastructure.OauthClient;
import mouda.backend.auth.util.TokenDecoder;

@Component
@RequiredArgsConstructor
public class OauthManager {

	private static final String SUB_CLAIM_KEY = "sub";

	private final OauthClient oauthClient;

	public String getSocialLoginId(String code) {
		String idToken = oauthClient.getIdToken(code);
		Map<String, String> payload = TokenDecoder.parseIdToken(idToken);
		return payload.get(SUB_CLAIM_KEY);
	}
}
