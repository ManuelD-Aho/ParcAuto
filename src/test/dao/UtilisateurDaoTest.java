package dao;

import com.miage.parcauto.dao.DbUtil;
import com.miage.parcauto.dao.UtilisateurDao;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour la classe UtilisateurDao
 * Utilise Mockito pour simuler les interactions avec la base de données
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class UtilisateurDaoTest {

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

    private UtilisateurDao utilisateurDao;
    private AutoCloseable autoCloseable;

    @BeforeEach
    public void setUp() throws SQLException {
        autoCloseable = MockitoAnnotations.openMocks(this);

        // Configuration des mocks
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(connection.createStatement()).thenReturn(statement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        // Création manuelle du DAO avec injection du mock DbUtil
        utilisateurDao = new UtilisateurDaoMock(dbUtil);
    }

    // Classe spéciale pour les tests qui permet d'injecter notre mock
    private static class UtilisateurDaoMock extends UtilisateurDao {
        public UtilisateurDaoMock(DbUtil dbUtil) {
            // Appel au constructeur parent sans argument
            super();

            // Injection du DbUtil mocké en utilisant la réflexion
            try {
                java.lang.reflect.Field dbUtilField = UtilisateurDao.class.getDeclaredField("dbUtil");
                dbUtilField.setAccessible(true);
                dbUtilField.set(this, dbUtil);
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de l'injection de DbUtil", e);
            }
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }

    @Test
    public void testAuthentifierSuccess() throws SQLException {
        // Configuration du mock resultSet pour simuler un utilisateur trouvé
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("login")).thenReturn("testuser");
        when(resultSet.getString("hash")).thenReturn("salt:hash"); // Format simplifié pour le test
        when(resultSet.getString("role")).thenReturn("U1");
        when(resultSet.getObject("id_personnel")).thenReturn(101);
        when(resultSet.getString("mfa_secret")).thenReturn("ABCDEFG");

        // Exécution de la méthode à tester
        Optional<UtilisateurDao.Utilisateur> result = utilisateurDao.authentifier("testuser", "password123");

        // Vérifications de base sur les appels aux méthodes mock
        verify(dbUtil).getConnection();
        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, "testuser");
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(dbUtil).closeResultSet(resultSet);
        verify(dbUtil).closePreparedStatement(preparedStatement);
        verify(dbUtil).releaseConnection(connection);
    }

    @Test
    public void testLoginExists() throws SQLException {
        // Configuration du mock resultSet pour simuler un login existant
        when(resultSet.next()).thenReturn(true);

        // Exécution de la méthode à tester
        boolean result = utilisateurDao.loginExists("testuser");

        // Vérifications
        assertTrue(result);

        // Vérification des appels aux méthodes mock
        verify(dbUtil).getConnection();
        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, "testuser");
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(dbUtil).closeResultSet(resultSet);
        verify(dbUtil).closePreparedStatement(preparedStatement);
        verify(dbUtil).releaseConnection(connection);
    }

    @Test
    public void testCreer() throws SQLException {
        // Configuration du mock pour simuler un login qui n'existe pas encore
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        // Mock de la méthode loginExists pour retourner false
        UtilisateurDao spyDao = spy(utilisateurDao);
        doReturn(false).when(spyDao).loginExists(anyString());

        // Exécution de la méthode à tester
        UtilisateurDao.Utilisateur utilisateur = spyDao.creer(
                "newuser",
                "password123",
                UtilisateurDao.Role.U2,
                102
        );

        // Vérifications
        assertNotNull(utilisateur);
        assertEquals("newuser", utilisateur.getLogin());
        assertEquals(UtilisateurDao.Role.U2, utilisateur.getRole());
        assertEquals(102, utilisateur.getIdPersonnel().intValue());
        assertEquals(1, utilisateur.getId().intValue());

        // Vérification des appels aux méthodes
        verify(spyDao).loginExists("newuser");
    }

    @Test
    public void testUpdatePassword() throws SQLException {
        // Configuration du mock pour simuler la mise à jour
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Exécution de la méthode à tester
        boolean result = utilisateurDao.updatePassword(1, "nouveauPassword");

        // Vérifications
        assertTrue(result);

        // Vérification des appels aux méthodes mock
        verify(dbUtil).getConnection();
        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeUpdate();
        verify(dbUtil).closePreparedStatement(preparedStatement);
        verify(dbUtil).releaseConnection(connection);
    }

    @Test
    public void testFindById() throws SQLException {
        // Configuration du mock resultSet
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("login")).thenReturn("testuser");
        when(resultSet.getString("hash")).thenReturn("salt:hash");
        when(resultSet.getString("role")).thenReturn("U1");
        when(resultSet.getObject("id_personnel")).thenReturn(101);
        when(resultSet.getString("mfa_secret")).thenReturn("ABCDEFG");

        // Exécution de la méthode à tester
        Optional<UtilisateurDao.Utilisateur> result = utilisateurDao.findById(1);

        // Vérifications
        assertTrue(result.isPresent());

        // Vérification des appels aux méthodes mock
        verify(dbUtil).getConnection();
        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(dbUtil).closeResultSet(resultSet);
        verify(dbUtil).closePreparedStatement(preparedStatement);
        verify(dbUtil).releaseConnection(connection);
    }

    @Test
    public void testDelete() throws SQLException {
        // Configuration du mock pour simuler une suppression réussie
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Exécution de la méthode à tester
        boolean result = utilisateurDao.delete(1);

        // Vérifications
        assertTrue(result);

        // Vérification des appels aux méthodes mock
        verify(dbUtil).getConnection();
        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();
        verify(dbUtil).closePreparedStatement(preparedStatement);
        verify(dbUtil).releaseConnection(connection);
    }

    @Test
    public void testToggleMfaEnable() throws SQLException {
        // Configuration du mock pour simuler la mise à jour
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Exécution de la méthode à tester
        String mfaSecret = utilisateurDao.toggleMfa(1, true);

        // Vérifications
        assertNotNull(mfaSecret);

        // Vérification des appels aux méthodes mock
        verify(dbUtil).getConnection();
        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeUpdate();
        verify(dbUtil).closePreparedStatement(preparedStatement);
        verify(dbUtil).releaseConnection(connection);
    }

    @Test
    public void testToggleMfaDisable() throws SQLException {
        // Configuration du mock pour simuler la mise à jour
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Exécution de la méthode à tester
        String mfaSecret = utilisateurDao.toggleMfa(1, false);

        // Vérifications
        assertNull(mfaSecret);

        // Vérification des appels aux méthodes mock
        verify(dbUtil).getConnection();
        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeUpdate();
        verify(dbUtil).closePreparedStatement(preparedStatement);
        verify(dbUtil).releaseConnection(connection);
    }

    @Test
    public void testUpdateRole() throws SQLException {
        // Configuration du mock pour simuler la mise à jour
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Exécution de la méthode à tester
        boolean result = utilisateurDao.updateRole(1, UtilisateurDao.Role.U2);

        // Vérifications
        assertTrue(result);

        // Vérification des appels aux méthodes mock
        verify(dbUtil).getConnection();
        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, "U2");
        verify(preparedStatement).setInt(2, 1);
        verify(preparedStatement).executeUpdate();
        verify(dbUtil).closePreparedStatement(preparedStatement);
        verify(dbUtil).releaseConnection(connection);
    }

    // Ajoutez d'autres tests selon vos besoins...
}