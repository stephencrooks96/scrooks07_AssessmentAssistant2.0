import {Component, OnInit} from '@angular/core';
import {Performance, QuestionAndAnswer} from "../modelObjs/objects.model";
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {DomSanitizer} from "@angular/platform-browser";
import {KatexOptions} from "ng-katex";

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit {

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
   * Called on initialization of component
   */
  ngOnInit() {
    this.getFeedback(this.testID);
  }

  /**
   * Outputs images of chosen question
   * @param base64
   */
  readyImage(base64): any {
    return this.sanitizer.bypassSecurityTrustResourceUrl("data:image/png;base64," + base64);
  }

  /**
   * Gets the feedback on the test from the database
   * @param testID
   */
  getFeedback(testID) {
    return this.testServ.getFeedback(testID)
      .subscribe(performance => {
        this.performance = performance;
        this.answerDetail = this.performance.testAndResult.questions[this.answerCounter];
      }, error => {
        this.router.navigate(['/myModules']);
      });
  }
}
