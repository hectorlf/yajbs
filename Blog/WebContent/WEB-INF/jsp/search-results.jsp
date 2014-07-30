<%@page session="false"%><%@page import="com.hectorlopezfernandez.dto.SearchResult"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><!DOCTYPE html>
<c:set var="pageName" value="Resultados para la b&uacute;squeda: ${actionBean.parsedQueryText}"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="queryText" value="${actionBean.q}"/><c:set var="postType" value="<%=SearchResult.POST_TYPE%>"/>
<html>

<%@include file="/WEB-INF/jsp/fragments/head.jsp"%>

<body>
<%@include file="/WEB-INF/jsp/fragments/header.jsp"%>

	<main role="main">
<c:choose>
	<c:when test="${empty actionBean.parsedQueryText}">
		<article class="preview">
			<header>
	            <div class="post-meta">Deber&iacute;as introducir un texto para la b&uacute;squeda. No esperes que te devuelva toda la base de datos&hellip;</div>
			</header>
		</article>
	</c:when>
	<c:when test="${fn:length(actionBean.results) eq 0}">
		<article class="preview">
			<header>
	            <div class="post-meta">No se han encontrado coincidencias para esa b&uacute;squeda</div>
			</header>
		</article>
	</c:when>
	<c:otherwise><c:forEach items="${actionBean.results}" var="result" varStatus="status">
		<c:choose>
			<c:when test="${result.type eq postType}"><fmt:formatNumber minIntegerDigits="2" type="number" value="${result.month}" var="month"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="resultUrl"><stripes:param name="year" value="${result.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="name" value="${result.titleUrl}"/></stripes:url></c:when>
			<c:otherwise><stripes:url beanclass="com.hectorlopezfernandez.action.PagesAction" var="resultUrl"><stripes:param name="name" value="${result.titleUrl}"/></stripes:url></c:otherwise>
		</c:choose>
		<article class="preview">
			<header>
	            <h1 class="post-title"><a href="${resultUrl}">${result.title}</a></h1>
	            <div class="post-meta">
					<c:choose>
						<c:when test="${result.type eq postType}">categor&iacute;a: entrada</c:when>
						<c:otherwise>categor&iacute;a: p&aacute;gina</c:otherwise>
					</c:choose> &#8226; publicada el <time datetime="<joda:format value="${result.publicationDate}" pattern="yyyy-MM-dd"/>"><joda:format value="${result.publicationDate}" pattern="d 'de' MMMM 'de' yyyy" locale="es_ES"/></time>	            
	            </div>
	        </header>
	        <section class="post-excerpt">
	            <p>${result.content}</p>
	            <p class="readmore"><a href="${resultUrl}">Ir al contenido <i class="fa fa-chevron-circle-right"></i></a></p>
	        </section>
	    </article>
	</c:forEach></c:otherwise>
</c:choose>
	</main>

<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>

<%@include file="/WEB-INF/jsp/fragments/deferred-styles.jsp"%>
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>
</body>

</html>