<template>
    <div id="game">
        <div v-if="response.status !== 'Waiting for an opponent'" id="board">
            <div id="my-score"></div>
            <div id="opponent-score"></div>
            <div v-for="row in response.rows" :key="row.$index" class="game-row">
                <div v-for="card in row" :key="card.$index" class="card">
                    Power: {{card.power}}
                    Owner: {{card.owner}}
                </div>
            </div>
            <div class="hand">
                <div v-for="card in response.hand" :key="card.$index" class="card">
                    Power: {{card.power}}
                </div>
            </div>
        </div>
        <div v-else id="waiting-for-opponent">
            <div id="game-status">
                Waiting for opponent
            </div>
            <div v-if="joinLink" id="join-link">{{joinLink}}</div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
export default {
  name: "Game",
  data: function() {
    var defaultData = {};
    defaultData.response = {};
    return defaultData;
  },
  methods: {
    updateGame: async function() {
      try {
        const req = (
          "http://backend:3000/games/" +
            this.$route.params.gameID +
            "/player/" +
            this.$route.params.playerID
        );
        console.log(req);
        const response = await axios.get(
          req
        );
        this.response = response.data;
        console.log(this.response);
      } catch (err) {
        throw err;
      }
    }
  },
  computed: {
    joinLink: function() {
      const loc = window.location;
      return (
        loc.protocol +
        "//" +
        loc.hostname +
        ":" +
        loc.port +
        "/#/join-game/" +
        this.$route.params.gameID
      );
    }
  },
  created: function() {
    this.updateGame();
    setInterval(
      function() {
        this.updateGame();
      }.bind(this),
      3000
    );
  }
};
</script>
