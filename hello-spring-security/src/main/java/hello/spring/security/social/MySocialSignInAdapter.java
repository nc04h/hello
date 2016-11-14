package hello.spring.security.social;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

public class MySocialSignInAdapter implements SignInAdapter {

	private static final Logger log = Logger.getLogger(MySocialSignInAdapter.class);

	private final RequestCache requestCache;

	@Autowired
	public MySocialSignInAdapter(RequestCache requestCache) {
		log.debug("MySocialSignInAdapter");
		this.requestCache = requestCache;
	}

	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		log.debug("signIn " + userId);
		String providerUserId = connection.getKey().getProviderUserId();
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(providerUserId, null, null));
		return extractOriginalUrl(request);
	}

	private String extractOriginalUrl(NativeWebRequest request) {
		log.debug("extractOriginalUrl " + request);
		HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
		SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
		if (saved == null) {
			return null;
		}
		requestCache.removeRequest(nativeReq, nativeRes);
		removeAutheticationAttributes(nativeReq.getSession(false));
		log.debug(saved.getRedirectUrl());
		return saved.getRedirectUrl();
	}

	private void removeAutheticationAttributes(HttpSession session) {
		log.debug("removeAutheticationAttributes " + session);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
