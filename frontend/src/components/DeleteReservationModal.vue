<template>
  <div>
    <b-row class="mt-2 mb-3">
      <h6 class="text-secondary">
        Czy na pewno chcesz usunąć rezerwację z bazy danych?
      </h6>
    </b-row>
    <b-row class="mt-2 mb-3">
      <p class="text-danger">
        Ta operacja jest nieodwracalna!
      </p>
    </b-row>
    <b-row class="mt-4">
      <b-col>
        <b-button variant="warning" @click="triggerClose">Zamknij</b-button>
      </b-col>
      <b-col>
        <b-button variant="danger" @click="deleteReservation">Usuń rezerwację</b-button>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import axios from "axios";

export default {
    name: "DeleteReservationModal",
    props: {
        reservationId: Number,
    },
    data() {
        return {
            reservation: {},
        };
    },
    methods: {
        triggerClose() {
            this.$emit("closeDeleteModal");
        },
        deleteReservation() {
            console.log('DeleteReservation');
            axios.delete(this.API_URL + '/reservations/' + this.reservationId)
                .then((response) => {
                    console.log(response);
                    this.$emit("closeDeleteModal");
                    this.$emit("reloadDataTable");
                    this.$emit("showSuccessAlert");
                })
                .catch((error) => {
                    console.log(error);
                })
        },
    },
};
</script>