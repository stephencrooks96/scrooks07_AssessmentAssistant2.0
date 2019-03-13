import {RouterModule, Routes} from '@angular/router';
import {MyModulesComponent} from "./my-modules/my-modules.component";
import {BasicPermission} from "./permissions/basic.permission";
import {LoginComponent} from "./login/login.component";
import {NgModule} from "@angular/core";
import {ModuleHomeComponent} from "./module-home/module-home.component";
import {AddTestComponent} from "./add-test/add-test.component";
import {MarkTestComponent} from "./mark-test/mark-test.component";
import {DelegateMarkingComponent} from "./delegate-marking/delegate-marking.component";
import {EditTestComponent} from "./edit-test/edit-test.component";
import {ReviewMarkingComponent} from "./review-marking/review-marking.component";
import {TakeTestComponent} from "./take-test/take-test.component";
import {ChangePasswordComponent} from "./change-password/change-password.component";
import {PerformanceComponent} from "./performance/performance.component";
import {FeedbackComponent} from "./feedback/feedback.component";
import {AddModuleComponent} from "./add-module/add-module.component";
import {BecomeTutorComponent} from "./become-tutor/become-tutor.component";
import {RequestResetPasswordComponent} from "./request-reset-password/request-reset-password.component";
import {ResetPasswordComponent} from "./reset-password/reset-password.component";
import {AdminAreaComponent} from "./admin-area/admin-area.component";
import {CreateProfileComponent} from "./create-profile/create-profile.component";
import {EditProfileComponent} from "./edit-profile/edit-profile.component";
import {AdminListComponent} from "./admin-list/admin-list.component";

const routes: Routes = [
  {path: 'myModules', component: MyModulesComponent, canActivate: [BasicPermission]},
  {path: 'moduleHome/:moduleID', component: ModuleHomeComponent, canActivate: [BasicPermission]},
  {path: 'changePassword', component: ChangePasswordComponent, canActivate: [BasicPermission]},
  {path: 'login', component: LoginComponent},
  {path: 'requestResetPassword', component: RequestResetPasswordComponent},
  {path: 'takeTest/:testID', component: TakeTestComponent, canActivate: [BasicPermission]},
  {path: 'mark/:testID', component: MarkTestComponent, canActivate: [BasicPermission]},
  {path: 'feedback/:testID', component: FeedbackComponent, canActivate: [BasicPermission]},
  {path: 'performance/:testID', component: PerformanceComponent, canActivate: [BasicPermission]},
  {path: 'delegateMarking/:testID', component: DelegateMarkingComponent, canActivate: [BasicPermission]},
  {path: 'addTest/:moduleID', component: AddTestComponent, canActivate: [BasicPermission]},
  {path: 'addModule', component: AddModuleComponent, canActivate: [BasicPermission]},
  {path: 'becomeTutor', component: BecomeTutorComponent, canActivate: [BasicPermission]},
  {path: 'editTest/:testID', component: EditTestComponent, canActivate: [BasicPermission]},
  {path: 'adminArea', component: AdminAreaComponent, canActivate: [BasicPermission]},
  {path: 'resetPassword/:resetString', component: ResetPasswordComponent},
  {path: 'createProfile', component: CreateProfileComponent},
  {path: 'editProfile', component: EditProfileComponent, canActivate: [BasicPermission]},
  {path: 'adminList', component: AdminListComponent, canActivate: [BasicPermission]},
  {path: 'reviewMarking/:testID', component: ReviewMarkingComponent, canActivate: [BasicPermission]},
  {path: '**', redirectTo: 'myModules'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

