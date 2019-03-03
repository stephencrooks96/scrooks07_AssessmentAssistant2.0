import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ModulesService} from "../services/modules.service";
import {MarkingService} from "../services/marking.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DomSanitizer} from "@angular/platform-browser";
import {QuestionAndAnswer, Performance} from "../modelObjs/objects.model";
import {KatexOptions} from "ng-katex";

@Component({
  selector: 'app-performance',
  templateUrl: './performance.component.html',
  styleUrls: ['./performance.component.css']
})
export class PerformanceComponent implements OnInit {

  testID;
  performance = new Performance();
  answerDetail = new QuestionAndAnswer();
  answerCounter = 0;
  options: KatexOptions = {
    displayMode: true,
  };

  constructor(private route: ActivatedRoute, private testServ: TestService, private sanitizer: DomSanitizer, private router: Router) {
    this.testID = +this.route.snapshot.paramMap.get('testID');
  }

  ngOnInit() {
    this.getPerformance(this.testID);
  }

  readyImage(base64): any {
    return this.sanitizer.bypassSecurityTrustResourceUrl("data:image/png;base64," + base64);
  }

  getPerformance(testID) {
    return this.testServ.getPerformance(testID)
      .subscribe(performance => {
          this.performance = performance;
          this.answerDetail = this.performance.testAndResult.questions[this.answerCounter];
        }, error => {
            this.router.navigate(['/myModules']);
        });
  }

}
