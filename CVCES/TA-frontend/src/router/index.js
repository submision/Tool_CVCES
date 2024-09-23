import { createRouter, createWebHistory } from 'vue-router'

// 引入需要的模块
import HomeView from '../views/HomeView.vue'
import Gsystemmodel from "@/views/Gsystemmodel";
import Gdeclare from "@/views/Gdeclare";
import Checkproperties from "@/views/Checkproperties";
import Main from "@/views/Main";
import Getdesignmodel from "@/views/Getdesignmodel.vue";
import Getdevicemodel from "@/views/Getdevicemodel.vue";
import GetDependence from "@/views/GetDependence.vue";
import GetSD from "@/views/GetSD.vue";
import GetCD from "@/views/GetCD.vue";
import ShowRDD from "@/views/ShowRDD.vue";
import showRDD from "@/views/ShowRDD.vue";

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,

    children:
        [
            {
                path:'/GCD',
                name:'getcd',
                component: GetCD
            },
            {
              path:'/GSD',
              name:'getsd',
              component: GetSD
            },
            {
                path:'/GRDD',
                name:'getrdd',
                component: showRDD
            },

          {
            path:'/Sys',
            name:'Systemmodel',
            component: Gsystemmodel
          },
            {
                path:'/Des',
                name:'Designmodel',
                component: Getdesignmodel
            },
            {
                path:'/Dev',
                name:'Devicemodel',
                component: Getdevicemodel
            },
          {
            path:'/DC',
            name:'Sysdeclare',
            component: Gdeclare
          },
          {
            path:'/CK',
            name:'Checkproper',
            component: Checkproperties
          },
            {
                path:'/Dep',
                name:'GetDependence',
                component: GetDependence
            },
        ]
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  mode: 'hash',
  base: '/static'
})

export default router
