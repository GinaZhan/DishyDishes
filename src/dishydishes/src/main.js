import { createAuth0 } from "@auth0/auth0-vue";
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import 'bulma/css/bulma.css'
import { Quasar } from "quasar";
import '@quasar/extras/material-icons/material-icons.css'
import 'quasar/src/css/index.sass'
import VueCookies from 'vue-cookies'

const app = createApp(App).use(router);

app.use(
  createAuth0({
    domain: "dev-24306jitwlww6cel.us.auth0.com",
    clientId: "O2iD9Ls4WI64y6VSRIE5eBRIDpcELt0V",
    authorizationParams: {
      redirect_uri: window.location.origin
    }
  })
);
app.use(Quasar);

app.use(VueCookies)

app.mount("#app");