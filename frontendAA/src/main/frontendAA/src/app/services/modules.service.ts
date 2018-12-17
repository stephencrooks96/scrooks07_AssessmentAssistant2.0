import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ModuleFE, ModuleWithTutorFE, Performance, TestAndGrade, TestMarking, Tests} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";
import {AppComponent} from "../app.component";


@Injectable({
  providedIn: 'root'
})
export class ModulesService {

  constructor(private http: HttpClient, private app: AppComponent) {
  }

  getMyModulesWithTutors(): Observable<ModuleWithTutorFE[]> {

    return this.http.get<ModuleWithTutorFE[]>(this.app.url + "/modules/getMyModulesWithTutors", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Modules fetched from server.'))
      );
  }


  getPerformance(moduleID): Observable<Performance[]> {

    return this.http.get<Performance[]>(this.app.url + "/modules/getPerformance?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Performance fetched from server.'))
      );
  }

  getModuleAndTutor(moduleID): Observable<ModuleWithTutorFE> {

    return this.http.get<ModuleWithTutorFE>("http://localhost:8080/modules/getModuleAndTutor?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Modules fetched from server.'))
      );
  }

  getModuleAssociation(moduleID): Observable<string> {

    return this.http.get<string>("http://localhost:8080/modules/getModuleAssociation?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Module association fetched from server.'))
      );
  }

  getActiveTests(moduleID): Observable<Tests[]> {

    return this.http.get<Tests[]>("http://localhost:8080/modules/getActiveTests?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Active tests fetched from server.'))
      );
  }

  getActiveResults(moduleID): Observable<TestAndGrade[]> {

    return this.http.get<TestAndGrade[]>("http://localhost:8080/modules/getActiveResults?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Active results fetched from server.'))
      );
  }

  getScheduledTests(moduleID): Observable<Tests[]> {

    return this.http.get<Tests[]>("http://localhost:8080/modules/getScheduledTests?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Scheduled tests fetched from server.'))
      );
  }

  getTestDrafts(moduleID): Observable<Tests[]> {

    return this.http.get<Tests[]>("http://localhost:8080/modules/getTestDrafts?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Test drafts fetched from server.'))
      );
  }

  getReviewMarking(moduleID): Observable<TestMarking[]> {

    return this.http.get<TestMarking[]>("http://localhost:8080/modules/getReviewMarking?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Review Marking fetched from server.'))
      );
  }

  getMarking(moduleID): Observable<TestMarking[]> {

    return this.http.get<TestMarking[]>("http://localhost:8080/modules/getMarking?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Marking fetched from server.'))
      );
  }

  getModuleByID(moduleID): Observable<ModuleFE> {

    return this.http.get<ModuleFE>("http://localhost:8080/modules/getByModuleID?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Modules fetched from server.'))
      );
  }


}
