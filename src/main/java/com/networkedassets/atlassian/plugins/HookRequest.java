package com.networkedassets.atlassian.plugins;

import java.net.URI;
import java.util.Map;

import javax.annotation.Nonnull;

import com.atlassian.bitbucket.repository.RefChange;
import com.atlassian.bitbucket.repository.Repository;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

public class HookRequest {
	private final URI hookUrl;
	private final URI sourceUrl;
	private final Repository repository;
	private final RefChange refChange;
	private final int connectionTimeOut;

	public HookRequest(@Nonnull URI sourceUrl, @Nonnull Repository repository, @Nonnull RefChange change,
			@Nonnull URI hookUrl, @Nonnull int connectionTimeOut) {

		Preconditions.checkNotNull(repository);
		Preconditions.checkNotNull(sourceUrl);
		Preconditions.checkNotNull(hookUrl);
		Preconditions.checkNotNull(change);
		Preconditions.checkNotNull(connectionTimeOut);

		this.hookUrl = hookUrl;
		this.repository = repository;
		this.refChange = change;
		this.connectionTimeOut = connectionTimeOut;
		this.sourceUrl = sourceUrl;
	}

	@Nonnull
	public URI getHookUri() {
		return this.hookUrl;
	}

	@Nonnull
	public Repository getRepository() {
		return this.repository;
	}

	@Nonnull
	public RefChange getRefChange() {
		return refChange;
	}

	public Map<String, Object> getContent() {
		return ImmutableMap.<String, Object> builder()
				.put("sourceUrl", getSourceUrl().toString())
				.put("projectKey", getRepository().getProject().getKey())
				.put("repositorySlug", getRepository().getSlug())
				.put("branchId", getRefChange().getRefId()).build();
	}

	@Nonnull
	public int getConnectionTimeOut() {
		return this.connectionTimeOut;
	}

	public URI getSourceUrl() {
		return sourceUrl;
	}

}
