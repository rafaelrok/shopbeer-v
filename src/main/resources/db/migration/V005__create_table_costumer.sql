CREATE TABLE costumer (
    code BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    type_person VARCHAR(15) NOT NULL,
    cpf_cnpj VARCHAR(30),
    telephone VARCHAR(20),
    email VARCHAR(50) NOT NULL,
    address VARCHAR(50),
    number VARCHAR(15),
    complement VARCHAR(20),
    zipCode VARCHAR(15),
    code_city BIGINT(20),
    FOREIGN KEY (code_city) REFERENCES city(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;