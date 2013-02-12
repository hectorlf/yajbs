<%@page session="false"%><%@page contentType="text/xml"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"><c:set var="hostname">http://${alias.name}</c:set>
	<url><stripes:url beanclass="com.hectorlopezfernandez.action.IndexAction" prependContext="true" var="indexUrl"></stripes:url>
		<loc>${hostname}${indexUrl}</loc>
		<changefreq>daily</changefreq>
	</url><c:forEach items="${actionBean.posts}" var="post">
	<url><fmt:formatNumber minIntegerDigits="2" type="number" value="${post.month}" var="month"/><fmt:formatNumber minIntegerDigits="2" type="number" value="${post.day}" var="day"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" prependContext="true" var="postUrl"><stripes:param name="year" value="${post.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="day" value="${day}"/><stripes:param name="name" value="${post.titleUrl}"/></stripes:url>
		<loc>${hostname}${postUrl}</loc>
		<lastmod><joda:format value="${post.lastModificationDate}" pattern="yyyy-MM-dd"/></lastmod>
		<changefreq>monthly</changefreq>
	</url></c:forEach><c:forEach items="${actionBean.pages}" var="page">
	<url><stripes:url beanclass="com.hectorlopezfernandez.action.PagesAction" var="pageUrl"><stripes:param name="name" value="${page.titleUrl}"/></stripes:url>
		<loc>${hostname}${pageUrl}</loc>
		<lastmod><joda:format value="${page.lastModificationDate}" pattern="yyyy-MM-dd"/></lastmod>
		<changefreq>monthly</changefreq>
	</url></c:forEach>
</urlset>