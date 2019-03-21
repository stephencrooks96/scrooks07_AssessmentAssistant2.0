import {Component, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "../services/user.service";

@Component({
  selector: 'app-request-reset-password',
  templateUrl: './request-reset-password.component.html',
  styleUrls: ['./request-reset-password.component.css']
})
export class RequestResetPasswordComponent implements OnInit {

  email: string;
  confirmEmail: string;
  error: boolean = false;
  errorMessage: string;

  constructor(private router: Router, private userService: UserService) {
  }

  /**
   * Called on initialisation of the component
   */
  ngOnInit() {
  }

  /**
   * Requests a reset password link for the user
   * Will include a reset string from the database which belongs to the user
   * Emails entered must match
   * @param form
   */
  requestReset(form: NgForm) {
    this.error = false;
    if (this.email != this.confirmEmail) {
      this.error = true;
      this.errorMessage = "Emails entered do not match."
    }
    this.userService.requestReset(this.email)
      .subscribe(success => {
        form.reset();
        this.router.navigate(['/login']);
      }, error => {
        this.error = true;
        this.errorMessage = "Sorry there does not seem to be an account associated with this email.";
        return;
      });
  }
}
