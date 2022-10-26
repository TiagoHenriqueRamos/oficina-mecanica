package model.dao;

import java.util.List;

import model.entities.Veiculo;

public interface VeiculoDao {

	void insert(Veiculo obj);
	void update(Veiculo obj);
	void deleteByPlaca(String placa);
	Veiculo findByPlaca(String placa);
	List<Veiculo> findAll();
}
