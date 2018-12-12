import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarkTestComponent } from './mark-test.component';

describe('MarkTestComponent', () => {
  let component: MarkTestComponent;
  let fixture: ComponentFixture<MarkTestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarkTestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarkTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
