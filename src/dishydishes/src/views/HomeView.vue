<template>
  <div class="home">
    <section class="hero is-dark">
      <div class="hero-body">
        <div class="container">
          <h1 class="title">Welcome to the DishyDishes</h1>
          <h2 class="subtitle">
            Explore a world of culinary delights at your fingertips!
          </h2>
        </div>
      </div>
    </section>

    <div class="boxes-container">
      <div class="leftbox">
        <Carousel>
        <!-- <Slide v-for="slide in 6" :key="slide">
          <div class="carousel__item"><img src="../assets/card2.jpg"></div> -->
        <Slide v-for="(recipe,index) in carouselRecipes" :key="index">
          <div class="carousel__item" :style="{ 'background-image': `url(/api/recipe/images/${recipe.coverPhoto})`}">
            <router-link :to="'/recipe/' + recipe.id">
            <div class="card-content">
              <h2 class="is-size-3 has-text-weight-bold">{{ recipe.name }}</h2>
              <small class="event-date">{{ new Date(recipe.uploadTime).toLocaleString() }}</small>
              <span>{{ recipe.uploaderName }}</span>
            </div>
          </router-link>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
          <Pagination />
        </template>
      </Carousel>
      </div>
      <div class="rightbox">
        <div class="columns is-multiline">
          <router-link v-for="recipe in rightBoxRecipes" :key="recipe.id" :to="'/recipe/' + recipe.id" class="column is-one-third">
            <RecipeCard :recipe="recipe"/>
          </router-link>
        </div>
      </div>
    </div>

    <div class="button-row">
      <div class="button-container">
        <button @click="handleCategorySearch('Asian')" class="button is-primary asian-button">Asian</button>
        <button @click="handleCategorySearch('Dessert')" class="button is-primary dessert-button">Dessert</button>
        <button @click="handleCategorySearch('Drinks')" class="button is-primary drinks-button">Drinks</button>
        <button @click="handleCategorySearch('Breakfast')" class="button is-primary breakfast-button">Breakfast</button>
        <button @click="handleCategorySearch('BBQ')" class="button is-primary bbq-button">BBQ</button>
      </div>
      <div class="button-container">
        <button @click="handleCategorySearch('Salads')" class="button is-primary salads-button">Salads</button>
        <button @click="handleCategorySearch('Soups')" class="button is-primary soups-button">Soups</button>
        <button @click="handleCategorySearch('Vegan')" class="button is-primary vegan-button">Vegan</button>
        <button @click="handleCategorySearch('Mediterranean')" class="button is-primary mediterranean-button">Mediterranean</button>
        <button @click="handleCategorySearch('American')" class="button is-primary american-button">American</button>
      </div>
    </div>

    <div class="recipe-row">
      <router-link v-for="recipe in recipeRowRecipes" :key="recipe.id" :to="'/recipe/' + recipe.id" class="column is-one-fifth">
        <RecipeCard :recipe="recipe"/>
      </router-link>
    </div>

    <router-link to="/recipe/create">
      <a class="float">
        <i class="fa fa-plus my-float"></i>
      </a>
  </router-link>
  </div>

</template>
<script>
  import axios from 'axios';
  import RecipeCard from '../components/RecipeCard';
  import { Carousel, Navigation, Pagination, Slide } from 'vue3-carousel'
  import 'vue3-carousel/dist/carousel.css'
  export default {
    name: 'home',
    components: {
      RecipeCard,
      Carousel,
      Slide,
      Pagination,
      Navigation,
    },
  data() {
    return {
      loading: false,
      carouselRecipes: [],
      rightBoxRecipes: [],
      recipeRowRecipes: [],
    };
  },
  methods: {
    clickItem: function (item) {
      window.alert(item.idx);
    },
    handleCategorySearch(category) {
      this.$router.push({ name: 'SearchView', query: { q: category, tab: 'tag' } });
    },
    async fetchRecommendedRecipes() {
      try {
        this.loading = true;
        const carouselResponse = await axios.get('/api/recommend/hot');
        this.carouselRecipes = carouselResponse.data; // Assuming the response directly provides an array of recipes
        const rightBoxResponse = await axios.get('/api/recommend/random');
        this.rightBoxRecipes = rightBoxResponse.data;
        const token = this.$cookies.get('cookieToken');
        if (token) {
          const response = await axios.post('/api/user/login/check', {}, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
          const uid = response.data;
          const recipeRowResponse = await axios.get('/api/recommend/followed', {
            params: {
              uid: uid, // You need to replace this with actual user ID
            },
          });
          this.recipeRowRecipes = recipeRowResponse.data;
          console.log(this.recipeRowRecipes);
        }
        // Fetch data for the recipe row
      } catch (error) {
        console.error('Error fetching recipes:', error);
      } finally {
        this.loading = false;
      }
    },
  },
  mounted() {
    this.fetchRecommendedRecipes();
  }
  };
</script>
<style lang="scss" scoped>
  .hero {
    text-align: center;
    background-image: url('../assets/background.jpg');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    height: 400px;
  }
  .hero-body .title {
    text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.6);
    padding: 40px 0 20px 0;
    font-size: 60px;
  }
  .title {
    margin-top: 4%;
  }
  .subtitle {
    text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.7);
    font-size: 30px;
  }
  .float{
	position:fixed;
	width:60px;
	height:60px;
	bottom:40px;
	left:40px;
  background: white url("../assets/add.svg") no-repeat 15px center;
  background-size: 30px 30px;
	background-color:#d89761;
	color:#FFF;
	border-radius:50px;
	text-align:center;
	box-shadow: 2px 2px 3px #999;
}
.my-float{
	margin-top:22px;
}

.carousel__item {
  margin-top: 1%;
  height: 400px;
  width: 80%;
  background-position: center;
  background-size: cover;
  text-align: center;
  background-color: rgba(0, 0, 0, 0.35);

  .card-content {
    padding-top: 50px;
    position: absolute;
    color: #fff;
    top: 0;
    padding: 10px;
    height: 400px;
    width: 80%;
    span {
      font-size: 18px;
      text-align: center;
      width: 100%;
      position: absolute;
      bottom: 10px;
      right: 0;
      text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.6);
    }
    h2 {
      margin-top: 50px;
      text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.6);
    }
    .event-date {
    background-color: #151515;
    color: #fff;
    font-size: 0.75em;
    padding: 1% 10px;
    position: absolute;
    top: 14.5px;
    right: 16.5px;
    }
  }
}

.carousel__slide {
  padding: 10px;
}

.carousel__prev,
.carousel__next {
  box-sizing: content-box;
  border: 5px solid white;
}
.leftbox {
  flex: 0 0 45%;
}
.rightbox {
  margin-top: 1%;
  margin-right: 1%;
  flex: 0 0 50%;
}

.boxes-container {
  display: flex;
  justify-content: space-between;
  align-items: stretch;
}

@media (max-width: 768px) {
    .boxes-container {
      flex-direction: column; /* Stack boxes vertically */
    }

    .leftbox{
      width: 100%; /* Make both boxes take up full width */
    }
    .rightbox{
      width: 90%;
      margin-left: 5%;
      margin-right: 5%;
    }
  }

.button-row {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.button-container {
  display: flex;
  flex-wrap: wrap; /* Allow buttons to wrap to the next line */
  justify-content: space-around; /* Center the buttons horizontally */
  width: 80%;
  margin-bottom: 10px;
}

.button-container button {
  flex: 1; /* Allow buttons to grow and shrink */
  margin: 0 5px;
  width: auto; /* Allow buttons to adjust their width */
}

.recipe-row {
  display: flex;
  flex-wrap: wrap; /* Allow recipe cards to wrap to the next line */
  justify-content: space-between; /* Distribute recipe cards evenly */
  margin-top: 20px;
  margin-right: 1%;
  margin-left: 1%;
}

.recipe-row .column {
  width: 18%;
  margin-bottom: 10px;
  margin-right: 1%;
  margin-left: 1%;
}


@media (max-width: 768px) {
  .button-container {
    width: 100%; /* Make the button container take up full width */
  }

  .button-container button {
    margin: 5px; /* Add some margin around buttons */
  }

  .recipe-row .column {
    width: 46%; /* Make recipe cards take up almost half the width */
    margin-right: 4%; /* Add some space between recipe cards */
    margin-left: 0;
  }
}

.asian-button {
    background-color: #f8731b !important; /* Yellow */
  }

  .dessert-button {
    background-color: #ff6666 !important; /* Pink */
  }

  .drinks-button {
    background-color: #ffc966 !important; /* Blue */
  }

  .breakfast-button {
    background-color: #ffcc00 !important; /* Orange */
  }

  .bbq-button {
    background-color: #8B4513 !important; /* Green */
  }

  .salads-button {
    background-color: #055e09 !important; /* Light Blue */
  }

  .soups-button {
    background-color: #f8921d !important; /* Purple */
  }

  .vegan-button {
    background-color: #99cc00 !important; /* Lime Green */
  }

  .mediterranean-button {
    background-color: #ff9966 !important; /* Coral */
  }

  .american-button {
    background-color: #bd8112 !important; /* Lavender */
  }
</style>