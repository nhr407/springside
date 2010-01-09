package org.springside.examples.showcase.functional.webservice.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springside.examples.showcase.ws.client.PasswordCallback;
import org.springside.examples.showcase.ws.server.UserWebService;
import org.springside.examples.showcase.ws.server.WsConstants;
import org.springside.examples.showcase.ws.server.result.GetAllUserResult;
import org.springside.examples.showcase.ws.server.result.GetUserResult;
import org.springside.modules.test.spring.SpringContextTestCase;

import com.google.common.collect.Maps;

/**
 * WS-Security 测试,测试PlainText, Digest, SpringSecurity三种EndPoint.
 * 
 * @author calvin
 */
@ContextConfiguration(locations = { "/webservice/applicationContext-cxf-client.xml" }, inheritLocations = false)
public class SecurityWebServiceTest extends SpringContextTestCase {

	/**
	 * 测试明文密码, 在Spring ApplicationContext中用<jaxws:client/>创建的Client.
	 */
	@Test
	public void getAllUserWithPlainPassword() {
		UserWebService userWebService = (UserWebService) applicationContext.getBean("userServiceWithPlainPassword");
		GetAllUserResult result = userWebService.getAllUser();
		assertTrue(result.getUserList().size() > 0);
	}

	/**
	 * 测试Digest密码认证, 使用JAXWS的API自行创建Client.
	 */
	@Test
	public void getAllUserWithDigestPassword() throws MalformedURLException {

		//创建UserWebService
		URL wsdlURL = new URL("http://localhost:8080/showcase/services/UserServiceWithDigestPassword?wsdl");
		QName UserServiceName = new QName(WsConstants.NS, "UserService");
		Service service = Service.create(wsdlURL, UserServiceName);
		UserWebService userWebService = service.getPort(UserWebService.class);

		//定义WSS4JOutInterceptor
		Map<String, Object> outProps = Maps.newHashMap();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		outProps.put(WSHandlerConstants.USER, "admin");
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
		outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, PasswordCallback.class.getName());
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);

		Client client = ClientProxy.getClient(userWebService);
		Endpoint endPoint = client.getEndpoint();
		endPoint.getOutInterceptors().add(wssOut);

		//调用UserWebService
		GetAllUserResult result = userWebService.getAllUser();
		assertTrue(result.getUserList().size() > 0);
	}
	
	/**
	 * 测试访问与SpringSecurity结合的EndPoint, 调用受SpringSecurity保护的方法.
	 */
	@Test
	public void getUserWithSpringSecurity() {
		UserWebService userWebService = (UserWebService) applicationContext.getBean("userServiceWithSpringSecurity");
		GetUserResult result = userWebService.getUser(1L);
		assertEquals("admin",result.getUser().getLoginName());
	}
	
	/**
	 * 测试访问没有与SpringSecurity结合的EndPoint, 调用受SpringSecurity保护的方法.
	 */
	@Test(expected=SOAPFaultException.class)
	public void getUserWithSpringSecurityWithoutPermission() {
		UserWebService userWebService = (UserWebService) applicationContext.getBean("userServiceWithPlainPassword");
		GetUserResult result = userWebService.getUser(1L);
		assertEquals("admin",result.getUser().getLoginName());
	}
}
