# Event Listener for Bitbucket Server

* Plugin needs to be installed as Bitbucket add-on
* It is used to send notifications indicating that some changes in the repository's code were made

Format of the JSON that is sent looks as follow:

```json
{
   "sourceUrl":"http://url.tobitbucket.com:port",
   "projectKey":"FOOKEY",
   "repositorySlug":"foo-plugin",
   "branchId":"refs/heads/master"
}
```

You can read `projectKey` and `repositorySlug` from the repository url:

```
localhost:7990/projects/CAT/repos/catrepo/browse
```

Where `CAT` is projectKey and `catrepo` is repositorySlug