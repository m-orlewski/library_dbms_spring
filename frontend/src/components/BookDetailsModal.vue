<template>
  <div>
    <b-row>
      <b-col cols="6">
        <b-form-group id="id" label="Id" label-for="id">
            <p class="font-weight-bold">{{ book.id }}</p>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row>
      <b-col cols="6">
        <b-form-group id="title" label="Tytuł" label-for="title">
            <p class="font-weight-bold">{{ book.title }}</p>
        </b-form-group>
      </b-col>
      <b-col cols="6">
        <b-form-group id="author" label="Autor" label-for="author">
            <p class="font-weight-bold">{{ book.author }}</p>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row class="mt-3">
      <b-col cols="6">
        <b-form-group id="genre" label="Gatunek" label-for="genre">
            <p class="font-weight-bold">{{ book.genre }}</p>
        </b-form-group>
      </b-col>
      <b-col cols="6">
        <b-form-group id="quantity" label="Ilość egzemplarzy" label-for="quantity">
            <p class="font-weight-bold">{{ book.quantity }}</p>
        </b-form-group>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import axios from "axios";

export default {
    name: "BookDetailsModal",
    props: {
        bookId: Number,
    },
    data() {
        return {
            book: {},
        };
    },
    mounted() {
        console.log('BookDetailsModal mounted id=' + this.bookId);
        this.getBookById();
    },
    methods: {
        triggerClose() {
            this.$emit("closeBookDetailsModal");
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
    },
};
</script>