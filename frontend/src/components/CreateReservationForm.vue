<template>
  <b-form class="mt-3" @submit.prevent="onSubmit">

    <p v-if="errors.length">
    <b>Błąd:</b>
    <ul>
      <li v-for="error in errors" :key="error">{{ error }}</li>
    </ul>
    </p>
    
    <b-row class="mt-3">
      <b-col cols="6">
        <b-form-group id="dueDate" label="Data rezerwacji" label-for="dueDate">
          <b-form-input
            id="dueDate"
            type="date"
            v-model="reservation.dueDate"
            required
          ></b-form-input>
        </b-form-group>
      </b-col>
      <b-col cols="6">
        <b-form-group id="returnDate" label="Data zwrotu" label-for="returnDate">
          <b-form-input
            id="returnDate"
            type="date"
            v-model="reservation.returnDate"
            required
          ></b-form-input>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row class="mt-3">
      <b-col cols="6">
        <b-form-group id="bookId" label="Książka" label-for="bookId">
            <b-form-select
                v-model="bookId"
                :options="books"
                class="mb-3"
                value-field="id"
                text-field="fullName"
                disabled-field="notEnabled">
            </b-form-select>
        </b-form-group>
      </b-col>
      <b-col cols="6">
        <b-form-group id="clientId" label="Klient" label-for="clientId">
            <b-form-select
                v-model="clientId"
                :options="clients"
                class="mb-3"
                value-field="id"
                text-field="fullName"
                disabled-field="notEnabled">
            </b-form-select>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row class="mt-4">
      <b-col>
        <b-button variant="warning" @click="triggerClose">Zamknij</b-button>
      </b-col>
      <b-col cols="3">
        <b-button type='submit' variant="primary" class="px-5">Dodaj rezerwację</b-button>
      </b-col>
    </b-row>
  </b-form>
</template>

<script>
import axios from "axios";

export default {
    name: "CreateReservationModal",
    data() {
        return {
            reservation: {},
            books: {},
            clients: {},
            bookId: Number,
            clientId: Number,
            errors: [],
        };
    },
    mounted() {
        this.getBooksData();
        this.getClientsData();
    },
    methods: {
    triggerClose() {
        this.$emit("closeCreateModal");
    },
    addNewReservation() {
        console.log('addNewReservation');

        this.reservation.book = { id: this.bookId };
        this.reservation.client = { id: this.clientId};

        axios.post(this.API_URL + '/reservations', this.reservation)
            .then((response) => {
                console.log(response);
                this.$emit("closeCreateModal");
                this.$emit("reloadDataTable");
                this.$emit("showSuccessAlert");
            })
            .catch((error) => {
                if (error.response.status == 409) {
                  this.$emit("closeCreateModal");
                  this.$emit("reloadDataTable");
                  this.$emit("showFailureAlert");
                }
                else {
                  console.log(error);
                }
            });
    },
    getBooksData() {
        console.log('getBooksData');
        axios.get(this.API_URL + '/books')
        .then((response) => {
            this.books = response.data;

            this.books = this.books.sort((a, b) => {
                if (a.id < b.id) {
                    return -1;
                }
            });

            this.books.forEach(book => {
                book.fullName = book.id + ': ' + book.title;
            });
        })
        .catch((error) => {
            console.log(error);
        })
    },
    getClientsData() {
        console.log('getClientsData');
        axios.get(this.API_URL + '/clients')
        .then((response) => {
            this.clients = response.data;

            this.clients = this.clients.sort((a, b) => {
                if (a.id < b.id) {
                    return -1;
                }
            });

            this.clients.forEach(client => {
                client.fullName = client.id + ': ' + client.firstName[0] + '. ' + client.lastName;
            });
        })
        .catch((error) => {
            console.log(error);
        })
    },
    onSubmit() {
      this.errors = [];
      const dueDate = new Date(this.reservation.dueDate);
      const returnDate = new Date(this.reservation.returnDate);

      if (dueDate < returnDate) {
        this.addNewReservation();
      }
      else {
        this.errors.push("Data zwrotu nie może być późniejsza niż data rezerwacji");
      }
    }
    }
};
</script>