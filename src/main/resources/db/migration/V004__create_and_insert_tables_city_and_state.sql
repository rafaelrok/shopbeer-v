CREATE TABLE state (
    code BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    acronym VARCHAR(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE city (
    code BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    code_state BIGINT(20) NOT NULL,
    FOREIGN KEY (code_state) REFERENCES state(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO state (code, name, acronym) VALUES (1,'Acre', 'AC');
INSERT INTO state (code, name, acronym) VALUES (2,'Bahia', 'BA');
INSERT INTO state (code, name, acronym) VALUES (3,'Goiás', 'GO');
INSERT INTO state (code, name, acronym) VALUES (4,'Minas Gerais', 'MG');
INSERT INTO state (code, name, acronym) VALUES (5,'Santa Catarina', 'SC');
INSERT INTO state (code, name, acronym) VALUES (6,'São Paulo', 'SP');


INSERT INTO city (name, code_state) VALUES ('Rio Branco', 1);
INSERT INTO city (name, code_state) VALUES ('Cruzeiro do Sul', 1);
INSERT INTO city (name, code_state) VALUES ('Salvador', 2);
INSERT INTO city (name, code_state) VALUES ('Porto Seguro', 2);
INSERT INTO city (name, code_state) VALUES ('Santana', 2);
INSERT INTO city (name, code_state) VALUES ('Goiânia', 3);
INSERT INTO city (name, code_state) VALUES ('Itumbiara', 3);
INSERT INTO city (name, code_state) VALUES ('Novo Brasil', 3);
INSERT INTO city (name, code_state) VALUES ('Belo Horizonte', 4);
INSERT INTO city (name, code_state) VALUES ('Uberlândia', 4);
INSERT INTO city (name, code_state) VALUES ('Montes Claros', 4);
INSERT INTO city (name, code_state) VALUES ('Florianópolis', 5);
INSERT INTO city (name, code_state) VALUES ('Criciúma', 5);
INSERT INTO city (name, code_state) VALUES ('Camboriú', 5);
INSERT INTO city (name, code_state) VALUES ('Lages', 5);
INSERT INTO city (name, code_state) VALUES ('São Paulo', 6);
INSERT INTO city (name, code_state) VALUES ('Ribeirão Preto', 6);
INSERT INTO city (name, code_state) VALUES ('Campinas', 6);
INSERT INTO city (name, code_state) VALUES ('Santos', 6);