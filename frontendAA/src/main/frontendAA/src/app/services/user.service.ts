import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthorizationService} from "./authorization.service";
import {Observable} from "rxjs";
import {User} from "../modelObjs/objects.model";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private auth: AuthorizationService) {
  }

  findByUserID(userID): Observable<User> {
    let headers = new HttpHeaders({
      'Accept': 'application/json',
      'Authorization': 'Basic ' + localStorage.getItem("creds")
    });
    console.log(headers);
    return this.http.get<User>("http://localhost:8080/user/findByUserID?userID=" + userID, {headers: headers})
      .pipe(
        tap(_ => console.log('User fetched from server.'))
      );
  }

  findUsers(): Observable<User[]> {
    let headers = new HttpHeaders({
      'Accept': 'application/json',
      'Authorization': 'Basic ' + localStorage.getItem("creds")
    });
    console.log(headers);
    return this.http.get<User[]>("http://localhost:8080/user/findAll", {headers: headers})
      .pipe(
        tap(_ => console.log('Users fetched from server.'))
      );
  }
}
