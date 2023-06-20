<template>
    <div>
        <h2>Lista klientów</h2>

        <b-alert v-model="showSuccessAlert" variant="success" dismissible>
            {{ alertMessage }}
        </b-alert>

        <b-button block id="show-btn" @click="showCreateModal">
            <b-icon-plus class="text-white"></b-icon-plus>
            <span class="h6 text-white">Dodaj klienta</span>
        </b-button>

        <b-table :items="clients" :fields="fields" stacked="md" responsive="sm" striped hover fixed>
            <template #cell(Akcje)="data">
              <b-button size="sm" @click="showUpdateModal(data.item.id)" class="mx-1">Edytuj</b-button>
              <b-button size="sm" variant="danger" @click="showDeleteModal(data.item.id)" class="mx-1">Usuń</b-button>
        </template>
        </b-table>
    
        <b-modal ref="create-client-modal" size="xl" hide-footer title="Nowy klient">
            <create-client-form @closeCreateModal="closeCreateModal" @reloadDataTable="getClientsData" @showSuccessAlert="showAlertCreate"></create-client-form>
        </b-modal>

        <b-modal ref="update-client-modal" size="xl" hide-footer title="Edytuj książkę">
            <update-client-form @closeUpdateModal="closeUpdateModal" @reloadDataTable="getClientsData" @showSuccessAlert="showAlertUpdate" :clientId="clientId"></update-client-form>
        </b-modal>

        <b-modal ref="delete-client-modal" size="md" hide-footer title="Potwierdź usunięcie książki">
            <delete-client-modal @closeDeleteModal="closeDeleteModal" @reloadDataTable="getClientsData" @showSuccessAlert="showAlertDelete" :clientId="clientId"></delete-client-modal>
        </b-modal>
    </div>
</template>
  
<script>

import axios from 'axios';

import CreateClientForm from '@/components/CreateClientForm.vue';
import UpdateClientForm from '@/components/UpdateClientForm.vue';
import DeleteClientModal from '@/components/DeleteClientModal.vue';

export default {
    components: {
        CreateClientForm,
        UpdateClientForm,
        DeleteClientModal,
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
                    key:'firstName',
                    label:'Imię', 
                    sortable: true
                },
                {
                    key:'lastName',
                    label:'Nazwisko',
                    sortable: true},
                {
                    key: 'Akcje',
                }],
            clients: [],
            clientId: Number,
            showSuccessAlert: false,
            alertMessage: '',
        };
    },
    methods: {
        getClientsData() {
            console.log('getClientsData');
            axios.get(this.API_URL + '/clients')
            .then((response) => {
                this.clients = response.data;
            })
            .catch((error) => {
                console.log(error);
            })
        },
        showCreateModal() {
            console.log('Create Form Opened');
            this.$refs["create-client-modal"].show();
        },
        closeCreateModal() {
            console.log('Create Form Closed');
            this.$refs["create-client-modal"].hide();
        },
        showAlertCreate() {
            this.showSuccessAlert = true;
            this.alertMessage = "Klient dodany do bazy";
        },
        showUpdateModal(id) {
            console.log('Update Form Opened for id=' + id);
            this.clientId = id;
            this.$refs["update-client-modal"].show();
        },
        closeUpdateModal() {
            console.log('Update Form Closed');
            this.$refs["update-client-modal"].hide();
        },
        showAlertUpdate() {
            this.showSuccessAlert = true;
            this.alertMessage = "Zaktualizowano klienta";
        },
        showDeleteModal(id) {
            console.log('Delete Form Opened for id=' + id);
            this.clientId = id;
            this.$refs["delete-client-modal"].show();
        },
        closeDeleteModal() {
            console.log('Delete Form Closed');
            this.$refs["delete-client-modal"].hide();
        },
        showAlertDelete() {
            this.showSuccessAlert = true;
            this.alertMessage = "Usunięto klienta";
        }
    },
    created() {
        this.getClientsData()
    }
}
</script>
     
  