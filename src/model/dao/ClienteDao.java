package model.dao;

import java.util.List;

import model.entities.Cliente;

public interface ClienteDao {

	void insert(Cliente obj);

	void update(Cliente obj);

	void deleteById(Integer id);

	Cliente findByCpf(String cpf);

	List<Cliente> findAll();

}
