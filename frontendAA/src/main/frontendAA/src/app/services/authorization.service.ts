import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {tap} from "rxjs/operators";
import {Performance, TokenPojo, User} from "../modelObjs/objects.model";
import {stringify} from "querystring";
import {Observable} from "rxjs";
import {AppComponent} from "../app.component";
import {ActivatedRoute, Router, RouterStateSnapshot} from "@angular/router";


@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {


  private _base64Credential: string;
  constructor(public _http: HttpClient, private app: AppComponent, private router : Router) {
  }


  get base64Credential(): string {
    return this._base64Credential;
  }

  /**
   *
   * @param user
   */
  public loginServ(user: User) {

    this._base64Credential = btoa(user.username + ':' + user.password);
    let headers = new HttpHeaders({'Accept': 'application/json', "Authorization": 'Basic ' + this._base64Credential});

    return this._http.get<TokenPojo>(this.app.url + "/main/login", {headers: headers}).pipe(tap(data => {

      let userDetails = data.user;
      console.log(data);
      if (userDetails) {
        // cache user details in browser
        this.app.principalUser = userDetails;
        localStorage.setItem('principalUser', JSON.stringify(userDetails));
        localStorage.setItem('creds', data.token);
      }
    }));
  }

  getUser(): Observable<User> {
    let headers = new HttpHeaders({
      'Accept': 'application/json',
      'Authorization': 'Basic ' + localStorage.getItem("creds")
    });
    return this._http.get<User>(this.app.url + "/main/getUser", {headers: headers})
      .pipe(
        tap(_ => {console.log('User fetched from server.')})
      );
  }

  /**
   *
   */
  public logoutServ() {
    return this._http.get<any>(this.app.url + '/main/logout', {headers: this.app.headers}).pipe(tap(data => {
      localStorage.removeItem('principalUser');
      localStorage.removeItem('creds');
    }));
  }


}
