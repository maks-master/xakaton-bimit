package ru.xakaton.bimit.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.xakaton.bimit.model.Model;

@Repository
public interface ModelRepository extends CrudRepository<Model, UUID> {

}
