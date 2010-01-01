<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="org.springside.modules.security.springsecurity.SpringSecurityUtils" %>
<%@ include file="/common/taglibs.jsp" %>
<div id="hd">
	<div id="title">
		<h1>Mini-Web示例</h1>
		<h2>--CRUD管理界面演示</h2>
	</div>
	<div id="menu">
		<ul>
			<li><a href="${ctx}/security/user.action">帐号列表</a></li>
			<li><a href="${ctx}/security/role.action">角色列表</a></li>
			<%if("admin".equals(SpringSecurityUtils.getCurrentUserName())){ %>
				<li><a href="${ctx}/security/resource.action">资源权限列表</a></li>
			<%} %>
			<li><a href="${ctx}/j_spring_security_logout">退出登录</a></li>
		</ul>
	</div>
</div>