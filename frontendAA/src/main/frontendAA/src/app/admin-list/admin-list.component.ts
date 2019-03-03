import { Component, OnInit } from '@angular/core';
import {User} from "../modelObjs/objects.model";
import {UserService} from "../services/user.service";
import {ModulesService} from "../services/modules.service";
import {AuthorizationService} from "../services/authorization.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-admin-list',
  templateUrl: './admin-list.component.html',
  styleUrls: ['./admin-list.component.css']
})
export class AdminListComponent implements OnInit {

  users : User[];
  constructor(private userService : UserService, private moduleService : ModulesService, private auth : AuthorizationService, private route : ActivatedRoute, private router: Router, private modalService: NgbModal) { }

  ngOnInit() {
    this.getAdmins();
  }

  getAdmins() {
    return this.userService.getAdmins()
      .subscribe(users => this.users = users);
  }

}
