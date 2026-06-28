CREATE DATABASE IF NOT EXISTS gestion_futbol CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;

USE gestion_futbol;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nombre_club VARCHAR(100),
    rol ENUM('CLUB', 'ADMIN') NOT NULL DEFAULT 'CLUB'
);

CREATE TABLE jugadores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    club_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    edad INT NOT NULL,
    altura DECIMAL(3,2) NOT NULL,
    peso DECIMAL(5,2) NOT NULL,
    habilidad INT NOT NULL,
    puesto ENUM('Arquero', 'Defensor', 'Mediocampista', 'Delantero') NOT NULL,
    CONSTRAINT fk_jugador_club FOREIGN KEY (club_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE partidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    club_local_id INT NOT NULL,
    club_visitante_id INT NOT NULL,
    goles_local INT DEFAULT NULL,
    goles_visitante INT DEFAULT NULL,
    fecha_jornada INT NOT NULL,
    jugado BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_partido_local FOREIGN KEY (club_local_id) REFERENCES usuarios(id),
    CONSTRAINT fk_partido_visitante FOREIGN KEY (club_visitante_id) REFERENCES usuarios(id)
);

INSERT INTO usuarios (username, password, nombre_club, rol) VALUES ('admin', 'admin123', NULL, 'ADMIN');

INSERT INTO usuarios (username, password, nombre_club, rol) VALUES
('club1', '1234', 'River Plate', 'CLUB'),
('club2', '1234', 'Boca Juniors', 'CLUB'),
('club3', '1234', 'Racing Club', 'CLUB'),
('club4', '1234', 'Independiente', 'CLUB');

INSERT INTO jugadores (club_id, nombre, edad, altura, peso, habilidad, puesto) VALUES
(2, 'Juan Perez', 24, 1.80, 78.5, 85, 'Delantero'),
(2, 'Carlos Gomez', 28, 1.75, 74.0, 80, 'Mediocampista'),
(2, 'Martin Lopez', 22, 1.90, 85.0, 75, 'Arquero');
