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
                <div id="row-card-title" class="row">
                    <div id="my-game-row-title"
                        class="col-6 text-center">
                        <h2>My cards</h2>
                    </div>
                    <div id="opponent-game-row-title"
                        class="col-6 text-center">
                        <h2>Opponent's cards</h2>
                    </div>
                </div>
                <div v-for="(row, rownum) in response.rows"
                     :key="row.$index"
                     class="game-row row">
                    <div class="col-6">
                        <div class="row text-center justify-content-end owned-by-me border"
                             v-on:dragover="allowDrop"
                             v-on:drop="dropOnRow"
                             :rownum="rownum"
                             style="height: 50px">
                            <div v-for="card in row"
                                 v-if="card.owner === 'me'"
                                 :key="card.$index"
                                 :rownum="rownum"
                                 class="card col-2">
                                {{card.power}}
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row text-center owned-by-opponent border"
                             style="height: 50px">
                            <div v-for="card in row"
                                 v-if="card.owner !== 'me'"
                                 :key="card.$index"
                                 :rownum="rownum"
                                 class="card col-2">
                                {{card.power}}
                            </div>
                        </div>
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
      const gameID = this.$route.params.gameID;
      const playerID = this.$route.params.playerID;
      return `http://backend:3000/games/${gameID}/player/${playerID}`;
    },
    updateGame: async function() {
      const req = this.gameReq();
      const response = await axios.get(req);
      this.response = response.data;
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
      const playData = {
        index: data,
        row: ev.target.getAttribute("rownum")
      };
      axios.post(req, playData);
    }
  },
  computed: {
    joinLink: function() {
      const l = window.location;
      const gameID = this.$route.params.gameID;
      return `${l.protocol}//${l.hostname}:${l.port}/#/join-game/${gameID}`;
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
