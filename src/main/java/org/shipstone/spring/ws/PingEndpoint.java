package org.shipstone.spring.ws;

import org.shipstone.spring.ws.error.exception.PingPongException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author François Robert
 */
@RestController
@RequestMapping("ping")
public class PingEndpoint {

  @GetMapping
  public String notPong() throws PingPongException {
    throw new PingPongException();
  }
}
