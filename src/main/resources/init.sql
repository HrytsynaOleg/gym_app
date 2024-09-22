DROP SCHEMA IF EXISTS gym CASCADE;

-- -----------------------------------------------------
-- Schema gym
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS gym;
USE gym;

-- -----------------------------------------------------
-- Table user
-- -----------------------------------------------------
DROP TABLE IF EXISTS user;

CREATE TABLE IF NOT EXISTS user (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  user_name VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  is_active TINYINT NULL DEFAULT NULL);
-- insert initial data
INSERT INTO user (id, first_name, last_name, user_name, password, is_active) VALUES (12, 'Kerry', 'King', 'Kerry.King', '1234567890', 1),
(13, 'Tom', 'Arraya', 'Tom.Arraya', '1234567890', 1),
(14, 'Jeff', 'Hanneman', 'Jeff.Hanneman', '1234567890', 1),
(15, 'Dave', 'Lombardo', 'Dave.Lombardo', '1234567890', 1),
(16, 'Bruce', 'Dickinson', 'Bruce.Dickinson', '1234567890', 1),
(17, 'Neil', 'Young', 'Neil.Young', '1234567890', 1),
(18, 'Ozzy', 'Osbourne', 'Ozzy.Osbourne', '1234567890', 1),
(19, 'David', 'Gilmoure', 'David.Gilmoure', '1234567890', 1),
(20, 'James', 'Hetfield', 'James.Hetfield', '1234567890', 1),
(21, 'Kirk', 'Hammett', 'Kirk.Hammett', '1234567890', 1);

-- -----------------------------------------------------
-- Table trainee
-- -----------------------------------------------------
DROP TABLE IF EXISTS trainee;
--
CREATE TABLE IF NOT EXISTS trainee (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  date_of_birth DATETIME NULL DEFAULT NULL,
  address VARCHAR(45) NULL DEFAULT NULL,
  user_id INT NOT NULL,
  CONSTRAINT fk_trainee_user1
    FOREIGN KEY (user_id)
    REFERENCES user (id));
--
-- insert initial data
INSERT INTO trainee (id, date_of_birth, address, user_id) VALUES (234, '1972-08-11', 'Los Angeles', 16),
(248, '1965-07-25', 'Toronto', 17),
(253, '1978-02-03', 'Boston', 18),
(258, '1971-11-12', 'Liverpool', 19),
(265, '1956-03-10', 'Chicago', 20),
(268, '1957-04-11', 'Paris', 21);
--
---- -----------------------------------------------------
---- Table training_type
---- -----------------------------------------------------
DROP TABLE IF EXISTS training_type;

CREATE TABLE IF NOT EXISTS training_type (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NULL DEFAULT NULL);
-- insert initial data
INSERT INTO training_type (id, name) VALUES (9, 'Fitness'),
(10, 'Yoga'),
(11, 'Zumba'),
(12, 'Stretching'),
(14, 'Resistance');

---- -----------------------------------------------------
---- Table trainer
---- -----------------------------------------------------
DROP TABLE IF EXISTS trainer;

CREATE TABLE IF NOT EXISTS trainer (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  training_type_id INT NOT NULL,
  CONSTRAINT fk_trainer_training_type1
    FOREIGN KEY (training_type_id)
    REFERENCES training_type (id),
  CONSTRAINT fk_trainer_user
    FOREIGN KEY (user_id)
    REFERENCES user (id));
--
-- insert initial data
INSERT INTO trainer (id, user_id, training_type_id) VALUES (116, 12, 10), (117, 13, 9), (118, 14, 11), (119, 15, 12);
--
---- -----------------------------------------------------
---- Table training
---- -----------------------------------------------------
DROP TABLE IF EXISTS training;

CREATE TABLE IF NOT EXISTS training(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  trainer_id INT NOT NULL,
  trainee_id INT NOT NULL,
  training_type_id INT NOT NULL,
  training_name VARCHAR(45) NULL DEFAULT NULL,
  training_date DATETIME NULL DEFAULT NULL,
  training_duration INT NULL DEFAULT NULL,
  CONSTRAINT fk_training_trainee1
    FOREIGN KEY (trainee_id)
    REFERENCES trainee (id),
  CONSTRAINT fk_training_trainer1
    FOREIGN KEY (trainer_id)
    REFERENCES trainer (id),
  CONSTRAINT fk_training_training_type1
    FOREIGN KEY (training_type_id)
    REFERENCES training_type (id));
--
-- insert initial data
INSERT INTO training (id, trainer_id, trainee_id, training_type_id, training_name, training_date, training_duration)
VALUES (856, 116, 234, 10, 'new training', '2024-09-12', 20),
(857, 116, 234, 10, 'new training', '2024-09-14', 30),
(858, 117, 248, 9, 'new training', '2024-09-12', 40),
(859, 117, 253, 9, 'new training', '2024-09-12', 40),
(860, 117, 258, 9, 'new training', '2024-09-12', 40),
(861, 118, 265, 11, 'new training', '2024-09-16', 20),
(862, 119, 268, 12, 'new training', '2024-09-18', 35);
--
---- -----------------------------------------------------
---- Table trainer_trainee
---- -----------------------------------------------------
DROP TABLE IF EXISTS trainer_trainee;

CREATE TABLE IF NOT EXISTS trainer_trainee (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  trainee_id INT NOT NULL,
  trainer_id INT NOT NULL,
  CONSTRAINT fk_trainer_trainee_trainee1
    FOREIGN KEY (trainee_id)
    REFERENCES trainee (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_trainer_trainee_trainer1
    FOREIGN KEY (trainer_id)
    REFERENCES trainer (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- insert initial data
INSERT INTO trainer_trainee (id, trainee_id, trainer_id)
VALUES (15, 234, 116),
(16, 248, 117),
(17, 253, 117),
(18, 258, 117),
(19, 265, 118),
(20, 268, 119);