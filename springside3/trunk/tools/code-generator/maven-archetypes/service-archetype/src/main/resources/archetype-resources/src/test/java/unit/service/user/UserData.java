#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.unit.service.user;

import org.apache.commons.lang.RandomStringUtils;
import ${package}.entity.user.Role;
import ${package}.entity.user.User;

/**
 * 测试用户数据生成.
 * 
 * @author calvin
 */
public class UserData {

	public static String random() {
		return RandomStringUtils.randomAlphanumeric(5);
	}

	public static User getRandomUser() {
		String userName = "User" + random();
		User user = new User();
		user.setLoginName(userName);
		user.setName(userName);
		user.setPassword("passwd");
		user.setEmail("foo@bar.com");

		return user;
	}

	public static Role getRandomRole() {
		Role role = new Role();
		role.setName("Role" + random());

		return role;
	}
}