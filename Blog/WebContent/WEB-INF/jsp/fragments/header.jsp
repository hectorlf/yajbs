<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><stripes:url beanclass="com.hectorlopezfernandez.action.IndexAction" var="indexUrl"/><stripes:url beanclass="com.hectorlopezfernandez.action.PagesAction" var="curriculumUrl"><stripes:param name="name" value="curriculum"/></stripes:url><stripes:url beanclass="com.hectorlopezfernandez.action.PagesAction" var="githubUrl"><stripes:param name="name" value="github"/></stripes:url>
	<header id="header-main" role="banner"> <!-- begin header --><shiro:authenticated>
		<%@include file="logged-user-menu.jsp"%>
</shiro:authenticated>		<ul id="headerbanner" class="clearfix">
			<li id="header_logo" class="mainl">
				<div id="site-logo" class="siteheader">
					<h1><a href="${indexUrl}" title="${blogTitle}">${blogTitle}</a></h1> <span style="font-style: italic;">{ ${blogTagline} }</span>
				</div>
			</li>
			<li id="nav" class="mainl">
				<nav class="clear fl" role="navigation">
					<h3 class="assistive-text">Men&uacute;</h3>
					<ul id="main-menu" class="menu">
						<li id="inicio"><a href="${indexUrl}">Inicio</a></li>
						<li id="curriculum"><a href="${curriculumUrl}">Curr&iacute;culum</a></li>
						<li id="github"><a href="${githubUrl}">GitHub</a></li>
					</ul>
				</nav>
			</li>
		</ul> <!-- end headerbanner -->
	</header> <!-- end header -->