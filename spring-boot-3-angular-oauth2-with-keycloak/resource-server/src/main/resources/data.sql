-- Table des permissions fines
CREATE TABLE permission (
                            code VARCHAR PRIMARY KEY,
                            description TEXT NOT NULL
);

-- Table des profils métier
CREATE TABLE profile (
                         code VARCHAR PRIMARY KEY,
                         label TEXT NOT NULL
);

-- Table des rôles Keycloak
CREATE TABLE role (
                      name VARCHAR PRIMARY KEY
);

-- Table de liaison profil <-> permission
CREATE TABLE profile_permission (
                                    profile_code VARCHAR NOT NULL REFERENCES profile(code) ON DELETE CASCADE,
                                    permission_code VARCHAR NOT NULL REFERENCES permission(code) ON DELETE CASCADE,
                                    PRIMARY KEY (profile_code, permission_code)
);

-- Table de liaison role <-> profile
CREATE TABLE role_profile (
                              role_name VARCHAR NOT NULL REFERENCES role(name) ON DELETE CASCADE,
                              profile_code VARCHAR NOT NULL REFERENCES profile(code) ON DELETE CASCADE,
                              PRIMARY KEY (role_name, profile_code)
);


-- INSERT
SET search_path TO mon_schema;

-- Permissions
INSERT INTO permission (code, description) VALUES
                                               ('CAN_VIEW_CLIENTS', 'Voir les clients'),
                                               ('CAN_MODIFY_CLIENTS', 'Modifier les clients'),
                                               ('CAN_EXPORT_PDF', 'Exporter les synthèses PDF'),
                                               ('CAN_VIEW_AUDIT_LOGS', 'Voir les logs'),
                                               ('CAN_OPEN_TICKETS', 'Ouvrir des tickets support'),
                                               ('CAN_ASSIGN_CONTRACTS', 'Attribuer des contrats');

-- Profils
INSERT INTO profile (code, label) VALUES
                                      ('ChefAgence', 'Chef d’Agence'),
                                      ('Conseiller', 'Conseiller Front'),
                                      ('Auditeur', 'Auditeur'),
                                      ('SupportClient', 'Support Client');

-- Profil <-> Permission
INSERT INTO profile_permission VALUES
                                   ('ChefAgence', 'CAN_VIEW_CLIENTS'),
                                   ('ChefAgence', 'CAN_MODIFY_CLIENTS'),
                                   ('ChefAgence', 'CAN_EXPORT_PDF'),
                                   ('ChefAgence', 'CAN_ASSIGN_CONTRACTS'),

                                   ('Conseiller', 'CAN_VIEW_CLIENTS'),
                                   ('Conseiller', 'CAN_MODIFY_CLIENTS'),

                                   ('Auditeur', 'CAN_VIEW_CLIENTS'),
                                   ('Auditeur', 'CAN_VIEW_AUDIT_LOGS'),

                                   ('SupportClient', 'CAN_VIEW_CLIENTS'),
                                   ('SupportClient', 'CAN_OPEN_TICKETS');

-- Rôles Keycloak
INSERT INTO role (name) VALUES
                            ('CHEF_AGENCE'),
                            ('CONSEILLER'),
                            ('AUDITEUR'),
                            ('SUPPORT');

-- Rôle <-> Profil
INSERT INTO role_profile VALUES
                             ('CHEF_AGENCE', 'ChefAgence'),
                             ('CONSEILLER', 'Conseiller'),
                             ('AUDITEUR', 'Auditeur'),
                             ('SUPPORT', 'SupportClient');
