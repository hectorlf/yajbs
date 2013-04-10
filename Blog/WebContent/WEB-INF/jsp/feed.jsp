<%@page session="false"%><%@page contentType="application/atom+xml"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom"><stripes:url beanclass="com.hectorlopezfernandez.action.IndexAction" var="indexUrl"/><stripes:url beanclass="com.hectorlopezfernandez.action.FeedAction" var="feedUrl"/>
	<title>${preferences.title}</title>
	<link rel="self" href="http://${alias.name}${feedUrl}"/>
	<updated><joda:format value="${actionBean.lastModificationDate}" pattern="yyyy-MM-dd'T'HH:mm:ssZZ"/></updated>
	<id>http://${alias.name}/</id>	
<c:forEach items="${actionBean.posts}" var="post">	<entry><fmt:formatNumber minIntegerDigits="2" type="number" value="${post.month}" var="month"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="postUrl"><stripes:param name="year" value="${post.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="name" value="${post.titleUrl}"/></stripes:url>
		<title type="text">${post.title}</title>
		<link rel="alternate" href="http://${alias.name}${postUrl}"/>
		<id>http://${alias.name}${postUrl}</id>
		<updated><joda:format value="${post.publicationDate}" pattern="yyyy-MM-dd'T'HH:mm:ssZZ"/></updated>
		<summary type="xhtml"><div xmlns="http://www.w3.org/1999/xhtml">${post.excerpt}</div></summary>
		<author><name>${post.authorName}</name></author>
	</entry>
</c:forEach></feed>