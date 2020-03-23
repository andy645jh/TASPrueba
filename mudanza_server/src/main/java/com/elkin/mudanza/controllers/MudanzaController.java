package com.elkin.mudanza.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.elkin.mudanza.entities.ScheduleJob;


@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE,RequestMethod.PUT })
public class MudanzaController 
{
	int[] numeros = new int[]{5,4,30,30,1,1,3,20,20,20,11,1,2,3,4,5,6,7,8,9,10,11,6,9,19,29,39,49,59,10,32,56,76,8,
			44,60,47,85,71,91};
	
	@RequestMapping(value = "/mudanza", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> findAll() 
	{
				
		int index = 0;
		
		List<ScheduleJob> list = new ArrayList<ScheduleJob>();
		ScheduleJob schedule = new ScheduleJob();
		int days = numeros[index];
		//dias
		index++;	
		
		do {
				
			schedule = new ScheduleJob();
			//schedule.setItems(Procces(index));
			//cantidad de items
			int cantidad = numeros[index];
			index++;
			
			
			int max = index+cantidad;
			List<Integer> items = new ArrayList<>();
			for(int i=index; i<max; i++)
			{
				items.add(numeros[i]);
				index++;
			}
			
			Comparator<Integer> c = Collections.reverseOrder();
			Collections.sort(items,c);
			schedule.setItems(items);
			schedule.ProccesJob();
			list.add(schedule);
			
		}while(index<numeros.length);	
			
		String result ="";
		int caseNumber=0;
		for (ScheduleJob scheduleTemp : list)
		{
			caseNumber++;
			result += "Case #"+caseNumber+":" + scheduleTemp.getDuration()+"\n";
		}
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/mudanza/pro", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	private String Procces()
	{
		//9,19,29,39,49,59
		List<Integer> items = new ArrayList<>();
		/*items.add(30);
		items.add(30);
		items.add(1);
		items.add(1);*/
		
		/*items.add(20);
		items.add(20);
		items.add(20);*/
		
		/*items.add(11);
		items.add(10);
		items.add(9);
		items.add(8);
		items.add(7);		
		items.add(6);
		items.add(5);
		items.add(4);
		items.add(3);
		items.add(2);
		items.add(1);	*/
		
		/*items.add(59);
		items.add(49);
		items.add(39);
		items.add(29);	
		items.add(19);
		items.add(9);*/
		
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
		while(items.size()>0)
		{					
			int val = items.remove(0);
			if(val < 60 && items.size()>0)
			{		
				backTracking(items, val, val);
			}
			cantidad++;
		}
		
		return "Case #4 = " + cantidad;		
	}
	
	private void backTracking(List<Integer> list, int val, int acum)
	{
		list.remove(list.size()-1);
		acum += val;
		if(acum < 60 && list.size()>0) backTracking(list, val, acum);		
	}
	
	@RequestMapping(value = "/mudanza/file", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String readFile(@RequestParam("file") MultipartFile file) 
	{
		List<Integer> numList = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();
		
		try {
			
			InputStream f = file.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(f));

			String line;
			while ((line = br.readLine()) != null) {
				numList.add(Integer.parseInt(line));
				sb.append(line + System.lineSeparator());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
