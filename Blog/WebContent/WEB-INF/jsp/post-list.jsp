<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><!DOCTYPE html>
<html dir="ltr" lang="es-ES">
<c:choose><c:when test="${!empty actionBean.month}"><c:set var="date">en <joda:format value="${actionBean.searchDate}" pattern="MMMM 'de' yyyy" locale="es_ES"/></c:set></c:when><c:otherwise><c:set var="date">en <joda:format value="${actionBean.searchDate}" pattern="yyyy"/></c:set></c:otherwise></c:choose>
<c:set var="pageName" value="Entradas archivadas ${date}"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="posts" value="${actionBean.posts}"/>
<%@include file="/WEB-INF/jsp/fragments/head.jsp"%>

<!--[if lt IE 7 ]><body class="single ie6"><![endif]-->
<!--[if IE 7 ]><body class="single ie7"><![endif]-->
<!--[if IE 8 ]><body class="single ie8"><![endif]-->
<!--[if IE 9 ]><body class="single ie9"><![endif<]-->
<!--[if (gt IE 9)|!(IE)]><!--><body class="archive date"><!--<![endif]-->

<div id="body-wrapper" class="clearfix">
<%@include file="/WEB-INF/jsp/fragments/header.jsp"%>
	<ul id="main-wrapper">
		<li id="maincontent" role="main"> <!-- begin maincontent -->
			<header>
				<h1 class="catheader">Entradas archivadas ${date}</h1>
			</header>
<c:if test="${fn:length(posts) eq 0}">						<div><p>No se encontr&oacute; ning&uacute;n resultado.</p></div>
</c:if><c:forEach items="${posts}" var="post" varStatus="status">						<c:set var="position" value="${status.count}"/><%@include file="/WEB-INF/jsp/fragments/post-excerpt-no-image.jsp"%></c:forEach>
		</li> <!-- end #maincontent -->
<%@include file="/WEB-INF/jsp/fragments/sidebar.jsp"%>
	</ul> <!-- end #main-wrapper ul -->
<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>
</div> <!-- end #body-wrapper -->
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>
</body>

</html>