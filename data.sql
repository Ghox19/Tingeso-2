-- Primera Vivienda
INSERT INTO loan (name, description, max_years, min_interest, max_interest, max_amount)
VALUES ('Primera Vivienda', 'Préstamo para adquisición de primera vivienda', 30, 3.5, 5.0, 80);

-- Segunda Vivienda
INSERT INTO loan (name, description, max_years, min_interest, max_interest, max_amount)
VALUES ('Segunda Vivienda', 'Préstamo para adquisición de segunda vivienda', 20, 4.0, 6.0, 70);

-- Propiedades Comerciales
INSERT INTO loan (name, description, max_years, min_interest, max_interest, max_amount)
VALUES ('Propiedades Comerciales', 'Préstamo para adquisición de propiedades comerciales', 25, 5.0, 7.0, 60);

-- Remodelación
INSERT INTO loan (name, description, max_years, min_interest, max_interest, max_amount)
VALUES ('Remodelación', 'Préstamo para remodelación de propiedades', 15, 4.5, 6.0, 50);

-- Insertar requisitos
INSERT INTO loan_requirements (loan_id, requirement) VALUES
                                                         (1, 'Comprobante de ingresos'),
                                                         (1, 'Certificado de avalúo'),
                                                         (1, 'Historial crediticio'),
                                                         (2, 'Comprobante de ingresos'),
                                                         (2, 'Certificado de avalúo'),
                                                         (2, 'Escritura de la primera vivienda'),
                                                         (2, 'Historial crediticio'),
                                                         (3, 'Estado financiero del negocio'),
                                                         (3, 'Comprobante de ingresos'),
                                                         (3, 'Certificado de avalúo'),
                                                         (3, 'Plan de negocios'),
                                                         (4, 'Comprobante de ingresos'),
                                                         (4, 'Presupuesto de la remodelación'),
                                                         (4, 'Certificado de avalúo actualizado');