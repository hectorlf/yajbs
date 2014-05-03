<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><!DOCTYPE html>
<c:set var="pageName" value="Etiquetas"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="tags" value="${actionBean.tags}"/>
<html>

<%@include file="/WEB-INF/jsp/fragments/head.jsp"%>

<body>
<%@include file="/WEB-INF/jsp/fragments/header.jsp"%>

	<main class="content" role="main">
		<article>
	        <header><h1 class="post-title">Etiquetas</h1></header>
	
	        <section class="post-content">
	        	<ul><c:forEach items="${tags}" var="tag" varStatus="status"><stripes:url beanclass="com.hectorlopezfernandez.action.TagsAction" var="tagUrl"><stripes:param name="name" value="${tag.nameUrl}"/></stripes:url>
	        		<li><a href="${tagUrl}">${tag.name}</a> &raquo; ${tag.count} entrada<c:choose><c:when test="${tag.count ne 1}">s</c:when></c:choose></li>
</c:forEach>	        	</ul>
	        </section>
	
	        <footer class="post-footer">
	        </footer>
	    </article>
	</main>

<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>
</body>

</html>