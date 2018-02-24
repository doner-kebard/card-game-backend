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
            Waiting for opponent
            <div id="join-link"></div>
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
  }
};
</script>
