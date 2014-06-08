<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><!DOCTYPE html>
<c:set var="pageName" value="Entradas etiquetadas en '${actionBean.tagName}'"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="posts" value="${actionBean.posts}"/>
<html>

<%@include file="/WEB-INF/jsp/fragments/head.jsp"%>

<body>
<%@include file="/WEB-INF/jsp/fragments/header.jsp"%>

	<main class="content" role="main">
		<c:choose>
		<c:when test="${fn:length(posts) eq 0}">
		<article class="preview">
			<header>
	            <div class="post-meta">No se ha encontrado ning&uacute;n resultado</div>
			</header>
		</article>
		</c:when>
		<c:otherwise>
<c:forEach items="${posts}" var="post" varStatus="status">
<%@include file="/WEB-INF/jsp/fragments/post-excerpt.jsp"%>
</c:forEach>
		</c:otherwise>
		</c:choose>
	</main>

<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>

<script type="text/javascript" src="http://cdn.hectorlopezfernandez.com/sh/shCore.js"></script>
<script type="text/javascript" src="http://cdn.hectorlopezfernandez.com/sh/shBrushJava.js"></script>
<script type="text/javascript" async defer>SyntaxHighlighter.all();</script>
</body>

</html>