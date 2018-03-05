<template>
    <div id="join-game">
    </div>
</template>

<script>
import axios from "axios";
export default {
  name: "JoinGame",
  methods: {
    fetchGame: async function(gameID) {
      return await axios.post("http://backend:3000/games/" + gameID);
    }
  },
  created: async function() {
    const gameID = this.$route.params.joinID;
    const response = await this.fetchGame(gameID);
    this.$router.push({
      name: "Game",
      params: {
        gameID: gameID,
        playerID: response.data["player-id"]
      }
    });
  }
};
</script>
