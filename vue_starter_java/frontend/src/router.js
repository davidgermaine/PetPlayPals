import Vue from 'vue'
import Router from 'vue-router'
import auth from './auth'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import Landing from './views/Landing.vue'
import Logout from './views/Logout.vue'
import Profile from './views/Profile.vue'
import Playdates from './views/Playdates.vue'
import PlaydateSignUp from './views/PlaydateSignUp'
import PlaydateRequests from './views/PlaydateRequests'
import PlaydateRequestDetail from './views/PlaydateRequestDetail'
import PlaydateDetail from './views/PlaydateDetail'


Vue.use(Router)

/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/playdateDetail/:id",
      name: "playdateDetail",
      component: PlaydateDetail,
      meta: {
        requiresAuth: true
      },
    },
    {
      path:"/playdateRequestDetail",
      name: "playdateRequestDetail",
      component: PlaydateRequestDetail,
      props: true,
      meta: {
        requiresAuth: true
      },
    },
    {
      path:"/playdateRequests",
      name: "playdateRequests",
      component: PlaydateRequests,
      meta: {
        requiresAuth: true
      },
    },
    {
      path: "/playdateSignUp/:id",
      name: "playdateSignUp",
      component: PlaydateSignUp,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/playdates",
      name: "playdates",
      component: Playdates,
      meta: {
        requiresAuth: false
      }
    },
    { 
      path: "/profile",
      name: "profile",
      component: Profile,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/logout",
      name: "logout",
      component: Logout,
      meta: {
      requiresAuth: false
      }
      },
    {
      path: '/',
      name: 'landing',
      component: Landing,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/login",
      name: "login",
      component: Login,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/register",
      name: "register",
      component: Register,
      meta: {
        requiresAuth: false
      }
    },
  ]
})

router.beforeEach((to, from, next) => {
  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);
  const user = auth.getUser();

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && !user) {
    next("/login");
  } else {
    // Else let them go to their next destination
    next();
  }
});

export default router;
