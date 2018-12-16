DROP TABLE IF EXISTS Alternative, Answer, AssociationType, Message, Module, ModuleAssociation, Password, Question, QuestionType, TestQuestion, TestResutlt, Tests, TimeModifier, User, UserRole, UserSessions;

CREATE TABLE IF NOT EXISTS Alternative
(
  alternativeID int
(
  11
) NOT NULL AUTO_INCREMENT,
  correctPointID int
(
  11
) NOT NULL,
  alternativePhrase text NOT NULL
  );

CREATE TABLE IF NOT EXISTS Answer
(
  answerID int
(
  11
) NOT NULL AUTO_INCREMENT,
  questionID int
(
  11
) NOT NULL,
  testID int
(
  11
) NOT NUll,
  answererID int
(
  11
) NOT NULL,
  markerID int
(
  11
) NOT NULL,
  content text NOT NULL,
  score int
(
  11
)
  );

-- --------------------------------------------------------

--
-- Table structure for table AssociationType
--

CREATE TABLE IF NOT EXISTS AssociationType
(
  associationTypeID int
(
  11
) NOT NULL AUTO_INCREMENT,
  associationType varchar
(
  30
) NOT NULL
  );

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS CorrectPoint
(
  correctPointID int
(
  11
) NOT NULL AUTO_INCREMENT,
  questionID int
(
  11
) NOT NULL,
  phrase text NOT NULL,
  marksWorth double NOT NULL,
  feedback text NOT NULL
  );

--
-- Table structure for table Message
--

CREATE TABLE IF NOT EXISTS Message
(
  messageID int
(
  11
) NOT NULL AUTO_INCREMENT,
  content text NOT NULL,
  recipientID int
(
  11
) NOT NULL,
  senderID int
(
  11
) NOT NULL,
  messageTimestamp timestamp
(
  3
) NOT NULL,
  newMessage tinyint
(
  1
) NOT NULL
  );

-- --------------------------------------------------------

--
-- Table structure for table Module
--

CREATE TABLE IF NOT EXISTS Module
(
  moduleID int
(
  11
) NOT NULL AUTO_INCREMENT,
  moduleName varchar
(
  50
) NOT NULL,
  moduleDescription varchar
(
  500
) NOT NULL,
  tutorUserID int
(
  11
) NOT NULL,
  year year
(
  4
) NOT NULL
  );

-- --------------------------------------------------------

--
-- Table structure for table ModuleAssociation
--

CREATE TABLE IF NOT EXISTS ModuleAssociation
(
  associationID int
(
  11
) NOT NULL AUTO_INCREMENT,
  moduleID int
(
  11
) NOT NULL,
  userID int
(
  11
) NOT NULL,
  associationType int
(
  11
) NOT NULL
  );

--
-- Table structure for table Options
--

CREATE TABLE IF NOT EXISTS OptionTbl
(
  optionID int
(
  11
) NOT NULL AUTO_INCREMENT,
  questionID int
(
  11
) NOT NULL,
  optionContent text NOT NULL,
  correct tinyint
(
  1
) NOT NULL
  );

-- --------------------------------------------------------

--
-- Table structure for table Password
--

CREATE TABLE IF NOT EXISTS Password
(
  userID int
(
  11
) NOT NULL,
  resetString varchar
(
  10
) NOT NULL
  );

-- --------------------------------------------------------

--
-- Table structure for table Question
--

CREATE TABLE IF NOT EXISTS Question
(
  questionType int
(
  11
) NOT NULL,
  questionID int
(
  11
) NOT NULL AUTO_INCREMENT,
  questionContent text NOT NULL,
  questionFigure varchar
(
  255
),
  maxScore int
(
  11
) NOT NULL,
  creatorID int
(
  11
) NOT NULL
  );

-- --------------------------------------------------------

--
-- Table structure for table QuestionType
--

CREATE TABLE IF NOT EXISTS QuestionType
(
  questionTypeID int
(
  11
) NOT NULL AUTO_INCREMENT,
  questionType varchar
(
  255
) NOT NULL
  );

--
-- Table structure for table TestQuestion
--

CREATE TABLE IF NOT EXISTS TestQuestion
(
  testQuestionID int
(
  11
) NOT NULL AUTO_INCREMENT,
  testID int
(
  11
) NOT NULL,
  questionID int
(
  11
) NOT NULL
  );

-- --------------------------------------------------------

--
-- Table structure for table TestResult
--

CREATE TABLE IF NOT EXISTS TestResult
(
  testResultID int
(
  11
) NOT NULL AUTO_INCREMENT,
  testID int
(
  11
) NOT NULL,
  studentID int
(
  11
) NOT NULL,
  testScore int
(
  11
) NOT NULL
  );

-- --------------------------------------------------------

--
-- Table structure for table Tests
--

CREATE TABLE IF NOT EXISTS Tests
(
  testID int
(
  11
) NOT NULL AUTO_INCREMENT,
  moduleID int
(
  11
) NOT NULL,
  testTitle varchar
(
  50
) NOT NULL,
  startDateTime timestamp NOT NULL,
  endDateTime timestamp NOT NULL,
  publishResults tinyint
(
  1
) NOT NULL,
  scheduled tinyint
(
  1
) NOT NULL,
  publishGrades tinyint
(
  1
) NOT NULL,
  );

-- --------------------------------------------------------

--
-- Table structure for table TimeModifier
--

CREATE TABLE IF NOT EXISTS TimeModifier
(
  userID int
(
  11
) NOT NULL,
  timeModifier double NOT NULL
  );

-- --------------------------------------------------------

--
-- Table structure for table User
--

CREATE TABLE IF NOT EXISTS User
(
  userID int
(
  11
) NOT NULL AUTO_INCREMENT,
  username varchar
(
  50
) NOT NULL,
  password varchar
(
  200
) NOT NULL,
  firstName varchar
(
  30
) NOT NULL,
  lastName varchar
(
  30
) NOT NULL,
  enabled tinyint
(
  1
) NOT NULL,
  userRoleID int
(
  11
) NOT NULL
  );

-- --------------------------------------------------------

--
-- Table structure for table UserRole
--

CREATE TABLE IF NOT EXISTS UserRole
(
  userRoleID int
(
  11
) NOT NULL AUTO_INCREMENT,
  role varchar
(
  50
) NOT NULL
  );

-- --------------------------------------------------------

--
-- Table structure for table UserSessions
--

-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS UserSessions
(

  username VARCHAR
(
  64
) NOT NULL,
  sessionID VARCHAR
(
  64
) NOT NULL,
  tokenID VARCHAR
(
  64
) NOT NULL,
  lastActive TIMESTAMP NOT NULL,

  );

--
-- Indexes for dumped tables
--

CREATE
PRIMARY
KEY
IF
NOT
EXISTS
alternativeID
ON
Alternative
(
alternativeID
);
CREATE
INDEX
IF
NOT
EXISTS
correctPointIDAlt
ON
Alternative
(
correctPointID
);

--
-- Indexes for table Answer
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
answerID
ON
Answer
(
answerID
);
CREATE
INDEX
IF
NOT
EXISTS
questionIDAns
ON
Answer
(
questionID
);
CREATE
INDEX
IF
NOT
EXISTS
markerID
ON
Answer
(
markerID
);
CREATE
INDEX
IF
NOT
EXISTS
testID
ON
Answer
(
testID
);

--
-- Indexes for table AssociationType
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
associationTypeID
ON
AssociationType
(
associationTypeID
);

CREATE
PRIMARY
KEY
IF
NOT
EXISTS
correctPointID
ON
CorrectPoint
(
correctPointID
);
CREATE
INDEX
IF
NOT
EXISTS
questionIDCorrect
ON
CorrectPoint
(
questionID
);

--
-- Indexes for table Message
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
messageID
ON
Message
(
messageID
);
CREATE
INDEX
IF
NOT
EXISTS
recipientID
ON
Message
(
recipientID
);
CREATE
INDEX
IF
NOT
EXISTS
senderID
ON
Message
(
senderID
);

--
-- Indexes for table Module
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
moduleID
ON
Module
(
moduleID
);
CREATE
INDEX
IF
NOT
EXISTS
tutorUserID
ON
Module
(
tutorUserID
);

--
-- Indexes for table ModuleAssociation
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
associationID
ON
ModuleAssociation
(
associationID
);
CREATE
INDEX
IF
NOT
EXISTS
moduleIDAssoc
ON
ModuleAssociation
(
moduleID
);
CREATE
INDEX
IF
NOT
EXISTS
userIDAssoc
ON
ModuleAssociation
(
userID
);
CREATE
INDEX
IF
NOT
EXISTS
associationType
ON
ModuleAssociation
(
associationType
);

--
-- Indexes for table Option
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
optionID
ON
OptionsTbl
(
optionID
);
CREATE
INDEX
IF
NOT
EXISTS
questionIDOpt
ON
OptionTbl
(
questioID
);


--
-- Indexes for table Password
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
userIDPass
ON
Password
(
userID
);

--
-- Indexes for table Question
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
questionID
ON
Question
(
questionID
);
CREATE
INDEX
IF
NOT
EXISTS
questionType
ON
Question
(
questionType
);
CREATE
INDEX
IF
NOT
EXISTS
creatorID
ON
Question
(
creatorID
);

--
-- Indexes for table QuestionType
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
questionTypeID
ON
QuestionType
(
questionTypeID
);

--
-- Indexes for table TestQuestion
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
testQuestionID
ON
TestQuestion
(
testQuestionID
);
CREATE
INDEX
IF
NOT
EXISTS
testIDTestQ
ON
TestQuestion
(
testID
);
CREATE
INDEX
IF
NOT
EXISTS
questionIDTestQ
ON
TestQuestion
(
questionID
);

--
-- Indexes for table TestResult
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
testResultID
ON
TestResult
(
testResultID
);
CREATE
INDEX
IF
NOT
EXISTS
studentID
ON
TestResult
(
studentID
);
CREATE
INDEX
IF
NOT
EXISTS
testIDTestRes
ON
TestResult
(
testID
);

--
-- Indexes for table Tests
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
testID
ON
Tests
(
testID
);
CREATE
INDEX
IF
NOT
EXISTS
moduleIDTests
ON
Tests
(
moduleID
);

--
-- Indexes for table TimeModifier
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
userIDTim
ON
TimeModifier
(
userID
);

--
-- Indexes for table User
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
userID
ON
User
(
userID
);
CREATE
UNIQUE
INDEX
IF
NOT
EXISTS
username
ON
User
(
username
);
CREATE
INDEX
IF
NOT
EXISTS
userRoleID
ON
User
(
userRoleID
);

--
-- Indexes for table UserRole
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
userRoleID
ON
UserRole
(
userRoleID
);

--
-- Indexes for table UserSessions
--
CREATE
PRIMARY
KEY
IF
NOT
EXISTS
sessionID
ON
UserSessions
(
sessionID
);
--
-- Constraints for dumped tables
--

--
-- Constraints for table Answer
--
ALTER TABLE Answer
  ADD FOREIGN KEY (questionID) REFERENCES Question (questionID);
ALTER TABLE Answer
  ADD FOREIGN KEY (answererID) REFERENCES User (userID);
ALTER TABLE Answer
  ADD FOREIGN KEY (markerID) REFERENCES User (userID);
ALTER TABLE Answer
  ADD FOREIGN KEY (testID) REFERENCES Tests (testID);

--
-- Constraints for table Message
--

ALTER TABLE Message
  ADD FOREIGN KEY (recipientID) REFERENCES User (userID);
ALTER TABLE Message
  ADD FOREIGN KEY (senderID) REFERENCES User (userID);

--
-- Constraints for table Module
--
ALTER TABLE Module
  ADD FOREIGN KEY (tutorUserID) REFERENCES User (userID);

--
-- Constraints for table ModuleAssociation
--
ALTER TABLE ModuleAssociation
  ADD FOREIGN KEY (associationType) REFERENCES AssociationType (associationTypeID);
ALTER TABLE ModuleAssociation
  ADD FOREIGN KEY (moduleID) REFERENCES Module (moduleID);
ALTER TABLE ModuleAssociation
  ADD FOREIGN KEY (userID) REFERENCES User (userID);

--
-- Constraints for table OptionTbl
--
ALTER TABLE OptionTbl
  ADD FOREIGN KEY (questionID) REFERENCES Question (questionID);

--
-- Constraints for table Password
--
ALTER TABLE Password
  ADD FOREIGN KEY (userID) REFERENCES User (userID);

--
-- Constraints for table Question
--
ALTER TABLE Question
  ADD FOREIGN KEY (questionType) REFERENCES QuestionType (questionTypeID);
ALTER TABLE Question
  ADD FOREIGN KEY (creatorID) REFERENCES User (userID);

--
-- Constraints for table TestQuestion
--
ALTER TABLE TestQuestion
  ADD FOREIGN KEY (questionID) REFERENCES Question (questionID);
ALTER TABLE TestQuestion
  ADD FOREIGN KEY (testID) REFERENCES Tests (testID);

--
-- Constraints for table TestResult
--
ALTER TABLE TestResult
  ADD FOREIGN KEY (testID) REFERENCES Tests (testID);
ALTER TABLE TestResult
  ADD FOREIGN KEY (studentID) REFERENCES User (userID);

--
-- Constraints for table Tests
--
ALTER TABLE Tests
  ADD FOREIGN KEY (moduleID) REFERENCES Module (moduleID);

--
-- Constraints for table TimeModifier
--
ALTER TABLE TimeModifier
  ADD FOREIGN KEY (userID) REFERENCES User (userID);

--
-- Constraints for table User
--
ALTER TABLE User
  ADD FOREIGN KEY (userRoleID) REFERENCES UserRole (userRoleID);
