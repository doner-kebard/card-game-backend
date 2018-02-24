import Vue from "vue";
import Router from "vue-router";
import Game from "./components/Game.vue";
import JoinGame from "./components/JoinGame.vue";
import CreateGame from "./components/CreateGame.vue";
import MainView from "./views/Main.vue";

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: "/",
      name: "Home",
      component: MainView
    },
    {
      path: "/create-game/",
      name: "CreateGame",
      component: CreateGame
    },
    {
      path: "/join-game/:joinID",
      name: "JoinGame",
      component: JoinGame
    },
    {
      path: "/game/:game-id/player/:player-id",
      name: "Game",
      component: Game
    }
  ]
});
