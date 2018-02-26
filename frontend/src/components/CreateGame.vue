<template>
    <div id="create-game">
    </div>
</template>

<script>
import axios from "axios";
export default {
  name: "CreateGame",
  beforeRouteEnter: async function(to, from, next) {
    try {
      const response = await axios.post("http://backend:3000/games");
      console.log(response);
      next({
        name: "Game",
        params: {
          gameID: response.data["game-id"],
          playerID: response.data["player-id"]
        }
      });
    } catch (err) {
      next();
      throw err;
    }
  }
};
</script>
