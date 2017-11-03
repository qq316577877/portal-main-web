<%--
  Created by IntelliJ IDEA.
  User: terry
  Date: 2017/6/7
  Time: 20:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<jsp:include page="forumdata/cache/cache_index.jsp"/>--%>
<jsp:include page="forumdata/cache/usergroup_${jsprun_groupid}.jsp"/>
<c:if test="${jsprun_uid==0||settings.frameon>0}"><c:set var="referer" value="${pageContext.request.requestURI}?${pageContext.request.queryString}" scope="request"/></c:if>
<jsp:forward page="/home" />
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
