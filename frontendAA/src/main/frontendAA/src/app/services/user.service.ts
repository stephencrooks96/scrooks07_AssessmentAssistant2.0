import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthorizationService} from "./authorization.service";
import {Observable} from "rxjs";
import {ChangePassword, TutorRequest, TutorRequestPojo, User, UserSession} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";
import {AppComponent} from "../app.component";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private auth: AuthorizationService, private app: AppComponent, private authorization: AuthorizationService) {
  }

  findByUserID(userID): Observable<User> {

    return this.http.get<User>(this.app.url + "/user/findByUserID?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('User fetched from server.'))
      );
  }

  changePassword(changePassword): Observable<UserSession> {
    return this.http.post<UserSession>(this.app.url + "/user/changePassword", changePassword, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Password changed.'))
      );
  }

  resetPassword(email, newPassword, resetString): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/resetPassword?email=" + email + "&newPassword=" + newPassword + "&resetString=" + resetString)
      .pipe(
        tap(_ => console.log('Password changed.'))
      );
  }

  requestReset(email): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/requestResetPassword?email=" + email)
      .pipe(
        tap(_ => console.log('Password changed.'))
      );
  }

  submitRequest(tutorRequest): Observable<TutorRequest> {
    return this.http.post<TutorRequest>(this.app.url + "/user/submitTutorRequest", tutorRequest, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor request submitted.'))
      );
  }

  createProfile(user : User): Observable<User> {
    return this.http.post<User>(this.app.url + "/user/createProfile", user)
      .pipe(
        tap(_ => console.log('User created.'))
      );
  }

  editProfile(user : User): Observable<User> {
    return this.http.post<User>(this.app.url + "/user/editProfile", user, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('User edited.'))
      );
  }

  getTutorRequest(): Observable<TutorRequest> {
    return this.http.get<TutorRequest>(this.app.url + "/user/getTutorRequest", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor request returned.'))
      );
  }

  getTutorRequests(): Observable<TutorRequestPojo[]> {
    return this.http.get<TutorRequestPojo[]>(this.app.url + "/user/getTutorRequests", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor requests returned.'))
      );
  }

  approveTutorRequest(userID): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/approveTutorRequest?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor request approved.'))
      );
  }

  removeUser(userID): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/removeUser?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('User removed.'))
      );
  }

  makeTutor(userID): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/makeTutor?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('User made to tutor.'))
      );
  }

  makeAdmin(userID): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/makeAdmin?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('User made to admin.'))
      );
  }

  rejectTutorRequest(userID): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/rejectTutorRequest?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor request rejected.'))
      );
  }

  getUsernames(username): Observable<boolean> {
    return this.http.get<boolean>(this.app.url + "/user/getUsernames?username=" + username)
      .pipe(
        tap(_ => console.log('Username check from server.'))
      );
  }

  getUsernamesLogged(username): Observable<boolean> {
    return this.http.get<boolean>(this.app.url + "/user/getUsernames?username=" + username, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Username check from server.'))
      );
  }

  getAdmins(): Observable<User[]> {

    return this.http.get<User[]>(this.app.url + "/user/getAdmins", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Admins fetched from server.'))
      );
  }

  addUsers(users : User[]): Observable<User[]> {

    return this.http.post<User[]>(this.app.url + "/user/addUsers", users, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Users added.'))
      );
  }

  findUsers(): Observable<User[]> {

    return this.http.get<User[]>(this.app.url + "/user/findAll", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Users fetched from server.'))
      );
  }
}
