---
 
insert into UserRole (role)
values ('ROLE_ADMIN');
 
insert into UserRole (role)
values ('ROLE_USER');
 
---

insert into QuestionType (questionType) values ('Text-based');
insert into QuestionType (questionType) values ('Multiple-Choice');
insert into QuestionType (questionType) values ('Insert the word');

insert into Users (username, password, firstname, lastname, enabled, userRoleID)
values ('pgault04@qub.ac.uk', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'Paul', 'Gault', 1, 1);
 
insert into Users (username, password, firstname, lastname, enabled, userRoleID)
values ('richard.gault@qub.ac.uk', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'Richard', 'Gault', 1, 2);

insert into Users (username, password, firstname, lastname, enabled, userRoleID)
values ('bart.simpson@qub.ac.uk', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'Bart', 'Simpson', 1, 2);
 
insert into Modules (moduleName, moduleDescription, tutorUserID, year) values ('Foundation Physics', 'Physics for beginners', 2, 2018);
insert into Modules (moduleName, moduleDescription, tutorUserID, year) values ('Ad Physics', 'Advanced Physics', 2, 2018);

insert into AssociationType (associationType) values ('tutor');
insert into AssociationType (associationType) values ('student');
insert into AssociationType (associationType) values ('teaching assistant');

insert into ModuleAssociation (moduleID, userID, associationType) values (1, 1, 2);
insert into ModuleAssociation (moduleID, userID, associationType) values (2, 1, 2);
insert into ModuleAssociation (moduleID, userID, associationType) values (1, 2, 1);
insert into ModuleAssociation (moduleID, userID, associationType) values (2, 2, 1);
insert into ModuleAssociation (moduleID, userID, associationType) values (1, 3, 3);
insert into ModuleAssociation (moduleID, userID, associationType) values (2, 3, 3);

-- Active Tests --
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Quantum Physics', '2018-11-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Medical Physics', '2018-11-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Astrophysics', '2018-11-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);

-- Scheduled Tests --
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Quantum Physics 2', '2019-06-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Medical Physics 2', '2019-06-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Astrophysics 2', '2019-06-11 00:00:00', '2019-11-11 00:00:00', 0, 1, 0);

-- Draft Tests --
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Quantum Physics 3', '2019-01-11 00:00:00', '2019-11-11 00:00:00', 0, 0, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Medical Physics 3', '2019-01-11 00:00:00', '2019-11-11 00:00:00', 0, 0, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Astrophysics 3', '2019-01-11 00:00:00', '2019-11-11 00:00:00', 0, 0, 0);

-- Marking --
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Quantum Physics 3', '2018-12-5 12:00:00', '2018-12-5 13:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Medical Physics 3', '2018-12-5 12:00:00', '2018-12-5 13:00:00', 0, 1, 0);
insert into tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (1, 'Astrophysics 3', '2018-12-5 12:00:00', '2018-12-5 13:00:00', 0, 1, 0);

insert into question(questionType, questionContent, questionFigure, maxScore, creatorID) values (1, 'What is the name of the negatively charged particles in an atom?', null, 3, 2);
insert into question(questionType, questionContent, questionFigure, maxScore, creatorID) values (1, 'How much wood would a wood chuck chuck if a wood chuck could chuck wood?', null, 3, 2);
insert into question(questionType, questionContent, questionFigure, maxScore, creatorID) values (1, 'In Star Wars what color was Luke Skywalkers lightsaber?', null, 2, 2);

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