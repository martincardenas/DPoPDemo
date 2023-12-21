package sf.frb.org.DPoPDem;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64URL;

public class JWKComp {

	public JWKComp() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RSAKey rsaKey;
		try {
			rsaKey = RSAKey.parse("{\"kty\": \"RSA\",\"e\": \"AQAB\",\"kid\": \"A\",\"n\": \"pLHdKvRojkKkesyrFZ1bHNpzcxFUlPgvwTl3PR1tNl2_lLVAi-3EPK0Gv3MnZnDLwazFLd4K46dx9QdK1e5vTcHW-E_s3l4AB-PoTx2hp6WAxhKMUqrQswUZph-mmeEyxxB8fBBi6QniCzW5c_3-FkzpQ1zJQjvcW19ULFZcwu9Vw51IBjp7LxUlPT9PLnuNEtg7p1v3ScYm3RHyN18fJzQ5Jlpn6XAddyV9CY4uEfTwFIAicYkYju_2TMEGPJyQgktJi2susSyfW_ZOWUBNV1YfaKUauCApM96Pn6bjCuhTuJlLPYniev5BxvjCUVMeSli6xAeSDFfCtvaopl_XuQ\"}");
			
			Base64URL thumbprint = rsaKey.computeThumbprint();
			System.out.println(thumbprint.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
