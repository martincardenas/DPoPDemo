package sf.frb.org.DPoPDem.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import sf.frb.org.DPoPDem.dataobj.ConfCCReqObj;
import sf.frb.org.DPoPDem.dataobj.DPoPTokenPrep;
import sf.frb.org.DPoPDem.dataobj.TokenBearerunc;
import sf.frb.org.DPoPDem.utils.TokenService;

@RestController

public class DPoPBootController {

	Logger LOG = LoggerFactory.getLogger(LoggingController.class);

	@Autowired
	private TokenService tokensrv;

	String DPoPJWT = null;

	public String getDPoPJWT() {

		return DPoPJWT;
	}

	public void setDPoPJWT(String dPoPJWT) {
		DPoPJWT = dPoPJWT;
	}

	HashMap<String, JSONObject> hm = new HashMap<String, JSONObject>();
	HashMap<String, Object> xm = new HashMap<String, Object>();

	@GetMapping(path = "/beapp")

	public BeappResourceBean beappResourceBean(@RequestHeader Map<String, String> headers,
			@RequestParam(required = false) Map<String, String> qparams) {

		return new BeappResourceBean(headers, qparams);

	}

	@PostMapping(value = "/getToken", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> getBearerToken(@RequestParam("description") String decription,
			@RequestParam(value = "idptarget", required = true) String idptarget,
			@RequestHeader(name = "X-COM-PERSIST", required = true) String headerPersist,
			@RequestHeader(name = "X-COM-LOCATION", defaultValue = "ASIA") String headerLocation,
			@RequestBody ConfCCReqObj ccConfReqObj) throws Exception {

		TokenBearerunc resp = tokensrv.getTokenunc(ccConfReqObj, false);

		xm.put("TokenRequest", ccConfReqObj);
		xm.put("TokenReqResp", resp);

		// Send location in response
		return new ResponseEntity<>(resp, HttpStatus.OK);

	}

	@PostMapping(value = "/getDPoPToken", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> getDPoPToken(@RequestParam("description") String decription,
			@RequestParam(value = "idptarget", required = true) String idptarget,
			@RequestHeader(name = "X-COM-PERSIST", required = true) String headerPersist,
			@RequestHeader(name = "X-COM-LOCATION", defaultValue = "ASIA") String headerLocation,
			@RequestBody ConfCCReqObj ccConfReqObj) throws Exception {

		// this.setDPoPJWT(jwtutilnim.genJWT());

		TokenBearerunc resp = tokensrv.getTokenunc(ccConfReqObj, true);

		LOG.info("Generated DPoP JWT: " + resp.getAccess_token());

		LOG.info("SHA256 Base64 Hash : " + encode(resp.getAccess_token()));

		xm.put("TokenDPoPRequest", ccConfReqObj);
		xm.put("TokenDPoPReqResp", resp);

		// Send location in response
		return new ResponseEntity<>(resp, HttpStatus.OK);

	}

	@PostMapping(value = "/getPRDPoPReq", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> getPRDPoPReq(@RequestParam("description") String decription,
			@RequestParam(value = "idptarget", required = true) String idptarget,
			@RequestHeader(name = "X-COM-PERSIST", required = true) String headerPersist,
			@RequestHeader(name = "X-COM-LOCATION", defaultValue = "ASIA") String headerLocation,
			@RequestHeader(name = "DPoPAccessToken", defaultValue = "xxx") String DPoPAccessToken,
			@RequestBody TokenBearerunc ccConfReqObj) throws Exception {

		// this.setDPoPJWT(jwtutilnim.genJWT());

		DPoPTokenPrep resp = tokensrv.getDPoPAthunc(ccConfReqObj, encode(DPoPAccessToken));

		LOG.info("Generated DPoP Ath JWT: " + resp.getSignedJWT().serialize());

		xm.put("TokenPRDPoPRequest", ccConfReqObj);
		xm.put("TokenPRDPoPReqResp", resp);

		// Send location in response
		return new ResponseEntity<>(resp, HttpStatus.OK);

	}

	public static String encode(final String clearText) throws NoSuchAlgorithmException {
		return new String(
				// Base64.getEncoder().encode(MessageDigest.getInstance("SHA-256").digest(clearText.getBytes(StandardCharsets.UTF_8)))
				Base64.getEncoder().encode(MessageDigest.getInstance("SHA-256").digest(clearText.getBytes())));
	}
	
	
	@PostMapping(value = "/postDPoPCall", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> postDPoPCall(@RequestParam("description") String decription,
			@RequestParam(value = "idptarget", required = true) String idptarget,
			@RequestHeader(name = "X-COM-PERSIST", required = true) String headerPersist,
			@RequestHeader(name = "X-COM-LOCATION", defaultValue = "ASIA") String headerLocation,
			@RequestBody ConfCCReqObj ccConfReqObj) throws Exception {

		// this.setDPoPJWT(jwtutilnim.genJWT());

		TokenBearerunc resp = tokensrv.postDPoPToekE2E(ccConfReqObj, true);

		LOG.info("Generated DPoP JWT: " + resp.getAccess_token());

		LOG.info("SHA256 Base64 Hash : " + encode(resp.getAccess_token()));

		xm.put("TokenDPoPRequest", ccConfReqObj);
		xm.put("TokenDPoPReqResp", resp);

		// Send location in response
		return new ResponseEntity<>(resp, HttpStatus.OK);

	}
	
	
	@GetMapping(path="/prtResourceDPoP")
	
	public PrtResourceBeanDPoPath prtResourceBeanDPoP(@RequestHeader Map<String, String> headers) {
		
		return new PrtResourceBeanDPoPath(headers);
		
	}





}
