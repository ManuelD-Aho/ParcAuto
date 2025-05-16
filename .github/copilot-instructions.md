Tu es un ingénieur aguerri capable de tout comprendre, résoudre n'importe quel problème, développer et structurer tout en détaillant chaque point que tu fais
En français, sans commentaire dans le code, tu dois être précis et complet, dans la rédaction du code, tu dois tout donner sans dire qu'il faut implémenter ceci ou cela à l'avenir, tu le fais immédiatement 
Jamais d'exemple, que du concret, complet et du code qui fonctionne

"Corrige les erreurs de compilation dans le code Java ci-dessous, en t'assurant que tout fonctionne correctement."

C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\DocumentSocietaireServiceImpl.java
Error:(1, 9) Package name 'main.java.com.miage.parcauto.mapper.impl' does not correspond to the file path 'main.java.com.miage.parcauto.service.impl'
Warning:(15, 14) Class 'DocumentSocietaireMapperImpl' is never used
Error:(26, 13) Cannot resolve method 'setIdDocument' in 'DocumentSocietaireDTO'
Error:(27, 38) Cannot resolve method 'getIdSocietaire' in 'DocumentSocietaire'
Error:(28, 13) Cannot resolve method 'setTypeDocument' in 'DocumentSocietaireDTO'
Error:(43, 29) Cannot resolve method 'getIdDocument' in 'DocumentSocietaireDTO'
Error:(44, 16) Cannot resolve method 'setIdSocietaire' in 'DocumentSocietaire'
Error:(45, 17) Cannot resolve method 'getTypeDocument' in 'DocumentSocietaireDTO'
Error:(47, 60) Cannot resolve method 'getTypeDocument' in 'DocumentSocietaireDTO'
Error:(50, 109) Cannot resolve method 'getTypeDocument' in 'DocumentSocietaireDTO'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\ReportingService.java
Warning:(3, 1) Unused import statement
Warning:(7, 1) Unused import statement
Warning:(27, 24) Method 'genererRapportVehicule(java.lang.Integer)' is never used
Warning:(35, 20) Method 'genererBilanFlotte()' is never used
Warning:(45, 12) Method 'exporterRapportPDF(main.java.com.miage.parcauto.dto.RapportDTO)' is never used
Warning:(54, 12) Method 'exporterRapportExcel(main.java.com.miage.parcauto.dto.RapportDTO)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\exception\AuthorizationException.java
Warning:(3, 14) Class 'AuthorizationException' is never used
Warning:(5, 12) Constructor 'AuthorizationException(java.lang.String)' is never used
Warning:(9, 12) Constructor 'AuthorizationException(java.lang.String, java.lang.Throwable)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\MouvementService.java
Warning:(4, 1) Unused import statement
Warning:(17, 18) Interface 'MouvementService' is never used
Warning:(29, 18) Method 'createMouvement(main.java.com.miage.parcauto.dto.MouvementDTO)' is never used
Warning:(38, 28) Method 'getMouvementById(java.lang.Integer)' is never used
Warning:(48, 24) Method 'getMouvementsByCompteSocietaireId(java.lang.Integer)' is never used
Warning:(56, 24) Method 'getAllMouvements()' is never used
Warning:(66, 24) Method 'getMouvementsByDateRange(java.time.LocalDateTime, java.time.LocalDateTime)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\exception\ReportGenerationException.java
Warning:(5, 12) Constructor 'ReportGenerationException(java.lang.String)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\exception\DocumentNotFoundException.java
Warning:(5, 12) Constructor 'DocumentNotFoundException(java.lang.String)' is never used
Warning:(9, 12) Constructor 'DocumentNotFoundException(java.lang.String, java.lang.Throwable)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\NotificationMapper.java
Error:(4, 43) Cannot resolve symbol 'Notification'
Error:(17, 27) Cannot resolve symbol 'Notification'
Error:(24, 5) Cannot resolve symbol 'Notification'
Error:(31, 42) Cannot resolve symbol 'Notification'
Error:(38, 10) Cannot resolve symbol 'Notification'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\UtilisateurMapper.java
Warning:(5, 1) Unused import statement
Warning:(8, 1) Unused import statement
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\SocietaireCompteService.java
Warning:(5, 1) Unused import statement
Warning:(18, 18) Interface 'SocietaireCompteService' is never used
Warning:(29, 25) Method 'createSocietaireCompte(main.java.com.miage.parcauto.dto.SocietaireCompteDTO)' is never used
Warning:(38, 35) Method 'getSocietaireCompteById(java.lang.Integer)' is never used
Warning:(47, 35) Method 'getSocietaireCompteByNumero(java.lang.String)' is never used
Warning:(56, 35) Method 'getSocietaireCompteByPersonnelId(java.lang.Integer)' is never used
Warning:(64, 31) Method 'getAllSocietaireComptes()' is never used
Warning:(76, 25) Method 'updateSocietaireCompte(main.java.com.miage.parcauto.dto.SocietaireCompteDTO)' is never used
Warning:(85, 10) Method 'deleteSocietaireCompte(java.lang.Integer)' is never used
Warning:(96, 10) Method 'updateSolde(java.lang.Integer, java.math.BigDecimal)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\exception\DuplicateEntityException.java
Warning:(9, 12) Constructor 'DuplicateEntityException(java.lang.String, java.lang.Throwable)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\impl\NotificationMapperImpl.java
Error:(5, 43) Cannot resolve symbol 'Notification'
Error:(20, 34) Cannot resolve symbol 'Notification'
Error:(25, 44) Cannot resolve method 'getIdNotification()'
Error:(26, 43) Cannot resolve method 'getIdUtilisateur()'
Error:(27, 35) Cannot resolve method 'getTitre()'
Error:(28, 37) Cannot resolve method 'getMessage()'
Error:(29, 42) Cannot resolve method 'getDateCreation()'
Error:(30, 13) Cannot resolve method 'setEstLue' in 'NotificationDTO'
Error:(30, 36) Cannot resolve method 'isEstLue()'
Error:(31, 13) Cannot resolve method 'setTypeNotification' in 'NotificationDTO'
Error:(31, 46) Cannot resolve method 'getTypeNotification()'
Error:(32, 13) Cannot resolve method 'setIdEntiteLiee' in 'NotificationDTO'
Error:(32, 42) Cannot resolve method 'getIdEntiteLiee()'
Error:(33, 13) Cannot resolve method 'setTypeEntiteLiee' in 'NotificationDTO'
Error:(33, 44) Cannot resolve method 'getTypeEntiteLiee()'
Error:(41, 12) Cannot resolve symbol 'Notification'
Error:(45, 9) Cannot resolve symbol 'Notification'
Error:(45, 35) Cannot resolve symbol 'Notification'
Error:(46, 16) Cannot resolve method 'setIdNotification(Integer)'
Error:(47, 16) Cannot resolve method 'setIdUtilisateur(Integer)'
Error:(48, 16) Cannot resolve method 'setTitre(String)'
Error:(49, 16) Cannot resolve method 'setMessage(String)'
Error:(50, 16) Cannot resolve method 'setDateCreation(LocalDateTime)'
Error:(51, 16) Cannot resolve method 'setEstLue(?)'
Error:(51, 30) Cannot resolve method 'isEstLue' in 'NotificationDTO'
Error:(52, 16) Cannot resolve method 'setTypeNotification(?)'
Error:(52, 40) Cannot resolve method 'getTypeNotification' in 'NotificationDTO'
Error:(53, 16) Cannot resolve method 'setIdEntiteLiee(?)'
Error:(53, 36) Cannot resolve method 'getIdEntiteLiee' in 'NotificationDTO'
Error:(54, 16) Cannot resolve method 'setTypeEntiteLiee(?)'
Error:(54, 38) Cannot resolve method 'getTypeEntiteLiee' in 'NotificationDTO'
Error:(62, 49) Cannot resolve symbol 'Notification'
Error:(75, 17) Cannot resolve symbol 'Notification'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\exception\SocietaireNotFoundException.java
Warning:(9, 12) Constructor 'SocietaireNotFoundException(java.lang.String, java.lang.Throwable)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\exception\UtilisateurNotFoundException.java
Warning:(9, 12) Constructor 'UtilisateurNotFoundException(java.lang.String, java.lang.Throwable)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\ValidationServiceImpl.java
Warning:(8, 1) Unused import statement
Warning:(16, 14) Class 'ValidationServiceImpl' is never used
Error:(44, 25) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(44, 67) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(46, 32) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(81, 26) Cannot resolve method 'getDateEntree' in 'EntretienDTO'
Error:(84, 26) Cannot resolve method 'getMotif' in 'EntretienDTO'
Error:(84, 61) Cannot resolve method 'getMotif' in 'EntretienDTO'
Error:(87, 26) Cannot resolve method 'getDateSortie' in 'EntretienDTO'
Error:(87, 66) Cannot resolve method 'getDateEntree' in 'EntretienDTO'
Error:(87, 103) Cannot resolve method 'getDateSortie' in 'EntretienDTO'
Error:(90, 26) Cannot resolve method 'getCoutEstime' in 'EntretienDTO'
Error:(90, 66) Cannot resolve method 'getCoutEstime' in 'EntretienDTO'
Error:(93, 26) Cannot resolve method 'getCoutReel' in 'EntretienDTO'
Error:(93, 64) Cannot resolve method 'getCoutReel' in 'EntretienDTO'
Error:(113, 24) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(116, 24) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(116, 65) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(116, 99) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(119, 24) Cannot resolve method 'getDateFinEffective' in 'MissionDTO'
Error:(119, 68) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(119, 102) Cannot resolve method 'getDateFinEffective' in 'MissionDTO'
Error:(145, 70) Cannot resolve method 'trim' in 'NatureDepenseMission'
Error:(151, 24) Cannot resolve method 'getDateDepense' in 'DepenseMissionDTO'
Error:(175, 32) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(175, 74) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(177, 39) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(180, 78) Cannot resolve method 'trim' in 'Role'
Error:(184, 32) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(184, 75) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(184, 119) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(202, 26) Cannot resolve method 'getNom' in 'PersonnelDTO'
Error:(202, 59) Cannot resolve method 'getNom' in 'PersonnelDTO'
Error:(205, 26) Cannot resolve method 'getPrenom' in 'PersonnelDTO'
Error:(205, 62) Cannot resolve method 'getPrenom' in 'PersonnelDTO'
Error:(214, 26) Cannot resolve method 'getIdService' in 'PersonnelDTO'
Error:(217, 26) Cannot resolve method 'getIdFonction' in 'PersonnelDTO'
Error:(260, 26) Cannot resolve method 'getIdSocietaireCompte' in 'MouvementDTO'
Error:(263, 26) Cannot resolve method 'getTypeMouvement' in 'MouvementDTO'
Error:(263, 69) Cannot resolve method 'getTypeMouvement' in 'MouvementDTO'
Error:(269, 26) Cannot resolve method 'getDateMouvement' in 'MouvementDTO'
Error:(289, 25) Cannot resolve method 'getTypeDocument' in 'DocumentSocietaireDTO'
Error:(289, 66) Cannot resolve method 'getTypeDocument' in 'DocumentSocietaireDTO'
Error:(312, 26) Cannot resolve method 'getDateDebut' in 'AssuranceDTO'
Error:(315, 26) Cannot resolve method 'getDateFin' in 'AssuranceDTO'
Error:(318, 26) Cannot resolve method 'getDateDebut' in 'AssuranceDTO'
Error:(318, 65) Cannot resolve method 'getDateFin' in 'AssuranceDTO'
Error:(318, 102) Cannot resolve method 'getDateDebut' in 'AssuranceDTO'
Error:(318, 138) Cannot resolve method 'getDateFin' in 'AssuranceDTO'
Error:(321, 26) Cannot resolve method 'getCout' in 'AssuranceDTO'
Error:(321, 60) Cannot resolve method 'getCout' in 'AssuranceDTO'
Error:(341, 28) Cannot resolve method 'getTypeAffectation' in 'AffectationDTO'
Error:(341, 75) Cannot resolve method 'getTypeAffectation' in 'AffectationDTO'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\MouvementMapper.java
Warning:(10, 18) Interface 'MouvementMapper' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\SocietaireCompteMapper.java
Warning:(10, 18) Interface 'SocietaireCompteMapper' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\FinanceMapper.java
Warning:(9, 18) Interface 'FinanceMapper' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\ReportingServiceImpl.java
Warning:(14, 1) Unused import statement
Warning:(19, 14) Class 'ReportingServiceImpl' is never used
Warning:(29, 12) Constructor 'ReportingServiceImpl()' is never used
Warning:(44, 12) Constructor 'ReportingServiceImpl(main.java.com.miage.parcauto.service.VehiculeService, main.java.com.miage.parcauto.service.EntretienService, main.java.com.miage.parcauto.service.MissionService)' is never used
Error:(63, 17) Cannot resolve method 'setEntretiens' in 'RapportVehiculeDTO'
Error:(66, 17) Cannot resolve method 'setMissions' in 'RapportVehiculeDTO'
Error:(85, 98) Cannot resolve method 'getLibelleEtatVoiture' in 'VehiculeDTO'
Error:(86, 15) Cannot resolve method 'setNombreVehiculesDisponibles' in 'BilanFlotteDTO'
Error:(88, 96) Cannot resolve method 'getLibelleEtatVoiture' in 'VehiculeDTO'
Error:(91, 100) Cannot resolve method 'getLibelleEtatVoiture' in 'VehiculeDTO'
Error:(109, 38) Cannot resolve method 'getLibelleEtatVoiture' in 'VehiculeDTO'
Error:(113, 29) Cannot resolve method 'getEntretiens' in 'RapportVehiculeDTO'
Error:(113, 72) Cannot resolve method 'getEntretiens' in 'RapportVehiculeDTO'
Error:(116, 51) Cannot resolve method 'getEntretiens' in 'RapportVehiculeDTO'
Error:(118, 61) Cannot resolve method 'getDateEntree' in 'EntretienDTO'
Error:(119, 55) Cannot resolve method 'getMotif' in 'EntretienDTO'
Error:(120, 59) Cannot resolve method 'getCoutReel' in 'EntretienDTO'
Error:(120, 85) Cannot resolve method 'getCoutReel' in 'EntretienDTO'
Error:(127, 29) Cannot resolve method 'getMissions' in 'RapportVehiculeDTO'
Error:(127, 70) Cannot resolve method 'getMissions' in 'RapportVehiculeDTO'
Error:(130, 49) Cannot resolve method 'getMissions' in 'RapportVehiculeDTO'
Error:(132, 57) Cannot resolve method 'getLibelle' in 'MissionDTO'
Error:(133, 55) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(134, 60) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(135, 56) Cannot resolve method 'getStatut' in 'MissionDTO'
Error:(147, 65) Cannot resolve method 'getNombreVehiculesDisponibles' in 'BilanFlotteDTO'
Warning:(164, 9) 'if' statement can be replaced with 'switch' statement
Error:(164, 13) Inconvertible types; cannot cast 'main.java.com.miage.parcauto.dto.RapportDTO' to 'main.java.com.miage.parcauto.dto.RapportVehiculeDTO'
Warning:(164, 13) Condition 'rapportDTO instanceof RapportVehiculeDTO' is always 'false'
Error:(165, 56) Inconvertible types; cannot cast 'main.java.com.miage.parcauto.dto.RapportDTO' to 'main.java.com.miage.parcauto.dto.RapportVehiculeDTO'
Error:(166, 20) Inconvertible types; cannot cast 'main.java.com.miage.parcauto.dto.RapportDTO' to 'main.java.com.miage.parcauto.dto.BilanFlotteDTO'
Warning:(166, 20) Condition 'rapportDTO instanceof BilanFlotteDTO' is always 'false'
Error:(167, 52) Inconvertible types; cannot cast 'main.java.com.miage.parcauto.dto.RapportDTO' to 'main.java.com.miage.parcauto.dto.BilanFlotteDTO'
Error:(168, 20) Inconvertible types; cannot cast 'main.java.com.miage.parcauto.dto.RapportDTO' to 'main.java.com.miage.parcauto.dto.TcoVehiculeDTO'
Warning:(168, 20) Condition 'rapportDTO instanceof TcoVehiculeDTO' is always 'false'
Warning:(194, 70) Exception 'main.java.com.miage.parcauto.exception.ReportGenerationException' is never thrown in the method
Error:(198, 13) Inconvertible types; cannot cast 'main.java.com.miage.parcauto.dto.RapportDTO' to 'main.java.com.miage.parcauto.dto.RapportVehiculeDTO'
Warning:(198, 13) Condition 'rapportDTO instanceof RapportVehiculeDTO' is always 'false'
Error:(199, 53) Inconvertible types; cannot cast 'main.java.com.miage.parcauto.dto.RapportDTO' to 'main.java.com.miage.parcauto.dto.RapportVehiculeDTO'
Error:(200, 20) Inconvertible types; cannot cast 'main.java.com.miage.parcauto.dto.RapportDTO' to 'main.java.com.miage.parcauto.dto.BilanFlotteDTO'
Warning:(200, 20) Condition 'rapportDTO instanceof BilanFlotteDTO' is always 'false'
Error:(201, 49) Inconvertible types; cannot cast 'main.java.com.miage.parcauto.dto.RapportDTO' to 'main.java.com.miage.parcauto.dto.BilanFlotteDTO'
Error:(218, 47) Cannot resolve method 'getLibelleEtatVoiture' in 'VehiculeDTO'
Error:(223, 21) Cannot resolve method 'getEntretiens' in 'RapportVehiculeDTO'
Error:(223, 57) Cannot resolve method 'getEntretiens' in 'RapportVehiculeDTO'
Error:(224, 43) Cannot resolve method 'getEntretiens' in 'RapportVehiculeDTO'
Error:(226, 35) Cannot resolve method 'getDateEntree' in 'EntretienDTO'
Error:(227, 48) Cannot resolve method 'getMotif' in 'EntretienDTO'
Error:(228, 35) Cannot resolve method 'getCoutReel' in 'EntretienDTO'
Error:(228, 61) Cannot resolve method 'getCoutReel' in 'EntretienDTO'
Error:(238, 21) Cannot resolve method 'getMissions' in 'RapportVehiculeDTO'
Error:(238, 55) Cannot resolve method 'getMissions' in 'RapportVehiculeDTO'
Error:(239, 41) Cannot resolve method 'getMissions' in 'RapportVehiculeDTO'
Error:(241, 48) Cannot resolve method 'getLibelle' in 'MissionDTO'
Error:(242, 35) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(243, 35) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(244, 35) Cannot resolve method 'getStatut' in 'MissionDTO'
Error:(256, 59) Cannot resolve method 'getNombreVehiculesDisponibles' in 'BilanFlotteDTO'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\ValidationService.java
Warning:(18, 10) Method 'validateVehicule(main.java.com.miage.parcauto.dto.VehiculeDTO)' is never used
Warning:(25, 10) Method 'validateEntretien(main.java.com.miage.parcauto.dto.EntretienDTO)' is never used
Warning:(32, 10) Method 'validateMission(main.java.com.miage.parcauto.dto.MissionDTO)' is never used
Warning:(39, 10) Method 'validateDepenseMission(main.java.com.miage.parcauto.dto.DepenseMissionDTO)' is never used
Warning:(47, 10) Method 'validateUtilisateur(main.java.com.miage.parcauto.dto.UtilisateurDTO, boolean)' is never used
Warning:(54, 10) Method 'validatePersonnel(main.java.com.miage.parcauto.dto.PersonnelDTO)' is never used
Warning:(61, 10) Method 'validateSocietaireCompte(main.java.com.miage.parcauto.dto.SocietaireCompteDTO)' is never used
Warning:(68, 10) Method 'validateMouvement(main.java.com.miage.parcauto.dto.MouvementDTO)' is never used
Warning:(75, 10) Method 'validateDocumentSocietaire(main.java.com.miage.parcauto.dto.DocumentSocietaireDTO)' is never used
Warning:(82, 10) Method 'validateAssurance(main.java.com.miage.parcauto.dto.AssuranceDTO)' is never used
Warning:(89, 10) Method 'validateAffectation(main.java.com.miage.parcauto.dto.AffectationDTO)' is never used
Warning:(96, 10) Method 'validateNotification(main.java.com.miage.parcauto.dto.NotificationDTO)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\DocumentSocietaireService.java
Warning:(16, 18) Interface 'DocumentSocietaireService' is never used
Warning:(27, 27) Method 'createDocumentSocietaire(main.java.com.miage.parcauto.dto.DocumentSocietaireDTO)' is never used
Warning:(36, 37) Method 'getDocumentSocietaireById(java.lang.Integer)' is never used
Warning:(46, 33) Method 'getDocumentsByCompteSocietaireId(java.lang.Integer)' is never used
Warning:(57, 33) Method 'getDocumentsByCompteSocietaireIdAndType(java.lang.Integer, main.java.com.miage.parcauto.model.document.TypeDocument)' is never used
Warning:(69, 27) Method 'updateDocumentSocietaire(main.java.com.miage.parcauto.dto.DocumentSocietaireDTO)' is never used
Warning:(79, 10) Method 'deleteDocumentSocietaire(java.lang.Integer)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\impl\PersonnelMapperImpl.java
Error:(21, 13) Cannot resolve method 'setIdService' in 'PersonnelDTO'
Error:(21, 36) Cannot resolve method 'getIdService' in 'Personnel'
Error:(22, 13) Cannot resolve method 'setIdFonction' in 'PersonnelDTO'
Error:(22, 37) Cannot resolve method 'getIdFonction' in 'Personnel'
Error:(27, 13) Cannot resolve method 'setNom' in 'PersonnelDTO'
Error:(27, 30) Cannot resolve method 'getNom' in 'Personnel'
Error:(28, 13) Cannot resolve method 'setPrenom' in 'PersonnelDTO'
Error:(28, 33) Cannot resolve method 'getPrenom' in 'Personnel'
Error:(33, 71) Incompatible types. Found: 'java.lang.String', required: 'main.java.com.miage.parcauto.model.rh.Sexe'
Error:(35, 13) Cannot resolve method 'setDateEmbauche' in 'PersonnelDTO'
Error:(35, 39) Cannot resolve method 'getDateEmbauche' in 'Personnel'
Error:(36, 13) Cannot resolve method 'setObservation' in 'PersonnelDTO'
Error:(36, 38) Cannot resolve method 'getObservation' in 'Personnel'
Error:(47, 16) Cannot resolve method 'setIdService' in 'Personnel'
Error:(47, 33) Cannot resolve method 'getIdService' in 'PersonnelDTO'
Error:(48, 16) Cannot resolve method 'setIdFonction' in 'Personnel'
Error:(48, 34) Cannot resolve method 'getIdFonction' in 'PersonnelDTO'
Error:(51, 16) Cannot resolve method 'setNom' in 'Personnel'
Error:(51, 27) Cannot resolve method 'getNom' in 'PersonnelDTO'
Error:(52, 16) Cannot resolve method 'setPrenom' in 'Personnel'
Error:(52, 30) Cannot resolve method 'getPrenom' in 'PersonnelDTO'
Error:(59, 44) 'valueOf(java.lang.String)' in 'main.java.com.miage.parcauto.model.rh.Sexe' cannot be applied to '(main.java.com.miage.parcauto.model.rh.Sexe)'
Error:(65, 16) Cannot resolve method 'setDateEmbauche' in 'Personnel'
Error:(65, 36) Cannot resolve method 'getDateEmbauche' in 'PersonnelDTO'
Error:(66, 16) Cannot resolve method 'setObservation' in 'Personnel'
Error:(66, 35) Cannot resolve method 'getObservation' in 'PersonnelDTO'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\impl\MissionMapperImpl.java
Error:(21, 35) Cannot resolve method 'getIdVehicule' in 'Mission'
Error:(23, 13) Cannot resolve method 'setLibelle' in 'MissionDTO'
Error:(23, 32) Cannot resolve method 'getLibelle' in 'Mission'
Error:(24, 13) Cannot resolve method 'setSiteDestination' in 'MissionDTO'
Error:(25, 13) Cannot resolve method 'setDateDebut' in 'MissionDTO'
Error:(26, 13) Cannot resolve method 'setDateFinPrevue' in 'MissionDTO'
Error:(27, 13) Cannot resolve method 'setDateFinEffective' in 'MissionDTO'
Error:(27, 41) Cannot resolve method 'getDateFinEffective' in 'Mission'
Error:(30, 13) Cannot resolve method 'setStatut' in 'MissionDTO'
Error:(30, 31) Cannot resolve method 'getStatut' in 'Mission'
Error:(30, 61) Cannot resolve method 'getStatut' in 'Mission'
Error:(31, 13) Cannot resolve method 'setCoutEstime' in 'MissionDTO'
Error:(31, 35) Cannot resolve method 'getCoutEstime' in 'Mission'
Error:(32, 13) Cannot resolve method 'setCoutTotalReel' in 'MissionDTO'
Error:(33, 13) Cannot resolve method 'setCircuit' in 'MissionDTO'
Error:(34, 13) Cannot resolve method 'setObservations' in 'MissionDTO'
Error:(45, 16) Cannot resolve method 'setIdVehicule' in 'Mission'
Error:(47, 16) Cannot resolve method 'setLibelle' in 'Mission'
Error:(47, 31) Cannot resolve method 'getLibelle' in 'MissionDTO'
Error:(48, 28) Cannot resolve method 'getSiteDestination' in 'MissionDTO'
Error:(49, 40) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(50, 38) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(51, 16) Cannot resolve method 'setDateFinEffective' in 'Mission'
Error:(51, 40) Cannot resolve method 'getDateFinEffective' in 'MissionDTO'
Error:(54, 17) Cannot resolve method 'getStatut' in 'MissionDTO'
Error:(56, 24) Cannot resolve method 'setStatut' in 'Mission'
Error:(56, 60) Cannot resolve method 'getStatut' in 'MissionDTO'
Error:(58, 82) Cannot resolve method 'getStatut' in 'MissionDTO'
Error:(61, 16) Cannot resolve method 'setCoutEstime' in 'Mission'
Error:(61, 34) Cannot resolve method 'getCoutEstime' in 'MissionDTO'
Error:(62, 33) Cannot resolve method 'getCoutTotalReel' in 'MissionDTO'
Error:(63, 38) Cannot resolve method 'getCircuit' in 'MissionDTO'
Error:(64, 42) Cannot resolve method 'getObservations' in 'MissionDTO'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\impl\AssuranceMapperImpl.java
Error:(26, 13) Cannot resolve method 'setDateDebut' in 'AssuranceDTO'
Error:(27, 13) Cannot resolve method 'setDateFin' in 'AssuranceDTO'
Error:(29, 13) Cannot resolve method 'setCout' in 'AssuranceDTO'
Error:(43, 42) Cannot resolve method 'getDateDebut' in 'AssuranceDTO'
Error:(44, 40) Cannot resolve method 'getDateFin' in 'AssuranceDTO'
Error:(46, 37) Cannot resolve method 'getCout' in 'AssuranceDTO'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\FinanceReportingServiceImpl.java
Error:(14, 51) Cannot resolve symbol 'CoutEntretienParVehicule'
Warning:(31, 14) Class 'FinanceReportingServiceImpl' is never used
Warning:(36, 37) Private field 'missionRepository' is assigned but never accessed
Warning:(38, 39) Private field 'assuranceRepository' is assigned but never accessed
Warning:(45, 12) Constructor 'FinanceReportingServiceImpl()' is never used
Warning:(58, 12) Constructor 'FinanceReportingServiceImpl(main.java.com.miage.parcauto.dao.FinanceRepository, main.java.com.miage.parcauto.dao.VehiculeRepository, main.java.com.miage.parcauto.dao.EntretienRepository, main.java.com.miage.parcauto.dao.MissionRepository, main.java.com.miage.parcauto.dao.DepenseMissionRepository, main.java.com.miage.parcauto.dao.AssuranceRepository, main.java.com.miage.parcauto.dao.CouvrirRepository)' is never used
Error:(80, 54) Incompatible types. Found: 'main.java.com.miage.parcauto.dto.BilanFinancierDTO', required: 'main.java.com.miage.parcauto.model.finance.BilanFinancier'
Warning:(83, 36) Method invocation 'getDateDebut' will produce 'NullPointerException'
Error:(85, 39) Cannot resolve method 'getTotalRevenus' in 'BilanFinancier'
Error:(86, 17) Cannot resolve method 'setTotalDepenses' in 'BilanFinancierDTO'
Error:(86, 40) Cannot resolve method 'getTotalDepenses' in 'BilanFinancier'
Error:(87, 17) Cannot resolve method 'setProfitOuPerte' in 'BilanFinancierDTO'
Error:(87, 40) Cannot resolve method 'getProfitOuPerte' in 'BilanFinancier'
Error:(105, 18) Cannot resolve symbol 'CoutEntretienParVehicule'
Error:(105, 70) Cannot resolve method 'getCoutEntretienParVehiculeAnnee' in 'FinanceRepository'
Error:(109, 40) Cannot resolve method 'getIdVehicule()'
Error:(110, 21) Cannot resolve method 'setImmatriculationVehicule' in 'CoutEntretienDTO'
Error:(110, 53) Cannot resolve method 'getImmatriculationVehicule()'
Error:(111, 21) Cannot resolve method 'setAnnee' in 'CoutEntretienDTO'
Error:(111, 35) Cannot resolve method 'getAnnee()'
Error:(112, 21) Cannot resolve method 'setTotalCoutsEntretien' in 'CoutEntretienDTO'
Error:(112, 49) Cannot resolve method 'getTotalCoutsEntretien()'
Error:(130, 61) Expected 1 argument but found 2
Error:(135, 17) Cannot resolve method 'setImmatriculation' in 'TcoVehiculeDTO'
Error:(136, 17) Cannot resolve method 'setMarque' in 'TcoVehiculeDTO'
Error:(137, 17) Cannot resolve method 'setModele' in 'TcoVehiculeDTO'
Error:(140, 17) Cannot resolve method 'setCoutAchat' in 'TcoVehiculeDTO'
Error:(144, 36) Cannot resolve method 'getCoutReel' in 'Entretien'
Error:(145, 37) Cannot resolve method 'getCoutReel'
Error:(146, 58) Cannot resolve method 'add'
Error:(147, 17) Cannot resolve method 'setCoutTotalEntretiens' in 'TcoVehiculeDTO'
Error:(149, 78) Cannot resolve method 'findByVehiculeId' in 'DepenseMissionRepository'
Error:(154, 17) Cannot resolve method 'setCoutTotalCarburant' in 'TcoVehiculeDTO'
Error:(160, 17) Cannot resolve method 'setCoutTotalAutresDepenses' in 'TcoVehiculeDTO'
Error:(163, 60) Cannot resolve method 'findAssurancesByIdVehicule' in 'CouvrirRepository'
Warning:(166, 22) 'filter()' and 'map()' can be swapped
Error:(168, 17) Cannot resolve method 'setCoutTotalAssurances' in 'TcoVehiculeDTO'
Error:(175, 47) Cannot resolve method 'getDateAmortissement' in 'Vehicule'
Error:(176, 117) Cannot resolve method 'getDateAmortissement' in 'Vehicule'
Warning:(179, 34) Call to 'BigDecimal.divide()' can use 'RoundingMode' enum constant
Warning:(179, 34) 'divide(java.math.BigDecimal, int, int)' is deprecated since version 9
Warning:(179, 99) 'ROUND_HALF_UP' is deprecated since version 9
Error:(183, 17) Cannot resolve method 'setDepreciation' in 'TcoVehiculeDTO'
Error:(191, 17) Cannot resolve method 'setTcoTotal' in 'TcoVehiculeDTO'
Warning:(194, 43) Call to 'BigDecimal.divide()' can use 'RoundingMode' enum constant
Warning:(194, 43) 'divide(java.math.BigDecimal, int, int)' is deprecated since version 9
Warning:(194, 109) 'ROUND_HALF_UP' is deprecated since version 9
Error:(216, 19) Cannot resolve method 'setDateDebut' in 'TCODTO'
Error:(217, 19) Cannot resolve method 'setDateFin' in 'TCODTO'
Error:(218, 19) Cannot resolve method 'setNombreVehicules' in 'TCODTO'
Error:(219, 19) Cannot resolve method 'setCoutTotalAchats' in 'TCODTO'
Error:(220, 19) Cannot resolve method 'setCoutTotalEntretiens' in 'TCODTO'
Error:(221, 19) Cannot resolve method 'setCoutTotalCarburant' in 'TCODTO'
Error:(222, 19) Cannot resolve method 'setCoutTotalAssurances' in 'TCODTO'
Error:(223, 19) Cannot resolve method 'setDepreciationTotale' in 'TCODTO'
Error:(224, 19) Cannot resolve method 'setTcoGlobalFlotte' in 'TCODTO'
Error:(225, 19) Cannot resolve method 'setCoutMoyenParKmFlotte' in 'TCODTO'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\impl\DepenseMissionMapperImpl.java
Error:(21, 41) Cannot resolve method 'getIdMission' in 'DepenseMission'
Error:(22, 87) Incompatible types. Found: 'java.lang.String', required: 'main.java.com.miage.parcauto.model.mission.NatureDepenseMission'
Error:(24, 13) Cannot resolve method 'setJustificatifPath' in 'DepenseMissionDTO'
Error:(25, 13) Cannot resolve method 'setDateDepense' in 'DepenseMissionDTO'
Error:(25, 43) Cannot resolve method 'getDateDepense' in 'DepenseMission'
Error:(36, 16) Cannot resolve method 'setIdMission' in 'DepenseMission'
Error:(39, 62) 'valueOf(java.lang.String)' in 'main.java.com.miage.parcauto.model.mission.NatureDepenseMission' cannot be applied to '(main.java.com.miage.parcauto.model.mission.NatureDepenseMission)'
Error:(45, 36) Cannot resolve method 'getJustificatifPath' in 'DepenseMissionDTO'
Error:(46, 16) Cannot resolve method 'setDateDepense' in 'DepenseMission'
Error:(46, 35) Cannot resolve method 'getDateDepense' in 'DepenseMissionDTO'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\impl\EntretienMapperImpl.java
Error:(22, 37) Cannot resolve method 'getIdVehicule' in 'Entretien'
Error:(23, 13) Cannot resolve method 'setDateEntree' in 'EntretienDTO'
Error:(23, 37) Cannot resolve method 'getDateEntree' in 'Entretien'
Error:(24, 13) Cannot resolve method 'setDateSortie' in 'EntretienDTO'
Error:(24, 37) Cannot resolve method 'getDateSortie' in 'Entretien'
Error:(25, 13) Cannot resolve method 'setMotif' in 'EntretienDTO'
Error:(25, 32) Cannot resolve method 'getMotif' in 'Entretien'
Error:(26, 13) Cannot resolve method 'setObservations' in 'EntretienDTO'
Error:(26, 39) Cannot resolve method 'getObservations' in 'Entretien'
Error:(27, 13) Cannot resolve method 'setCoutEstime' in 'EntretienDTO'
Error:(27, 37) Cannot resolve method 'getCoutEstime' in 'Entretien'
Error:(28, 13) Cannot resolve method 'setCoutReel' in 'EntretienDTO'
Error:(28, 35) Cannot resolve method 'getCoutReel' in 'Entretien'
Error:(29, 13) Cannot resolve method 'setLieu' in 'EntretienDTO'
Error:(29, 31) Cannot resolve method 'getLieu' in 'Entretien'
Error:(30, 13) Cannot resolve method 'setTypeEntretien' in 'EntretienDTO'
Error:(31, 83) Incompatible types. Found: 'java.lang.String', required: 'main.java.com.miage.parcauto.model.entretien.StatutOT'
Error:(47, 16) Cannot resolve method 'setIdVehicule' in 'Entretien'
Error:(48, 16) Cannot resolve method 'setDateEntree' in 'Entretien'
Error:(48, 34) Cannot resolve method 'getDateEntree' in 'EntretienDTO'
Error:(49, 16) Cannot resolve method 'setDateSortie' in 'Entretien'
Error:(49, 34) Cannot resolve method 'getDateSortie' in 'EntretienDTO'
Error:(50, 16) Cannot resolve method 'setMotif' in 'Entretien'
Error:(50, 29) Cannot resolve method 'getMotif' in 'EntretienDTO'
Error:(51, 16) Cannot resolve method 'setObservations' in 'Entretien'
Error:(51, 36) Cannot resolve method 'getObservations' in 'EntretienDTO'
Error:(52, 16) Cannot resolve method 'setCoutEstime' in 'Entretien'
Error:(52, 34) Cannot resolve method 'getCoutEstime' in 'EntretienDTO'
Error:(53, 16) Cannot resolve method 'setCoutReel' in 'Entretien'
Error:(53, 32) Cannot resolve method 'getCoutReel' in 'EntretienDTO'
Error:(54, 16) Cannot resolve method 'setLieu' in 'Entretien'
Error:(54, 28) Cannot resolve method 'getLieu' in 'EntretienDTO'
Error:(56, 17) Cannot resolve method 'getTypeEntretien' in 'EntretienDTO'
Error:(58, 58) Cannot resolve method 'getTypeEntretien' in 'EntretienDTO'
Error:(60, 81) Cannot resolve method 'getTypeEntretien' in 'EntretienDTO'
Error:(65, 52) 'valueOf(java.lang.String)' in 'main.java.com.miage.parcauto.model.entretien.StatutOT' cannot be applied to '(main.java.com.miage.parcauto.model.entretien.StatutOT)'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\AffectationServiceImpl.java
Warning:(21, 14) Class 'AffectationServiceImpl' is never used
Warning:(33, 12) Constructor 'AffectationServiceImpl()' is never used
Warning:(45, 12) Constructor 'AffectationServiceImpl(main.java.com.miage.parcauto.dao.AffectationRepository, main.java.com.miage.parcauto.dao.VehiculeRepository, main.java.com.miage.parcauto.dao.PersonnelRepository, main.java.com.miage.parcauto.dao.SocietaireCompteRepository, main.java.com.miage.parcauto.mapper.AffectationMapper, main.java.com.miage.parcauto.service.VehiculeService)' is never used
Warning:(61, 104) There is a more general exception, 'main.java.com.miage.parcauto.exception.EntityNotFoundException', in the throws list already.
Error:(62, 96) Cannot resolve method 'getTypeAffectation' in 'AffectationDTO'
Error:(80, 45) Expected 1 argument but found 2
Error:(83, 89) Expected 1 argument but found 2
Error:(86, 97) Expected 1 argument but found 2
Warning:(92, 31) '!Stream.anyMatch(...)' can be replaced with 'noneMatch()'
Error:(98, 71) Expected 1 argument but found 2
Error:(102, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(117, 51) Expected 1 argument but found 2
Error:(134, 77) Cannot resolve method 'findAll(Connection)'
Error:(150, 45) Expected 1 argument but found 2
Error:(169, 46) Expected 1 argument but found 2
Error:(188, 53) Expected 1 argument but found 2
Error:(205, 54) Cannot resolve method 'getIdAffectation' in 'AffectationDTO'
Error:(218, 99) Cannot resolve method 'getIdAffectation' in 'AffectationDTO'
Error:(219, 123) Cannot resolve method 'getIdAffectation' in 'AffectationDTO'
Error:(221, 118) Cannot resolve method 'getIdVehicule' in 'Affectation'
Error:(222, 49) Expected 1 argument but found 2
Error:(225, 37) Cannot resolve method 'setIdVehicule' in 'Affectation'
Error:(227, 81) Cannot resolve method 'getIdPersonnel' in 'Affectation'
Error:(227, 169) Cannot resolve method 'getIdPersonnel' in 'Affectation'
Error:(228, 50) Expected 1 argument but found 2
Error:(231, 37) Cannot resolve method 'setIdPersonnel' in 'Affectation'
Error:(232, 37) Cannot resolve method 'setIdSocietaire' in 'Affectation'
Error:(233, 89) Cannot resolve method 'getIdSocietaire' in 'Affectation'
Error:(233, 179) Cannot resolve method 'getIdSocietaire' in 'Affectation'
Error:(234, 57) Expected 1 argument but found 2
Error:(237, 37) Cannot resolve method 'setIdSocietaire' in 'Affectation'
Error:(238, 37) Cannot resolve method 'setIdPersonnel' in 'Affectation'
Error:(242, 31) Cannot resolve method 'getTypeAffectation' in 'AffectationDTO'
Error:(242, 175) Cannot resolve method 'getTypeAffectation' in 'AffectationDTO'
Error:(247, 75) Expected 1 argument but found 2
Error:(251, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(268, 48) Expected 1 argument but found 2
Error:(272, 60) Expected 1 argument but found 2
Error:(278, 20) Cannot resolve method 'rollback' in 'DbUtil'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\impl\AffectationMapperImpl.java
Error:(26, 13) Cannot resolve method 'setIdAffectation' in 'AffectationDTO'
Error:(27, 39) Cannot resolve method 'getIdVehicule' in 'Affectation'
Error:(28, 40) Cannot resolve method 'getIdPersonnel' in 'Affectation'
Error:(29, 41) Cannot resolve method 'getIdSocietaire' in 'Affectation'
Error:(30, 13) Cannot resolve method 'setTypeAffectation' in 'AffectationDTO'
Error:(45, 26) Cannot resolve method 'getIdAffectation' in 'AffectationDTO'
Error:(46, 16) Cannot resolve method 'setIdVehicule' in 'Affectation'
Error:(47, 16) Cannot resolve method 'setIdPersonnel' in 'Affectation'
Error:(48, 16) Cannot resolve method 'setIdSocietaire' in 'Affectation'
Error:(49, 17) Cannot resolve method 'getTypeAffectation' in 'AffectationDTO'
Error:(51, 60) Cannot resolve method 'getTypeAffectation' in 'AffectationDTO'
Error:(53, 83) Cannot resolve method 'getTypeAffectation' in 'AffectationDTO'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\AffectationService.java
Warning:(24, 20) Method 'createAffectation(main.java.com.miage.parcauto.dto.AffectationDTO)' is never used
Warning:(33, 30) Method 'getAffectationById(java.lang.Integer)' is never used
Warning:(41, 26) Method 'getAllAffectations()' is never used
Warning:(51, 26) Method 'getAffectationsByVehiculeId(java.lang.Integer)' is never used
Warning:(61, 26) Method 'getAffectationsByPersonnelId(java.lang.Integer)' is never used
Warning:(71, 26) Method 'getAffectationsBySocietaireId(java.lang.Integer)' is never used
Warning:(83, 20) Method 'updateAffectation(main.java.com.miage.parcauto.dto.AffectationDTO)' is never used
Warning:(92, 10) Method 'deleteAffectation(java.lang.Integer)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\UtilisateurManagementServiceImpl.java
Warning:(21, 14) Class 'UtilisateurManagementServiceImpl' is never used
Warning:(27, 12) Constructor 'UtilisateurManagementServiceImpl()' is never used
Warning:(33, 12) Constructor 'UtilisateurManagementServiceImpl(main.java.com.miage.parcauto.dao.UtilisateurRepository, main.java.com.miage.parcauto.mapper.UtilisateurMapper)' is never used
Error:(44, 35) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(44, 77) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(64, 78) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(66, 25) Cannot resolve method 'setSalt' in 'Utilisateur'
Error:(68, 25) Cannot resolve method 'setActif' in 'Utilisateur'
Error:(69, 25) Cannot resolve method 'setDateCreation' in 'Utilisateur'
Error:(72, 71) Expected 1 argument but found 2
Error:(77, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(80, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(92, 83) Expected 1 argument but found 2
Error:(120, 75) Cannot resolve method 'findAll(Connection)'
Error:(145, 78) Expected 1 argument but found 2
Error:(158, 61) 'valueOf(java.lang.String)' in 'main.java.com.miage.parcauto.model.utilisateur.Role' cannot be applied to '(main.java.com.miage.parcauto.model.utilisateur.Role)'
Error:(165, 32) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(165, 75) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(167, 82) Cannot resolve method 'getMotDePasse' in 'UtilisateurDTO'
Error:(168, 37) Cannot resolve method 'setSalt' in 'Utilisateur'
Error:(173, 37) Cannot resolve method 'setIdPersonnel' in 'Utilisateur'
Error:(175, 31) Cannot resolve method 'getMfaSecret' in 'UtilisateurDTO'
Error:(176, 65) Cannot resolve method 'getMfaSecret' in 'UtilisateurDTO'
Error:(178, 33) Cannot resolve method 'setActif' in 'Utilisateur'
Error:(178, 57) Cannot resolve method 'isActif' in 'UtilisateurDTO'
Error:(180, 75) Expected 1 argument but found 2
Error:(185, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(188, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(202, 48) Expected 1 argument but found 2
Error:(208, 60) Expected 1 argument but found 2
Error:(215, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(238, 70) Expected 1 argument but found 2
Error:(244, 25) Cannot resolve method 'setSalt' in 'Utilisateur'
Error:(247, 42) Expected 1 argument but found 2
Error:(251, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(254, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(268, 70) Expected 1 argument but found 2
Error:(271, 25) Cannot resolve method 'setActif' in 'Utilisateur'
Error:(272, 42) Expected 1 argument but found 2
Error:(276, 20) Cannot resolve method 'rollback' in 'DbUtil'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\AuthenticationServiceImpl.java
Warning:(20, 14) Class 'AuthenticationServiceImpl' is never used
Warning:(25, 12) Constructor 'AuthenticationServiceImpl()' is never used
Warning:(30, 12) Constructor 'AuthenticationServiceImpl(main.java.com.miage.parcauto.dao.UtilisateurRepository, main.java.com.miage.parcauto.mapper.UtilisateurMapper)' is never used
Error:(54, 30) Cannot resolve method 'isActif' in 'Utilisateur'
Error:(60, 85) Cannot resolve method 'getSalt' in 'Utilisateur'
Error:(69, 25) Cannot resolve method 'setDateDerniereConnexion' in 'Utilisateur'
Error:(70, 42) Expected 1 argument but found 2
Error:(76, 20) Cannot resolve method 'rollback' in 'DbUtil'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\MissionService.java
Warning:(26, 16) Method 'createMission(main.java.com.miage.parcauto.dto.MissionDTO)' is never used
Warning:(35, 26) Method 'getMissionById(java.lang.Integer)' is never used
Warning:(43, 22) Method 'getAllMissions()' is never used
Warning:(63, 22) Method 'getMissionsByPersonnelId(java.lang.Integer)' is never used
Warning:(76, 16) Method 'updateMission(main.java.com.miage.parcauto.dto.MissionDTO)' is never used
Warning:(86, 10) Method 'deleteMission(java.lang.Integer)' is never used
Warning:(100, 16) Method 'terminerMission(java.lang.Integer, int, java.time.LocalDateTime, java.lang.String)' is never used
Warning:(112, 23) Method 'ajouterDepenseAMission(java.lang.Integer, main.java.com.miage.parcauto.dto.DepenseMissionDTO)' is never used
Warning:(122, 29) Method 'getDepensesByMissionId(java.lang.Integer)' is never used
Warning:(131, 22) Method 'getMissionsActivesPourVehicule(java.lang.Integer)' is never used
Warning:(141, 22) Method 'getMissionsParPeriode(java.time.LocalDateTime, java.time.LocalDateTime)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\NotificationService.java
Warning:(25, 10) Method 'createNotification(main.java.com.miage.parcauto.dto.NotificationDTO)' is never used
Warning:(35, 27) Method 'getNotificationsNonLues(java.lang.Integer)' is never used
Warning:(45, 27) Method 'getAllNotificationsForUtilisateur(java.lang.Integer)' is never used
Warning:(55, 10) Method 'marquerNotificationCommeLue(java.lang.Integer)' is never used
Warning:(64, 10) Method 'marquerNotificationsCommeLues(java.util.List<java.lang.Integer>, java.lang.Integer)' is never used
Warning:(73, 10) Method 'deleteNotification(java.lang.Integer)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\NotificationServiceImpl.java
Warning:(23, 14) Class 'NotificationServiceImpl' is never used
Warning:(32, 12) Constructor 'NotificationServiceImpl()' is never used
Warning:(44, 12) Constructor 'NotificationServiceImpl(main.java.com.miage.parcauto.dao.NotificationRepository, main.java.com.miage.parcauto.dao.UtilisateurRepository, main.java.com.miage.parcauto.mapper.NotificationMapper)' is never used
Error:(68, 48) Expected 1 argument but found 2
Error:(72, 60) Incompatible types. Found: 'Notification', required: 'main.java.com.miage.parcauto.model.notification.Notification'
Error:(76, 26) Cannot resolve method 'setEstLue' in 'Notification'
Error:(78, 41) Expected 1 argument but found 2
Error:(81, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(96, 48) Expected 1 argument but found 2
Error:(99, 71) Cannot resolve method 'findNonLuesByUtilisateurId' in 'NotificationRepository'
Error:(100, 48) 'toDTOList(java.util.List<Notification>)' in 'main.java.com.miage.parcauto.mapper.NotificationMapper' cannot be applied to '(java.util.List<main.java.com.miage.parcauto.model.notification.Notification>)'
Error:(116, 48) Expected 1 argument but found 2
Error:(119, 71) Cannot resolve method 'findAllByUtilisateurId' in 'NotificationRepository'
Error:(120, 48) 'toDTOList(java.util.List<Notification>)' in 'main.java.com.miage.parcauto.mapper.NotificationMapper' cannot be applied to '(java.util.List<main.java.com.miage.parcauto.model.notification.Notification>)'
Error:(138, 73) Expected 1 argument but found 2
Error:(141, 31) Cannot resolve method 'isEstLue' in 'Notification'
Error:(142, 30) Cannot resolve method 'setEstLue' in 'Notification'
Error:(143, 47) Expected 1 argument but found 2
Error:(147, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(167, 48) Expected 1 argument but found 2
Error:(172, 17) Cannot resolve symbol 'Optional'
Error:(172, 81) Cannot resolve method 'findByIdAndUtilisateurId' in 'NotificationRepository'
Error:(173, 37) Cannot resolve method 'isPresent()'
Error:(174, 65) Cannot resolve method 'get()'
Error:(175, 39) Cannot resolve method 'isEstLue' in 'Notification'
Error:(176, 38) Cannot resolve method 'setEstLue' in 'Notification'
Error:(177, 55) Expected 1 argument but found 2
Error:(186, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(203, 49) Expected 1 argument but found 2
Error:(207, 61) Expected 1 argument but found 2
Error:(213, 20) Cannot resolve method 'rollback' in 'DbUtil'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\impl\UtilisateurMapperImpl.java
Error:(24, 75) Incompatible types. Found: 'java.lang.String', required: 'main.java.com.miage.parcauto.model.utilisateur.Role'
Error:(25, 40) Cannot resolve method 'getIdPersonnel' in 'Utilisateur'
Error:(26, 13) Cannot resolve method 'setMfaSecret' in 'UtilisateurDTO'
Error:(27, 13) Cannot resolve method 'setActif' in 'UtilisateurDTO'
Error:(27, 34) Cannot resolve method 'isActif' in 'Utilisateur'
Error:(28, 13) Cannot resolve method 'setDateCreation' in 'UtilisateurDTO'
Error:(28, 41) Cannot resolve method 'getDateCreation' in 'Utilisateur'
Error:(29, 13) Cannot resolve method 'setDateDerniereConnexion' in 'UtilisateurDTO'
Error:(29, 50) Cannot resolve method 'getDateDerniereConnexion' in 'Utilisateur'
Error:(45, 44) 'valueOf(java.lang.String)' in 'main.java.com.miage.parcauto.model.utilisateur.Role' cannot be applied to '(main.java.com.miage.parcauto.model.utilisateur.Role)'
Error:(51, 16) Cannot resolve method 'setIdPersonnel' in 'Utilisateur'
Error:(52, 33) Cannot resolve method 'getMfaSecret' in 'UtilisateurDTO'
Error:(53, 16) Cannot resolve method 'setActif' in 'Utilisateur'
Error:(53, 29) Cannot resolve method 'isActif' in 'UtilisateurDTO'
Error:(54, 16) Cannot resolve method 'setDateCreation' in 'Utilisateur'
Error:(54, 36) Cannot resolve method 'getDateCreation' in 'UtilisateurDTO'
Error:(55, 16) Cannot resolve method 'setDateDerniereConnexion' in 'Utilisateur'
Error:(55, 45) Cannot resolve method 'getDateDerniereConnexion' in 'UtilisateurDTO'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\PersonnelServiceImpl.java
Warning:(22, 14) Class 'PersonnelServiceImpl' is never used
Warning:(30, 12) Constructor 'PersonnelServiceImpl()' is never used
Warning:(38, 12) Constructor 'PersonnelServiceImpl(main.java.com.miage.parcauto.dao.PersonnelRepository, main.java.com.miage.parcauto.dao.ServiceRHRepository, main.java.com.miage.parcauto.dao.FonctionRepository, main.java.com.miage.parcauto.mapper.PersonnelMapper)' is never used
Error:(50, 30) Cannot resolve method 'getNom' in 'PersonnelDTO'
Error:(50, 63) Cannot resolve method 'getNom' in 'PersonnelDTO'
Error:(54, 26) Cannot resolve method 'getIdService' in 'PersonnelDTO'
Error:(54, 65) Cannot resolve method 'getIdFonction' in 'PersonnelDTO'
Error:(70, 65) Cannot resolve method 'getIdService' in 'PersonnelDTO'
Error:(71, 84) Cannot resolve method 'getIdService' in 'PersonnelDTO'
Error:(73, 64) Cannot resolve method 'getIdFonction' in 'PersonnelDTO'
Error:(74, 85) Cannot resolve method 'getIdFonction' in 'PersonnelDTO'
Error:(79, 65) Expected 1 argument but found 2
Error:(84, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(96, 77) Expected 1 argument but found 2
Error:(125, 69) Cannot resolve method 'findAll(Connection)'
Error:(146, 72) Expected 1 argument but found 2
Error:(166, 30) Cannot resolve method 'getIdService' in 'PersonnelDTO'
Error:(167, 69) Cannot resolve method 'getIdService' in 'PersonnelDTO'
Error:(168, 88) Cannot resolve method 'getIdService' in 'PersonnelDTO'
Error:(170, 35) Cannot resolve method 'setIdService' in 'Personnel'
Error:(170, 61) Cannot resolve method 'getIdService' in 'PersonnelDTO'
Error:(172, 30) Cannot resolve method 'getIdFonction' in 'PersonnelDTO'
Error:(173, 68) Cannot resolve method 'getIdFonction' in 'PersonnelDTO'
Error:(174, 89) Cannot resolve method 'getIdFonction' in 'PersonnelDTO'
Error:(176, 35) Cannot resolve method 'setIdFonction' in 'Personnel'
Error:(176, 62) Cannot resolve method 'getIdFonction' in 'PersonnelDTO'
Error:(179, 29) Cannot resolve method 'getNom' in 'PersonnelDTO'
Error:(179, 65) Cannot resolve method 'setNom' in 'Personnel'
Error:(179, 85) Cannot resolve method 'getNom' in 'PersonnelDTO'
Error:(180, 29) Cannot resolve method 'getPrenom' in 'PersonnelDTO'
Error:(180, 68) Cannot resolve method 'setPrenom' in 'Personnel'
Error:(180, 91) Cannot resolve method 'getPrenom' in 'PersonnelDTO'
Error:(184, 124) 'valueOf(java.lang.String)' in 'main.java.com.miage.parcauto.model.rh.Sexe' cannot be applied to '(main.java.com.miage.parcauto.model.rh.Sexe)'
Error:(185, 29) Cannot resolve method 'getDateEmbauche' in 'PersonnelDTO'
Error:(185, 74) Cannot resolve method 'setDateEmbauche' in 'Personnel'
Error:(185, 103) Cannot resolve method 'getDateEmbauche' in 'PersonnelDTO'
Error:(186, 29) Cannot resolve method 'getObservation' in 'PersonnelDTO'
Error:(186, 73) Cannot resolve method 'setObservation' in 'Personnel'
Error:(186, 101) Cannot resolve method 'getObservation' in 'PersonnelDTO'
Error:(189, 69) Expected 1 argument but found 2
Error:(194, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(197, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(214, 46) Expected 1 argument but found 2
Error:(220, 58) Expected 1 argument but found 2
Error:(226, 20) Cannot resolve method 'rollback' in 'DbUtil'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\PersonnelService.java
Warning:(26, 18) Method 'createPersonnel(main.java.com.miage.parcauto.dto.PersonnelDTO)' is never used
Warning:(35, 28) Method 'getPersonnelById(java.lang.Integer)' is never used
Warning:(44, 28) Method 'getPersonnelByMatricule(java.lang.String)' is never used
Warning:(52, 24) Method 'getAllPersonnel()' is never used
Warning:(64, 18) Method 'updatePersonnel(main.java.com.miage.parcauto.dto.PersonnelDTO)' is never used
Warning:(73, 10) Method 'deletePersonnel(java.lang.Integer)' is never used
Warning:(81, 24) Method 'getPersonnelByServiceId(java.lang.Integer)' is never used
Warning:(89, 24) Method 'getPersonnelByFonctionId(java.lang.Integer)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\FinanceReportingService.java
Warning:(26, 23) Method 'genererBilanFinancierPeriode(java.time.LocalDate, java.time.LocalDate)' is never used
Warning:(35, 28) Method 'getCoutsEntretiensParVehiculeAnnee(int)' is never used
Warning:(45, 20) Method 'calculerTCOVehicule(java.lang.Integer)' is never used
Warning:(54, 12) Method 'calculerTCOFlotte(java.time.LocalDate, java.time.LocalDate)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\VehiculeService.java
Warning:(27, 17) Method 'createVehicule(main.java.com.miage.parcauto.dto.VehiculeDTO)' is never used
Warning:(56, 17) Method 'updateVehicule(main.java.com.miage.parcauto.dto.VehiculeDTO)' is never used
Warning:(65, 10) Method 'deleteVehicule(java.lang.Integer)' is never used
Warning:(76, 10) Method 'updateKilometrage(java.lang.Integer, int)' is never used
Warning:(97, 23) Method 'getVehiculesRequerantMaintenance()' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\AssuranceService.java
Warning:(28, 18) Method 'createAssurance(main.java.com.miage.parcauto.dto.AssuranceDTO)' is never used
Warning:(37, 28) Method 'getAssuranceByNumCarte(java.lang.Integer)' is never used
Warning:(45, 24) Method 'getAllAssurances()' is never used
Warning:(56, 18) Method 'updateAssurance(main.java.com.miage.parcauto.dto.AssuranceDTO)' is never used
Warning:(66, 10) Method 'deleteAssurance(java.lang.Integer)' is never used
Warning:(77, 10) Method 'lierVehiculeAssurance(java.lang.Integer, java.lang.Integer)' is never used
Warning:(88, 10) Method 'delierVehiculeAssurance(java.lang.Integer, java.lang.Integer)' is never used
Warning:(98, 23) Method 'getVehiculesByAssurance(java.lang.Integer)' is never used
Warning:(108, 24) Method 'getAssurancesByVehicule(java.lang.Integer)' is never used
Warning:(116, 24) Method 'getAssurancesExpirantBientot(java.time.LocalDateTime)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\EntretienService.java
Warning:(28, 18) Method 'createEntretien(main.java.com.miage.parcauto.dto.EntretienDTO)' is never used
Warning:(37, 28) Method 'getEntretienById(java.lang.Integer)' is never used
Warning:(45, 24) Method 'getAllEntretiens()' is never used
Warning:(67, 18) Method 'updateEntretien(main.java.com.miage.parcauto.dto.EntretienDTO)' is never used
Warning:(76, 10) Method 'deleteEntretien(java.lang.Integer)' is never used
Warning:(92, 18) Method 'terminerEntretien(java.lang.Integer, java.time.LocalDateTime, java.math.BigDecimal, java.lang.Integer, java.lang.Integer, java.lang.String)' is never used
Warning:(102, 24) Method 'getEntretiensPlanifiesEntre(java.time.LocalDateTime, java.time.LocalDateTime)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\mapper\impl\VehiculeMapperImpl.java
Warning:(23, 35) Field 'etatVoitureRepository' may be 'final'
Error:(42, 39) Cannot resolve method 'getIdEtatVoiture' in 'Vehicule'
Error:(45, 22) Cannot resolve method 'getIdEtatVoiture' in 'Vehicule'
Error:(49, 95) Cannot resolve method 'getIdEtatVoiture' in 'Vehicule'
Error:(50, 54) Cannot resolve method 'setLibelleEtatVoiture' in 'VehiculeDTO'
Error:(50, 88) Cannot resolve method 'getLibelleEtatVoiture' in 'EtatVoiture'
Error:(50, 113) ';' expected
Error:(50, 113) Unexpected token
Warning:(50, 114) Unnecessary semicolon ';'
Error:(60, 78) Incompatible types. Found: 'java.lang.String', required: 'main.java.com.miage.parcauto.model.vehicule.Energie'
Error:(61, 13) Cannot resolve method 'setNumeroChassis' in 'VehiculeDTO'
Error:(61, 39) Cannot resolve method 'getNumeroChassis' in 'Vehicule'
Error:(83, 16) Cannot resolve method 'setIdEtatVoiture' in 'Vehicule'
Error:(86, 50) 'valueOf(java.lang.String)' in 'main.java.com.miage.parcauto.model.vehicule.Energie' cannot be applied to '(main.java.com.miage.parcauto.model.vehicule.Energie)'
Error:(91, 16) Cannot resolve method 'setNumeroChassis' in 'Vehicule'
Error:(91, 37) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\AuthenticationService.java
Warning:(25, 30) Method 'authenticate(java.lang.String, java.lang.String)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\UtilisateurManagementService.java
Warning:(26, 20) Method 'createUtilisateur(main.java.com.miage.parcauto.dto.UtilisateurDTO)' is never used
Warning:(35, 30) Method 'getUtilisateurById(java.lang.Integer)' is never used
Warning:(44, 30) Method 'getUtilisateurByLogin(java.lang.String)' is never used
Warning:(52, 26) Method 'getAllUtilisateurs()' is never used
Warning:(65, 20) Method 'updateUtilisateur(main.java.com.miage.parcauto.dto.UtilisateurDTO)' is never used
Warning:(74, 10) Method 'deleteUtilisateur(java.lang.Integer)' is never used
Warning:(85, 10) Method 'changePassword(java.lang.Integer, java.lang.String)' is never used
Warning:(95, 10) Method 'setUtilisateurActif(java.lang.Integer, boolean)' is never used
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\AssuranceServiceImpl.java
Warning:(27, 1) Unused import statement
Warning:(32, 14) Class 'AssuranceServiceImpl' is never used
Warning:(43, 12) Constructor 'AssuranceServiceImpl()' is never used
Warning:(59, 12) Constructor 'AssuranceServiceImpl(main.java.com.miage.parcauto.dao.AssuranceRepository, main.java.com.miage.parcauto.dao.VehiculeRepository, main.java.com.miage.parcauto.dao.CouvrirRepository, main.java.com.miage.parcauto.mapper.AssuranceMapper, main.java.com.miage.parcauto.mapper.VehiculeMapper)' is never used
Error:(72, 50) Cannot resolve method 'getDateDebut' in 'AssuranceDTO'
Error:(72, 89) Cannot resolve method 'getDateFin' in 'AssuranceDTO'
Error:(75, 26) Cannot resolve method 'getDateDebut' in 'AssuranceDTO'
Error:(75, 62) Cannot resolve method 'getDateFin' in 'AssuranceDTO'
Error:(85, 65) Expected 1 argument but found 2
Error:(89, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(104, 49) Expected 1 argument but found 2
Error:(121, 73) Cannot resolve method 'findAll(Connection)'
Error:(137, 26) Cannot resolve method 'getDateDebut' in 'AssuranceDTO'
Error:(137, 65) Cannot resolve method 'getDateFin' in 'AssuranceDTO'
Error:(137, 102) Cannot resolve method 'getDateDebut' in 'AssuranceDTO'
Error:(137, 138) Cannot resolve method 'getDateFin' in 'AssuranceDTO'
Error:(146, 72) Expected 1 argument but found 2
Error:(149, 29) Cannot resolve method 'getDateDebut' in 'AssuranceDTO'
Error:(149, 106) Cannot resolve method 'getDateDebut' in 'AssuranceDTO'
Error:(150, 29) Cannot resolve method 'getDateFin' in 'AssuranceDTO'
Error:(150, 102) Cannot resolve method 'getDateFin' in 'AssuranceDTO'
Error:(152, 29) Cannot resolve method 'getCout' in 'AssuranceDTO'
Error:(152, 96) Cannot resolve method 'getCout' in 'AssuranceDTO'
Error:(154, 69) Expected 1 argument but found 2
Error:(158, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(175, 46) Expected 1 argument but found 2
Error:(179, 31) Cannot resolve method 'deleteByNumCarteAssurance' in 'CouvrirRepository'
Error:(181, 58) Expected 1 argument but found 2
Error:(187, 20) Cannot resolve method 'rollback' in 'DbUtil'
Warning:(201, 93) There is a more general exception, 'main.java.com.miage.parcauto.exception.EntityNotFoundException', in the throws list already.
Error:(207, 45) Expected 1 argument but found 2
Error:(210, 46) Expected 1 argument but found 2
Error:(213, 35) Cannot resolve method 'findByIdVehiculeAndIdAssurance' in 'CouvrirRepository'
Error:(218, 18) Cannot resolve method 'setIdVehicule' in 'Couvrir'
Error:(219, 18) Cannot resolve method 'setNumCarteAssurance' in 'Couvrir'
Error:(220, 31) Cannot resolve method 'save' in 'CouvrirRepository'
Error:(223, 20) Cannot resolve method 'rollback' in 'DbUtil'
Warning:(234, 95) There is a more general exception, 'main.java.com.miage.parcauto.exception.EntityNotFoundException', in the throws list already.
Error:(240, 45) Expected 1 argument but found 2
Error:(243, 46) Expected 1 argument but found 2
Error:(247, 49) Cannot resolve method 'delete' in 'CouvrirRepository'
Error:(253, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(268, 46) Expected 1 argument but found 2
Error:(271, 58) Cannot resolve method 'findVehiculesByNumCarteAssurance' in 'CouvrirRepository'
Error:(288, 45) Expected 1 argument but found 2
Error:(291, 60) Cannot resolve method 'findAssurancesByIdVehicule' in 'CouvrirRepository'
Error:(306, 47) 'OperationFailedException(java.lang.String)' in 'main.java.com.miage.parcauto.exception.OperationFailedException' cannot be applied to '(main.java.com.miage.parcauto.exception.ValidationException)'
Error:(311, 62) Cannot resolve method 'findExpiringBefore' in 'AssuranceRepository'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\MissionServiceImpl.java
Warning:(29, 39) Private field 'personnelRepository' is assigned but never accessed
Warning:(46, 12) Constructor 'MissionServiceImpl(main.java.com.miage.parcauto.dao.MissionRepository, main.java.com.miage.parcauto.dao.VehiculeRepository, main.java.com.miage.parcauto.dao.PersonnelRepository, main.java.com.miage.parcauto.dao.DepenseMissionRepository, main.java.com.miage.parcauto.mapper.MissionMapper, main.java.com.miage.parcauto.mapper.DepenseMissionMapper, main.java.com.miage.parcauto.service.VehiculeService)' is never used
Error:(62, 84) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(62, 121) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(65, 24) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(65, 58) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(77, 45) Expected 1 argument but found 2
Error:(81, 18) Cannot resolve symbol 'VehiculeDTO'
Error:(81, 99) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(81, 126) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(82, 77) Cannot resolve method 'getIdVehicule()'
Error:(93, 25) Cannot resolve method 'getStatut' in 'Mission'
Error:(94, 25) Cannot resolve method 'setStatut' in 'Mission'
Error:(97, 59) Expected 1 argument but found 2
Error:(102, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(114, 71) Expected 1 argument but found 2
Error:(128, 63) Cannot resolve method 'findAll(Connection)'
Error:(142, 45) Expected 1 argument but found 2
Error:(165, 20) Cannot resolve symbol 'Collections'
Error:(186, 66) Expected 1 argument but found 2
Error:(190, 33) Cannot resolve method 'getStatut' in 'Mission'
Error:(190, 85) Cannot resolve method 'getStatut' in 'MissionDTO'
Error:(190, 157) Cannot resolve method 'getStatut' in 'MissionDTO'
Error:(195, 106) Cannot resolve method 'getIdVehicule' in 'Mission'
Error:(196, 49) Expected 1 argument but found 2
Error:(200, 55) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(200, 91) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(201, 53) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(201, 93) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(203, 26) Cannot resolve symbol 'VehiculeDTO'
Error:(204, 85) Cannot resolve method 'getIdVehicule()'
Error:(205, 62) Cannot resolve method 'findOverlappingMissionsForVehicule' in 'MissionRepository'
Error:(206, 55) Cannot resolve method 'getIdMission()'
Error:(210, 33) Cannot resolve method 'setIdVehicule' in 'Mission'
Error:(220, 27) Cannot resolve method 'getLibelle' in 'MissionDTO'
Error:(220, 65) Cannot resolve method 'setLibelle' in 'Mission'
Error:(220, 87) Cannot resolve method 'getLibelle' in 'MissionDTO'
Error:(221, 27) Cannot resolve method 'getSiteDestination' in 'MissionDTO'
Error:(221, 92) Cannot resolve method 'getSiteDestination' in 'MissionDTO'
Error:(222, 27) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(222, 98) Cannot resolve method 'getDateDebut' in 'MissionDTO'
Error:(223, 27) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(223, 100) Cannot resolve method 'getDateFinPrevue' in 'MissionDTO'
Error:(224, 27) Cannot resolve method 'getDateFinEffective' in 'MissionDTO'
Error:(224, 74) Cannot resolve method 'setDateFinEffective' in 'Mission'
Error:(224, 105) Cannot resolve method 'getDateFinEffective' in 'MissionDTO'
Error:(227, 27) Cannot resolve method 'getStatut' in 'MissionDTO'
Error:(227, 64) Cannot resolve method 'setStatut' in 'Mission'
Error:(227, 107) Cannot resolve method 'getStatut' in 'MissionDTO'
Error:(228, 27) Cannot resolve method 'getCoutEstime' in 'MissionDTO'
Error:(228, 68) Cannot resolve method 'setCoutEstime' in 'Mission'
Error:(228, 93) Cannot resolve method 'getCoutEstime' in 'MissionDTO'
Error:(229, 27) Cannot resolve method 'getCoutTotalReel' in 'MissionDTO'
Error:(229, 95) Cannot resolve method 'getCoutTotalReel' in 'MissionDTO'
Error:(230, 27) Cannot resolve method 'getCircuit' in 'MissionDTO'
Error:(230, 94) Cannot resolve method 'getCircuit' in 'MissionDTO'
Error:(231, 27) Cannot resolve method 'getObservations' in 'MissionDTO'
Error:(231, 103) Cannot resolve method 'getObservations' in 'MissionDTO'
Error:(234, 63) Expected 1 argument but found 2
Error:(239, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(253, 58) Expected 1 argument but found 2
Error:(256, 25) Cannot resolve method 'getStatut' in 'Mission'
Error:(256, 54) Cannot resolve symbol 'ENCOURS'
Error:(262, 49) Expected 1 argument but found 2
Error:(265, 56) Expected 1 argument but found 2
Error:(271, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(292, 58) Expected 1 argument but found 2
Error:(295, 25) Cannot resolve method 'getStatut' in 'Mission'
Error:(302, 75) Cannot resolve method 'getIdVehicule' in 'Mission'
Error:(303, 128) Cannot resolve method 'getIdVehicule' in 'Mission'
Error:(311, 21) Cannot resolve method 'setDateFinEffective' in 'Mission'
Error:(312, 21) Cannot resolve method 'setStatut' in 'Mission'
Error:(322, 63) Expected 1 argument but found 2
Error:(327, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(349, 58) Expected 1 argument but found 2
Error:(352, 25) Cannot resolve method 'getStatut' in 'Mission'
Error:(358, 21) Cannot resolve method 'setIdMission' in 'DepenseMission'
Error:(359, 25) Cannot resolve method 'getDateDepense' in 'DepenseMission'
Error:(360, 25) Cannot resolve method 'setDateDepense' in 'DepenseMission'
Error:(364, 73) Expected 1 argument but found 2
Error:(372, 38) Expected 1 argument but found 2
Error:(378, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(390, 44) Expected 1 argument but found 2
Error:(407, 45) Expected 1 argument but found 2
Error:(410, 56) Cannot resolve method 'findActiveByVehiculeId' in 'MissionRepository'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\EntretienServiceImpl.java
Warning:(38, 12) Constructor 'EntretienServiceImpl(main.java.com.miage.parcauto.dao.EntretienRepository, main.java.com.miage.parcauto.dao.VehiculeRepository, main.java.com.miage.parcauto.mapper.EntretienMapper)' is never used
Error:(50, 26) Cannot resolve method 'getDateEntree' in 'EntretienDTO'
Error:(50, 66) Cannot resolve method 'getMotif' in 'EntretienDTO'
Error:(50, 101) Cannot resolve method 'getMotif' in 'EntretienDTO'
Error:(60, 45) Expected 1 argument but found 2
Error:(69, 65) Expected 1 argument but found 2
Error:(74, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(86, 77) Expected 1 argument but found 2
Error:(100, 69) Cannot resolve method 'findAll(Connection)'
Error:(114, 45) Expected 1 argument but found 2
Error:(138, 72) Expected 1 argument but found 2
Error:(141, 112) Cannot resolve method 'getIdVehicule' in 'Entretien'
Error:(142, 49) Expected 1 argument but found 2
Error:(145, 35) Cannot resolve method 'setIdVehicule' in 'Entretien'
Error:(148, 29) Cannot resolve method 'getDateEntree' in 'EntretienDTO'
Error:(148, 72) Cannot resolve method 'setDateEntree' in 'Entretien'
Error:(148, 99) Cannot resolve method 'getDateEntree' in 'EntretienDTO'
Error:(149, 29) Cannot resolve method 'getDateSortie' in 'EntretienDTO'
Error:(149, 72) Cannot resolve method 'setDateSortie' in 'Entretien'
Error:(149, 99) Cannot resolve method 'getDateSortie' in 'EntretienDTO'
Error:(150, 29) Cannot resolve method 'getMotif' in 'EntretienDTO'
Error:(150, 67) Cannot resolve method 'setMotif' in 'Entretien'
Error:(150, 89) Cannot resolve method 'getMotif' in 'EntretienDTO'
Error:(151, 29) Cannot resolve method 'getObservations' in 'EntretienDTO'
Error:(151, 74) Cannot resolve method 'setObservations' in 'Entretien'
Error:(151, 103) Cannot resolve method 'getObservations' in 'EntretienDTO'
Error:(152, 29) Cannot resolve method 'getCoutEstime' in 'EntretienDTO'
Error:(152, 72) Cannot resolve method 'setCoutEstime' in 'Entretien'
Error:(152, 99) Cannot resolve method 'getCoutEstime' in 'EntretienDTO'
Error:(153, 29) Cannot resolve method 'getCoutReel' in 'EntretienDTO'
Error:(153, 70) Cannot resolve method 'setCoutReel' in 'Entretien'
Error:(153, 95) Cannot resolve method 'getCoutReel' in 'EntretienDTO'
Error:(154, 29) Cannot resolve method 'getLieu' in 'EntretienDTO'
Error:(154, 66) Cannot resolve method 'setLieu' in 'Entretien'
Error:(154, 87) Cannot resolve method 'getLieu' in 'EntretienDTO'
Error:(155, 29) Cannot resolve method 'getTypeEntretien' in 'EntretienDTO'
Error:(155, 163) Cannot resolve method 'getTypeEntretien' in 'EntretienDTO'
Error:(156, 143) 'valueOf(java.lang.String)' in 'main.java.com.miage.parcauto.model.entretien.StatutOT' cannot be applied to '(main.java.com.miage.parcauto.model.entretien.StatutOT)'
Error:(159, 69) Expected 1 argument but found 2
Error:(164, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(178, 46) Expected 1 argument but found 2
Error:(181, 58) Expected 1 argument but found 2
Error:(187, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(215, 64) Expected 1 argument but found 2
Error:(222, 77) Cannot resolve method 'getIdVehicule' in 'Entretien'
Error:(223, 131) Cannot resolve method 'getIdVehicule' in 'Entretien'
Error:(226, 23) Cannot resolve method 'setDateSortie' in 'Entretien'
Error:(227, 23) Cannot resolve method 'setCoutReel' in 'Entretien'
Error:(228, 23) Cannot resolve method 'setObservations' in 'Entretien'
Error:(228, 50) Cannot resolve method 'getObservations' in 'Entretien'
Error:(228, 93) Cannot resolve method 'getObservations' in 'Entretien'
Error:(236, 43) Expected 1 argument but found 2
Error:(243, 69) Expected 1 argument but found 2
Error:(248, 20) Cannot resolve method 'rollback' in 'DbUtil'
C:\THEBLACK\ParcAuto\src\main\java\com\miage\parcauto\service\impl\VehiculeServiceImpl.java
Warning:(40, 12) Constructor 'VehiculeServiceImpl(main.java.com.miage.parcauto.dao.VehiculeRepository, main.java.com.miage.parcauto.dao.EtatVoitureRepository, main.java.com.miage.parcauto.dao.MissionRepository, main.java.com.miage.parcauto.dao.AffectationRepository, main.java.com.miage.parcauto.mapper.VehiculeMapper)' is never used
Error:(55, 29) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(55, 71) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(71, 36) Cannot resolve method 'findByNumeroChassis' in 'VehiculeRepository'
Error:(71, 74) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(72, 108) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(74, 48) Expected 1 argument but found 2
Error:(82, 62) Expected 1 argument but found 2
Error:(87, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(99, 74) Expected 1 argument but found 2
Error:(113, 66) Cannot resolve method 'findAll(Connection)'
Error:(134, 69) Expected 1 argument but found 2
Error:(147, 29) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(147, 72) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(147, 115) Cannot resolve method 'getNumeroChassis' in 'Vehicule'
Error:(148, 36) Cannot resolve method 'findByNumeroChassis' in 'VehiculeRepository'
Error:(148, 74) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(149, 28) Cannot resolve method 'getIdVehicule()'
Error:(150, 143) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(153, 34) Cannot resolve method 'setNumeroChassis' in 'Vehicule'
Error:(153, 63) Cannot resolve method 'getNumeroChassis' in 'VehiculeDTO'
Error:(158, 52) Expected 1 argument but found 2
Error:(161, 38) Cannot resolve method 'getIdEtatVoiture' in 'Vehicule'
Error:(162, 38) Cannot resolve method 'setIdEtatVoiture' in 'Vehicule'
Error:(166, 138) 'valueOf(java.lang.String)' in 'main.java.com.miage.parcauto.model.vehicule.Energie' cannot be applied to '(main.java.com.miage.parcauto.model.vehicule.Energie)'
Error:(182, 66) Expected 1 argument but found 2
Error:(187, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(190, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(207, 45) Expected 1 argument but found 2
Error:(211, 36) Cannot resolve method 'findActiveByVehiculeId' in 'MissionRepository'
Error:(216, 57) Expected 1 argument but found 2
Error:(222, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(239, 61) Expected 1 argument but found 2
Error:(248, 39) Expected 1 argument but found 2
Error:(251, 20) Cannot resolve method 'rollback' in 'DbUtil'
Error:(272, 69) Cannot resolve method 'findByEtatId' in 'VehiculeRepository'
Error:(277, 72) Cannot resolve method 'findOverlappingMissionsForVehicule' in 'MissionRepository'
Error:(277, 107) Variable used in lambda expression should be final or effectively final
Error:(278, 80) Cannot resolve method 'findOverlappingAffectationsForVehicule' in 'AffectationRepository'
Error:(278, 119) Variable used in lambda expression should be final or effectively final