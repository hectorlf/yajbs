<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:formatNumber minIntegerDigits="2" type="number" value="${post.month}" var="month"/><fmt:formatNumber minIntegerDigits="2" type="number" value="${post.day}" var="day"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="postUrl"><stripes:param name="year" value="${post.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="day" value="${day}"/><stripes:param name="name" value="${post.titleUrl}"/></stripes:url><stripes:url beanclass="com.hectorlopezfernandez.action.AuthorsAction" var="authorUrl"><stripes:param name="name" value="${post.author.username}"/></stripes:url>			<article id="post-${post.id}" class="post">
				<a href="${postUrl}" title="${post.title}" class="image-anchor"><img width="600" height="250" src="${post.headerImageUrl}" class="alignnone wp-post-image"/></a>
				<header>
					<hgroup>
						<h1 class="posttitle"><a href="${postUrl}" title="Enlace permanente a ${post.title}" rel="bookmark">${post.title}</a></h1>
						<h2 class="meta">por <a href="${authorUrl}" title="Entradas de ${post.author.displayName}" rel="author">${post.author.displayName}</a> &#8226; publicado el <joda:format value="${post.publicationDate}" style="F-" locale="es_ES"/><c:if test="${post.lastModificationDateAsLong gt post.publicationDateAsLong}"> &#8226; editado el <joda:format value="${post.lastModificationDate}" style="F-" locale="es_ES"/></c:if><c:if test="${fn:length(post.comments) > 0 || !post.commentsClosed}"> &#8226; <a href="${postUrl}#comments" title="Comentarios sobre ${post.title}">${fn:length(post.comments)} comentario<c:if test="${fn:length(post.comments) ne 1}">s</c:if></a></c:if></h2>
<c:if test="${fn:length(post.tags) > 0}">						<h2 class="meta">etiquetado en <c:forEach items="${post.tags}" var="tag" varStatus="status"><stripes:url beanclass="com.hectorlopezfernandez.action.TagsAction" var="tagUrl"><stripes:param name="name" value="${tag.nameUrl}"/></stripes:url><c:if test="${status.index > 0}">, </c:if><a href="${tagUrl}" title="Entradas etiquetadas en ${tag.name}" rel="category tag">${tag.name}</a></c:forEach></h2>
</c:if>					</hgroup>
				</header>
				<div class="storycontent">
					${post.content}
				</div> 
				<footer class="clearfix fl"></footer> 
			</article>

<c:set var="author" value="${post.author}"/><%@include file="/WEB-INF/jsp/fragments/author-detail.jsp"%>