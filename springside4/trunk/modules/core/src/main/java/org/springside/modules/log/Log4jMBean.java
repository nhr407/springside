/**
 * Copyright (c) 2005-2011 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id$
 */
package org.springside.modules.log;

import java.util.Enumeration;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springside.modules.utils.AssertUtils;

/**
 * 基于JMX动态配置Log4J日志级别的MBean.
 * 
 * @author calvin
 */
@ManagedResource(objectName = Log4jMBean.LOG4J_MBEAN_NAME, description = "Log4j Management Bean")
public class Log4jMBean {

	/**
	 * Log4jMbean的注册名称.
	 */
	public static final String LOG4J_MBEAN_NAME = "Log4j:name=log4j";

	private static org.slf4j.Logger mbeanLogger = LoggerFactory.getLogger(Log4jMBean.class);

	private String defaultLoggerName;

	private String traceAppenderName;

	/**
	 * 获取Logger的日志级别.
	 */
	@ManagedOperation(description = "Get logging level of the logger")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "loggerName", description = "Logger name") })
	public String getLoggerLevel(String loggerName) {
		Logger logger = Logger.getLogger(loggerName);
		return logger.getEffectiveLevel().toString();
	}

	/**
	 * 设置Logger的日志级别.
	 * 如果日志级别名称错误, 设为DEBUG.
	 */
	@ManagedOperation(description = "Set new logging level to the logger")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "loggerName", description = "Logger name"),
			@ManagedOperationParameter(name = "newlevel", description = "New level") })
	public void setLoggerLevel(String loggerName, String newLevel) {
		Logger logger = Logger.getLogger(loggerName);
		Level level = Level.toLevel(newLevel);
		logger.setLevel(level);
		mbeanLogger.info("设置{}级别为{}", loggerName, newLevel);
	}

	/**
	 * 获得项目默认logger的级别.
	 * 默认logger名称通过#setDefaultLoggerName(String)配置.
	 */
	@ManagedAttribute(description = "Project default logging level of the logger")
	public String getDefaultLoggerLevel() {
		return getLoggerLevel(defaultLoggerName);
	}

	/**
	 * 设置项目默认logger的级别.
	 * 默认logger名称通过#setDefaultLoggerName(String)配置.
	 */
	@ManagedAttribute(description = "Project default logging level of the logger")
	public void setDefaultLoggerLevel(String newLevel) {
		setLoggerLevel(defaultLoggerName, newLevel);
	}

	@ManagedOperation(description = "Start trace")
	public void startTrace() {
		assertTraceInfoInjected();
		Logger logger = Logger.getLogger(defaultLoggerName);
		logger.setLevel(Level.DEBUG);
		setTraceAppenderThreshold(logger, Level.DEBUG);
		mbeanLogger.info("Start trace");
	}

	@ManagedOperation(description = "Stop trace")
	public void stopTrace() {
		assertTraceInfoInjected();
		Logger logger = Logger.getLogger(defaultLoggerName);
		logger.setLevel(Level.INFO);
		setTraceAppenderThreshold(logger, Level.OFF);
		mbeanLogger.info("Stop trace");
	}

	private void setTraceAppenderThreshold(Logger logger, Level level) {
		Enumeration e = logger.getAllAppenders();
		while (e.hasMoreElements()) {
			AppenderSkeleton appender = (AppenderSkeleton) e.nextElement();
			if (appender.getName().equals(traceAppenderName)) {
				appender.setThreshold(level);
			}
		}
	}

	private void assertTraceInfoInjected() {
		AssertUtils.hasText(defaultLoggerName);
		AssertUtils.hasText(traceAppenderName);
	}

	/**
	 * 设置项目默认的logger名称, 如org.springside.examples.miniweb.
	 */
	public void setDefaultLoggerName(String defaultLoggerName) {
		this.defaultLoggerName = defaultLoggerName;
	}

	public void setTraceAppenderName(String traceAppenderName) {
		this.traceAppenderName = traceAppenderName;
	}

}