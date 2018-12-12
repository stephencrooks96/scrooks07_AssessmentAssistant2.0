import {Component} from '@angular/core';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
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
