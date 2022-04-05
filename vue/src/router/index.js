import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/ManagerInterface.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'ManagerInterface',
    component: () => import('../views/ManagerInterface.vue'),
    redirect: "/home",
    children: [
      {path: 'home', name: 'Home', meta: {indexName: "首页"}, component: () => import('../views/Home.vue')},
      {path: 'user', name: 'User', meta: {indexName: "用户管理"}, component: () => import('../views/User.vue')},
      {path: 'settings', name: 'Settings', meta: {indexName: "系统设置"}, component: () => import('../views/Settings.vue')},
      {path: 'log', name: 'Log', meta: {indexName: "使用记录"}, component: () => import('../views/Log.vue')},
      {path: 'classroom', name: 'Classroom', meta: {indexName: "教室管理"}, component: () => import('../views/Classroom.vue')}
    ]
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
