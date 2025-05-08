/**
 * Script pour générer et afficher dynamiquement des alertes dans le tableau de bord
 * Compatible avec JavaFX WebView / WebEngine
 */

// Fonction appelée par le contrôleur JavaFX pour initialiser les alertes
function initializeAlerts(vehiculesData, entretienData, alerteFinanceData) {
    // Conversion des données JSON en objets JavaScript
    const vehicules = JSON.parse(vehiculesData || '[]');
    const entretiens = JSON.parse(entretienData || '[]');
    const finances = JSON.parse(alerteFinanceData || '[]');
    
    // Construction du HTML pour les alertes
    let alertesHTML = '';
    
    // Alertes d'entretien
    entretiens.forEach(entretien => {
        alertesHTML += `
        <div class="alerte-item entretien-alerte">
            <button class="alerte-close" onclick="closeAlerte(this, 'entretien', '${entretien.id || ''}')" title="Fermer">&times;</button>
            <div class="alerte-header">
                <span class="alerte-icon">🔧</span>
                <span class="alerte-titre">Entretien requis</span>
                <span class="alerte-date">${entretien.dateEcheance}</span>
            </div>
            <div class="alerte-text">
                Véhicule: ${entretien.vehicule} - ${entretien.description}
            </div>
        </div>`;
    });
    
    // Alertes de véhicules (niveau carburant, kilométrage...)
    vehicules.forEach(vehicule => {
        alertesHTML += `
        <div class="alerte-item vehicule-alerte">
            <button class="alerte-close" onclick="closeAlerte(this, 'vehicule', '${vehicule.id || ''}')" title="Fermer">&times;</button>
            <div class="alerte-header">
                <span class="alerte-icon">🚗</span>
                <span class="alerte-titre">${vehicule.titre}</span>
                <span class="alerte-date">${vehicule.date}</span>
            </div>
            <div class="alerte-text">
                ${vehicule.message}
            </div>
        </div>`;
    });
    
    // Alertes financières
    finances.forEach(finance => {
        alertesHTML += `
        <div class="alerte-item finance-alerte">
            <button class="alerte-close" onclick="closeAlerte(this, 'finance', '${finance.id || ''}')" title="Fermer">&times;</button>
            <div class="alerte-header">
                <span class="alerte-icon">💰</span>
                <span class="alerte-titre">${finance.titre}</span>
                <span class="alerte-date">${finance.date}</span>
            </div>
            <div class="alerte-text">
                ${finance.message} - Montant: ${finance.montant}€
            </div>
        </div>`;
    });
    
    // Si aucune alerte n'est présente
    if (alertesHTML === '') {
        alertesHTML = `
        <div class="alerte-item info-alerte">
            <div class="alerte-header">
                <span class="alerte-icon">ℹ️</span>
                <span class="alerte-titre">Information</span>
            </div>
            <div class="alerte-text">
                Aucune alerte à afficher pour le moment
            </div>
        </div>`;
    }
    
    // Ajout du style CSS spécifique aux alertes
    const alertesCSS = `
    <style>
        .alertes-container {
            display: flex;
            flex-direction: column;
            gap: 10px;
            width: 100%;
            padding-top: 10px;
        }
        .alerte-item {
            background-color: #f9fafb;
            border-radius: 6px;
            padding: 12px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            border-left: 4px solid #ef4444;
            position: relative;
        }
        .alerte-header {
            display: flex;
            align-items: center;
            margin-bottom: 6px;
        }
        .alerte-icon {
            font-size: 18px;
            margin-right: 8px;
        }
        .alerte-titre {
            font-weight: bold;
            color: #111827;
            flex-grow: 1;
        }
        .alerte-date {
            color: #ef4444;
            font-size: 12px;
            font-weight: bold;
        }
        .alerte-text {
            color: #4b5563;
            font-size: 14px;
        }
        .alerte-close {
            position: absolute;
            top: 10px;
            right: 10px;
            background: none;
            border: none;
            font-size: 16px;
            cursor: pointer;
            color: #6b7280;
        }
        .alerte-close:hover {
            color: #ef4444;
        }
        .entretien-alerte { border-color: #f59e0b; }
        .entretien-alerte .alerte-date { color: #f59e0b; }
        .vehicule-alerte { border-color: #4f46e5; }
        .vehicule-alerte .alerte-date { color: #4f46e5; }
        .finance-alerte { border-color: #10b981; }
        .finance-alerte .alerte-date { color: #10b981; }
        .info-alerte { border-color: #06b6d4; }
        .info-alerte .alerte-date { color: #06b6d4; }
    </style>`;
    
    // Construction du contenu final
    return alertesCSS + '<div class="alertes-container">' + alertesHTML + '</div>';
}

// Ajout d'une fonction pour fermer une alerte et notifier JavaFX si besoin
function closeAlerte(btn, type, id) {
    const alerteDiv = btn.closest('.alerte-item');
    if (alerteDiv) {
        alerteDiv.style.transition = 'opacity 0.3s';
        alerteDiv.style.opacity = 0;
        setTimeout(() => {
            alerteDiv.remove();
            if (window.java && typeof window.java.onAlerteFermee === 'function') {
                window.java.onAlerteFermee(type, id);
            }
        }, 300);
    }
}

// Fonction appelée pour mettre à jour dynamiquement les compteurs
function updateStatistics(vehiculeCount, missionCount, financeBalance, entretienCount) {
    // Retourner ces valeurs au contrôleur JavaFX si nécessaire
    return {
        vehicules: vehiculeCount,
        missions: missionCount,
        finances: financeBalance,
        entretiens: entretienCount
    };
}

// Exposition des fonctions au contexte JavaFX
if (typeof window !== 'undefined') {
    window.dashboardAlerts = {
        initializeAlerts: initializeAlerts,
        updateStatistics: updateStatistics
    };
}