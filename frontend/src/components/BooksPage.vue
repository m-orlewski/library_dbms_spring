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

        <b-modal ref="update-book-modal" size="xl" hide-footer title="Edytuj książkę">
            <update-book-form @closeUpdateModal="closeUpdateModal" @reloadDataTable="getBooksData" @showSuccessAlert="showAlertUpdate" :bookId="bookId"></update-book-form>
        </b-modal>

        <b-modal ref="delete-book-modal" size="md" hide-footer title="Potwierdź usunięcie książki">
            <delete-book-modal @closeDeleteModal="closeDeleteModal" @reloadDataTable="getBooksData" @showSuccessAlert="showAlertDelete" :bookId="bookId"></delete-book-modal>
        </b-modal>
    </div>
</template>
  
<script>

import axios from 'axios';

import CreateBookForm from '@/components/CreateBookForm.vue';
import UpdateBookForm from '@/components/UpdateBookForm.vue';
import DeleteBookModal from '@/components/DeleteBookModal.vue';

export default {
    components: {
        CreateBookForm,
        UpdateBookForm,
        DeleteBookModal,
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
            bookId: Number,
            showSuccessAlert: false,
            alertMessage: '',
        };
    },
    methods: {
        getBooksData() {
            console.log('getBooksData');
            axios.get(this.API_URL + '/books')
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
            console.log('Update Form Opened for id=' + id);
            this.bookId = id;
            this.$refs["update-book-modal"].show();
        },
        closeUpdateModal() {
            console.log('Update Form Closed');
            this.$refs["update-book-modal"].hide();
        },
        showAlertUpdate() {
            this.showSuccessAlert = true;
            this.alertMessage = "Zaktualizowano książkę";
        },
        showDeleteModal(id) {
            console.log('Delete Form Opened for id=' + id);
            this.bookId = id;
            this.$refs["delete-book-modal"].show();
        },
        closeDeleteModal() {
            console.log('Delete Form Closed');
            this.$refs["delete-book-modal"].hide();
        },
        showAlertDelete() {
            this.showSuccessAlert = true;
            this.alertMessage = "Usunięto książkę";
        }
    },
    created() {
        this.getBooksData()
    }
}
</script>
     
  