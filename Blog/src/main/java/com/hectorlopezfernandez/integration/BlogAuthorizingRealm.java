package com.hectorlopezfernandez.integration;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.model.User;

public class BlogAuthorizingRealm extends AuthorizingRealm {

	private final static Logger logger = LoggerFactory.getLogger(BlogAuthorizingRealm.class);

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Set<String> roles = new HashSet<String>(1);
		roles.add("administrator");
		AuthorizationInfo ai = new SimpleAuthorizationInfo(roles);
		return ai;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upt = (UsernamePasswordToken)token;
		logger.debug("Petición de login recibida para el usuario: {}", upt.getUsername());
		if (upt.getUsername() == null || upt.getUsername().length() == 0) throw new AuthenticationException("El nombre de usuario no puede estar vacio");
		if (upt.getPassword() == null || upt.getPassword().length == 0) throw new AuthenticationException("La contraseña no puede estar vacia");
		User u = null;
		try {
			u = findLogin(upt);
		} catch(NoResultException nre) {
			logger.debug("No se ha encontrado un objeto User para el login: {}", upt.getUsername());
			throw new UnknownAccountException("No se ha encontrado un objeto User para el login: " + upt.getUsername());
		} catch(NonUniqueResultException nue) {
			logger.error("OJO! Se han encontrado varios objetos User para el login: {}. Deberia existir un indice en base de datos impidiendo esto!", upt.getUsername());
			throw new AuthenticationException("Se han encontrado varios objetos User para el login: " + upt.getUsername());
		} catch (Exception e) {
			logger.error("Error desconocido en el acceso a base de datos: " + e.getMessage());
			throw new AuthenticationException("Error desconocido en el acceso a base de datos");
		}
		AuthenticationInfo ai = new SimpleAuthenticationInfo(u.getId(), u.getPassword(), ByteSource.Util.bytes(u.getUsername()), "BlogAuthorizingRealm");
		boolean matched = getCredentialsMatcher().doCredentialsMatch(token, ai);
		if (!matched) {
			logger.debug("La contraseña introducida no es correcta para el login: {}", upt.getUsername());
			throw new AuthenticationException("Contraseña incorrecta para el login: " + upt.getUsername());
		}
		logger.debug("Usuario autenticado correctamente para el login: {}", upt.getUsername());
		return ai;
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		if (token instanceof UsernamePasswordToken) return true;
		return false;
	}

	private User findLogin(UsernamePasswordToken token) {
		EntityManager em = PersistenceThreadLocalHelper.get();
		TypedQuery<Long> q = em.createQuery("select u.id from User u where u.username = :username", Long.class);
		q.setParameter("username", token.getUsername());
		Long id = q.getSingleResult();
		User u = em.find(User.class, id);
		return u;
	}

}