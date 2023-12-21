package sf.frb.org.DPoPDem.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeappResourceBean {

	Map<String, String> qparams;
	Map<String, String> headers;
	
	


	public Map<String, String> getQparams() {
		return qparams;
	}

	public void setQparams(Map<String, String> qparams) {
		this.qparams = qparams;
	}



	Logger LOG = LoggerFactory.getLogger(LoggingController.class);

	public BeappResourceBean() {
		// TODO Auto-generated constructor stub
	}
	
	public void setHeaders(Map<String, String> hdr) {
		this.headers = hdr;
	}

	
	public Map<String, String> getHeaders() {
		return headers;
	}

	
	
	public BeappResourceBean(Map<String, String> hdr,Map<String,String> qparams) {
		// TODO Auto-generated constructor stub
		
		// TODO Auto-generated constructor stub
		this.setQparams(qparams);
		this.setHeaders(hdr);
		
		
		
		qparams.forEach((key, value) -> {
	        LOG.info(String.format("Qparms '%s' = %s", key, value));
	    });
		
		
		
		
	    headers.forEach((key, value) -> {
	        LOG.info(String.format("Header '%s' = %s", key, value));
	    });

		
		
	}


}
