package sf.frb.org.DPoPDem.utils;

import sf.frb.org.DPoPDem.dataobj.ConfCCReqObj;
import sf.frb.org.DPoPDem.dataobj.DPoPTokenPrep;
import sf.frb.org.DPoPDem.dataobj.TokenBearerunc;

public interface ITokenService {
	
	public TokenBearerunc getTokenunc(ConfCCReqObj confclreq, boolean DPoP);

	public DPoPTokenPrep getDPoPAthunc(TokenBearerunc confclreq, String ath);

	TokenBearerunc postDPoPToekE2E(ConfCCReqObj confclreq, boolean DPoP);

}
