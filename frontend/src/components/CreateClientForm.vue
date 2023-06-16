<template>
  <b-form class="mt-3">
    <b-row>
      <b-col cols="6">
        <b-form-group id="firstName" label="Imię" label-for="firstName">
          <b-form-input
            id="firstName"
            type="text"
            placeholder="Imię"
            v-model="client.firstName"
          ></b-form-input>
        </b-form-group>
      </b-col>
      <b-col cols="6">
        <b-form-group id="lastName" label="Nazwisko" label-for="lastName">
          <b-form-input
            id="lastName"
            type="text"
            placeholder="Nazwisko"
            v-model="client.lastName"
          ></b-form-input>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row class="mt-4">
      <b-col>
        <b-button variant="warning" @click="triggerClose">Zamknij</b-button>
      </b-col>
      <b-col cols="3">
        <b-button variant="primary" class="px-5" @click="addNewClient">Dodaj klienta</b-button>
      </b-col>
    </b-row>
  </b-form>
</template>

<script>
import axios from "axios";

export default {
  name: "CreateClientModal",
  data() {
    return {
      client: {},
    };
  },
  methods: {
    triggerClose() {
      this.$emit("closeCreateModal");
    },
    addNewClient() {
        console.log('addNewClient');
        axios.post(this.API_URL + '/clients', this.client)
            .then((response) => {
                console.log(response.data);
                this.$emit("closeCreateModal");
                this.$emit("reloadDataTable");
                this.$emit("showSuccessAlert");
            })
            .catch((error) => {
                console.log(error);
            });
    },
},
};
</script>