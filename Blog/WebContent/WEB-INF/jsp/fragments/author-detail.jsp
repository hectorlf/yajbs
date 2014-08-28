<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>				<section class="author">
					<div class="authorimage" style="background:url(//d9xqzluw8al1.cloudfront.net/avatar-${author.username}.jpg)"></div>
					<p class="attr">Autor</p>
					<h4><a href="<c:url value="${author.relatedUrl}"/>">${author.displayName}</a></h4>
					<p class="bio">${author.about}</p>
				</section>