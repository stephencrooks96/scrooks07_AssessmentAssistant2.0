import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AppComponent} from "../app.component";
import {
  Alternative,
  CorrectPoint,
  Option,
  Question, QuestionAndAnswer, QuestionAndBase64,
  QuestionType,
  Tests,
  TutorQuestionPojo, Performance
} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";


@Injectable({
  providedIn: 'root'
})
export class TestService {

  constructor(private http: HttpClient, private app: AppComponent) {
  }

  /**
   *
   * @param test
   */
  public addTest(test: Tests): Observable<Tests> {

    return this.http.post<Tests>(this.app.url + "/tests/addTest", test, {headers: this.app.headers}).pipe(
      tap((test: Tests) => console.log(`Added test with id=${test.testID}`))
    );
  }

  /**
   *
   * @param test
   */
  public editTest(test: Tests): Observable<Tests> {

    return this.http.post<Tests>(this.app.url + "/tests/editTest", test, {headers: this.app.headers}).pipe(
      tap((test: Tests) => console.log(`Added test with id=${test.testID}`))
    );
  }

  /**
   *
   * @param questionData
   */
  public editQuestion(questionData: TutorQuestionPojo): Observable<TutorQuestionPojo> {

    return this.http.post<TutorQuestionPojo>(this.app.url + "/tests/editQuestion", questionData, {headers: this.app.headers}).pipe(
      tap((question: TutorQuestionPojo) => console.log(`Edited question with id=${question.question.questionID}`))
    );
  }

  /**
   *
   * @param questionData
   */
  public addQuestion(questionData: TutorQuestionPojo): Observable<TutorQuestionPojo> {

    return this.http.post<TutorQuestionPojo>(this.app.url + "/tests/addQuestion", questionData, {headers: this.app.headers}).pipe(
      tap((question: TutorQuestionPojo) => console.log(`Added question with id=${question.question.questionID}`))
    );
  }

  public submitTest(script: QuestionAndAnswer[]): Observable<number> {

    return this.http.post<number>(this.app.url + "/tests/submitTest", script, {headers: this.app.headers}).pipe(
      tap((testID: number) => console.log(`Added script for test #${testID}.`))
    );
  }

  /**
   *
   * @param questionID
   * @param testID
   */
  public addExistingQuestion(questionID, testID): Observable<Question> {
    return this.http.get<Question>(this.app.url + "/tests/addExistingQuestion?questionID=" + questionID + "&testID=" + testID, {headers: this.app.headers}).pipe(
      tap((question: Question) => console.log(`Added question with id=${question.questionID} to test with id=${testID}`))
    );
  }

  public getPerformance(testID): Observable<Performance> {
    return this.http.get<Performance>(this.app.url + "/tests/getPerformance?testID=" + testID, {headers: this.app.headers}).pipe(
      tap(() => console.log(`Performance retrieved for test with with id=${testID}`))
    );
  }

  public getFeedback(testID): Observable<Performance> {
    return this.http.get<Performance>(this.app.url + "/tests/getFeedback?testID=" + testID, {headers: this.app.headers}).pipe(
      tap(() => console.log(`Feedback retrieved for test with with id=${testID}`))
    );
  }

  public getAnsweredTests(): Observable<number[]> {
    return this.http.get<number[]>(this.app.url + "/tests/getAnsweredTests", {headers: this.app.headers}).pipe(
      tap((answeredTests: number[]) => console.log(`Returned users answered tests size ${answeredTests.length}`))
    );
  }

  public duplicateQuestion(questionID): Observable<Question> {
    return this.http.get<Question>(this.app.url + "/tests/duplicateQuestion?questionID=" + questionID, {headers: this.app.headers}).pipe(
      tap((question: Question) => console.log(`Duplicated question with id=${question.questionID}`))
    );
  }

  /**
   *
   * @param testID
   */
  getByTestID(testID): Observable<Tests> {

    return this.http.get<Tests>(this.app.url + "/tests/getByTestID?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Test fetched from server.'))
      );
  }

  /**
   *
   * @param testID
   */
  getQuestions(testID): Observable<TutorQuestionPojo[]> {

    return this.http.get<TutorQuestionPojo[]>(this.app.url + "/tests/getQuestionsByTestIDTutorView?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Questions fetched from server.'))
      );
  }

  /**
   *
   * @param testID
   */
  getQuestionsStudent(testID): Observable<QuestionAndAnswer[]> {

    return this.http.get<QuestionAndAnswer[]>(this.app.url + "/tests/getQuestionsStudent?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Questions fetched from server.'))
      );
  }

  /**
   *
   * @param testID
   */
  getOldQuestions(testID): Observable<TutorQuestionPojo[]> {

    return this.http.get<TutorQuestionPojo[]>(this.app.url + "/tests/getOldQuestions?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Old Questions fetched from server.'))
      );
  }

  /**
   *
   */
  getQuestionTypes(): Observable<QuestionType[]> {

    return this.http.get<QuestionType[]>(this.app.url + "/tests/getQuestionTypes", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Question types fetched from server.'))
      );
  }

  scheduleTest(testID): Observable<any> {
    return this.http.get<any>(this.app.url + "/tests/scheduleTest?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Test with id #${testID} scheduled for release.`))
      );
  }


  /**
   *
   * @param questionID
   * @param testID
   */
  removeQuestion(questionID: number, testID: number): Observable<any> {
    return this.http.delete<any>(this.app.url + "/tests/removeQuestionFromTest?testID=" + testID + "&questionID=" + questionID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Question id=${questionID} removed from test id=${testID}`))
      );
  }


  removeAlternative(alternativeID): Observable<any> {
    return this.http.delete<any>(this.app.url + "/tests/removeAlternative?alternativeID=" + alternativeID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Alternative id=${alternativeID} removed.`))
      );
  }

  removeCorrectPoint(correctPointID): Observable<any> {
    return this.http.delete<any>(this.app.url + "/tests/removeCorrectPoint?correctPointID=" + correctPointID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`CorrectPoint id=${correctPointID} removed.`))
      );
  }

  removeOption(option: Option): Observable<any> {
    return this.http.delete<any>(this.app.url + "/tests/removeOption?optionID=" + option.optionID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Option id=${option.optionID} removed.`))
      );
  }

  /**
   *
   * @param testID
   */
  deleteTest(testID: number): Observable<any> {
    return this.http.delete<any>(this.app.url + "/tests/deleteTest?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Test with id=${testID} deleted.`))
      );
  }
}
