<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><!DOCTYPE html>
<c:choose><c:when test="${!empty actionBean.month}"><c:set var="date">en <joda:format value="${actionBean.searchDate}" pattern="MMMM 'de' yyyy" locale="es_ES"/></c:set></c:when><c:otherwise><c:set var="date">en <joda:format value="${actionBean.searchDate}" pattern="yyyy"/></c:set></c:otherwise></c:choose>
<c:set var="pageName" value="Entradas archivadas ${date}"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="posts" value="${actionBean.posts}"/>
<html>

<%@include file="/WEB-INF/jsp/fragments/head.jsp"%>

<body>
<%@include file="/WEB-INF/jsp/fragments/header.jsp"%>

	<main role="main"><c:choose><c:when test="${fn:length(posts) eq 0}">
		<article class="preview">
			<header>
				<div class="post-meta">No se ha encontrado ning&uacute;n resultado</div>
			</header>
		</article></c:when><c:otherwise>
<c:forEach items="${posts}" var="post" varStatus="status">
<%@include file="/WEB-INF/jsp/fragments/post-excerpt.jsp"%>
</c:forEach></c:otherwise></c:choose><c:if test="${actionBean.paginationInfo.enabled}"><c:if test="${actionBean.month ne null}">
<fmt:formatNumber minIntegerDigits="2" type="number" value="${actionBean.month}" var="month"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="previousUrl"><stripes:param name="year" value="${actionBean.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="page" value="${actionBean.paginationInfo.previousPage}"/></stripes:url>
<fmt:formatNumber minIntegerDigits="2" type="number" value="${actionBean.month}" var="month"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="nextUrl"><stripes:param name="year" value="${actionBean.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="page" value="${actionBean.paginationInfo.nextPage}"/></stripes:url>
</c:if><c:if test="${actionBean.month eq null}">
<stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="previousUrl"><stripes:param name="year" value="${actionBean.year}"/><stripes:param name="page" value="${actionBean.paginationInfo.previousPage}"/></stripes:url>
<stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="nextUrl"><stripes:param name="year" value="${actionBean.year}"/><stripes:param name="page" value="${actionBean.paginationInfo.nextPage}"/></stripes:url>
</c:if>
		<nav class="pagination" role="pagination"><c:if test="${!actionBean.paginationInfo.onFirstPage}">
			<a href="${previousUrl}" class="newer-posts"><i class="fa fa-chevron-circle-left"></i> P&aacute;gina anterior</a></c:if>
			<span class="page-number">P&aacute;gina ${actionBean.paginationInfo.currentPage} de ${actionBean.paginationInfo.lastPage}</span><c:if test="${!actionBean.paginationInfo.onLastPage}">
			<a href="${nextUrl}" class="older-posts">Siguiente p&aacute;gina <i class="fa fa-chevron-circle-right"></i></a></c:if>
		</nav></c:if>
	</main>

<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>

<%@include file="/WEB-INF/jsp/fragments/deferred-styles.jsp"%>
<%@include file="/WEB-INF/jsp/fragments/syntaxhighlighter-scripts.jsp"%>
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>
</body>

</html>