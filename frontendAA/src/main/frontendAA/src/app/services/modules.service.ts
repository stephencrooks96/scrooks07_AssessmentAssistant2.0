import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {
  AddModulePojo,
  Associate,
  ModuleFE,
  ModuleRequestPojo,
  ModuleWithTutorFE,
  Performance,
  TestAndGrade,
  TestMarking,
  Tests
} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";
import {AppComponent} from "../app.component";

@Injectable({
  providedIn: 'root'
})
export class ModulesService {

  constructor(private http: HttpClient, private app: AppComponent) {
  }

  /**
   * Gets any modules that are pending approval for admins
   */
  getPendingApproval(): Observable<ModuleFE[]> {
    return this.http.get<ModuleFE[]>(this.app.url + "/modules/getModulesPendingApproval", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Modules returned.'))
      );
  }

  /**
   * Sends new module info to the backend
   * @param modulePojo
   */
  addModule(modulePojo): Observable<AddModulePojo> {
    return this.http.post<AddModulePojo>(this.app.url + "/modules/addModule", modulePojo, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Module added.'))
      );
  }

  /**
   * Sends new associations to backend
   * @param moduleID
   * @param associations
   */
  addAssociations(moduleID, associations): Observable<any> {
    return this.http.post<any>(this.app.url + "/modules/addAssociations?moduleID=" + moduleID, associations, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Module added.'))
      );
  }

  /**
   * Gets all module requests for tutor from backend
   */
  getModuleRequests(): Observable<ModuleRequestPojo[]> {
    return this.http.get<ModuleRequestPojo[]>(this.app.url + "/modules/getModuleRequests", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Module requests returned.'))
      );
  }

  /**
   * Sends info on removal of association to backend
   * @param username
   * @param moduleID
   */
  removeAssociation(username, moduleID): Observable<Associate> {
    return this.http.get<Associate>(this.app.url + "/modules/removeAssociate?username=" + username + "&moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Associates returned.'))
      );
  }

  /**
   * Gets associate information from backend
   * @param moduleID
   */
  getAssociates(moduleID): Observable<Associate[]> {
    return this.http.get<Associate[]>(this.app.url + "/modules/getAssociates?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Associates returned.'))
      );
  }

  /**
   * Sends information on module approval to backend
   * @param moduleID
   */
  approveModuleRequest(moduleID): Observable<any> {
    return this.http.get<any>(this.app.url + "/modules/approveModuleRequest?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Module request approved.'))
      );
  }

  /**
   * Sends information on module rejection to backend
   * @param moduleID
   */
  rejectModuleRequest(moduleID): Observable<any> {
    return this.http.get<any>(this.app.url + "/modules/rejectModuleRequest?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor request rejected.'))
      );
  }

  /**
   * Gets modules along with tutor info from backend
   */
  getMyModulesWithTutors(): Observable<ModuleWithTutorFE[]> {
    return this.http.get<ModuleWithTutorFE[]>(this.app.url + "/modules/getMyModulesWithTutors", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Modules fetched from server.'))
      );
  }

  /**
   * Gets performance information from backend
   * @param moduleID
   */
  getPerformance(moduleID): Observable<Performance[]> {
    return this.http.get<Performance[]>(this.app.url + "/modules/getPerformance?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Performance fetched from server.'))
      );
  }

  /**
   * Gets module and tutor info for specific module from backend
   * @param moduleID
   */
  getModuleAndTutor(moduleID): Observable<ModuleWithTutorFE> {
    return this.http.get<ModuleWithTutorFE>(this.app.url + "/modules/getModuleAndTutor?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Modules fetched from server.'))
      );
  }

  /**
   * Gets a users association to a module from backend
   * @param moduleID
   */
  getModuleAssociation(moduleID): Observable<number> {
    return this.http.get<number>(this.app.url + "/modules/getModuleAssociation?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Module association fetched from server.'))
      );
  }

  /**
   * Gets the active tests from backend
   * @param moduleID
   */
  getActiveTests(moduleID): Observable<Tests[]> {
    return this.http.get<Tests[]>(this.app.url + "/modules/getActiveTests?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Active tests fetched from server.'))
      );
  }

  /**
   * Gets practice tests from backend
   * @param moduleID
   */
  getPracticeTests(moduleID): Observable<Tests[]> {
    return this.http.get<Tests[]>(this.app.url + "/modules/getPracticeTests?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Practice tests fetched from server.'))
      );
  }

  /**
   * Gets active results from backend
   * @param moduleID
   */
  getActiveResults(moduleID): Observable<TestAndGrade[]> {
    return this.http.get<TestAndGrade[]>(this.app.url + "/modules/getActiveResults?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Active results fetched from server.'))
      );
  }

  /**
   * Gets scheduled tests from backend
   * @param moduleID
   */
  getScheduledTests(moduleID): Observable<Tests[]> {
    return this.http.get<Tests[]>(this.app.url + "/modules/getScheduledTests?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Scheduled tests fetched from server.'))
      );
  }

  /**
   * Gets test drafts from backend for module
   * @param moduleID
   */
  getTestDrafts(moduleID): Observable<Tests[]> {
    return this.http.get<Tests[]>(this.app.url + "/modules/getTestDrafts?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Test drafts fetched from server.'))
      );
  }

  /**
   * Gets review marking info from backend for module
   * @param moduleID
   */
  getReviewMarking(moduleID): Observable<TestMarking[]> {
    return this.http.get<TestMarking[]>(this.app.url + "/modules/getReviewMarking?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Review Marking fetched from server.'))
      );
  }

  /**
   * Gets marking info from backend for module
   * @param moduleID
   */
  getMarking(moduleID): Observable<TestMarking[]> {
    return this.http.get<TestMarking[]>(this.app.url + "/modules/getMarking?moduleID=" + moduleID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Marking fetched from server.'))
      );
  }
}
