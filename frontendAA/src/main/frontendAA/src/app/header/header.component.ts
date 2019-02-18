import {Component, OnInit} from '@angular/core';
import {AuthorizationService} from "../services/authorization.service";
import {Router} from "@angular/router";
import {User} from "../modelObjs/objects.model";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  currentUser: User;
  user = new User();

  constructor(public auth: AuthorizationService, public router: Router) {
  }


  ngOnInit() {
    this.getUser();
  }

  getUser() {
    return this.auth.getUser()
      .subscribe(user => this.user = user);
  }

// login out from the app
  logout() {
    this.auth.logoutServ()
      .subscribe(
        data => {
          this.router.navigate(['/login']);
        },
        error => {

        });
  }


}
