package com.elkin.mudanza.services;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elkin.mudanza.entities.Persona;
import com.elkin.mudanza.repositories.PersonaRepository;

@Service
public class PersonaService implements IPersona {
	@Autowired
	private PersonaRepository repository;
	
	@PostConstruct
	private void init()
	{
		for(int i=0; i<10; i++)
		{
			Persona p = new Persona();
			p.setCedula(i);
			p.setFecha(new Date());
			repository.save(p);
		}
	}
	
	@Override
	public Persona save(Persona p) {
		return repository.save(p);
	}

	@Override
	public List<Persona> getAll() {		
		return (List<Persona>) repository.findAll();
	}
	
}
