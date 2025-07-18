# Docker Infrastructure - GIRA Project

Ce dossier contient toute l'infrastructure Docker pour le projet GIRA, organisée par environnement avec un stack de monitoring complet.

## 📁 Structure

```
docker/
├── Dockerfile.backend          # Dockerfile pour le backend Spring Boot
├── docker-compose.dev.yaml     # Configuration pour le développement
├── docker-compose.prod.yaml    # Configuration pour la production
├── docker-compose.test.yaml    # Configuration pour les tests
├── docker-compose.monitoring.yaml # Stack de monitoring (Prometheus + Grafana)
├── .env.dev                    # Variables d'environnement pour le dev
├── .env.prod                   # Variables d'environnement pour la prod
├── .env.test                   # Variables d'environnement pour les tests
├── scripts/
│   └── gira-dev.sh             # Script de gestion de l'environnement dev
├── monitoring/                 # Configuration du stack de monitoring
│   ├── prometheus.yml          # Configuration Prometheus
│   └── grafana/                # Configuration Grafana
│       ├── dashboards/         # Dashboards personnalisés
│       └── provisioning/       # Provisioning automatique
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

# Monitoring
./gira-dev.sh monitoring       # Démarrer Prometheus + Grafana
./gira-dev.sh start-all        # Démarrer GIRA + monitoring ensemble
./gira-dev.sh start-all-compose # Démarrer tout avec un seul compose
```

### Ou utiliser les commandes Docker Compose directement
```bash
# Développement
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

### Monitoring
- **Prometheus** : `http://localhost:9090` (Collecte de métriques)
- **Grafana** : `http://localhost:3000` (Dashboards - admin/admin)
- **cAdvisor** : `http://localhost:8080` (Métriques containers)
- **Postgres Exporter** : `http://localhost:9187` (Métriques PostgreSQL)
- **RabbitMQ Exporter** : `http://localhost:9419` (Métriques RabbitMQ)

### Production
- **Backend** : `http://localhost:8081` (API Spring Boot)
- **PostgreSQL** : Non exposé (communication interne uniquement)
- **RabbitMQ** : Non exposé (communication interne uniquement)

### Tests
- **Backend** : Non exposé (tests internes uniquement)
- **PostgreSQL** : Base de données jetable
- **RabbitMQ** : Queue jetable

## 📊 Monitoring et Observabilité

### Health Checks
Tous les services incluent des health checks configurés :

- **Backend** : `/actuator/health/liveness` et `/actuator/health/readiness`
- **PostgreSQL** : `pg_isready`
- **RabbitMQ** : `rabbitmq-diagnostics ping`
- **Prometheus** : Endpoint `/-/healthy`
- **Grafana** : Endpoint `/api/health`

### Métriques Collectées
- **Application** : Métriques Spring Boot (HTTP, JVM, base de données)
- **Containers** : CPU, mémoire, réseau via cAdvisor
- **Base de données** : Connexions, requêtes, performance via postgres-exporter
- **Message Queue** : Messages, queues, consumers via rabbitmq-exporter

### Dashboards Grafana
- **GIRA Overview** : Vue d'ensemble complète du système
- Métriques en temps réel
- Alertes configurables

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

### Monitoring (optionnel)
- `GRAFANA_ADMIN_USER` : Utilisateur admin Grafana (défaut: admin)
- `GRAFANA_ADMIN_PASS` : Mot de passe admin Grafana (défaut: admin)

## 🛠️ Scripts de Gestion

### Commandes principales
```bash
./gira-dev.sh start [--isolate]     # Démarrer GIRA (--isolate arrête les autres conteneurs)
./gira-dev.sh monitoring            # Démarrer le stack de monitoring
./gira-dev.sh start-all [--isolate] # Démarrer GIRA + monitoring
./gira-dev.sh start-all-compose     # Démarrer tout avec un seul compose
./gira-dev.sh stop                  # Arrêter GIRA
./gira-dev.sh restart               # Redémarrer GIRA
./gira-dev.sh status                # Statut des conteneurs
./gira-dev.sh logs [service]        # Logs (défaut: backend)
./gira-dev.sh clean                 # Nettoyer tout (conteneurs + volumes + images)
```

### Exemples d'utilisation
```bash
# Démarrage complet avec monitoring
./gira-dev.sh start-all-compose

# Vérification du statut
./gira-dev.sh status

# Logs en temps réel
./gira-dev.sh logs backend

# Nettoyage complet
./gira-dev.sh clean
```

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
- Health checks rapides pour les tests

## 📋 TODO

- [ ] Ajouter le service frontend
- [ ] Configurer un reverse proxy (nginx/traefik) pour la production
- [ ] Configurer SSL/TLS pour la production
- [ ] Ajouter des tests d'intégration automatisés
- [ ] Configurer un utilisateur non-root dans le Dockerfile
- [ ] Ajouter des alertes Prometheus
- [ ] Configurer la sauvegarde automatique des données
- [ ] Ajouter des métriques business personnalisées

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

4. **Health checks échouent**
   ```bash
   # Vérifier les health checks
   ./gira-dev.sh status
   # Vérifier les logs du service
   ./gira-dev.sh logs backend
   ```

5. **Monitoring ne démarre pas**
   ```bash
   # Vérifier les logs Prometheus
   docker compose -f docker/docker-compose.monitoring.yaml logs prometheus
   # Vérifier les logs Grafana
   docker compose -f docker/docker-compose.monitoring.yaml logs grafana
   ```

### Commandes utiles

```bash
# Voir tous les conteneurs GIRA
docker ps --filter "name=gira"

# Voir les réseaux
docker network ls

# Voir les volumes
docker volume ls

# Nettoyer les images non utilisées
docker image prune -a

# Nettoyer les volumes non utilisés
docker volume prune
```