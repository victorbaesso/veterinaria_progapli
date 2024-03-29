package br.com.unoesc.veterinaria.controller.cadastro;

import br.com.unoesc.veterinaria.banco.TipoAnimalBanco;
import br.com.unoesc.veterinaria.dao.TipoAnimalDao;
import br.com.unoesc.veterinaria.model.TipoAnimal;
import br.com.unoesc.veterinaria.staticos.auxiliares.EstaticosParaGeral;
import br.com.unoesc.veterinaria.staticos.auxiliares.EstaticosParaTipoAnimal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerCadastroTipoAnimal {

	@FXML
	private Label lblMaster;

	@FXML
	private TextField tfNome;

	@FXML
	private Button btnCancelar;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnLimpar;

	private TipoAnimal tipoAnimal;
	private Stage dialogStage;
	private boolean clicadoSalvar;
	private TipoAnimalDao tipoAnimalDao = new TipoAnimalBanco();

	@FXML
	private void initialize() {

		if (EstaticosParaTipoAnimal.isEditando) {
			this.tipoAnimal = EstaticosParaTipoAnimal.tipoAnimal;
			populaTela();
			lblMaster.setText("Editando Tipo Animal");
		}
	}

	@FXML
	void cancelar(ActionEvent event) {
		limpaTela();
		dialogStage.close();
	}

	@FXML
	void limpar(ActionEvent event) {
		limpaTela();
	}

	@FXML
	void salvar(ActionEvent event) {

		if (populaTipoAnimal()) {
			if (EstaticosParaTipoAnimal.isEditando) {

				tipoAnimalDao.alterar(tipoAnimal);
				EstaticosParaTipoAnimal.isEditando = false;
			} else {
				tipoAnimalDao.inserir(tipoAnimal);
			}
			clicadoSalvar = true;
			if (dialogStage != null) {
				dialogStage.close();
			}
		}

	}

	public void populaTela() {
		tfNome.setText(tipoAnimal.getNome());
	}

	public boolean populaTipoAnimal() {
		if (tfNome.getText().isEmpty()) {
			EstaticosParaGeral.chamaErroNaoPreenchido(dialogStage);
			return false;
		} else {
			tipoAnimal = new TipoAnimal();
			tipoAnimal.setNome(tfNome.getText());
			tipoAnimal.setIdTipoAnimal(EstaticosParaTipoAnimal.tipoAnimal.getIdTipoAnimal());
			return true;
		}
	}

	public void limpaTela() {
		tfNome.clear();
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean clicadoSalvar() {
		return clicadoSalvar;
	}
}
