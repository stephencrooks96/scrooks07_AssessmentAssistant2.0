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

  user = new User();

  constructor(public auth: AuthorizationService, public router: Router) {
  }

  /**
   * Called on initialization of component
   */
  ngOnInit() {
    this.getUser();
  }

  /**
   * Gets logged in user to show they are logged in
   */
  getUser() {
    return this.auth.getUser()
      .subscribe(user => this.user = user);
  }

  /**
   * Logs user out of system
   */
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
