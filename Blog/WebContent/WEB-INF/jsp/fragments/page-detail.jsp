<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><stripes:url beanclass="com.hectorlopezfernandez.action.PagesAction" prependContext="true" var="pageUrl"><stripes:param name="name" value="${page.titleUrl}"/></stripes:url>	    <article>
	        <header>
	        	<h1 class="post-title">${page.title}</h1>
	        </header>
	
	        <section class="post-content">
	            ${page.content}
	        </section>
	
	        <section class="share">
	            <p class="info prompt">Compartir esta p&aacute;gina</p>
	            <a href="https://twitter.com/share?text=${page.title}&url=https://www.hectorlopezfernandez.com${pageUrl}"
	                onclick="window.open(this.href, 'twitter-share', 'width=550,height=235');return false;">
	                <i class="fa fa-2x fa-fw fa-twitter"></i> <span class="hidden">Twitter</span>
	            </a>
	            <a href="https://www.facebook.com/sharer/sharer.php?u=https://www.hectorlopezfernandez.com${pageUrl}"
	                onclick="window.open(this.href, 'facebook-share','width=580,height=296');return false;">
	                <i class="fa fa-2x fa-fw fa-facebook-square"></i> <span class="hidden">Facebook</span>
	            </a>
	            <a href="https://plus.google.com/share?url=https://www.hectorlopezfernandez.com${pageUrl}"
	               onclick="window.open(this.href, 'google-plus-share', 'width=490,height=530');return false;">
	                <i class="fa fa-2x fa-fw fa-google-plus-square"></i> <span class="hidden">Google+</span>
	            </a>
	        </section>
	
	        <footer class="post-footer">
	        </footer>
	    </article>