package com.dev.backend;

import com.dev.backend.model.Book;
import com.dev.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>{

    /**
     * Metoda zwracająca listę rezerwacji na określoną książkę
     * @param book książka której mają dotyczyć rezerwacje
     * @return lista rezerwacji
     */
    List<Reservation> findByBookAndIdNot(Book book, int excludedId);
}
