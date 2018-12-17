import {Component} from '@angular/core';
import {HttpHeaders} from "@angular/common/http";



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
