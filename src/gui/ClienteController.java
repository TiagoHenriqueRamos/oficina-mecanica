package gui;

import java.io.IOException;
import java.net.URL;
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
import model.entities.Cliente;
import model.services.ClienteService;

public class ClienteController implements Initializable, DataChangeListener {

	private ClienteService clienteService;

	@FXML
	private TableView<Cliente> tableViewCliente;

	@FXML
	private TableColumn<Cliente, Integer> tableColumnId;

	@FXML
	private TableColumn<Cliente, String> tableColumnNome;

	@FXML
	private TableColumn<Cliente, String> tableColumnCPF;

	@FXML
	private TableColumn<Cliente, String> tableColumnEndereco;

	@FXML
	private TableColumn<Cliente, String> tableColumnTelefone;
	
		
	@FXML
	private TableColumn<Cliente, Cliente> tableColumnEDIT;

	@FXML
	private TableColumn<Cliente, Cliente> tableColumnREMOVE;
	

	@FXML
	private Button btNovo;

	private ObservableList<Cliente> obsList;

	@FXML
	public void onBtNovo(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Cliente obj = new Cliente();
		createDialogForm(obj, "/gui/ClienteFormulario.fxml", parentStage);

	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	@Override
	public void onDataChanged() {
		updateTableViewCliente();
	}

	private void initializeNodes() {

		tableColumnCPF.setCellValueFactory(new PropertyValueFactory<>("CPF"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		tableColumnEndereco.setCellValueFactory(new PropertyValueFactory<>("Endereco"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("Telefone"));
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
	
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCliente.prefHeightProperty().bind(stage.heightProperty());
	}

	
	private void createDialogForm(Cliente obj, String absoluteName, Stage parentStage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ClienteFormulario controller = loader.getController();
			controller.setEntidade(obj);
			controller.setClienteService(new ClienteService());
			controller.subscribeDataChengeListener(this);
			controller.updateFormData();
			
			
			Stage cadastroStage = new Stage();
			cadastroStage.setTitle("Novo Cliente");
			cadastroStage.setScene(new Scene(pane));
			cadastroStage.setResizable(false);
			cadastroStage.initOwner(parentStage);
			cadastroStage.initModality(Modality.WINDOW_MODAL);
			cadastroStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a página!", e.getMessage(), AlertType.ERROR);
		}
	}

	public void updateTableViewCliente() {
		if (clienteService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Cliente> list = clienteService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewCliente.setItems(obsList);
		initEditButtons();
		initRemoveButtons();

	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Cliente obj, boolean empty) {
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

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Cliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/ClienteFormulario.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void removeEntity(Cliente obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (clienteService == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				clienteService.remove(obj);
				updateTableViewCliente();

			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);

			}
		}
	}

}
