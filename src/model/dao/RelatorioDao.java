package model.dao;

import java.util.List;

import model.entities.Relatorio;

public interface RelatorioDao {
	
	
	List<Relatorio>findAll();
	void insert(Relatorio obj);
	void update(Relatorio obj);
	void deleteById(Integer id);
	
	

}
