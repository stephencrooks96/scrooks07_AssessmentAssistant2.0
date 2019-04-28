import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthorizationService} from "./authorization.service";
import {Observable} from "rxjs";
import {TutorRequest, TutorRequestPojo, User, UserSession} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";
import {AppComponent} from "../app.component";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private auth: AuthorizationService, private app: AppComponent, private authorization: AuthorizationService) {
  }

  /**
   * Sends request to change password to backend
   * @param changePassword
   */
  changePassword(changePassword): Observable<UserSession> {
    return this.http.post<UserSession>(this.app.url + "/user/changePassword", changePassword, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Password changed.'))
      );
  }

  /**
   * Sends request to reset password to backend
   * @param email
   * @param newPassword
   * @param resetString
   */
  resetPassword(email, newPassword, resetString): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/resetPassword?email=" + email + "&newPassword=" + newPassword + "&resetString=" + resetString)
      .pipe(
        tap(_ => console.log('Password changed.'))
      );
  }

  /**
   * Sends request for reset password email to backend
   * @param email
   */
  requestReset(email): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/requestResetPassword?email=" + email)
      .pipe(
        tap(_ => console.log('Password changed.'))
      );
  }

  /**
   * Sends request to become tutor to backend
   * @param tutorRequest
   */
  submitRequest(tutorRequest): Observable<TutorRequest> {
    return this.http.post<TutorRequest>(this.app.url + "/user/submitTutorRequest", tutorRequest, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor request submitted.'))
      );
  }

  /**
   * Sends request to create profile to backend
   * @param user
   */
  createProfile(user: User): Observable<User> {
    return this.http.post<User>(this.app.url + "/user/createProfile", user)
      .pipe(
        tap(_ => console.log('User created.'))
      );
  }

  /**
   * Sends request to edit profile to backend
   * @param user
   */
  editProfile(user: User): Observable<UserSession> {
    return this.http.post<UserSession>(this.app.url + "/user/editProfile", user, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('User edited.'))
      );
  }

  /**
   * Sends request to get all tutor requests for user from backend
   */
  getTutorRequest(): Observable<TutorRequest> {
    return this.http.get<TutorRequest>(this.app.url + "/user/getTutorRequest", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor request returned.'))
      );
  }

  /**
   * Sends request to get all tutor requests from backend
   */
  getTutorRequests(): Observable<TutorRequestPojo[]> {
    return this.http.get<TutorRequestPojo[]>(this.app.url + "/user/getTutorRequests", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor requests returned.'))
      );
  }

  /**
   * Sends request to approve tutor request to backend
   * @param userID
   */
  approveTutorRequest(userID): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/approveTutorRequest?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor request approved.'))
      );
  }

  /**
   * Sends request to remove user to backend
   * @param userID
   */
  removeUser(userID): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/removeUser?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('User removed.'))
      );
  }

  /**
   * Sends request to make a user a tutor to backend
   * @param userID
   */
  makeTutor(userID): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/makeTutor?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('User made to tutor.'))
      );
  }

  /**
   * Sends request to make user an admin to backend
   * @param userID
   */
  makeAdmin(userID): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/makeAdmin?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('User made to admin.'))
      );
  }

  /**
   * Sends request to reject a tutor request to backend
   * @param userID
   */
  rejectTutorRequest(userID): Observable<any> {
    return this.http.get<any>(this.app.url + "/user/rejectTutorRequest?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Tutor request rejected.'))
      );
  }

  /**
   * Gets user name check
   * @param username
   */
  getUserNames(username): Observable<boolean> {
    return this.http.get<boolean>(this.app.url + "/user/getUsernames?username=" + username)
      .pipe(
        tap(_ => console.log('Username check from server.'))
      );
  }

  /**
   * Gets user name check for when the user is logged in as one of the user name
   * @param username
   */
  getUserNamesLogged(username): Observable<boolean> {
    return this.http.get<boolean>(this.app.url + "/user/getUsernames?username=" + username, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Username check from server.'))
      );
  }

  /**
   * Gets the admins contact info from backend
   */
  getAdmins(): Observable<User[]> {
    return this.http.get<User[]>(this.app.url + "/user/getAdmins", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Admins fetched from server.'))
      );
  }

  /**
   * Sends users information to backend
   * @param users
   */
  addUsers(users: User[]): Observable<User[]> {
    return this.http.post<User[]>(this.app.url + "/user/addUsers", users, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Users added.'))
      );
  }

  /**
   * Sends request to find all users to backend and receives them back
   */
  findUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.app.url + "/user/findAll", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Users fetched from server.'))
      );
  }
}
