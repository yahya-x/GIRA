/**
 * GIRA - Airport Complaint Management System
 * TypeScript type definitions for frontend application
 */

// ============================================================================
// USER TYPES
// ============================================================================

export enum UserRole {
  PASSAGER = 'PASSAGER',
  SUPERVISEUR = 'SUPERVISEUR',
  ADMINISTRATEUR = 'ADMINISTRATEUR'
}

export interface Role {
  id: string;
  nom: string;
  description?: string;
  permissions: string[];
  actif: boolean;
  dateCreation: string;
  dateModification: string;
}

export interface User {
  id: string;
  email: string;
  nom: string;
  prenom: string;
  telephone?: string;
  langue?: string;
  role: Role;
  actif: boolean;
  emailVerifie: boolean;
  dateCreation: string;
  derniereConnexion?: string;
  preferences?: Record<string, any>;
  nombreReclamations?: number;
}

export interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
}

// ============================================================================
// COMPLAINT TYPES
// ============================================================================

export enum ComplaintStatus {
  SOUMISE = 'SOUMISE',
  EN_COURS = 'EN_COURS',
  EN_ATTENTE_INFO = 'EN_ATTENTE_INFO',
  RESOLUE = 'RESOLUE',
  FERMEE = 'FERMEE',
  ANNULEE = 'ANNULEE'
}

export enum ComplaintPriority {
  BASSE = 'BASSE',
  NORMALE = 'NORMALE',
  HAUTE = 'HAUTE',
  URGENTE = 'URGENTE'
}

export interface Categorie {
  id: string;
  nom: string;
  description?: string;
  ordreAffichage?: number;
  actif: boolean;
  sousCategories?: SousCategorie[];
}

export interface SousCategorie {
  id: string;
  nom: string;
  description?: string;
  categorie?: Categorie;
  actif: boolean;
}

export interface Fichier {
  id: string;
  nom: string;
  type: string;
  taille: number;
  url: string;
  hash: string;
  dateUpload: string;
}

export interface Commentaire {
  id: string;
  contenu: string;
  auteur: User;
  dateCreation: string;
  isInterne: boolean;
}

export interface Complaint {
  id: string;
  numero: string;
  titre: string;
  description: string;
  statut: ComplaintStatus;
  priorite: ComplaintPriority;
  categorieNom: string;
  sousCategorieNom?: string;
  demandeur: User;
  agentAssigne?: User;
  fichiers: Fichier[];
  commentaires: Commentaire[];
  dateCreation: string;
  dateModification: string;
  dateResolution?: string;
  dateEcheance: string;
  localisation?: string;
  lieuDescription?: string;
  satisfaction?: number;
  commentaireSatisfaction?: string;
  metadonnees?: string;
}

export interface ComplaintState {
  items: Complaint[];
  currentComplaint: Complaint | null;
  isLoading: boolean;
  error: string | null;
  filters: ComplaintFilters;
  pagination: PaginationState;
}

export interface ComplaintFilters {
  statut?: ComplaintStatus[];
  priorite?: ComplaintPriority[];
  categorie?: string[];
  agent?: string[];
  dateDebut?: string;
  dateFin?: string;
  recherche?: string;
}

export interface PaginationState {
  page: number;
  size: number;
  total: number;
  totalPages: number;
}

// ============================================================================
// NOTIFICATION TYPES
// ============================================================================

export enum NotificationType {
  ASSIGNMENT = 'ASSIGNMENT',
  STATUS_CHANGE = 'STATUS_CHANGE',
  COMMENT = 'COMMENT',
  ESCALATION = 'ESCALATION',
  SLA_BREACH = 'SLA_BREACH',
  RESOLUTION = 'RESOLUTION'
}

export interface Notification {
  id: string;
  titre: string;
  message: string;
  type: NotificationType;
  destinataire: User;
  isLue: boolean;
  dateCreation: string;
  lien?: string;
  metadata?: Record<string, any>;
}

export interface NotificationState {
  items: Notification[];
  unreadCount: number;
  isLoading: boolean;
  error: string | null;
}

// ============================================================================
// DASHBOARD TYPES
// ============================================================================

export interface DashboardAnalytics {
  totalComplaints: number;
  complaintsEnCours: number;
  complaintsResolues: number;
  tauxResolution: number;
  tempsResolutionMoyen: Record<string, number>;
  satisfactionClients: SatisfactionStats;
  slaComplianceRate: number;
  slaBreachedCount: number;
}

export interface SatisfactionStats {
  moyenne: number;
  totalEvaluations: number;
  repartition: Record<string, number>;
}

export interface DashboardState {
  analytics: DashboardAnalytics | null;
  isLoading: boolean;
  error: string | null;
  filters: DashboardFilters;
}

export interface DashboardFilters {
  periode: 'jour' | 'semaine' | 'mois' | 'annee';
  dateDebut?: string;
  dateFin?: string;
  agent?: string;
  categorie?: string;
}

// ============================================================================
// API RESPONSE TYPES
// ============================================================================

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  message?: string;
  errors?: string[];
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// ============================================================================
// FORM TYPES
// ============================================================================

export interface LoginForm {
  email: string;
  password: string;
}

export interface RegisterForm {
  email: string;
  password: string;
  confirmPassword: string;
  nom: string;
  prenom: string;
  telephone?: string;
  dateNaissance?: string;
  adresse?: string;
}

export interface ComplaintForm {
  titre: string;
  description: string;
  categorieId: string;
  sousCategorieId?: string;
  priorite: ComplaintPriority;
  fichiers?: File[];
}

export interface CommentForm {
  contenu: string;
  isInterne: boolean;
}

// ============================================================================
// ROOT STATE
// ============================================================================

export interface RootState {
  auth: AuthState;
  complaints: ComplaintState;
  notifications: NotificationState;
  dashboard: DashboardState;
} 