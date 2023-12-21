package sf.frb.org.DPoPDem.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.dpop.JWKThumbprintConfirmation;
import com.nimbusds.oauth2.sdk.dpop.verifiers.AccessTokenValidationException;
import com.nimbusds.oauth2.sdk.dpop.verifiers.DPoPIssuer;
import com.nimbusds.oauth2.sdk.dpop.verifiers.DPoPProtectedResourceRequestVerifier;
import com.nimbusds.oauth2.sdk.dpop.verifiers.DefaultDPoPSingleUseChecker;
import com.nimbusds.oauth2.sdk.dpop.verifiers.InvalidDPoPProofException;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.JWTID;
import com.nimbusds.oauth2.sdk.token.DPoPAccessToken;
import com.nimbusds.oauth2.sdk.util.singleuse.SingleUseChecker;


public class PrtResourceBeanDPoPath {

	Map<String, String> headers;

	Logger LOG = LoggerFactory.getLogger(LoggingController.class);
	String[] authHeader;
	String dPoPHeader;

	String authType;

	String authDPoPAccessToken;

	String confSig;
	String issuerIdp;
	String crtFingerprint;
	String valStatus;
	String DPoP;

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getDPoP() {
		return DPoP;
	}

	public void setDPoP(String dPoP) {
		DPoP = dPoP;
	}

	Map claimSet;

	public String getValStatus() {
		return valStatus;
	}

	public void setValStatus(String valStatus) {
		this.valStatus = valStatus;
	}

	public String getIssuerIdp() {
		return issuerIdp;
	}

	public void setIssuerIdp(String issuerIdp) {
		this.issuerIdp = issuerIdp;
	}

	public String getConfSig() {
		return confSig;
	}

	public void setConfSig(String confSig) {
		this.confSig = confSig;
	}

	public Map getClaimSet() {
		return claimSet;
	}

	public void setClaimSet(Map claimSet) {
		this.claimSet = claimSet;
	}

	public String getCrtFingerprint() {
		return crtFingerprint;
	}

	public void setCrtFingerprint(String crtFingerprint) {
		this.crtFingerprint = crtFingerprint;
	}

	public PrtResourceBeanDPoPath(Map<String, String> hdr) {
		// TODO Auto-generated constructor stub
		this.setHeaders(hdr);
		this.setVars();
		this.validate();

	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> hdr) {
		this.headers = hdr;
	}

	private void setVars() {

		headers.forEach((key, value) -> {
			LOG.info(String.format("Header '%s' = %s", key, value));
		});

		authHeader = (headers.get("authorization")).split(" ");

		this.setAuthType(authHeader[0]);
		// Get only the accesstoken from the Authorization, skip "DPoP"
		this.setAuthDPoPAccessToken(authHeader[1]);
		this.setDPoP(headers.get("dpop"));

		this.setIssuerIdp(headers.get("issueridp"));

	}

	public String getdPoPHeader() {
		return dPoPHeader;
	}

	public void setdPoPHeader(String dPoPHeader) {
		this.dPoPHeader = dPoPHeader;
	}

	public String getAuthDPoPAccessToken() {
		return authDPoPAccessToken;
	}

	public void setAuthDPoPAccessToken(String authDPoPAccessToken) {
		this.authDPoPAccessToken = authDPoPAccessToken;
	}

	private void validate() {

		// Validate the token
		// TokenSrvs tokSvc = new TokenSrvs(certSig);
		Map tokenMap = null;

		if (issuerIdp.equals("OKTA")) {

			// tokenMap = tokSvc.valtokenOKTA(authToken);
			this.setClaimSet(tokenMap);

		} else {

			// The accepted DPoP proof JWS algorithms
			Set<JWSAlgorithm> acceptedAlgs = new HashSet<>(
					Arrays.asList(JWSAlgorithm.RS256, JWSAlgorithm.PS256, JWSAlgorithm.ES256));

			// The max accepted age of the DPoP proof JWTs
			//long proofMaxAgeSeconds = 60;
			long proofMaxAgeSeconds = 120;

			// DPoP single use checker, caches the DPoP proof JWT jti claims
			long cachePurgeIntervalSeconds = 600;
			SingleUseChecker<Map.Entry<DPoPIssuer, JWTID>> singleUseChecker = new DefaultDPoPSingleUseChecker(
					proofMaxAgeSeconds, cachePurgeIntervalSeconds);

			// Create the DPoP proof and access token binding verifier,
			// the class is thread-safe
			DPoPProtectedResourceRequestVerifier verifier = new DPoPProtectedResourceRequestVerifier(acceptedAlgs,
					proofMaxAgeSeconds, singleUseChecker);

			// Verify some request

			// The HTTP request method and URL
			String httpMethod = "GET";
			URI httpURI = null;

			// The DPoP proof, obtained from the HTTP DPoP header
			SignedJWT dPoPProof = null;
			;
			try {

				httpURI = new URI("https://localhost/prtResourceDPoP");
				dPoPProof = SignedJWT.parse(this.getDPoP());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} /* ... */ catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;

			// The DPoP access token, obtained from the HTTP Authorization header
			DPoPAccessToken accessToken = new DPoPAccessToken(this.getAuthDPoPAccessToken());
			System.out.println("getAuthDPoPAccessToken: "+ accessToken.toJSONString());
			LOG.info(String.format("getAuthDPoPAccessToken: "+ accessToken.toJSONString()));
			// The DPoP proof issuer, typically the client ID obtained from the
			// access token introspection
			DPoPIssuer dPoPIssuer = new DPoPIssuer(new ClientID("icavqper2nygw"));

			// The JWK SHA-256 thumbprint confirmation, obtained from the
			// access token introspection
			JWKThumbprintConfirmation cnf = null/* ... */;

			

			try {
				
				SignedJWT parsedJWT = SignedJWT.parse(this.getAuthDPoPAccessToken());
				JWTClaimsSet jwtClaimsSet = parsedJWT.getJWTClaimsSet();
				cnf = JWKThumbprintConfirmation.parse(jwtClaimsSet);
				
				LOG.info(httpMethod);
				LOG.info(httpURI.getPath());
				LOG.info(dPoPIssuer.getValue());
				LOG.info(dPoPProof.serialize());
				LOG.info(accessToken.getValue());
				LOG.info(cnf.toString());
				
				verifier.verify(httpMethod, httpURI, dPoPIssuer, dPoPProof, accessToken, cnf,null);
				
				this.setValStatus("DPoP Verified :" + httpMethod + ": "+ httpURI.getPath() + ": "+ dPoPIssuer.getValue() + ": cnf :"+ cnf.toString());
				
				
			} catch (InvalidDPoPProofException e) {
				System.err.println("Invalid DPoP proof: " + e.getMessage());
				this.setValStatus("DPoP Failed :" + e.getMessage());
				
			} catch (AccessTokenValidationException e) {
				System.err.println("Invalid access token binding: " + e.getMessage());
				this.setValStatus("DPoP Failed :" + e.getMessage());
			} catch (JOSEException e) {
				System.err.println("JOSEException error: " + e.getMessage());
				this.setValStatus("DPoP Failed :" + e.getMessage());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.err.println("ParseException error: " + e.getMessage());
				this.setValStatus("DPoP Failed :" + e.getMessage());
			}

		}
		// tokenMap = issuerIdp.equals("OKTA")? tokSvc.valtokenOKTA(authToken):
		// tokSvc.valtoken(authToken);

		
	}

}
