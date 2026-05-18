DELETE FROM conta;
DELETE FROM cliente;

INSERT INTO cliente (id, nome, cpf, email) VALUES (1, 'Ana Silva', '111', 'ana@test.com');
INSERT INTO cliente (id, nome, cpf, email) VALUES (2, 'Bruno Souza', '222', 'bruno@test.com');

INSERT INTO conta (id, titular, saldo, cliente_id) VALUES (1, 'Ana', 1000.00, 1);
INSERT INTO conta (id, titular, saldo, cliente_id) VALUES (2, 'Bruno', 1000.00, 2);