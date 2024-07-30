# RepositoryAPI

### 
Repository API is an application designed to handle the retrieval of repositories for a specified GitHub user, along 
with their branches.

This application allows you to retrieve a list of repositories for a specified GitHub user, excluding forked 
repositories. For each repository, it provides details on the branches, including branch names and their latest commit 
SHA.

Additionally, the application provides error handling in case the specified user does not exist.

#### DOCS
GET users/{username}/repositories

Get repositories excluding forks with branches from given username

Response

SUCCESS
```
200 OK
{
  "ownerLogin": "string",
  "repositories": [
    {
      "name": "string",
      "branches": [
        {
          "name": "string",
          "lastCommitSHA": "string"
        }
      ]
    }
  ]
}
```
ERROR
```
400 BAD REQUEST
{
  "status": int,
  "message": "string"
}
```
```
404 NOT FOUND (User not found)
{
  "status": int,
  "message": "string"
}
```