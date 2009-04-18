<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>RESTful Web Service演示</title>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<link href="${ctx}/css/default.css" type="text/css" rel="stylesheet" />
</head>
<body>
<h2>RESTful Web Service演示</h2>
<p>技术说明：
1.服务端使用Jersey <br/>
2.客户端使用HttpClient 4.0<br/>
主要目的是简化Web Service的提供与使用，因此并不严格遵循GET/POST/PUT等REST概念，而注重演示REST中的ETag缓存与安全等特性。
</p>

<div id="footer">
	<a href="${ctx}/">返回首页</a>
</div>
</body>
</html>