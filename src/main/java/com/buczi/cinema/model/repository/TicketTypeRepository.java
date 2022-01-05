package com.buczi.cinema.model.repository;

import com.buczi.cinema.model.entity.TicketType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository extends CrudRepository<TicketType,String> {
}
