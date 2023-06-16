<template>
    <b-form class="mt-3">
      <b-row>
        <b-col cols="6">
          <b-form-group id="title" label="Tytuł" label-for="title">
            <b-form-input
              id="title"
              type="text"
              placeholder="Tytuł"
              v-model="book.title"
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
            ></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group id="quantity" label="Ilość egzemplarzy" label-for="quantity">
            <b-form-input
              id="quantity"
              type="number"
              placeholder="Ilość egzemplarzy"
              v-model="book.quantity"
            ></b-form-input>
          </b-form-group>
        </b-col>
      </b-row>
      <b-row class="mt-4">
        <b-col>
          <b-button variant="warning" @click="triggerClose">Close</b-button>
        </b-col>
        <b-col cols="3">
          <b-button variant="primary" class="px-5" @click="updateBook">Edytuj książkę</b-button>
        </b-col>
      </b-row>
    </b-form>
  </template>

<script>
import axios from "axios";

export default {
    name: "UpdateBookModal",
    props: {
        bookId: Number,
    },
    data() {
        return {
            book: {},
        };
    },
    mounted() {
        this.getBookById();
    },
    methods: {
        triggerClose() {
            this.$emit("closeUpdateModal");
        },
        getBookById() {
            console.log('getBookById for id=' + this.bookId);
            axios.get(this.API_URL + '/books/' + this.bookId)
                .then((response) => {
                    this.book = response.data;
                })
                .catch((error) => {
                    console.log(error);
                });
        },
        updateBook() {
            console.log('updateBook');
            axios.put(this.API_URL + '/books/' + this.bookId, this.book)
                .then((response) => {
                    console.log(response);
                    this.$emit("closeUpdateModal");
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