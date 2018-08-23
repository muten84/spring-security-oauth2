package it.luigibifulco.microservice.cloud.counterservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository extends JpaRepository<CounterBean, Integer> {

}
