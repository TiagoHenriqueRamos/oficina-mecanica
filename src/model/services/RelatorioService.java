package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.RelatorioDao;
import model.entities.Relatorio;

public class RelatorioService {

	private RelatorioDao dao = DaoFactory.createRelatorioDao();

	public void insert(Relatorio obj) {
		dao.insert(obj);
	}

	public void update(Relatorio obj) {
		dao.update(obj);
	}

	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	public List<Relatorio> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Relatorio obj) {
		if (obj.getId() == null) {
			dao.insert(obj);

		} else {
			dao.update(obj);
		}
	}

	public void remove(Relatorio relatorio) {
		dao.deleteById(relatorio.getId());
	}
}
