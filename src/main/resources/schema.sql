DROP SCHEMA IF EXISTS gym CASCADE;

-- -----------------------------------------------------
-- Schema gym
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS gym;
USE gym;

-- -----------------------------------------------------
-- Table user
-- -----------------------------------------------------
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  user_name VARCHAR(45) NOT NULL,
  password VARCHAR(255) NOT NULL,
  is_active TINYINT NULL DEFAULT NULL);

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
    REFERENCES users (id));

---- -----------------------------------------------------
---- Table training_type
---- -----------------------------------------------------
DROP TABLE IF EXISTS training_type;

CREATE TABLE IF NOT EXISTS training_type (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NULL DEFAULT NULL);

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
    REFERENCES users (id));
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
    REFERENCES trainee (id)
    ON DELETE CASCADE,
  CONSTRAINT fk_training_trainer1
    FOREIGN KEY (trainer_id)
    REFERENCES trainer (id),
  CONSTRAINT fk_training_training_type1
    FOREIGN KEY (training_type_id)
    REFERENCES training_type (id));

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
