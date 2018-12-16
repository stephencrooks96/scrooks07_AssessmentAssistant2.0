import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from "@angular/common/http";
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './login/login.component';
import {MyModulesComponent} from './my-modules/my-modules.component';
import {HeaderComponent} from './header/header.component';
import {AddTestComponent} from './add-test/add-test.component';
import {EditTestComponent} from './edit-test/edit-test.component';
import {ModuleHomeComponent} from './module-home/module-home.component';
import {TakeTestComponent} from './take-test/take-test.component';
import {ViewResultsComponent} from './view-results/view-results.component';
import {ViewFeedbackComponent} from './view-feedback/view-feedback.component';
import {DelegateMarkingComponent} from './delegate-marking/delegate-marking.component';
import {MarkTestComponent} from './mark-test/mark-test.component';
import {ReviewMarkingComponent} from './review-marking/review-marking.component';
import {AddModuleComponent} from './add-module/add-module.component';
import {EditAttendeesComponent} from './edit-attendees/edit-attendees.component';
import {AdminAreaComponent} from './admin-area/admin-area.component';
import {PermissionsPermission} from './permissions/permissions.permission';
import {FormsModule} from "@angular/forms";
import {AuthorizationService} from "./services/authorization.service";
import {Ng2GoogleChartsModule} from 'ng2-google-charts';
import {ViewProgressComponent} from './view-progress/view-progress.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { QuestionDetailComponent } from './question-detail/question-detail.component';
import { AddQuestionComponent } from './add-question/add-question.component';
import { OldQuestionComponent } from './old-question/old-question.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MyModulesComponent,
    HeaderComponent,
    AddTestComponent,
    EditTestComponent,
    ModuleHomeComponent,
    TakeTestComponent,
    ViewResultsComponent,
    ViewFeedbackComponent,
    DelegateMarkingComponent,
    MarkTestComponent,
    ReviewMarkingComponent,
    AddModuleComponent,
    EditAttendeesComponent,
    AdminAreaComponent,
    ViewProgressComponent,
    QuestionDetailComponent,
    AddQuestionComponent,
    OldQuestionComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    Ng2GoogleChartsModule,
    HttpClientModule,
    FormsModule,
    NgbModule
  ],
  providers: [AuthorizationService, PermissionsPermission, AppComponent],
  bootstrap: [AppComponent],
})
export class AppModule {
}
