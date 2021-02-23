<template>
  <div v-if="isAuthenticate">
    <b-card no-body
        class="overflow-hidden"
        style="max-width: 540px;"
        border-variant="dark"
        header="Profil">
      <b-row no-gutters>
        <b-col md="4">
          <b-card-img v-bind:src="photo" alt="Profile picture" class="rounded-circle"></b-card-img>
        </b-col>
        <b-col md="8">
          <b-card-body v-bind:title="user.displayName" v-bind:sub-title="user.jobTitle">
            <b-card-text>
              <p>E-mail: {{ user.mail }}</p>
              <p>{{ user.apiData }}</p>
              <b-link v-on:click="logout">Logout</b-link>
            </b-card-text>
          </b-card-body>
        </b-col>
      </b-row>
    </b-card>
  </div>
</template>

<style scoped>
  body {
    padding: 10px;
  }

  .card-img {
    padding: 10px;
  }
</style>

<script>
export default {
  data() {
    return {
      isAuthenticate: false,
      user: null,
      // empty image
      photo: "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7"
    }
  },

  methods: {
    logout () {
      self.isAuthenticate = false;
      window.location = "/web-app/logout"
    }
  },

  // Fetches user data when the component is created.
  created() {
    let self = this;

    this.$http.get('/api/user-info')
    .then(response => {
      self.isAuthenticate = true;
      self.user = response.data;
      self.photo = "data:image/jpeg;base64," + response.data.photo;
    })
  }
}
</script>