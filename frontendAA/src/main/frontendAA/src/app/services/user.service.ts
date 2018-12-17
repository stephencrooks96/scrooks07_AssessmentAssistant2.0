import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthorizationService} from "./authorization.service";
import {Observable} from "rxjs";
import {User} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";
import {AppComponent} from "../app.component";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private auth: AuthorizationService, private app: AppComponent) {
  }

  findByUserID(userID): Observable<User> {

    return this.http.get<User>("http://localhost:8080/user/findByUserID?userID=" + userID, {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('User fetched from server.'))
      );
  }

  findUsers(): Observable<User[]> {

    return this.http.get<User[]>("http://localhost:8080/user/findAll", {headers: this.app.headers})
      .pipe(
        tap(_ => console.log('Users fetched from server.'))
      );
  }
}
