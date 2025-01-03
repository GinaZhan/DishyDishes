<template>
  <div class="recipe-single">
    <section class="hero is-primary" :style="{ 'background-image': 'url(' + coverPhoto + ')' }">
      <div class="hero-body" @click="triggerCoverPhotoUploader">
        <div class="container">
          <input class="title" v-model="recipe_name" @click.stop placeholder="Enter your recipe name here! 🥙">
        </div>

        <label for="coverPhotoUploader" class="subtitle">Click to change the cover photo</label>
        <input
          style="display: none;"
          id="coverPhotoUploader"
          type="file" 
          accept="image/*"
          @change="uploadCoverPhoto()"
        />
      </div>
    </section>
    <section>
      <!-- leftbox -->
      <div class="leftbox">
        <!-- description -->
        <section>
          <h2>Description</h2>
          <textarea 
            v-model="description"
            rows="4"
            class="inputbox"
            placeholder="Write a description about your recipe. 🥗">
          </textarea>
        </section>
        <!-- ingredients -->
        <section>
          <h2>Ingredients</h2>
          <table class="ingredients-table">
            <tr v-for="(ingredient, index) in ingredients" :key="index">
              <td><input class="inputbox" v-model="ingredient.name" placeholder="Enter ingredient name"></td>
              <td><input class="inputbox" v-model="ingredient.amount" placeholder="Enter ingredient amount"></td>
              <td><button @click="removeIngredient(index)">Remove</button></td>
            </tr>
            <tr>
              <td><input class="inputbox" v-model="new_ingredient_name" placeholder="Enter ingredient name"></td>
              <td><input class="inputbox" v-model="new_ingredient_amount" placeholder="Enter ingredient amount"></td>
              <td><button @click="addIngredient()">Add</button></td>
            </tr>
          </table>
        </section>
        <!-- recipe contents/steps -->
        <section>
          <h2>Steps</h2>
          <ol>
            <li v-for="(step,index) in steps" :key="index">
                <div style="display: flex;">
                  <div class="step-container">
                    <textarea 
                      v-model="step.text"
                      rows="10"
                      class="inputbox"
                      style="width: 45%;"
                      :placeholder="'Enter your recipe step ' + (index + 1)">
                    </textarea>

                    <img
                      v-if="step.imageUrl"
                      :src="step.imageUrl"
                      class="step-image"
                      @click="discardStepPicture(index)"
                    />

                    <div 
                      v-else class="image-placeholder" 
                      @click="triggerStepPictureUploader(index)">
                      <label :for="'stepPictureUploader'+index">Click to upload an image</label>
                      <input
                        style="display: none;"
                        :id="'stepPictureUploader'+index"
                        type="file"
                        accept="image/*"
                        @change="uploadStepPicture(index)"
                      />
                    </div>
                  </div>
                  <button @click="removeStep(index)">Remove</button>
                </div>
            </li>
          </ol>
          <div style="text-align: right;">
            <button @click="addStep">Add</button>
          </div>
        </section>
      </div>
      <!-- rightbox -->
      <div class="rightbox">
        <section>
          <h2>Related Tags</h2>
          <div style="display: flex; flex-wrap: wrap;">
            <div class="tag-box" v-for="(tag, index) in tags" :key="index">
              {{ tag }}
              <button class="delete-button" @click="removeTag(index)">X</button>
            </div>
          </div>
          <div style="display: flex;margin: 0 5px 50px 5px;">
            <input class="inputbox" placeholder="Add a tag for your recipe" v-model="new_tag">
            <button @click="addTag(new_tag)" style="margin-right: 0;margin-top: 3px;">Add</button>
          </div>
        </section>

        <button class="submit-button" @click="submitRecipe">Submit Recipe</button>
      </div>
    </section>
  </div>
</template>

<script>
  import axios from 'axios';

  export default {
    data() {
      return {
        coverPhoto: "",
        recipe_name : "",
        description : "",
        steps: [{ text: '', imageUrl: '' }],
        ingredients: [], // { name: '', amount: ''}
        tags: [],
        new_ingredient_name: '',
        new_ingredient_amount: '',
        new_tag: ''
      }
    },

    mounted() {
      if (this.$route.params.id) {
        this.fetchData(this.$route.params.id);
      }
    },

    methods: {
      addIngredient() {
        if (this.new_ingredient_name && this.new_ingredient_amount) {
          this.ingredients.push({ name: this.new_ingredient_name, amount: this.new_ingredient_amount });
          // Clear input fields after adding a new ingredient
          this.new_ingredient_name = '';
          this.new_ingredient_amount = '';
        } else {
          alert("Please enter both ingredient name and amount.");
        }
      },
      removeIngredient(index) {
        this.ingredients.splice(index, 1);
      },

      addStep() {
        this.steps.push({
          text: '',
          imageUrl: '',
        });
      },
      removeStep(index) {
        this.steps.splice(index, 1);
      },
      discardStepPicture(index) {
        this.steps[index].imageUrl = null;
      },
      uploadStepPicture(index) {
        const file = event.target.files[0]
        if (file.size / 1024 > 1024*5) {
          alert(`Image size exceeds the maximum limit of 5MB.`);
          return;
        }
        this.steps[index].imageUrl = URL.createObjectURL(file)
      },
      triggerStepPictureUploader(index) {
        const fileInput = this.$el.querySelector(`#stepPictureUploader${index}`);
        fileInput.click();
      },

      uploadCoverPhoto() {
        const file = event.target.files[0]
        if (file.size / 1024 > 1024*5) {
          alert(`Image size exceeds the maximum limit of 5MB.`);
          return;
        }
        this.coverPhoto = URL.createObjectURL(file)
      },
      triggerCoverPhotoUploader() {
        const fileInput = this.$el.querySelector(`#coverPhotoUploader`);
        fileInput.click();
      },

      removeTag(index) {
        this.tags.splice(index, 1);
      },
      addTag(new_tag) {
        new_tag = new_tag.trim()
        if (new_tag) {
          if (!this.tags.includes(new_tag)) this.tags.push(new_tag);
          this.new_tag = '';
        } else {
          alert("Please enter a valid tag.");
        }
      },

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
        })
        .catch(error => {
          alert('Error fetching data, please try again later.\n', error);
          this.$router.go(-1);
        });
      },

      submitRecipe() {
        // Check if recipe name is empty
        if (!this.recipe_name.trim()) {
            alert("Recipe name can't be empty.");
            return;
        }

        if (!this.coverPhoto) {
            alert("Cover photo can't be empty.");
            return;
        }

        // Check if any step text is empty
        for (let i = 0; i < this.steps.length; i++) {
            if (!this.steps[i].text.trim()) {
                alert(`Step ${i + 1} text can't be empty.`);
                return;
            }
        }
        if (this.$route.params.id) {
          this.updateRecipe(this.$route.params.id);
        } else {
          this.createRecipe();
        }
      },

      async convertImageToBase64(imageUrl) {
        try {
          const response = await fetch(imageUrl);
          const blob = await response.blob();
          return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => {
              const base64String = reader.result.split(',')[1];
              resolve(base64String);
            };
            reader.onerror = () => {
              reject(new Error('Error converting image to base64'));
            };
            reader.readAsDataURL(blob);
          });
        } catch (error) {
          console.error('Error fetching image:', error);
          throw error;
        }
      },

      async createRecipe() {
        const coverPhotoBase64 = await this.convertImageToBase64(this.coverPhoto);
        const stepsPromises = this.steps.map(async (step) => ({
            text: step.text,
            imageBase64: step.imageUrl ? await this.convertImageToBase64(step.imageUrl) : null
        }));
        const steps = await Promise.all(stepsPromises);

        axios.post('/api/recipe/upload', {
          name: this.recipe_name,
          coverPhoto: coverPhotoBase64,
          description: this.description,
          steps: steps,
          ingredients: this.ingredients,
          tags: this.tags
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+this.$cookies.get('cookieToken')
          }
        })
        .then(response => {
          const recipeId = response.data.split(":")[1].trim();
          this.$router.push(`/recipe/${recipeId}`);
        })
        .catch(error => {
          alert('Error creating recipe, please login or try again later.\n', error);
        });
      },

      /* TODO: currently we don't have api ready for this.
      async updateRecipe(id) {
        const coverPhotoBase64 = await this.convertImageToBase64(this.coverPhoto);
        const stepsPromises = this.steps.map(async (step) => ({
            text: step.text,
            imageBase64: step.imageUrl ? await this.convertImageToBase64(step.imageUrl) : null
        }));
        const steps = await Promise.all(stepsPromises);

        axios.put(`/api/recipe/${id}`, {
          name: this.recipe_name,
          coverPhoto: coverPhotoBase64,
          description: this.description,
          steps: steps,
          ingredients: this.ingredients,
          tags: this.tags
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+this.$cookies.get('cookieToken')
          }
        })
        .then(response => {
          console.log('Recipe updated:', response.data);
          this.$router.push(`/recipe/${id}`);
        })
        .catch(error => {
          alert('Error updating recipe, please login or try again later.\n', error);
          this.$router.push(`/recipe/${id}`);
        });
      }
      */
    }
  }
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
    cursor: pointer;
  }
  section {
    margin-bottom: 10px;
  }
  .title {
    text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.6);
    font-size: 60px;
    margin-top: 90px;
    margin-bottom: 10px;
    text-align: center;
    background-color: transparent;
    border: none;
    width: 100%;
  }
  .title::placeholder {
    color: white;
  }
  .subtitle {
    text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.7);
    font-size: 30px;
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
  .inputbox{
    width: 100%;
    font-size:medium;
    border: 1px solid #ddd;
    padding: 8px;
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
        text-align: right;

        button {
          margin-top: 3px;
        }
      }
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
    width: 100%;

    .step-image {
      width: 52%;
      margin-left: 3%;
      object-fit: contain;
      cursor: pointer;
    }

    .image-placeholder {
      width: 52%;
      margin-left: 3%;
      aspect-ratio: 1 / 1;
      display: flex;
      align-items: center;
      justify-content: center;
      text-align: center;
      background-color: #eee;
      cursor: pointer;
    }
  }
  .rightbox {
    float:right; 
    width:30%;
    padding-right: 2%;

    .submit-button{
      width: inherit;
      margin: 5px 8px;
      background-color: #749cc1;
      position:fixed;
      bottom:5px;
      right:5px;

      &:hover {
        background-color: #6284a5;
      }
    }
  }
  .tag-box {
    text-align: center;
    margin: 5px;
    padding: 8px;
    background-color: #f0f0f0;
    border: 1px solid #ccc;
    border-radius: 5px;
    width:fit-content;

    .delete-button {
      top: 0px;
      background-color: #f05555;
      border: none;
      border-radius: 50%;
      cursor: pointer;
      width: 18px;
      height: 18px;
      padding: 0;
      margin: 0;
    }
  }
</style>