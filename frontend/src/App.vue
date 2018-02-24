<template>
  <div id="app">
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
