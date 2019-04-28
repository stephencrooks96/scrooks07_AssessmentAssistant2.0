DROP TABLE IF EXISTS Alternative, Answer, AssociationType, CorrectPoint, Inputs, Message, Modules, ModuleAssociation, OptionEntries, OptionTbl, PasswordReset, Question, QuestionType, QuestionMathLines, TestQuestion, TestResult, Tests, TimeModifier, TutorRequests, Users, UserRole, UserSessions;

CREATE TABLE IF NOT EXISTS Alternative
(
  alternativeID     int(11)    NOT NULL AUTO_INCREMENT,
  correctPointID    int(11)    NOT NULL,
  alternativePhrase text       NOT NULL,
  math              tinyint(1) NOT NULL,
  PRIMARY KEY (alternativeID)
);

CREATE TABLE IF NOT EXISTS Answer
(
  answerID       int(11) NOT NULL AUTO_INCREMENT,
  questionID     int(11) NOT NULL,
  testID         int(11) NOT NUll,
  answererID     int(11) NOT NULL,
  markerID       int(11) NOT NULL,
  content        text,
  score          int(11),
  feedback       text,
  markerApproved tinyint(1),
  tutorApproved  tinyint(1),
  PRIMARY KEY (answerID)
);

CREATE TABLE IF NOT EXISTS AssociationType
(
  associationTypeID int(11)     NOT NULL AUTO_INCREMENT,
  associationType   varchar(30) NOT NULL,
  PRIMARY KEY (associationTypeID)
);

CREATE TABLE IF NOT EXISTS CorrectPoint
(
  correctPointID int(11)    NOT NULL AUTO_INCREMENT,
  questionID     int(11)    NOT NULL,
  phrase         text       NOT NULL,
  marksWorth     double     NOT NULL,
  feedback       text       NOT NULL,
  indexedAt      int(11),
  math           tinyint(1) NOT NULL,
  PRIMARY KEY (correctPointID)
);


CREATE TABLE IF NOT EXISTS Inputs
(
  inputID    int(11)    NOT NULL AUTO_INCREMENT,
  inputValue text,
  inputIndex int(11)    NOT NULL,
  answerID   int(11)    NOT NULL,
  math       tinyint(1) NOT NULL,
  PRIMARY KEY (inputID)
);

CREATE TABLE IF NOT EXISTS Message
(
  messageID        int(11)      NOT NULL AUTO_INCREMENT,
  content          text         NOT NULL,
  recipientID      int(11)      NOT NULL,
  senderID         int(11)      NOT NULL,
  messageTimestamp timestamp(3) NOT NULL,
  newMessage       tinyint(1)   NOT NULL,
  PRIMARY KEY (messageID)
);

CREATE TABLE IF NOT EXISTS Modules
(
  moduleID          int(11)      NOT NULL AUTO_INCREMENT,
  moduleName        varchar(50)  NOT NULL,
  moduleDescription varchar(500) NOT NULL,
  tutorUserID       int(11)      NOT NULL,
  commencementDate  date         NOT NULL,
  endDate           date         NOT NULL,
  approved          tinyint(1)   NOT NULL,
  PRIMARY KEY (moduleID)
);


CREATE TABLE IF NOT EXISTS ModuleAssociation
(
  associationID   int(11) NOT NULL AUTO_INCREMENT,
  moduleID        int(11) NOT NULL,
  userID          int(11) NOT NULL,
  associationType int(11) NOT NULL,
  PRIMARY KEY (associationID)
);


CREATE TABLE IF NOT EXISTS OptionTbl
(
  optionID      int(11) NOT NULL AUTO_INCREMENT,
  questionID    int(11) NOT NULL,
  optionContent text    NOT NULL,
  worthMarks    int(11) NOT NULL,
  feedback      text    NOT NULL,
  PRIMARY KEY (optionID)
);

CREATE TABLE IF NOT EXISTS OptionEntries
(
  optionEntryID int(11) NOT NULL AUTO_INCREMENT,
  optionID      int(11) NOT NULL,
  answerID      int(11) NOT NULL,
  PRIMARY KEY (optionEntryID)
);

CREATE TABLE IF NOT EXISTS PasswordReset
(
  userID      int(11)     NOT NULL,
  resetString varchar(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS Question
(
  questionType    int(11) NOT NULL,
  questionID      int(11) NOT NULL AUTO_INCREMENT,
  questionContent text    NOT NULL,
  questionFigure  MEDIUMBLOB,
  maxScore        int(11) NOT NULL,
  minScore        int(11) NOT NULL,
  creatorID       int(11) NOT NULL,
  allThatApply    tinyint(1),
  PRIMARY KEY (questionID)
);

CREATE TABLE IF NOT EXISTS QuestionMathLines
(
  questionMathLineID int(11) NOT NULL AUTO_INCREMENT,
  questionID         int(11) NOT NULL,
  content            text    NOT NULL,
  indexedAt          int(11) NOT NULL,
  PRIMARY KEY (questionMathLineID)
);


CREATE TABLE IF NOT EXISTS QuestionType
(
  questionTypeID int(11)      NOT NULL AUTO_INCREMENT,
  questionType   varchar(255) NOT NULL,
  PRIMARY KEY (questionTypeID)
);

CREATE TABLE IF NOT EXISTS TestQuestion
(
  testQuestionID int(11) NOT NULL AUTO_INCREMENT,
  testID         int(11) NOT NULL,
  questionID     int(11) NOT NULL,
  PRIMARY KEY (testQuestionID)
);

CREATE TABLE IF NOT EXISTS TestResult
(
  testResultID int(11) NOT NULL AUTO_INCREMENT,
  testID       int(11) NOT NULL,
  studentID    int(11) NOT NULL,
  testScore    int(11) NOT NULL,
  PRIMARY KEY (testResultID)
);

CREATE TABLE IF NOT EXISTS Tests
(
  testID         int(11)     NOT NULL AUTO_INCREMENT,
  moduleID       int(11)     NOT NULL,
  testTitle      varchar(50) NOT NULL,
  startDateTime  timestamp 	 NOT NULL DEFAULT '1970-01-01 00:00:01',
  endDateTime    timestamp   NOT NULL DEFAULT '1970-01-01 00:00:01',
  publishResults tinyint(1)  NOT NULL,
  scheduled      tinyint(1)  NOT NULL,
  publishGrades  tinyint(1)  NOT NULL,
  practice       tinyint(1)  NOT NULL,
  PRIMARY KEY (testID)
);

CREATE TABLE IF NOT EXISTS TimeModifier
(
  userID       int(11) NOT NULL,
  timeModifier double  NOT NULL
);

CREATE TABLE IF NOT EXISTS TutorRequests
(
  tutorRequestID int(11)    NOT NULL AUTO_INCREMENT,
  userID         int(11)    NOT NULL UNIQUE,
  reason         text       NOT NULL,
  approved       tinyint(1) NOT NULL,
  PRIMARY KEY (tutorRequestID)
);

CREATE TABLE IF NOT EXISTS Users
(
  userID     int(11)      NOT NULL AUTO_INCREMENT,
  username   varchar(50)  NOT NULL,
  password   varchar(200) NOT NULL,
  firstName  varchar(30)  NOT NULL,
  lastName   varchar(30)  NOT NULL,
  enabled    tinyint(1)   NOT NULL,
  userRoleID int(11)      NOT NULL,
  tutor      tinyint(1)   NOT NULL,
  PRIMARY KEY (userID)
);

CREATE TABLE IF NOT EXISTS UserRole
(
  userRoleID int(11)     NOT NULL AUTO_INCREMENT,
  role       varchar(50) NOT NULL,
  PRIMARY KEY (userRoleID)
);

CREATE TABLE IF NOT EXISTS UserSessions
(
  username   VARCHAR(64)  NOT NULL,
  token      VARCHAR(255) NOT NULL,
  lastActive TIMESTAMP    NOT NULL
);

CREATE INDEX correctPointIDAlt ON Alternative (correctPointID);

CREATE INDEX questionIDAns ON Answer (questionID);
CREATE INDEX markerID ON Answer (markerID);
CREATE INDEX testID ON Answer (testID);

CREATE INDEX questionIDCorrect ON CorrectPoint (questionID);

CREATE INDEX answerID_input ON Inputs (answerID);

CREATE INDEX recipientID ON Message (recipientID);
CREATE INDEX senderID ON Message (senderID);

CREATE INDEX tutorUserID ON Modules (tutorUserID);

CREATE INDEX moduleIDAssoc ON ModuleAssociation (moduleID);
CREATE INDEX userIDAssoc ON ModuleAssociation (userID);
CREATE INDEX associationType ON ModuleAssociation (associationType);

CREATE INDEX questionIDOpt ON OptionTbl (questionID);

CREATE INDEX optionIDEnt ON OptionEntries (optionID);
CREATE INDEX answerIDEnt ON OptionEntries (answerID);

CREATE INDEX questionIDMath ON QuestionMathLines (questionID);

CREATE INDEX questionType ON Question (questionType);
CREATE INDEX creatorID ON Question (creatorID);

CREATE INDEX testIDTestQ ON TestQuestion (testID);
CREATE INDEX questionIDTestQ ON TestQuestion (questionID);

CREATE INDEX studentID ON TestResult (studentID);
CREATE INDEX testIDTestRes ON TestResult (testID);

CREATE INDEX moduleIDTests ON Tests (moduleID);

CREATE INDEX userIDTutorRequest ON TutorRequests (userID);

CREATE UNIQUE INDEX username ON Users (username);
CREATE INDEX userRoleID ON Users (userRoleID);

CREATE UNIQUE INDEX usernameS ON UserSessions (username);

ALTER TABLE Alternative
  ADD FOREIGN KEY (correctPointID) REFERENCES CorrectPoint (correctPointID) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Answer
  ADD FOREIGN KEY (questionID) REFERENCES Question (questionID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE Answer
  ADD FOREIGN KEY (answererID) REFERENCES Users (userID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE Answer
  ADD FOREIGN KEY (markerID) REFERENCES Users (userID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE Answer
  ADD FOREIGN KEY (testID) REFERENCES Tests (testID) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE CorrectPoint
  ADD FOREIGN KEY (questionID) REFERENCES Question (questionID) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Inputs
  ADD FOREIGN KEY (answerID) REFERENCES Answer (answerID) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE Message
  ADD FOREIGN KEY (recipientID) REFERENCES Users (userID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE Message
  ADD FOREIGN KEY (senderID) REFERENCES Users (userID) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE Modules
  ADD FOREIGN KEY (tutorUserID) REFERENCES Users (userID) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE ModuleAssociation
  ADD FOREIGN KEY (associationType) REFERENCES AssociationType (associationTypeID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE ModuleAssociation
  ADD FOREIGN KEY (moduleID) REFERENCES Modules (moduleID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE ModuleAssociation
  ADD FOREIGN KEY (userID) REFERENCES Users (userID) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE OptionTbl
  ADD FOREIGN KEY (questionID) REFERENCES Question (questionID) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE OptionEntries
  ADD FOREIGN KEY (optionID) REFERENCES OptionTbl (optionID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE OptionEntries
  ADD FOREIGN KEY (answerID) REFERENCES Answer (answerID) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE PasswordReset
  ADD FOREIGN KEY (userID) REFERENCES Users (userID) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE Question
  ADD FOREIGN KEY (questionType) REFERENCES QuestionType (questionTypeID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE Question
  ADD FOREIGN KEY (creatorID) REFERENCES Users (userID) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE QuestionMathLines
  ADD FOREIGN KEY (questionID) REFERENCES Question (questionID) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE TestQuestion
  ADD FOREIGN KEY (questionID) REFERENCES Question (questionID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE TestQuestion
  ADD FOREIGN KEY (testID) REFERENCES Tests (testID) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE TestResult
  ADD FOREIGN KEY (testID) REFERENCES Tests (testID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE TestResult
  ADD FOREIGN KEY (studentID) REFERENCES Users (userID) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE Tests
  ADD FOREIGN KEY (moduleID) REFERENCES Modules (moduleID) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE TimeModifier
  ADD FOREIGN KEY (userID) REFERENCES Users (userID) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE TutorRequests
  ADD FOREIGN KEY (userID) REFERENCES Users (userID) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Users
  ADD FOREIGN KEY (userRoleID) REFERENCES UserRole (userRoleID) ON UPDATE CASCADE ON DELETE CASCADE;

INSERT INTO AssociationType (associationTypeID, associationType) VALUES (1,'tutor');
INSERT INTO AssociationType (associationTypeID, associationType) VALUES (2,'student');
INSERT INTO AssociationType (associationTypeID, associationType) VALUES (3,'teaching assistant');

INSERT INTO UserRole (userRoleID, role) VALUES (1,'ROLE_ADMIN');
INSERT INTO UserRole (userRoleID, role) VALUES (2,'ROLE_USER');

INSERT INTO Users (userID, username, password, firstName, lastName, enabled, userRoleID, tutor) VALUES (1,'tutor@qub.ac.uk','$2a$10$fcYVmhWKfvSHIOgsy8vQiuqdYbyangYXr88k3fyRDAscm3Qjg80Py','Tutor','User',1,1,1);
INSERT INTO Users (userID, username, password, firstName, lastName, enabled, userRoleID, tutor) VALUES (2,'student@qub.ac.uk','$2a$10$DrO3jV2QIQbqeCf5G2H4hux/vX7D0XpDgPNwzfaDmFpzsAU.ZiFkO','Student','User',0,1,0);
INSERT INTO Users (userID, username, password, firstName, lastName, enabled, userRoleID, tutor) VALUES (3,'t_a@qub.ac.uk','$2a$10$aDBYhCrLseqkfRKNYIOBiOZQfq14NONtGr1xo0Vj5qkCQJ.fsg7fi','Teaching','Assistant',0,1,0);
INSERT INTO Users (userID, username, password, firstName, lastName, enabled, userRoleID, tutor) VALUES (4,'richard.gault@qub.ac.uk','$2a$10$zKCpaI0Y0y5m8VMdsnUaheVrMtXy2SFzjGbwiDab3eunA5cY4FEvu','Richard','Gault',0,1,1);

INSERT INTO PasswordReset (userID, resetString) VALUES (1,'reset');
INSERT INTO PasswordReset (userID, resetString) VALUES (2,'oScUl5fFfTT9EYz');
INSERT INTO PasswordReset (userID, resetString) VALUES (3,'DZU5FdrngkzpBOH');
INSERT INTO PasswordReset (userID, resetString) VALUES (4,'fobtTPnQuTICp8o');

INSERT INTO Modules (moduleID, moduleName, moduleDescription, tutorUserID, commencementDate, endDate, approved) VALUES (1,'Example Module','Tester module for production build.',1,'2019-03-20','2020-03-20',1);

INSERT INTO ModuleAssociation (associationID, moduleID, userID, associationType) VALUES (1,1,1,1);
INSERT INTO ModuleAssociation (associationID, moduleID, userID, associationType) VALUES (2,1,2,2);
INSERT INTO ModuleAssociation (associationID, moduleID, userID, associationType) VALUES (5,1,3,3);

INSERT INTO QuestionType (questionTypeID, questionType) VALUES (1,'Text-based');
INSERT INTO QuestionType (questionTypeID, questionType) VALUES (2,'Multiple-Choice');
INSERT INTO QuestionType (questionTypeID, questionType) VALUES (3,'Insert the word');
INSERT INTO QuestionType (questionTypeID, questionType) VALUES (4,'Math/Text');

INSERT INTO Tests (testID, moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades, practice) VALUES (9,1,'Marked Test Example','2019-04-02 00:00:00','2019-04-02 14:50:00',1,1,1,0);
INSERT INTO Tests (testID, moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades, practice) VALUES (18,1,'Active Test Example','2019-04-26 00:00:00','2019-06-27 00:00:00',0,1,0,0);
INSERT INTO Tests (testID, moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades, practice) VALUES (24,1,'Practice Test Example','2019-04-26 00:00:00','2019-06-27 00:00:00',0,1,1,1);
INSERT INTO Tests (testID, moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades, practice) VALUES (25,1,'Scheduled Test Example','2019-06-27 00:00:00','2019-06-30 00:00:00',0,1,0,0);

INSERT INTO Question (questionType, questionID, questionContent, questionFigure, maxScore, minScore, creatorID, allThatApply) VALUES (1,1,'Which ‘BBT’ was first formulated by George Lemaitre and suggests that the universe formed billions of years ago and has been expanding ever since?',NULL,10,0,1,NULL);
INSERT INTO Question (questionType, questionID, questionContent, questionFigure, maxScore, minScore, creatorID, allThatApply) VALUES (2,2,'Triton is the largest of the satellites orbiting which of the traditional solar system planets?',NULL,10,0,1,0);
INSERT INTO Question (questionType, questionID, questionContent, questionFigure, maxScore, minScore, creatorID, allThatApply) VALUES (3,3,'A water molecule is composed of two [[hydrogen]] atoms and one [[oxygen]] atom.',NULL,10,0,1,NULL);
INSERT INTO Question (questionType, questionID, questionContent, questionFigure, maxScore, minScore, creatorID, allThatApply) VALUES (4,4,'Solve the following simultaneous equations.',NULL,10,0,1,NULL);

INSERT INTO QuestionMathLines (questionMathLineID, questionID, content, indexedAt) VALUES (1,4,'y + 2x = 15',0);
INSERT INTO QuestionMathLines (questionMathLineID, questionID, content, indexedAt) VALUES (2,4,'y = 2x + 3',1);

INSERT INTO CorrectPoint (correctPointID, questionID, phrase, marksWorth, feedback, indexedAt, math) VALUES (1,1,'Big Bang Theory',10,'Yes the big bang theory',NULL,0);
INSERT INTO CorrectPoint (correctPointID, questionID, phrase, marksWorth, feedback, indexedAt, math) VALUES (2,3,'hydrogen',5,'Yes 2 hydrogen',0,0);
INSERT INTO CorrectPoint (correctPointID, questionID, phrase, marksWorth, feedback, indexedAt, math) VALUES (3,3,'oxygen',5,'Yes one oxygen',1,0);
INSERT INTO CorrectPoint (correctPointID, questionID, phrase, marksWorth, feedback, indexedAt, math) VALUES (4,4,'x=3',5,'Yes x is 3.',NULL,1);
INSERT INTO CorrectPoint (correctPointID, questionID, phrase, marksWorth, feedback, indexedAt, math) VALUES (5,4,'y=9',5,'Yes y is 9.',NULL,1);

INSERT INTO Alternative (alternativeID, correctPointID, alternativePhrase, math) VALUES (1,1,'Big Bang',0);
INSERT INTO Alternative (alternativeID, correctPointID, alternativePhrase, math) VALUES (2,4,'x = 3',1);
INSERT INTO Alternative (alternativeID, correctPointID, alternativePhrase, math) VALUES (3,5,'y = 9',1);

INSERT INTO OptionTbl (optionID, questionID, optionContent, worthMarks, feedback) VALUES (1,2,'Mars',0,'Not Mars - Neptune.');
INSERT INTO OptionTbl (optionID, questionID, optionContent, worthMarks, feedback) VALUES (2,2,'Saturn',0,'Not Saturn - Neptune.');
INSERT INTO OptionTbl (optionID, questionID, optionContent, worthMarks, feedback) VALUES (3,2,'Neptune',10,'Yes Neptune.');

INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (18,9,2);
INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (19,9,1);
INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (20,9,3);
INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (21,9,4);
INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (61,18,1);
INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (62,18,2);
INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (63,18,3);
INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (64,18,4);
INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (65,24,1);
INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (66,24,2);
INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (67,24,3);
INSERT INTO TestQuestion (testQuestionID, testID, questionID) VALUES (68,24,4);


INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (26,2,9,1,3,'',0,'Not Saturn - Neptune.\n',1,0);
INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (27,1,9,1,3,'bbt',0,'',1,0);
INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (28,3,9,1,3,'',10,'Yes 2 hydrogen\nYes one oxygen\n',1,0);
INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (29,4,9,1,3,'',10,'Yes x is 3.\nYes y is 9.\n',1,0);
INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (30,2,9,2,1,'',10,'Yes Neptune.\n',1,0);
INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (31,1,9,2,1,'The Big Bang Theory',10,'Yes the big bang theory\n',1,0);
INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (32,3,9,2,1,'',10,'Yes 2 hydrogen\nYes one oxygen\n',1,0);
INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (33,4,9,2,1,'',10,'Yes x is 3.\nYes y is 9.\n',1,0);
INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (34,2,9,3,1,'',0,'Not Saturn - Neptune.\n',1,0);
INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (35,1,9,3,1,'bbt',0,'',1,0);
INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (36,3,9,3,1,'',0,'',1,1);
INSERT INTO Answer (answerID, questionID, testID, answererID, markerID, content, score, feedback, markerApproved, tutorApproved) VALUES (37,4,9,3,1,'',0,'',1,1);

INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (25,'hydrogen',0,28,0);
INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (26,'oxygen',1,28,0);
INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (27,'x=3',0,29,1);
INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (28,'y=9',1,29,1);
INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (29,'hydrogen',0,32,0);
INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (30,'oxygen',1,32,0);
INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (31,'x=3',0,33,1);
INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (32,'y=9',1,33,1);
INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (33,'oxy',0,36,0);
INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (34,'hydro',1,36,0);
INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (35,'x=12',0,37,1);
INSERT INTO Inputs (inputID, inputValue, inputIndex, answerID, math) VALUES (36,'y=1',1,37,1);

INSERT INTO OptionEntries (optionEntryID, optionID, answerID) VALUES (7,2,26);
INSERT INTO OptionEntries (optionEntryID, optionID, answerID) VALUES (8,3,30);
INSERT INTO OptionEntries (optionEntryID, optionID, answerID) VALUES (9,2,34);

INSERT INTO TestResult (testResultID, testID, studentID, testScore) VALUES (5,9,1,20);
INSERT INTO TestResult (testResultID, testID, studentID, testScore) VALUES (6,9,2,40);
INSERT INTO TestResult (testResultID, testID, studentID, testScore) VALUES (7,9,3,0);

INSERT INTO TutorRequests (tutorRequestID, userID, reason, approved) VALUES (1,1,'I want to be a tutor',1);
INSERT INTO TutorRequests (tutorRequestID, userID, reason, approved) VALUES (2,4,'RMG: testing tutor request functionality',1);

INSERT INTO UserSessions (username, token, lastActive) VALUES ('richard.gault@qub.ac.uk','cmljaGFyZC5nYXVsdEBxdWIuYWMudWs6UGF1bEdhdWx0MQ==','2019-04-18 10:05:33');
INSERT INTO UserSessions (username, token, lastActive) VALUES ('student@qub.ac.uk','c3R1ZGVudEBxdWIuYWMudWs6c3R1ZGVudFVzZXIx','2019-04-26 23:02:01');
INSERT INTO UserSessions (username, token, lastActive) VALUES ('tutor@qub.ac.uk','dHV0b3JAcXViLmFjLnVrOnR1dG9yVXNlcjE=','2019-04-26 22:58:21');
INSERT INTO UserSessions (username, token, lastActive) VALUES ('t_a@qub.ac.uk','dF9hQHF1Yi5hYy51azphc3Npc3RhbnRVc2VyMQ==','2019-04-26 23:24:27');