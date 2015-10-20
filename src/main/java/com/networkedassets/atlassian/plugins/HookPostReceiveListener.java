package com.networkedassets.atlassian.plugins;

import java.net.URI;
import java.util.Collection;

import javax.annotation.Nonnull;

import com.atlassian.bitbucket.hook.repository.AsyncPostReceiveRepositoryHook;
import com.atlassian.bitbucket.hook.repository.RepositoryHookContext;
import com.atlassian.bitbucket.repository.RefChange;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.server.ApplicationPropertiesService;
import com.atlassian.bitbucket.setting.RepositorySettingsValidator;
import com.atlassian.bitbucket.setting.Settings;
import com.atlassian.bitbucket.setting.SettingsValidationErrors;
import com.google.common.base.Preconditions;

public class HookPostReceiveListener implements AsyncPostReceiveRepositoryHook, RepositorySettingsValidator {

	private static final String CONN_TIMEOUT_KEY = "timeout";
	private static final String URL_NAME_KEY = "url";

	private final HookRequestSender requestSender;
	private final ApplicationPropertiesService propertiesService;

	public HookPostReceiveListener(HookRequestSender requestSender, ApplicationPropertiesService propertiesService) {
		this.requestSender = requestSender;
		this.propertiesService = propertiesService;
	}

	@Override
	public void postReceive(@Nonnull RepositoryHookContext context, @Nonnull Collection<RefChange> refChanges) {

		Preconditions.checkNotNull(context);
		Preconditions.checkNotNull(refChanges);

		URI sourceUrl = propertiesService.getBaseUrl();

		for (RefChange change : refChanges) {
			this.requestSender.execute(new HookRequest(sourceUrl, context.getRepository(), change,
					URI.create(context.getSettings().getString(URL_NAME_KEY)),
					context.getSettings().getInt(CONN_TIMEOUT_KEY)));
		}
	}

	@Override
	public void validate(Settings settings, SettingsValidationErrors errors, Repository repository) {
		if (settings.getString(URL_NAME_KEY).isEmpty()) {
			errors.addFieldError(URL_NAME_KEY, "Url field is blank");
		}

		if (settings.getString(CONN_TIMEOUT_KEY).isEmpty()) {
			errors.addFieldError("CONN_TIMEOUT_KEY", "Connection time out field is blank");
		}
	}

}
