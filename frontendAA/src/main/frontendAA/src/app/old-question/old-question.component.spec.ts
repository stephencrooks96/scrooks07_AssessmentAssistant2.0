import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OldQuestionComponent } from './old-question.component';

describe('OldQuestionComponent', () => {
  let component: OldQuestionComponent;
  let fixture: ComponentFixture<OldQuestionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OldQuestionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OldQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
