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
import {DelegateMarkingComponent} from './delegate-marking/delegate-marking.component';
import {MarkTestComponent} from './mark-test/mark-test.component';
import {ReviewMarkingComponent} from './review-marking/review-marking.component';
import {AddModuleComponent} from './add-module/add-module.component';
import {AdminAreaComponent} from './admin-area/admin-area.component';
import {BasicPermission} from './permissions/basic.permission';
import {FormsModule} from "@angular/forms";
import {AuthorizationService} from "./services/authorization.service";
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {QuestionDetailComponent} from './question-detail/question-detail.component';
import {AddQuestionComponent} from './add-question/add-question.component';
import {OldQuestionComponent} from './old-question/old-question.component';
import {Ng2FileSizeModule} from "ng2-file-size";
import {DelegateDetailComponent} from './delegate-detail/delegate-detail.component';
import {ChartsModule} from "ng2-charts";
import {ChangePasswordComponent} from './change-password/change-password.component';
import {PerformanceComponent} from './performance/performance.component';
import {FeedbackComponent} from './feedback/feedback.component';
import {BecomeTutorComponent} from './become-tutor/become-tutor.component';
import {RequestResetPasswordComponent} from './request-reset-password/request-reset-password.component';
import {ResetPasswordComponent} from './reset-password/reset-password.component';
import {CreateProfileComponent} from './create-profile/create-profile.component';
import {EditProfileComponent} from './edit-profile/edit-profile.component';
import {AdminListComponent} from './admin-list/admin-list.component';
import {KatexModule} from "ng-katex";


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
    DelegateMarkingComponent,
    MarkTestComponent,
    ReviewMarkingComponent,
    AddModuleComponent,
    AdminAreaComponent,
    QuestionDetailComponent,
    AddQuestionComponent,
    OldQuestionComponent,
    DelegateDetailComponent,
    ChangePasswordComponent,
    PerformanceComponent,
    FeedbackComponent,
    BecomeTutorComponent,
    RequestResetPasswordComponent,
    ResetPasswordComponent,
    CreateProfileComponent,
    EditProfileComponent,
    AdminListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ChartsModule,
    HttpClientModule,
    FormsModule,
    NgbModule,
    Ng2FileSizeModule,
    KatexModule
  ],
  providers: [AuthorizationService, BasicPermission, AppComponent],
  bootstrap: [AppComponent],
})
export class AppModule {
}
