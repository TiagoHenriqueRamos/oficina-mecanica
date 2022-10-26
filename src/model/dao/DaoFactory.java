package model.dao;

import db.DB;
import model.dao.impl.AgendamentoDaoJDBC;
import model.dao.impl.ClienteDaoJDBC;
import model.dao.impl.OrdemDeServicoDaoJDBC;
import model.dao.impl.VeiculoDaoJDBC;

public class DaoFactory {

	public static ClienteDao createClienteDao() {
		return new ClienteDaoJDBC(DB.getConnection());
	}

	public static VeiculoDao createVeiculoDao() {
		return new VeiculoDaoJDBC(DB.getConnection());
	}

	public static AgendamentoDao createAgendamentoDao() {
		return new AgendamentoDaoJDBC(DB.getConnection());

	}

	public static OrdemDeServicoDao createOrdemDeServicoDao() {
		return new OrdemDeServicoDaoJDBC(DB.getConnection());
	}
}
