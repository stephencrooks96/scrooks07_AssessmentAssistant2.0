import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AppComponent} from "../app.component";
import {
  Option,
  Performance,
  Question,
  QuestionAndAnswer,
  QuestionType,
  Tests,
  TutorQuestionPojo
} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";
import {AuthorizationService} from "./authorization.service";

@Injectable({
  providedIn: 'root'
})
export class TestService {

  constructor(private http: HttpClient, private app: AppComponent, private authorization: AuthorizationService) {
  }

  /**
   * Sends new test info to backend
   * @param test
   */
  public addTest(test: Tests): Observable<Tests> {
    return this.http.post<Tests>(this.app.url + "/tests/addTest", test, {headers: this.app.headers}).pipe(
      tap((test: Tests) => console.log(`Added test with id=${test.testID}`))
    );
  }

  /**
   * Sends test editing info to backend
   * @param test
   */
  public editTest(test: Tests): Observable<Tests> {
    return this.http.post<Tests>(this.app.url + "/tests/editTest", test, {headers: this.app.headers}).pipe(
      tap((test: Tests) => console.log(`Added test with id=${test.testID}`))
    );
  }

  /**
   * Sends question editing information to backend
   * @param questionData
   */
  public editQuestion(questionData: TutorQuestionPojo): Observable<TutorQuestionPojo> {
    return this.http.post<TutorQuestionPojo>(this.app.url + "/tests/editQuestion", questionData, {headers: this.app.headers}).pipe(
      tap((question: TutorQuestionPojo) => console.log(`Edited question with id=${question.question.questionID}`))
    );
  }

  /**
   * Sends add question data to backend
   * @param questionData
   */
  public addQuestion(questionData: TutorQuestionPojo): Observable<TutorQuestionPojo> {
    return this.http.post<TutorQuestionPojo>(this.app.url + "/tests/addQuestion", questionData, {headers: this.app.headers}).pipe(
      tap((question: TutorQuestionPojo) => console.log(`Added question with id=${question.question.questionID}`))
    );
  }

  /**
   * Sends test submission data to backend
   * @param script
   */
  public submitTest(script: QuestionAndAnswer[]): Observable<number> {
    return this.http.post<number>(this.app.url + "/tests/submitTest", script, {headers: this.app.headers}).pipe(
      tap((testID: number) => console.log(`Added script for test #${testID}.`))
    );
  }

  /**
   * Sends request to add existing question to backend
   * @param questionID
   * @param testID
   */
  public addExistingQuestion(questionID, testID): Observable<Question> {
    return this.http.get<Question>(this.app.url + "/tests/addExistingQuestion?questionID=" + questionID + "&testID=" + testID, {headers: this.app.headers}).pipe(
      tap((question: Question) => console.log(`Added question with id=${question.questionID} to test with id=${testID}`))
    );
  }

  /**
   * Gets performance info from backend
   * @param testID
   */
  public getPerformance(testID): Observable<Performance> {
    return this.http.get<Performance>(this.app.url + "/tests/getPerformance?testID=" + testID, {headers: this.app.headers}).pipe(
      tap(() => console.log(`Performance retrieved for test with with id=${testID}`))
    );
  }

  /**
   * Gets feedback from backend
   * @param testID
   */
  public getFeedback(testID): Observable<Performance> {
    return this.http.get<Performance>(this.app.url + "/tests/getFeedback?testID=" + testID, {headers: this.app.headers}).pipe(
      tap(() => console.log(`Feedback retrieved for test with with id=${testID}`))
    );
  }

  /**
   * Gets answered tests from backend
   */
  public getAnsweredTests(): Observable<number[]> {
    return this.http.get<number[]>(this.app.url + "/tests/getAnsweredTests", {headers: this.app.headers}).pipe(
      tap((answeredTests: number[]) => console.log(`Returned users answered tests size ${answeredTests.length}`))
    );
  }

  /**
   * Sends request to duplicate question to backend
   * @param questionID
   */
  public duplicateQuestion(questionID): Observable<Question> {
    return this.http.get<Question>(this.app.url + "/tests/duplicateQuestion?questionID=" + questionID, {headers: this.app.headers}).pipe(
      tap((question: Question) => console.log(`Duplicated question with id=${question.questionID}`))
    );
  }

  /**
   * Gets info on a given test from backend
   * @param testID
   */
  getByTestID(testID): Observable<Tests> {
    return this.http.get<Tests>(this.app.url + "/tests/getByTestID?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Test fetched from server.'))
      );
  }

  /**
   * Gets questions for a given test from backend
   * @param testID
   */
  getQuestions(testID): Observable<TutorQuestionPojo[]> {
    return this.http.get<TutorQuestionPojo[]>(this.app.url + "/tests/getQuestionsByTestIDTutorView?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Questions fetched from server.'))
      );
  }

  /**
   * Gets questions for a test in form students can see
   * @param testID
   */
  getQuestionsStudent(testID): Observable<QuestionAndAnswer[]> {
    return this.http.get<QuestionAndAnswer[]>(this.app.url + "/tests/getQuestionsStudent?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Questions fetched from server.'))
      );
  }

  /**
   * Gets all questions for user that arent in current test from backend
   * @param testID
   */
  getOldQuestions(testID): Observable<TutorQuestionPojo[]> {
    return this.http.get<TutorQuestionPojo[]>(this.app.url + "/tests/getOldQuestions?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Old Questions fetched from server.'))
      );
  }

  /**
   * Gets all question type from backend
   */
  getQuestionTypes(): Observable<QuestionType[]> {
    return this.http.get<QuestionType[]>(this.app.url + "/tests/getQuestionTypes", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Question types fetched from server.'))
      );
  }

  /**
   * Sends info to schedule test to backend
   * @param testID
   */
  scheduleTest(testID): Observable<any> {
    return this.http.get<any>(this.app.url + "/tests/scheduleTest?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Test with id #${testID} scheduled for release.`))
      );
  }

  /**
   * Sends request to remove question to backend
   * @param questionID
   * @param testID
   */
  removeQuestion(questionID: number, testID: number): Observable<any> {
    return this.http.delete<any>(this.app.url + "/tests/removeQuestionFromTest?testID=" + testID + "&questionID=" + questionID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Question id=${questionID} removed from test id=${testID}`))
      );
  }

  /**
   * Sends request to remove alternative to backend
   * @param alternativeID
   */
  removeAlternative(alternativeID): Observable<any> {
    return this.http.delete<any>(this.app.url + "/tests/removeAlternative?alternativeID=" + alternativeID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Alternative id=${alternativeID} removed.`))
      );
  }

  /**
   * Sends request to remove correct point to backend
   * @param correctPointID
   */
  removeCorrectPoint(correctPointID): Observable<any> {
    return this.http.delete<any>(this.app.url + "/tests/removeCorrectPoint?correctPointID=" + correctPointID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`CorrectPoint id=${correctPointID} removed.`))
      );
  }

  /**
   * Sends request to remove math line to backend
   * @param questionMathLineID
   */
  removeQuestionMathLine(questionMathLineID): Observable<any> {
    return this.http.delete<any>(this.app.url + "/tests/removeQuestionMathLine?questionMathLineID=" + questionMathLineID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Question Math Line id=${questionMathLineID} removed.`))
      );
  }

  /**
   * Sends request to remove option to backend
   * @param option
   */
  removeOption(option: Option): Observable<any> {
    return this.http.delete<any>(this.app.url + "/tests/removeOption?optionID=" + option.optionID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Option id=${option.optionID} removed.`))
      );
  }

  /**
   * Sends request to delete test to backend
   * @param testID
   */
  deleteTest(testID: number): Observable<any> {
    return this.http.delete<any>(this.app.url + "/tests/deleteTest?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Test with id=${testID} deleted.`))
      );
  }
}
