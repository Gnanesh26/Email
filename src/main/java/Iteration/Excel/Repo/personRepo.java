package Iteration.Excel.Repo;

import Iteration.Excel.Entity.person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface personRepo extends JpaRepository<person, Long> {
    boolean existsByNameAndAgeAndDob(String name, int age, LocalDate dob);

}
