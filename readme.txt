https://connect2id.com/products/nimbus-jose-jwt/examples/jwk-thumbprints

DPoP sample end to end.

Sample DPoP proof 1:

{
  "sub": "icavqper2nygw",
  "scp": [
    "frb_abano"
  ],
  "iss": "https://localhost/webapps/c2id",
  "cnf": {
    "jkt": "l-q1xv1n_7gM6C1FHCtBwa8Mti_nU-ZUbmIB5AJ5fRI"
  },
  "exp": 1701970336,
  "iat": 1701969736,
  "jti": "P-Lu_UFuAFY",
  "cid": "icavqper2nygw"
}

Sample DPoP proof 2:

{
  "typ": "dpop+jwt",
  "alg": "RS256",
  "jwk": {
    "kty": "RSA",
    "e": "AQAB",
    "kid": "A",
    "n": "ixxxm0AwWnxRUZeNPnei_Kb840l1oTqqO8xIKkoFYm6F-ADt6sak41QPDQXgR0yLL68EXJGlwF1iaSUMYAbVrYq1E7yU7N8f2nk4eZIFA1xMshVQFgEfRuJnz3zmfbrnf58Lvs2PndA7KEwww2qAscX88JW_IMEGsuiMLQy_uyW8E6u-52STAp7k2X52rcfmdpkBsFg5WHDoiRFABceBMZLQ0kOL1EKD4jFx1WYofDhFB9YT4Ke-SPpRdojv97ho4v2t2CF52SUArx8MQExLFl2NPXc8gRXc9wk63Ne2fyLOMkfuIK8jXxjQS4Bag_fJeFrq3XShKnT53psNDoyd5w"
  }
}

{
  "htm": "GET",
  "htu": "http://localhost:8888/prtResourceDPoP",
  "ath": "ZsjgwfXiss8_uCxiU9lbTH6bQilImEN_z9wr10s4Geg",
  "iat": 1701969736,
  "jti": "BPA9Wzi-0JYWL1Yz"
}

Sequence

	@PostMapping(value = "/postDPoPCall", consumes = "application/json", produces = "application/json")
..
		TokenBearerunc resp = tokensrv.postDPoPToekE2E(ccConfReqObj, true);

		LOG.info("Generated DPoP JWT: " + resp.getAccess_token());
		LOG.info("SHA256 Base64 Hash : " + encode(resp.getAccess_token()));
		xm.put("TokenDPoPRequest", ccConfReqObj);
		xm.put("TokenDPoPReqResp", resp);
		// Send location in response
		return new ResponseEntity<>(resp, HttpStatus.OK);

	}

	
	
		public TokenBearerunc postDPoPToekE2E(@RequestBody ConfCCReqObj confclreq, boolean DPoP) {
		// TODO Auto-generated method stub

		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<String> response = null;

	.
	.
	.
	Call IdP for DPoP access token
	
	Build Request for Protected resource call.
	
			// Prepare Request for the Protected Resource.
.
.

		HTTPRequest reqBody = BuildDPoPProtectedRes(DPoPAccessToken);

		headertreeMap = reqBody.getHeaderMap();
		tkb.setHeadertreeMap(headertreeMap);
		
		response can be used to now call PR.
		
		Note: DPoP proof 2 header public key matches jkt cnf claim, see above. Sec. 7.1 RFC9449
		
		RSAKey rsaKey;
		try {
			rsaKey = RSAKey.parse("{\"kty\": \"RSA\",\"e\": \"AQAB\",\"kid\": \"A\",\"n\": \"ixxxm0AwWnxRUZeNPnei_Kb840l1oTqqO8xIKkoFYm6F-ADt6sak41QPDQXgR0yLL68EXJGlwF1iaSUMYAbVrYq1E7yU7N8f2nk4eZIFA1xMshVQFgEfRuJnz3zmfbrnf58Lvs2PndA7KEwww2qAscX88JW_IMEGsuiMLQy_uyW8E6u-52STAp7k2X52rcfmdpkBsFg5WHDoiRFABceBMZLQ0kOL1EKD4jFx1WYofDhFB9YT4Ke-SPpRdojv97ho4v2t2CF52SUArx8MQExLFl2NPXc8gRXc9wk63Ne2fyLOMkfuIK8jXxjQS4Bag_fJeFrq3XShKnT53psNDoyd5w\"}");
			
			Base64URL thumbprint = rsaKey.computeThumbprint();
			System.out.println(thumbprint.toString());
		
	Output: l-q1xv1n_7gM6C1FHCtBwa8Mti_nU-ZUbmIB5AJ5fRI
	
	
	SAMPLE OUTPUT FROM CONSOLE ON BOTH POST AND GET CALLS:
	
	 DPoP Proof 1 JWT: eyJ0eXAiOiJkcG9wK2p3dCIsImFsZyI6IlJTMjU2IiwiandrIjp7Imt0eSI6IlJTQSIsImUiOiJBUUFCIiwia2lkIjoiQSIsIm4iOiJwTEhkS3ZSb2prS2tlc3lyRloxYkhOcHpjeEZVbFBndndUbDNQUjF0TmwyX2xMVkFpLTNFUEswR3YzTW5abkRMd2F6RkxkNEs0NmR4OVFkSzFlNXZUY0hXLUVfczNsNEFCLVBvVHgyaHA2V0F4aEtNVXFyUXN3VVpwaC1tbWVFeXh4QjhmQkJpNlFuaUN6VzVjXzMtRmt6cFExekpRanZjVzE5VUxGWmN3dTlWdzUxSUJqcDdMeFVsUFQ5UExudU5FdGc3cDF2M1NjWW0zUkh5TjE4Zkp6UTVKbHBuNlhBZGR5VjlDWTR1RWZUd0ZJQWljWWtZanVfMlRNRUdQSnlRZ2t0Smkyc3VzU3lmV19aT1dVQk5WMVlmYUtVYXVDQXBNOTZQbjZiakN1aFR1SmxMUFluaWV2NUJ4dmpDVVZNZVNsaTZ4QWVTREZmQ3R2YW9wbF9YdVEifX0.eyJodG0iOiJQT1NUIiwiaHR1IjoiaHR0cHM6XC9cL2xvY2FsaG9zdFwvd2ViYXBwc1wvYzJpZFwvdG9rZW4iLCJpYXQiOjE3MDI3NzE0NjUsImp0aSI6IktYZ3l3LWdPd1BLQU9lWDMifQ.B-QjA2mBUiTjJ1Bb_eNwKDMCJzDNKwyi_qpl7dO_uaxeOOcmzTfdKp_tQqcvAyjbDFfEjtCe3sBUTzSsmYLtDNRxyXwpaiRatgCq0GHSZa0SSy2aECJQAXvPOkTZEXIGi_hbcCwgG2OLaSdBhU3vMcMRQnsBY8BMn8EOIWPFaRcXeCenfxFBkwl9ot-d2Q3bUWntBFAx7dWbcP9YORCJcgnXvQb5H4SVyci5ZadEkNl4w6z8xYQ5xl2fIpydsPvGWPPfQpvX8IBMnKeXM-JcvmmKtRktzuEhAd3zFP3YUhkQfKUulDqonvzSdJfHUk3se1eoO0g-tzkk4qiEGT-02g
2023-12-16 16:04:25.819  INFO 5078 --- [nio-8443-exec-5] s.f.o.D.controller.LoggingController     : Response from IDP: {"access_token":"eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJpY2F2cXBlcjJueWd3Iiwic2NwIjpbImZyYl9hYmFubyJdLCJpc3MiOiJodHRwczovL2xvY2FsaG9zdC93ZWJhcHBzL2MyaWQiLCJjbmYiOnsiamt0IjoiT1J3OWNMd3JDU1pzM081akFlZUNnRjg4b3duXzFVT2l3Tm43S0xUd2dUZyJ9LCJleHAiOjE3MDI3NzIwNjUsImlhdCI6MTcwMjc3MTQ2NSwianRpIjoid2FhUU1DM1hTWEkiLCJjaWQiOiJpY2F2cXBlcjJueWd3In0.HNxsKFfsbatAQq1zJU4aQdsHBNcH3geXbZ5D6OA1JT3yAocM0NkgBi6jzAwNrNrpbigVtatTlunxfaThnrbV27dvqowwiSPopWFOFcnyaXajuGI_Ay6aa92Jy47QqZD519IH4mDlW4VfexNk_roo_cQ775naX0y29FLtjWl74WnOWEdjckK-wwwzDU1HIDQSEg2gtGe_YruHVRGJOyWt2AUZGyeQZOFQW2f_cwzI-E4Bc0bFpFwFgcYv8r2db73tdxSKEFYb-oY8n3Rn8sE5JQJFcW3wS0cOi-AVtOAdk-O3Pns732-Nn3qL9UF67HslfKPnJk7IjmIUSQhWUql30A","scope":"frb_abano","token_type":"DPoP","expires_in":600}
2023-12-16 16:04:25.819  INFO 5078 --- [nio-8443-exec-5] s.f.o.D.controller.LoggingController     : DPoPAccessToken Response :{"access_token":"eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJpY2F2cXBlcjJueWd3Iiwic2NwIjpbImZyYl9hYmFubyJdLCJpc3MiOiJodHRwczovL2xvY2FsaG9zdC93ZWJhcHBzL2MyaWQiLCJjbmYiOnsiamt0IjoiT1J3OWNMd3JDU1pzM081akFlZUNnRjg4b3duXzFVT2l3Tm43S0xUd2dUZyJ9LCJleHAiOjE3MDI3NzIwNjUsImlhdCI6MTcwMjc3MTQ2NSwianRpIjoid2FhUU1DM1hTWEkiLCJjaWQiOiJpY2F2cXBlcjJueWd3In0.HNxsKFfsbatAQq1zJU4aQdsHBNcH3geXbZ5D6OA1JT3yAocM0NkgBi6jzAwNrNrpbigVtatTlunxfaThnrbV27dvqowwiSPopWFOFcnyaXajuGI_Ay6aa92Jy47QqZD519IH4mDlW4VfexNk_roo_cQ775naX0y29FLtjWl74WnOWEdjckK-wwwzDU1HIDQSEg2gtGe_YruHVRGJOyWt2AUZGyeQZOFQW2f_cwzI-E4Bc0bFpFwFgcYv8r2db73tdxSKEFYb-oY8n3Rn8sE5JQJFcW3wS0cOi-AVtOAdk-O3Pns732-Nn3qL9UF67HslfKPnJk7IjmIUSQhWUql30A","scope":"frb_abano","token_type":"DPoP","expires_in":600}
2023-12-16 16:04:25.831  INFO 5078 --- [nio-8443-exec-5] s.f.o.D.controller.LoggingController     : DPoPAccessToken Cleansed : eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJpY2F2cXBlcjJueWd3Iiwic2NwIjpbImZyYl9hYmFubyJdLCJpc3MiOiJodHRwczovL2xvY2FsaG9zdC93ZWJhcHBzL2MyaWQiLCJjbmYiOnsiamt0IjoiT1J3OWNMd3JDU1pzM081akFlZUNnRjg4b3duXzFVT2l3Tm43S0xUd2dUZyJ9LCJleHAiOjE3MDI3NzIwNjUsImlhdCI6MTcwMjc3MTQ2NSwianRpIjoid2FhUU1DM1hTWEkiLCJjaWQiOiJpY2F2cXBlcjJueWd3In0.HNxsKFfsbatAQq1zJU4aQdsHBNcH3geXbZ5D6OA1JT3yAocM0NkgBi6jzAwNrNrpbigVtatTlunxfaThnrbV27dvqowwiSPopWFOFcnyaXajuGI_Ay6aa92Jy47QqZD519IH4mDlW4VfexNk_roo_cQ775naX0y29FLtjWl74WnOWEdjckK-wwwzDU1HIDQSEg2gtGe_YruHVRGJOyWt2AUZGyeQZOFQW2f_cwzI-E4Bc0bFpFwFgcYv8r2db73tdxSKEFYb-oY8n3Rn8sE5JQJFcW3wS0cOi-AVtOAdk-O3Pns732-Nn3qL9UF67HslfKPnJk7IjmIUSQhWUql30A
2023-12-16 16:04:25.831  INFO 5078 --- [nio-8443-exec-5] s.f.o.D.controller.LoggingController     : 256 ath hash: 7TJjLiluW401ONipgZqQ/oQpuB3r2tvPEpOfb6L9alM=
2023-12-16 16:04:25.836  INFO 5078 --- [nio-8443-exec-5] s.f.o.D.controller.LoggingController     : DPoP proof 2: eyJ0eXAiOiJkcG9wK2p3dCIsImFsZyI6IlJTMjU2IiwiandrIjp7Imt0eSI6IlJTQSIsImUiOiJBUUFCIiwia2lkIjoiQSIsIm4iOiJwTEhkS3ZSb2prS2tlc3lyRloxYkhOcHpjeEZVbFBndndUbDNQUjF0TmwyX2xMVkFpLTNFUEswR3YzTW5abkRMd2F6RkxkNEs0NmR4OVFkSzFlNXZUY0hXLUVfczNsNEFCLVBvVHgyaHA2V0F4aEtNVXFyUXN3VVpwaC1tbWVFeXh4QjhmQkJpNlFuaUN6VzVjXzMtRmt6cFExekpRanZjVzE5VUxGWmN3dTlWdzUxSUJqcDdMeFVsUFQ5UExudU5FdGc3cDF2M1NjWW0zUkh5TjE4Zkp6UTVKbHBuNlhBZGR5VjlDWTR1RWZUd0ZJQWljWWtZanVfMlRNRUdQSnlRZ2t0Smkyc3VzU3lmV19aT1dVQk5WMVlmYUtVYXVDQXBNOTZQbjZiakN1aFR1SmxMUFluaWV2NUJ4dmpDVVZNZVNsaTZ4QWVTREZmQ3R2YW9wbF9YdVEifX0.eyJodG0iOiJHRVQiLCJodHUiOiJodHRwczpcL1wvbG9jYWxob3N0XC9wcnRSZXNvdXJjZURQb1AiLCJhdGgiOiI3VEpqTGlsdVc0MDFPTmlwZ1pxUV9vUXB1QjNyMnR2UEVwT2ZiNkw5YWxNIiwiaWF0IjoxNzAyNzcxNDY1LCJqdGkiOiJyTHMxay1aNDdzMHcxbDRXIn0.Uh7uNJaQGTsNUwGbHy93BTBekH34EU_P8q6PDagL4-3orPn6_toTaKfYqYZ4pd-V92SRhOIcTIL01Sx-moYNCP7P6JfPwHbXuiapnThfoq0dxS5g7TBdwPVvyNRT9gUACc2eNMrjUIANcviYQ-aIKhbSBj2ji0y6GydyL44N4zVDAZGpXzTu1I-8ks_zil5yW-vAu59t9N4bTFNDvADmzPTYnq-2CSOnSHJVUx7jCf6RlTBRR2l3Rjd7wlWFHSBZ1D4JOIDY9fl7eBJZO-_VTgOfdtxMKv6FzZ_YJFFM0giXU3sfyG1eIJUeDgcOKQOO2ZeVFQXKARFiKWCM6hX5xw
2023-12-16 16:04:25.837  INFO 5078 --- [nio-8443-exec-5] s.f.o.D.controller.LoggingController     : Generated DPoP JWT: eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJpY2F2cXBlcjJueWd3Iiwic2NwIjpbImZyYl9hYmFubyJdLCJpc3MiOiJodHRwczovL2xvY2FsaG9zdC93ZWJhcHBzL2MyaWQiLCJjbmYiOnsiamt0IjoiT1J3OWNMd3JDU1pzM081akFlZUNnRjg4b3duXzFVT2l3Tm43S0xUd2dUZyJ9LCJleHAiOjE3MDI3NzIwNjUsImlhdCI6MTcwMjc3MTQ2NSwianRpIjoid2FhUU1DM1hTWEkiLCJjaWQiOiJpY2F2cXBlcjJueWd3In0.HNxsKFfsbatAQq1zJU4aQdsHBNcH3geXbZ5D6OA1JT3yAocM0NkgBi6jzAwNrNrpbigVtatTlunxfaThnrbV27dvqowwiSPopWFOFcnyaXajuGI_Ay6aa92Jy47QqZD519IH4mDlW4VfexNk_roo_cQ775naX0y29FLtjWl74WnOWEdjckK-wwwzDU1HIDQSEg2gtGe_YruHVRGJOyWt2AUZGyeQZOFQW2f_cwzI-E4Bc0bFpFwFgcYv8r2db73tdxSKEFYb-oY8n3Rn8sE5JQJFcW3wS0cOi-AVtOAdk-O3Pns732-Nn3qL9UF67HslfKPnJk7IjmIUSQhWUql30A
2023-12-16 16:04:25.837  INFO 5078 --- [nio-8443-exec-5] s.f.o.D.controller.LoggingController     : SHA256 Base64 Hash : 7TJjLiluW401ONipgZqQ/oQpuB3r2tvPEpOfb6L9alM=
2023-12-16 16:05:25.030  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : Header 'host' = localhost:8443
2023-12-16 16:05:25.030  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : Header 'user-agent' = insomnia/8.4.0
2023-12-16 16:05:25.030  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : Header 'authorization' = DPoP eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJpY2F2cXBlcjJueWd3Iiwic2NwIjpbImZyYl9hYmFubyJdLCJpc3MiOiJodHRwczovL2xvY2FsaG9zdC93ZWJhcHBzL2MyaWQiLCJjbmYiOnsiamt0IjoiT1J3OWNMd3JDU1pzM081akFlZUNnRjg4b3duXzFVT2l3Tm43S0xUd2dUZyJ9LCJleHAiOjE3MDI3NzIwNjUsImlhdCI6MTcwMjc3MTQ2NSwianRpIjoid2FhUU1DM1hTWEkiLCJjaWQiOiJpY2F2cXBlcjJueWd3In0.HNxsKFfsbatAQq1zJU4aQdsHBNcH3geXbZ5D6OA1JT3yAocM0NkgBi6jzAwNrNrpbigVtatTlunxfaThnrbV27dvqowwiSPopWFOFcnyaXajuGI_Ay6aa92Jy47QqZD519IH4mDlW4VfexNk_roo_cQ775naX0y29FLtjWl74WnOWEdjckK-wwwzDU1HIDQSEg2gtGe_YruHVRGJOyWt2AUZGyeQZOFQW2f_cwzI-E4Bc0bFpFwFgcYv8r2db73tdxSKEFYb-oY8n3Rn8sE5JQJFcW3wS0cOi-AVtOAdk-O3Pns732-Nn3qL9UF67HslfKPnJk7IjmIUSQhWUql30A
2023-12-16 16:05:25.030  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : Header 'x-com-persist' = 1
2023-12-16 16:05:25.030  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : Header 'x-com-location' = Asia
2023-12-16 16:05:25.031  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : Header 'dpop' = eyJ0eXAiOiJkcG9wK2p3dCIsImFsZyI6IlJTMjU2IiwiandrIjp7Imt0eSI6IlJTQSIsImUiOiJBUUFCIiwia2lkIjoiQSIsIm4iOiJwTEhkS3ZSb2prS2tlc3lyRloxYkhOcHpjeEZVbFBndndUbDNQUjF0TmwyX2xMVkFpLTNFUEswR3YzTW5abkRMd2F6RkxkNEs0NmR4OVFkSzFlNXZUY0hXLUVfczNsNEFCLVBvVHgyaHA2V0F4aEtNVXFyUXN3VVpwaC1tbWVFeXh4QjhmQkJpNlFuaUN6VzVjXzMtRmt6cFExekpRanZjVzE5VUxGWmN3dTlWdzUxSUJqcDdMeFVsUFQ5UExudU5FdGc3cDF2M1NjWW0zUkh5TjE4Zkp6UTVKbHBuNlhBZGR5VjlDWTR1RWZUd0ZJQWljWWtZanVfMlRNRUdQSnlRZ2t0Smkyc3VzU3lmV19aT1dVQk5WMVlmYUtVYXVDQXBNOTZQbjZiakN1aFR1SmxMUFluaWV2NUJ4dmpDVVZNZVNsaTZ4QWVTREZmQ3R2YW9wbF9YdVEifX0.eyJodG0iOiJHRVQiLCJodHUiOiJodHRwczpcL1wvbG9jYWxob3N0XC9wcnRSZXNvdXJjZURQb1AiLCJhdGgiOiI3VEpqTGlsdVc0MDFPTmlwZ1pxUV9vUXB1QjNyMnR2UEVwT2ZiNkw5YWxNIiwiaWF0IjoxNzAyNzcxNDY1LCJqdGkiOiJyTHMxay1aNDdzMHcxbDRXIn0.Uh7uNJaQGTsNUwGbHy93BTBekH34EU_P8q6PDagL4-3orPn6_toTaKfYqYZ4pd-V92SRhOIcTIL01Sx-moYNCP7P6JfPwHbXuiapnThfoq0dxS5g7TBdwPVvyNRT9gUACc2eNMrjUIANcviYQ-aIKhbSBj2ji0y6GydyL44N4zVDAZGpXzTu1I-8ks_zil5yW-vAu59t9N4bTFNDvADmzPTYnq-2CSOnSHJVUx7jCf6RlTBRR2l3Rjd7wlWFHSBZ1D4JOIDY9fl7eBJZO-_VTgOfdtxMKv6FzZ_YJFFM0giXU3sfyG1eIJUeDgcOKQOO2ZeVFQXKARFiKWCM6hX5xw
2023-12-16 16:05:25.031  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : Header 'issueridp' = C2ID
2023-12-16 16:05:25.031  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : Header 'accept' = */*
getAuthDPoPAccessToken: {"access_token":"eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJpY2F2cXBlcjJueWd3Iiwic2NwIjpbImZyYl9hYmFubyJdLCJpc3MiOiJodHRwczovL2xvY2FsaG9zdC93ZWJhcHBzL2MyaWQiLCJjbmYiOnsiamt0IjoiT1J3OWNMd3JDU1pzM081akFlZUNnRjg4b3duXzFVT2l3Tm43S0xUd2dUZyJ9LCJleHAiOjE3MDI3NzIwNjUsImlhdCI6MTcwMjc3MTQ2NSwianRpIjoid2FhUU1DM1hTWEkiLCJjaWQiOiJpY2F2cXBlcjJueWd3In0.HNxsKFfsbatAQq1zJU4aQdsHBNcH3geXbZ5D6OA1JT3yAocM0NkgBi6jzAwNrNrpbigVtatTlunxfaThnrbV27dvqowwiSPopWFOFcnyaXajuGI_Ay6aa92Jy47QqZD519IH4mDlW4VfexNk_roo_cQ775naX0y29FLtjWl74WnOWEdjckK-wwwzDU1HIDQSEg2gtGe_YruHVRGJOyWt2AUZGyeQZOFQW2f_cwzI-E4Bc0bFpFwFgcYv8r2db73tdxSKEFYb-oY8n3Rn8sE5JQJFcW3wS0cOi-AVtOAdk-O3Pns732-Nn3qL9UF67HslfKPnJk7IjmIUSQhWUql30A","token_type":"DPoP"}
2023-12-16 16:05:25.050  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : getAuthDPoPAccessToken: {"access_token":"eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJpY2F2cXBlcjJueWd3Iiwic2NwIjpbImZyYl9hYmFubyJdLCJpc3MiOiJodHRwczovL2xvY2FsaG9zdC93ZWJhcHBzL2MyaWQiLCJjbmYiOnsiamt0IjoiT1J3OWNMd3JDU1pzM081akFlZUNnRjg4b3duXzFVT2l3Tm43S0xUd2dUZyJ9LCJleHAiOjE3MDI3NzIwNjUsImlhdCI6MTcwMjc3MTQ2NSwianRpIjoid2FhUU1DM1hTWEkiLCJjaWQiOiJpY2F2cXBlcjJueWd3In0.HNxsKFfsbatAQq1zJU4aQdsHBNcH3geXbZ5D6OA1JT3yAocM0NkgBi6jzAwNrNrpbigVtatTlunxfaThnrbV27dvqowwiSPopWFOFcnyaXajuGI_Ay6aa92Jy47QqZD519IH4mDlW4VfexNk_roo_cQ775naX0y29FLtjWl74WnOWEdjckK-wwwzDU1HIDQSEg2gtGe_YruHVRGJOyWt2AUZGyeQZOFQW2f_cwzI-E4Bc0bFpFwFgcYv8r2db73tdxSKEFYb-oY8n3Rn8sE5JQJFcW3wS0cOi-AVtOAdk-O3Pns732-Nn3qL9UF67HslfKPnJk7IjmIUSQhWUql30A","token_type":"DPoP"}
2023-12-16 16:05:25.052  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : GET
2023-12-16 16:05:25.052  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : /prtResourceDPoP
2023-12-16 16:05:25.053  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : icavqper2nygw
2023-12-16 16:05:25.053  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : eyJ0eXAiOiJkcG9wK2p3dCIsImFsZyI6IlJTMjU2IiwiandrIjp7Imt0eSI6IlJTQSIsImUiOiJBUUFCIiwia2lkIjoiQSIsIm4iOiJwTEhkS3ZSb2prS2tlc3lyRloxYkhOcHpjeEZVbFBndndUbDNQUjF0TmwyX2xMVkFpLTNFUEswR3YzTW5abkRMd2F6RkxkNEs0NmR4OVFkSzFlNXZUY0hXLUVfczNsNEFCLVBvVHgyaHA2V0F4aEtNVXFyUXN3VVpwaC1tbWVFeXh4QjhmQkJpNlFuaUN6VzVjXzMtRmt6cFExekpRanZjVzE5VUxGWmN3dTlWdzUxSUJqcDdMeFVsUFQ5UExudU5FdGc3cDF2M1NjWW0zUkh5TjE4Zkp6UTVKbHBuNlhBZGR5VjlDWTR1RWZUd0ZJQWljWWtZanVfMlRNRUdQSnlRZ2t0Smkyc3VzU3lmV19aT1dVQk5WMVlmYUtVYXVDQXBNOTZQbjZiakN1aFR1SmxMUFluaWV2NUJ4dmpDVVZNZVNsaTZ4QWVTREZmQ3R2YW9wbF9YdVEifX0.eyJodG0iOiJHRVQiLCJodHUiOiJodHRwczpcL1wvbG9jYWxob3N0XC9wcnRSZXNvdXJjZURQb1AiLCJhdGgiOiI3VEpqTGlsdVc0MDFPTmlwZ1pxUV9vUXB1QjNyMnR2UEVwT2ZiNkw5YWxNIiwiaWF0IjoxNzAyNzcxNDY1LCJqdGkiOiJyTHMxay1aNDdzMHcxbDRXIn0.Uh7uNJaQGTsNUwGbHy93BTBekH34EU_P8q6PDagL4-3orPn6_toTaKfYqYZ4pd-V92SRhOIcTIL01Sx-moYNCP7P6JfPwHbXuiapnThfoq0dxS5g7TBdwPVvyNRT9gUACc2eNMrjUIANcviYQ-aIKhbSBj2ji0y6GydyL44N4zVDAZGpXzTu1I-8ks_zil5yW-vAu59t9N4bTFNDvADmzPTYnq-2CSOnSHJVUx7jCf6RlTBRR2l3Rjd7wlWFHSBZ1D4JOIDY9fl7eBJZO-_VTgOfdtxMKv6FzZ_YJFFM0giXU3sfyG1eIJUeDgcOKQOO2ZeVFQXKARFiKWCM6hX5xw
2023-12-16 16:05:25.053  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJpY2F2cXBlcjJueWd3Iiwic2NwIjpbImZyYl9hYmFubyJdLCJpc3MiOiJodHRwczovL2xvY2FsaG9zdC93ZWJhcHBzL2MyaWQiLCJjbmYiOnsiamt0IjoiT1J3OWNMd3JDU1pzM081akFlZUNnRjg4b3duXzFVT2l3Tm43S0xUd2dUZyJ9LCJleHAiOjE3MDI3NzIwNjUsImlhdCI6MTcwMjc3MTQ2NSwianRpIjoid2FhUU1DM1hTWEkiLCJjaWQiOiJpY2F2cXBlcjJueWd3In0.HNxsKFfsbatAQq1zJU4aQdsHBNcH3geXbZ5D6OA1JT3yAocM0NkgBi6jzAwNrNrpbigVtatTlunxfaThnrbV27dvqowwiSPopWFOFcnyaXajuGI_Ay6aa92Jy47QqZD519IH4mDlW4VfexNk_roo_cQ775naX0y29FLtjWl74WnOWEdjckK-wwwzDU1HIDQSEg2gtGe_YruHVRGJOyWt2AUZGyeQZOFQW2f_cwzI-E4Bc0bFpFwFgcYv8r2db73tdxSKEFYb-oY8n3Rn8sE5JQJFcW3wS0cOi-AVtOAdk-O3Pns732-Nn3qL9UF67HslfKPnJk7IjmIUSQhWUql30A
2023-12-16 16:05:25.053  INFO 5078 --- [nio-8443-exec-6] s.f.o.D.controller.LoggingController     : {"cnf":{"jkt":"ORw9cLwrCSZs3O5jAeeCgF88own_1UOiwNn7KLTwgTg"}}