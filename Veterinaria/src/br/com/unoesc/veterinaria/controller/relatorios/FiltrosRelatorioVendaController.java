package br.com.unoesc.veterinaria.controller.relatorios;

import java.net.URL;
import java.util.List;

import org.controlsfx.control.textfield.TextFields;

import br.com.unoesc.veterinaria.banco.ClienteBanco;
import br.com.unoesc.veterinaria.banco.VendaBanco;
import br.com.unoesc.veterinaria.banco.conf.ConexaoPrincipal;
import br.com.unoesc.veterinaria.dao.ClienteDao;
import br.com.unoesc.veterinaria.dao.VendaDao;
import br.com.unoesc.veterinaria.model.Venda;
import br.com.unoesc.veterinaria.model.filtros.FiltrosVenda;
import br.com.unoesc.veterinaria.staticos.auxiliares.EstaticosParaCliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class FiltrosRelatorioVendaController {

	private final String MAIOR_QUE = "Maior que ";
	private final String MENOR_QUE = "Menor que ";
	private final String IGUAL_A = "Igual a ";

	@FXML
	private TextField tfCliente;

	@FXML
	private DatePicker dpData;

	@FXML
	private ComboBox<String> cbxTipoRange;

	@FXML
	private TextField tfValorRange;

	@FXML
	private Button btnCancelar;

	@FXML
	private Button btnContinuar;

	@FXML
	private Button btnLimpar;

	private Stage dialogStage;

	private boolean clicadoSalvar;

	private FiltrosVenda filtrosVenda;

	private VendaDao vendaDao = new VendaBanco();

	private ClienteDao clienteDao = new ClienteBanco();

	@FXML
	private void initialize() {
		populaCombo();
		TextFields.bindAutoCompletion(tfCliente, clienteDao.listar());
	}

	@FXML
	void Cancelar(ActionEvent event) {
		dialogStage.close();
	}

	@FXML
	void Continuar(ActionEvent event) {
		validaFiltros();
		geraRelatorio();
		clicadoSalvar = true;
		if (dialogStage != null) {
			dialogStage.close();
		}

	}

	public void geraRelatorio() {
		URL url = getClass().getResource("/br/com/unoesc/veterinaria/relatorios/RelatorioVendas.jasper");
		JasperPrint jasperPrint;
		List<Venda> listaVendas = vendaDao.findByFiltros(filtrosVenda);
		try {
			if (listaVendas != null) {
				JRBeanCollectionDataSource pegaLista = new JRBeanCollectionDataSource(listaVendas);
				jasperPrint = JasperFillManager.fillReport(url.getPath(), null, pegaLista);
			} else {
				jasperPrint = JasperFillManager.fillReport(url.getPath(), null, ConexaoPrincipal.retornaconecao());
			}
			JasperViewer.viewReport(jasperPrint, false);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void Limpar(ActionEvent event) {
		tfCliente.clear();
		tfValorRange.clear();
		cbxTipoRange.setValue(null);
		dpData.setValue(null);
	}

	private FiltrosVenda validaFiltros() {
		filtrosVenda = new FiltrosVenda();
		if (tfCliente.getText() != null) {
			filtrosVenda.setCliente(EstaticosParaCliente.achaClienteByName(tfCliente.getText()));
		}
		if (dpData.getValue() != null) {
			filtrosVenda.setDataVenda(dpData.getValue());
		}

		if (tfValorRange.getText() != null && cbxTipoRange.getValue() != null) {
			String range = null;
			switch (cbxTipoRange.getValue()) {
			case MAIOR_QUE:
				range = ">" + tfValorRange.getText();
				break;
			case MENOR_QUE:
				range = "<" + tfValorRange.getText();
				break;
			case IGUAL_A:
				range = "=" + tfValorRange.getText();
				break;
			}
			filtrosVenda.setTipoCondicaoValor(range);
		} else {
			filtrosVenda.setTipoCondicaoValor("is not null");

		}
		return filtrosVenda;
	}

	private void populaCombo() {
		cbxTipoRange.getItems().add(MAIOR_QUE);
		cbxTipoRange.getItems().add(MENOR_QUE);
		cbxTipoRange.getItems().add(IGUAL_A);
	}

	public void setStageDialog(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean clicadoSalvar() {
		return clicadoSalvar;
	}

}