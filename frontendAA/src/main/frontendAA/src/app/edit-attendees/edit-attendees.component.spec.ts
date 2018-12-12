import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditAttendeesComponent } from './edit-attendees.component';

describe('EditAttendeesComponent', () => {
  let component: EditAttendeesComponent;
  let fixture: ComponentFixture<EditAttendeesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditAttendeesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditAttendeesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
