<template>
  <div id="app" class="text-center container">
    <h1 class="display-1">
        <router-link :to="{ name: 'Home' }">
            Troll Card Game
        </router-link>
    </h1>
    <router-view/>
  </div>
</template>

<script>
import MainView from "@/views/Main.vue";

export default {
  name: "App",
  components: {
    MainView
  }
};
</script>
