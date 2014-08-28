<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><fmt:formatNumber minIntegerDigits="2" type="number" value="${post.month}" var="month"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="postUrl"><stripes:param name="year" value="${post.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="name" value="${post.titleUrl}"/></stripes:url>		<article>
	        <header>
		        <c:if test="${fn:length(post.tags) > 0}"><div class="post-meta tags">Etiquetado en <c:forEach items="${post.tags}" var="tag" varStatus="status"><stripes:url beanclass="com.hectorlopezfernandez.action.TagsAction" var="tagUrl"><stripes:param name="name" value="${tag.nameUrl}"/></stripes:url><c:if test="${status.index > 0}">, </c:if><a href="${tagUrl}" rel="category tag">${tag.name}</a></c:forEach></div></c:if>
		        <h1 class="post-title">${post.title}</h1>
		        <div class="post-meta"><time datetime="<joda:format value="${post.publicationDate}" pattern="yyyy-MM-dd"/>"><joda:format value="${post.publicationDate}" pattern="EEEE, d 'de' MMMM, yyyy" locale="es_ES"/></time></div>
	        </header>
	
	        <section class="post-content">
	        	${post.excerpt}
	        	<div id="readmore"></div>
	            ${post.content}
	        </section>
	
	        <section class="share">
	            <p class="info prompt">Compartir esta entrada</p>
	            <a href="https://twitter.com/share?text=${post.title}&url=https://${alias.name}${postUrl}"
	                onclick="window.open(this.href, 'twitter-share', 'width=550,height=235');return false;">
	                <i class="fa fa-2x fa-fw fa-twitter"></i> <span class="hidden">Twitter</span>
	            </a>
	            <a href="https://www.facebook.com/sharer/sharer.php?u=https://${alias.name}${postUrl}"
	                onclick="window.open(this.href, 'facebook-share','width=580,height=296');return false;">
	                <i class="fa fa-2x fa-fw fa-facebook-square"></i> <span class="hidden">Facebook</span>
	            </a>
	            <a href="https://plus.google.com/share?url=https://${alias.name}${postUrl}"
	               onclick="window.open(this.href, 'google-plus-share', 'width=490,height=530');return false;">
	                <i class="fa fa-2x fa-fw fa-google-plus-square"></i> <span class="hidden">Google+</span>
	            </a>
	        </section>
	
	        <footer class="post-footer">
<c:set var="author" value="${post.author}"/><%@include file="/WEB-INF/jsp/fragments/author-detail.jsp"%>
	        </footer><c:if test="${!post.commentsClosed}">

			<div id="disqus_thread"></div>
			<script type="text/javascript">
				var disqus_shortname = 'hectorlopezfernandez';
				var disqus_identifier = '${post.id}';
				var disqus_title = '${pageName}';
				(function() {
					var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
					dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
					(document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
				})();
			</script>
			<noscript>Se necesita Javascript activado para ver los <a href="http://disqus.com/?ref_noscript">comentarios proporcionados por Disqus.</a></noscript>
			<a href="http://disqus.com" class="dsq-brlink">comments powered by <span class="logo-disqus">Disqus</span></a></c:if>    
	    </article>