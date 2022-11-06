package gui;

import java.net.URL;
import java.time.LocalTime;
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
import model.entities.Relatorio;
import model.services.RelatorioService;

public class RelatorioController implements Initializable, DataChangeListener {

	private RelatorioService relatorioService;
			
	@FXML
	private TableView<Relatorio> tableViewRelatorio;

	@FXML
	private TableColumn<Relatorio, Integer> tableColumnId;

	@FXML
	private TableColumn<Relatorio, String> tableColumnCliente;

	@FXML
	private TableColumn<Relatorio, String> tableColumnVeiculo;

	@FXML
	private TableColumn<Relatorio, Date> tableColumnData;

	@FXML
	private TableColumn<Relatorio, LocalTime> tableColumnHorario;

	@FXML
	private TableColumn<Agendamento, String> tableColumnObservacao;
	
	@FXML
	private TableColumn<Agendamento, String> tableColumnPagamento;
	
	

	private ObservableList<Relatorio> obsList;

	public void setRelatorioService(RelatorioService relatorioService) {
		this.relatorioService = relatorioService;
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
		if (relatorioService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Relatorio> list = relatorioService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewRelatorio.setItems(obsList);

	}

	@Override
	public void onDataChanged() {
		updateTableViewRelatorio();
	}

}
