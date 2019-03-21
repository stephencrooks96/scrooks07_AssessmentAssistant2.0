import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppComponent} from "../app.component";
import {Observable} from "rxjs";
import {
  Alternative,
  Answer,
  AnswerData,
  CorrectPoint,
  MarkerAndReassigned,
  MarkerWithChart,
  ResultChartPojo
} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";
import {AuthorizationService} from "./authorization.service";

@Injectable({
  providedIn: 'root'
})
export class MarkingService {

  constructor(private http: HttpClient, private app: AppComponent, private authorization: AuthorizationService) {
  }

  /**
   * Gets the markers data from the backend server
   * @param testID
   */
  getMarkersData(testID): Observable<MarkerWithChart> {
    return this.http.get<MarkerWithChart>(this.app.url + "/marking/getMarkersData?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Markers fetched from server for test #${testID}.`))
      );
  }

  /**
   * Sends reassignment data to backend
   * @param reassignment
   * @param testID
   */
  reassignAnswers(reassignment: MarkerAndReassigned[], testID: number): Observable<any> {
    return this.http.post<any>(this.app.url + "/marking/reassignAnswers?testID=" + testID, reassignment, {headers: this.app.headers}).pipe(
      tap(_ => console.log("Reassigned"))
    );
  }

  /**
   * Sends manual score editing to backend
   * @param answer
   */
  public editScore(answer: Answer): Observable<Answer> {
    return this.http.post<Answer>(this.app.url + "/marking/editScore", answer, {headers: this.app.headers}).pipe(
      tap((answer: Answer) => console.log(`Edited answer with id=${answer.answerID}`))
    );
  }

  /**
   * Sends manual feedback editing to backend
   * @param answer
   */
  public editFeedback(answer: Answer): Observable<Answer> {
    return this.http.post<Answer>(this.app.url + "/marking/editFeedback", answer, {headers: this.app.headers}).pipe(
      tap((answer: Answer) => console.log(`Edited answer with id=${answer.answerID}`))
    );
  }

  /**
   * Sends manual answer editing to backend
   * @param answer
   */
  public editAnswer(answer: Answer): Observable<Answer> {
    return this.http.post<Answer>(this.app.url + "/marking/editAnswer", answer, {headers: this.app.headers}).pipe(
      tap((answer: Answer) => console.log(`Edited answer with id=${answer.answerID}`))
    );
  }

  /**
   * Sends new correct point data to backend
   * @param correctPoint
   * @param testID
   */
  public addCorrectPoint(correctPoint: CorrectPoint, testID): Observable<CorrectPoint> {
    return this.http.post<CorrectPoint>(this.app.url + "/marking/addCorrectPoint?testID=" + testID, correctPoint, {headers: this.app.headers}).pipe(
      tap((correctPoint: CorrectPoint) => console.log(`CorrectPoint added with id=${correctPoint.correctPointID}`))
    );
  }

  /**
   * Sends new alternative data to backend
   * @param alternative
   * @param testID
   */
  public addAlternative(alternative: Alternative, testID): Observable<Alternative> {
    return this.http.post<Alternative>(this.app.url + "/marking/addAlternative?testID=" + testID, alternative, {headers: this.app.headers}).pipe(
      tap((alternative: Alternative) => console.log(`Alternative added with id=${alternative.alternativeID}`))
    );
  }

  /**
   * Sends data on answer approval to backend
   * @param answerID
   */
  approve(answerID): Observable<any> {
    return this.http.get<any>(this.app.url + "/marking/approve?answerID=" + answerID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Answer #${answerID} approved.`))
      );
  }

  /**
   * Sends publish grade data to backend
   * @param testID
   */
  publishGrades(testID): Observable<any> {
    return this.http.get<any>(this.app.url + "/marking/publishGrades?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Scripts fetched from server for test #${testID}.`))
      );
  }

  /**
   * Sends publish result data to backend
   * @param testID
   */
  publishResults(testID): Observable<any> {
    return this.http.get<any>(this.app.url + "/marking/publishResults?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Scripts fetched from server for test #${testID}.`))
      );
  }

  /**
   * Retrives scripts for a certain marker in a certain test from backend
   * @param testID
   */
  getScriptsByTestIDMarker(testID): Observable<AnswerData[]> {
    return this.http.get<AnswerData[]>(this.app.url + "/marking/getScriptsMarker?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Scripts fetched from server for test #${testID}.`))
      );
  }

  /**
   * Gets all scripts for a test from backend
   * @param testID
   */
  getScriptsByTestIDTutor(testID): Observable<AnswerData[]> {
    return this.http.get<AnswerData[]>(this.app.url + "/marking/getScriptsTutor?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Scripts fetched from server for test #${testID}.`))
      );
  }

  /**
   * Gets all correct points for a question from backend
   * @param questionID
   * @param testID
   */
  getCorrectPoints(questionID, testID): Observable<CorrectPoint[]> {
    return this.http.get<CorrectPoint[]>(this.app.url + "/marking/getCorrectPoints?questionID=" + questionID + "&testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Correct Points fetched for #${questionID}.`))
      );
  }

  /**
   * Gets the result chart info from backend
   * @param testID
   */
  getResultChart(testID): Observable<ResultChartPojo> {
    return this.http.get<ResultChartPojo>(this.app.url + "/marking/getResultChart?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Results fetched for #${testID}.`))
      );
  }

  /**
   * Gets question result chart data from backend
   * @param testID
   */
  getQuestionResultChart(testID): Observable<ResultChartPojo[]> {
    return this.http.get<ResultChartPojo[]>(this.app.url + "/marking/getQuestionsResultChart?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Results fetched for #${testID}.`))
      );
  }

  /**
   * Sends information to remove an alternative to backend
   * @param alternativeID
   * @param testID
   */
  removeAlternative(alternativeID, testID): Observable<any> {
    return this.http.delete<any>(this.app.url + "/marking/removeAlternative?alternativeID=" + alternativeID + "&testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`Alternative id=${alternativeID} removed.`))
      );
  }

  /**
   * Sends remove correct point data to backend
   * @param correctPointID
   * @param testID
   */
  removeCorrectPoint(correctPointID, testID): Observable<any> {
    return this.http.delete<any>(this.app.url + "/marking/removeCorrectPoint?correctPointID=" + correctPointID + "&testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log(`CorrectPoint id=${correctPointID} removed.`))
      );
  }
}
