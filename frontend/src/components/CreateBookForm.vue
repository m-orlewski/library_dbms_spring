<template>
  <b-form class="mt-3" @submit="addNewBook">
    <b-row>
      <b-col cols="6">
        <b-form-group id="title" label="Tytuł" label-for="title">
          <b-form-input
            id="title"
            type="text"
            placeholder="Tytuł"
            v-model="book.title"
            required
          ></b-form-input>
        </b-form-group>
      </b-col>
      <b-col cols="6">
        <b-form-group id="author" label="Autor" label-for="author">
          <b-form-input
            id="author"
            type="text"
            placeholder="Autor"
            v-model="book.author"
            required
          ></b-form-input>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row class="mt-3">
      <b-col cols="6">
        <b-form-group id="genre" label="Gatunek" label-for="genre">
          <b-form-input
            id="genre"
            type="text"
            placeholder="Gatunek"
            v-model="book.genre"
            required
          ></b-form-input>
        </b-form-group>
      </b-col>
      <b-col cols="6">
        <b-form-group id="quantity" label="Ilość egzemplarzy" label-for="quantity">
          <b-form-input
            id="quantity"
            type="number"
            min=1
            placeholder="Ilość egzemplarzy"
            v-model="book.quantity"
            required
          ></b-form-input>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row class="mt-4">
      <b-col>
        <b-button variant="warning" @click="triggerClose">Zamknij</b-button>
      </b-col>
      <b-col cols="3">
        <b-button type='submit' variant="primary" class="px-5">Dodaj książkę</b-button>
      </b-col>
    </b-row>
  </b-form>
</template>

<script>
import axios from "axios";

export default {
  name: "CreateBookModal",
  data() {
    return {
      book: {},
      errorMessage: '',
    };
  },
  methods: {
    triggerClose() {
      this.$emit("closeCreateModal");
    },
    addNewBook() {
        console.log('addNewBook');

        axios.post(this.API_URL + '/books', this.book)
            .then((response) => {
                console.log(response.data);
                this.$emit("closeCreateModal");
                this.$emit("reloadDataTable");
                this.$emit("showSuccessAlert");
            })
            .catch((error) => {
                console.log(error);
            });
    }
},
};
</script>