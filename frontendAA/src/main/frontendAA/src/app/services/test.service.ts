import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AppComponent} from "../app.component";
import {ModuleFE, QuestionType, Tests} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class TestService {

  constructor(private http: HttpClient, private app: AppComponent) {
  }

  public addTest(test: Tests): Observable<Tests> {
    let headers = new HttpHeaders({
      'Accept': 'application/json',
      'Authorization': 'Basic ' + localStorage.getItem("creds")
    });
    return this.http.post<Tests>(this.app.url + "/tests/addTest", test, {headers: headers}).pipe(
      tap((test: Tests) => console.log(`Added test with id=${test.testID}`))
    );
  }

  getByTestID(testID): Observable<Tests> {
    let headers = new HttpHeaders({
      'Accept': 'application/json',
      'Authorization': 'Basic ' + localStorage.getItem("creds")
    });
    console.log(headers);
    return this.http.get<Tests>(this.app.url + "/tests/getByTestIDTutorView?testID=" + testID, {headers: headers})
      .pipe(
        tap(_ => console.log('Test fetched from server.'))
      );
  }

  getQuestionTypes(): Observable<QuestionType[]> {
    let headers = new HttpHeaders({
      'Accept': 'application/json',
      'Authorization': 'Basic ' + localStorage.getItem("creds")
    });
    console.log(headers);
    return this.http.get<QuestionType[]>(this.app.url + "/tests/getQuestionTypes", {headers: headers})
      .pipe(
        tap(_ => console.log('Question types fetched from server.'))
      );
  }
}
