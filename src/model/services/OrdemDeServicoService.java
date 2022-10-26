package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.OrdemDeServicoDao;
import model.entities.OrdemDeServico;

public class OrdemDeServicoService {

	private OrdemDeServicoDao dao = DaoFactory.createOrdemDeServicoDao();

	public void insert(OrdemDeServico obj) {
		dao.insert(obj);
	}

	public void update(OrdemDeServico obj) {
		dao.update(obj);
	}

	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	public List<OrdemDeServico> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(OrdemDeServico obj) {
		if (obj.getId() == null) {
			dao.insert(obj);

		} else {
			dao.update(obj);
		}
	}

	public void remove(OrdemDeServico ordemDeServico) {
		dao.deleteById(ordemDeServico.getId());
	}
}
