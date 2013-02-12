<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:if test="${fn:length(post.comments) > 0 || !post.commentsClosed}">			<div id="comments"> <!-- beging comments -->
				<h3 id="comments-title">${fn:length(post.comments)} comentario<c:if test="${fn:length(post.comments) ne 1}">s</c:if> sobre <em>${post.title}</em></h3>
				<ol class="commentlist">
<c:forEach items="${post.comments}" var="comment" varStatus="status">
					<li class="comment <c:choose><c:when test="${status.count % 2 eq 0}">even</c:when><c:otherwise>odd</c:otherwise></c:choose>" id="li-comment-${comment.id}">
						<div id="comment-${comment.id}">
							<div class="comment-avatar"><img src="http://media.hectorlopezfernandez.com/avatar-${comment.author.username}.jpg" class="avatar" height="60" width="60"></div>     
							<div class="comment-content">
								<div class="comment-author"><%--<a href="${authorUrl}" rel="external nofollow" class="url">--%>${comment.author.displayName}</div>
								<div class="comment-meta"><joda:format value="${comment.publicationDate}" style="FM" locale="es_ES"/></div>
								<div class="comment-text"><p>${comment.content}</p></div>
<%--								<div class="reply">
									<a class="comment-reply-link" href="http://demos.presswork.me/blog/2008/comment-test/?replytocom=13#respond" onclick="return addComment.moveForm(&quot;comment-13&quot;, &quot;13&quot;, &quot;respond&quot;, &quot;149&quot;)">Reply</a>
								</div>--%>
							</div>
						</div>
					</li>
</c:forEach>
				</ol>
				<div id="respond">
<c:choose><c:when test="${!post.commentsClosed}">					<h3 id="reply-title">Dejar un comentario</h3><stripes:url beanclass="com.hectorlopezfernandez.action.AddCommentAction" var="addCommentUrl"/>
					<form action="${addCommentUrl}" method="post" id="commentform">
					<p class="comment-notes">La direcci&oacute;n de email no ser&aacute; publicada.</p>
					<p class="comment-form-author"><input id="author" name="author" type="text" required="true" value="" size="30" placeholder="Your name *" aria-required="true"></p>
					<p class="comment-form-email"><input id="email" name="email" type="email" required="true" value="" size="30" placeholder="Your email *" aria-required="true"></p>
					<p class="comment-form-url"><input id="url" name="url" type="url" value="" size="30" placeholder="Your website"></p>
					<p class="comment-form-comment"><textarea id="comment" name="commentText" required="true" cols="45" rows="8" placeholder="Your comment *" aria-required="true"></textarea></p>
					<p class="form-submit">
						<input name="submit" type="submit" id="submit" value="Post Comment">
						<input type="hidden" name="comment_post_ID" value="149" id="comment_post_ID">
						<input type="hidden" name="comment_parent" id="comment_parent" value="12">
					</p>
					</form>
</c:when><c:otherwise>					<h3 id="reply-title">Los comentarios est&aacute;n cerrados</h3>
</c:otherwise></c:choose>				</div><!-- #respond -->
			</div> <!-- end comments --></c:if>