package com.elkin.mudanza.entities;

import java.util.List;


public class ScheduleJob {
		
	private int cantidad;
	private List<Integer> items;
	

	public ScheduleJob()
	{
		
	}
	
	public void ProccesJob()
	{			
		this.cantidad = 0;
		while(items.size()>0)
		{					
			int val = items.remove(0);
			if(val < 60 && items.size()>0)
			{		
				backTracking(items, val, val);
			}
			this.cantidad++;
		}			
	}
	
	private void backTracking(List<Integer> list, int val, int acum)
	{
		list.remove(list.size()-1);
		acum += val;
		if(acum < 60 && list.size()>0) backTracking(list, val, acum);		
	}

	
	public int getDuration() {
		return cantidad;
	}

	public void setDuration(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public List<Integer> getItems() {
		return items;
	}

	public void setItems(List<Integer> items) {
		this.items = items;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cantidad;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScheduleJob other = (ScheduleJob) obj;
		if (cantidad != other.cantidad)
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScheduleJob [cantidad=" + cantidad + ", items=" + items + "]";
	}
	


}
