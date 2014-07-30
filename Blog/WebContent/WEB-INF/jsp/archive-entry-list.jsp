<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><!DOCTYPE html>
<c:set var="pageName" value="Archivo"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="entries" value="${actionBean.entries}"/>
<html>

<%@include file="/WEB-INF/jsp/fragments/head.jsp"%>

<body>
<%@include file="/WEB-INF/jsp/fragments/header.jsp"%>

	<main role="main">
		<article>
	        <header><h1 class="post-title">Archivo</h1></header>
	
	        <section class="post-content">
	        	<ul><c:forEach items="${entries}" var="entry" varStatus="status"><fmt:formatNumber minIntegerDigits="2" type="number" value="${entry.month}" var="month"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="monthUrl"><stripes:param name="year" value="${entry.year}"/><stripes:param name="month" value="${month}"/></stripes:url><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="yearUrl"><stripes:param name="year" value="${entry.year}"/></stripes:url><joda:format value="${entry.date}" pattern="MMMM" locale="es_ES" var="entryMonth"/><joda:format value="${entry.date}" pattern="yyyy" locale="es_ES" var="entryYear"/>
	        		<li><a href="${monthUrl}" title="${entryMonth}">${h:toTitleCase(entryMonth)}</a> de <a href="${yearUrl}" title="${entryYear}">${entryYear}</a> &raquo; ${entry.count} entrada<c:choose><c:when test="${entry.count ne 1}">s</c:when></c:choose></li>
</c:forEach>	        	</ul>
	        </section>
	
	        <footer class="post-footer">
	        </footer>
	    </article>
	</main>

<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>

<%@include file="/WEB-INF/jsp/fragments/deferred-styles.jsp"%>
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>
</body>

</html>