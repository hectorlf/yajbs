<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><!DOCTYPE html>
<html dir="ltr" lang="es-ES">
<c:set var="pageName" value="Inicio"/><c:set var="blogTitle" value="${preferences.title}"/><c:set var="blogTagline" value="${preferences.tagline}"/><c:set var="posts" value="${actionBean.posts}"/><c:set var="metaDescription" value="Un blog sobre Internet, Java, Android, tecnología y consultoría, aderezado con historias de terror extraídas del curro diario."/>
<%@include file="/WEB-INF/jsp/fragments/head.jsp"%>

<!--[if lt IE 7 ]><body class="home ie6"><![endif]-->
<!--[if IE 7 ]><body class="home ie7"><![endif]-->
<!--[if IE 8 ]><body class="home ie8"><![endif]-->
<!--[if IE 9 ]><body class="home ie9"><![endif<]-->
<!--[if (gt IE 9)|!(IE)]><!--><body class="home"><!--<![endif]-->

<div id="body-wrapper" class="clearfix">
<%@include file="/WEB-INF/jsp/fragments/header.jsp"%>
	<ul id="main-wrapper">
		<li id="maincontent" role="main"> <!-- begin maincontent -->
<c:forEach items="${posts}" var="post" begin="0" end="0"><c:set var="position" value="1"/><%@include file="/WEB-INF/jsp/fragments/post-excerpt-fullwidth-image.jsp"%></c:forEach>
<c:forEach items="${posts}" var="post" begin="1" end="1"><c:set var="position" value="2"/><%@include file="/WEB-INF/jsp/fragments/post-excerpt-leftside-image.jsp"%></c:forEach>
			<div id="indexposts" class="clear fl">
<c:forEach items="${posts}" var="post" varStatus="status" begin="2"><c:set var="position" value="${status.count}"/><%@include file="/WEB-INF/jsp/fragments/post-excerpt-no-image.jsp"%></c:forEach>
			</div>
		</li> <!-- end #maincontent -->
<%@include file="/WEB-INF/jsp/fragments/sidebar.jsp"%>
	</ul> <!-- end #main-wrapper ul -->
<%@include file="/WEB-INF/jsp/fragments/footer.jsp"%>
</div> <!-- end #body-wrapper -->
<%@include file="/WEB-INF/jsp/fragments/tracking.jsp"%>
</body>

</html>