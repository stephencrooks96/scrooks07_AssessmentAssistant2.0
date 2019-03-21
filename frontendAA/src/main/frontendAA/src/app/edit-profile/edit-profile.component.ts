import {Component, OnInit} from '@angular/core';
import {User} from "../modelObjs/objects.model";
import {UserService} from "../services/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import {AuthorizationService} from "../services/authorization.service";

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  user = new User();
  usernameError: boolean;
  usernameErrorMessage: string;
  firstNameErrorMessage: string;
  firstNameError: boolean;
  lastNameErrorMessage: string;
  lastNameError: boolean;
  success: boolean;

  constructor(private userService: UserService, private auth: AuthorizationService, private route: ActivatedRoute, private router: Router) {
  }

  /**
   * Called on initialization of component
   */
  ngOnInit() {
    this.usernameError = false;
    this.usernameErrorMessage = "";
    this.firstNameErrorMessage = "";
    this.firstNameError = false;
    this.lastNameErrorMessage = "";
    this.lastNameError = false;
    this.success = false;
    this.getUser();
  }

  /**
   * Gets user names to ensure user does not select one that is in use
   * @param username
   */
  getUserNames(username) {
    this.usernameError = false;
    return this.userService.getUserNamesLogged(username)
      .subscribe(usernameCheck => {
        this.usernameError = usernameCheck;
        this.usernameErrorMessage = "This email is already associated with an account on this site.";
      });
  }

  /**
   * Gets the currently logged in user
   */
  getUser() {
    return this.auth.getUser()
      .subscribe(user => this.user = user);
  }

  /**
   * Submits the edited profile info from the form
   * Performs validation and outputs necessary error messages
   * @param form
   */
  editProfile(form: NgForm) {

    this.success = false;

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

    this.userService.editProfile(this.user)
      .subscribe(test => {
        form.reset();
        this.getUser();
        this.success = true;
      }, error => {
      });
  }
}
