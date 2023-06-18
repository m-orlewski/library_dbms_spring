<template>
    <div>
        <h2>Lista rezerwacji</h2>

        <b-alert v-model="showSuccessAlert" variant="success" dismissible>
            {{ alertMessage }}
        </b-alert>

        <b-alert v-model="showFailureAlert" variant="danger" dismissible>
            {{ alertMessage }}
        </b-alert>

        <b-button block id="show-btn" @click="showCreateModal">
            <b-icon-plus class="text-white"></b-icon-plus>
            <span class="h6 text-white">Dodaj rezerwację</span>
        </b-button>

        <b-table :items="reservations" :fields="fields" stacked="md" responsive="sm" striped hover fixed>
            <template #cell(actions1)="data">
              <b-icon-book class="action-item mx-3" variant="primary" @click="showBookDetails(data.item.book.id)"></b-icon-book>
              <b-icon-person class="action-item mx-3" variant="primary" @click="showClientDetails(data.item.client.id)"></b-icon-person>
            </template>
            <template #cell(actions2)="data">
              <b-button size="sm" @click="showUpdateModal(data.item.id)" class="mx-1">Edytuj</b-button>
              <b-button size="sm" variant="danger" @click="showDeleteModal(data.item.id)" class="mx-1">Usuń</b-button>
            </template>
        </b-table>

        <b-modal ref="book-details-modal" size="xl" hide-footer title="Szczegóły książki">
            <book-details-modal @closeBookDetailsModal="closeBookDetailsModal" :bookId="bookId"></book-details-modal>
        </b-modal>

        <b-modal ref="client-details-modal" size="xl" hide-footer title="Szczegóły klienta">
            <client-details-modal @closeClientDetailsModal="closeClientDetailsModal" :clientId="clientId"></client-details-modal>
        </b-modal>
    
        <b-modal ref="create-reservation-modal" size="xl" hide-footer title="Nowa rezerwacja">
            <create-reservation-form @closeCreateModal="closeCreateModal" @reloadDataTable="getReservationsData" @showSuccessAlert="showAlertCreate" @showFailureAlert="showAlertCreateFailed"></create-reservation-form>
        </b-modal>

        <b-modal ref="update-reservation-modal" size="xl" hide-footer title="Edytuj rezerwację">
            <update-reservation-form @closeUpdateModal="closeUpdateModal" @reloadDataTable="getReservationsData" @showSuccessAlert="showAlertUpdate" @showFailureAlert="showAlertCreateFailed" :reservationId="reservationId"></update-reservation-form>
        </b-modal>

        <b-modal ref="delete-reservation-modal" size="md" hide-footer title="Potwierdź usunięcie rezerwacji">
            <delete-reservation-modal @closeDeleteModal="closeDeleteModal" @reloadDataTable="getReservationsData" @showSuccessAlert="showAlertDelete" :reservationId="reservationId"></delete-reservation-modal>
        </b-modal>
    </div>
</template>
  
<script>

import axios from 'axios';

import BookDetailsModal from '@/components/BookDetailsModal.vue';
import ClientDetailsModal from '@/components/ClientDetailsModal.vue';
import CreateReservationForm from '@/components/CreateReservationForm.vue';
import UpdateReservationForm from '@/components/UpdateReservationForm.vue';
import DeleteReservationModal from '@/components/DeleteReservationModal.vue';

export default {
    components: {
        BookDetailsModal,
        ClientDetailsModal,
        CreateReservationForm,
        UpdateReservationForm,
        DeleteReservationModal,
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
                    key:'dueDate',
                    label:'Data rezerwacji',
                    sortable: true
                },
                {
                    key:'returnDate',
                    label:'Data zwrotu',
                    sortable: true
                },
                {
                    key: 'actions1',
                },
                {
                    key: 'actions2',
                }],
            reservations: [],
            reservationId: Number,
            bookId: Number,
            clientId: Number,
            showSuccessAlert: false,
            showFailureAlert: false,
            alertMessage: '',
        };
    },
    methods: {
        getReservationsData() {
            console.log('getReservationsData');
            axios.get(this.API_URL + '/reservations')
            .then((response) => {
                this.reservations = response.data;
            })
            .catch((error) => {
                console.log(error);
            })
        },
        showBookDetails(id) {
            console.log('showBookDetails for id=' + id)
            this.bookId = id;
            this.$refs["book-details-modal"].show();
        },
        closeBookDetailsModal() {
            console.log('closeBookDetails')
            this.$refs["book-details-modal"].hide();
        },
        showClientDetails(id) {
            console.log('showClientDetails for id=' + id)
            this.clientId = id;
            this.$refs["client-details-modal"].show();
        },
        closeClientDetailsModal() {
            console.log('closeClientDetails')
            this.$refs["client-details-modal"].hide();
        },
        showCreateModal() {
            console.log('Create Form Opened');
            this.$refs["create-reservation-modal"].show();
        },
        closeCreateModal() {
            console.log('Create Form Closed');
            this.$refs["create-reservation-modal"].hide();
        },
        showAlertCreate() {
            this.showSuccessAlert = true;
            this.showFailureAlert = false;
            this.alertMessage = "Rezerwacja dodana do bazy";
        },
        showAlertCreateFailed() {
            console.log('showAlertCreateFailed');
            this.showFailureAlert = true;
            this.showSuccessAlert = false;
            this.alertMessage = "Nowa rezerwacja konfliktuje z obecnymi";
        },
        showUpdateModal(id) {
            console.log('Update Form Opened for id=' + id);
            this.reservationId = id;
            this.$refs["update-reservation-modal"].show();
        },
        closeUpdateModal() {
            console.log('Update Form Closed');
            this.$refs["update-reservation-modal"].hide();
        },
        showAlertUpdate() {
            this.showSuccessAlert = true;
            this.showFailureAlert = false;
            this.alertMessage = "Zaktualizowano rezerwację";
        },
        showDeleteModal(id) {
            console.log('Delete Form Opened for id=' + id);
            this.reservationId = id;
            this.$refs["delete-reservation-modal"].show();
        },
        closeDeleteModal() {
            console.log('Delete Form Closed');
            this.$refs["delete-reservation-modal"].hide();
        },
        showAlertDelete() {
            this.showSuccessAlert = true;
            this.showFailureAlert = false;
            this.alertMessage = "Usunięto rezerwację";
        }
    },
    created() {
        this.getReservationsData()
    }
}
</script>
     
  