import {Component} from '@angular/core';
import {HttpHeaders} from "@angular/common/http";
import {User} from "./modelObjs/objects.model";
import {environment} from '../environments/environment';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  headers = new HttpHeaders({
    'Accept': 'application/json',
    'Authorization': 'Basic ' + localStorage.getItem("creds")
  });

  principalUser = new User();

  title = 'Assessment Assistant';

  public url = environment.url;

  authenticated() {
    /*
   If logged in
    */
    return !!localStorage.getItem('principalUser');
  }

}
