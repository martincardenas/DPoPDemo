package sf.frb.org.DPoPDem.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.dpop.DPoPProofFactory;
import com.nimbusds.oauth2.sdk.dpop.DefaultDPoPProofFactory;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.AccessTokenType;
import com.nimbusds.oauth2.sdk.token.DPoPAccessToken;
import com.nimbusds.oauth2.sdk.token.Tokens;

import sf.frb.org.DPoPDem.controller.LoggingController;
import sf.frb.org.DPoPDem.dataobj.ConfCCReqObj;
import sf.frb.org.DPoPDem.dataobj.DPoPTokenPrep;
import sf.frb.org.DPoPDem.dataobj.TokenBearerunc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component

public class TokenService implements ITokenService {

	Logger LOG = LoggerFactory.getLogger(LoggingController.class);

	@Value("${idp.server.url}")
	private String idpservurl;

	@Value("${idp.authllt}")
	private String idpllt;

	public TokenService() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JWTGenUtilNim jtutilnim;

	public RSAKey jwk = null;

	public RSAKey getJwk() {
		return jwk;
	}

	public void setJwk(RSAKey jwk) {
		this.jwk = jwk;
	}

	@Override
	public TokenBearerunc getTokenunc(@RequestBody ConfCCReqObj confclreq, boolean DPoP) {
		// TODO Auto-generated method stub

		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<String> response = null;

		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String DPoPProof = null;

		if (DPoP) {

			RSAKey jwk;
			try {
				jwk = new RSAKeyGenerator(2048).keyID("A").generate();

				// Create a DPoP proof factory for the EC key
				DPoPProofFactory proofFactory = new DefaultDPoPProofFactory(jwk, JWSAlgorithm.RS256);

				DPoPProof = jtutilnim.genJWTDPoP(proofFactory, confclreq);
				this.setJwk(jwk);
				headers.add("DPoP", DPoPProof);
				LOG.info("DPoP Proof 1 JWT: " + DPoPProof);

			} catch (JOSEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*
		 * 
		 * 
		 * httpRequest.setDPoP(proof);
		 * 
		 * // Send the token request to the OAuth 2.0 server HTTPResponse httpResponse =
		 * httpRequest.send();
		 * 
		 * 
		 * TokenResponse tokenResponse = TokenResponse.parse(httpResponse);
		 * 
		 * if (! tokenResponse.indicatesSuccess()) { // The token request failed
		 * ErrorObject error = tokenResponse.toErrorResponse().getErrorObject();
		 * System.err.println(error.getHTTPStatusCode());
		 * System.err.println(error.getCode()); return ""; }
		 * 
		 * Tokens tokens = tokenResponse.toSuccessResponse().getTokens();
		 * DPoPAccessToken dPoPAccessToken = tokens.getDPoPAccessToken();
		 * 
		 * return dPoPAccessToken.getValue();
		 * 
		 * 
		 */
		String access_token_url = idpservurl;

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

		map.add("client_id", confclreq.getClient_id());
		map.add("client_secret", confclreq.getClient_secret());
		map.add("grant_type", confclreq.getgrant_type());
		map.add("scope", confclreq.getScope());

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
		response = restTemplate.exchange(access_token_url, HttpMethod.POST, entity, String.class);

		LOG.info("Response from IDP: " + response.getBody());

		String DPoPAccessToken = response.getBody();

		ObjectMapper mapper = new ObjectMapper();

		TokenBearerunc tkb = null;
		try {
			tkb = mapper.readValue(response.getBody(), TokenBearerunc.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * tkb.setAccess_token(
		 * "eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJtZDcyYmtlNHBtbHJpIiwic2NwIjpbImZyYl9hYmFubyJdLCJpc3MiOiJodHRwOlwvXC9lYzItc2YuZnJiLm9yZ1wvYzJpZCIsImNuZiI6eyJ4NXQjUzI1NiI6Im9vanlobi1fOF9mMTcxT2FCMy1QQkEzZnlxWm00M0drMDVtWUtfbWI5aU0ifSwiZXhwIjoxNjM1NjE1MzczLCJpYXQiOjE2MzU2MTQ3NzMsImp0aSI6IkhNM0hoZVBKa1Q0IiwiY2lkIjoibWQ3MmJrZTRwbWxyaSJ9.ReLCzcsGBlGxxMN4RUYngraPQxdci9bavLt4nUtg3u6awtc1kUwxgLqyKqjLaPD2RQv49fKnRvhTvVC5xAU4sgQHPhVgbntBk4ojTR3AiC-o83FJk7jCOhN5d2wRn2hXZ37UIxfu1zBSYilU3PKndc1jBgDT8rAMY_ejiqOy9xVeCa2STkxm1_iLC95fwtLySw6mfmGUcb6fMHCmn18df7siu8WWudBYA7Vq77SR3iz3jr7twR2oM7NWnaumy5eZmO7nslST1yNy7sVzrDE4Lh-iCUYclwM1giXd80CLrztvKd8iANKK7tQlpNHVLuYpBPq-Egzq0eI-D2LIs4zVrw"
		 * ); tkb.setExpires_in(600); tkb.setScope("frb_abano");
		 * tkb.setToken_type("Bearer");
		 */
		return tkb;
	}

	public HTTPRequest BuildDPoPProtectedRes(String DPoPAccessToken) {

		DPoPAccessToken dPoPAccessToken = new DPoPAccessToken(DPoPAccessToken);

		// Access some DPoP aware resource with the token
		HTTPRequest httpRequest = null;
		Instant now = Instant.now();

		try {
			httpRequest = new HTTPRequest(HTTPRequest.Method.GET, new URL("https://localhost/prtResourceDPoP"));

			httpRequest.setAuthorization(dPoPAccessToken.toAuthorizationHeader());

			DPoPProofFactory proofFactory = new DefaultDPoPProofFactory(this.getJwk(), JWSAlgorithm.RS256);

			// Generate a new DPoP proof for the resource request
			SignedJWT proof = proofFactory.createDPoPJWT(httpRequest.getMethod().name(), httpRequest.getURI(),
					dPoPAccessToken);

			httpRequest.setDPoP(proof);
			LOG.info("DPoP proof 2: " + proof.serialize());

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return httpRequest;
	}

	@Override
	public DPoPTokenPrep getDPoPAthunc(@RequestBody TokenBearerunc dPoPIDPResp, String ath) {
		// TODO Auto-generated method stub

		DPoPTokenPrep dPoP = new DPoPTokenPrep();

		dPoP.setTkDPoPuc(dPoPIDPResp);
		SignedJWT signJWT = jtutilnim.genJWTDPoPAth(ath);

		dPoP.setSignedJWT(signJWT);
		dPoP.setSignedJWTSerialized(signJWT.serialize());

		/*
		 * tkb.setAccess_token(
		 * "eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJtZDcyYmtlNHBtbHJpIiwic2NwIjpbImZyYl9hYmFubyJdLCJpc3MiOiJodHRwOlwvXC9lYzItc2YuZnJiLm9yZ1wvYzJpZCIsImNuZiI6eyJ4NXQjUzI1NiI6Im9vanlobi1fOF9mMTcxT2FCMy1QQkEzZnlxWm00M0drMDVtWUtfbWI5aU0ifSwiZXhwIjoxNjM1NjE1MzczLCJpYXQiOjE2MzU2MTQ3NzMsImp0aSI6IkhNM0hoZVBKa1Q0IiwiY2lkIjoibWQ3MmJrZTRwbWxyaSJ9.ReLCzcsGBlGxxMN4RUYngraPQxdci9bavLt4nUtg3u6awtc1kUwxgLqyKqjLaPD2RQv49fKnRvhTvVC5xAU4sgQHPhVgbntBk4ojTR3AiC-o83FJk7jCOhN5d2wRn2hXZ37UIxfu1zBSYilU3PKndc1jBgDT8rAMY_ejiqOy9xVeCa2STkxm1_iLC95fwtLySw6mfmGUcb6fMHCmn18df7siu8WWudBYA7Vq77SR3iz3jr7twR2oM7NWnaumy5eZmO7nslST1yNy7sVzrDE4Lh-iCUYclwM1giXd80CLrztvKd8iANKK7tQlpNHVLuYpBPq-Egzq0eI-D2LIs4zVrw"
		 * ); tkb.setExpires_in(600); tkb.setScope("frb_abano");
		 * tkb.setToken_type("Bearer");
		 */
		return dPoP;
	}

	@Override
	public TokenBearerunc postDPoPToekE2E(@RequestBody ConfCCReqObj confclreq, boolean DPoP) {
		// TODO Auto-generated method stub

		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<String> response = null;

		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String DPoPProof = null;

		if (DPoP) {

			try {
				jwk = new RSAKeyGenerator(2048).keyID("A").generate();

				// Create a DPoP proof factory for the EC key
				DPoPProofFactory proofFactory = new DefaultDPoPProofFactory(jwk, JWSAlgorithm.RS256);
				// jwk.toKeyPair().getPrivate();
				this.setJwk(jwk);
				DPoPProof = jtutilnim.genJWTDPoP(proofFactory, confclreq);

				this.setJwk(jwk);

				headers.add("DPoP", DPoPProof);
				LOG.info("DPoP Proof 1 JWT: " + DPoPProof);

			} catch (JOSEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*
		 * 
		 * 
		 * httpRequest.setDPoP(proof);
		 * 
		 * // Send the token request to the OAuth 2.0 server HTTPResponse httpResponse =
		 * httpRequest.send();
		 * 
		 * 
		 * TokenResponse tokenResponse = TokenResponse.parse(httpResponse);
		 * 
		 * if (! tokenResponse.indicatesSuccess()) { // The token request failed
		 * ErrorObject error = tokenResponse.toErrorResponse().getErrorObject();
		 * System.err.println(error.getHTTPStatusCode());
		 * System.err.println(error.getCode()); return ""; }
		 * 
		 * Tokens tokens = tokenResponse.toSuccessResponse().getTokens();
		 * DPoPAccessToken dPoPAccessToken = tokens.getDPoPAccessToken();
		 * 
		 * return dPoPAccessToken.getValue();
		 * 
		 * 
		 */
		String access_token_url = idpservurl;

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

		map.add("client_id", confclreq.getClient_id());
		map.add("client_secret", confclreq.getClient_secret());
		map.add("grant_type", confclreq.getgrant_type());
		map.add("scope", confclreq.getScope());

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
		response = restTemplate.exchange(access_token_url, HttpMethod.POST, entity, String.class);

		LOG.info("Response from IDP: " + response.getBody());

		String DPoPAccessToken = response.getBody();

		MultiValueMap<String, String> header = new LinkedMultiValueMap<>();

		ObjectMapper mapper = new ObjectMapper();

		LOG.info("DPoPAccessToken Response :" + DPoPAccessToken);
		TokenBearerunc tkb = null;
		Map<String, List<String>> headertreeMap = new TreeMap<>();

		// header = (MultiValueMap<String, String>) reqBody.getHeaderMap();

		String ath256Hash = null;
		try {
			tkb = mapper.readValue(DPoPAccessToken, TokenBearerunc.class);
			DPoPAccessToken = tkb.getAccess_token();
			LOG.info("DPoPAccessToken Cleansed : " + DPoPAccessToken);
			// get SHS256 hash of the DPoPAccessToken

			ath256Hash = encode(DPoPAccessToken);
			LOG.info("256 ath hash: " + ath256Hash);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get hash of the access token for the 'ath' claim.

		// Prepare Request for the Protected Resource.

		HTTPRequest reqBody = BuildDPoPProtectedRes(DPoPAccessToken);

		headertreeMap = reqBody.getHeaderMap();
		tkb.setHeadertreeMap(headertreeMap);

//		SignedJWT signJWT = jtutilnim.genJWTDPoPAthB(this.getJwk(), ath256Hash);

		// LOG.info("DPoP Proof 2A: " + signJWT.serialize());

		/*
		 * tkb.setAccess_token(
		 * "eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJtZDcyYmtlNHBtbHJpIiwic2NwIjpbImZyYl9hYmFubyJdLCJpc3MiOiJodHRwOlwvXC9lYzItc2YuZnJiLm9yZ1wvYzJpZCIsImNuZiI6eyJ4NXQjUzI1NiI6Im9vanlobi1fOF9mMTcxT2FCMy1QQkEzZnlxWm00M0drMDVtWUtfbWI5aU0ifSwiZXhwIjoxNjM1NjE1MzczLCJpYXQiOjE2MzU2MTQ3NzMsImp0aSI6IkhNM0hoZVBKa1Q0IiwiY2lkIjoibWQ3MmJrZTRwbWxyaSJ9.ReLCzcsGBlGxxMN4RUYngraPQxdci9bavLt4nUtg3u6awtc1kUwxgLqyKqjLaPD2RQv49fKnRvhTvVC5xAU4sgQHPhVgbntBk4ojTR3AiC-o83FJk7jCOhN5d2wRn2hXZ37UIxfu1zBSYilU3PKndc1jBgDT8rAMY_ejiqOy9xVeCa2STkxm1_iLC95fwtLySw6mfmGUcb6fMHCmn18df7siu8WWudBYA7Vq77SR3iz3jr7twR2oM7NWnaumy5eZmO7nslST1yNy7sVzrDE4Lh-iCUYclwM1giXd80CLrztvKd8iANKK7tQlpNHVLuYpBPq-Egzq0eI-D2LIs4zVrw"
		 * ); tkb.setExpires_in(600); tkb.setScope("frb_abano");
		 * tkb.setToken_type("Bearer");
		 */
		return tkb;
	}

	public static String encode(final String clearText) throws NoSuchAlgorithmException {
		return new String(
				// Base64.getEncoder().encode(MessageDigest.getInstance("SHA-256").digest(clearText.getBytes(StandardCharsets.UTF_8)))
				Base64.getEncoder().encode(MessageDigest.getInstance("SHA-256").digest(clearText.getBytes())));
	}

}
