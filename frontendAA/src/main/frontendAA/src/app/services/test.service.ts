import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AppComponent} from "../app.component";
import {Question, QuestionType, Tests, TutorQuestionPojo} from "../modelObjs/objects.model";
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
  public addQuestion(questionData: TutorQuestionPojo): Observable<TutorQuestionPojo> {

    return this.http.post<TutorQuestionPojo>(this.app.url + "/tests/addQuestion", questionData, {headers: this.app.headers}).pipe(
      tap((question: TutorQuestionPojo) => console.log(`Added question with id=${question.question.questionID}`))
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

  /**
   *
   * @param testID
   */
  getByTestID(testID): Observable<Tests> {

    return this.http.get<Tests>(this.app.url + "/tests/getByTestIDTutorView?testID=" + testID, {headers: this.app.headers})
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
