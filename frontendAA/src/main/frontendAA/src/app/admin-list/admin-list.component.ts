import {Component, OnInit} from '@angular/core';
import {User} from "../modelObjs/objects.model";
import {UserService} from "../services/user.service";

@Component({
  selector: 'app-admin-list',
  templateUrl: './admin-list.component.html',
  styleUrls: ['./admin-list.component.css']
})
export class AdminListComponent implements OnInit {

  users: User[];

  constructor(private userService: UserService) {
  }

  /**
   * Called when the component is initialised
   */
  ngOnInit() {
    this.getAdmins();
  }

  /**
   * Gets all the admins in the system
   */
  getAdmins() {
    return this.userService.getAdmins()
      .subscribe(users => this.users = users);
  }
}
