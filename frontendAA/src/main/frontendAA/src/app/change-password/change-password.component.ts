import {Component, OnInit} from '@angular/core';
import {ChangePassword} from "../modelObjs/objects.model";
import {UserService} from "../services/user.service";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  changePasswordObj = new ChangePassword();
  errorMessage: string;
  error = false;

  constructor(private userService: UserService) {
  }

  /**
   * Called on initialization of component
   */
  ngOnInit() {
  }

  /**
   * Submits change password request from form
   * to database
   * Performs validation and output necessary error messages
   * Also updates users token info in line with new password
   */
  changePassword() {
    if (this.changePasswordObj.newPassword.length < 8) {
      this.errorMessage = "Password must be at least 8 characters in length.";
      this.error = true;
      return;
    }

    let numReg = new RegExp('[1-9]');
    if (!numReg.test(this.changePasswordObj.newPassword)) {
      this.errorMessage = "Password must contain at least one number.";
      this.error = true;
      return;
    }

    let capitalReg = new RegExp('[A-Z]');
    if (!capitalReg.test(this.changePasswordObj.newPassword)) {
      this.errorMessage = "Password must contain at least one capital letter.";
      this.error = true;
      return;
    }

    if (this.changePasswordObj.newPassword != this.changePasswordObj.repeatPassword) {
      this.errorMessage = "Passwords must match.";
      this.error = true;
      return;
    }

    this.userService.changePassword(this.changePasswordObj)
      .subscribe(test => {
        this.error = true;
        this.errorMessage = "Password changed successfully.";
        localStorage.removeItem('creds');
        localStorage.setItem('creds', test.token);
        location.reload();
      }, error => {
        this.error = true;
        this.errorMessage = "Current password incorrect."
      });
  }
}
