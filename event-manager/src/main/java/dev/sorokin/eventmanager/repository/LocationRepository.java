package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

}