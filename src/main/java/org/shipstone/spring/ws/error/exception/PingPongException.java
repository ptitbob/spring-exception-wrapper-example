package org.shipstone.spring.ws.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author François Robert
 */
@ResponseStatus(value= HttpStatus.I_AM_A_TEAPOT, reason="Je ne suis pas une table de ping pong !!")
public class PingPongException extends Exception {
}
