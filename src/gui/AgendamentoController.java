package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
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
import model.entities.Agendamento;
import model.entities.Cliente;
import model.entities.Veiculo;
import model.services.AgendamentoService;
import model.services.ClienteService;
import model.services.VeiculoService;

public class AgendamentoController implements Initializable, DataChangeListener {

	private AgendamentoService agendamentoService;
	
	@FXML
	private TableView<Agendamento> tableViewAgendamento;

	@FXML
	private TableColumn<Agendamento, Integer> tableColumnId;

	@FXML
	private TableColumn<Agendamento, Cliente> tableColumnCliente;

	@FXML
	private TableColumn<Agendamento, Veiculo> tableColumnVeiculo;

	@FXML
	private TableColumn<Agendamento, Date> tableColumnData;

	@FXML
	private TableColumn<Agendamento, LocalTime> tableColumnHorario;

	@FXML
	private TableColumn<Agendamento, String> tableColumnObservacao;

	@FXML
	private TableColumn<Agendamento, Agendamento> tableColumnREMOVE;
	
	@FXML
	private TableColumn<Agendamento, Agendamento> tableColumnEDIT;
	
	@FXML
	private Button btNovo;

	private ObservableList<Agendamento> obsList;

	public void setEntidade(Agendamento obj) {
		
	}
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Agendamento obj = new Agendamento();
		createDialogForm(obj, "/gui/AgendamentoFormulario.fxml", parentStage);
	}

	public void setAgendamentoService(AgendamentoService agendamentoService) {
		this.agendamentoService = agendamentoService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initailizeNodes();

	}

	private void initailizeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("Data"));
		Utils.formatTableColumnDate(tableColumnData, "dd/MM/yyyy");
		tableColumnHorario.setCellValueFactory(new PropertyValueFactory<>("Horario"));
		tableColumnObservacao.setCellValueFactory(new PropertyValueFactory<>("Observacao"));
		tableColumnCliente.setCellValueFactory(new PropertyValueFactory<>("Cliente"));
		tableColumnVeiculo.setCellValueFactory(new PropertyValueFactory<>("Veiculo"));
		
		
		

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewAgendamento.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableViewAgendamento() {
		if (agendamentoService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Agendamento> list = agendamentoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewAgendamento.setItems(obsList);
		initRemoveButtons();
		initEditButtons();
		
	}

	private void createDialogForm(Agendamento obj, String absoluteName, Stage parentStage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			AgendamentoFormulario controller = loader.getController();
			controller.setEntidade(obj);
			controller.setService(new VeiculoService(), new ClienteService(), new AgendamentoService());
			controller.loadAssociateObjects();
			controller.subscribeDataChengeListener(this);
			controller.updateFormData();

			Stage cadastroStage = new Stage();
			cadastroStage.setTitle("Agendamento Abertura");
			cadastroStage.setScene(new Scene(pane));
			cadastroStage.setResizable(false);
			cadastroStage.initOwner(parentStage);
			cadastroStage.initModality(Modality.WINDOW_MODAL);
			cadastroStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a página!", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableViewAgendamento();
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Agendamento, Agendamento>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Agendamento obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/AgendamentoFormulario.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Agendamento, Agendamento>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Agendamento obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Agendamento obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (agendamentoService == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				agendamentoService.remove(obj);
				updateTableViewAgendamento();

			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);

			}
		}
	}

	
}
