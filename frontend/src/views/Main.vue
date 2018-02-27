<template>
    <div id="main-view">
        <router-link id="create-game"
            :to="{ name: 'CreateGame' }"
            class="btn btn-primary">
            Create Game
        </router-link>
    </div>
</template>

<script>
export default {
  name: "MainView"
};
</script>
