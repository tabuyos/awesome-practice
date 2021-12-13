/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth2.server.authorization.web;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospection;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2TokenIntrospectionHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.TestRegisteredClients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link OAuth2TokenIntrospectionEndpointFilter}.
 *
 * @author Gerardo Roza
 * @author Joe Grandja
 */
public class OAuth2TokenIntrospectionEndpointFilterTests {
	private static final String DEFAULT_TOKEN_INTROSPECTION_ENDPOINT_URI = "/oauth2/introspect";
	private AuthenticationManager authenticationManager;
	private OAuth2TokenIntrospectionEndpointFilter filter;
	private final HttpMessageConverter<OAuth2TokenIntrospection> tokenIntrospectionHttpResponseConverter =
			new OAuth2TokenIntrospectionHttpMessageConverter();
	private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter =
			new OAuth2ErrorHttpMessageConverter();

	@Before
	public void setUp() {
		this.authenticationManager = mock(AuthenticationManager.class);
		this.filter = new OAuth2TokenIntrospectionEndpointFilter(this.authenticationManager);
	}

	@After
	public void cleanup() {
		SecurityContextHolder.clearContext();
	}

	@Test
	public void constructorWhenAuthenticationManagerNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> new OAuth2TokenIntrospectionEndpointFilter(null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("authenticationManager cannot be null");
	}

	@Test
	public void constructorWhenTokenIntrospectionEndpointUriNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> new OAuth2TokenIntrospectionEndpointFilter(this.authenticationManager, null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("tokenIntrospectionEndpointUri cannot be empty");
	}

	@Test
	public void doFilterWhenNotTokenIntrospectionRequestThenNotProcessed() throws Exception {
		String requestUri = "/path";
		MockHttpServletRequest request = new MockHttpServletRequest("POST", requestUri);
		request.setServletPath(requestUri);
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);

		this.filter.doFilter(request, response, filterChain);

		verify(filterChain).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
	}

	@Test
	public void doFilterWhenTokenIntrospectionRequestGetThenNotProcessed() throws Exception {
		String requestUri = DEFAULT_TOKEN_INTROSPECTION_ENDPOINT_URI;
		MockHttpServletRequest request = new MockHttpServletRequest("GET", requestUri);
		request.setServletPath(requestUri);
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);

		this.filter.doFilter(request, response, filterChain);

		verify(filterChain).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
	}

	@Test
	public void doFilterWhenTokenIntrospectionRequestMissingTokenThenInvalidRequestError() throws Exception {
		MockHttpServletRequest request = createTokenIntrospectionRequest(
				"token", OAuth2TokenType.ACCESS_TOKEN.getValue());
		request.removeParameter(OAuth2ParameterNames.TOKEN);

		doFilterWhenTokenIntrospectionRequestInvalidParameterThenError(
				OAuth2ParameterNames.TOKEN, OAuth2ErrorCodes.INVALID_REQUEST, request);
	}

	@Test
	public void doFilterWhenTokenIntrospectionRequestMultipleTokenThenInvalidRequestError() throws Exception {
		MockHttpServletRequest request = createTokenIntrospectionRequest(
				"token", OAuth2TokenType.ACCESS_TOKEN.getValue());
		request.addParameter(OAuth2ParameterNames.TOKEN, "other-token");

		doFilterWhenTokenIntrospectionRequestInvalidParameterThenError(
				OAuth2ParameterNames.TOKEN, OAuth2ErrorCodes.INVALID_REQUEST, request);
	}

	@Test
	public void doFilterWhenTokenIntrospectionRequestMultipleTokenTypeHintThenInvalidRequestError() throws Exception {
		MockHttpServletRequest request = createTokenIntrospectionRequest(
				"token", OAuth2TokenType.ACCESS_TOKEN.getValue());
		request.addParameter(OAuth2ParameterNames.TOKEN_TYPE_HINT, OAuth2TokenType.ACCESS_TOKEN.getValue());

		doFilterWhenTokenIntrospectionRequestInvalidParameterThenError(
				OAuth2ParameterNames.TOKEN_TYPE_HINT, OAuth2ErrorCodes.INVALID_REQUEST, request);
	}

	@Test
	public void doFilterWhenTokenIntrospectionRequestValidThenSuccessResponse() throws Exception {
		RegisteredClient registeredClient = TestRegisteredClients.registeredClient().build();
		Authentication clientPrincipal = new OAuth2ClientAuthenticationToken(
				registeredClient, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, registeredClient.getClientSecret());
		OAuth2AccessToken accessToken = new OAuth2AccessToken(
				OAuth2AccessToken.TokenType.BEARER, "token",
				Instant.now(), Instant.now().plus(Duration.ofHours(1)),
				new HashSet<>(Arrays.asList("scope1", "scope2")));
		// @formatter:off
		OAuth2TokenIntrospection tokenClaims = OAuth2TokenIntrospection.builder(true)
				.clientId("authorized-client-id")
				.username("authorizing-username")
				.issuedAt(accessToken.getIssuedAt())
				.expiresAt(accessToken.getExpiresAt())
				.scopes(scopes -> scopes.addAll(accessToken.getScopes()))
				.tokenType(accessToken.getTokenType().getValue())
				.notBefore(accessToken.getIssuedAt())
				.subject("authorizing-subject")
				.audience("authorized-client-id")
				.issuer("https://provider.com")
				.id("jti")
				.build();
		// @formatter:on
		OAuth2TokenIntrospectionAuthenticationToken tokenIntrospectionAuthenticationResult =
				new OAuth2TokenIntrospectionAuthenticationToken(
						accessToken.getTokenValue(), clientPrincipal, tokenClaims);

		when(this.authenticationManager.authenticate(any())).thenReturn(tokenIntrospectionAuthenticationResult);

		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(clientPrincipal);
		SecurityContextHolder.setContext(securityContext);

		MockHttpServletRequest request = createTokenIntrospectionRequest(
				accessToken.getTokenValue(), OAuth2TokenType.ACCESS_TOKEN.getValue());
		request.addParameter("custom-param-1", "custom-value-1");
		request.addParameter("custom-param-2", "custom-value-2");

		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);

		this.filter.doFilter(request, response, filterChain);

		ArgumentCaptor<OAuth2TokenIntrospectionAuthenticationToken> tokenIntrospectionAuthentication =
				ArgumentCaptor.forClass(OAuth2TokenIntrospectionAuthenticationToken.class);

		verifyNoInteractions(filterChain);
		verify(this.authenticationManager).authenticate(tokenIntrospectionAuthentication.capture());

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(tokenIntrospectionAuthentication.getValue().getAdditionalParameters())
				.contains(
						entry("custom-param-1", "custom-value-1"),
						entry("custom-param-2", "custom-value-2"));

		OAuth2TokenIntrospection tokenIntrospectionResponse = readTokenIntrospectionResponse(response);
		assertThat(tokenIntrospectionResponse.isActive()).isEqualTo(tokenClaims.isActive());
		assertThat(tokenIntrospectionResponse.getClientId()).isEqualTo(tokenClaims.getClientId());
		assertThat(tokenIntrospectionResponse.getUsername()).isEqualTo(tokenClaims.getUsername());
		assertThat(tokenIntrospectionResponse.getIssuedAt()).isBetween(
				tokenClaims.getIssuedAt().minusSeconds(1), tokenClaims.getIssuedAt().plusSeconds(1));
		assertThat(tokenIntrospectionResponse.getExpiresAt()).isBetween(
				tokenClaims.getExpiresAt().minusSeconds(1), tokenClaims.getExpiresAt().plusSeconds(1));
		assertThat(tokenIntrospectionResponse.getScopes()).containsExactlyInAnyOrderElementsOf(tokenClaims.getScopes());
		assertThat(tokenIntrospectionResponse.getTokenType()).isEqualTo(tokenClaims.getTokenType());
		assertThat(tokenIntrospectionResponse.getNotBefore()).isBetween(
				tokenClaims.getNotBefore().minusSeconds(1), tokenClaims.getNotBefore().plusSeconds(1));
		assertThat(tokenIntrospectionResponse.getSubject()).isEqualTo(tokenClaims.getSubject());
		assertThat(tokenIntrospectionResponse.getAudience()).containsExactlyInAnyOrderElementsOf(tokenClaims.getAudience());
		assertThat(tokenIntrospectionResponse.getIssuer()).isEqualTo(tokenClaims.getIssuer());
		assertThat(tokenIntrospectionResponse.getId()).isEqualTo(tokenClaims.getId());
	}

	private void doFilterWhenTokenIntrospectionRequestInvalidParameterThenError(String parameterName, String errorCode,
			MockHttpServletRequest request) throws Exception {

		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);

		this.filter.doFilter(request, response, filterChain);

		verifyNoInteractions(filterChain);

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		OAuth2Error error = readError(response);
		assertThat(error.getErrorCode()).isEqualTo(errorCode);
		assertThat(error.getDescription()).isEqualTo("OAuth 2.0 Token Introspection Parameter: " + parameterName);
	}

	private OAuth2Error readError(MockHttpServletResponse response) throws Exception {
		MockClientHttpResponse httpResponse = new MockClientHttpResponse(
				response.getContentAsByteArray(), HttpStatus.valueOf(response.getStatus()));
		return this.errorHttpResponseConverter.read(OAuth2Error.class, httpResponse);
	}

	private OAuth2TokenIntrospection readTokenIntrospectionResponse(MockHttpServletResponse response) throws Exception {
		MockClientHttpResponse httpResponse = new MockClientHttpResponse(
				response.getContentAsByteArray(), HttpStatus.valueOf(response.getStatus()));
		return this.tokenIntrospectionHttpResponseConverter.read(OAuth2TokenIntrospection.class, httpResponse);
	}

	private static MockHttpServletRequest createTokenIntrospectionRequest(String token, String tokenTypeHint) {
		String requestUri = DEFAULT_TOKEN_INTROSPECTION_ENDPOINT_URI;
		MockHttpServletRequest request = new MockHttpServletRequest("POST", requestUri);
		request.setServletPath(requestUri);
		request.addParameter(OAuth2ParameterNames.TOKEN, token);
		request.addParameter(OAuth2ParameterNames.TOKEN_TYPE_HINT, tokenTypeHint);
		return request;
	}

}
