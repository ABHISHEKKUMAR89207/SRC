package com.example.jwt.booksystem1.books;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookTableRepository extends JpaRepository<BookTable, Long> {

}
