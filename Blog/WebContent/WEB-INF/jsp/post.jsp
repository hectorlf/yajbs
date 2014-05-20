<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><!DOCTYPE html>
<c:set var="pageName" value="${actionBean.post.title}"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="alias" value="${actionBean.context.alias}"/><c:set var="post" value="${actionBean.post}"/><c:set var="metaDescription" value="${actionBean.post.metaDescription}"/>
<html>

<%@include file="/WEB-INF/jsp/fragments/head.jsp"%>

<body>
<%@include file="/WEB-INF/jsp/fragments/header.jsp"%>

	<main class="content" role="main">
<%@include file="/WEB-INF/jsp/fragments/post-detail.jsp"%>
	</main>

<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>

<script type="text/javascript" src="http://media.hectorlopezfernandez.com/syntaxhighlighter/shCore.js" async></script>
<script type="text/javascript" src="http://media.hectorlopezfernandez.com/syntaxhighlighter/shBrushJava.js" async></script>
<script type="text/javascript" async>SyntaxHighlighter.all();</script>
</body>

</html>