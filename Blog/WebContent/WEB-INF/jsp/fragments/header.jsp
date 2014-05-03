<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><stripes:url beanclass="com.hectorlopezfernandez.action.IndexAction" var="indexUrl"/><stripes:url beanclass="com.hectorlopezfernandez.action.PagesAction" var="curriculumUrl"><stripes:param name="name" value="curriculum"/></stripes:url><stripes:url beanclass="com.hectorlopezfernandez.action.PagesAction" var="acercadeUrl"><stripes:param name="name" value="about"/></stripes:url>
	<header id="site-head">
        <a id="blog-logo" href="${indexUrl}"><div class="bloglogo" style="background:url(http://media.hectorlopezfernandez.com/hector.png)"></div></a>
        <h1 class="blog-title"><a href="${indexUrl}">${blogTitle}</a></h1>
        <h2 class="blog-description">${blogTagline}</h2>
        <nav class="menu" role="nav">
		    <ul>
		      <li><stripes:link beanclass="com.hectorlopezfernandez.action.ArchiveAction">Archivo</stripes:link></li>
		      <li><stripes:link beanclass="com.hectorlopezfernandez.action.TagsAction">Etiquetas</stripes:link></li>
		      <li><a href="${curriculumUrl}">Curr&iacute;culum</a></li>
		      <li><a href="https://github.com/hectorlf" target="_blank">GitHub</a></li>
		      <li><a href="${acercadeUrl}">Acerca de&hellip;</a></li><shiro:authenticated>
		      <li><stripes:link beanclass="com.hectorlopezfernandez.action.admin.IndexAction">Administraci&oacute;n</stripes:link></li>
</shiro:authenticated>		    </ul>
		</nav>
    </header>