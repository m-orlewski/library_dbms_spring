<template>
  <b-navbar toggleable="lg" type="dark" variant="primary">
    <b-container>
      <b-row class="text-white">
        <b-col>
          <span class="h4">Library DBMS</span>
        </b-col>
        <b-col>
          <b-button @click="$router.push('/books')">Książki</b-button>
        </b-col>
        <b-col>
          <b-button @click="$router.push('/clients')">Klienci</b-button>
        </b-col>
        <b-col>
          <b-button @click="$router.push('/reservations')">Rezerwacje</b-button>
        </b-col>
      </b-row>
    </b-container>
  </b-navbar>
</template>

<script>
export default {
  name: "HeaderBar",
};
</script>