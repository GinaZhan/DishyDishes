<template>
    <div class="login-page">
      <div v-if="!showSignup">
        <h1>Login</h1>
        <form @submit.prevent="login">
          <input type="username" v-model="username" placeholder="Username" required>
          <input type="password" v-model="password" placeholder="Password" required>
          <button type="submit">Login</button>
          <p>Don't have an account? <a @click="toggleSignup">Sign up here</a>.</p>
        </form>
      </div>
    
      <div v-if="showSignup">
        <h1>Sign Up</h1>
        <form @submit.prevent="signup">
          <input type="username" v-model="username" placeholder="Username" required>
          <input type="password" v-model="password" placeholder="Password" required>
          <input type="alias" v-model="alias" placeholder="Alias" required>
          <button type="submit">Sign up</button>
          <p>Already have an account? <a @click="toggleSignup">Log in here</a>.</p>
        </form>
      </div>
  
      <p v-if="error" class="error">{{ error }}</p>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
//   import { useRouter } from 'vue-router';
  
export default {
    data() {
        return {
            username: "",
            password: "",
            alias: "",
            error: "",
            showSignup: false
        };
    },
    methods: {
        async login() {
            axios.post('/api/user/login?username=' + this.username + '&password=' + this.password)
            .then(response => {
                console.log('User login:', response.data);
                this.$cookies.set('cookieToken', response.data)
                // Redirect to previous page on successful login
                this.$router.go(-1);
            })
            .catch(error => {
                this.error = 'Invalid username or password';
                console.error('Login error:', error);
            });
        }, 
        
        async signup() {
            axios.post('/api/user/create?username=' + this.username + '&password=' + this.password + '&alias=' + this.alias)
            .then(response => {
                console.log('User created:', response.data);
                // Redirect to login on successful signup
                this.showSignup = !this.showSignup
            })
            .catch(error => {
                alert('Error creating user, please try again later.\n', error);
            });
        },
        toggleSignup() {
            this.showSignup = !this.showSignup;
            this.error = ''; // Clear error message when toggling between login and signup
        }
    }
};
  </script>
  
  <style scoped>
  .login-page {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  
  input {
    width: 100%;
    margin-bottom: 10px;
    padding: 10px;
  }
  
  button {
    width: 100%;
    padding: 10px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .error {
    color: red;
  }
  </style>