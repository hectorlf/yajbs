<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><fmt:formatNumber minIntegerDigits="2" type="number" value="${post.month}" var="month"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="postUrl"><stripes:param name="year" value="${post.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="name" value="${post.titleUrl}"/></stripes:url><stripes:url beanclass="com.hectorlopezfernandez.action.AuthorsAction" var="authorUrl"><stripes:param name="name" value="${post.author.username}"/></stripes:url>
			<article id="post-${post.id}" class="post sticky pw pw2 <c:choose><c:when test="${position % 2 == 1}">odd</c:when><c:otherwise>even</c:otherwise></c:choose>">
				<a href="${postUrl}" title="${post.title}" class="image-anchor"><img width="150" height="150" src="${post.sideImageUrl}" class="alignleft wp-post-image"/></a>
				<div class="content-col">
					<header>
						<hgroup>
							<h1 class="posttitle"><a href="${postUrl}" title="Enlace permanente a ${post.title}" rel="bookmark">${post.title}</a></h1>
							<h2 class="meta">por <a href="${authorUrl}" title="Entradas de ${post.author.displayName}" rel="author">${post.author.displayName}</a> &#8226; <joda:format value="${post.publicationDate}" style="F-" locale="es_ES"/><c:if test="${fn:length(post.comments) > 0 || !post.commentsClosed}"> &#8226; <a href="${postUrl}#comments" title="Comentarios sobre ${post.title}">${fn:length(post.comments)} comentario<c:if test="${fn:length(post.comments) ne 1}">s</c:if></a></c:if></h2>
						</hgroup>
					</header>
					<div class="storycontent">
						${post.excerpt}
						<a href="${postUrl}" class="more-link">Seguir leyendo &raquo;</a>
					</div> 
				</div>
			</article>
