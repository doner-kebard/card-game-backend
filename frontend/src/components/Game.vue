<template>
    <div id="game">
        <div v-if="response.status != 'Waiting for an opponent'"
            id="board"
            class="row">
            <!-- my-score should show how many rows I'm currently winnning in -->
            <div id="my-score" class="col text-right">0</div>
            <!-- opponent-score should show how many rows I'm currently losing in -->
            <div id="opponent-score" class="col text-left">0</div>
            <div class="w-100"></div>
            <div id="rows" class="col-12">
                <div v-for="(row, rownum) in response.rows"
                    :key="row.$index"
                    class="game-row row border"
                    style="height: 50px"
                    :rownum="rownum"
                    v-on:dragover="allowDrop"
                    v-on:drop="dropOnRow">
                    <div v-for="card in row"
                        :key="card.$index"
                        :rownum="rownum"
                        class="card col-1">
                        {{card.power}}
                        {{card.owner | cut}}
                    </div>
                </div>
            </div>
            <div class="col-12">
                <div class="hand row">
                    <div v-for="(card, index) in response.hand"
                        :key="index"
                        class="card col-1"
                        :index="index"
                        draggable
                        v-on:dragstart="dragCardFromHand">
                        {{card.power}}
                    </div>
                </div>
            </div>
        </div>
        <div v-else id="waiting-for-opponent" class="row">
            <div id="game-status" class="col-12">
                Waiting for opponent
            </div>
            <div v-if="joinLink" id="join-link" class="col-12">{{joinLink}}</div>
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
    dropOnRow: function(ev) {
      ev.preventDefault();
      const req = this.gameReq();
      const data = ev.dataTransfer.getData("handIndex");
      const sentData = { index: data, row: ev.target.getAttribute("rownum") };
      axios.post(req, sentData);
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
    this.response = {};
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
  },
  filters: {
    cut: function(word) {
      return word.slice(0, 3);
    }
  }
};
</script>
