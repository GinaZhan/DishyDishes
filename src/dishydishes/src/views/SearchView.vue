<template>
  <div class="search-view">
    <div v-if="loading">Loading...</div>
    <div v-else>
      <q-card-section>
          <q-tabs v-model="tab" align="justify">
              <q-tab @click="fetchRecipesByName" :ripple="false" name="name" label="recipes' names"></q-tab>
              <q-tab @click="fetchRecipesByTag" :ripple="false" name="tag" label="recipes' tags"></q-tab>
              <q-tab @click="fetchRecipesByIngredient" :ripple="false" name="ingredient" label="recipes' ingredients"></q-tab>
              <q-tab @click="fetchUsers" :ripple="false" name="alias" label="user alias"></q-tab>
          </q-tabs>
      </q-card-section>

      <div class="recipe-row" v-if="tab === 'name'">
        <router-link v-for="recipe in paginatedResults" :key="recipe.id" :to="'/recipe/' + recipe.id" class="column is-one-fifth">
          <RecipeCard :recipe="recipe" />
        </router-link>
      </div>
      <div class="recipe-row" v-if="tab === 'tag'">
        <router-link v-for="recipe in paginatedResults" :key="recipe.id" :to="'/recipe/' + recipe.id" class="column is-one-fifth">
          <RecipeCard :recipe="recipe" />
        </router-link>
      </div>
      <div class="recipe-row" v-if="tab === 'ingredient'">
        <router-link v-for="recipe in paginatedResults" :key="recipe.id" :to="'/recipe/' + recipe.id" class="column is-one-fifth">
          <RecipeCard :recipe="recipe" />
        </router-link>
      </div>
      <div class="recipe-row" v-if="tab === 'alias'">
        <router-link v-for="user in paginatedResults" :key="user.id" :to="'/user/' + user.id" class="column is-one-fifth">
          <UserCard :user="user" />
        </router-link>
      </div>
      <section v-show="totalPages > 0" style="margin-top: 20px;transform: translateX(39%);">
        <!-- Pagination controls -->
        <button @click="previousPage" :disabled="currentPage === 1">Previous</button>
        <span style="margin: 0 10px;">Page
          <input type="number" v-model.number="currentPage" min="1" :max="totalPages" style="width: 60px; padding: 5px;" :placeholder="currentPage">
          of {{ totalPages }}</span>
        <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
      </section>
    </div>
  </div>
</template>

<script>
import RecipeCard from '../components/RecipeCard';
import UserCard from '../components/UserCard';
import axios from 'axios';

export default {
  components: {
    RecipeCard,
    UserCard
  },
  data() {
    return {
      loading: false,
      tab: 'name',
      currentPage: 1,
      results: [], // Initialize recipes array
      pageSize: 15, // Number of recipes per page
    };
  },    
  computed: {
    totalPages() {
      return Math.ceil(this.results.length / this.pageSize);
    },
    paginatedResults() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.results.slice(startIndex, endIndex);
    }
  },
  methods: {
    async fetchRecipesByName() {
      try {
        this.loading = true;
        this.results = [];
        const response = await axios.get(`/api/recipe/search`, {
          params: { name: this.$route.query.q } // Fetch recipes based on the search query in the route query parameter
        });
        console.log(response);
        this.results = response.data;
      } catch (error) {
        console.error('Error fetching recipes:', error);
      } finally {
        this.loading = false;
      }
    },
    async fetchRecipesByTag() {
      try {
        this.loading = true;
        this.results = [];
        const response = await axios.get(`/api/recipe/search/byTag`, {
          params: { tag: this.$route.query.q } // Fetch recipes based on the search query in the route query parameter
        });
        console.log(response);
        this.results = response.data;
      } catch (error) {
        console.error('Error fetching recipes:', error);
      } finally {
        this.loading = false;
      }
    },
    async fetchRecipesByIngredient() {
      try {
        this.loading = true;
        this.results = [];
        const response = await axios.get(`/api/recipe/search/byIngredient`, {
          params: { ingredientName: this.$route.query.q } // Fetch recipes based on the search query in the route query parameter
        });
        console.log(response);
        this.results = response.data;
      } catch (error) {
        console.error('Error fetching recipes:', error);
      } finally {
        this.loading = false;
      }
    },
    async fetchUsers() {
      try {
        this.loading = true;
        this.results = [];
        const response = await axios.get(`/api/user/search`, {
          params: { alias: this.$route.query.q } // Fetch recipes based on the search query in the route query parameter
        });
        console.log(response);
        this.results = response.data;
      } catch (error) {
        console.error('Error fetching users:', error);
      } finally {
        this.loading = false;
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    }
  },
  watch: {
    // '$route.query.q': {
    //   handler: 'fetchRecipes', // Call fetchRecipes method when route query parameter changes
    //   immediate: true // Call fetchRecipes immediately when component is mounted
    // },
    '$route.query.q': {
      handler: 'fetchUsers', // Call fetchRecipes method when route query parameter changes
      immediate: true // Call fetchRecipes immediately when component is mounted
    }
  },
  mounted() {
    if (this.$route.query.tab) {
      this.tab = this.$route.query.tab;

      if (this.tab == "tag")
        this.fetchRecipesByTag();
      else if (this.tab == "ingredient")
        this.fetchRecipesByIngredient();
      else if (this.tab == "alias")
        this.fetchUsers();
      else
        this.fetchRecipesByName();
    } else {
      this.fetchRecipesByName();
    }
  }
};
</script>

<style scoped>
  .recipe-row {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    margin-top: 20px;
    margin-left: 1%;
    margin-right: 1%;
  }

  .recipe-row .column {
    width: 20%;
    margin-bottom: 10px;
  }

  @media (max-width: 768px) {
    .recipe-row {
      flex-direction: column; /* Stack boxes vertically */
      margin-right: 0;
      margin-left: 0;
    }
    .recipe-row .column{
      width: 94%;
      margin-right: 3%;
      margin-left: 3%;
    }
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
</style>