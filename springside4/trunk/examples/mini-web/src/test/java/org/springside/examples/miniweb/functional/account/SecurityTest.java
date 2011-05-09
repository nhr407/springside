package org.springside.examples.miniweb.functional.account;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springside.examples.miniweb.functional.BaseFunctionalTestCase;
import org.springside.examples.miniweb.functional.Gui;
import org.springside.examples.miniweb.functional.Gui.UserColumn;
import org.springside.modules.test.groups.Groups;
import org.springside.modules.test.utils.SeleniumUtils;
import org.springside.modules.utils.ThreadUtils;

/**
 * 系统安全控制的功能测试, 测试主要用户故事.
 * 
 * @author calvin
 */
public class SecurityTest extends BaseFunctionalTestCase {

	/**
	 * 测试匿名用户访问系统时的行为.
	 */
	@Test
	@Groups(NIGHTLY)
	public void checkAnonymous() {
		//访问退出登录页面,退出之前的登录
		driver.get(BASE_URL + "/logout.action");
		assertEquals("Mini-Web 登录页", driver.getTitle());

		//访问任意页面会跳转到登录界面
		ThreadUtils.sleep(2000);
		driver.get(BASE_URL + "/account/user.action");
		assertEquals("Mini-Web 登录页", driver.getTitle());
	}

	/**
	 * 只有用户权限组的操作员访问系统时的受限行为.
	 */
	@Test
	@Groups(NIGHTLY)
	public void checkUserPermission() {
		//访问退出登录页面,退出之前的登录
		driver.get(BASE_URL + "/logout.action");
		assertEquals("Mini-Web 登录页", driver.getTitle());

		//登录普通用户
		driver.findElement(By.name("username")).sendKeys("user");
		driver.findElement(By.name("password")).sendKeys("user");
		driver.findElement(By.xpath(Gui.BUTTON_LOGIN)).click();

		//校验用户权限组的操作单元格为空
		driver.findElement(By.linkText(Gui.MENU_USER)).click();
		ThreadUtils.sleep(2000);
		WebElement table = driver.findElement(By.xpath("//table[@id='contentTable']"));
		assertEquals("查看", SeleniumUtils.getTable(table, 1, UserColumn.OPERATIONS.ordinal()));
	}
}
