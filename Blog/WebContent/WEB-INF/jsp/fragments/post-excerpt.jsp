<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><fmt:formatNumber minIntegerDigits="2" type="number" value="${post.month}" var="month"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="postUrl"><stripes:param name="year" value="${post.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="name" value="${post.titleUrl}"/></stripes:url>		<article class="preview">
			<header>
	            <h1 class="post-title"><a href="${postUrl}">${post.title}</a></h1>
	            <div class="post-meta"><time datetime="<joda:format value="${post.publicationDate}" pattern="yyyy-MM-dd"/>"><joda:format value="${post.publicationDate}" pattern="EEEE, d 'de' MMMM, yyyy" locale="es_ES"/></time></div>
	        </header>
	        <section class="post-excerpt">
	            ${post.excerpt}
	            <p class="readmore"><a href="${postUrl}#readmore">Leer el resto de la entrada <i class="fa fa-chevron-circle-right"></i></a></p>
	        </section>
	    </article>