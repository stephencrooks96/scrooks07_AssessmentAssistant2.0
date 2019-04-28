import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ModuleRequestPojo, TutorRequestPojo, User} from "../modelObjs/objects.model";
import {UserService} from "../services/user.service";
import {AuthorizationService} from "../services/authorization.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ModulesService} from "../services/modules.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NgForm} from "@angular/forms";
import {AppComponent} from "../app.component";

@Component({
  selector: 'app-admin-area',
  templateUrl: './admin-area.component.html',
  styleUrls: ['./admin-area.component.css']
})
export class AdminAreaComponent implements OnInit {

  @ViewChild('userInput') userInput: ElementRef;
  tutorRequests: TutorRequestPojo[];
  moduleRequests: ModuleRequestPojo[];
  users: User[];
  usersToAdd: User[] = [];
  userToRemove: number;
  userFile: any;
  fileSuccess: boolean;
  fileError: boolean;
  fileErrorMessage: string;
  user = new User();

  constructor(private app: AppComponent, private userService: UserService, private moduleService: ModulesService, private auth: AuthorizationService, private route: ActivatedRoute, private router: Router, private modalService: NgbModal) {
  }

  /**
   * Called on initialization of component
   */
  ngOnInit() {
    this.getTutorRequests();
    this.getModuleRequests();
    this.getUsers();
    this.getUser();
  }

  /**
   * Gets the users who is logged in from the database
   * If they aren't an admin they are returned home
   */
  getUser() {
    return this.auth.getUser()
      .subscribe(user => {
        if (user.userRoleID != 1) {
          this.backHome();
        }
      });
  }

  /**
   * Returns user home
   */
  backHome() {
    this.router.navigate(['/myModules']);
  }

  /**
   * Gets all users in system so admins can perform actions on them
   */
  getUsers() {
    return this.userService.findUsers()
      .subscribe(users => this.users = users);
  }

  /**
   * From ngBootstrap framework
   * @param modal
   */
  open(modal) {
    this.modalService.open(modal, {ariaLabelledBy: 'modal-basic-title'});
  }

  /**
   * Adds users from CSV form
   * @param form
   */
  addUsers(form: NgForm) {
    if (this.fileError) {
      return;
    }
    this.userService.addUsers(this.usersToAdd)
      .subscribe(_ => {
        form.reset();
        this.getUsers();
        this.userInput.nativeElement.value = null;
        this.fileSuccess = false;
        this.fileError = false;
      }, _ => {
        return;
      });
  }

  /**
   * Processes users from CSV and converts them to user objects
   * @param csv
   */
  readUsers(csv: any) {
    this.userFile = csv.target.files[0];
    let fileReader = new FileReader();
    let stopPush;
    fileReader.readAsText(this.userFile);

    fileReader.onload = () => {
      this.fileSuccess = false;
      this.fileError = false;
      this.fileErrorMessage = "";
      // Split on rows
      let data = fileReader.result.toString().split(/[\r\n]/);

      // Split header on commas
      let headers = data[0].split(',');

      let spliceArray = [];
      for (let i = 0; i < data.length; i++) {
        if (data[i] == "") {
          spliceArray.push([i]);
        }
      }
      for (let i = 0; i < spliceArray.length; i++) {
        data.splice(spliceArray[i] - i, 1);
      }
      for (let i = 1; i < data.length; i++) {
        stopPush = false;
        // split content based on comma
        let subData = data[i].split(',');
        let userToAdd = new User();
        if (subData.length === headers.length) {
          for (let j = 0; j < headers.length; j++) {
            if (headers[j] == "Email") {
              userToAdd.username = subData[j];
            } else if (headers[j] == "Tutor") {
              userToAdd.tutor = subData[j] == 'Y' ? 1 : 0;
            } else if (headers[j] == "FirstName") {
              userToAdd.firstName = subData[j];
            } else if (headers[j] == "LastName") {
              userToAdd.lastName = subData[j];
            }
          }
          if (!userToAdd.tutor && !userToAdd.username && !userToAdd.firstName && !userToAdd.lastName) {
            stopPush = true;
          } else if (userToAdd.tutor > 1 || userToAdd.tutor < 0 || !userToAdd.firstName || !userToAdd.lastName || !userToAdd.username) {
            this.fileError = true;
            this.fileErrorMessage = "One or more cells have been left empty.";
            this.userInput.nativeElement.value = null;
            return;
          }
        } else {
          this.fileError = true;
          this.fileErrorMessage = "Column count does not match headings.";
          this.userInput.nativeElement.value = null;
          return;
        }
        if (!stopPush) {
          this.usersToAdd.push(userToAdd);
        }
      }
      this.fileSuccess = true;
    };
  }

  /**
   * Gets any module request that have been made that require admin approval
   */
  getModuleRequests() {
    return this.moduleService.getModuleRequests()
      .subscribe(moduleRequests => this.moduleRequests = moduleRequests);
  }

  /**
   * Removes a selected user from system
   * @param userID
   */
  removeUser(userID) {
    return this.userService.removeUser(userID)
      .subscribe(user => this.getUsers());
  }

  /**
   * Makes a selected user in to an admin
   * @param userID
   */
  makeAdmin(userID) {
    return this.userService.makeAdmin(userID)
      .subscribe(user => this.getUsers());
  }

  /**
   * Makes a selected user in to a tutor
   * @param userID
   */
  makeTutor(userID) {
    return this.userService.makeTutor(userID)
      .subscribe(user => this.getUsers());
  }

  /**
   * Approves a selected module request made by a tutor
   * @param moduleID
   */
  approveModuleRequest(moduleID) {
    return this.moduleService.approveModuleRequest(moduleID)
      .subscribe(moduleRequests => this.getModuleRequests());
  }

  /**
   * Rejects a selected module request made by a tutor
   * @param moduleID
   */
  rejectModuleRequest(moduleID) {
    return this.moduleService.rejectModuleRequest(moduleID)
      .subscribe(moduleRequests => this.getModuleRequests());
  }

  /**
   * Gets all requests users have made to become a tutor
   */
  getTutorRequests() {
    return this.userService.getTutorRequests()
      .subscribe(tutorRequests => this.tutorRequests = tutorRequests);
  }

  /**
   * Approves a selected tutor request made by a user
   * @param userID
   */
  approveTutorRequest(userID) {
    return this.userService.approveTutorRequest(userID)
      .subscribe(tutorRequests => this.getTutorRequests());
  }

  /**
   * Rejects a selected tutor request made by a user
   * @param userID
   */
  rejectTutorRequest(userID) {
    return this.userService.rejectTutorRequest(userID)
      .subscribe(tutorRequests => this.getTutorRequests());
  }
}
