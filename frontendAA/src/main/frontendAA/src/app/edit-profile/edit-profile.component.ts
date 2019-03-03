import { Component, OnInit } from '@angular/core';
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
  usernameError : boolean;
  usernameErrorMessage: string;
  firstnameErrorMessage: string;
  firstnameError: boolean;
  lastnameErrorMessage: string;
  lastnameError: boolean;
  success: boolean;

  constructor(private userService : UserService, private auth : AuthorizationService, private route : ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.usernameError = false;
    this.usernameErrorMessage = "";
    this.firstnameErrorMessage = "";
    this.firstnameError = false;
    this.lastnameErrorMessage = "";
    this.lastnameError = false;
    this.success = false;
    this.getUser();
  }

  getUsernames(username) {
    this.usernameError = false;
    return this.userService.getUsernamesLogged(username)
      .subscribe(usernameCheck => {
        this.usernameError = usernameCheck;
        this.usernameErrorMessage = "This email is already associated with an account on this site.";
      });
  }

  getUser() {
    return this.auth.getUser()
      .subscribe(user => this.user = user);
  }

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
      this.firstnameErrorMessage = "You must enter a first name.";
      this.firstnameError = true;
      return;
    }

    if (this.user.firstName && this.user.firstName.length > 30) {
      this.firstnameErrorMessage = "First name must be less than 30 characters long.";
      this.firstnameError = true;
      return;
    }

    if (!this.user.lastName && this.user.lastName.length < 1) {
      this.lastnameErrorMessage = "You must enter a last name.";
      this.lastnameError = true;
      return;
    }

    if (this.user.lastName && this.user.lastName.length > 30) {
      this.lastnameErrorMessage = "Last name must be less than 30 characters long.";
      this.lastnameError = true;
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
