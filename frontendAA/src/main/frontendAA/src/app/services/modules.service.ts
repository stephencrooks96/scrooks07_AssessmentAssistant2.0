import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {
  AddModulePojo, Associate,
  ModuleFE, ModuleRequestPojo,
  ModuleWithTutorFE,
  Performance,
  TestAndGrade,
  TestMarking,
  Tests, TutorRequestPojo
} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";
import {AppComponent} from "../app.component";
import {AuthorizationService} from "./authorization.service";


@Injectable({
  providedIn: 'root'
})
export class ModulesService {

  constructor(private http: HttpClient, private app: AppComponent, private autorization : AuthorizationService) {
  }

  getPendingApproval() : Observable<ModuleFE[]> {
    return this.http.get<ModuleFE[]>(this.app.url + "/modules/getModulesPendingApproval", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Modules returned.'))
      );
  }

  addModule(modulePojo) : Observable<AddModulePojo> {
    return this.http.post<AddModulePojo>(this.app.url + "/modules/addModule", modulePojo, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Module added.'))
      );
  }

  addAssociations(moduleID, associations) : Observable<any> {
    return this.http.post<any>(this.app.url + "/modules/addAssociations?moduleID=" + moduleID, associations, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Module added.'))
      );
  }

  getModuleRequests(): Observable<ModuleRequestPojo[]> {
    return this.http.get<ModuleRequestPojo[]>(this.app.url + "/modules/getModuleRequests", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Module requests returned.'))
      );
  }

  removeAssociation(username, moduleID): Observable<Associate> {
    return this.http.get<Associate>(this.app.url + "/modules/removeAssociate?username=" + username + "&moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Associates returned.'))
      );
  }

  getAssociates(moduleID): Observable<Associate[]> {
    return this.http.get<Associate[]>(this.app.url + "/modules/getAssociates?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Associates returned.'))
      );
  }

  approveModuleRequest(moduleID): Observable<any> {
    return this.http.get<any>(this.app.url + "/modules/approveModuleRequest?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Module request approved.'))
      );
  }

  rejectModuleRequest(moduleID): Observable<any> {
    return this.http.get<any>(this.app.url + "/modules/rejectModuleRequest?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor request rejected.'))
      );
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

    return this.http.get<ModuleWithTutorFE>(this.app.url +  "/modules/getModuleAndTutor?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Modules fetched from server.'))
      );
  }

  getModuleAssociation(moduleID): Observable<number> {

    return this.http.get<number>(this.app.url +  "/modules/getModuleAssociation?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Module association fetched from server.'))
      );
  }

  getActiveTests(moduleID): Observable<Tests[]> {
    return this.http.get<Tests[]>(this.app.url + "/modules/getActiveTests?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Active tests fetched from server.'))
      );
  }

  getPracticeTests(moduleID): Observable<Tests[]> {
    return this.http.get<Tests[]>(this.app.url + "/modules/getPracticeTests?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Practice tests fetched from server.'))
      );
  }

  getActiveResults(moduleID): Observable<TestAndGrade[]> {

    return this.http.get<TestAndGrade[]>(this.app.url + "/modules/getActiveResults?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Active results fetched from server.'))
      );
  }

  getScheduledTests(moduleID): Observable<Tests[]> {

    return this.http.get<Tests[]>(this.app.url + "/modules/getScheduledTests?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Scheduled tests fetched from server.'))
      );
  }

  getTestDrafts(moduleID): Observable<Tests[]> {

    return this.http.get<Tests[]>(this.app.url + "/modules/getTestDrafts?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Test drafts fetched from server.'))
      );
  }

  getReviewMarking(moduleID): Observable<TestMarking[]> {

    return this.http.get<TestMarking[]>(this.app.url + "/modules/getReviewMarking?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Review Marking fetched from server.'))
      );
  }

  getMarking(moduleID): Observable<TestMarking[]> {

    return this.http.get<TestMarking[]>(this.app.url + "/modules/getMarking?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Marking fetched from server.'))
      );
  }

  getModuleByID(moduleID): Observable<ModuleFE> {

    return this.http.get<ModuleFE>(this.app.url + "/modules/getByModuleID?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Modules fetched from server.'))
      );
  }


}
