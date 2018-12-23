export class User {
  userID: number;
  username: string = "";
  password: string = "";
  firstName: string = "";
  lastName: string = "";

}

export class ModuleFE {

  moduleID: number;
  moduleName: string = "";
  moduleDescription: string = "";
  year: number;
  tutorUserID: number;

}

export class ModuleWithTutorFE {

  module = new ModuleFE();
  tutor = new User();

}

export class Tests {

  testID: number;
  testTitle: string ="";
  startDateTime = new Date();
  endDateTime = new Date();
  moduleID: number=0;
}

export class TestAndResultFE {

  test = new Tests();
  testResult = new TestResult();
  questions: Question[];
  answers: Answer[];
  percentageScore: number;

}

export class TestAndGrade {

  test = new Tests();
  grade: string="";

}


export class TestResult {

  testResultID: number;
  testID: number;
  studentID: number;
  testScore: number;

}

export class Question {

  questionType: number;
  questionID: number;
  questionContent: string = "";
  questionFigure: string = "";
  maxScore: number;
  modelAnswerID: number;
  creatorID: number;

}

export class Answer {

  answerID: number;
  questionID: number;
  answererID: number;
  markerID: number;
  testID: number;
  content: string = "";
  score: number;
  feedback: string = "";
  marksGainedFor: string = "";

}

export class TestMarking {

  test = new Tests();
  toBeMarkedByYou: number=0;
  toBeMarkedByTAs: number=0;
  marked: number=0;
  totalForYou: number=0;
  totalForTAs: number=0;

}

export class Performance {
  testAndResult = new TestAndResultFE();
  classAverage: number=0;
}

export class QuestionType {
  questionTypeID = new Number();
  questionType = new String();
}

export class TutorQuestionPojo {
  testID: number;
  question = new Question();
  options: Option[];
  correctPoints: CorrectPoint[];
}

export class Option {
  optionID: number;
  questionID: number;
  option: string="";
}

export class CorrectPoint {
  correctPointID: number;
  questionID: number;
  phrase: string="";
  marksWorth: number;
  feedback: string="";
  alternatives: Alternative[];
}

export class Alternative {
  alternativeID: number;
  correctPointID: number;
  alternativePhrase: string="";
}


