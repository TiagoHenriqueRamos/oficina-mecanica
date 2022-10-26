package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.VeiculoDao;
import model.entities.Veiculo;

public class VeiculoService {

	private VeiculoDao dao = DaoFactory.createVeiculoDao();

	public List<Veiculo> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Veiculo obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void insert(Veiculo obj) {
		dao.insert(obj);
	}
	
	public void remove(Veiculo obj) {
		dao.deleteByPlaca(obj.getPlaca());
	}

	
}
