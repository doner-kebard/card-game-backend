<template>
    <div id="main-view">
        <router-link id="create-game" to="{ name: 'CreateGame' }">
            Create Game
        </router-link>
    </div>
</template>

<script>
import router from "@/router";

export default {
  router,
  name: "MainView"
};
</script>
