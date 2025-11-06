-- =========================
-- LIMPEZA DE DADOS
-- =========================


-- =========================
-- REINÍCIO DE SEQUÊNCIAS (H2)
-- =========================


-- =========================
-- PAÍSES
-- =========================
INSERT INTO pais (nome) VALUES ('Brasil');
INSERT INTO pais (nome) VALUES ('Argentina');

-- =========================
-- ESTADOS
-- =========================
INSERT INTO estado (nome, sigla, id_pais) VALUES ('Bahia', 'BA', 1);
INSERT INTO estado (nome, sigla, id_pais) VALUES ('Goiás', 'GO', 1);
INSERT INTO estado (nome, sigla, id_pais) VALUES ('Mendoza', 'MD', 2);

-- =========================
-- CIDADES
-- =========================
INSERT INTO cidade (nome, id_estado) VALUES ('Salvador', 1);
INSERT INTO cidade (nome, id_estado) VALUES ('Goiânia', 2);
INSERT INTO cidade (nome, id_estado) VALUES ('Mendoza', 3);

-- =========================
-- MARCAS
-- =========================
INSERT INTO marca (nome, paisDeOrigem, anofundacao, classificacao)
VALUES ('Casa Perini', 'Brasil', '1980', 'Premium');

INSERT INTO marca (nome, paisDeOrigem, anofundacao, classificacao)
VALUES ('Miolo', 'Brasil', '1990', 'Standard');

-- =========================
-- ESTILOS
-- =========================
INSERT INTO estilo (nome) VALUES ('Suave');
INSERT INTO estilo (nome) VALUES ('Seco');

-- =========================
-- OCASIÕES
-- =========================
INSERT INTO ocasiao (nome) VALUES ('Aniversário');
INSERT INTO ocasiao (nome) VALUES ('Casamento');

-- =========================
-- SAFRAS
-- =========================
INSERT INTO safra ( ano, descricao) VALUES (2020, 'Safra especial');
INSERT INTO safra (ano, descricao) VALUES (2021, 'Safra jovem');

-- =========================
-- TIPOS DE VINHO
-- =========================
INSERT INTO tipovinho (nome) VALUES ('Tinto');
INSERT INTO tipovinho (nome) VALUES ('Branco');

-- =========================
-- UVAS
-- =========================
INSERT INTO uva (nome) VALUES ( 'Cabernet Sauvignon');
INSERT INTO uva (nome) VALUES ( 'Merlot');

-- =========================
-- VINHOS
-- =========================
INSERT INTO vinho ( nome, descricao, preco, quantEstoque, imagem, sku, id_marca, id_estilo, id_ocasiao, id_pais, id_safra, id_tipo_vinho)
VALUES ('Vinho Tinto Perini', 'Tinto encorpado', 79.90, 10, 'perini.jpg', 'VIN001', 1, 1, 1, 1, 1, 1);

INSERT INTO vinho ( nome, descricao, preco, quantEstoque, imagem, sku, id_marca, id_estilo, id_ocasiao, id_pais, id_safra, id_tipo_vinho)
VALUES ( 'Vinho Branco Miolo', 'Branco leve', 59.90, 8, 'miolo.jpg', 'VIN002', 2, 2, 2, 1, 2, 2);

-- =========================
-- VINHO x UVA (N:N)
-- =========================
INSERT INTO vinho_uva (id_vinho, id_uva) VALUES (1, 1);
INSERT INTO vinho_uva (id_vinho, id_uva) VALUES (1, 2);
INSERT INTO vinho_uva (id_vinho, id_uva) VALUES (2, 2);

-- =========================
-- AJUSTE FINAL DAS SEQUÊNCIAS
-- =========================
