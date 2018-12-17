import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AppComponent} from "../app.component";
import {QuestionType, Tests, TutorQuestionPojo} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";


@Injectable({
  providedIn: 'root'
})
export class TestService {

  constructor(private http: HttpClient, private app: AppComponent) {
  }

  public addTest(test: Tests): Observable<Tests> {

    return this.http.post<Tests>(this.app.url + "/tests/addTest", test, {headers: this.app.headers}).pipe(
      tap((test: Tests) => console.log(`Added test with id=${test.testID}`))
    );
  }

  public addQuestion(questionData: TutorQuestionPojo): Observable<TutorQuestionPojo> {

    return this.http.post<TutorQuestionPojo>(this.app.url + "/tests/addQuestion", questionData, {headers: this.app.headers}).pipe(
      tap((question: TutorQuestionPojo) => console.log(`Added question with id=${question.question.questionID}`))
    );
  }

  getByTestID(testID): Observable<Tests> {

    return this.http.get<Tests>(this.app.url + "/tests/getByTestIDTutorView?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Test fetched from server.'))
      );
  }

  getQuestions(testID): Observable<TutorQuestionPojo[]> {

    return this.http.get<TutorQuestionPojo[]>(this.app.url + "/tests/getQuestionsByTestIDTutorView?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Questions fetched from server.'))
      );
  }

  getOldQuestions(testID): Observable<TutorQuestionPojo[]> {

    return this.http.get<TutorQuestionPojo[]>(this.app.url + "/tests/getOldQuestions?testID=" + testID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Old Questions fetched from server.'))
      );
  }

  getQuestionTypes(): Observable<QuestionType[]> {

    return this.http.get<QuestionType[]>(this.app.url + "/tests/getQuestionTypes", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Question types fetched from server.'))
      );
  }
}
