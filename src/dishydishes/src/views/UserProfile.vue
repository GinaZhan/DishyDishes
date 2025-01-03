<template>
    <q-layout view="lHh lpr lFf" class="shadow-2 rounded-borders">
        <q-page-container>
            <q-page padding>
                <q-card class="q-my-md">
                    <q-card-section>
                        <div class="row">
                            <div class="col-auto" style="padding-left:50px;padding-top:50px;padding-bottom:50px">
                                <q-avatar size="200px" @click="triggerAvatarPictureUploader">
                                    <img :src="avatar" class="rounded-full">
                                    <input
                                        style="display: none;"
                                        id="avatarPictureUploader"
                                        type="file"
                                        accept="image/*"
                                        @change="uploadAvatarPicture"
                                    />
                                </q-avatar>
                            </div>

                            <div class="col" style="padding-left:50px;padding-top:60px">
                                <div class="text-h6">{{ alias }}</div>
                                <div style="padding-top:10px">preferences</div>
                                <div v-for="(pref, index) in preferences" :key="index">
                                    <q-chip dense>{{ pref }}</q-chip>
                                </div>
                            </div>

                            <div class="col-1" style="padding-top:50px;padding-bottom:50px">
                                <q-btn v-if="!isMe" @click="toggleFollow" :ripple="false" :label="isFollower ? 'Unfollow' : 'Follow'"></q-btn>
                                <div class="text-caption" style="padding-top:20px">Followers: {{ followers ? followers.length : 0 }}</div>
                                <div class="text-caption">Following: {{ followed ? followed.length : 0 }}</div>
                            </div>
                        </div>
                    </q-card-section>

                    <q-card-section>
                        <q-tabs v-model="tab" align="justify">
                            <q-tab :ripple="false" name="my" label="My Post"></q-tab>
                            <q-tab :ripple="false" name="fav" label="Bookmarks"></q-tab>
                            <q-tab :ripple="false" name="comments" label="Comments"></q-tab>
                        </q-tabs>
                    </q-card-section>
                </q-card>

                <div class="recipe-row" v-if="tab === 'my'">
                    <router-link v-for="recipe in recipes" :key="recipe.id" :to="'/recipe/' + recipe.id" class="column is-one-fifth">
                    <RecipeCard :recipe="recipe" />
                    </router-link>
                </div>
                <div class="recipe-row" v-else-if="tab === 'fav'">
                    <router-link v-for="recipe in bookmarked" :key="recipe.id" :to="'/recipe/' + recipe.id" class="column is-one-fifth">
                    <RecipeCard :recipe="recipe" />
                    </router-link>
                </div>
                <div class="q-pa-md row items-start q-gutter-md" v-else-if="tab === 'comments'">
                    This is in progress. Thank you for waiting!
                </div>
            </q-page>
        </q-page-container>
    </q-layout>
</template>

<script>
import axios from 'axios';
import RecipeCard from '../components/RecipeCard';

export default {
    components: {
      RecipeCard
    },
    data(){
        return{
            userid: this.$route.params.id,
            username: "",
            alias: "",
            avatar: require("../assets/avatar.png"),
            preferences: [],
            followed: [],
            followers: [],
            bookmarked: [],
            tab: 'my',
            loading: false,
            recipes: [],
            isFollower: false,
            isMe: false,
            myUid: this.$route.params.id
        }
    },
    mounted() {
      if (this.$route.params.id) {
        this.fetchData(this.$route.params.id);
      }
    },
    methods:{
        fetchData(id){
            axios.get(`/api/user/id/${id}`)
            .then(response => {
                // Process the response data
                console.log(response.data);
                this.username = response.data["username"]
                this.alias = response.data["alias"]
                this.avatar = response.data["avatar"] ? `/api/recipe/images/`+response.data["avatar"] : this.avatar
                this.preferences = response.data["preferences"]
                this.followed = response.data["followed"]
                this.followers = response.data["followers"]
                this.bookmarked = response.data["bookmarked"]

                this.checkIfFollowed();
                this.fetchBookmarkedRecipes();
                this.fetchMyRecipes();
            })
            .catch(error => {
                // Handle errors
                alert('Error fetching data, please try again later.\n', error);
                this.$router.go(-1);
            });
        },
        async checkIfFollowed() {
            try {
            const token = this.$cookies.get('cookieToken');
            if (token) {
                const response = await axios.post('/api/user/login/check', {}, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
                });
                this.myUid = response.data;
                if (this.myUid == this.userid)
                    this.isMe = true
                this.isFollower = this.followers.includes(this.myUid)
            } else {
                this.isFollower = false
            }
            } catch (error) {
                alert('Please login first:', error);
            }
        },
        async fetchBookmarkedRecipes() {
            try {
                const token = this.$cookies.get('cookieToken');
                const promises = this.bookmarked.map(id =>
                    axios.get(`/api/recipe/${id}`, {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    })
                );
                const responses = await Promise.all(promises);
                this.bookmarked = responses.map((response, index) => ({
                    ...response.data,
                    id: this.bookmarked[index] // Assign 'id' from the original array
                }));
            } catch (error) {
                console.error('Error fetching bookmarked recipes:', error);
            }
        },
        toggleFollow() {
            const token = this.$cookies.get('cookieToken');
            if (this.isFollower) {
                axios.post('/api/user/follow/del?followId='+this.userid, {}, {headers: {Authorization: `Bearer ${token}`}})
                .then (() => {
                    const index = this.followers.indexOf(this.myUid);
                    if (index !== -1) this.followers.splice(index, 1);
                    this.isFollower = !this.isFollower;
                })
                .catch (error => {
                    alert('Error unfollow this user, please login or try again later.\n', error);
                });
            }
            else {
                axios.post('/api/user/follow/add?followId='+this.userid, {}, {headers: {Authorization: `Bearer ${token}`}})
                .then (() => {
                    this.followers.push(this.myUid);
                    this.isFollower = !this.isFollower;
                })
                .catch (error => {
                    alert('Error follow this user, please login or try again later.\n', error);
                });
            }
        },
        uploadAvatarPicture() {
            const file = event.target.files[0]
            if (file.size / 1024 > 1024*5) {
            alert(`Image size exceeds the maximum limit of 5MB.`);
            return;
            }
            const formData = new FormData();
            formData.append('avatar', file);
            //this.avatar = URL.createObjectURL(file)
            this.changeAvatar(formData);
        },
        triggerAvatarPictureUploader() {
            const fileInput = this.$el.querySelector(`#avatarPictureUploader`);
            fileInput.click();
        },
        async changeAvatar(formData) {
            try {
            const response = await axios.post('/api/user/avatar/set',formData,
                {headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': 'Bearer '+this.$cookies.get('cookieToken')
                }
            });
            if (response.data === true) {
                // Assuming the avatar was successfully updated
                alert('Avatar updated successfully!');
                // Optionally, update the user data after changing the avatar
                this.fetchData(this.$route.params.id);
            } else {
                console.log(response)
                alert('Failed to update avatar. Please try again.');
            }
            } catch (error) {
            alert('Error updating avatar. Please try again later.\n', error);
            }
        },
        async fetchMyRecipes() {
            try {
                this.loading = true;
                const response = await axios.get(`/api/user/recipe`, {params: { uid: this.myUid }});
                this.recipes = response.data;
                console.log(this.recipes)
            } catch (error) {
                console.error('Error fetching recipes:', error);
            } finally {
                this.loading = false;
            }
        },
    }
};
</script>
<style scoped>
.recipe-row {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  margin-top: 20px;
}

.recipe-row .column {
  width: 20%;
  margin-bottom: 10px;
}
</style>