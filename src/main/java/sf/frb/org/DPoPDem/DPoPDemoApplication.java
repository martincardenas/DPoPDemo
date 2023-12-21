package sf.frb.org.DPoPDem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication
public class DPoPDemoApplication {
	
	@Value("${server.ssl.trust-store-password}")
	private String trustStorePassword;
	@Value("${server.ssl.trust-store}")
	private Resource trustStore;
	@Value("${server.ssl.key-store-password}")
	private String keyStorePassword;
	@Value("${server.ssl.key-password}")
	private String keyPassword;

	@Value("classpath:/sf/frb/org/DPoPDem/conf/server-nonprod.jks")
	Resource resourceFile;

	Logger logger = LoggerFactory.getLogger(DPoPDemoApplication.class);
	
	public static final int DEFAULT_BUFFER_SIZE = 8192;


	
	
	public static void main(String[] args) {

		ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(DPoPDemoApplication.class)
				.properties("spring.config.name:application,claims",
						"spring.config.location:classpath:/,classpath:/sf/frb/org/DPoPDem/conf/")
				.build().run(args);

		ConfigurableEnvironment environment = applicationContext.getEnvironment();

		// SpringApplication.run(JwedemApplication.class, args);
	}
// specific RestTemplate method to use a specific Key and Trust properties and to NOT perform host alidaion.

	@Bean
	public RestTemplate getRestTemplateClientAuthentication() throws IOException, UnrecoverableKeyException,
			CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		final String allPassword = keyStorePassword;

		//logger.info("The File : " + resourceFile.getFile());
		
		//ClassPathResource classPathResource  = new ClassPathResource("server-nonprod.jks");
		File file = null;
	
		
		
		try (InputStream inputStream = resourceFile.getInputStream()) {

             file = new File("server-nonprod.jwks");

            copyInputStreamToFile(inputStream, file);

        }		
		
		

		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = SSLContextBuilder.create()
				.loadKeyMaterial(file, allPassword.toCharArray(), allPassword.toCharArray())
				.loadTrustMaterial(null, acceptingTrustStrategy).build();
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.setSSLContext(sslContext).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(client);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		return restTemplate;
	}

	private InputStream getFileAsIOStream(final String fileName) {
		InputStream ioStream = this.getClass().getClassLoader().getResourceAsStream(fileName);

		if (ioStream == null) {
			throw new IllegalArgumentException(fileName + " is not found");
		}
		return ioStream;
	}

	private void printFileContent(InputStream is) throws IOException {
		try (InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr);) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			is.close();
		}
	}
	
	
	private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }


}
