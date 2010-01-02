package org.springside.examples.miniservice.unit.ws;

import java.util.Collections;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springside.examples.miniservice.data.UserData;
import org.springside.examples.miniservice.entity.user.User;
import org.springside.examples.miniservice.service.user.UserManager;
import org.springside.examples.miniservice.ws.api.dto.UserDTO;
import org.springside.examples.miniservice.ws.api.result.AuthUserResult;
import org.springside.examples.miniservice.ws.api.result.GetAllUserResult;
import org.springside.examples.miniservice.ws.api.result.WSResult;
import org.springside.examples.miniservice.ws.impl.UserWebServiceImpl;
import org.springside.modules.utils.ReflectionUtils;

/**
 * User Web Service的单元测试用例,测试WebService操作的返回码.
 * 
 * 使用EasyMock对UserManager进行模拟.
 * 
 * @author calvin
 */
public class UserWebServiceTest extends Assert {
	private UserWebServiceImpl userWebService;
	private UserManager mockUserManager;

	@Before
	public void setUp() {
		userWebService = new UserWebServiceImpl();
		ReflectionUtils.setFieldValue(userWebService, "dozer", new DozerBeanMapper());
		//创建mock对象
		mockUserManager = EasyMock.createMock(UserManager.class);
		ReflectionUtils.setFieldValue(userWebService, "userManager", mockUserManager);
	}

	@After
	public void tearDown() {
		//确认的脚本都已执行
		EasyMock.verify(mockUserManager);
	}

	/**
	 * 测试dozer正确映射.
	 */
	@Test
	public void dozerBinding() {
		User user = UserData.getRandomUserWithAdminRole();
		List<User> list = Collections.singletonList(user);
		EasyMock.expect(mockUserManager.getAllUser()).andReturn(list);
		EasyMock.replay(mockUserManager);

		GetAllUserResult result = userWebService.getAllUser();
		assertEquals(WSResult.SUCCESS, result.getCode());
		UserDTO dto = result.getUserList().get(0);
		assertEquals(user.getLoginName(), dto.getLoginName());
		assertEquals(user.getRoleList().get(0).getName(), dto.getRoleList().get(0).getName());
	}

	/**
	 * 测试参数错误时的返回码.
	 */
	@Test
	public void validateParamter() {
		EasyMock.replay(mockUserManager);
		WSResult result = userWebService.createUser(null);
		assertEquals(WSResult.PARAMETER_ERROR, result.getCode());
	}

	/**
	 * 测试系统内部抛出异常时的处理.
	 */
	@Test
	public void handleException() {
		EasyMock.expect(mockUserManager.getAllUser()).andThrow(new RuntimeException("Expected exception.."));
		EasyMock.replay(mockUserManager);

		GetAllUserResult result = userWebService.getAllUser();
		assertEquals(WSResult.SYSTEM_ERROR, result.getCode());
		assertEquals(WSResult.SYSTEM_ERROR_MESSAGE, result.getMessage());
	}

	/**
	 * 用户认证测试.
	 * 分别测试正确用户名与正确,错误密码,无密码三种情况的返回码.
	 */
	@Test
	public void authUser() {
		//准备数据,录制脚本
		EasyMock.expect(mockUserManager.authenticate("admin", "admin")).andReturn(true);
		EasyMock.expect(mockUserManager.authenticate("admin", "errorPasswd")).andReturn(false);
		EasyMock.replay(mockUserManager);

		//执行输入正确的测试
		AuthUserResult result = userWebService.authUser("admin", "admin");
		assertEquals(WSResult.SUCCESS, result.getCode());
		assertEquals(true, result.isValid());

		//执行输入错误的测试
		result = userWebService.authUser("admin", "errorPasswd");
		assertEquals(WSResult.SUCCESS, result.getCode());
		assertEquals(false, result.isValid());

		result = userWebService.authUser("admin", "");
		assertEquals(WSResult.PARAMETER_ERROR, result.getCode());
		assertEquals(false, result.isValid());
	}
}