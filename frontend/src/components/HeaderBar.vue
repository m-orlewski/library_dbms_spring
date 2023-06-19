<template>
  <b-navbar toggleable="lg" type="dark" variant="primary">
    <b-container>
      <b-row class="text-white">
        <b-col>
          <span class="h4">Library DBMS</span>
        </b-col>
        <b-col>
          <b-button @click="navigateTo('/books')">Książki</b-button>
        </b-col>
        <b-col>
          <b-button @click="navigateTo('/clients')">Klienci</b-button>
        </b-col>
        <b-col>
          <b-button @click="navigateTo('/reservations')">Rezerwacje</b-button>
        </b-col>
      </b-row>
    </b-container>
  </b-navbar>
</template>

<script>
export default {
  methods: {
    navigateTo(path) {
      this.$router.push(path).catch(()=>{});
    }
  }
};
</script>