<template>
    <div id="join-game">
    </div>
</template>

<script>
import axios from "axios";
export default {
  name: "JoinGame",
  created: async function() {
    try {
      const gameID = this.$route.params.joinID;
      const response = await axios.post("http://backend:3000/games/" + gameID);
      console.log(response);
      this.$router.push({
        name: "Game",
        params: {
          gameID: gameID,
          playerID: response.data["player-id"]
        }
      });
    } catch (err) {
      throw err;
    }
  }
};
</script>
