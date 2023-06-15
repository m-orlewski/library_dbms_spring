import Vue from 'vue'
import VueRouter from 'vue-router'

import BooksPage from '@/components/BooksPage.vue';
import ClientsPage from '@/components/ClientsPage.vue';
import ReservationsPage from '@/components/ReservationsPage.vue';

Vue.use(VueRouter)

const routes = [
  {
     path: '/',
     name: 'Home',
  },
  {
     path: '/home',
     redirect: '/',
  },
  {
    path: '/books',
    name: 'Książki',
    component: BooksPage,
  },
  {
    path: '/clients',
    name: 'Klienci',
    component: ClientsPage,
  },
  {
    path: '/reservations',
    name: 'Rezerwacje',
    component: ReservationsPage,
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
