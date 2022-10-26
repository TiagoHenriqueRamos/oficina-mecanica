package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listener.DataChangeListener;
import gui.utils.Alerts;
import gui.utils.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.OrdemDeServico;
import model.entities.Veiculo;
import model.services.AgendamentoService;
import model.services.ClienteService;
import model.services.OrdemDeServicoService;
import model.services.VeiculoService;

public class OrdemDeServicoController implements Initializable, DataChangeListener {

	@SuppressWarnings("unused")
	private AgendamentoService agendamentoService;

	private OrdemDeServicoService ordemDeServicoService;
	
	@FXML
	private TableView<OrdemDeServico> tableViewOrdemDeServico;

	@FXML
	private TableColumn<OrdemDeServico, Integer> tableColumnId;

	@FXML
	private TableColumn<OrdemDeServico, String> tableColumnCliente;

	@FXML
	private TableColumn<OrdemDeServico, String> tableColumnVeiculo;

	@FXML
	private TableColumn<OrdemDeServico, Date> tableColumnData;

	@FXML
	private TableColumn<OrdemDeServico, Veiculo> tableColumnHorario;

	@FXML
	private TableColumn<OrdemDeServico, Veiculo> tableColumnObservacao;

	@FXML
	private TableColumn<OrdemDeServico, OrdemDeServico> tableColumnFinalizar;

	private ObservableList<OrdemDeServico> obsList;
	
	
	
	public void setServices(AgendamentoService agendamentoService, OrdemDeServicoService ordemDeServicoService) {
		this.agendamentoService = agendamentoService;
		this.ordemDeServicoService = ordemDeServicoService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initailizeNodes();

	}
	@Override
	public void onDataChanged() {
		updateTableViewOrdemDeServico();
	}

	private void initailizeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnCliente.setCellValueFactory(new PropertyValueFactory<>("Cliente"));
		tableColumnVeiculo.setCellValueFactory(new PropertyValueFactory<>("Veiculo"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("Data"));
		Utils.formatTableColumnDate(tableColumnData, "dd/MM/yyyy");
		tableColumnHorario.setCellValueFactory(new PropertyValueFactory<>("Horario"));
		tableColumnObservacao.setCellValueFactory(new PropertyValueFactory<>("Observacao"));
		initiFinalizarButtons();

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrdemDeServico.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableViewOrdemDeServico() {
		if (ordemDeServicoService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<OrdemDeServico> list = ordemDeServicoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewOrdemDeServico.setItems(obsList);

	}

	
	private void initiFinalizarButtons() {
		tableColumnFinalizar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnFinalizar.setCellFactory(param -> new TableCell<OrdemDeServico, OrdemDeServico>() {
			private final Button button = new Button("Finalizar");

			@Override
			protected void updateItem(OrdemDeServico obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> finalizarEntity(obj, event));
			}
		});
	}

	private void finalizarEntity(OrdemDeServico obj, ActionEvent event) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Deseja mesmo finalizar?");

		if (result.get() == ButtonType.OK) {
			if (ordemDeServicoService == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				serviceDone(obj, "/gui/OrdemDeServicoFormulario.fxml", Utils.currentStage(event));
				updateTableViewOrdemDeServico();

			} catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao finalizar serviço", null, e.getMessage(), AlertType.ERROR);

			}
		}

	}

	private synchronized void serviceDone(OrdemDeServico obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			OrdemDeServicoFormulario controller = loader.getController();
			controller.setEntidade(obj);
			controller.setServices(new VeiculoService(), new ClienteService(), new OrdemDeServicoService());
			controller.loadAssociateObjects();
			controller.subscribeDataChengeListener(this);
			controller.updateFormData();

			Stage cadastroStage = new Stage();
			cadastroStage.setTitle("Finalizar");
			cadastroStage.setScene(new Scene(pane));
			cadastroStage.setResizable(false);
			cadastroStage.initOwner(parentStage);
			cadastroStage.initModality(Modality.WINDOW_MODAL);
			cadastroStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a página!", e.getMessage(), AlertType.ERROR);
		}
	}


}
