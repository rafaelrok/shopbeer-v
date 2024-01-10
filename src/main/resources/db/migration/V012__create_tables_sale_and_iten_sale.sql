CREATE TABLE sale (
    code BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    create_date DATETIME NOT NULL,
    value_shipping DECIMAL(10,2),
    value_discount DECIMAL(10,2),
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    observation VARCHAR(200),
    date_time_delivery DATETIME,
    code_costumer BIGINT(20) NOT NULL,
    code_user_employee BIGINT(20) NOT NULL,
    FOREIGN KEY (code_costumer) REFERENCES costumer(code),
    FOREIGN KEY (code_user_employee) REFERENCES user_employee(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE item_sale (
    code BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    quantity INTEGER NOT NULL,
    unitary_value DECIMAL(10,2) NOT NULL,
    code_beer BIGINT(20) NOT NULL,
    code_sale BIGINT(20) NOT NULL,
    FOREIGN KEY (code_beer) REFERENCES beer(code),
    FOREIGN KEY (code_sale) REFERENCES sale(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;