<template>
    <div>
        <h2>Lista książek</h2>

        <b-alert v-model="showSuccessAlert" variant="success" dismissible>
            {{ alertMessage }}
        </b-alert>

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
    
        <b-modal ref="create-book-modal" size="xl" hide-footer title="Nowa książka">
            <create-book-form @closeCreateModal="closeCreateModal" @reloadDataTable="getBooksData" @showSuccessAlert="showAlertCreate"></create-book-form>
        </b-modal>
    </div>
</template>
  
<script>

import axios from 'axios';

import CreateBookForm from '@/components/CreateBookForm.vue';

let BOOKS_API_URL = 'http://localhost:8085/api/books';

export default {
    components: {
        CreateBookForm,
    },
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
            showSuccessAlert: false,
            alertMessage: '',
        };
    },
    methods: {
        getBooksData() {
            console.log('getBooksData');
            axios.get(BOOKS_API_URL)
            .then((response) => {
                this.books = response.data;
            })
            .catch((error) => {
                console.log(error);
            })
        },
        showCreateModal() {
            console.log('Create Form Opened');
            this.$refs["create-book-modal"].show();
        },
        closeCreateModal() {
            console.log('Create Form Closed');
            this.$refs["create-book-modal"].hide();
        },
        showAlertCreate() {
            this.showSuccessAlert = true;
            this.alertMessage = "Książka dodana do bazy";
        },
        showUpdateModal(id) {
            console.log('Update element with id=' + id);
        },
        showDeleteModal(id) {
            console.log('Delete element with id=' + id);
        },
    },
    created() {
        this.getBooksData()
    }
}
</script>
     
  