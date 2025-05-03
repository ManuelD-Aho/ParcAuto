package dao;

import com.miage.parcauto.dao.DbUtil;
import com.miage.parcauto.dao.VehiculeDao;
import com.miage.parcauto.model.vehicule.EtatVoiture;
import com.miage.parcauto.model.vehicule.Vehicule;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour la classe VehiculeDao
 * Utilise Mockito pour simuler les interactions avec la base de données
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class VehiculeDaoTest {

    @Mock
    private DbUtil dbUtil;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private VehiculeDao vehiculeDao;
    private AutoCloseable autoCloseable;

    private Vehicule testVehicule;
    private EtatVoiture testEtatVoiture;

    @BeforeEach
    public void setUp() throws SQLException {
        autoCloseable = MockitoAnnotations.openMocks(this);

        // Configuration des mocks
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(connection.createStatement()).thenReturn(statement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        // Initialisation de l'état de voiture pour les tests
        testEtatVoiture = new EtatVoiture();
        testEtatVoiture.setIdEtatVoiture(1);
        testEtatVoiture.setLibEtatVoiture("Disponible");

        // Initialisation du véhicule pour les tests
        testVehicule = new Vehicule();
        testVehicule.setIdVehicule(1);
        testVehicule.setEtatVoiture(testEtatVoiture);
        testVehicule.setEnergie(Vehicule.TypeEnergie.Diesel);
        testVehicule.setNumeroChassi("VF123456789012345");
        testVehicule.setImmatriculation("AA-123-BB");
        testVehicule.setMarque("Renault");
        testVehicule.setModele("Clio");
        testVehicule.setNbPlaces(5);
        testVehicule.setDateAcquisition(LocalDateTime.now().minusMonths(3));
        testVehicule.setDateAmmortissement(LocalDateTime.now().plusYears(5));
        testVehicule.setDateMiseEnService(LocalDateTime.now().minusMonths(3));
        testVehicule.setPuissance(90);
        testVehicule.setCouleur("Noir");
        testVehicule.setPrixVehicule(new BigDecimal("15000.00"));
        testVehicule.setKmActuels(1500);
        testVehicule.setDateEtat(LocalDateTime.now());

        // Création de l'instance à tester avec le mock DbUtil
        vehiculeDao = new VehiculeDao(dbUtil);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }

    @Test
    public void testFindAll() throws SQLException {
        // Configuration du mock resultSet
        when(resultSet.next()).thenReturn(true, true, false); // Deux véhicules

        // Première ligne puis deuxième ligne
        when(resultSet.getInt("id_vehicule")).thenReturn(1, 2);
        when(resultSet.getInt("id_etat_voiture")).thenReturn(1, 2);
        when(resultSet.getString("energie")).thenReturn("Diesel", "Essence");
        when(resultSet.getString("numero_chassi")).thenReturn("VF123456789012345", "VF987654321098765");
        when(resultSet.getString("immatriculation")).thenReturn("AA-123-BB", "CC-456-DD");
        when(resultSet.getString("marque")).thenReturn("Renault", "Peugeot");
        when(resultSet.getString("modele")).thenReturn("Clio", "308");
        when(resultSet.getInt("nb_places")).thenReturn(5, 5);
        when(resultSet.getTimestamp("date_acquisition")).thenReturn(
                Timestamp.valueOf(LocalDateTime.now().minusMonths(3)),
                Timestamp.valueOf(LocalDateTime.now().minusMonths(2))
        );
        when(resultSet.getTimestamp("date_ammortissement")).thenReturn(
                Timestamp.valueOf(LocalDateTime.now().plusYears(5)),
                Timestamp.valueOf(LocalDateTime.now().plusYears(5))
        );
        when(resultSet.getTimestamp("date_mise_en_service")).thenReturn(
                Timestamp.valueOf(LocalDateTime.now().minusMonths(3)),
                Timestamp.valueOf(LocalDateTime.now().minusMonths(2))
        );
        when(resultSet.getInt("puissance")).thenReturn(90, 110);
        when(resultSet.getString("couleur")).thenReturn("Noir", "Blanc");
        when(resultSet.getBigDecimal("prix_vehicule")).thenReturn(
                new BigDecimal("15000.00"),
                new BigDecimal("18000.00")
        );
        when(resultSet.getInt("km_actuels")).thenReturn(1500, 2000);
        when(resultSet.getTimestamp("date_etat")).thenReturn(
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now())
        );
        when(resultSet.getString("lib_etat_voiture")).thenReturn("Disponible", "En mission");

        // Exécution de la méthode à tester
        List<Vehicule> vehicules = vehiculeDao.findAll();

        // Vérifications
        assertNotNull(vehicules);
        assertEquals(2, vehicules.size());
        assertEquals("Renault", vehicules.get(0).getMarque());
        assertEquals("Peugeot", vehicules.get(1).getMarque());
    }

    @Test
    public void testFindById() throws SQLException {
        // Configuration du mock resultSet
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id_vehicule")).thenReturn(1);
        when(resultSet.getInt("id_etat_voiture")).thenReturn(1);
        when(resultSet.getString("energie")).thenReturn("Diesel");
        when(resultSet.getString("numero_chassi")).thenReturn("VF123456789012345");
        when(resultSet.getString("immatriculation")).thenReturn("AA-123-BB");
        when(resultSet.getString("marque")).thenReturn("Renault");
        when(resultSet.getString("modele")).thenReturn("Clio");
        when(resultSet.getInt("nb_places")).thenReturn(5);
        when(resultSet.getTimestamp("date_acquisition")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getTimestamp("date_ammortissement")).thenReturn(Timestamp.valueOf(LocalDateTime.now().plusYears(5)));
        when(resultSet.getTimestamp("date_mise_en_service")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getInt("puissance")).thenReturn(90);
        when(resultSet.getString("couleur")).thenReturn("Noir");
        when(resultSet.getBigDecimal("prix_vehicule")).thenReturn(new BigDecimal("15000.00"));
        when(resultSet.getInt("km_actuels")).thenReturn(1500);
        when(resultSet.getTimestamp("date_etat")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(resultSet.getString("lib_etat_voiture")).thenReturn("Disponible");

        // Exécution de la méthode à tester
        Optional<Vehicule> vehiculeOpt = vehiculeDao.findById(1);

        // Vérifications
        assertTrue(vehiculeOpt.isPresent());
        Vehicule vehicule = vehiculeOpt.get();
        assertEquals(1, vehicule.getIdVehicule());
        assertEquals("Renault", vehicule.getMarque());
    }

    @Test
    public void testCreate() throws SQLException {
        // Configuration du mock pour simuler l'insertion
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        // Exécution de la méthode à tester
        Vehicule createdVehicule = vehiculeDao.create(testVehicule);

        // Vérifications
        assertNotNull(createdVehicule);
        assertEquals(1, createdVehicule.getIdVehicule());
    }

    @Test
    public void testUpdate() throws SQLException {
        // Configuration du mock pour simuler la mise à jour
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Exécution de la méthode à tester
        boolean result = vehiculeDao.update(testVehicule);

        // Vérifications
        assertTrue(result);
    }

    /**
     * Test modifié de la méthode delete() qui prend en compte la vérification des enregistrements liés
     */
    @Test
    public void testDelete() throws SQLException {
        // Configuration pour simuler l'absence d'enregistrements liés
        when(resultSet.next()).thenReturn(false);
        // Configuration pour simuler la suppression réussie
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Exécution de la méthode à tester
        boolean result = vehiculeDao.delete(1);

        // Vérifications
        assertTrue(result);

        // Vérifions que la requête de suppression est bien exécutée
        verify(preparedStatement, atLeastOnce()).executeUpdate();
        // Vérifions que l'ID du véhicule est bien utilisé dans au moins une des requêtes préparées
        verify(preparedStatement, atLeastOnce()).setInt(anyInt(), eq(1));
    }

    /**
     * Test modifié de la méthode updateEtat() qui prend en compte les paramètres réels
     */
    @Test
    public void testUpdateEtat() throws SQLException {
        // Configuration du mock pour simuler la mise à jour
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Exécution de la méthode à tester
        boolean result = vehiculeDao.updateEtat(1, 2);

        // Vérifications
        assertTrue(result);

        // Vérifions que les bons paramètres sont passés à la requête préparée
        verify(preparedStatement).setInt(1, 2); // id_etat_voiture
        // Vérifions que le timestamp est défini (paramètre 2)
        verify(preparedStatement).setTimestamp(eq(2), any(Timestamp.class));
        verify(preparedStatement).setInt(3, 1); // id_vehicule
    }

    @Test
    public void testFindByEtat() throws SQLException {
        // Configuration du mock resultSet
        when(resultSet.next()).thenReturn(true, false); // Une ligne à lire
        when(resultSet.getInt("id_vehicule")).thenReturn(1);
        when(resultSet.getInt("id_etat_voiture")).thenReturn(1);
        when(resultSet.getString("energie")).thenReturn("Diesel");
        when(resultSet.getString("numero_chassi")).thenReturn("VF123456789012345");
        when(resultSet.getString("immatriculation")).thenReturn("AA-123-BB");
        when(resultSet.getString("marque")).thenReturn("Renault");
        when(resultSet.getString("modele")).thenReturn("Clio");
        when(resultSet.getInt("nb_places")).thenReturn(5);
        when(resultSet.getTimestamp("date_acquisition")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getTimestamp("date_ammortissement")).thenReturn(Timestamp.valueOf(LocalDateTime.now().plusYears(5)));
        when(resultSet.getTimestamp("date_mise_en_service")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getInt("puissance")).thenReturn(90);
        when(resultSet.getString("couleur")).thenReturn("Noir");
        when(resultSet.getBigDecimal("prix_vehicule")).thenReturn(new BigDecimal("15000.00"));
        when(resultSet.getInt("km_actuels")).thenReturn(1500);
        when(resultSet.getTimestamp("date_etat")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(resultSet.getString("lib_etat_voiture")).thenReturn("Disponible");

        // Exécution de la méthode à tester
        List<Vehicule> vehicules = vehiculeDao.findByEtat(1);

        // Vérifications
        assertNotNull(vehicules);
        assertEquals(1, vehicules.size());
        assertEquals("Renault", vehicules.get(0).getMarque());
    }

    @Test
    public void testFindByImmatriculation() throws SQLException {
        // Configuration du mock resultSet
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id_vehicule")).thenReturn(1);
        when(resultSet.getInt("id_etat_voiture")).thenReturn(1);
        when(resultSet.getString("energie")).thenReturn("Diesel");
        when(resultSet.getString("numero_chassi")).thenReturn("VF123456789012345");
        when(resultSet.getString("immatriculation")).thenReturn("AA-123-BB");
        when(resultSet.getString("marque")).thenReturn("Renault");
        when(resultSet.getString("modele")).thenReturn("Clio");
        when(resultSet.getInt("nb_places")).thenReturn(5);
        when(resultSet.getTimestamp("date_acquisition")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getTimestamp("date_ammortissement")).thenReturn(Timestamp.valueOf(LocalDateTime.now().plusYears(5)));
        when(resultSet.getTimestamp("date_mise_en_service")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getInt("puissance")).thenReturn(90);
        when(resultSet.getString("couleur")).thenReturn("Noir");
        when(resultSet.getBigDecimal("prix_vehicule")).thenReturn(new BigDecimal("15000.00"));
        when(resultSet.getInt("km_actuels")).thenReturn(1500);
        when(resultSet.getTimestamp("date_etat")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(resultSet.getString("lib_etat_voiture")).thenReturn("Disponible");

        // Exécution de la méthode à tester
        Optional<Vehicule> vehiculeOpt = vehiculeDao.findByImmatriculation("AA-123-BB");

        // Vérifications
        assertTrue(vehiculeOpt.isPresent());
        Vehicule vehicule = vehiculeOpt.get();
        assertEquals("AA-123-BB", vehicule.getImmatriculation());
    }

    @Test
    public void testFindNeedingMaintenance() throws SQLException {
        // Configuration du mock resultSet
        when(resultSet.next()).thenReturn(true, false); // Une ligne à lire
        when(resultSet.getInt("id_vehicule")).thenReturn(1);
        when(resultSet.getInt("id_etat_voiture")).thenReturn(1);
        when(resultSet.getString("energie")).thenReturn("Diesel");
        when(resultSet.getString("numero_chassi")).thenReturn("VF123456789012345");
        when(resultSet.getString("immatriculation")).thenReturn("AA-123-BB");
        when(resultSet.getString("marque")).thenReturn("Renault");
        when(resultSet.getString("modele")).thenReturn("Clio");
        when(resultSet.getInt("nb_places")).thenReturn(5);
        when(resultSet.getTimestamp("date_acquisition")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getTimestamp("date_ammortissement")).thenReturn(Timestamp.valueOf(LocalDateTime.now().plusYears(5)));
        when(resultSet.getTimestamp("date_mise_en_service")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getInt("puissance")).thenReturn(90);
        when(resultSet.getString("couleur")).thenReturn("Noir");
        when(resultSet.getBigDecimal("prix_vehicule")).thenReturn(new BigDecimal("15000.00"));
        when(resultSet.getInt("km_actuels")).thenReturn(15000);
        when(resultSet.getTimestamp("date_etat")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(resultSet.getString("lib_etat_voiture")).thenReturn("Disponible");

        // Exécution de la méthode à tester
        List<Vehicule> vehicules = vehiculeDao.findNeedingMaintenance(10000);

        // Vérifications
        assertNotNull(vehicules);
        assertEquals(1, vehicules.size());
        assertEquals(15000, vehicules.get(0).getKmActuels());
    }

    @Test
    public void testSearch() throws SQLException {
        // Configuration du mock resultSet
        when(resultSet.next()).thenReturn(true, false); // Une ligne à lire
        when(resultSet.getInt("id_vehicule")).thenReturn(1);
        when(resultSet.getInt("id_etat_voiture")).thenReturn(1);
        when(resultSet.getString("energie")).thenReturn("Diesel");
        when(resultSet.getString("numero_chassi")).thenReturn("VF123456789012345");
        when(resultSet.getString("immatriculation")).thenReturn("AA-123-BB");
        when(resultSet.getString("marque")).thenReturn("Renault");
        when(resultSet.getString("modele")).thenReturn("Clio");
        when(resultSet.getInt("nb_places")).thenReturn(5);
        when(resultSet.getTimestamp("date_acquisition")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getTimestamp("date_ammortissement")).thenReturn(Timestamp.valueOf(LocalDateTime.now().plusYears(5)));
        when(resultSet.getTimestamp("date_mise_en_service")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getInt("puissance")).thenReturn(90);
        when(resultSet.getString("couleur")).thenReturn("Noir");
        when(resultSet.getBigDecimal("prix_vehicule")).thenReturn(new BigDecimal("15000.00"));
        when(resultSet.getInt("km_actuels")).thenReturn(1500);
        when(resultSet.getTimestamp("date_etat")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(resultSet.getString("lib_etat_voiture")).thenReturn("Disponible");

        // Exécution de la méthode à tester
        List<Vehicule> vehicules = vehiculeDao.search("Renault");

        // Vérifications
        assertNotNull(vehicules);
        assertEquals(1, vehicules.size());
    }

    @Test
    public void testCalculateParcStats() throws SQLException {
        // Configuration du mock resultSet pour simuler les statistiques
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("total")).thenReturn(20);
        when(resultSet.getInt("disponibles")).thenReturn(10);
        when(resultSet.getInt("en_mission")).thenReturn(5);
        when(resultSet.getInt("hors_service")).thenReturn(2);
        when(resultSet.getInt("en_entretien")).thenReturn(1);
        when(resultSet.getInt("attribues")).thenReturn(1);
        when(resultSet.getInt("panne")).thenReturn(1);
        when(resultSet.getDouble("km_moyen")).thenReturn(30000.0);

        // Exécution de la méthode à tester
        VehiculeDao.ParcStats stats = vehiculeDao.calculateParcStats();

        // Vérifications
        assertNotNull(stats);
        assertEquals(20, stats.getTotalVehicules());
        assertEquals(10, stats.getDisponibles());
        assertEquals(5, stats.getEnMission());
        assertEquals(2, stats.getHorsService());
        assertEquals(1, stats.getEnEntretien());
        assertEquals(1, stats.getAttribues());
        assertEquals(1, stats.getEnPanne());
        assertEquals(30000.0, stats.getKmMoyen());
        assertEquals(50.0, stats.getPourcentageDisponibilite());
        assertEquals(25.0, stats.getPourcentageUtilisation());
    }

    @Test
    public void testGetTCOInfo() throws SQLException {
        // Configuration du mock resultSet pour simuler les informations TCO
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBigDecimal("couts_totaux")).thenReturn(new BigDecimal("5000.00"));
        when(resultSet.getInt("km_actuels")).thenReturn(25000);

        // Exécution de la méthode à tester
        Optional<VehiculeDao.TCOInfo> tcoInfo = vehiculeDao.getTCOInfo(1);

        // Vérifications
        assertTrue(tcoInfo.isPresent());
        assertEquals(new BigDecimal("5000.00"), tcoInfo.get().getCoutsTotaux());
        assertEquals(25000, tcoInfo.get().getKmActuels());
    }

    @Test
    public void testFindByNumeroChassi() throws SQLException {
        // Configuration du mock resultSet
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id_vehicule")).thenReturn(1);
        when(resultSet.getInt("id_etat_voiture")).thenReturn(1);
        when(resultSet.getString("energie")).thenReturn("Diesel");
        when(resultSet.getString("numero_chassi")).thenReturn("VF123456789012345");
        when(resultSet.getString("immatriculation")).thenReturn("AA-123-BB");
        when(resultSet.getString("marque")).thenReturn("Renault");
        when(resultSet.getString("modele")).thenReturn("Clio");
        when(resultSet.getInt("nb_places")).thenReturn(5);
        when(resultSet.getTimestamp("date_acquisition")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getTimestamp("date_ammortissement")).thenReturn(Timestamp.valueOf(LocalDateTime.now().plusYears(5)));
        when(resultSet.getTimestamp("date_mise_en_service")).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
        when(resultSet.getInt("puissance")).thenReturn(90);
        when(resultSet.getString("couleur")).thenReturn("Noir");
        when(resultSet.getBigDecimal("prix_vehicule")).thenReturn(new BigDecimal("15000.00"));
        when(resultSet.getInt("km_actuels")).thenReturn(1500);
        when(resultSet.getTimestamp("date_etat")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(resultSet.getString("lib_etat_voiture")).thenReturn("Disponible");

        // Exécution de la méthode à tester
        Optional<Vehicule> vehiculeOpt = vehiculeDao.findByNumeroChassi("VF123456789012345");

        // Vérifications
        assertTrue(vehiculeOpt.isPresent());
        assertEquals("VF123456789012345", vehiculeOpt.get().getNumeroChassi());
    }
}