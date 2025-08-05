CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       lastname VARCHAR(100) NOT NULL
);


INSERT INTO users (name, lastname) VALUES ('Alice', 'Johnson');
INSERT INTO users (name, lastname) VALUES ('Bob', 'Smith');



INSERT INTO permission (code, description) VALUES
                                               ('CAN_VIEW_CLIENTS', 'Voir les clients'),
                                               ('CAN_MODIFY_CLIENTS', 'Modifier les clients'),
                                               ('CAN_EXPORT_PDF', 'Exporter les synthèses PDF'),
                                               ('CAN_VIEW_AUDIT_LOGS', 'Voir les journaux d’audit'),
                                               ('CAN_OPEN_TICKETS', 'Ouvrir un ticket support'),
                                               ('CAN_ASSIGN_CONTRACTS', 'Attribuer un contrat');

INSERT INTO role (name) VALUES
                            ('CONSEILLER'),
                            ('CHEF_AGENCE'),
                            ('AUDITEUR'),
                            ('SUPPORT');

INSERT INTO profile (code, label) VALUES
                                      ('ProfilConseiller', 'Conseiller'),
                                      ('ProfilChefAgence', 'Chef d’Agence'),
                                      ('ProfilAuditeur', 'Auditeur'),
                                      ('ProfilSupportClient', 'Support Client');

-- On lie les profils aux rôles Keycloak
INSERT INTO profile_role VALUES
                             ('ProfilConseiller', 'CONSEILLER'),
                             ('ProfilChefAgence', 'CHEF_AGENCE'),
                             ('ProfilAuditeur', 'AUDITEUR'),
                             ('ProfilSupportClient', 'SUPPORT');

-- Permissions du rôle CONSEILLER
INSERT INTO role_permission VALUES
                                ('CONSEILLER', 'CAN_VIEW_CLIENTS'),
                                ('CONSEILLER', 'CAN_MODIFY_CLIENTS');

-- Permissions du rôle CHEF_AGENCE
INSERT INTO role_permission VALUES
                                ('CHEF_AGENCE', 'CAN_VIEW_CLIENTS'),
                                ('CHEF_AGENCE', 'CAN_MODIFY_CLIENTS'),
                                ('CHEF_AGENCE', 'CAN_EXPORT_PDF'),
                                ('CHEF_AGENCE', 'CAN_ASSIGN_CONTRACTS');

-- Permissions du rôle AUDITEUR
INSERT INTO role_permission VALUES
                                ('AUDITEUR', 'CAN_VIEW_CLIENTS'),
                                ('AUDITEUR', 'CAN_VIEW_AUDIT_LOGS');

-- Permissions du rôle SUPPORT
INSERT INTO role_permission VALUES
                                ('SUPPORT', 'CAN_VIEW_CLIENTS'),
                                ('SUPPORT', 'CAN_OPEN_TICKETS');


