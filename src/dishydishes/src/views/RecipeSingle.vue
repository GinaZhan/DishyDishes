<template>
  <div class="recipe-single">
    <section class="hero is-primary" :style="{ 'background-image': 'url(' + coverPhoto + ')' }">
      <div class="hero-body">
        <div class="container">
          <h1 class="title">{{ recipe_name }}</h1>
        </div>
      </div>
    </section>

    <section>
      <!-- leftbox -->
      <div class="leftbox">
        <!-- description -->
        <section>
          <h2>Description</h2>
          <p>{{ description }}</p>
        </section>
        <!-- ingredients -->
        <section>
          <h2>Ingredients</h2>
          <table class="ingredients-table">
            <tr v-for="ingredient in ingredients" :key="ingredient.name">
              <td>{{ingredient.name}}</td>
              <td>{{ingredient.amount}}</td>
            </tr>
          </table>
        </section>
        <!-- recipe contents/steps -->
        <section>
          <h2>Steps</h2>
          <ol>
            <li v-for="(step,index) in steps" :key="index">
              <div class="step-container">
                <p style="width: 45%;">{{ step.text }}</p>
                <img :src="step.imageUrl" class="step-image">
              </div>
            </li>
          </ol>
        </section>
      </div>
      <!-- rightbox -->
      <div class="rightbox">
        <section v-if="myUid == uploaderInfo.id" style="display: flex;justify-content:end;">
          <!-- /* TODO: currently we don't have api ready for this.
          <button class="update-button" @click="this.$router.push(`update/`+this.$route.params.id);">Update my recipe</button>
          -->
          <button class="delete-button" @click="deleteRecipe">Delete my recipe</button>
        </section>
        <section class="recipe-info">
          <router-link :to="'/user/' + uploaderInfo.id"  class="author-avatar">
            <!-- Add the URL for the author's avatar -->
            <img :src="uploaderInfo.avatar? `/api/recipe/images/`+uploaderInfo.avatar : defaultAvatar" alt="Author Avatar">
          </router-link>
          <div class="author-details">
            <h2 style="color: black;">{{ uploaderInfo.alias }}</h2>
            posted on {{ uploadTime }}
          </div>
        </section>
        <section style="display: flex; align-items: center;">
          <p style="float:left;width:80%;margin-right:5px;">{{bookmarkNumber}} users bookmarked this recipe</p>
          <div class="heart" :class="{'bookmarked': bookmarked}" @click="bookmark"></div>
        </section>
        <section>
          <h2>Related Tags</h2>
          <div style="display: flex; flex-wrap: wrap;">
            <router-link :to="{ name: 'SearchView', query: { q: tag, tab: 'tag' }}" class="tag-box" v-for="(tag, index) in tags" :key="index">
              {{ tag }}
            </router-link>
          </div>
        </section>
      </div>
    </section>

    <section style="float: left; padding-left: 2%; padding-right: 2%; width: 100%;">
      <section style="display: flex; align-items: center;">
        <h2 style="margin-right: auto;">Comments</h2>
        <p style="margin-right: 10px; margin-top: 15px; cursor: pointer;" @click="sortByLikes">Sort by Likes</p>
        <p style="margin-right: 10px; margin-top: 15px;">|</p>
        <p style="cursor: pointer; margin-top: 15px;" @click="sortByTime">Sort by Time</p>
      </section>
      <div style="position: relative;display: flex;">
        <textarea class="inputbox" 
          :style="{ width: 'calc(100% - 120px)' }"
          v-model="newComment" 
          placeholder="Leave a comment..."></textarea>
        <button style="position: absolute;right: 10px;top: 50%;transform: translateY(-50%);" @click="addComment">Post</button>
      </div>
      <div v-if="comments.length === 0">
        <p style="padding-top: 10px;">No comments yet. Be the first to leave a comment!</p>
      </div>
      <div v-else>
        <div v-for="(comment, index) in paginatedComments" :key="index" class="comment">
          <div class="comment-details">
            <router-link :to="'/user/' + comment.uid" class="comment-user-info">
              <img :src="comment.avatar? `/api/recipe/images/`+comment.avatar : defaultAvatar" alt="User Avatar" class="comment-user-avatar">
              <span>{{ comment.alias }}</span>
            </router-link>
            <div class="comment-actions">
              <div class="likes-container">
                <span>{{ comment.liked.length }}</span>
              </div>
              <button @click="likeComment(index)" class="thumb-up-button" :class="{ active: comment.liked.includes(myUid) }">
                <svg style="margin-bottom: -2.5px;" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-hand-thumbs-up" viewBox="0 0 16 16">
                  <path d="M8.864.046C7.908-.193 7.02.53 6.956 1.466c-.072 1.051-.23 2.016-.428 2.59-.125.36-.479 1.013-1.04 1.639-.557.623-1.282 1.178-2.131 1.41C2.685 7.288 2 7.87 2 8.72v4.001c0 .845.682 1.464 1.448 1.545 1.07.114 1.564.415 2.068.723l.048.03c.272.165.578.348.97.484.397.136.861.217 1.466.217h3.5c.937 0 1.599-.477 1.934-1.064a1.86 1.86 0 0 0 .254-.912c0-.152-.023-.312-.077-.464.201-.263.38-.578.488-.901.11-.33.172-.762.004-1.149.069-.13.12-.269.159-.403.077-.27.113-.568.113-.857 0-.288-.036-.585-.113-.856a2 2 0 0 0-.138-.362 1.9 1.9 0 0 0 .234-1.734c-.206-.592-.682-1.1-1.2-1.272-.847-.282-1.803-.276-2.516-.211a10 10 0 0 0-.443.05 9.4 9.4 0 0 0-.062-4.509A1.38 1.38 0 0 0 9.125.111zM11.5 14.721H8c-.51 0-.863-.069-1.14-.164-.281-.097-.506-.228-.776-.393l-.04-.024c-.555-.339-1.198-.731-2.49-.868-.333-.036-.554-.29-.554-.55V8.72c0-.254.226-.543.62-.65 1.095-.3 1.977-.996 2.614-1.708.635-.71 1.064-1.475 1.238-1.978.243-.7.407-1.768.482-2.85.025-.362.36-.594.667-.518l.262.066c.16.04.258.143.288.255a8.34 8.34 0 0 1-.145 4.725.5.5 0 0 0 .595.644l.003-.001.014-.003.058-.014a9 9 0 0 1 1.036-.157c.663-.06 1.457-.054 2.11.164.175.058.45.3.57.65.107.308.087.67-.266 1.022l-.353.353.353.354c.043.043.105.141.154.315.048.167.075.37.075.581 0 .212-.027.414-.075.582-.05.174-.111.272-.154.315l-.353.353.353.354c.047.047.109.177.005.488a2.2 2.2 0 0 1-.505.805l-.353.353.353.354c.006.005.041.05.041.17a.9.9 0 0 1-.121.416c-.165.288-.503.56-1.066.56z"/>
                </svg>
              </button>
            </div>
          </div>
          <div style="display: flex;">
            <p style="width: 95%;" v-html="comment.content.replace(/\n/g, '<br>')"></p>
            <button v-show="myUid == comment.uid" class="delete-button" @click="deleteComment(comment.id)">Delete</button>
          </div>
          <div class="comment-meta">
            <span class="comment-time">{{ new Date(comment.timestamp).toLocaleString() }}</span>
          </div>
        </div>
      </div>
      <section v-show="totalPages > 0" style="margin-top: 20px;float: right;">
        <!-- Pagination controls -->
        <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
        <span style="margin: 0 10px;">Page
          <input type="number" v-model.number="currentPage" min="1" :max="totalPages" style="width: 60px; padding: 5px;" :placeholder="currentPage">
          of {{ totalPages }}</span>
        <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
      </section>
    </section>
  </div>
</template>

<script>
  import axios from 'axios';

  export default {
    data() {
      return {
        coverPhoto: require('../assets/card2.jpg'),
        recipe_name : "recipe name",
        description : "",

        steps: [],

        ingredients: [],

        bookmarked: false,
        bookmarkedUsers: [],

        uploaderInfo: {},
        uploadTime: "some day some time",

        tags: [],

        comments: [],
        newComment: '',
        // Pagination properties
        currentPage: 1,
        pageSize: 5,

        defaultAvatar: require("../assets/avatar.png"),
        myUid: ""
      };
    },
    mounted() {
      if (this.$route.params.id) {
        this.fetchData(this.$route.params.id);
        this.fetchComments(this.$route.params.id);
      }
    },
    computed: {
      paginatedComments() {
        const startIndex = (this.currentPage - 1) * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        return this.comments.slice(startIndex, endIndex);
      },
      totalPages() {
        return Math.ceil(this.comments.length / this.pageSize);
      },
      bookmarkNumber() {
        return this.bookmarkedUsers.length
      }
    },
    methods: {
      toImageUrl(id){
        if (id == null) return null;
        return `/api/recipe/images/${id}`;
      },
      fetchData(id){
        axios.get(`/api/recipe/${id}`)
        .then(response => {
          // Process the response data
          console.log(response.data);
          this.coverPhoto = this.toImageUrl(response.data["coverPhoto"]);
          this.recipe_name = response.data["name"];
          this.description = response.data["description"];
          this.steps = response.data["steps"].map(step => {
            return {
              text: step.text,
              imageUrl: this.toImageUrl(step.imageUrl)
            };
          });
          this.ingredients = response.data["ingredients"];
          this.tags = response.data["tags"];
          this.uploadTime = new Date(response.data["uploadTime"]).toLocaleString();
          this.bookmarkedUsers = response.data["bookmarkedUsers"];

          this.checkIfBookmarked()

          axios.get(`/api/user/id/`+response.data["uploaderId"])
            .then(userDataResponse => {
              this.uploaderInfo = userDataResponse.data;
            });
        })
        .catch(error => {
          // Handle errors
          alert('Error fetching data, please try again later.\n', error);
          this.$router.go(-1);
        });
      },
      fetchComments(id) {
        axios.get(`/api/comment/get?rid=${id}`)
          .then(response => {
            const comments = response.data;

            // Iterate over each comment to fetch user details
            const fetchUserDetailsPromises = comments.map(comment => {
              return axios.get(`/api/user/id/${comment.uid}`)
                .then(userDataResponse => {
                  // Update the comment with user alias and avatar
                  comment.alias = userDataResponse.data.alias;
                  comment.avatar = userDataResponse.data.avatar;
                });
            })
            
            return Promise.all(fetchUserDetailsPromises)
              .then(() => {
                // Once all user details are fetched and updated, assign comments to the component data
                this.comments = comments;
                this.sortByLikes()
              });
          })
          .catch(error => {
            alert('Error fetching comments, please try again later.\n', error);
          });
      },
      async checkIfBookmarked() {
        try {
          const token = this.$cookies.get('cookieToken');
          if (token) {
            const response = await axios.post('/api/user/login/check', {}, {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
            this.myUid = response.data;
            this.bookmarked = this.bookmarkedUsers.includes(this.myUid)
          } else {
            this.bookmarked = false
          }
        } catch (error) {
          this.bookmarked = false
        }
      },
      deleteRecipe(){
        //pop up to ask if they sure
        const confirmDelete = confirm('Are you sure you want to delete this recipe?');
        if (confirmDelete) {
          // Proceed with the deletion logic
          const token = this.$cookies.get('cookieToken');
          axios.delete(`/api/recipe/`+this.$route.params.id, {headers: {Authorization: `Bearer ${token}`}})
            .then(() => {
              this.$router.go(-1);
            })
            .catch(error =>{
              alert('Error deleting this recipe, please try again later.\n', error);
            })
        }
      },
      bookmark() {
        const token = this.$cookies.get('cookieToken');
        if (this.bookmarked) {
          axios.post('/api/user/bookmark/del?rid='+this.$route.params.id, {}, {headers: {Authorization: `Bearer ${token}`}})
          .then (() => {
            const index = this.bookmarkedUsers.indexOf(this.myUid);
            if (index !== -1) this.bookmarkedUsers.splice(index, 1);
            this.bookmarked = !this.bookmarked;
          })
          .catch (error => {
            alert('Error unbookmark this recipe, please login or try again later.\n', error);
          });
        }
        else {
          axios.post('/api/user/bookmark/add?rid='+this.$route.params.id, {}, {headers: {Authorization: `Bearer ${token}`}})
          .then (() => {
            this.bookmarkedUsers.push(this.myUid);
            this.bookmarked = !this.bookmarked;
          })
          .catch (error => {
            alert('Error bookmark this recipe, please login or try again later.\n', error);
          });
        }
      },
      addComment() {
        const token = this.$cookies.get('cookieToken');
        if (this.newComment.trim() !== '') {
          axios.post(`/api/comment/add?rid=`+this.$route.params.id+`&content=`+encodeURIComponent(this.newComment),{}, {headers: {Authorization: `Bearer ${token}`}})
          .then (() => {
            // Reload the page and keep the position
            const scrollPosition = window.scrollY;
            location.reload();
            window.scrollTo(0, scrollPosition);
          })
          .catch (error => {
            alert('Error post this comment, please login or try again later.\n', error);
          });
        }
      },
      deleteComment(id) {
        const token = this.$cookies.get('cookieToken');
        axios.post(`/api/comment/del?id=`+id,{}, {headers: {Authorization: `Bearer ${token}`}})
          .then (() => {
            this.comments = this.comments.filter(comment => comment.id !== id);
          })
          .catch (error => {
            alert('Error delete this comment, please login or try again later.\n', error);
          });
      },
      likeComment(commentIndex) {
        const comment = this.paginatedComments[commentIndex]; // Get the comment from paginatedComments
        const globalIndex = (this.currentPage - 1) * this.pageSize + commentIndex; // Calculate the global index
        const token = this.$cookies.get('cookieToken');
        if (comment.liked.includes(this.myUid)) {
          axios.post('/api/comment/liked/del?cid='+comment.id, {}, {headers: {Authorization: `Bearer ${token}`}})
          .then (() => {
            const index = this.comments[globalIndex].liked.indexOf(this.myUid);
            if (index !== -1) this.comments[globalIndex].liked.splice(index, 1);
          })
          .catch (error => {
            alert('Error unbookmark this recipe, please login or try again later.\n', error);
          });
        } else {
          axios.post('/api/comment/liked/add?cid='+comment.id, {}, {headers: {Authorization: `Bearer ${token}`}})
          .then (() => {
            this.comments[globalIndex].liked.push(this.myUid);
          })
          .catch (error => {
            alert('Error bookmark this recipe, please login or try again later.\n', error);
          });
        }
      },
      sortByLikes() {
        this.comments.sort((a, b) => b.liked.length - a.liked.length); // Sort comments by likes in descending order
        this.currentPage = 1
      },
      sortByTime() {
        this.comments.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp)); // Sort comments by time in descending order
        this.currentPage = 1
      },
      // Pagination methods
      nextPage() {
        if (this.currentPage < this.totalPages) {
          this.currentPage++;
        }
      },
      prevPage() {
        if (this.currentPage > 1) {
          this.currentPage--;
        }
      }
    }
  };
</script>

<style lang="scss" scoped>
  .recipe-single {
    margin-top: 30px;
  }
  .hero {
    text-align: center;
    background-size: cover;
    background-color: gray;
    background-position: center;
    background-repeat: no-repeat;
    height: 400px;
    margin-bottom: 20px;
  }
  section {
    margin-bottom: 10px;
  }
  .hero-body .title {
    text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.6);
    padding: 100px 0 0 0;
    font-size: 60px;
  }
  .leftbox {
    float:left; 
    width:65%;
    padding-left: 2%;
  }
  h2 {
    font-size: 25px;
    color: #d88d00;
  }
  .ingredients-table {
    border-collapse: collapse;
    width: 100%;

    tr {
      &:hover {
        background-color: rgb(243, 183, 161);
      }

      th {
        padding: 8px;
        text-align: center;
      }

      td {
        padding: 8px;
        position: relative;
        border-bottom: 1px solid #ddd;
      }
    }
  }
  ol {
    padding-left: 20px;

    li {
      padding: 5px;
    }
  }
  .step-container {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;

    .step-image {
      width: 52%;
      margin-left: 3%;
      object-fit: contain;
  }
  }
  .rightbox {
    float:right; 
    width:30%;
    padding-right: 2%;
  }
  .recipe-info {
    display: flex;
    margin-top: 10px;
  }
  .author-avatar img {
    width: 50px; /* Set the width of the avatar as needed */
    height: 50px; /* Set the height of the avatar as needed */
    border-radius: 50%; /* Make it circular */
    object-fit: cover; /* Maintain aspect ratio and cover the entire container */
    margin-right: 10px;
  }
  .author-details {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-top: -15px;
    margin-left: 10px;
  }

  .heart{
    float:right;
    background:#f00;
    height:20px;
    width:20px;
    opacity:.3;
    position:relative;
    transform:rotate(-45deg);
    transition: background 0.3s ease,opacity 0.3s ease;
    margin-top: -20px;
    margin-left: 3px;
  }
  .heart:hover{
    opacity:1;
  }
  .heart:active{
    background:#000;
    opacity:1;
  }
  .heart:before,.heart:after{
    content:"";
    background:inherit;
    border-radius:50%;
    position:absolute;
    height:20px;
    width:20px;
  }
  .heart:before{
    left:0;
    top:-10px;
  }
  .heart:after{
    left:10px;
    top:0;
  }
  .bookmarked {
    opacity: 1;
  }
  .tag-box {
    text-align: center;
    margin: 5px;
    padding: 8px;
    background-color: #f0f0f0;
    border: 1px solid #ccc;
    border-radius: 5px;
    width:fit-content;
  }
  button {
    background-color: #6ab391;
    color: white;
    border: none;
    padding: 8px 12px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 14px;
    margin: 0 8px;
    cursor: pointer;
    border-radius: 4px;
    transition: background-color 0.3s;
    height: fit-content;
    width: 75px;
    
    &:hover {
      background-color: #5fa081;
    }
  }

  .inputbox{
    font-size:medium;
    border: 1px solid #ddd;
    padding: 8px;
  }

  .comment {
    border-bottom: 1px solid #ccc;
    padding-top: 10px;
    padding-bottom: 4px;
  }

  .comment-details {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
  }

  .comment-user-info {
    display: flex;
    align-items: center;
    font-weight: bold;
  }

  .comment-user-avatar {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    margin-right: 8px;
  }

  .comment-meta {
    font-size: 12px;
  }

  .comment-actions {
    margin-top: 8px;
  }

  .delete-button {
    background-color: rgb(243, 99, 99);
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 4px;
    cursor: pointer;
    width: fit-content;

    &:hover {
      background-color: rgb(206, 84, 84);
    }
  }

  .update-button {
    background-color: rgb(81, 140, 218);
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 4px;
    cursor: pointer;
    width: fit-content;

    &:hover {
      background-color: rgb(58, 97, 158);
    }
  }

  .thumb-up-button {
    background-color: white;
    color: #6ab391;
    border: none;
    padding: 0;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    cursor: pointer;
    border-radius: 4px;
    transition: background-color 0.3s, color 0.3s;
    width: fit-content;
  }

  .thumb-up-button.active {
    background-color: #6ab391;
    color: white;
  }

  .thumb-up-button:hover {
    color: #5fa081;
  }

  .likes-container {
    display: inline-block;
    vertical-align: middle;
  }
</style>