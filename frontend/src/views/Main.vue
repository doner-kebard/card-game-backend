<template>
    <div id="main-view">
        <div v-if="state === 'waiting'">
            <div v-if="link" id="join-link">{{this.$router.resolve(link).href}}</div>
            <div id="game-status">Waiting for opponent</div>
        </div>
        <div v-else>
            <a id="create-game" href=# v-on:click="WaitForOpponent">Create Game</a>
        </div>
        <router-view/>
    </div>
</template>

<script>
import router from "@/router";
import axios from "axios";

export default {
  router,
  name: "MainView",
  data: function() {
    return {
      state: "welcome",
      link: ""
    };
  },
  methods: {
    WaitForOpponent: function() {
      this.state = "waiting";
      axios
        .post("http://localhost:3000/games/")
        .then(response => {
          this.link = "join-game/" + response.data["game-id"];
        })
        .catch(() => {});
    }
  }
};
</script>
