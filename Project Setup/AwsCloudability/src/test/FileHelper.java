/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/*
 *
 * @author Admin
 */
public class FileHelper {
	public static void main(String[] args) {
		
		
		// Instantiate the custom HttpClient
		DefaultHttpClient client = new MyHttpClient();
		HttpGet get = new HttpGet("https://www.google.com");
		// Execute the GET call and obtain the response
		HttpResponse getResponse;
		try {
			getResponse = client.execute(get);
			HttpEntity responseEntity = getResponse.getEntity();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
//		
//		FileHelper fh = new FileHelper();
//		byte[] bytes = null;
//		try {
//			bytes = Files.readAllBytes(new File("D:\\work\\test.json").toPath());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		HashMap map = new HashMap();
//		map.put("x-api-key", "mW5rqe34tPCXiinkRcUw3WPbjwwLNkx7aQBmKxf5");
//		map.put("Accept", "application/json");
//		map.put("Content-Type", "application/json");
//		fh.sendJSONServer("https://api.opticks.com.br/001/exam/scanexam", new String(bytes), map);
	}

	public static String sendJSONServer(String url, String json, HashMap headerParameters) {

		try {

			DefaultHttpClient httpClient =  new MyHttpClient();
//			DefaultHttpClient httpClient =  new DefaultHttpClient();

			// httpClient = Test.wrapClient(httpClient);

			// DefaultHttpClient httpClient = new DefaultHttpClient();
			// HttpClient httpClient = Test.wrapClient();
			HttpPost postRequest = new HttpPost(url);
			// Log.v( TAG,"Posting URL "+url);
			StringEntity input = new StringEntity(json);
			input.setContentType("application/json");
			postRequest.setEntity(input);
			// Getting a Set of Key-value pairs
			Set entrySet = headerParameters.entrySet();
			// Obtaining an iterator for the entry set
			Iterator it = entrySet.iterator();
			// Iterate through HashMap entries(Key-Value pairs)
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				postRequest.setHeader(me.getKey().toString(), me.getValue().toString());
			}

			HttpResponse response = httpClient.execute(postRequest);

//			if (response.getStatusLine().getStatusCode() != 201) {
//				// Log.v( TAG,response.getStatusLine().toString());
//				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
//			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			StringBuffer output = new StringBuffer();
			String opLine = "";
			System.out.println("Output from Server .... \n");
			while ((opLine = br.readLine()) != null) {
				// System.out.println(output);
				output.append(opLine);
			}
			System.out.println(output.toString());
			httpClient.getConnectionManager().shutdown();
			return output.toString();
		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return "";

	}
}
