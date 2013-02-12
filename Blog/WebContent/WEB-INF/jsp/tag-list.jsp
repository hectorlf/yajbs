<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><!DOCTYPE html>
<html dir="ltr" lang="es-ES">
<c:set var="pageName" value="Etiquetas"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="tags" value="${actionBean.tags}"/>
<%@include file="/WEB-INF/jsp/fragments/head.jsp"%>

<!--[if lt IE 7 ]><body class="single ie6"><![endif]-->
<!--[if IE 7 ]><body class="single ie7"><![endif]-->
<!--[if IE 8 ]><body class="single ie8"><![endif]-->
<!--[if IE 9 ]><body class="single ie9"><![endif<]-->
<!--[if (gt IE 9)|!(IE)]><!--><body class="single"><!--<![endif]-->

<div id="body-wrapper" class="clearfix">
<%@include file="/WEB-INF/jsp/fragments/header.jsp"%>
	<ul id="main-wrapper">
		<li id="maincontent" role="main"> <!-- begin maincontent -->
			<header>
				<h1 class="catheader">Etiquetas</h1>
			</header>

<c:choose><c:when test="${fn:length(tags) eq 0}">			<p>No se encontr&oacute; ning&uacute;n resultado.</p>
</c:when><c:otherwise>
			<article class="post">
				<div class="storycontent">
					<ul>
<c:forEach items="${tags}" var="tag" varStatus="status"><stripes:url beanclass="com.hectorlopezfernandez.action.TagsAction" var="tagUrl"><stripes:param name="name" value="${tag.nameUrl}"/>
</stripes:url>						<li><a href="${tagUrl}" title="Entradas etiquetadas en ${tag.name}">${tag.name}</a> &raquo; ${tag.count} entrada<c:choose><c:when test="${tag.count ne 1}">s</c:when></c:choose></li>
</c:forEach>					</ul>
				</div>
			</article>
</c:otherwise></c:choose>		</li> <!-- end #maincontent -->
<%@include file="/WEB-INF/jsp/fragments/sidebar.jsp"%>
	</ul> <!-- end #main-wrapper ul -->
<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>
</div> <!-- end #body-wrapper -->
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>
</body>

</html>