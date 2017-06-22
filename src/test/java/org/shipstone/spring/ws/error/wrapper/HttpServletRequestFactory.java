package org.shipstone.spring.ws.error.wrapper;

import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

/**
 * @author François Robert
 */
public interface HttpServletRequestFactory {

  default HttpServletRequest getHttpServletRequest() {
    HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
    StringBuffer stringBuffer = new StringBuffer("http://test/users/1");
    Mockito.when(httpServletRequest.getRequestURL()).thenReturn(stringBuffer);
    return httpServletRequest;
  }

}
