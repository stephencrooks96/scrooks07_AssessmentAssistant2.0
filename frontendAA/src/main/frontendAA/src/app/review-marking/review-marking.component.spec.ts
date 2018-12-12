import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewMarkingComponent } from './review-marking.component';

describe('ReviewMarkingComponent', () => {
  let component: ReviewMarkingComponent;
  let fixture: ComponentFixture<ReviewMarkingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewMarkingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewMarkingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
