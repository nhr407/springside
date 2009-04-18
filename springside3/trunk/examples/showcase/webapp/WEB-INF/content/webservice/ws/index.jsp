<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>WebService高级演示</title>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<link href="${ctx}/css/default.css" type="text/css" rel="stylesheet" />
</head>
<body>
<h2>WebService高级演示</h2>
<p>技术说明：<br/>
1.WS-Security：认证机制，建议大家从DIY的WebService安全机制逐渐转到标准之中。<br/>
2.MTOM:附件机制。
</p>
<p>未来版本：<br/>
异步回调机制，客户端发送请求后立即返回，服务端产生结果后回调客户端提供的接口通知结果。<br/>
同样鼓励大家逐步转到标准机制之中。
</p>
<div id="footer">
	<a href="${ctx}/">返回首页</a>
</div>
</body>
</html>