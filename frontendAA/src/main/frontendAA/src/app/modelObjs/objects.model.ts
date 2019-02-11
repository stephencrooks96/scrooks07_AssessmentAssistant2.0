export class ResultChartPojo {
  labels : string[];
  scores : number[];
  classAverage : number;
  colors : string[];
}

export class ChangePassword {
  password : string;
  newPassword : string;
  repeatPassword : string;
}

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
  publishGrades : number;
  publishResults : number;
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
  questionFigure : Blob;
  maxScore: number;
  creatorID: number;
  allThatApply : number;

}

export class MarkerAndReassigned {

  markerID : number;
  previousMarkerID : number;
  specifyQuestion : number;
  numberToReassign : number;
}

export class Answer {

  answerID : number;
  questionID : number;
  answererID : number;
  markerID : number;
  testID : number;
  content : string="";
  score : number;
  feedback : string="";
  markerApproved : number;
  tutorApproved : number;

}

export class Inputs {
  inputValue : string="";
  inputIndex : number;
  answerID : number;
}

export class OptionEntries {
  optionEntryID : number;
  optionID : number;
  answerID : number;
}

export class QuestionAndAnswer {
  question = new QuestionAndBase64();
  answer = new Answer();
  inputs : Inputs[];
  optionEntries : OptionEntries[];
}

export class QuestionAndBase64 {
  question = new Question();
  options : Option[];
  base64 : string="";
}

export class Input {
  value : string="";
}

export class AnswerData {
  questionAndAnswer = new QuestionAndAnswer();
  student = new User();
  correctPoints : CorrectPoint[];
}

export class TestMarking {

  test = new Tests();
  toBeMarkedByYou: number=0;
  toBeMarkedByTAs: number=0;
  marked: number=0;
  totalForYou: number=0;
  totalForTAs: number=0;

}

export class Marker {

  test = new Tests();
  marker = new User();
  markerType: string;
  scripts: Answer[];
  marked: number;
  unmarked: number;

}

export class MarkerWithChart {
  markers : Marker[];
  labels : [];
  data : [];
  colours : [];
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
  base64;
}

export class Option {
  optionID: number;
  questionID: number;
  optionContent: string="";
  worthMarks: number;
  feedback: string="";
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


