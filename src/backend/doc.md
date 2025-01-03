# API Reference
[User](#user)  
[Recipe](#recipe)  
[Comment](#comment)  
[Recommend](#recommend)  

## User
```
User{
    String id,
    String username,
    String credential,
    String alias,
    String avatar,
    List<String> followers,
    List<String> followed,
    List<String> bookmarked,
    List<String> preferences
}

UserDTO{
    String id,
    String username,
    String alias,
    String avatar,
    List<String> followers,
    List<String> followed,
    List<String> bookmarked,
    List<String> preferences
}
```
### create user
`/user/create`  
method: POST  
params:
- `String username`
- `String password`
- `String alias`

return: `String id`  
exceptions:
- `409 Conflict` if username exists
### login
`/user/login`  
method: POST  
params:
- `String username`
- `String password`

return: `String token`  
exceptions:
- `404 Not Found` if username do not exist
- `401 Unauthorized` if password incorrect
### check login
`/user/login/check`  
method: POST  
params: `String token`

headers:
- `Authorization: Bearer token`

return: `String uid`  
exceptions:
- `401 Unauthorized` if invalid token
### get user by id
`/user/id/{id}`  
method: GET  
params: `String id`  
return: `UserDTO`
### get user by username
`/user/username/{username}`  
method: GET  
params: `String username`  
return: `UserDTO`
### search users
`/user/search`  
method: GET  
params: `String alias`  
return: `List<UserDTO>`
### get all users(for test purpose)
`/user/all`  
method: GET  
return: `List<User>`
### follow
`/user/follow/add`  
method: POST  
params:
- `String followId`

headers:
- `Authorization: Bearer token`

return: `Boolean`  
exceptions:
- `409 Conflict` if tring to follow themselves
- `404 Not Found` if current user or followee do not exist
- `401 Unauthorized` if invalid token
### unfollow
`/user/follow/del`  
method: POST  
params:
- `String followId`

headers:
- `Authorization: Bearer token`

return: `Boolean`  
exceptions:
- `404 Not Found` if current user or followee do not exist
- `401 Unauthorized` if invalid token
### bookmark
`/user/bookmark/add`  
method: POST  
params:
- `String rid`

headers:
- `Authorization: Bearer token`

return: `Boolean`  
exceptions:
- `404 Not Found` if current user or recipe do not exist
- `401 Unauthorized` if invalid token
### unbookmark
`/user/bookmark/del`  
method: POST  
params:
- `String rid`

headers:
- `Authorization: Bearer token`

return: `Boolean`  
exceptions:
- `404 Not Found` if current user or recipe do not exist
- `401 Unauthorized` if invalid token
### add preference
`/user/preference/add`  
method: POST  
params:
- `String pref`

headers:
- `Authorization: Bearer token`

return: `Boolean`  
exceptions:
- `404 Not Found` if current user or do not exist
- `401 Unauthorized` if invalid token
### del preference
`/user/preference/del`  
method: POST  
params:
- `String pref`

headers:
- `Authorization: Bearer token`

return: `Boolean`  
exceptions:
- `404 Not Found` if current user do not exist
- `401 Unauthorized` if invalid token
### set avatar
`/user/avatar/set`  
method: POST  
params:
- `FormData(avatar:file)`

headers:
- `Authorization: Bearer token`

return: `Boolean`  
exceptions:
- `404 Not Found` if current user do not exist
- `401 Unauthorized` if invalid token
### get all recipes posted by a user
`/user/recipe`  
method: GET  
params:
- `String uid`

return: `List<Recipe>`

## Recipe
```
Recipe{
    String id;
    String name;
    String description;
    String coverPhoto;
    String uploaderId;
    Date uploadTime;
    List<String> bookmarkedUsers;
    List<Step> steps;
    List<Ingredient> ingredients;
    List<String> tags;
}

Step{
    String text;
    String imageUrl;
}

Ingredient{
    String name;
    String amount;
}

RecipeDTO{
    String id;
    String name;
    String description;
    String coverPhoto;
    String uploaderId;
    Date uploadTime;
    List<String> bookmarkedUsers;
    List<StepDto> steps;
    List<IngredientDto> ingredients;
    List<String> tags;
}

StepDto{
    String text;
    String imageBase64;
}

IngredientDto{
    String name;
    String amount;
}
```

### search by name
`/recipe/search`  
method: GET  
params:
- `String name`

return: `List<Recipe>`

### search by tag
`/recipe/search/byTag`  
method: GET  
params:
- `String tag`

return: `List<RecipeDTO>`

### search by ingredient
`/recipe/search/byIngredient`  
method: GET
params:
- `String ingredientName`

return: `List<RecipeDTO>`

### get recipe by id
`/recipe/{id}`  
method: GET
params:
- `String id`
- `404 Not Found` if invalid recipe id

return: `RecipeDTO`

### upload recipe
`/recipe/upload`  
method: POST
params:
- `RecipeDTO recipeDTO`

headers:
- `Authorization: Bearer token`

return: `RecipeDTO`

exceptions:
- `400 Bad Request` if request format not correct
- `401 Unauthorized` if invalid token

### delete recipe by id
`/recipe/{id}`  
method: DELETE
params:
- `String id`

headers:
- `Authorization: Bearer token`

exceptions:
- `401 Unauthorized` if invalid token
- `404 Not Found` if invalid recipe id

### get image by image id
`/recipe/images/{id}`  
method: GET
params:
- `String id`

return: `Resource`

exceptions:
- `404 Not Found` if invalid image id

## Comment
```
Comment{
    String id;
    String uid;
    String rid;
    String content;
    List<String> liked;
    Date timestamp;
}
```
### get by id
`/comment/id/{id}`  
method: GET  
return: `Comment`
### add comment
`/comment/add`  
method: POST  
params:
- `String rid`
- `String content`

headers:
- `Authorization: Bearer token`

return: `true`  
exceptions:
- `401 Unauthorized` if invalid token
### delete comment
`/comment/del`  
method: POST  
params:
- `String id`

headers:
- `Authorization: Bearer token`

return: `true`  
exceptions:
- `403 Forbidden` if uid doesnt match
- `401 Unauthorized` if invalid token
### get comments by rid
`/comment/get`  
method: GET  
params:
- `String rid`

headers:
- `Authorization: Bearer token`

return: `List<Comment>`

### like
`/comment/liked/add`  
method: POST  
params:
- `String cid`

headers:
- `Authorization: Bearer token`

return: `true`  
exceptions:
- `404 Not Found` if current user or comment do not exist
- `401 Unauthorized` if invalid token
### unlike
`/comment/liked/del`  
method: POST  
params:
- `String cid`

headers:
- `Authorization: Bearer token`

return: `true`  
exceptions:
- `404 Not Found` if current user or comment do not exist
- `401 Unauthorized` if invalid token

## Recommend
### 6 within a week with most bookmarks
`/recommend/hot`  
method: GET  
return: `List<Recipe>`
### random 6 within a day
`/recommend/random`  
method: GET  
return: `List<Recipe>`
### 5 newest by followed user
`/recommend/followed`  
method: GET  
params:
- `String uid`

return: `List<Recipe>`  
exceptions:
- `404 Not Found` if user does not exist
