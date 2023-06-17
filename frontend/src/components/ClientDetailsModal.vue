<template>
  <div>
    <b-row>
      <b-col cols="6">
        <b-form-group id="id" label="Id" label-for="id">
            <p class="font-weight-bold">{{ client.id }}</p>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row>
      <b-col cols="6">
        <b-form-group id="firstName" label="Imię" label-for="firstName">
            <p class="font-weight-bold">{{ client.firstName }}</p>
        </b-form-group>
      </b-col>
      <b-col cols="6">
        <b-form-group id="lastName" label="Nazwisko" label-for="lastName">
            <p class="font-weight-bold">{{ client.lastName }}</p>
        </b-form-group>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import axios from "axios";

export default {
    name: "ClientDetailsModal",
    props: {
        clientId: Number,
    },
    data() {
        return {
            client: {},
        };
    },
    mounted() {
        console.log('ClientDetailsModal mounted id=' + this.clientId);
        this.getClientById();
    },
    methods: {
        triggerClose() {
            this.$emit("closeClientDetailsModal");
        },
        getClientById() {
            console.log('getClientById for id=' + this.clientId);
            axios.get(this.API_URL + '/clients/' + this.clientId)
                .then((response) => {
                    this.client = response.data;
                })
                .catch((error) => {
                    console.log(error);
                });
        },
    },
};
</script>