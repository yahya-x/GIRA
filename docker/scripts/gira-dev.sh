#!/bin/bash

# ================================
# GIRA DEVELOPMENT ENVIRONMENT MANAGER
# ================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Project configuration
COMPOSE_FILE="../docker-compose.dev.yaml"
ENV_FILE="../.env"  # Standard Docker Compose env file in project root
PROJECT_NAME="gira"

# Function to print colored output
print_status() {
    echo -e "${BLUE}[GIRA]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[GIRA]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[GIRA]${NC} $1"
}

print_error() {
    echo -e "${RED}[GIRA]${NC} $1"
}

# Function to check if GIRA containers are running
check_gira_containers() {
    local containers=$(docker ps --filter "name=gira" --format "{{.Names}}" 2>/dev/null)
    if [ -n "$containers" ]; then
        return 0
    else
        return 1
    fi
}

# Function to stop other containers
stop_other_containers() {
    print_status "Stopping other project containers..."
    
    # Get all running containers except GIRA
    local other_containers=$(docker ps --format "{{.Names}}" | grep -v "gira" || true)
    
    if [ -n "$other_containers" ]; then
        echo "$other_containers" | xargs -r docker stop
        print_success "Other containers stopped"
    else
        print_warning "No other containers found"
    fi
}

# Function to start GIRA environment
start_gira() {
    print_status "Starting GIRA development environment..."
    
    # Stop other containers if requested
    if [ "$1" = "--isolate" ]; then
        stop_other_containers
    fi
    
    # Start GIRA containers
    docker compose -f "$COMPOSE_FILE" --env-file "$ENV_FILE" up -d --build
    
    print_success "GIRA environment started!"
    print_status "Services available:"
    echo "  - Backend API: http://localhost:8081"
    echo "  - PostgreSQL: localhost:5433"
    echo "  - RabbitMQ Management: http://localhost:15672"
}

# Function to start monitoring
start_monitoring() {
    print_status "Starting monitoring stack..."
    docker compose -f "../docker-compose.monitoring.yaml" --env-file "$ENV_FILE" up -d
    
    print_success "Monitoring stack started!"
    print_status "Monitoring services available:"
    echo "  - Prometheus: http://localhost:9090"
    echo "  - Grafana: http://localhost:3000 (admin/admin)"
    echo "  - cAdvisor: http://localhost:8080"
}

# Function to start everything (GIRA + monitoring)
start_all() {
    print_status "Starting GIRA with monitoring..."
    start_gira "$1"
    start_monitoring
    
    print_success "All services started!"
    print_status "Access your applications:"
    echo "  - GIRA Backend: http://localhost:8081"
    echo "  - Grafana Dashboard: http://localhost:3000"
    echo "  - Prometheus: http://localhost:9090"
}

# Function to stop GIRA environment
stop_gira() {
    print_status "Stopping GIRA development environment..."
    docker compose -f "$COMPOSE_FILE" down
    print_success "GIRA environment stopped!"
}

# Function to restart GIRA environment
restart_gira() {
    print_status "Restarting GIRA development environment..."
    stop_gira
    start_gira
}

# Function to show GIRA status
status_gira() {
    print_status "GIRA containers status:"
    docker ps --filter "name=gira" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    
    print_status "Health check status:"
    docker ps --filter "name=gira" --format "table {{.Names}}\t{{.Health}}"
}

# Function to show logs
logs_gira() {
    local service=${1:-backend}
    print_status "Showing logs for $service..."
    docker compose -f "$COMPOSE_FILE" logs -f "$service"
}

# Function to clean up
clean_gira() {
    print_warning "This will remove all GIRA containers, volumes, and images!"
    read -p "Are you sure? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_status "Cleaning up GIRA environment..."
        docker compose -f "$COMPOSE_FILE" down -v --rmi all
        print_success "GIRA environment cleaned up!"
    else
        print_status "Cleanup cancelled"
    fi
}

# Function to start both dev and monitoring stacks ensemble
start_all_compose() {
    print_status "Starting GIRA dev + monitoring stacks..."
    docker compose -f "$COMPOSE_FILE" -f "../docker-compose.monitoring.yaml" --env-file "$ENV_FILE" up -d --build
    print_success "GIRA dev + monitoring started!"
    print_status "Services available:"
    echo "  - Backend API: http://localhost:8081"
    echo "  - PostgreSQL: localhost:5433"
    echo "  - RabbitMQ Management: http://localhost:15672"
    echo "  - Prometheus: http://localhost:9090"
    echo "  - Grafana: http://localhost:3000 (admin/admin)"
    echo "  - cAdvisor: http://localhost:8080"
}

# Main script logic
case "${1:-help}" in
    "start")
        start_gira "$2"
        ;;
    "monitoring")
        start_monitoring
        ;;
    "start-all")
        start_all "$2"
        ;;
    "stop")
        stop_gira
        ;;
    "restart")
        restart_gira
        ;;
    "status")
        status_gira
        ;;
    "logs")
        logs_gira "$2"
        ;;
    "clean")
        clean_gira
        ;;
    "isolate")
        start_gira --isolate
        ;;
    "start-all-compose")
        start_all_compose
        ;;
    "help"|*)
        echo "GIRA Development Environment Manager"
        echo ""
        echo "Usage: $0 [command] [options]"
        echo ""
        echo "Commands:"
echo "  start [--isolate]  Start GIRA environment (--isolate stops other containers)"
echo "  start-all-compose  Start GIRA dev + monitoring with both compose files"
echo "  monitoring         Start monitoring stack (Prometheus + Grafana)"
echo "  start-all [--isolate] Start GIRA + monitoring together"
echo "  stop               Stop GIRA environment"
echo "  restart            Restart GIRA environment"
echo "  status             Show GIRA containers status"
echo "  logs [service]     Show logs (default: backend)"
echo "  clean              Remove all GIRA containers, volumes, and images"
echo "  isolate            Start GIRA in isolated environment"
echo "  help               Show this help message"
        echo ""
        echo "Examples:"
        echo "  $0 start           # Start GIRA normally"
        echo "  $0 start --isolate # Start GIRA and stop other containers"
        echo "  $0 logs backend    # Show backend logs"
        echo "  $0 status          # Show GIRA containers status"
        ;;
esac 