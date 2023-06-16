<template>
  <div>
    <b-row class="mt-2 mb-3">
      <h6 class="text-secondary">
        Czy na pewno chcesz usunąć książkę z bazy danych?
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
        <b-button variant="danger" @click="deleteBook">Usuń książkę</b-button>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import axios from "axios";

export default {
    name: "DeleteBookModal",
    props: {
        bookId: Number,
    },
    data() {
        return {
            book: {},
        };
    },
    methods: {
        triggerClose() {
            this.$emit("closeDeleteModal");
        },
        deleteBook() {
            console.log('DeleteBook');
            axios.delete(this.API_URL + '/books/' + this.bookId)
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