DROP TABLE IF EXISTS pet;

CREATE TABLE pet (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  ownerId INT NOT NULL,
  PetType VARCHAR(25) NOT NULL,
  TrackerType VARCHAR(25) DEFAULT NULL,
  inZone BOOLEAN DEFAULT NOT NULL,
  lostTracker BOOLEAN DEFAULT NULL
);