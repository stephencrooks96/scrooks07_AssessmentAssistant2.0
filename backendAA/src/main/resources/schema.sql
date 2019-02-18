DROP TABLE IF EXISTS Alternative, Answer, AssociationType, CorrectPoint, Inputs, Message, Modules, ModuleAssociation, OptionEntries, OptionTbl, PasswordReset, Question, QuestionType, TestQuestion, TestResult, Tests, TimeModifier, TutorRequests, Users, UserRole, UserSessions;

CREATE TABLE IF NOT EXISTS Alternative (
  alternativeID     int(11) NOT NULL AUTO_INCREMENT,
  correctPointID    int(11) NOT NULL,
  alternativePhrase text    NOT NULL,
  PRIMARY KEY(alternativeID)
);

CREATE TABLE IF NOT EXISTS Answer (
  answerID       int(11) NOT NULL AUTO_INCREMENT,
  questionID     int(11) NOT NULL,
  testID         int(11) NOT NUll,
  answererID     int(11) NOT NULL,
  markerID       int(11) NOT NULL,
  content        text    NOT NULL,
  score          int(11),
  feedback       text,
  markerApproved tinyint(1),
  tutorApproved  tinyint(1),
  PRIMARY KEY(answerID)
);

CREATE TABLE IF NOT EXISTS AssociationType (
  associationTypeID int(11)     NOT NULL AUTO_INCREMENT,
  associationType   varchar(30) NOT NULL,
  PRIMARY KEY(associationTypeID)
);

CREATE TABLE IF NOT EXISTS CorrectPoint (
  correctPointID int(11) NOT NULL AUTO_INCREMENT,
  questionID     int(11) NOT NULL,
  phrase         text    NOT NULL,
  marksWorth     double  NOT NULL,
  feedback       text    NOT NULL,
  indexedAt      int(11),
  PRIMARY KEY(correctPointID)
);


CREATE TABLE IF NOT EXISTS Inputs (
  inputID    int(11) NOT NULL AUTO_INCREMENT,
  inputValue text,
  inputIndex int(11) NOT NULL,
  answerID   int(11) NOT NULL,
  PRIMARY KEY(inputID)
);

CREATE TABLE IF NOT EXISTS Message (
  messageID        int(11)      NOT NULL AUTO_INCREMENT,
  content          text         NOT NULL,
  recipientID      int(11)      NOT NULL,
  senderID         int(11)      NOT NULL,
  messageTimestamp timestamp(3) NOT NULL,
  newMessage       tinyint(1)   NOT NULL,
  PRIMARY KEY(messageID)
);

CREATE TABLE IF NOT EXISTS Modules (
  moduleID          int(11)      NOT NULL AUTO_INCREMENT,
  moduleName        varchar(50)  NOT NULL,
  moduleDescription varchar(500) NOT NULL,
  tutorUserID       int(11)      NOT NULL,
  year              year(4)      NOT NULL,
  approved          tinyint(1)   NOT NULL,
  PRIMARY KEY(moduleID)
);


CREATE TABLE IF NOT EXISTS ModuleAssociation (
  associationID   int(11) NOT NULL AUTO_INCREMENT,
  moduleID        int(11) NOT NULL,
  userID          int(11) NOT NULL,
  associationType int(11) NOT NULL,
  PRIMARY KEY(associationID)
);


CREATE TABLE IF NOT EXISTS OptionTbl (
  optionID      int(11) NOT NULL AUTO_INCREMENT,
  questionID    int(11) NOT NULL,
  optionContent text    NOT NULL,
  worthMarks    int(11) NOT NULL,
  feedback      text    NOT NULL,
  PRIMARY KEY(optionID)
);

CREATE TABLE IF NOT EXISTS OptionEntries (
  optionEntryID int(11) NOT NULL AUTO_INCREMENT,
  optionID      int(11) NOT NULL,
  answerID      int(11) NOT NULL,
  PRIMARY KEY(optionEntryID)
);

CREATE TABLE IF NOT EXISTS PasswordReset (
  userID      int(11) NOT NULL,
  resetString varchar(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS Question (
  questionType    int(11) NOT NULL,
  questionID      int(11) NOT NULL AUTO_INCREMENT,
  questionContent text    NOT NULL,
  questionFigure  BLOB,
  maxScore        int(11) NOT NULL,
  creatorID       int(11) NOT NULL,
  allThatApply    tinyint(1),
  PRIMARY KEY(questionID)
);


CREATE TABLE IF NOT EXISTS QuestionType (
  questionTypeID int(11) NOT NULL AUTO_INCREMENT,
  questionType   varchar(255) NOT NULL,
  PRIMARY KEY(questionTypeID)
);

CREATE TABLE IF NOT EXISTS TestQuestion (
  testQuestionID int(11) NOT NULL AUTO_INCREMENT,
  testID         int(11) NOT NULL,
  questionID     int(11) NOT NULL,
  PRIMARY KEY(testQuestionID)
);

CREATE TABLE IF NOT EXISTS TestResult (
  testResultID int(11) NOT NULL AUTO_INCREMENT,
  testID       int(11) NOT NULL,
  studentID    int(11) NOT NULL,
  testScore    int(11) NOT NULL,
  PRIMARY KEY(testResultID)
);

CREATE TABLE IF NOT EXISTS Tests (
  testID         int(11)     NOT NULL AUTO_INCREMENT,
  moduleID       int(11)     NOT NULL,
  testTitle      varchar(50) NOT NULL,
  startDateTime  timestamp   NOT NULL,
  endDateTime    timestamp   NOT NULL,
  publishResults tinyint(1)  NOT NULL,
  scheduled      tinyint(1)  NOT NULL,
  publishGrades  tinyint(1)  NOT NULL,
  PRIMARY KEY(testID)
);

CREATE TABLE IF NOT EXISTS TimeModifier (
  userID       int(11)    NOT NULL,
  timeModifier double NOT NULL
);

CREATE TABLE IF NOT EXISTS TutorRequests (
  tutorRequestID int(11)    NOT NULL AUTO_INCREMENT,
  userID         int(11)    NOT NULL UNIQUE,
  reason         text       NOT NULL,
  approved       tinyint(1) NOT NULL,
  PRIMARY KEY(tutorRequestID)
);

CREATE TABLE IF NOT EXISTS Users (
  userID     int(11)      NOT NULL AUTO_INCREMENT,
  username   varchar(50)  NOT NULL,
  password   varchar(200) NOT NULL,
  firstName  varchar(30)  NOT NULL,
  lastName   varchar(30)  NOT NULL,
  enabled    tinyint(1)   NOT NULL,
  userRoleID int(11)      NOT NULL,
  tutor      tinyint(1)   NOT NULL,
  PRIMARY KEY(userID)
);

CREATE TABLE IF NOT EXISTS UserRole (
  userRoleID int(11)     NOT NULL AUTO_INCREMENT,
  role       varchar(50) NOT NULL,
  PRIMARY KEY(userRoleID)
);

CREATE TABLE IF NOT EXISTS UserSessions (
  username   VARCHAR(64)       NOT NULL,
  sessionID  VARCHAR(64)       NOT NULL,
  tokenID    VARCHAR(64)       NOT NULL,
  lastActive TIMESTAMP NOT NULL
);

CREATE INDEX correctPointIDAlt ON Alternative(correctPointID);

CREATE INDEX questionIDAns ON Answer(questionID);
CREATE INDEX markerID ON Answer(markerID);
CREATE INDEX testID ON Answer(testID);

CREATE INDEX questionIDCorrect ON CorrectPoint(questionID);

CREATE INDEX answerID_input ON Inputs(answerID);

CREATE INDEX recipientID ON Message(recipientID);
CREATE INDEX senderID ON Message(senderID);

CREATE INDEX tutorUserID ON Modules(tutorUserID);

CREATE INDEX moduleIDAssoc ON ModuleAssociation(moduleID);
CREATE INDEX userIDAssoc ON ModuleAssociation(userID);
CREATE INDEX associationType ON ModuleAssociation(associationType);

CREATE INDEX questionIDOpt ON OptionTbl(questionID);

CREATE INDEX optionIDEnt ON OptionEntries(optionID);
CREATE INDEX answerIDEnt ON OptionEntries(answerID);

CREATE INDEX questionType ON Question(questionType);
CREATE INDEX creatorID ON Question(creatorID);

CREATE INDEX testIDTestQ ON TestQuestion(testID);
CREATE INDEX questionIDTestQ ON TestQuestion(questionID);

CREATE INDEX studentID ON TestResult(studentID);
CREATE INDEX testIDTestRes ON TestResult(testID);

CREATE INDEX moduleIDTests ON Tests(moduleID);

CREATE INDEX userIDTutorRequest ON TutorRequests(userID);

CREATE UNIQUE INDEX username ON Users(username);
CREATE INDEX userRoleID ON Users(userRoleID);

ALTER TABLE Answer
  ADD FOREIGN KEY (questionID) REFERENCES Question (questionID);
ALTER TABLE Answer
  ADD FOREIGN KEY (answererID) REFERENCES Users (userID);
ALTER TABLE Answer
  ADD FOREIGN KEY (markerID) REFERENCES Users (userID);
ALTER TABLE Answer
  ADD FOREIGN KEY (testID) REFERENCES Tests (testID);


ALTER TABLE Inputs
  ADD FOREIGN KEY (answerID) REFERENCES Answer (answerID);


ALTER TABLE Message
  ADD FOREIGN KEY (recipientID) REFERENCES Users (userID);
ALTER TABLE Message
  ADD FOREIGN KEY (senderID) REFERENCES Users (userID);


ALTER TABLE Modules
  ADD FOREIGN KEY (tutorUserID) REFERENCES Users (userID);


ALTER TABLE ModuleAssociation
  ADD FOREIGN KEY (associationType) REFERENCES AssociationType (associationTypeID);
ALTER TABLE ModuleAssociation
  ADD FOREIGN KEY (moduleID) REFERENCES Modules (moduleID);
ALTER TABLE ModuleAssociation
  ADD FOREIGN KEY (userID) REFERENCES Users (userID);


ALTER TABLE OptionTbl
  ADD FOREIGN KEY (questionID) REFERENCES Question (questionID);

ALTER TABLE OptionEntries
  ADD FOREIGN KEY (optionID) REFERENCES OptionTbl (optionID);
ALTER TABLE OptionEntries
  ADD FOREIGN KEY (answerID) REFERENCES Answer (answerID);


ALTER TABLE PasswordReset
  ADD FOREIGN KEY (userID) REFERENCES Users (userID);


ALTER TABLE Question
  ADD FOREIGN KEY (questionType) REFERENCES QuestionType (questionTypeID);
ALTER TABLE Question
  ADD FOREIGN KEY (creatorID) REFERENCES Users (userID);


ALTER TABLE TestQuestion
  ADD FOREIGN KEY (questionID) REFERENCES Question (questionID);
ALTER TABLE TestQuestion
  ADD FOREIGN KEY (testID) REFERENCES Tests (testID);


ALTER TABLE TestResult
  ADD FOREIGN KEY (testID) REFERENCES Tests (testID);
ALTER TABLE TestResult
  ADD FOREIGN KEY (studentID) REFERENCES Users (userID);


ALTER TABLE Tests
  ADD FOREIGN KEY (moduleID) REFERENCES Modules (moduleID);

ALTER TABLE TimeModifier
  ADD FOREIGN KEY (userID) REFERENCES Users (userID);

ALTER TABLE TutorRequests
  ADD FOREIGN KEY (userID) REFERENCES Users (userID);

ALTER TABLE Users
  ADD FOREIGN KEY (userRoleID) REFERENCES UserRole (userRoleID);

insert into UserRole (role)
values ('ROLE_ADMIN');

insert into UserRole (role)
values ('ROLE_USER');

insert into QuestionType (questionType) values ('Text-based');
insert into QuestionType (questionType) values ('Multiple-Choice');
insert into QuestionType (questionType) values ('Insert the word');

insert into Users (username, password, firstname, lastname, enabled, userRoleID, tutor)
values ('pgault04@qub.ac.uk', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'Paul', 'Gault', 1, 1, 0);

insert into Users (username, password, firstname, lastname, enabled, userRoleID, tutor)
values ('richard.gault@qub.ac.uk', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'Richard', 'Gault', 1, 2, 1);

insert into Users (username, password, firstname, lastname, enabled, userRoleID, tutor)
values ('bart.simpson@qub.ac.uk', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'Bart', 'Simpson', 1, 2, 0);

insert into Users (username, password, firstname, lastname, enabled, userRoleID, tutor)
values ('clark.kent@qub.ac.uk', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'Clark', 'Kent', 1, 2, 0);

insert into Users (username, password, firstname, lastname, enabled, userRoleID, tutor)
values ('bruce.wayne@qub.ac.uk', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'Bruce', 'Wayne', 1, 2, 0);

insert into Users (username, password, firstname, lastname, enabled, userRoleID, tutor)
values ('peter.parker@qub.ac.uk', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'Peter', 'Parker', 1, 2, 0);

insert into Users (username, password, firstname, lastname, enabled, userRoleID, tutor)
values ('luke.skywalker@qub.ac.uk', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'Luke', 'Skywalker', 1, 2, 0);

insert into Users (username, password, firstname, lastname, enabled, userRoleID, tutor)
values ('harry.potter@qub.ac.uk', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'Harry', 'Potter', 1, 2, 0);

insert into Modules (moduleName, moduleDescription, tutorUserID, year, approved) values ('Foundation Physics', 'Physics for beginners', 2, 2018, 1);
insert into Modules (moduleName, moduleDescription, tutorUserID, year, approved) values ('Ad Physics', 'Advanced Physics', 2, 2018, 1);

insert into AssociationType (associationType) values ('tutor');
insert into AssociationType (associationType) values ('student');
insert into AssociationType (associationType) values ('teaching assistant');

insert into ModuleAssociation (moduleID, userID, associationType) values (1, 1, 2);
insert into ModuleAssociation (moduleID, userID, associationType) values (2, 1, 2);
insert into ModuleAssociation (moduleID, userID, associationType) values (1, 2, 1);
insert into ModuleAssociation (moduleID, userID, associationType) values (2, 2, 1);
insert into ModuleAssociation (moduleID, userID, associationType) values (1, 3, 3);
insert into ModuleAssociation (moduleID, userID, associationType) values (2, 3, 3);

insert into ModuleAssociation (moduleID, userID, associationType) values (1, 4, 2);
insert into ModuleAssociation (moduleID, userID, associationType) values (1, 5, 2);
insert into ModuleAssociation (moduleID, userID, associationType) values (1, 6, 2);
insert into ModuleAssociation (moduleID, userID, associationType) values (1, 7, 2);
insert into ModuleAssociation (moduleID, userID, associationType) values (1, 8, 2);

insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Quantum Physics', '2018-11-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Medical Physics', '2018-11-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Astrophysics', '2018-11-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);


insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Quantum Physics 2', '2019-06-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Medical Physics 2', '2019-06-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Astrophysics 2', '2019-06-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);


insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Quantum Physics 3', '2019-01-11 00:00:00', '2019-11-11 00:00:00', 0, 0, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Medical Physics 3', '2019-01-11 00:00:00', '2019-11-11 00:00:00', 0, 0, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Astrophysics 3', '2019-01-11 00:00:00', '2019-11-11 00:00:00', 0, 0, 0);


insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Quantum Physics 3', '2018-12-5 12:00:00', '2018-12-5 13:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Medical Physics 3', '2018-12-5 12:00:00', '2018-12-5 13:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Astrophysics 3', '2018-12-5 12:00:00', '2018-12-5 13:00:00', 0, 1, 0);

insert into question(questionType, questionContent, questionFigure, maxScore, creatorID, allThatApply) values (1, 'What is the name of the negatively charged particles in an atom?', null, 3, 2, 0);
insert into question(questionType, questionContent, questionFigure, maxScore, creatorID, allThatApply) values (1, 'How much wood would a wood chuck chuck if a wood chuck could chuck wood?', null, 3, 2, 0);
insert into question(questionType, questionContent, questionFigure, maxScore, creatorID, allThatApply) values (1, 'In Star Wars what color was Luke Skywalkers lightsaber?', null, 2, 2, 0);

insert into testquestion(testID, questionID) values (7, 1);
insert into testquestion(testID, questionID) values (7, 2);
insert into testquestion(testID, questionID) values (7, 3);

insert into testquestion(testID, questionID) values (8, 1);
insert into testquestion(testID, questionID) values (8, 2);
insert into testquestion(testID, questionID) values (8, 3);

insert into testquestion(testID, questionID) values (9, 1);
insert into testquestion(testID, questionID) values (9, 2);
insert into testquestion(testID, questionID) values (9, 3);

insert into testquestion(testID, questionID) values (10, 1);
insert into testquestion(testID, questionID) values (10, 2);
insert into testquestion(testID, questionID) values (10, 3);

insert into testquestion(testID, questionID) values (11, 1);
insert into testquestion(testID, questionID) values (11, 2);
insert into testquestion(testID, questionID) values (11, 3);

insert into testquestion(testID, questionID) values (12, 1);
insert into testquestion(testID, questionID) values (12, 2);
insert into testquestion(testID, questionID) values (12, 3);

insert into Answer(questionID, testID, answererID, markerID, content, score, markerApproved, tutorApproved) values (1, 10, 1, 2, 'Electrons', null, 1, 0);
insert into Answer(questionID, testID, answererID, markerID, content, score, markerApproved, tutorApproved) values (2, 10, 1, 3, 'A lot', 3, 1 ,0);
insert into Answer(questionID, testID, answererID, markerID, content, score, markerApproved, tutorApproved) values (3, 10, 1, 2, 'Blue then green', 2, 0, 0);

insert into Answer(questionID, testID, answererID, markerID, content, score) values (1, 11, 1, 2, 'Electrons', null);
insert into Answer(questionID, testID, answererID, markerID, content, score) values (2, 11, 1, 2, 'A lot', null);
insert into Answer(questionID, testID, answererID, markerID, content, score) values (3, 11, 1, 2, 'Blue then green', null);

insert into Answer(questionID, testID, answererID, markerID, content, score) values (1, 12, 1, 2, 'Electrons', null);
insert into Answer(questionID, testID, answererID, markerID, content, score) values (2, 12, 1, 3, 'A lot', null);
insert into Answer(questionID, testID, answererID, markerID, content, score) values (3, 12, 1, 2, 'Blue then green', 2);