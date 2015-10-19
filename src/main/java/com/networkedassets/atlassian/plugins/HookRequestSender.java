package com.networkedassets.atlassian.plugins;

import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.RequestFactory;
import com.atlassian.sal.api.net.Response;
import com.atlassian.sal.api.net.ResponseException;
import com.atlassian.sal.api.net.ResponseHandler;
import com.atlassian.sal.api.net.ResponseProtocolException;
import com.atlassian.sal.api.net.ResponseTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.networkedassets.atlassian.plugins.HookRequest;

public class HookRequestSender {

	private static final Logger log = LoggerFactory.getLogger(HookRequestSender.class);
	private final RequestFactory<Request<?, Response>> requestFactory;

	public HookRequestSender(RequestFactory<Request<?, Response>> requestFactory) {

		this.requestFactory = requestFactory;

	}

	public void execute(HookRequest hook) {
		
		

		Request<?, Response> request = this.requestFactory.createRequest(Request.MethodType.POST,
				hook.getHookUri().toString());
		request.setSoTimeout(hook.getConnectionTimeOut());
		request.setConnectionTimeout(hook.getConnectionTimeOut());
		request.setHeader("X-Atlassian-Token", "no-check");
		request.setHeader("Accept", "text/html,text/plain,application/json,application/xml;q=0.9,*/*;q=0.8");
		request.setHeader("Content-Type", "application/json");
		request.setEntity(new Gson().toJson(hook.getContent()));

		try {
			request.execute(new ResponseHandler<Response>() {
				public void handle(Response response) throws ResponseException {

					if (!response.isSuccessful()) {
						HookRequestSender.log.warn(
								"Invalid response from hook '{}', repository ID '{}', status code '{}', status text '{}'",
								new Object[] { hook.getHookUri().toString(), hook.getRepository().getId(),
										Integer.valueOf(response.getStatusCode()), response.getStatusText() });
					} else {
						if (log.isDebugEnabled()) {
							HookRequestSender.log.debug(
									"Response from hook '{}', repository ID '{}', status code '{}', body '{}'",
									new Object[] { hook.getHookUri().toString(), hook.getRepository().getId(),
											Integer.valueOf(response.getStatusCode()),
											response.getResponseBodyAsString() });
						}

					}

				}
			});
		}

		catch (ResponseProtocolException e) {

			String message = "Unable post to '{}' hook for repository ID: '{}'";

			if (log.isDebugEnabled()) {
				log.debug(message,
						new Object[] { hook.getHookUri().toString(), hook.getRepository().getId(), e.getMessage(), e });
			} else {
				log.warn(message,
						new Object[] { hook.getHookUri().toString(), hook.getRepository().getId(), e.getMessage(), e });
			}

		}

		catch (ResponseTimeoutException e) {

			String message = "Unable post to '{}' hook for repository ID: '{}', Connection timeout: {}";
			if (log.isDebugEnabled()) {
				log.debug(message, new Object[] { hook.getHookUri().toString(), hook.getRepository().getId(),
						Integer.valueOf(hook.getConnectionTimeOut()), e.getMessage(), e });
			} else {
				log.warn(message,
						new Object[] { hook.getHookUri().toString(), hook.getRepository().getId(), e.getMessage(), e });
			}

		}

		catch (ResponseException e) {

			String message = "Unable post to '{}' hook for repository ID: '{}'";
			if (log.isDebugEnabled()) {
				log.debug(message,
						new Object[] { hook.getHookUri().toString(), hook.getRepository().getId(), e.getMessage(), e });
			} else {
				log.warn(message,
						new Object[] { hook.getHookUri().toString(), hook.getRepository().getId(), e.getMessage(), e });
			}

		}

	}

}
