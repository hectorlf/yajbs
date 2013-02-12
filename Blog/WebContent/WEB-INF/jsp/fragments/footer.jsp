<%@page import="com.hectorlopezfernandez.utils.Constants"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><stripes:useActionBean beanclass="com.hectorlopezfernandez.action.RecentArchiveEntriesAction" event="execute" var="archiveEntriesBean"/><stripes:useActionBean beanclass="com.hectorlopezfernandez.action.PopularTagsAction" event="execute" var="popularTagsBean"/><stripes:useActionBean beanclass="com.hectorlopezfernandez.action.RecentCommentsAction" event="execute" var="recentCommentsBean"/>
	<footer id="footer-main" class="clearfix fl" role="contentinfo"> <!-- begin footer -->
		<ul id="footer" class="clearfix">
			<li id="extendedfooter" class="foot" role="complementary">
				<aside id="archives" class="widget1 bottom-widget widget_archive">
					<h3><stripes:link beanclass="com.hectorlopezfernandez.action.ArchiveAction">Archivo</stripes:link></h3>
					<ul>
<c:forEach items="${archiveEntriesBean.entries}" var="entry"><fmt:formatNumber minIntegerDigits="2" type="number" value="${entry.month}" var="month"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="monthlyArchiveUrl"><stripes:param name="year" value="${entry.year}"/><stripes:param name="month" value="${month}"/></stripes:url><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="yearlyArchiveUrl"><stripes:param name="year" value="${entry.year}"/></stripes:url><joda:format value="${entry.date}" pattern="MMMM" locale="es_ES" var="entryMonth"/><joda:format value="${entry.date}" pattern="yyyy" locale="es_ES" var="entryYear"/>						<li><a href="${monthlyArchiveUrl}" title="${entryMonth}">${h:toTitleCase(entryMonth)}</a> de <a href="${yearlyArchiveUrl}" title="${entryYear}">${entryYear}</a></li>
</c:forEach>					</ul>
				</aside>
				<aside id="tagcloud" class="widget2 bottom-widget widget_tag_cloud">
					<h3><stripes:link beanclass="com.hectorlopezfernandez.action.TagsAction">Etiquetas frecuentes</stripes:link></h3>
					<div>
<c:forEach items="${popularTagsBean.tags}" var="tag" varStatus="status"><stripes:url beanclass="com.hectorlopezfernandez.action.TagsAction" var="tagUrl"><stripes:param name="name" value="${tag.nameUrl}"/></stripes:url>						<c:if test="${status.index > 0}">&#8226; </c:if><a href="${tagUrl}" title="${tag.count} entrada<c:if test="${tag.count != 1}">s</c:if>" style="font-size:8pt;">${tag.name}</a>
</c:forEach>					</div>
				</aside>
				<aside id="recent-comments-3" class="widget3 bottom-widget widget_recent_comments">
					<h3>Comentarios recientes</h3>
					<ul id="recentcomments">
<c:forEach items="${recentCommentsBean.comments}" var="comment"><fmt:formatNumber minIntegerDigits="2" type="number" value="${comment.post.month}" var="month"/><fmt:formatNumber minIntegerDigits="2" type="number" value="${comment.post.day}" var="day"/><stripes:url anchor="comment-${comment.id}" beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="commentUrl"><stripes:param name="year" value="${comment.post.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="day" value="${day}"/><stripes:param name="name" value="${comment.post.titleUrl}"/></stripes:url>						<li class="recentcomments">${comment.author.displayName} en <a href="${commentUrl}">${comment.post.title}</a></li>
</c:forEach>					</ul>
				</aside>
			</li>
			<li id="copyright" class="foot">&copy;2013 All Rights Reserved &#8226; Served by <%=Constants.APP_NAME%> <%=Constants.APP_VERSION%> &#8226; Original design by <a href="http://presswork.me">PressWork</a></li>
		</ul> <!-- end #footer -->
	</footer> <!-- end footer -->