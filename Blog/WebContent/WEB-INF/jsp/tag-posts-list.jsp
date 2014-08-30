<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><!DOCTYPE html>
<c:set var="pageName" value="Entradas etiquetadas en '${actionBean.tagName}'"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="posts" value="${actionBean.posts}"/>
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
</c:forEach></c:otherwise></c:choose><c:if test="${actionBean.paginationInfo.enabled}">
		<nav class="pagination" role="pagination"><c:if test="${!actionBean.paginationInfo.onFirstPage}">
			<stripes:link beanclass="com.hectorlopezfernandez.action.TagsAction" class="newer-posts"><stripes:param name="name" value="${actionBean.name}"/><stripes:param name="page" value="${actionBean.paginationInfo.previousPage}"/><i class="fa fa-chevron-circle-left"></i> P&aacute;gina anterior</stripes:link></c:if>
			<span class="page-number">P&aacute;gina ${actionBean.paginationInfo.currentPage} de ${actionBean.paginationInfo.lastPage}</span><c:if test="${!actionBean.paginationInfo.onLastPage}">
			<stripes:link beanclass="com.hectorlopezfernandez.action.TagsAction" class="older-posts"><stripes:param name="name" value="${actionBean.name}"/><stripes:param name="page" value="${actionBean.paginationInfo.nextPage}"/>Siguiente p&aacute;gina <i class="fa fa-chevron-circle-right"></i></stripes:link></c:if>
		</nav></c:if>
	</main>

<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>

<%@include file="/WEB-INF/jsp/fragments/deferred-styles.jsp"%>
<%@include file="/WEB-INF/jsp/fragments/syntaxhighlighter-scripts.jsp"%>
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>
</body>

</html>