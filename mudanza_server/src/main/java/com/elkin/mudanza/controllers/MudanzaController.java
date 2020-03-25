package com.elkin.mudanza.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.InputStreamResource;

import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.elkin.mudanza.entities.Persona;
import com.elkin.mudanza.entities.ScheduleJob;
import com.elkin.mudanza.services.PersonaService;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE,
		RequestMethod.PUT })
public class MudanzaController {
	
	@Autowired
	private PersonaService personaService;	
	
	public static final String UPLOAD_LOCATION = "C:\\java-exec\\upload-dir\\";	
	
	@PostConstruct
	public void init() {
		Path uploadLocation = Paths.get(UPLOAD_LOCATION);
		try {
			Files.createDirectories(uploadLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage", e);
		}
	}	

	@RequestMapping(value = "/mudanza", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	private List<Persona> getAllPersonas() 
	{
		return personaService.getAll();
	}

	private String ProccessFileData(List<Integer> numbers) {
		int index = 0;

		List<ScheduleJob> listJobs = new ArrayList<ScheduleJob>();
		ScheduleJob schedule = new ScheduleJob();
		int days = numbers.get(index); // dias

		index++;

		do {

			schedule = new ScheduleJob();

			// cantidad de items
			int cantidad = numbers.get(index);
			index++;

			int max = index + cantidad;
			List<Integer> items = new ArrayList<>();
			for (int i = index; i < max; i++) {
				items.add(numbers.get(i));
				index++;
			}

			//ordenar
			Comparator<Integer> c = Collections.reverseOrder();
			Collections.sort(items, c);
			
			// agregar items y procesar
			schedule.setItems(items);
			schedule.ProccesJob();
			
			listJobs.add(schedule);

		} while (index < numbers.size());

		String result = "";
		int caseNumber = 0;
		for (ScheduleJob scheduleTemp : listJobs)
		{
			caseNumber++;
			result += "Case #" + caseNumber + ":" + scheduleTemp.getDuration() + "\n";
		}

		return result;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity readFile(@RequestParam("file") MultipartFile file, int cedula) {

		List<Integer> numList = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();
		String result = "";

		try {
			
			InputStream f = file.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(f));

			String line;
			while ((line = br.readLine()) != null) {
				numList.add(Integer.parseInt(line));
				sb.append(line + System.lineSeparator());
			}
			
			result = ProccessFileData(numList);	
			
			Persona p = new Persona();
			p.setCedula(cedula);
			p.setFecha(new Date());
			p.setResultado(result);
			
			personaService.save(p);			
			
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		
		//retornamos resultado aunq no se use
		return ResponseEntity.ok(result);
	}	

	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Object> createFileAndDownload(@PathVariable Long id) throws IOException {
		
		//obtener persona
		Persona persona = personaService.findById(id);
		
		File fileTemp = new File(UPLOAD_LOCATION + "output_"+persona.getCedula()+".txt");
		
		// crear archivos
		boolean existe = fileTemp.createNewFile();
		
		// verificar existencia
		if (existe) {
			System.out.println("File is created!");
		} else {
			System.out.println("File already exists.");
		}
				
		
		// escribir contenido
		FileWriter writer = new FileWriter(fileTemp);
		writer.write(persona.getResultado());
		writer.close();

		InputStreamResource resource = new InputStreamResource(new FileInputStream(fileTemp));
		HttpHeaders headers = new HttpHeaders();

		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileTemp.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(fileTemp.length())
				.contentType(MediaType.parseMediaType("application/txt")).body(resource);

		return responseEntity;
	}
}
