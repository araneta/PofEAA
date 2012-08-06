<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="helper"
	type="asakichy.architecture.web_presentation.template_view.ArtistHelper"
	scope="request" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>アーティスト</title>
</head>
<body>
	<div>アーティスト名：<%=helper.getName()%></div>
	<div>所属レコード会社：<%=helper.getLabel()%></div>
</body>
</html>