package service;

import com.miage.parcauto.controller.FinanceController;
import com.miage.parcauto.dao.FinanceDao.AlerteAssurance;
import com.miage.parcauto.dao.FinanceDao.AlerteEntretien;
import com.miage.parcauto.dao.FinanceDao.BilanFinancier;
import com.miage.parcauto.dao.FinanceDao.BilanMensuel;
import com.miage.parcauto.dao.FinanceDao.RentabiliteVehicule;
import com.miage.parcauto.service.FinanceService;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires complets pour le contrôleur FinanceController.
 * Couvre tous les scénarios métier, erreurs, permissions et cas limites.
 *
 * @author ParcAuto
 */
class FinanceControllerTest {
    private FinanceService financeService;
    private FinanceController controller;

    // Mocks JavaFX
    private DatePicker dateDebut;
    private DatePicker dateFin;
    private Button btnAfficherBilan;
    private Label lblRecettes;
    private Label lblDepenses;
    private Label lblSolde;
    private Label lblMarge;
    private TableView<FinanceController.BilanMensuelRow> tableEvolution;
    private TableColumn<FinanceController.BilanMensuelRow, String> colMois;
    private TableColumn<FinanceController.BilanMensuelRow, String> colRecettes;
    private TableColumn<FinanceController.BilanMensuelRow, String> colDepenses;
    private TableColumn<FinanceController.BilanMensuelRow, String> colSolde;
    private TableView<AlerteAssurance> tableAlertesAssurance;
    private TableView<AlerteEntretien> tableAlertesEntretien;
    private TableView<FinanceController.RepartitionRow> tableRepartition;
    private TableView<RentabiliteVehicule> tableRentabilite;
    private VBox tcoPane;
    private Label lblTcoVehicule;
    private Label lblTcoCoutTotal;
    private Label lblTcoCoutParKm;
    private Spinner<Integer> spinnerAnnee;
    private Spinner<Integer> spinnerJoursAlerte;
    private Spinner<Integer> spinnerKmEntretien;

    @BeforeEach
    void setUp() {
        financeService = mock(FinanceService.class);
        controller = new FinanceController(financeService);
        // Mocks JavaFX
        dateDebut = new DatePicker();
        dateFin = new DatePicker();
        btnAfficherBilan = new Button();
        lblRecettes = new Label();
        lblDepenses = new Label();
        lblSolde = new Label();
        lblMarge = new Label();
        tableEvolution = new TableView<>();
        colMois = new TableColumn<>();
        colRecettes = new TableColumn<>();
        colDepenses = new TableColumn<>();
        colSolde = new TableColumn<>();
        tableAlertesAssurance = new TableView<>();
        tableAlertesEntretien = new TableView<>();
        tableRepartition = new TableView<>();
        tableRentabilite = new TableView<>();
        tcoPane = new VBox();
        lblTcoVehicule = new Label();
        lblTcoCoutTotal = new Label();
        lblTcoCoutParKm = new Label();
        spinnerAnnee = new Spinner<>();
        spinnerJoursAlerte = new Spinner<>();
        spinnerKmEntretien = new Spinner<>();
        injectFields();
    }

    /**
     * Injection des mocks dans les champs du contrôleur via la réflexion (pour compatibilité inter-package).
     */
    private void injectFields() {
        try {
            setField(controller, "dateDebut", dateDebut);
            setField(controller, "dateFin", dateFin);
            setField(controller, "btnAfficherBilan", btnAfficherBilan);
            setField(controller, "lblRecettes", lblRecettes);
            setField(controller, "lblDepenses", lblDepenses);
            setField(controller, "lblSolde", lblSolde);
            setField(controller, "lblMarge", lblMarge);
            setField(controller, "tableEvolution", tableEvolution);
            setField(controller, "colMois", colMois);
            setField(controller, "colRecettes", colRecettes);
            setField(controller, "colDepenses", colDepenses);
            setField(controller, "colSolde", colSolde);
            setField(controller, "tableAlertesAssurance", tableAlertesAssurance);
            setField(controller, "tableAlertesEntretien", tableAlertesEntretien);
            setField(controller, "tableRepartition", tableRepartition);
            setField(controller, "tableRentabilite", tableRentabilite);
            setField(controller, "tcoPane", tcoPane);
            setField(controller, "lblTcoVehicule", lblTcoVehicule);
            setField(controller, "lblTcoCoutTotal", lblTcoCoutTotal);
            setField(controller, "lblTcoCoutParKm", lblTcoCoutParKm);
            setField(controller, "spinnerAnnee", spinnerAnnee);
            setField(controller, "spinnerJoursAlerte", spinnerJoursAlerte);
            setField(controller, "spinnerKmEntretien", spinnerKmEntretien);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Utilitaire pour injecter un champ privé via la réflexion.
     */
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    /**
     * Utilitaire pour lire un champ privé via la réflexion.
     */
    private static Object getField(Object target, String fieldName) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Bilan financier : cas nominal")
    void testBilanFinancierNominal() throws SQLException {
        dateDebut.setValue(LocalDate.of(2024, 1, 1));
        dateFin.setValue(LocalDate.of(2024, 12, 31));
        BilanFinancier bilan = new BilanFinancier(
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(7000),
                BigDecimal.valueOf(2000),
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(3000),
                LocalDate.of(2024, 1, 1).atStartOfDay(),
                LocalDate.of(2024, 12, 31).atTime(23,59,59)
        );
        when(financeService.getBilanFinancier(any(), any())).thenReturn(bilan);
        invokePrivateLoadBilan();
        assertEquals("10000 €", lblRecettes.getText());
        assertEquals("7000 €", lblDepenses.getText());
        assertEquals("3000 €", lblSolde.getText());
        assertEquals("30.00 %", lblMarge.getText());
    }

    @Test
    @DisplayName("Bilan financier : exception service")
    void testBilanFinancierException() throws SQLException {
        when(financeService.getBilanFinancier(any(), any())).thenThrow(new RuntimeException("Erreur DB"));
        invokePrivateLoadBilan();
        assertTrue(lblRecettes.getText().isEmpty() || lblRecettes.getText() == null);
    }

    @Test
    @DisplayName("Evolution mensuelle : année complète")
    void testEvolutionMensuelle() throws SQLException {
        spinnerAnnee.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2030, 2024));
        Map<Month, BilanMensuel> map = new EnumMap<>(Month.class);
        for (Month m : Month.values()) {
            map.put(m, new BilanMensuel(BigDecimal.valueOf(1000), BigDecimal.valueOf(800)));
        }
        when(financeService.getEvolutionMensuelle(2024)).thenReturn(map);
        invokePrivateLoadEvolutionMensuelle();
        assertEquals(12, tableEvolution.getItems().size());
        assertEquals("1000.00 €", tableEvolution.getItems().get(0).recettes);
    }

    @Test
    @DisplayName("Alertes : cas nominal")
    void testAlertesNominal() throws SQLException {
        spinnerJoursAlerte.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 90, 30));
        spinnerKmEntretien.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1000, 50000, 20000, 1000));
        List<AlerteAssurance> assurances = List.of(mock(AlerteAssurance.class));
        List<AlerteEntretien> entretiens = List.of(mock(AlerteEntretien.class));
        when(financeService.getAlertesAssurances(30)).thenReturn(assurances);
        when(financeService.getAlertesEntretiens(20000)).thenReturn(entretiens);
        invokePrivateLoadAlertes();
        assertEquals(1, tableAlertesAssurance.getItems().size());
        assertEquals(1, tableAlertesEntretien.getItems().size());
    }

    @Test
    @DisplayName("Répartition budgétaire : cas nominal")
    void testRepartitionNominal() throws SQLException {
        spinnerAnnee.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2030, 2024));
        Map<String, BigDecimal> repartition = new HashMap<>();
        repartition.put("Carburant", BigDecimal.valueOf(5000));
        repartition.put("Entretien", BigDecimal.valueOf(2000));
        when(financeService.getRepartitionBudgetaire(2024)).thenReturn(repartition);
        invokePrivateLoadRepartition();
        assertEquals(2, tableRepartition.getItems().size());
        assertTrue(tableRepartition.getItems().stream().anyMatch(r -> r.type.equals("Carburant")));
    }

    @Test
    @DisplayName("Rapport de rentabilité : cas nominal")
    void testRentabiliteNominal() throws SQLException {
        spinnerAnnee.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2030, 2024));
        List<RentabiliteVehicule> rapport = List.of(mock(RentabiliteVehicule.class));
        when(financeService.getRapportRentabilite(2024)).thenReturn(rapport);
        invokePrivateLoadRentabilite();
        assertEquals(1, tableRentabilite.getItems().size());
    }

    @Test
    @DisplayName("Permissions refusées : accès interdit")
    void testPermissionsRefusees() {
        FinanceController ctrl = spy(new FinanceController(financeService));
        doReturn(false).when(ctrl).checkPermissions();
        try {
            setField(ctrl, "btnAfficherBilan", new Button());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ctrl.initialize(null, null);
        Button btn = (Button) getField(ctrl, "btnAfficherBilan");
        assertTrue(btn.isDisable());
    }

    // --- Méthodes utilitaires pour invoquer les méthodes privées du contrôleur ---
    private void invokePrivateLoadBilan() {
        try {
            var m = FinanceController.class.getDeclaredMethod("loadBilan");
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void invokePrivateLoadEvolutionMensuelle() {
        try {
            var m = FinanceController.class.getDeclaredMethod("loadEvolutionMensuelle");
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void invokePrivateLoadAlertes() {
        try {
            var m = FinanceController.class.getDeclaredMethod("loadAlertes");
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void invokePrivateLoadRepartition() {
        try {
            var m = FinanceController.class.getDeclaredMethod("loadRepartition");
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void invokePrivateLoadRentabilite() {
        try {
            var m = FinanceController.class.getDeclaredMethod("loadRentabilite");
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
