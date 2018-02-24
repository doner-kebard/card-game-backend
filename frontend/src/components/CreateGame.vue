<template>
    <div id="create-game">
    </div>
</template>

<script>
import axios from "axios";
export default {
  name: "CreateGame",
  beforeRouteEnter: async function(to, from, next) {
    const response = await axios.post("http://localhost:3000/games");
    next({
      name: "Game",
      params: {
        gameID: response.data["game-id"],
        playerID: response.data["player-id"]
      }
    });
  }
};
</script>
