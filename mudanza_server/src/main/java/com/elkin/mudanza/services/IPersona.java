package com.elkin.mudanza.services;

import java.util.List;

import com.elkin.mudanza.entities.Persona;

public interface IPersona {
	Persona save(Persona p);
	List<Persona> getAll();	
}
