package com.networkedassets.atlassian.plugins;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class HookRequestSender {

	private static final Logger log = LoggerFactory.getLogger(HookRequestSender.class);

	public void execute(HookRequest hook) {

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(hook.getConnectionTimeOut()).build();

		CloseableHttpClient httpClient = null;
		try {
			httpClient = HttpClients.custom().setHostnameVerifier(new AllowAllHostnameVerifier())
					.setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
						public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
							return true;
						}
					}).build()).setDefaultRequestConfig(requestConfig).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			log.error("Problem with create custom httpClient", e);
		}

		Unirest.setHttpClient(httpClient);

		try {
			log.info("Response received: {}", Unirest.post(hook.getHookUri().toString())

			.header("Accept", "text/html,text/plain,application/json,application/xml;q=0.9,*/*;q=0.8")
					.header("X-Atlassian-Token", "no-check").header("Content-Type", "application/json")
					.body( new JSONObject(hook.getContent()).toString()).asString().getBody());
		} catch (

		UnirestException e1)

		{
			String message = "Unable post to '{}' hook for repository ID: '{}'";
			if (log.isDebugEnabled()) {
				log.debug(message, new Object[] { hook.getHookUri().toString(), hook.getRepository().getId(),
						e1.getMessage(), e1 });
			} else {
				log.warn(message, new Object[] { hook.getHookUri().toString(), hook.getRepository().getId(),
						e1.getMessage(), e1 });
			}
		}

	}

}
