# Bitbucket Hook Post Receive Plugin

* Plugin needs to be installed as Bitbucket add-on
* It is used to send notifications to transformer that some changes in the repository's code were made

Format of the JSON that is sent to the transformer looks as follow:

```json
{  
   "sourceUrl":"refs/heads/master",
   "projectKey":"FOOKEY",
   "repositorySlug":"foo-plugin",
   "branchId":"http://url.tobitbucket.com:port"
}
```

You can read `projectKey` and `repositorySlug` from the repository url:

```
localhost:7990/projects/CAT/repos/catrepo/browse
```

Where `CAT` is projectKey and `catrepo` is repositorySlug