import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DelegateMarkingComponent } from './delegate-marking.component';

describe('DelegateMarkingComponent', () => {
  let component: DelegateMarkingComponent;
  let fixture: ComponentFixture<DelegateMarkingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DelegateMarkingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DelegateMarkingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
