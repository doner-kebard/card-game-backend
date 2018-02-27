<template>
    <div id="game">
        <div v-if="response.status != 'Waiting for an opponent'" id="board">
            <div id="my-score">0</div>
            <div id="opponent-score">0</div>
            <div v-for="(row, rownum) in response.rows"
                :key="row.$index"
                class="game-row"
                :rownum="rownum"
                v-on:dragover="allowDrop"
                v-on:drop="dropOnRow">
                <div v-for="card in row"
                    :key="card.$index"
                    :rownum="rownum"
                    v-on:dragover="allowDrop"
                    v-on:drop="dropOnRow"
                    class="card">
                    Power: {{card.power}}
                    Owner: {{card.owner}}
                </div>
            </div>
            <div class="hand">
                <div v-for="(card, index) in response.hand"
                    :key="index"
                    class="card"
                    :index="index"
                    draggable
                    v-on:dragstart="dragCardFromHand">
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
    gameReq: function() {
      return (
        "http://backend:3000/games/" +
        this.$route.params.gameID +
        "/player/" +
        this.$route.params.playerID
      );
    },
    updateGame: async function() {
      try {
        const req = this.gameReq();
        const response = await axios.get(req);
        this.response = response.data;
      } catch (err) {
        throw err;
      }
    },
    dragCardFromHand: function(ev) {
      ev.dataTransfer.setData("handIndex", ev.target.getAttribute("index"));
    },
    allowDrop: function(ev) {
      ev.preventDefault();
    },
    dropOnRow: async function(ev) {
      ev.preventDefault();
      try {
        const req = this.gameReq();
        const data = ev.dataTransfer.getData("handIndex");
        const sentData = { index: data, row: ev.target.getAttribute("rownum") };
        console.log(ev.target);
        console.log(sentData);
        const response = await axios.post(req, sentData);
        console.log(response);
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
    this.intervalID = setInterval(
      function() {
        this.updateGame();
      }.bind(this),
      1000
    );
  },
  beforeRouteLeave: function(to, from, next) {
      clearInterval(this.intervalID);
      next();
  }
};
</script>

<style>
.game-row {
  height: 900px;
  width: 100%;
}
</style>
