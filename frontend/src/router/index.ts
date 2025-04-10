import { createRouter, createWebHistory } from 'vue-router';
import MemeBrowser from '../components/MemeBrowser.vue';
import MemeDetail from '../components/MemeDetail.vue';
import PublicMemeBrowser from '../components/PublicMemeBrowser.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: MemeBrowser
    },
    {
      path: '/public',
      name: 'public-memes',
      component: PublicMemeBrowser
    },
    {
      path: '/meme/:id',
      name: 'meme-detail',
      component: MemeDetail
    }
  ]
});

export default router;