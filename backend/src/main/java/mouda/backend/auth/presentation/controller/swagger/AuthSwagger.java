package mouda.backend.auth.presentation.controller.swagger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mouda.backend.auth.presentation.request.GoogleLoginRequest;
import mouda.backend.auth.presentation.request.KakaoConvertRequest;
import mouda.backend.auth.presentation.response.LoginResponse;
import mouda.backend.common.config.argumentresolver.LoginMember;
import mouda.backend.common.response.RestResponse;
import mouda.backend.member.domain.Member;

public interface AuthSwagger {

	@Operation(summary = "카카오 로그인", description = "카카오 Oauth Code를 사용하여 로그인한다(accessToken 발급).")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "로그인 성공!"),
	})
	ResponseEntity<Void> convert(
		@LoginMember Member member,
		@RequestBody KakaoConvertRequest kakaoConvertRequest);

	@Operation(summary = "구글 oauth 로그인", description = "구글 Oauth Identity Token 를 사용하여 로그인한다(accessToken 발급).")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "로그인 성공!"),
	})
	ResponseEntity<RestResponse<LoginResponse>> loginGoogle(@RequestBody GoogleLoginRequest googleLoginRequest);

	@Operation(summary = "애플 oauth 로그인", description = "애플 서버로부터 직접 Identity Token과 사용자 정보를 받아 토큰을 발급")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "로그인 성공!"),
	})
	ResponseEntity<Void> loginApple(String id_token, String user);
}
