package main.java.com.miage.parcauto.component;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import main.java.com.miage.parcauto.dto.NotificationDTO;
import main.java.com.miage.parcauto.service.NotificationService;
import main.java.com.miage.parcauto.viewmodel.NotificationViewModel;

/**
 * Composant réutilisable pour l'affichage des notifications.
 * Peut être intégré dans n'importe quelle interface utilisateur.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class NotificationComponent extends StackPane {

    @FXML
    private ToggleButton btnNotifications;

    @FXML
    private Label lblCompteurNonLues;

    @FXML
    private Circle badgeCompteur;

    @FXML
    private VBox panelNotifications;

    @FXML
    private Label lblVide;

    @FXML
    private Button btnToutMarquer;

    @FXML
    private ListView<NotificationViewModel> listViewNotifications;

    @FXML
    private ScrollPane notificationPane;

    private final IntegerProperty nonLuesCount = new SimpleIntegerProperty(0);
    private final ObjectProperty<ObservableList<NotificationViewModel>> notifications = new SimpleObjectProperty<>(
            FXCollections.observableArrayList());
    private final NotificationService notificationService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Constructeur par défaut.
     */
    public NotificationComponent() {
        notificationService = NotificationService.getInstance();
        loadFXML();
        configureListeners();
        setupNotificationUpdater();
    }

    /**
     * Charge le fichier FXML associé au composant.
     */
    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/main/resources/fxml/components/notification_component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement du composant de notification", e);
        }
    }

    /**
     * Configure les listeners pour réagir aux changements de données.
     */
    private void configureListeners() {
        // Listener pour le compteur de notifications non lues
        nonLuesCount.addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() > 0) {
                lblCompteurNonLues.setText(String.valueOf(newVal.intValue()));
                badgeCompteur.setVisible(true);
                animateCounterChange();
            } else {
                lblCompteurNonLues.setText("");
                badgeCompteur.setVisible(false);
            }
        });

        // Listener pour la liste de notifications
        notifications.addListener((obs, oldList, newList) -> {
            if (oldList != null) {
                oldList.removeListener(this::onNotificationsChanged);
            }
            if (newList != null) {
                newList.addListener(this::onNotificationsChanged);
                listViewNotifications.setItems(newList);
                updateEmptyState();
            }
        });

        // Configuration bouton "tout marquer comme lu"
        btnToutMarquer.setOnAction(e -> marquerToutesCommeLues());

        // Configuration du panneau de notifications
        btnNotifications.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                showNotificationPanel();
            } else {
                hideNotificationPanel();
            }
        });
    }

    /**
     * Réagit aux changements dans la liste des notifications.
     */
    private void onNotificationsChanged(ListChangeListener.Change<? extends NotificationViewModel> change) {
        updateEmptyState();
        updateNonLuesCount();
    }

    /**
     * Met à jour l'état vide/non-vide du panneau de notifications.
     */
    private void updateEmptyState() {
        boolean isEmpty = notifications.get() == null || notifications.get().isEmpty();
        lblVide.setVisible(isEmpty);
        listViewNotifications.setVisible(!isEmpty);
        btnToutMarquer.setVisible(!isEmpty && nonLuesCount.get() > 0);
    }

    /**
     * Compte le nombre de notifications non lues.
     */
    private void updateNonLuesCount() {
        if (notifications.get() == null) {
            nonLuesCount.set(0);
            return;
        }

        int count = 0;
        for (NotificationViewModel notification : notifications.get()) {
            if (!notification.isVue()) {
                count++;
            }
        }

        nonLuesCount.set(count);
    }

    /**
     * Initialise le planificateur pour mettre à jour les notifications.
     */
    private void setupNotificationUpdater() {
        // Tâche pour mettre à jour l'indicateur de temps écoulé
        scheduler.scheduleAtFixedRate(() -> {
            if (notifications.get() != null) {
                for (NotificationViewModel notification : notifications.get()) {
                    notification.updateTempsEcoule();
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * Affiche le panneau des notifications avec animation.
     */
    private void showNotificationPanel() {
        panelNotifications.setVisible(true);
        panelNotifications.setOpacity(0);
        panelNotifications.setTranslateY(-10);

        ParallelTransition transition = new ParallelTransition();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), panelNotifications);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(200), panelNotifications);
        slideIn.setFromY(-10);
        slideIn.setToY(0);

        transition.getChildren().addAll(fadeIn, slideIn);
        transition.play();
    }

    /**
     * Masque le panneau des notifications avec animation.
     */
    private void hideNotificationPanel() {
        ParallelTransition transition = new ParallelTransition();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), panelNotifications);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(200), panelNotifications);
        slideOut.setFromY(0);
        slideOut.setToY(-10);

        transition.getChildren().addAll(fadeOut, slideOut);
        transition.setOnFinished(e -> panelNotifications.setVisible(false));
        transition.play();
    }

    /**
     * Anime le compteur de notifications non lues.
     */
    private void animateCounterChange() {
        ParallelTransition transition = new ParallelTransition();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(100), badgeCompteur);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0.5);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(100), badgeCompteur);
        fadeIn.setFromValue(0.5);
        fadeIn.setToValue(1);
        fadeIn.setDelay(Duration.millis(100));

        TranslateTransition bounce = new TranslateTransition(Duration.millis(200), badgeCompteur);
        bounce.setFromY(0);
        bounce.setToY(-5);
        bounce.setCycleCount(2);
        bounce.setAutoReverse(true);

        transition.getChildren().addAll(fadeOut, fadeIn, bounce);
        transition.play();
    }

    /**
     * Affiche une notification temporaire dans l'interface.
     * 
     * @param type    Type de notification
     * @param titre   Titre de la notification
     * @param message Message de la notification
     */
    public void showNotification(NotificationDTO.TypeNotification type, String titre, String message) {
        Pane toast = createToastNotification(type, titre, message);
        getChildren().add(toast);

        // Animation d'entrée
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), toast);
        slideIn.setFromY(20);
        slideIn.setToY(0);

        ParallelTransition showTransition = new ParallelTransition(fadeIn, slideIn);
        showTransition.play();

        // Disparition après délai
        scheduler.schedule(() -> {
            javafx.application.Platform.runLater(() -> {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), toast);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);

                TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), toast);
                slideOut.setFromY(0);
                slideOut.setToY(20);

                ParallelTransition hideTransition = new ParallelTransition(fadeOut, slideOut);
                hideTransition.setOnFinished(e -> getChildren().remove(toast));
                hideTransition.play();
            });
        }, 3, TimeUnit.SECONDS);
    }

    /**
     * Crée un élément visuel pour une notification toast.
     * 
     * @param type    Type de la notification
     * @param titre   Titre
     * @param message Message
     * @return Nœud représentant la notification toast
     */
    private Pane createToastNotification(NotificationDTO.TypeNotification type, String titre, String message) {
        VBox toast = new VBox(5);
        toast.setAlignment(Pos.CENTER_LEFT);
        toast.setPrefWidth(300);
        toast.setMaxWidth(300);
        toast.getStyleClass().addAll("notification-toast", type.getCssClass());

        Label titleLabel = new Label(titre);
        titleLabel.getStyleClass().add("notification-title");

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add("notification-message");

        Button closeButton = new Button("×");
        closeButton.getStyleClass().add("notification-close");
        closeButton.setOnAction(e -> {
            ParallelTransition hideTransition = new ParallelTransition(
                    new FadeTransition(Duration.millis(200), toast) {
                        {
                            setFromValue(1);
                            setToValue(0);
                        }
                    });
            hideTransition.setOnFinished(event -> getChildren().remove(toast));
            hideTransition.play();
        });

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().addAll(titleLabel);
        HBox.setHgrow(titleLabel, javafx.scene.layout.Priority.ALWAYS);
        header.getChildren().add(closeButton);

        toast.getChildren().addAll(header, messageLabel);

        // Positionnement
        StackPane.setAlignment(toast, Pos.BOTTOM_RIGHT);
        toast.setTranslateY(-20);
        toast.setTranslateX(-20);
        toast.setOpacity(0);

        return toast;
    }

    /**
     * Marque toutes les notifications comme lues.
     */
    private void marquerToutesCommeLues() {
        Integer idUtilisateur = getIdUtilisateurCourant();
        if (idUtilisateur != null) {
            notificationService.marquerToutesCommeVues(idUtilisateur);
            // La mise à jour de l'UI est gérée par les listeners
        }
    }

    /**
     * Récupère l'ID de l'utilisateur connecté.
     * 
     * @return ID utilisateur ou null si non connecté
     */
    private Integer getIdUtilisateurCourant() {
        return main.java.com.miage.parcauto.security.SessionManager.getInstance().getUtilisateurConnecte() != null
                ? main.java.com.miage.parcauto.security.SessionManager.getInstance().getUtilisateurConnecte()
                        .getIdUtilisateur()
                : null;
    }

    /**
     * Initialise les données du composant.
     * 
     * @param notificationViewModels Liste de notifications à afficher
     */
    public void setNotifications(List<NotificationViewModel> notificationViewModels) {
        ObservableList<NotificationViewModel> observableList = FXCollections
                .observableArrayList(notificationViewModels);
        notifications.set(observableList);
        updateNonLuesCount();
    }

    /**
     * Ajoute une notification à la liste.
     * 
     * @param notification Notification à ajouter
     */
    public void addNotification(NotificationViewModel notification) {
        if (notification != null && notifications.get() != null) {
            notifications.get().add(0, notification);
            updateNonLuesCount();
        }
    }

    /**
     * Marque une notification comme vue.
     * 
     * @param idNotification ID de la notification
     */
    public void marquerCommeVue(int idNotification) {
        if (notifications.get() != null) {
            for (NotificationViewModel notification : notifications.get()) {
                if (notification.getIdNotification() == idNotification) {
                    notification.setVue(true);
                    notificationService.marquerCommeVue(idNotification);
                    updateNonLuesCount();
                    break;
                }
            }
        }
    }

    /**
     * Nettoie les ressources utilisées par le composant.
     */
    public void cleanup() {
        scheduler.shutdown();
    }

    /**
     * Retourne le compteur de notifications non lues.
     * 
     * @return Nombre de notifications non lues
     */
    public int getUnreadCount() {
        return nonLuesCount.get();
    }

    /**
     * Propriété observable du compteur de notifications non lues.
     * 
     * @return Propriété entière
     */
    public IntegerProperty unreadCountProperty() {
        return nonLuesCount;
    }

    /**
     * Réinitialise l'état du composant.
     */
    public void reset() {
        if (notifications.get() != null) {
            notifications.get().clear();
        }
        nonLuesCount.set(0);
        btnNotifications.setSelected(false);
        panelNotifications.setVisible(false);
    }
}
