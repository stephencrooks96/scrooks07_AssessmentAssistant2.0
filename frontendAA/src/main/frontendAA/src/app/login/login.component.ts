import {Component, OnInit} from '@angular/core';
import {User} from "../modelObjs/objects.model";
import {AuthorizationService} from "../services/authorization.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: User = new User();
  errorMsg: string;

  constructor(private auth: AuthorizationService, private router: Router) {
  }

  /**
   * Called on initialization of component
   */
  ngOnInit() {
  }

  /**
   * Logs user in to system based on credentials entered
   */
  login() {
    this.auth.loginServ(this.user).subscribe(data => {
      this.router.navigate(['/myModules']);
    }, error => {
      this.errorMsg = "error : Username or password is incorrect";
    });
  }
}
