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
package org.springframework.security.oauth2.server.authorization.config;

import java.time.Duration;

import org.junit.Test;

import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link TokenSettings}.
 *
 * @author Joe Grandja
 */
public class TokenSettingsTests {

	@Test
	public void buildWhenDefaultThenDefaultsAreSet() {
		TokenSettings tokenSettings = TokenSettings.builder().build();
		assertThat(tokenSettings.getSettings()).hasSize(4);
		assertThat(tokenSettings.getAccessTokenTimeToLive()).isEqualTo(Duration.ofMinutes(5));
		assertThat(tokenSettings.isReuseRefreshTokens()).isTrue();
		assertThat(tokenSettings.getRefreshTokenTimeToLive()).isEqualTo(Duration.ofMinutes(60));
		assertThat(tokenSettings.getIdTokenSignatureAlgorithm()).isEqualTo(SignatureAlgorithm.RS256);
	}

	@Test
	public void accessTokenTimeToLiveWhenProvidedThenSet() {
		Duration accessTokenTimeToLive = Duration.ofMinutes(10);
		TokenSettings tokenSettings = TokenSettings.builder()
				.accessTokenTimeToLive(accessTokenTimeToLive)
				.build();
		assertThat(tokenSettings.getAccessTokenTimeToLive()).isEqualTo(accessTokenTimeToLive);
	}

	@Test
	public void accessTokenTimeToLiveWhenNullOrZeroOrNegativeThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> TokenSettings.builder().accessTokenTimeToLive(null))
				.isInstanceOf(IllegalArgumentException.class)
				.extracting(Throwable::getMessage)
				.isEqualTo("accessTokenTimeToLive cannot be null");

		assertThatThrownBy(() -> TokenSettings.builder().accessTokenTimeToLive(Duration.ZERO))
				.isInstanceOf(IllegalArgumentException.class)
				.extracting(Throwable::getMessage)
				.isEqualTo("accessTokenTimeToLive must be greater than Duration.ZERO");

		assertThatThrownBy(() -> TokenSettings.builder().accessTokenTimeToLive(Duration.ofSeconds(-10)))
				.isInstanceOf(IllegalArgumentException.class)
				.extracting(Throwable::getMessage)
				.isEqualTo("accessTokenTimeToLive must be greater than Duration.ZERO");
	}

	@Test
	public void reuseRefreshTokensWhenFalseThenSet() {
		TokenSettings tokenSettings = TokenSettings.builder()
				.reuseRefreshTokens(false)
				.build();
		assertThat(tokenSettings.isReuseRefreshTokens()).isFalse();
	}

	@Test
	public void refreshTokenTimeToLiveWhenProvidedThenSet() {
		Duration refreshTokenTimeToLive = Duration.ofDays(10);
		TokenSettings tokenSettings = TokenSettings.builder()
				.refreshTokenTimeToLive(refreshTokenTimeToLive)
				.build();
		assertThat(tokenSettings.getRefreshTokenTimeToLive()).isEqualTo(refreshTokenTimeToLive);
	}

	@Test
	public void refreshTokenTimeToLiveWhenNullOrZeroOrNegativeThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> TokenSettings.builder().refreshTokenTimeToLive(null))
				.isInstanceOf(IllegalArgumentException.class)
				.extracting(Throwable::getMessage)
				.isEqualTo("refreshTokenTimeToLive cannot be null");

		assertThatThrownBy(() -> TokenSettings.builder().refreshTokenTimeToLive(Duration.ZERO))
				.isInstanceOf(IllegalArgumentException.class)
				.extracting(Throwable::getMessage)
				.isEqualTo("refreshTokenTimeToLive must be greater than Duration.ZERO");

		assertThatThrownBy(() -> TokenSettings.builder().refreshTokenTimeToLive(Duration.ofSeconds(-10)))
				.isInstanceOf(IllegalArgumentException.class)
				.extracting(Throwable::getMessage)
				.isEqualTo("refreshTokenTimeToLive must be greater than Duration.ZERO");
	}

	@Test
	public void idTokenSignatureAlgorithmWhenProvidedThenSet() {
		SignatureAlgorithm idTokenSignatureAlgorithm = SignatureAlgorithm.RS512;
		TokenSettings tokenSettings = TokenSettings.builder()
				.idTokenSignatureAlgorithm(idTokenSignatureAlgorithm)
				.build();
		assertThat(tokenSettings.getIdTokenSignatureAlgorithm()).isEqualTo(idTokenSignatureAlgorithm);
	}

	@Test
	public void settingWhenCustomThenSet() {
		TokenSettings tokenSettings = TokenSettings.builder()
				.setting("name1", "value1")
				.settings(settings -> settings.put("name2", "value2"))
				.build();
		assertThat(tokenSettings.getSettings()).hasSize(6);
		assertThat(tokenSettings.<String>getSetting("name1")).isEqualTo("value1");
		assertThat(tokenSettings.<String>getSetting("name2")).isEqualTo("value2");
	}

}
