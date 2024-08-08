package mouda.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
		MissingServletRequestParameterException exception) {
		return ResponseEntity.badRequest().body(new ErrorResponse(exception.getParameterName() + "은 NULL일 수 없습니다."));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		String error = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

		return ResponseEntity.badRequest().body(new ErrorResponse(error));
	}

	@ExceptionHandler(MoudaException.class)
	public ResponseEntity<ErrorResponse> handleMoudaException(MoudaException exception) {
		StackTraceElement[] stackTrace = exception.getStackTrace();
		String className = stackTrace[0].getClassName();
		String methodName = stackTrace[0].getMethodName();

		String exceptionMessage = exception.getMessage();

		log.info("Exception occurred in class = {}, method = {}, message = {}, exception class = {}",
			className, methodName, exceptionMessage, exception.getClass().getCanonicalName());

		return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception exception) {
		StackTraceElement[] stackTrace = exception.getStackTrace();
		String className = stackTrace[0].getClassName();
		String methodName = stackTrace[0].getMethodName();

		String exceptionMessage = exception.getMessage();

		log.warn("Exception occurred in class = {}, method = {}, message = {}, exception class = {}",
			className, methodName, exceptionMessage, exception.getClass().getCanonicalName());

		return ResponseEntity.internalServerError().body(new ErrorResponse("서버 오류가 발생했습니다."));
	}
}
