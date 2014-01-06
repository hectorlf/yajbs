<%@page import="com.hectorlopezfernandez.utils.Constants"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><stripes:useActionBean beanclass="com.hectorlopezfernandez.action.RecentArchiveEntriesAction" event="execute" var="archiveEntriesBean"/><stripes:useActionBean beanclass="com.hectorlopezfernandez.action.PopularTagsAction" event="execute" var="popularTagsBean"/><stripes:useActionBean beanclass="com.hectorlopezfernandez.action.RecentCommentsAction" event="execute" var="recentCommentsBean"/>
	<footer id="footer-main" class="clearfix fl" role="contentinfo"> <!-- begin footer -->
		<ul id="footer" class="clearfix">
			<li id="extendedfooter" class="foot" role="complementary">
				<aside id="archives" class="widget1 bottom-widget widget_archive">
					<h3><stripes:link beanclass="com.hectorlopezfernandez.action.ArchiveAction">Archivo</stripes:link></h3>
					<ul>
<c:forEach items="${archiveEntriesBean.entries}" var="entry"><fmt:formatNumber minIntegerDigits="2" type="number" value="${entry.month}" var="month"/><stripes:url beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="monthlyArchiveUrl"><stripes:param name="year" value="${entry.year}"/><stripes:param name="month" value="${month}"/></stripes:url><joda:format value="${entry.date}" pattern="MMMM yyyy" locale="es_ES" var="entryDateAsText"/>						<li><a href="${monthlyArchiveUrl}" title="Entradas de ${entryDateAsText}">${entryDateAsText}</a></li>
</c:forEach>					</ul>
				</aside>
				<aside id="tagcloud" class="widget2 bottom-widget widget_tag_cloud">
					<h3><stripes:link beanclass="com.hectorlopezfernandez.action.TagsAction">Etiquetas frecuentes</stripes:link></h3>
					<div>
<c:forEach items="${popularTagsBean.tags}" var="tag" varStatus="status"><stripes:url beanclass="com.hectorlopezfernandez.action.TagsAction" var="tagUrl"><stripes:param name="name" value="${tag.nameUrl}"/></stripes:url>						<span><c:if test="${status.index > 0}">&#8226; </c:if><a href="${tagUrl}" title="${tag.name}" style="font-size:8pt;">${tag.name}</a></span>
</c:forEach>					</div>
				</aside>
				<aside id="recent-comments-3" class="widget3 bottom-widget widget_recent_comments">
					<h3>&Uacute;ltimos comentarios</h3>
					<ul id="recentcomments">
<c:forEach items="${recentCommentsBean.comments}" var="comment"><fmt:formatNumber minIntegerDigits="2" type="number" value="${comment.post.month}" var="month"/><stripes:url anchor="comment-${comment.id}" beanclass="com.hectorlopezfernandez.action.ArchiveAction" var="commentUrl"><stripes:param name="year" value="${comment.post.year}"/><stripes:param name="month" value="${month}"/><stripes:param name="name" value="${comment.post.titleUrl}"/></stripes:url>						<li class="recentcomments">${comment.author.displayName} en <a href="${commentUrl}">${comment.post.title}</a></li>
</c:forEach>					</ul>
				</aside>
			</li>
			<li id="copyright" class="foot"><a rel="license" title="Esta obra est&aacute; bajo una licencia Creative Commons" href="http://creativecommons.org/licenses/by/3.0/es/" style="vertical-align:middle;"><img alt="Esta obra est&aacute; bajo una licencia Creative Commons" style="border-width:0" src="http://i.creativecommons.org/l/by/3.0/es/80x15.png" /></a> &#8226; <stripes:link beanclass="com.hectorlopezfernandez.action.PagesAction"><stripes:param name="name" value="aviso-legal"/>Aviso legal</stripes:link> &#8226; Served by YAJBS 0.3 &#8226; Original design by <a href="http://presswork.me">PressWork</a></li>
		</ul> <!-- end #footer -->
	</footer> <!-- end footer -->