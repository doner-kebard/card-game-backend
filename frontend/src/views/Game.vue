<template>
    <div id="game">
        <div id="my-score"></div>
        <div id="opponent-score"></div>
        <div class="game-row"></div>
        <div class="game-row"></div>
        <div class="game-row"></div>
        <div class="game-row"></div>
        <div class="game-row"></div>
        <div class="hand">
            <div v-for="card in hand" :key="card.$index" class="card">
                Power: {{card.power}}
            </div>
        </div>
    </div>
</template>

<script>
export default {
  data: function() {
    var defaultData = {};
    defaultData["game-id"] = 0;
    defaultData["hand"] = [];
    for (var i = 0; i < 12; i++) {
      defaultData["hand"].push({ power: 10 });
    }
    defaultData["player-id"] = "";
    defaultData["rows"] = [[], [], [], [], []];
    return defaultData;
  }
};
</script>
