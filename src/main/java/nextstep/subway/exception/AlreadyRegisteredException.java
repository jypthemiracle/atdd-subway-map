package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyRegisteredException extends RuntimeException {

	public AlreadyRegisteredException(String message) {
		super(message);
	}
}
