import {Component, OnInit} from '@angular/core';
import {User} from "../modelObjs/objects.model";
import {UserService} from "../services/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-create-profile',
  templateUrl: './create-profile.component.html',
  styleUrls: ['./create-profile.component.css']
})
export class CreateProfileComponent implements OnInit {

  user = new User();
  repeatPassword: string;
  usernameError: boolean;
  usernameErrorMessage: string;
  firstNameErrorMessage: string;
  firstNameError: boolean;
  lastNameErrorMessage: string;
  lastNameError: boolean;
  passwordErrorMessage: string;
  passwordError: boolean;

  constructor(private userService: UserService, private route: ActivatedRoute, private router: Router) {
  }

  /**
   * Called on initialization of component
   */
  ngOnInit() {
    this.repeatPassword = "";
    this.usernameError = false;
    this.usernameErrorMessage = "";
    this.firstNameErrorMessage = "";
    this.firstNameError = false;
    this.lastNameErrorMessage = "";
    this.lastNameError = false;
    this.passwordErrorMessage = "";
    this.passwordError = false;
  }

  /**
   * Gets all usernames from database to ensure user doesn't choose one that
   * is already in use
   * @param username
   */
  getUserNames(username) {
    this.usernameError = false;
    return this.userService.getUserNames(username)
      .subscribe(usernameCheck => {
        this.usernameError = usernameCheck;
        this.usernameErrorMessage = "This email is already associated with an account on this site.";
      });
  }

  /**
   * Creates a users profile based on the form entries
   * Performs validation and outputs necessary error messages
   * @param form
   */
  createProfile(form: NgForm) {
    if (this.usernameError) {
      return;
    }

    if (!this.user.username && this.user.username.length < 1) {
      this.usernameErrorMessage = "You must enter an email address.";
      this.usernameError = true;
      return;
    }

    if (this.user.username && this.user.username.length > 50) {
      this.usernameErrorMessage = "Email address must be less than 50 characters long.";
      this.usernameError = true;
      return;
    }

    if (!this.user.firstName && this.user.username.length < 1) {
      this.firstNameErrorMessage = "You must enter a first name.";
      this.firstNameError = true;
      return;
    }

    if (this.user.firstName && this.user.firstName.length > 30) {
      this.firstNameErrorMessage = "First name must be less than 30 characters long.";
      this.firstNameError = true;
      return;
    }

    if (!this.user.lastName && this.user.lastName.length < 1) {
      this.lastNameErrorMessage = "You must enter a last name.";
      this.lastNameError = true;
      return;
    }

    if (this.user.lastName && this.user.lastName.length > 30) {
      this.lastNameErrorMessage = "Last name must be less than 30 characters long.";
      this.lastNameError = true;
      return;
    }

    if (this.user.password.length < 8) {
      this.passwordErrorMessage = "Password must be at least 8 characters in length.";
      this.passwordError = true;
      return;
    }

    let numReg = new RegExp('[1-9]');
    if (!numReg.test(this.user.password)) {
      this.passwordErrorMessage = "Password must contain at least one number.";
      this.passwordError = true;
      return;
    }

    let capitalReg = new RegExp('[A-Z]');
    if (!capitalReg.test(this.user.password)) {
      this.passwordErrorMessage = "Password must contain at least one capital letter.";
      this.passwordError = true;
      return;
    }

    if (this.user.password != this.repeatPassword) {
      this.passwordErrorMessage = "Passwords must match.";
      this.passwordError = true;
      return;
    }

    this.userService.createProfile(this.user)
      .subscribe(test => {
        form.reset();
        this.router.navigate(['/login']);
      }, error => {
      });
  }
}
