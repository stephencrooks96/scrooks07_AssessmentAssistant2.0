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
    let parameters = [];
    for (const s of window.location.search.replace("?", "").split("&")) {
      let parameter: string[];
      parameter = s.split("=");
      parameters[parameter[0]] = parameter[1];
    }
    if (!parameters["cacheCleared"]) {
      (window as any).location.search = '?cacheCleared=1';
    }

    this.getMyModulesWithTutors();
  }

  getMyModulesWithTutors() {
    return this.modServ.getMyModulesWithTutors()
      .subscribe(modules => {this.modulesWithTutors = modules});
  }

}
