package org.springside.examples.showcase.unit.schedule;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springside.examples.showcase.schedule.ExecutorJob;
import org.springside.examples.showcase.unit.common.BaseTxTestCase;
import org.springside.modules.test.mock.MockAppender;
import org.springside.modules.test.utils.TimeUtils;

@ContextConfiguration(locations = { "/schedule/applicationContext-executor.xml" })
public class ExecutorJobTest extends BaseTxTestCase {
	@Test
	public void test() {
		//加载测试用logger appender
		MockAppender appender = new MockAppender();
		appender.addToLogger(ExecutorJob.class);

		//等待任务启动
		TimeUtils.sleep(3000);

		//验证任务已执行
		assertEquals(1, appender.getAllLogs().size());
		assertEquals("There are 6 user in database.", appender.getFirstLog().getMessage());
	}
}
