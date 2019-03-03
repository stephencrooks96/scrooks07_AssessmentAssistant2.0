import {Component} from '@angular/core';
import {HttpHeaders} from "@angular/common/http";
import {User} from "./modelObjs/objects.model";



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

  public url = "http://localhost:8080";

  authenticated() {
    /*
   If logged in
    */
    if (localStorage.getItem('principalUser')) {
      return true;
    } else {
      return false;
    }
  }

}
