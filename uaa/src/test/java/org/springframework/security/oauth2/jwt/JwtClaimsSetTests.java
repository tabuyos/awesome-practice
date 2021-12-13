/*
 * Copyright 2020 the original author or authors.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link JwtClaimsSet}.
 *
 * @author Joe Grandja
 */
public class JwtClaimsSetTests {

	@Test
	public void buildWhenClaimsEmptyThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> JwtClaimsSet.builder().build())
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("claims cannot be empty");
	}

	@Test
	public void buildWhenAllClaimsProvidedThenAllClaimsAreSet() {
		JwtClaimsSet expectedJwtClaimsSet = TestJwtClaimsSets.jwtClaimsSet().build();

		JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
				.issuer(expectedJwtClaimsSet.getIssuer().toExternalForm())
				.subject(expectedJwtClaimsSet.getSubject())
				.audience(expectedJwtClaimsSet.getAudience())
				.issuedAt(expectedJwtClaimsSet.getIssuedAt())
				.notBefore(expectedJwtClaimsSet.getNotBefore())
				.expiresAt(expectedJwtClaimsSet.getExpiresAt())
				.id(expectedJwtClaimsSet.getId())
				.claims(claims -> claims.put("custom-claim-name", "custom-claim-value"))
				.build();

		assertThat(jwtClaimsSet.getIssuer()).isEqualTo(expectedJwtClaimsSet.getIssuer());
		assertThat(jwtClaimsSet.getSubject()).isEqualTo(expectedJwtClaimsSet.getSubject());
		assertThat(jwtClaimsSet.getAudience()).isEqualTo(expectedJwtClaimsSet.getAudience());
		assertThat(jwtClaimsSet.getIssuedAt()).isEqualTo(expectedJwtClaimsSet.getIssuedAt());
		assertThat(jwtClaimsSet.getNotBefore()).isEqualTo(expectedJwtClaimsSet.getNotBefore());
		assertThat(jwtClaimsSet.getExpiresAt()).isEqualTo(expectedJwtClaimsSet.getExpiresAt());
		assertThat(jwtClaimsSet.getId()).isEqualTo(expectedJwtClaimsSet.getId());
		assertThat(jwtClaimsSet.<String>getClaim("custom-claim-name")).isEqualTo("custom-claim-value");
		assertThat(jwtClaimsSet.getClaims()).isEqualTo(expectedJwtClaimsSet.getClaims());
	}

	@Test
	public void fromWhenNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> JwtClaimsSet.from(null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("claims cannot be null");
	}

	@Test
	public void fromWhenClaimsProvidedThenCopied() {
		JwtClaimsSet expectedJwtClaimsSet = TestJwtClaimsSets.jwtClaimsSet().build();
		JwtClaimsSet jwtClaimsSet = JwtClaimsSet.from(expectedJwtClaimsSet).build();
		assertThat(jwtClaimsSet.getClaims()).isEqualTo(expectedJwtClaimsSet.getClaims());
	}

	@Test
	public void claimWhenNameNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> JwtClaimsSet.builder().claim(null, "value"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("name cannot be empty");
	}

	@Test
	public void claimWhenValueNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> JwtClaimsSet.builder().claim("name", null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("value cannot be null");
	}
}
