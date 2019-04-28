import {Component, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";
import {UserService} from "../services/user.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  email: string;
  newPassword: string;
  repeatPassword: string;
  success: boolean = false;
  successMessage: string = "";
  error: boolean = false;
  errorMessage: string = "";
  resetString: string;

  constructor(private router: Router, private userService: UserService, private route: ActivatedRoute) {
    this.resetString = this.route.snapshot.paramMap.get('resetString');
  }

  /**
   * Called on initialisation of method
   */
  ngOnInit() {
  }

  /**
   * Resets the users password, providing criteria is met
   * If criteria is not met error messages will be output
   * @param form
   */
  resetPassword(form: NgForm) {
    if (!this.email && this.email.length < 1) {
      this.errorMessage = "You must enter an email address.";
      this.error = true;
      return;
    }

    if (this.newPassword.length < 8) {
      this.errorMessage = "Password must be at least 8 characters in length.";
      this.error = true;
      return;
    }

    let numReg = new RegExp('[1-9]');
    if (!numReg.test(this.newPassword)) {
      this.errorMessage = "Password must contain at least one number.";
      this.error = true;
      return;
    }

    let capitalReg = new RegExp('[A-Z]');
    if (!capitalReg.test(this.newPassword)) {
      this.errorMessage = "Password must contain at least one capital letter.";
      this.error = true;
      return;
    }

    if (this.newPassword != this.repeatPassword) {
      this.errorMessage = "Passwords must match.";
      this.error = true;
      return;
    }

    this.userService.resetPassword(this.email, this.newPassword, this.resetString)
      .subscribe(test => {
        form.reset();
        this.success = true;
        this.error = false;
        this.successMessage = "Password changed successfully. Please return to login.";
      }, error => {
        this.error = true;
        this.errorMessage = "Reset string does not match what is stored in database for this user."
      });
  }
}
