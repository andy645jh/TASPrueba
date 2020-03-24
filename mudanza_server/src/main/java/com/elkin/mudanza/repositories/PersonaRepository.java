package com.elkin.mudanza.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.elkin.mudanza.entities.Persona;

@Repository
public interface PersonaRepository extends CrudRepository<Persona,Long>{

}
