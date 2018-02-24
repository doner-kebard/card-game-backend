<template>
    <div id="game">
        <div v-if="status === 'playing'" id="board">
            <div id="my-score"></div>
            <div id="opponent-score"></div>
            <div v-for="row in rows" :key="row.$index" class="game-row">
                <div v-for="card in rows" :key="card.$index" class="card">
                    Power: {{card.power}}
                    Owner: {{card.owner}}
                </div>
            </div>
            <div class="hand">
                <div v-for="card in hand" :key="card.$index" class="card">
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
export default {
  name: "Game",
  data: function() {
    var defaultData = {};
    defaultData.status = "waiting";
    return defaultData;
  },
  computed: {
    joinLink: function() {
      return (
        window.location.hostname +
        ":" +
        window.location.port +
        "/#/join-game" +
        this.$route.params.gameID
      );
    }
  }
};
</script>
