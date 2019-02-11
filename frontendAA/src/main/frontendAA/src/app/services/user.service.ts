import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthorizationService} from "./authorization.service";
import {Observable} from "rxjs";
import {ChangePassword, User} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";
import {AppComponent} from "../app.component";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private auth: AuthorizationService, private app: AppComponent) {
  }

  findByUserID(userID): Observable<User> {

    return this.http.get<User>(this.app.url + "/user/findByUserID?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('User fetched from server.'))
      );
  }

  changePassword(changePassword): Observable<ChangePassword> {
    return this.http.post<ChangePassword>(this.app.url + "/user/changePassword", changePassword, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Password changed.'))
      );
  }

  findUsers(): Observable<User[]> {

    return this.http.get<User[]>(this.app.url + "/user/findAll", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Users fetched from server.'))
      );
  }
}
