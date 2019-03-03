import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DelegateDetailComponent } from './delegate-detail.component';

describe('DelegateDetailComponent', () => {
  let component: DelegateDetailComponent;
  let fixture: ComponentFixture<DelegateDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DelegateDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DelegateDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
