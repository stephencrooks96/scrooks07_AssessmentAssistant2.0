import {AfterViewInit, Component, DoCheck, Input, OnInit} from '@angular/core';
import {
  Marker,
  MarkerAndReassigned,
  MarkerWithChart,
  Question,
  Tests,
  TutorQuestionPojo
} from "../modelObjs/objects.model";
import {Chart} from 'chart.js';
import {DelegateMarkingComponent} from "../delegate-marking/delegate-marking.component";
import {MarkingService} from "../services/marking.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-delegate-detail',
  templateUrl: './delegate-detail.component.html',
  styleUrls: ['./delegate-detail.component.css']
})
export class DelegateDetailComponent implements OnInit, DoCheck, AfterViewInit {

  @Input() markerDetail = new Marker();
  detailChart;
  markerDetailCheck;
  testID: number;
  percentageError = false;
  questions : TutorQuestionPojo[];
  reassign : MarkerAndReassigned[];
  markerWithChart = new MarkerWithChart();

  constructor(private testServ: TestService, private route: ActivatedRoute, private markServ: MarkingService, private del: DelegateMarkingComponent) {
    this.testID = +this.route.snapshot.paramMap.get('testID');
  }

  ngOnInit() {
    this.getMarkersData(this.testID);
    this.getQuestions(this.testID);
    this.markerDetailCheck = this.markerDetail;
  }

  ngDoCheck() {
    if (this.markerDetailCheck != this.markerDetail) {
      this.markerDetailCheck = this.markerDetail;
      this.detailChart = this.chartInit();
    }
  }

  ngAfterViewInit() {
    this.detailChart = this.chartInit();
  }

  getQuestions(testID) {
    return this.testServ.getQuestions(testID).subscribe(questions => this.questions = questions);
  }

  getMarkersData(testID) {
    return this.markServ.getMarkersData(testID)
      .subscribe(markers => {
          this.markerWithChart = markers;
          this.reassign = [];
          for (let m = 0; m < markers.markers.length; m++) {
            let assigned = new MarkerAndReassigned();
            assigned.previousMarkerID = markers.markers[m].marker.userID;
            assigned.specifyQuestion = 0;
            this.reassign.push(assigned);
          }
        }
      );
  }

  chartInit() {
    return new Chart('detail', {
      type: 'doughnut',
      data: {
        labels: ['Marked', 'Unmarked'],
        datasets: [{
          data: [this.markerDetail.marked, this.markerDetail.unmarked],
          backgroundColor: ['rgb(153,255,51)', 'rgb(255,51,51)']
        }]
      },
      options: {
        responsive: true,
        legend: {
          position: 'left',
        }
      }
    });
  }

  assign(form: NgForm) {

    for (let x = 0; x < this.reassign.length; x++) {
      this.reassign[x].markerID = this.markerDetail.marker.userID;
      if (this.reassign[x].numberToReassign > 100) {
        this.percentageError = true;
        return;
      }
    }
    this.markServ.reassignAnswers(this.reassign, this.testID)
      .subscribe(x => {
        form.reset();
        this.getMarkersData(this.testID);
        this.del.getMarkersData(this.testID);
        this.percentageError = false;
      }, error => {
        return;
      });
  }

}
