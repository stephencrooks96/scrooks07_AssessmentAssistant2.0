import {AfterViewInit, ChangeDetectorRef, Component, DoCheck, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DomSanitizer} from "@angular/platform-browser";
import {Marker, MarkerWithChart, Tests} from "../modelObjs/objects.model";
import {MarkingService} from "../services/marking.service";
import {Chart} from 'chart.js';

@Component({
  selector: 'app-delegate-marking',
  templateUrl: './delegate-marking.component.html',
  styleUrls: ['./delegate-marking.component.css']
})
export class DelegateMarkingComponent implements OnInit, AfterViewInit, DoCheck {

  testID: number;
  test = new Tests();
  markerWithChart = new MarkerWithChart();
  markerDetail = new Marker();
  chart;
  chartCheck = false;
  chartCount = 0;

  constructor(private cdr: ChangeDetectorRef, private markServ: MarkingService, private router: Router, private route: ActivatedRoute, private testServ: TestService, private modalService: NgbModal, private sanitizer: DomSanitizer) {
    this.testID = +this.route.snapshot.paramMap.get('testID');
  }

  ngOnInit() {
    this.getByTestID(this.testID);
    this.getMarkersData(this.testID);
  }

  ngDoCheck() {
    if (this.markerWithChart) {
      if (this.markerWithChart.labels && this.markerWithChart.colours && this.markerWithChart.data) {
        if (this.markerWithChart.labels.length > 1 && this.markerWithChart.colours.length > 1 && this.markerWithChart.data.length > 1) {
          this.chartCheck = true;

          if (this.chartCount == 0) {
            this.chart = this.mainChartInit();
            this.chartCount++;
          }
        }
      }
    }
  }

  ngAfterViewInit() {
    this.chart = this.mainChartInit();
    this.cdr.detectChanges();
  }

  mainChartInit() {
    return new Chart('main', {
      type: 'doughnut',
      data: {
        labels: this.markerWithChart.labels,
        datasets: [{
          data: this.markerWithChart.data,
          backgroundColor: this.markerWithChart.colours
        }]
      },
      options: {
        responsive: true
      }
    });
  }

  getByTestID(testID) {
    return this.testServ.getByTestID(testID)
      .subscribe(test => {
          this.test = test;
        }
      );
  }

  getMarkersData(testID) {
    return this.markServ.getMarkersData(testID)
      .subscribe(markers => {
          this.markerWithChart = markers;
          this.markerDetail = markers.markers[0];
        }
      );
  }

}
