# Docker Infrastructure - GIRA Project

Ce dossier contient toute l'infrastructure Docker pour le projet GIRA, organisée par environnement.

## 📁 Structure

```
docker/
├── Dockerfile.backend          # Dockerfile pour le backend Spring Boot
├── docker-compose.dev.yaml     # Configuration pour le développement
├── docker-compose.prod.yaml    # Configuration pour la production
├── docker-compose.test.yaml    # Configuration pour les tests
├── .env.dev                    # Variables d'environnement pour le dev
├── .env.prod                   # Variables d'environnement pour la prod
├── .env.test                   # Variables d'environnement pour les tests
├── scripts/
│   └── gira-dev.sh             # Script de gestion de l'environnement dev
└── README.md                   # Ce fichier
```

## 🚀 Utilisation

### Développement (Recommandé)
```bash
# Utiliser le script de gestion (plus simple)
cd docker/scripts
./gira-dev.sh start --isolate  # Démarre GIRA et arrête les autres conteneurs
./gira-dev.sh status           # Voir le statut des conteneurs GIRA
./gira-dev.sh logs backend     # Voir les logs du backend
./gira-dev.sh stop             # Arrêter GIRA

# Ou utiliser les commandes Docker Compose directement
docker compose -f docker/docker-compose.dev.yaml --env-file docker/.env.dev up --build

# Lancer en arrière-plan
docker compose -f docker/docker-compose.dev.yaml --env-file docker/.env.dev up -d --build

# Arrêter l'environnement
docker compose -f docker/docker-compose.dev.yaml down
```

### Production
```bash
# Lancer l'environnement de production
docker compose -f docker/docker-compose.prod.yaml --env-file docker/.env.prod up --build

# Lancer en arrière-plan
docker compose -f docker/docker-compose.prod.yaml --env-file docker/.env.prod up -d --build

# Arrêter l'environnement
docker compose -f docker/docker-compose.prod.yaml down
```

### Tests
```bash
# Lancer l'environnement de test
docker compose -f docker/docker-compose.test.yaml --env-file docker/.env.test up --build

# Lancer les tests et arrêter automatiquement
docker compose -f docker/docker-compose.test.yaml --env-file docker/.env.test up --build --abort-on-container-exit
```

## 🔧 Services disponibles

### Développement
- **Backend** : `http://localhost:8081` (API Spring Boot)
- **PostgreSQL** : `localhost:5433` (Base de données)
- **RabbitMQ** : `localhost:5672` (Message queue)
- **RabbitMQ Management** : `http://localhost:15672` (Interface web)

### Production
- **Backend** : `http://localhost:8081` (API Spring Boot)
- **PostgreSQL** : Non exposé (communication interne uniquement)
- **RabbitMQ** : Non exposé (communication interne uniquement)

### Tests
- **Backend** : Non exposé (tests internes uniquement)
- **PostgreSQL** : Base de données jetable
- **RabbitMQ** : Queue jetable

## 📝 Variables d'environnement

### Obligatoires
- `DB_USER` : Utilisateur PostgreSQL
- `DB_PASSWORD` : Mot de passe PostgreSQL
- `JWT_SECRET` : Clé secrète pour JWT
- `MAIL_HOST` : Serveur SMTP
- `MAIL_PORT` : Port SMTP
- `MAIL_USER` : Utilisateur email
- `MAIL_PASS` : Mot de passe email
- `FRONTEND_URL` : URL du frontend
- `RABBITMQ_USER` : Utilisateur RabbitMQ
- `RABBITMQ_PASS` : Mot de passe RabbitMQ
- `SPRING_PROFILES_ACTIVE` : Profil Spring Boot (dev/prod/test)

## 🔒 Sécurité

### Production
- ⚠️ **Ne jamais commiter** le fichier `.env.prod` avec les vraies valeurs
- Utiliser un gestionnaire de secrets (HashiCorp Vault, AWS Secrets Manager, etc.)
- Changer tous les mots de passe par défaut
- Utiliser des clés JWT longues et uniques

### Développement
- Les ports sont exposés pour faciliter le debug
- Les logs sont détaillés
- Les volumes sont persistants

## 🧪 Tests

L'environnement de test utilise :
- Base de données jetable (pas de volume persistant)
- Configuration email de test (Mailtrap)
- Logs minimaux
- Pas de redémarrage automatique

## 📋 TODO

- [ ] Ajouter le service frontend
- [ ] Configurer Spring Boot Actuator pour les healthchecks
- [ ] Ajouter un reverse proxy (nginx/traefik) pour la production
- [ ] Configurer SSL/TLS pour la production
- [ ] Ajouter des tests d'intégration automatisés
- [ ] Configurer un utilisateur non-root dans le Dockerfile
- [ ] Ajouter des métriques et monitoring

## 🐛 Dépannage

### Problèmes courants

1. **Port déjà utilisé**
   ```bash
   # Vérifier les ports utilisés
   docker ps
   # Arrêter les conteneurs existants
   docker compose down
   ```

2. **Variables d'environnement manquantes**
   ```bash
   # Vérifier que le fichier .env existe
   ls -la docker/.env.*
   ```

3. **Base de données ne démarre pas**
   ```bash
   # Vérifier les logs
   docker compose logs postgres
   ```