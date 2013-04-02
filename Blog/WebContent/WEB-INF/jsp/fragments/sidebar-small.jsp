<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>		<li id="firstsidebar" role="complementary" class="el2"> <!-- begin firstsidebar -->
<%@include file="/WEB-INF/jsp/fragments/small-search-bar.jsp"%>
			<aside id="who" class="side-widget">
				<header><h3>&iquest;Qui&eacute;n?</h3></header>
				<article id="who" class="post side-featured odd">
					<img width="50" height="65" src="http://media.hectorlopezfernandez.com/hector.png" class="alignleft wp-post-image" alt="H&eacute;ctor" title="H&eacute;ctor"/>
					<div class="content-col">
						<header>
							<h1 class="posttitle">H&eacute;ctor L&oacute;pez Fern&aacute;ndez</h1>
						</header>
						<div class="storycontent">
							<p class="excerpt">Ingeniero de estudios, consultor de T.I. de profesi&oacute;n, afincado en Madrid y con la cabeza en la Web y en Android.</p>
						</div>
					</div>
				</article>
			</aside>
			<aside id="feeds" class="side-widget">
				<header><h3>&iquest;Quieres suscribirte?</h3></header>
				<ul>
					<li><a href="<stripes:url beanclass="com.hectorlopezfernandez.action.FeedAction"/>" target="_blank"><img width="20" height="20" src="http://media.hectorlopezfernandez.com/rss20x20.png" style="vertical-align: middle;"/> feed de entradas en ATOM</a></li>
				</ul>
			</aside>
			<aside id="contacto" class="side-widget">
				<header><h3>Contacto</h3></header>
				<ul>
					<li><a href="http://es.linkedin.com/in/hectorlopezfernandez" target="_blank"><img width="20" height="20" src="http://media.hectorlopezfernandez.com/linkedin20x20.png" style="vertical-align: middle;"/> linkedin</a></li>
					<li><a href="http://twitter.com/hectorlf" target="_blank"><img width="20" height="20" src="http://media.hectorlopezfernandez.com/twitter20x20.png" style="vertical-align: middle;"/> twitter</a></li>
					<li><a href="http://plus.google.com/115609463128735595001" target="_blank"><img width="20" height="20" src="http://media.hectorlopezfernandez.com/gplus20x20.png" style="vertical-align: middle;"/> google plus</a></li>
				</ul>
			</aside>
			<aside id="creditos" class="side-widget">
				<header><h3>Cr&eacute;ditos</h3></header>
				<ul>
					<li><a href="http://presswork.me" target="_blank">PressWork</a> - Dise&ntilde;o del blog</li>
					<li><a href="http://arbent.net/blog" target="_blank">Arbenting</a> - Iconos de redes sociales</li>
				</ul>
			</aside>
		</li> <!-- end firstsidebar -->