package model.dao;

import java.util.List;

import model.entities.OrdemDeServico;

public interface OrdemDeServicoDao {
	
	
	List<OrdemDeServico>findAll();
	void insert(OrdemDeServico obj);
	void update(OrdemDeServico obj);
	void deleteById(Integer id);
	
	

}
