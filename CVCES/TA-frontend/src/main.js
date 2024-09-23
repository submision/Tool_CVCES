import {createApp} from 'vue'
import router from './router';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import {QuillEditor} from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import axios from "axios";
import Vue from 'vue'

const app = createApp(App)
app
	.use(router)
	.use(ElementPlus,QuillEditor)
	.mount('#app');

axios.defaults.withCredentials = true;
