import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('../views/AboutView.vue')
  },
  {
    path: '/recipe/:id',
    name: 'RecipeSingle',
    component: () => import('../views/RecipeSingle.vue'),

  },
  {
    path: '/recipe/create',
    name: 'RecipeCreation',
    component: () => import('../views/RecipeCreation.vue'),
  },
  /* /* TODO: currently we don't have api ready for this.
  {
    path: '/recipe/update/:id',
    name: 'RecipeUpdate',
    component: () => import('../views/RecipeCreation.vue'),
  },
  */
  {
    path: '/user/:id',
    name: 'UserProfile',
    component: () => import('../views/UserProfile.vue'),
  },
  {
    path: '/user/login',
    name: 'UserLogin',
    component: () => import('../views/LoginView.vue'),
  },
  {
    path: '/search',
    name: 'SearchView',
    component: () => import('../views/SearchView.vue'),
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
