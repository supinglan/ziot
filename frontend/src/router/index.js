import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

export const baseRoutes = [
  {
    path: '/login',
    component: () => import('@/pages/login/index'),
    hidden: true
  },

  {
    path: '/register',
    component: () => import('@/pages/register/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/pages/404/index'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [{
      path: '/home',
      name: 'home',
      component: () => import('@/pages/home/index'),
      meta: { title: '主页', icon: 'home' }
    }]

  },

  {
    path: '/userCenter',
    component: Layout,
    redirect: '/userCenter',
    children: [{
      path: '/userCenter',
      name: 'userCenter',
      component: () => import('@/pages/user/index'),
      meta: { title: '个人中心', icon: 'user' }
    }]
  },

  {
    path: '/device',
    component: Layout,
    redirect: '/device',
    children: [{
      path: '/device',
      name: 'device',
      component: () => import('@/pages/device/index'),
      meta: { title: '设备列表', icon: 'el-icon-setting' }
    }]
  },

  {
    path: '/info/:id',
    component: Layout,
    redirect: '/info/:id',
    hidden: true,
    children: [
      {
        path: '/info/:id',
        name: 'info',
        component: () => import('@/pages/message/index'),
        meta: { title: '设备消息', icon: 'el-icon-chat-dot-square' }
      }
    ]
  },

  {
    path: '/location/:id',
    component: Layout,
    redirect: '/location/:id',
    hidden: true,
    children: [
      {
        path: '/location/:id',
        name: 'location',
        component: () => import('@/pages/location/index'),
        meta: { title: '设备位置', icon: 'el-icon-map-location' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: baseRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
