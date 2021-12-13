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
package org.springframework.security.oauth2.jwt;

import org.junit.Test;

import org.springframework.security.oauth2.jose.JwaAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link JoseHeader}.
 *
 * @author Joe Grandja
 */
public class JoseHeaderTests {

	@Test
	public void withAlgorithmWhenNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> JoseHeader.withAlgorithm(null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("jwaAlgorithm cannot be null");
	}

	@Test
	public void buildWhenAllHeadersProvidedThenAllHeadersAreSet() {
		JoseHeader expectedJoseHeader = TestJoseHeaders.joseHeader().build();

		JoseHeader joseHeader = JoseHeader.withAlgorithm(expectedJoseHeader.getAlgorithm())
				.jwkSetUrl(expectedJoseHeader.getJwkSetUrl().toExternalForm())
				.jwk(expectedJoseHeader.getJwk())
				.keyId(expectedJoseHeader.getKeyId())
				.x509Url(expectedJoseHeader.getX509Url().toExternalForm())
				.x509CertificateChain(expectedJoseHeader.getX509CertificateChain())
				.x509SHA1Thumbprint(expectedJoseHeader.getX509SHA1Thumbprint())
				.x509SHA256Thumbprint(expectedJoseHeader.getX509SHA256Thumbprint())
				.type(expectedJoseHeader.getType())
				.contentType(expectedJoseHeader.getContentType())
				.headers(headers -> headers.put("custom-header-name", "custom-header-value"))
				.build();

		assertThat(joseHeader.<JwaAlgorithm>getAlgorithm()).isEqualTo(expectedJoseHeader.getAlgorithm());
		assertThat(joseHeader.getJwkSetUrl()).isEqualTo(expectedJoseHeader.getJwkSetUrl());
		assertThat(joseHeader.getJwk()).isEqualTo(expectedJoseHeader.getJwk());
		assertThat(joseHeader.getKeyId()).isEqualTo(expectedJoseHeader.getKeyId());
		assertThat(joseHeader.getX509Url()).isEqualTo(expectedJoseHeader.getX509Url());
		assertThat(joseHeader.getX509CertificateChain()).isEqualTo(expectedJoseHeader.getX509CertificateChain());
		assertThat(joseHeader.getX509SHA1Thumbprint()).isEqualTo(expectedJoseHeader.getX509SHA1Thumbprint());
		assertThat(joseHeader.getX509SHA256Thumbprint()).isEqualTo(expectedJoseHeader.getX509SHA256Thumbprint());
		assertThat(joseHeader.getCritical()).isEqualTo(expectedJoseHeader.getCritical());
		assertThat(joseHeader.getType()).isEqualTo(expectedJoseHeader.getType());
		assertThat(joseHeader.getContentType()).isEqualTo(expectedJoseHeader.getContentType());
		assertThat(joseHeader.<String>getHeader("custom-header-name")).isEqualTo("custom-header-value");
		assertThat(joseHeader.getHeaders()).isEqualTo(expectedJoseHeader.getHeaders());
	}

	@Test
	public void fromWhenNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> JoseHeader.from(null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("headers cannot be null");
	}

	@Test
	public void fromWhenHeadersProvidedThenCopied() {
		JoseHeader expectedJoseHeader = TestJoseHeaders.joseHeader().build();
		JoseHeader joseHeader = JoseHeader.from(expectedJoseHeader).build();
		assertThat(joseHeader.getHeaders()).isEqualTo(expectedJoseHeader.getHeaders());
	}

	@Test
	public void headerWhenNameNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> JoseHeader.withAlgorithm(SignatureAlgorithm.RS256).header(null, "value"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("name cannot be empty");
	}

	@Test
	public void headerWhenValueNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> JoseHeader.withAlgorithm(SignatureAlgorithm.RS256).header("name", null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("value cannot be null");
	}

	@Test
	public void getHeaderWhenNullThenThrowIllegalArgumentException() {
		JoseHeader joseHeader = TestJoseHeaders.joseHeader().build();

		assertThatThrownBy(() -> joseHeader.getHeader(null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("name cannot be empty");
	}
}
