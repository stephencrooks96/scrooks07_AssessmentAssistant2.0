import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {AddModulePojo, Associate, ModuleFE} from "../modelObjs/objects.model";
import {NgForm} from "@angular/forms";
import {ModulesService} from "../services/modules.service";

const errorColour: string = "#dc3545";
const normalColour: string = "#202529";

@Component({
  selector: 'app-add-module',
  templateUrl: './add-module.component.html',
  styleUrls: ['./add-module.component.css']
})
export class AddModuleComponent implements OnInit {

  @ViewChild('assocInput') assocInput: ElementRef;
  modulePojo = new AddModulePojo();
  fileError: boolean;
  fileErrorMessage: string;
  today = new Date();
  nextYear = new Date();
  nameError;
  descriptError;
  fileSuccess: boolean = false;
  modules: ModuleFE[];
  checkStartDate: string = normalColour;
  checkEndDate: string = normalColour;
  endDateError = false;
  dateError = false;
  startDateError = false;
  private associationsFile: any;

  constructor(private moduleService: ModulesService) {
  }

  /*
  * Called every time this component is initialised
   */
  ngOnInit() {
    this.nextYear.setFullYear(this.nextYear.getFullYear() + 1);
    this.getPendingApproval();
  }

  /**
   * Gets the modules pending approval for the user
   */
  getPendingApproval() {
    return this.moduleService.getPendingApproval().subscribe(mod => this.modules = mod);
  }

  /**
   * Reads in associations from CSV uploaded by user
   * Converts CSV entries in to objects of type Associate
   * List of Associate's sent to back app for processing and entry to database
   * @param csv
   */
  readAssociations(csv: any) {
    this.associationsFile = csv.target.files[0];

    let fileReader = new FileReader();
    let stopPush;
    fileReader.readAsText(this.associationsFile);

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
        let assocToAdd = new Associate();
        if (subData.length === headers.length) {
          for (let j = 0; j < headers.length; j++) {
            if (headers[j] == "Email") {
              assocToAdd.username = subData[j];
            } else if (headers[j] == "AssociationType") {
              assocToAdd.associateType = subData[j];
            } else if (headers[j] == "FirstName") {
              assocToAdd.firstName = subData[j];
            } else if (headers[j] == "LastName") {
              assocToAdd.lastName = subData[j];
            }
          }
          if (!assocToAdd.associateType && !assocToAdd.username && !assocToAdd.firstName && !assocToAdd.lastName) {
            stopPush = true;
          } else if (!assocToAdd.associateType || !assocToAdd.username) {
            this.fileError = true;
            this.fileErrorMessage = "One or more cells have been left empty.";
            this.assocInput.nativeElement.value = null;
            return;
          } else if (assocToAdd.associateType != "S" && assocToAdd.associateType != "TA") {
            this.fileError = true;
            this.fileErrorMessage = "AssociationType can only be S or TA.";
            this.assocInput.nativeElement.value = null;
            return;
          }
        } else {
          this.fileError = true;
          this.fileErrorMessage = "Column count does not match headings.";
          this.assocInput.nativeElement.value = null;
          return;
        }
        if (!stopPush) {
          this.modulePojo.associations.push(assocToAdd);
        }
      }
      this.fileSuccess = true;
    };

  }

  /**
   * Adds a module from the data inserted to the form
   * Performs validation on all inputs and triggers errors to be shown on screen in necessary
   * @param form
   */
  async addModule(form: NgForm) {

    let generalError = false;
    this.descriptError = false;
    this.nameError = false;

    if (this.fileError) {
      generalError = true;
    }

    if (this.modulePojo.module.commencementDate.valueOf() >= this.modulePojo.module.endDate.valueOf()) {
      this.dateError = true;
      generalError = true;
    }
    if (this.modulePojo.module.commencementDate.valueOf() < new Date().valueOf() || this.modulePojo.module.commencementDate.valueOf() > new Date().setFullYear(new Date().getFullYear() + 1).valueOf()) {
      this.startDateError = true;
      this.checkStartDate = errorColour;
      generalError = true;
    }
    if (this.modulePojo.module.endDate.valueOf() < new Date().valueOf() || this.modulePojo.module.endDate.valueOf() > new Date().setFullYear(new Date().getFullYear() + 1).valueOf()) {
      this.endDateError = true;
      this.checkEndDate = errorColour;
      generalError = true;
    }

    this.modulePojo.module.moduleName = this.modulePojo.module.moduleName.trim();
    if (!this.modulePojo.module.moduleName || this.modulePojo.module.moduleName.length < 1 || this.modulePojo.module.moduleName.length > 50) {
      this.nameError = true;
      generalError = true;
    }

    this.modulePojo.module.moduleDescription = this.modulePojo.module.moduleDescription.trim();
    if (!this.modulePojo.module.moduleDescription || this.modulePojo.module.moduleDescription.length < 1 || this.modulePojo.module.moduleDescription.length > 500) {
      this.descriptError = true;
      generalError = true;
    }

    if (generalError) {
      return;
    }

    this.moduleService.addModule(this.modulePojo)
      .subscribe(_ => {
        form.reset();
        this.assocInput.nativeElement.value = null;
        this.fileSuccess = false;
        this.descriptError = false;
        this.nameError = false;
        this.fileError = false;
        this.checkEndDate = normalColour;
        this.checkStartDate = normalColour;
        this.dateError = false;
        this.startDateError = false;
        this.endDateError = false;
        this.getPendingApproval();
      }, _ => {
        return;
      });
  }
}
