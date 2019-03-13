import {Component, OnInit} from '@angular/core';
import {ModuleWithTutorFE} from "../modelObjs/objects.model";
import {ModulesService} from "../services/modules.service";

@Component({
  selector: 'app-my-modules',
  templateUrl: './my-modules.component.html',
  styleUrls: ['./my-modules.component.css']
})
export class MyModulesComponent implements OnInit {

  modulesWithTutors: ModuleWithTutorFE[];

  constructor(private modServ: ModulesService) {
  }

  ngOnInit() {
    this.getMyModulesWithTutors();
  }

  getMyModulesWithTutors() {
    return this.modServ.getMyModulesWithTutors()
      .subscribe(modules => {this.modulesWithTutors = modules}, error => {
        location.reload();
      });
  }

}
