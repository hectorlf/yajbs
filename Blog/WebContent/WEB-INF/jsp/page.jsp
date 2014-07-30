<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><!DOCTYPE html>
<c:set var="pageName" value="${actionBean.page.title}"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="alias" value="${actionBean.context.alias}"/><c:set var="page" value="${actionBean.page}"/><c:set var="metaDescription" value="${actionBean.page.metaDescription}"/>
<html>

<%@include file="/WEB-INF/jsp/fragments/head.jsp"%>

<body>
<%@include file="/WEB-INF/jsp/fragments/header.jsp"%>

	<main role="main">
<%@include file="/WEB-INF/jsp/fragments/page-detail.jsp"%>
	</main>

<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>

<%@include file="/WEB-INF/jsp/fragments/deferred-styles.jsp"%>
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>
</body>

</html>