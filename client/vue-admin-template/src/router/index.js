import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
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

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '仪表盘',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '仪表盘', icon: 'dashboard' }
    }]
  },
  {
    path: '/teacher',
    component: Layout,
    redirect: '/teacher/list',
    name: '讲师管理',
    meta: { title: '讲师管理', icon: 'el-icon-user' },
    children: [{
      path: 'list',
      name: 'list',
      component: () => import('@/views/teacher/index'),
      meta: { title: '讲师列表', icon: 'el-icon-s-help' }
    },
    {
      path: 'save',
      name: 'save',
      component: () => import('@/views/teacher/form'),
      meta: { title: '添加讲师', icon: 'el-icon-document-add' }
    },
    {
      path: 'edit',
      name: 'edit',
      component: () => import('@/views/teacher/form'),
      meta: { title: '编辑讲师', icon: 'el-icon-edit-outline' },
      hidden: true
    },
    ]
  },
  {
    path: '/subject',
    component: Layout,
    redirect: '/subject/list',
    children: [
      {
        path: 'list',
        name: '科目管理',
        component: () => import('@/views/subject/index'),
        meta: { title: '科目管理', icon: 'tree' }
      }
    ]
  },
  {
    path: '/course',
    component: Layout,
    redirect: '/course/tree',
    name: '课程管理',
    meta: { title: '课程管理', icon: 'el-icon-reading' },
    children: [{
      path: 'tree',
      name: 'course-list',
      component: () => import('@/views/course/tree'),
      meta: { title: '课程目录', icon: 'el-icon-notebook-1' }
    },
    {
      path: 'list',
      name: 'course-list',
      component: () => import('@/views/course/index'),
      meta: { title: '课程列表', icon: 'el-icon-tickets' }
    },
    {
      path: 'save',
      name: 'course-save',
      component: () => import('@/views/course/form'),
      meta: { title: '添加课程', icon: 'el-icon-document-add' }
    }
      ,
    {
      path: 'edit',
      name: 'course-edit',
      component: () => import('@/views/course/form'),
      meta: { title: '编辑课程', icon: 'el-icon-edit-outline' },
      hidden: true
    }
    ]
  },
  {
    path: '/chapter',
    component: Layout,
    redirect: '/chapter/list',
    name: '章节管理',
    meta: { title: '章节管理', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'list',
        name: 'chapter-list',
        component: () => import('@/views/chapter/index'),
        meta: { title: '章节列表', icon: 'el-icon-office-building' }
      },
      {
        path: 'save',
        name: 'chapter-save',
        component: () => import('@/views/chapter/form'),
        meta: { title: '添加章节', icon: 'el-icon-document-add' },
        hidden: true
      },
      {
        path: 'edit',
        name: 'chapter-edit',
        component: () => import('@/views/chapter/form'),
        meta: { title: '编辑章节', icon: 'el-icon-edit-outline' },
        hidden: true
      }
    ]
  },
  {
    path: '/video',
    component: Layout,
    redirect: '/video/list',
    name: '视频管理',
    meta: { title: '视频管理', icon: 'el-icon-film' },
    children: [
      {
        path: 'list',
        name: 'video-list',
        component: () => import('@/views/video/index'),
        meta: { title: '视频列表', icon: 'el-icon-film' }
      },
      {
        path: 'save',
        name: 'video-save',
        component: () => import('@/views/video/form'),
        meta: { title: '添加视频', icon: 'el-icon-document-add' },
        hidden: true
      },
      {
        path: 'edit',
        name: 'video-edit',
        component: () => import('@/views/video/form'),
        meta: { title: '编辑视频', icon: 'el-icon-edit-outline' },
        hidden: true
      }
    ]
  },
  {
    path: '/comment',
    component: Layout,
    redirect: '/comment/list',
    name: '评论管理',
    meta: { title: '评论管理', icon: 'el-icon-chat-dot-square' },
    children: [
      {
        path: 'list',
        name: 'comment-list',
        component: () => import('@/views/comment/index'),
        meta: { title: '评论列表', icon: 'el-icon-chat-dot-square' }
      },
      {
        path: 'save',
        name: 'comment-report',
        component: () => import('@/views/comment/index2'),
        meta: { title: '举报处理', icon: 'el-icon-phone-outline' }
      },
    ]
  },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/list',
    name: '订单管理',
    meta: { title: '订单管理', icon: 'el-icon-bank-card' },
    children: [
      {
        path: 'list',
        name: 'order-unpaid',
        component: () => import('@/views/order/unpaid'),
        meta: { title: '未支付', icon: 'el-icon-shopping-cart-2' }
      },
      {
        path: 'save',
        name: 'order-paid',
        component: () => import('@/views/order/paid'),
        meta: { title: '已支付', icon: 'el-icon-sold-out' }
      },
    ]
  },
  {
    path: '/statistics',
    component: Layout,
    redirect: '/statistics/register',
    name: '统计分析',
    meta: { title: '统计分析', icon: 'el-icon-s-data' },
    children: [
      {
        path: 'register',
        name: 'statistics-register',
        component: () => import('@/views/statistics/user-register'),
        meta: { title: '注册人数', icon: 'el-icon-s-custom' }
      },
      {
        path: 'course',
        name: 'statistics-course',
        component: () => import('@/views/statistics/new-course'),
        meta: { title: '新增课程', icon: 'el-icon-data-analysis' }
      },
    ]
  },
  {
    path: '/banner',
    component: Layout,
    redirect: '/banner/list',
    children: [
      {
        path: 'list',
        name: 'banner-list',
        component: () => import('@/views/banner/index'),
        meta: { title: 'banner管理', icon: 'el-icon-menu' }
      }
    ]
  },
  {
    path: '/config',
    component: Layout,
    redirect: '/config/list',
    children: [
      {
        path: 'list',
        name: 'config-list',
        component: () => import('@/views/config/index'),
        meta: { title: '系统配置', icon: 'el-icon-setting' }
      }
    ]
  },
 
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
