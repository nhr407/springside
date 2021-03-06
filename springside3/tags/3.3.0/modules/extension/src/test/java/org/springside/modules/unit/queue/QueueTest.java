package org.springside.modules.unit.queue;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.junit.Assert;
import org.junit.Test;
import org.springside.modules.queue.QueueConsumer;
import org.springside.modules.queue.QueueHolder;

@SuppressWarnings("unchecked")
public class QueueTest extends Assert {

	@Test
	public void backup() throws IOException, ClassNotFoundException {
		String queueName = "testBackup";
		String filePath = System.getProperty("java.io.tmpdir") + File.separator + "queue" + File.separator + queueName;

		BlockingQueue queue = QueueHolder.getQueue(queueName);
		Date date1 = new Date();
		Date date2 = new Date();
		queue.offer(date1);
		queue.offer(date2);

		MockConsumerTask task = new MockConsumerTask();
		task.setQueueName(queueName);
		task.start();
		task.stop();

		//判断存在持久化文件
		File file = new File(filePath);
		assertEquals(true, file.exists());

		ObjectInputStream oos = new ObjectInputStream(new FileInputStream(file));
		List list = new ArrayList();
		while (true) {
			try {
				Object obj = oos.readObject();
				list.add(obj);
			} catch (EOFException e) {
				break;
			}
		}
		oos.close();
		file.delete();
		assertEquals(2, list.size());
		assertEquals(date1, list.get(0));
	}

	@Test
	public void restore() throws IOException, ClassNotFoundException {
		String queueName = "testRestore";
		String filePath = System.getProperty("java.io.tmpdir") + File.separator + "queue" + File.separator + queueName;

		BlockingQueue queue = QueueHolder.getQueue(queueName);
		Date date1 = new Date();
		Date date2 = new Date();
		queue.offer(date1);
		queue.offer(date2);

		MockConsumerTask task = new MockConsumerTask();
		task.setQueueName(queueName);
		task.start();
		task.stop();

		//判断存在持久化文件
		File file = new File(filePath);
		assertEquals(true, file.exists());

		BlockingQueue newQueue = QueueHolder.getQueue(queueName);
		task.start();

		List list = new ArrayList();
		newQueue.drainTo(list);

		assertEquals(2, list.size());
		assertEquals(date1, list.get(0));

		//判断存在持久化文件
		file = new File(filePath);
		assertEquals(false, file.exists());
	}

	static class MockConsumerTask extends QueueConsumer {
		public void run() {
		}
	}
}
