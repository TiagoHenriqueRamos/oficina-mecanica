package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listener.DataChangeListener;
import gui.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Agendamento;
import model.entities.OrdemDeServico;
import model.entities.Veiculo;
import model.services.AgendamentoService;
import model.services.OrdemDeServicoService;

public class RelatorioController implements Initializable, DataChangeListener {

	private AgendamentoService agendamentoService;
	
	@SuppressWarnings("unused")
	private OrdemDeServicoService ordemDeServicoService;
	
	@FXML
	private TableView<Agendamento> tableViewRelatorio;

	@FXML
	private TableColumn<Agendamento, Integer> tableColumnId;

	@FXML
	private TableColumn<Agendamento, String> tableColumnCliente;

	@FXML
	private TableColumn<Agendamento, String> tableColumnVeiculo;

	@FXML
	private TableColumn<Agendamento, Date> tableColumnData;

	@FXML
	private TableColumn<Agendamento, Veiculo> tableColumnHorario;

	@FXML
	private TableColumn<Agendamento, Veiculo> tableColumnObservacao;
	
	@FXML
	private TableColumn<Agendamento, Agendamento> tableColumnPagamento;

	

	private ObservableList<Agendamento> obsList;

	public void setRelatorioService(AgendamentoService agendamentoService,
			OrdemDeServicoService ordemDeServicoService) {
		this.agendamentoService = agendamentoService;
		this.ordemDeServicoService = ordemDeServicoService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initailizeNodes();

	}

	private void initailizeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnCliente.setCellValueFactory(new PropertyValueFactory<>("Cliente"));
		tableColumnVeiculo.setCellValueFactory(new PropertyValueFactory<>("Veiculo"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("Data"));
		Utils.formatTableColumnDate(tableColumnData, "dd/MM/yyyy");
		tableColumnHorario.setCellValueFactory(new PropertyValueFactory<>("Horario"));
		tableColumnObservacao.setCellValueFactory(new PropertyValueFactory<>("Observacao"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewRelatorio.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableViewRelatorio() {
		if (agendamentoService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Agendamento> list = agendamentoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewRelatorio.setItems(obsList);

	}

	@Override
	public void onDataChanged() {
		updateTableViewRelatorio();
	}

}
