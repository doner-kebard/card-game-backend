import Vue from "vue";
import Router from "vue-router";
import Game from "./views/Game.vue";

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: "/join-game/:id",
      component: Game
    }
  ]
});
