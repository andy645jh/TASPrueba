package com.elkin.mudanza.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.elkin.mudanza.entities.Persona;
import com.elkin.mudanza.entities.ScheduleJob;
import com.elkin.mudanza.services.PersonaService;
import com.elkin.mudanza.services.StorageService;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT })
public class MudanzaController 
{
	@Autowired
	private StorageService storageService;
	
	@Autowired
    private ServletContext servletContext;
	
	@Autowired
    private PersonaService personaService;
	 
	int[] numeros = new int[] { 5, 4, 30, 30, 1, 1, 3, 20, 20, 20, 11, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 6, 9, 19, 29,
			39, 49, 59, 10, 32, 56, 76, 8, 44, 60, 47, 85, 71, 91 };

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

			Comparator<Integer> c = Collections.reverseOrder();
			Collections.sort(items, c);
			schedule.setItems(items);
			schedule.ProccesJob();
			listJobs.add(schedule);

		} while (index < numbers.size());

		String result = "";
		int caseNumber = 0;
		for (ScheduleJob scheduleTemp : listJobs) {
			caseNumber++;
			result += "Case #" + caseNumber + ":" + scheduleTemp.getDuration() + "\n";
		}

		return result;
	}

	@RequestMapping(value = "/mudanza/pro", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	private String Procces() {
		// 9,19,29,39,49,59
		List<Integer> items = new ArrayList<>();
		/*
		 * items.add(30); items.add(30); items.add(1); items.add(1);
		 */

		/*
		 * items.add(20); items.add(20); items.add(20);
		 */

		/*
		 * items.add(11); items.add(10); items.add(9); items.add(8); items.add(7);
		 * items.add(6); items.add(5); items.add(4); items.add(3); items.add(2);
		 * items.add(1);
		 */

		/*
		 * items.add(59); items.add(49); items.add(39); items.add(29); items.add(19);
		 * items.add(9);
		 */

		items.add(91);
		items.add(85);
		items.add(76);
		items.add(71);
		items.add(60);
		items.add(56);
		items.add(44);
		items.add(47);
		items.add(32);
		items.add(8);

		int cantidad = 0;
		while (items.size() > 0) {
			int val = items.remove(0);
			if (val < 60 && items.size() > 0) {
				backTracking(items, val, val);
			}
			cantidad++;
		}

		return "Case #4 = " + cantidad;
	}

	private void backTracking(List<Integer> list, int val, int acum) {
		list.remove(list.size() - 1);
		acum += val;
		if (acum < 60 && list.size() > 0)
			backTracking(list, val, acum);
	}

	@RequestMapping(value = "/mudanza/up", method = RequestMethod.POST)
	public ResponseEntity readFile(@RequestParam("file") MultipartFile file, int cedula) {
		
		List<Integer> numList = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();
		
		
		try {
			//saveUploadedFiles(file);
			InputStream f = file.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(f));
			
			String line;
			while ((line = br.readLine()) != null) {
				numList.add(Integer.parseInt(line));
				sb.append(line + System.lineSeparator());
			}
						
			Persona p = new Persona();
			p.setCedula(cedula);
			p.setFecha(new Date());
			personaService.save(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		String result = ProccessFileData(numList);
		/*File tempFile = new File("/temp/job.txt");
		try {
			FileWriter myWriter = new FileWriter(tempFile);
			myWriter.write(result);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}*/

		return ResponseEntity.ok(result);
		/*return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tempFile.getName() + "\"")
				.body(tempFile);*/

	}

	@RequestMapping(value = "/dl", method = RequestMethod.GET, produces =MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> downloadPDFFile()
	        throws IOException {

		String testJson= "Prueba";
		InputStream is = new ByteArrayInputStream(testJson.getBytes());

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content - Disposition", "attachment; filename = test.json");
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");
	    
	    return ResponseEntity
	            .ok()	            
	            .headers(headers)	            
	            .contentType(MediaType.parseMediaType("application/octet-stream"))
	            .body(new InputStreamResource(is));
	}
	
    
    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public ResponseEntity uploadToLocalFileSystem(@RequestParam("file") MultipartFile file, @RequestParam("test") String test) {
    	String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    	Path path = Paths.get(fileName);
    	try {
    		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
    			.path("/files/download/")
    			.path(fileName)
    			.toUriString();
    	return ResponseEntity.ok(test);
    }
    
    private static final String EXTERNAL_FILE_PATH = "C:/fileDownloadExample/";

	@RequestMapping("/file/{fileName:.+}")
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) throws IOException {

		File file = new File(EXTERNAL_FILE_PATH + fileName);
		if (file.exists()) {

			//get the mimetype
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				//unknown mimetype so set the mimetype to application/octet-stream
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);
			
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			 //Here we have mentioned it to show as attachment
			 //response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());

		}
	}
	
	@RequestMapping(value = "/files/upload", method = RequestMethod.POST)
	public void handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
		storageService.store(file);
		
		InputStream is = file.getInputStream();
		//IOUtils.copy(is, response.getOutputStream());
		//response.flushBuffer();
		String fileName =  file.getOriginalFilename();
		MediaType mediaType = getMediaTypeForFileName(this.servletContext, fileName);
        System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + mediaType);
 
        File fileTemp = new File(StorageService.UPLOAD_LOCATION + fileName);
 
        // Content-Type
        // application/pdf
        response.setContentType("application/txt");
 
        // Content-Disposition
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
 
        // Content-Length
        response.setContentLength((int) file.getSize());
 
        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(fileTemp));
        BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
 
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();
        inStream.close();
        
        response.flushBuffer();
    
	}
	
	public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        // application/pdf
        // application/xml
        // image/gif, ...
        String mineType = servletContext.getMimeType(fileName);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
	
	@RequestMapping(value="/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
	 Resource file = storageService.loadAsResource("input_server.txt");
	 return ResponseEntity.ok()
	 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
	 .body(file);
	}
}
