package swshin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

public class TestHttpClient {
	// Reference:
	// http://boredwookie.net/index.php/blog/how-to-use-bouncy-castle-lightweight-api-s-tlsclient/
	// bcprov-jdk15on-153.tar\src\org\bouncycastle\crypto\tls\test\TlsClientTest.java
	public static void main(String[] args) {
		new TestHttpClient().testIt();
	}

	public void testIt() {
		String u = "https://m8brrdkpo3.execute-api.ap-northeast-1.amazonaws.com/prod/event/counts?pmoNos=11";

		final URL url;

		try {
			url = new URL(u);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setSSLSocketFactory(new TLSSocketConnectionFactory());

			// dumpl all cert info
			print_https_cert(con);

			// dump all the content
			print_content(con);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void print_https_cert(HttpsURLConnection con) {

		if (con != null) {

			try {

				System.out.println("Response Code : " + con.getResponseCode());
//				System.out.println("Cipher Suite : " + con.getCipherSuite());
				System.out.println("\n");

				Certificate[] certs = con.getServerCertificates();
				for (Certificate cert : certs) {
					System.out.println("Cert Type : " + cert.getType());
					System.out.println("Cert Hash Code : " + cert.hashCode());
					System.out.println("Cert Public Key Algorithm : "
							+ cert.getPublicKey().getAlgorithm());
					System.out.println("Cert Public Key Format : "
							+ cert.getPublicKey().getFormat());
					System.out.println("\n");
				}

			} catch (SSLPeerUnverifiedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private void print_content(HttpsURLConnection con) {
		if (con != null) {

			try {

				System.out.println("****** Content of the URL ********");
				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream()));

				String input;

				while ((input = br.readLine()) != null) {
					System.out.println(input);
				}
				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
