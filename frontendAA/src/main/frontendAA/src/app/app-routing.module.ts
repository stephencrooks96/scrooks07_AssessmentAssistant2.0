import {RouterModule, Routes} from '@angular/router';
import {MyModulesComponent} from "./my-modules/my-modules.component";
import {PermissionsPermission} from "./permissions/permissions.permission";
import {LoginComponent} from "./login/login.component";
import {NgModule} from "@angular/core";
import {ModuleHomeComponent} from "./module-home/module-home.component";
import {AddTestComponent} from "./add-test/add-test.component";
import {ViewFeedbackComponent} from "./view-feedback/view-feedback.component";
import {MarkTestComponent} from "./mark-test/mark-test.component";
import {ViewProgressComponent} from "./view-progress/view-progress.component";
import {DelegateMarkingComponent} from "./delegate-marking/delegate-marking.component";
import {EditTestComponent} from "./edit-test/edit-test.component";
import {ReviewMarkingComponent} from "./review-marking/review-marking.component";
import {TakeTestComponent} from "./take-test/take-test.component";

const routes: Routes = [
  {path: 'myModules', component: MyModulesComponent, canActivate: [PermissionsPermission]},
  {path: 'moduleHome/:moduleID', component: ModuleHomeComponent, canActivate: [PermissionsPermission]},
  {path: 'login', component: LoginComponent},
  {path: 'testFeedback/:testID', component: ViewFeedbackComponent, canActivate: [PermissionsPermission]},
  {path: 'viewProgress/:testID', component: ViewProgressComponent, canActivate: [PermissionsPermission]},
  {path: 'takeTest/:testID', component: TakeTestComponent, canActivate: [PermissionsPermission]},
  {path: 'mark/:testID', component: MarkTestComponent, canActivate: [PermissionsPermission]},
  {path: 'delegateMarking/:testID', component: DelegateMarkingComponent, canActivate: [PermissionsPermission]},
  {path: 'addTest/:moduleID', component: AddTestComponent, canActivate: [PermissionsPermission]},
  {path: 'editTest/:testID', component: EditTestComponent, canActivate: [PermissionsPermission]},
  {path: 'reviewMarking/:testID', component: ReviewMarkingComponent, canActivate: [PermissionsPermission]},

  {path: '**', redirectTo: '/myModules'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

