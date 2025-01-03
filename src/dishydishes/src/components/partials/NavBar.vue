<template>
  <nav class="navbar container" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <a class="navbar-item" href="/">
        <strong class="is-size-4">DishyDishes</strong>
      </a>
      <a
        role="button"
        class="navbar-burger burger"
        aria-label="menu"
        aria-expanded="false"
        data-target="navbarBasicExample"
        @click="toggleMenu"
      >
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
      </a>
    </div>

    <div :class="['navbar-menu', { 'is-active': isMenuOpen }]">
      <div class="navbar-start">
        <router-link to="/" class="navbar-item">Home</router-link>
        <router-link to="/about" class="navbar-item">About</router-link>
      </div>
    </div>
      <div class="navbar-start">
        <input type="text" v-model="input" @keyup.enter="handleSearch" placeholder="Search recipe, tags, or user alias..." />
      </div>
    <div :class="['navbar-menu', { 'is-active': isMenuOpen }]">
      <div class="navbar-end">
        <div class="navbar-item">
          <div class="buttons">
            <div>
              <!-- show login when not authenticated -->
              <router-link v-if="!isAuthenticated" :to="'/user/login'" class="button is-dark">
                <strong>Log in</strong>
              </router-link>
              <!-- show logout when authenticated -->
              <a v-if="isAuthenticated" @click="logout" class="button is-dark"
                ><strong>Log out</strong></a
              >
              <router-link v-if="isAuthenticated" :to="'/user/' + user.id">
                <img :src="user.avatar ? `/api/recipe/images/`+user.avatar : defaultAvatar" class="user-avatar rounded-full">
                {{ user.alias }}
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>
<script>
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  name: 'NavBar',
  data() {
    return {
      defaultAvatar: require("../../assets/avatar.png"),
      input: '',
      isAuthenticated: false,
      user: { name: '' },
      isMenuOpen: false
    };
  },
  async mounted() {
    await this.checkLoginAndFetchUserData();
  },
  async created() {
    this.checkLoginAndFetchUserData(); // Use created lifecycle hook
    const router = useRouter(); // Get router instance
    router.beforeEach((to, from, next) => {
      this.checkLoginAndFetchUserData(); // Trigger checkLoginAndFetchUserData on route change
      next(); // Continue navigation
    });
  },
  methods: {
    async handleSearch() {
      try {
        if (this.input.trim()) {
          this.$router.push({ name: 'SearchView', query: { q: this.input } });
        }
      } catch (error) {
        console.error('Error searching recipes:', error);
      }
    },
    async logout() {
      try {
        this.$cookies.remove('cookieToken');
        this.isAuthenticated = false;
        this.user = { name: '' };
        await this.$router.push('/');
      } catch (error) {
        console.error('Error logging out:', error);
      }
    },
    async checkLoginAndFetchUserData() {
      try {
        const token = this.$cookies.get('cookieToken');
        if (token) {
          const response = await axios.post('/api/user/login/check', {}, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
          const uid = response.data;
          const userDataResponse = await axios.get(`/api/user/id/${uid}`);
          this.user = userDataResponse.data;
          this.isAuthenticated = true;
        } else {
          this.isAuthenticated = false;
        }
      } catch (error) {
        console.error('Error checking login status:', error);
      }
    },
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen;
    }
  }
};
</script>
<style lang="scss" scoped>
@import url("https://fonts.googleapis.com/css2?family=Montserrat&display=swap");
  nav {
    margin-top: 25px;
    margin-bottom: 30px;
    a {
      font-weight: bold;
      color: #2c3e50;
      &.router-link-exact-active {
        color: #d88d00;
      }
    }
  }
  input {
  display: block;
  width: 350px;
  margin: 20px auto;
  padding: 10px 45px;
  background: white url("../../assets/search.svg") no-repeat 15px center;
  background-size: 15px 15px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  box-shadow: rgba(50, 50, 93, 0.25) 0px 2px 5px -1px,
    rgba(0, 0, 0, 0.3) 0px 1px 3px -1px;
}
.user-avatar {
  border-radius: 50%; /* Round corners to make it circular */
}
@media screen and (max-width: 1100px) {
  .navbar-menu {
    display: none;
  }
  .navbar-menu.is-active {
    display: block;
    text-align: center;
  }
  .buttons {
    display: inline-block; /* Center the buttons horizontally */
  }
}
</style>