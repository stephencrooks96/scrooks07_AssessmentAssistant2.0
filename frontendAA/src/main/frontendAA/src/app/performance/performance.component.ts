import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {DomSanitizer} from "@angular/platform-browser";
import {Performance, QuestionAndAnswer} from "../modelObjs/objects.model";
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

  /**
   * Called on initialization of the component
   */
  ngOnInit() {
    this.getPerformance(this.testID);
  }

  /**
   * Converts the image from base64 to an actual visible image
   * @param base64
   */
  readyImage(base64): any {
    return this.sanitizer.bypassSecurityTrustResourceUrl("data:image/png;base64," + base64);
  }

  /**
   * Gets the performance data on this test for this user
   * Contains all information such as scores and feedback
   * @param testID
   */
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
