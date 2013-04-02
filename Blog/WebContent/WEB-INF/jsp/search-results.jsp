<%@page import="com.hectorlopezfernandez.dto.SearchResult"%><%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><!DOCTYPE html>
<html dir="ltr" lang="es-ES">
<c:set var="pageName" value="Resultados para la b&uacute;squeda: ${actionBean.parsedQueryText}"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="queryText" value="${actionBean.q}"/><c:set var="postType" value="<%=SearchResult.POST_TYPE%>"/>
<%@include file="/WEB-INF/jsp/fragments/head.jsp"%>

<!--[if lt IE 7 ]><body class="single ie6"><![endif]-->
<!--[if IE 7 ]><body class="single ie7"><![endif]-->
<!--[if IE 8 ]><body class="single ie8"><![endif]-->
<!--[if IE 9 ]><body class="single ie9"><![endif<]-->
<!--[if (gt IE 9)|!(IE)]><!--><body class="search results"><!--<![endif]-->

<div id="body-wrapper" class="clearfix">
<%@include file="/WEB-INF/jsp/fragments/header.jsp"%>
	<ul id="main-wrapper">
		<li id="maincontent" role="main"> <!-- begin maincontent -->
			<header>
				<h1 class="catheader">Resultados para: ${actionBean.parsedQueryText}</h1>
			</header>
<c:choose><c:when test="${empty actionBean.parsedQueryText}">						<div><p>Deber&iacute;as introducir un texto para la b&uacute;squeda. No esperes que te devuelva toda la base de datos...</p></div></c:when><c:when test="${fn:length(actionBean.results) eq 0}">						<div><p>No se han encontrado coincidencias para esa b&uacute;squeda.</p></div></c:when>
<c:otherwise><c:forEach items="${actionBean.results}" var="result" varStatus="status">			<article id="result-${status.index}" class="post <c:choose><c:when test="${status.count % 2 == 1}">odd</c:when><c:otherwise>even</c:otherwise></c:choose>">
				<header>
					<hgroup><c:choose><c:when test="${result.type eq postType}"><fmt:formatNumber minIntegerDigits="2" type="number" value="${result.month}" var="month"/><fmt:formatNumber minIntegerDigits="2" type="number" value="${result.day}" var="day"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="resultUrl"><stripes:param name="year" value="${result.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="day" value="${day}"/><stripes:param name="name" value="${result.titleUrl}"/></stripes:url></c:when><c:otherwise><stripes:url beanclass="com.hectorlopezfernandez.action.PagesAction" var="resultUrl"><stripes:param name="name" value="${result.titleUrl}"/></stripes:url></c:otherwise></c:choose>
						<h1 class="posttitle"><a href="${resultUrl}" title="Enlace a ${result.title}" rel="bookmark">${result.title}</a></h1>
						<h2 class="meta"><c:choose><c:when test="${result.type eq postType}">categor&iacute;a: entrada</c:when><c:otherwise>categor&iacute;a: p&aacute;gina</c:otherwise></c:choose> &#8226; publicada el <joda:format value="${result.publicationDate}" style="F-" locale="es_ES"/></h2>
					</hgroup>
				</header>
				<div class="storycontent">
					<p>${h:limitLength(result.content,300)}</p>
					<a href="${resultUrl}" class="more-link">Seguir leyendo &raquo;</a>
				</div> 
			</article></c:forEach></c:otherwise></c:choose>
		</li> <!-- end #maincontent -->
<%@include file="/WEB-INF/jsp/fragments/sidebar.jsp"%>
	</ul> <!-- end #main-wrapper ul -->
<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>
</div> <!-- end #body-wrapper -->
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>
</body>

</html>