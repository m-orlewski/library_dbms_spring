<template>
    <div>
        <h2>Lista książek</h2>

        <b-button block id="show-btn" @click="showCreateModal">
            <b-icon-plus class="text-white"></b-icon-plus>
            <span class="h6 text-white">Dodaj książkę</span>
        </b-button>

        <b-table :items="books" :fields="fields" stacked="md" responsive="sm" striped hover fixed>
            <template #cell(actions)="data">
              <b-button size="sm" @click="showUpdateModal(data.item.id)" class="mx-1">Edytuj</b-button>
              <b-button size="sm" variant="danger" @click="showDeleteModal(data.item.id)" class="mx-1">Usuń</b-button>
        </template>
        </b-table>
    </div>
</template>
  
<script>

import axios from 'axios';

let BOOKS_API_URL = 'http://localhost:8085/api/books';

export default {
    data() {
        return {
            fields: [
                {
                    key:'id',
                    label:'ID', 
                    sortable: true
                },
                {
                    key:'title',
                    label:'Tytuł', 
                    sortable: true
                },
                {
                    key:'author',
                    label:'Autor',
                    sortable: true},
                {
                    key:'genre',
                    label:'Gatunek',
                    sortable: true},
                {
                    key:'quantity',
                    label:'Ilość egzemplarzy'
                },
                {
                    key: 'actions',
                }],
            books: [],
        };
    },
    methods: {
        fetchBooks() {
            axios.get(BOOKS_API_URL)
            .then((response) => {
                this.books = response.data;
            })
            .catch((error) => {
                console.log(error);
            })
        },
        showCreateModal() {
            console.log('Create Form');
        },
        showUpdateModal(id) {
            console.log('Update element with id=' + id);
        },
        showDeleteModal(id) {
            console.log('Delete element with id=' + id);
        },
    },
    created() {
        this.fetchBooks()
    }
}
</script>
     
  