import { Component, OnInit } from '@angular/core';
import {Tests, TutorRequest, User} from "../modelObjs/objects.model";
import {NgForm} from "@angular/forms";
import {UserService} from "../services/user.service";
import {Router} from "@angular/router";
import {AuthorizationService} from "../services/authorization.service";

@Component({
  selector: 'app-become-tutor',
  templateUrl: './become-tutor.component.html',
  styleUrls: ['./become-tutor.component.css']
})
export class BecomeTutorComponent implements OnInit {

  request = new TutorRequest();
  user = new User();
  constructor(private userService : UserService, private router : Router, private authorizationService : AuthorizationService) { }

  ngOnInit() {
    this.request.tutorRequestID = -1;
    this.getTutorRequest();
    this.getUser();
  }

  getUser() {
    this.authorizationService.getUser().subscribe(
      user => {
        this.user = user;
        if (user.tutor == 1) {
          this.router.navigate(['/myModules']);
        }
      }, error => {
      });
  }

  getTutorRequest() {
    this.userService.getTutorRequest().subscribe(
      request => {
        if (request != null && request.approved == 1) {
          this.request = request;
          this.router.navigate(['/myModules']);
        } else {
          this.request.tutorRequestID = -1;
        }
      }, error => {
    });
  }

  submitRequest(form: NgForm) {
    if (!this.request.reason || this.request.reason.length < 1 || this.request.reason.length > 56535) {
      return;
    }

    this.userService.submitRequest(this.request)
      .subscribe(test => {
        form.reset();
        this.getTutorRequest();
      }, error => {
        return;
      });
  }

}
