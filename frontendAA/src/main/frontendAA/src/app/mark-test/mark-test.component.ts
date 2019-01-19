import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DomSanitizer} from "@angular/platform-browser";
import {Tests} from "../modelObjs/objects.model";

@Component({
  selector: 'app-mark-test',
  templateUrl: './mark-test.component.html',
  styleUrls: ['./mark-test.component.css']
})
export class MarkTestComponent implements OnInit {

  testID : number;
  test = new Tests();
  constructor(private router: Router, private route: ActivatedRoute, private testServ: TestService, private modalService: NgbModal, private sanitizer: DomSanitizer) {
    this.testID = +this.route.snapshot.paramMap.get('testID');
  }
  ngOnInit() {
    this.getByTestID(this.testID);

  }

  getByTestID(testID) {
    return this.testServ.getByTestID(testID)
      .subscribe(test => {
          this.test = test;
        }
      );
  }
}
